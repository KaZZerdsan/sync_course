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
import ru.sfedu.Sync.models.*;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CSVDataProvider implements IDataProvider {

    private CSVReader reader;
    private CSVWriter writer;

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

    public <T extends BaseClass> Result<T> insertRecord(List<T> listRecord, boolean append, Class cn) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        if (append) {
            Result<T> res = get(cn);
            List<T> oldRecords = res.getData();
            if (res.getResultType() == ResultType.ERROR) {
                oldRecords = new ArrayList<T>();
            }
            if (hasDuplicates(listRecord, oldRecords)) {
                return new Result<>(null, ResultType.ERROR, Constants.ALREADY_EXIST);
            }
            listRecord = Stream
                    .concat(oldRecords.stream(), listRecord.stream())
                    .collect(Collectors.toList());
        }
        add(listRecord, cn);
        return new Result<>(listRecord, ResultType.OK, Constants.INSERTED_SUCCESSFULLY);
    }

    public <T extends BaseClass> Result<T> getRecords (Class cn) throws IOException {
        Result<T> res = get(cn);
        if (res.getResultType() == ResultType.ERROR) {
            return res;
        } else {
            List<T> listRecords = res.getData();
            if (listRecords.size() == 0) {
                return new Result<>(null, ResultType.ERROR, Constants.LIST_EMPTY);
            }
            listRecords = removeEmptyInstances(listRecords);
            return new Result<>(listRecords, ResultType.OK, Constants.RECORDS_FOUND);
        }
    }

    public <T extends BaseClass> Result<T> getRecordById(int id, Class cn) throws IOException {
        Result<T> res = get(cn);
        if (res.getResultType() == ResultType.ERROR) {
            return res;
        }
        List<T> listRecord = res.getData();
        listRecord = listRecord
                .stream()
                .filter(el -> el.getId() == id)
                .collect(Collectors.toList());
        if (listRecord.size() == 0) {
            return new Result<T>(null, ResultType.ERROR, Constants.NOT_FOUND);
        }
        return new Result<T>(listRecord, ResultType.OK, Constants.FOUND_ELEMENT);
    }

    public <T extends BaseClass> Result<T> deleteRecord(int id, Class cn) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        Result<T> res;
        res = getRecordById(id, cn);
        if (res.getResultType() == ResultType.ERROR) {
            return res;
        }
        T recordToDelete = res.getData().get(0);
        removeNestedRecords(recordToDelete, cn);
        res = get(cn);
        if (res.getResultType() == ResultType.ERROR) {
            return res;
        }
        List<T> listRecord =  res.getData();
        listRecord = listRecord
                .stream()
                .filter(el -> el.getId() != id)
                .collect(Collectors.toList());
        res = add(listRecord, cn);
        if (res.getResultType() == ResultType.ERROR) {
            return res;
        }
        return new Result<>(listRecord, ResultType.OK, Constants.DELETED_SUCCESSFULLY);
    }

    private <T extends BaseClass> void removeNestedRecords(T record, Class cn) throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        if (cn == Event.class) {
            Event event = (Event) record;
            deleteRecord(event.getManager().getId(), Manager.class);
            List<Zone> zoneList = event.getZoneList();
            for (Zone zone : zoneList) {
                deleteRecord(zone.getId(), zone.getClass());
            }
        }
        if (cn == Zone.class) {
            Zone zone = (Zone) record;
            List<Channel> channelList = zone.getChannelList();
            for (Channel channel : channelList) {
                deleteRecord(channel.getId(), channel.getClass());
            }
        }
        if (cn == Channel.class) {
            Channel channel = (Channel) record;
            List<Speaker> speakerList = channel.getSpeakers();
            for (Speaker speaker : speakerList) {
                deleteRecord(speaker.getId(), speaker.getClass());
            }
        }
    }

    public <T extends BaseClass> Result<T> updateRecord(T record) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Result<T> res = deleteRecord(record.getId(), record.getClass());
        if (res.getResultType() == ResultType.ERROR) {
            return res;
        }
        List<T> listRecord = res.getData();
        listRecord.add(record);
        if (record.getClass() == Channel.class) {
        }
        listRecord = sortById(listRecord);
        if (record.getClass() == Channel.class) {
        }
        return insertRecord(listRecord, false, record.getClass());
    }

    public Result<Zone> changeZoneStatus(int id, boolean status) throws IOException, NoSuchMethodException, InvocationTargetException, CsvRequiredFieldEmptyException, InstantiationException, IllegalAccessException, CsvDataTypeMismatchException {
        Result res = getRecordById(id, Zone.class);
        if (res.getResultType() == ResultType.ERROR) {
            return res;
        }
        Zone zone = (Zone) res.getData().get(0);
        zone.setStatus(status);
        updateRecord(zone);
        List<Channel> channelList = zone.getChannelList();
        for (Channel channel : channelList) {
            channel.setStatus(status);
            updateRecord(channel);
        }
        return new Result<>(null, ResultType.OK, Constants.STATUS_CHANGED);
    }

    private <T extends BaseClass> Boolean hasDuplicates(List<T> newRecords, List<T> oldRecords) {
        List<Integer> newIds = newRecords
                .stream()
                .map(BaseClass::getId)
                .collect(Collectors.toList());
        return oldRecords
                .stream()
                .anyMatch(el -> newIds.contains(el.getId()));
    }

    private <T extends BaseClass> List<T> removeEmptyInstances(List<T> listRecords) {
        //IF OPENCSV PARSER MEETS EMPTY LINE HE WILL READ IT AS EMPTY INSTANCE,
        //WITH INT=0 AND EMPTY STRING SO WE SHOULD FILTER THE RESULTS
        return listRecords
                        .stream()
                        .filter(el -> el.getId() != 0)
                        .collect(Collectors.toList());
    }

    private <T extends BaseClass> List<T> sortById(List<T> list) {
        return list.stream().sorted(Comparator.comparing(T::getId)).collect(Collectors.toList());
    }

    private <T extends BaseClass> Result<T> add(List<T> list, Class cn) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        initWriter(cn);
        StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(writer)
                .withApplyQuotesToAll(false)
                .build();
        beanToCsv.write(list);
        close();
        return new Result<>(list, ResultType.OK, Constants.INSERTED_SUCCESSFULLY);
    }

    private <T> Result<T> get(Class cn) throws IOException, RuntimeException {
        try {
            initReader(cn);
            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                    .withType(cn)
                    .build();
            List<T> list = csvToBean.parse();
            return new Result<>(list, ResultType.OK, Constants.RECORDS_FOUND);
        }
        catch (RuntimeException e) {
            flushFile(cn);
            return new Result<>(null, ResultType.ERROR, Constants.BAD_FILE);
        }
        finally {
            close();
        }
    }

    private void flushFile(Class cn) throws IOException {
        log.debug(Constants.FLUSH_FILE);
        FileWriter file = new FileWriter(getPath(cn));
        file.flush();
    }

    private void initReader(Class cn) throws IOException {
        String path = getPath(cn);
        initDataSource(path);
        this.reader = new CSVReader(new FileReader(path));
    }

    private void initWriter(Class cn) throws IOException {
        String path = getPath(cn);
        initDataSource(path);
        this.writer = new CSVWriter(new FileWriter(path, false));
    }

    private void close() {
        try {
            if (reader != null) {
                this.reader.close();
            }
            if (writer != null) {
                this.writer.close();
            }
        }
        catch (IOException e) {
            log.debug(Constants.STREAM_IS_CLOSED);
        }
    }
}


