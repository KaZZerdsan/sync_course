package ru.sfedu.Sync.api;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.Sync.models.BaseClass;
import ru.sfedu.Sync.utils.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class JAXBDataProvider implements IDataProvider {

    private final String PATH = ConfigurationUtil.getConfigurationEntry("PATH_TO_XML");

    private final String FILE_EXTENSION = "XML_FILE_EXTENSION";

    private Logger log = LogManager.getLogger(JAXBDataProvider.class);

    public JAXBDataProvider() throws IOException {
    }

    public void initDataSource(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            Path dirPath = Paths.get(PATH);
            Files.createDirectories(dirPath);
            file.createNewFile();
        }
    }

    @Override
    public String getPath(Class cn) throws IOException {
        return PATH + cn.getSimpleName().toLowerCase() + "_jaxb" + ConfigurationUtil.getConfigurationEntry(FILE_EXTENSION);
    }

    @Override
    public <T extends BaseClass> Result<T> insertRecord(List<T> listRecord, boolean append, Class cn) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, Exception {
        try {
            String path = getPath(cn);
            initDataSource(path);
            if (append) {
                Result res = getRecords(cn);
                List<T> oldList = (List<T>) res.data;
                if (this.isDuplicate(oldList, listRecord)) {
                    return new Result<>(null, ResultType.ERROR, Constants.ALREADY_EXIST);
                }
            }
            File f = new File(path);

            XMLWrapper<T> wrapper = new XMLWrapper<T>(listRecord);
            JAXBContext context = JAXBContext.newInstance(XMLWrapper.class, cn);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(wrapper, f);
            return new Result(listRecord, ResultType.OK, Constants.INSERTED_SUCCESSFULLY);
        } catch(Exception e) {
            log.error(123);
            return null;
        }
    }

    @Override
    public <T> Result<T> getRecords(Class cn) throws Exception {
        try {
            String path = getPath(cn);
            FileReader fr = new FileReader(path);
            JAXBContext context = JAXBContext.newInstance(XMLWrapper.class, cn);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            XMLWrapper wrapper = (XMLWrapper) unmarshaller.unmarshal(fr);
            List<T> list = wrapper.getList();
            return new Result(list, ResultType.OK, Constants.RECORDS_FOUND);
        }
        catch (IOException e) {
            log.error(e);
            return new Result(new ArrayList<T>(), ResultType.ERROR, Constants.FILE_IN_USE);
        }
    }

    @Override
    public <T extends BaseClass> Result getRecordById(int id, Class cn) throws Exception {
        try {
            Result res = getRecords(cn);
            if (res.resultType == ResultType.ERROR) {
                return res;
            }
            List<T> list = (List<T>) res.data;
            T record = list.stream().filter(el -> el.getId() == id).findFirst().get();
            return new Result(record, ResultType.OK, Constants.FOUND_ELEMENT);
        }
        catch (Exception e) {
            log.error(e);
            return new Result(null, ResultType.ERROR, Constants.NOT_FOUND);
        }
    }

    @Override
    public <T extends BaseClass> Result deleteRecord(int id, Class cn) throws Exception {
        Result res = getRecords(cn);
        if (res.resultType == ResultType.ERROR) {
            return res;
        }
        List<T> list = (List<T>) res.data;
        list = list
                .stream()
                .filter(el -> el.getId() != id)
                .collect(Collectors.toList());
        return insertRecord(list, false, cn);
    }

    @Override
    public <T extends BaseClass> Result updateRecord(T record) throws Exception {
        Result res = this.deleteRecord(record.getId(), record.getClass());
        if (res.resultType == ResultType.ERROR) {
            return res;
        }
        List<T> list = (List<T>) res.data;
        list.add(record);
        list = sort(list);
        return insertRecord(list, false, record.getClass());
    }

    @Override
    public <T extends BaseClass> List<T> sort(List<T> list) {
        return list.stream().sorted(Comparator.comparing(T::getId)).collect(Collectors.toList());
    }

    public <T extends BaseClass> boolean isDuplicate (List<T> oldList, List<T> newList) throws Exception {
        List<Integer> oldIds = oldList.stream().map(el -> el.getId()).collect(Collectors.toList());
        List<Integer> newIds = newList.stream().map(el -> el.getId()).collect(Collectors.toList());
        for (Integer newId : newIds) {
            if (oldIds.contains(newId)) {
                return true;
            }
        }
        return false;
    }
}
