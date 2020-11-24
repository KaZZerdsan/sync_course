package ru.sfedu.Sync.api;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.Sync.models.BaseClass;
import ru.sfedu.Sync.utils.ConfigurationUtil;
import ru.sfedu.Sync.utils.Constants;
import ru.sfedu.Sync.utils.Result;
import ru.sfedu.Sync.utils.ResultType;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CSVDataProvider implements IDataProvider {

    private final String PATH = ConfigurationUtil.getConfigurationEntry("PATH_TO_CSV");

    private final String FILE_EXTENSION = "CSV_FILE_EXTENSION";

    private Logger log = LogManager.getLogger(CSVDataProvider.class);

    public CSVDataProvider() throws IOException {
    }

    public String getPath (Class cn) throws IOException {
        return PATH + cn.getSimpleName().toLowerCase() + ConfigurationUtil.getConfigurationEntry(FILE_EXTENSION);
    }

    public void initDataSource(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            Path dirPath = Paths.get(PATH);
            Files.createDirectories(dirPath);
            file.createNewFile();
        }
    }

    public <T extends BaseClass> Result<T> insertRecord(List <T> listRecord, boolean append, Class cn) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        try {
            String path = getPath(cn);
            initDataSource(path);
            List<T> oldListRecord = (List<T>) this.getRecords(cn).data;
            if (append) {
                if (oldListRecord != null && oldListRecord.size() > 0) {
                    int id = listRecord.get(0).getId();
                    if (oldListRecord.stream().anyMatch(el -> el.getId() == id)) {
                        return new Result<>(null, ResultType.ERROR, Constants.ALREADY_EXIST);
                    }
                    listRecord = Stream
                            .concat(oldListRecord.stream(), listRecord.stream())
                            .collect(Collectors.toList());
                }
            }
            FileWriter fw = new FileWriter(path, false);
            CSVWriter writer = new CSVWriter(fw);
            StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(writer)
                    .withApplyQuotesToAll(false)
                    .build();
            beanToCsv.write(listRecord);
            writer.close();
        }
        catch(IndexOutOfBoundsException e) {
            return new Result(null, ResultType.ERROR, Constants.LIST_EMPTY);
        }
        return new Result(null, ResultType.OK, Constants.INSERTED_SUCCESSFULLY);
    }

    public <T> Result getRecords (Class cn) throws IOException {
        String path = getPath(cn);
        File f = new File(path);
        if (!f.exists()) {
            return new Result<>(null, ResultType.ERROR, Constants.FILE_DOES_NOT_EXIST);
        }
        FileReader file = new FileReader(path);
        CSVReader reader = new CSVReader(file);
        CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                .withType(cn)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        List<T> listRecords = csvToBean.parse();
        reader.close();
        if (listRecords.size() == 0) {
            return new Result<>(null, ResultType.ERROR, Constants.LIST_EMPTY);
        }
        return new Result<>((T) listRecords, ResultType.OK, Constants.RECORDS_FOUND);
    }

    public <T extends BaseClass> Result getRecordById(int id, Class cn) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Result res = this.getRecords(cn);
        if (res.resultType == ResultType.ERROR) {
            return res;
        }
        List<T> listRecord = (List<T>) res.data;
        try {
            T record = listRecord.stream().filter(el -> el.getId() == id).findFirst().get();
            return new Result(record, ResultType.OK, Constants.FOUND_ELEMENT);
        }
        catch (NoSuchElementException e) {
            return new Result(null, ResultType.ERROR, Constants.NOT_FOUND);
        }
    }

    public <T extends BaseClass> Result deleteRecord(int id, Class cn) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Result res = this.getRecordById(id, cn);
        if (res.resultType == ResultType.ERROR) {
            return res;
        }
        res = this.getRecords(cn);
        List<T> listRecord = (List<T>) res.data;
        listRecord = listRecord
                .stream()
                .filter(el -> el.getId() != id)
                .collect(Collectors.toList());
        res = insertRecord(listRecord, false, cn);
        if (res.resultType == ResultType.ERROR) {
            return res;
        }
        return new Result(listRecord, ResultType.OK, Constants.DELETED_SUCCESSFULLY);
    }

    public <T extends BaseClass> Result updateRecord(T record) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Result res = deleteRecord(record.getId(), record.getClass());
        if (res.resultType == ResultType.ERROR) {
            return res;
        }
        List<T> listRecord = (List<T>) res.data;
        listRecord.add(record);
        listRecord = sort(listRecord);
        return insertRecord(listRecord, false, record.getClass());
    }

    public <T extends BaseClass> List<T> sort(List<T> list) {
        return list.stream().sorted(Comparator.comparing(T::getId)).collect(Collectors.toList());
    }
}


