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
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CSVDataProvider {

    private CSVReader reader;
    private CSVWriter writer;

    private final String PATH = ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_CSV);

    private final String FILE_EXTENSION = ConfigurationUtil.getConfigurationEntry(Constants.CSV_FILE_EXTENSION);

    private final Logger log = LogManager.getLogger(CSVDataProvider.class);

    public CSVDataProvider() throws IOException {
    }


    public Result<Admin> createAdmin(List<Admin> list, boolean append) throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return insertRecord(list, append, Admin.class);
    }
    public Result<Manager> createManager(List<Manager> list, boolean append) throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return insertRecord(list, append, Manager.class);
    }
    public Result<Speaker> createSpeaker(List<Speaker> list, boolean append) throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return insertRecord(list, append, Speaker.class);
    }
    public Result<Event> createEvent(List<Event> list, boolean append) throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return insertRecord(list, append, Event.class);
    }
    public Result<Zone> createZone(List<Zone> list, boolean append) throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return insertRecord(list, append, Zone.class);
    }
    public Result<Channel> createChannel(List<Channel> list, boolean append) throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return insertRecord(list, append, Channel.class);
    }

    public String getPath (Class<?> cn) {
        return PATH + cn.getSimpleName().toLowerCase() + FILE_EXTENSION;
    }

    public void initDataSource(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            Path dirPath = Paths.get(PATH);
            Files.createDirectories(dirPath);
            file.createNewFile();
        }
    }

    public <T> Result<T> insertRecord(List<T> listRecord, boolean append, Class<T> cn) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (append) {
            Result<T> res = get(cn);
            List<T> oldRecords = res.getData();
            if (res.getResultType() == ResultType.ERROR) {
                oldRecords = new ArrayList<>();
            }
            if (hasDuplicates(listRecord, oldRecords)) {
                return new Result<T>(null, ResultType.ERROR, Constants.ALREADY_EXIST);
            }
            listRecord = Stream
                    .concat(oldRecords.stream(), listRecord.stream())
                    .collect(Collectors.toList());
        }
        add(listRecord, cn);
        return new Result<T>(listRecord, ResultType.OK, Constants.INSERTED_SUCCESSFULLY);
    }

    public <T> Result<T> getRecords (Class<T> cn) throws IOException {
        Result<T> res = get(cn);
        if (res.getResultType() == ResultType.ERROR) {
            return res;
        } else {
            List<T> listRecords = res.getData();
            if (listRecords.size() == 0) {
                return new Result<>(null, ResultType.ERROR, Constants.LIST_EMPTY);
            }
//            listRecords = removeEmptyInstances(listRecords);
            return new Result<>(listRecords, ResultType.OK, Constants.RECORDS_FOUND);
        }
    }

    public <T> Result<T> getRecordById(Long id, Class<T> cn) throws IOException {
        Result<T> res = get(cn);
        if (res.getResultType() == ResultType.ERROR) {
            return res;
        }
        List<T> listRecord = res.getData();
        try {
            Method getter = cn.getMethod(Constants.METHOD_GET_ID);
            for (T element : listRecord) {
                Long recordId = (Long) getter.invoke(element);
                if (recordId.equals(id)) {
                    listRecord.add(element);
                    return new Result<>(Collections.singletonList(element), ResultType.OK, Constants.FOUND_ELEMENT);
                }
            }
            return new Result<>(null, ResultType.ERROR, Constants.NOT_FOUND);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error(e);
            return new Result<>(null, ResultType.ERROR, Constants.NO_METHOD);
        }
    }

        public <T> Result<T> deleteRecord(Long id, Class<T> cn) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, NoSuchMethodException {
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
        listRecord.remove(recordToDelete);
        res = add(listRecord, cn);
        if (res.getResultType() == ResultType.ERROR) {
            return res;
        }
        return new Result<>(listRecord, ResultType.OK, Constants.DELETED_SUCCESSFULLY);
    }

    private <T> void removeNestedRecords(T record, Class<?> cn) throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException, NoSuchMethodException {
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

    public <T> Result<T> updateRecord(T record) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Class<T> cn = (Class<T>) record.getClass();
        Result<T> res = deleteRecord(12L, cn);
        if (res.getResultType() == ResultType.ERROR) {
            return res;
        }
        List<T> listRecord = res.getData();
        listRecord.add(record);
        return insertRecord(listRecord, false, cn);
    }

    public Result<Zone> changeZoneStatus(Long id, boolean status) throws IOException, NoSuchMethodException, InvocationTargetException, CsvRequiredFieldEmptyException, InstantiationException, IllegalAccessException, CsvDataTypeMismatchException {
        Result<Zone> res = getRecordById(id, Zone.class);
        if (res.getResultType() == ResultType.ERROR) {
            return res;
        }
        Zone zone = res.getData().get(0);
        zone.setStatus(status);
        updateRecord(zone);
        List<Channel> channelList = zone.getChannelList();
        for (Channel channel : channelList) {
            channel.setStatus(status);
            updateRecord(channel);
        }
        return new Result<>(null, ResultType.OK, Constants.STATUS_CHANGED);
    }

    private <T> Boolean hasDuplicates(List<T> newRecords, List<T> oldRecords) throws IllegalAccessException {
        Class<?> cn = newRecords.get(0).getClass();
        try {
            Method idGetter = cn.getMethod(Constants.METHOD_GET_ID);
            List<Long> newIds = new ArrayList<>();
            for (T el: newRecords) {
                newIds.add((Long) idGetter.invoke(el));
            }
            List<Long> oldIds = new ArrayList<>();
            for (T el: oldRecords) {
                oldIds.add((Long) idGetter.invoke(el));
            }
            return oldIds
                    .stream()
                    .anyMatch(newIds::contains);
        }
        catch (NoSuchMethodException | InvocationTargetException e) {
            log.error(e);
            log.info(Constants.NO_METHOD);
            return false;
        }
    }

    private <T> Result<T> add(List<T> list, Class<?> cn) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        initWriter(cn);
        StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(writer)
                .withApplyQuotesToAll(false)
                .build();
        beanToCsv.write(list);
        close();
        return new Result<>(list, ResultType.OK, Constants.INSERTED_SUCCESSFULLY);
    }

    private <T> Result<T> get(Class<T> cn) throws IOException, RuntimeException {
        try {
            initReader(cn);
            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                    .withType(cn)
                    .build();
            List<T> list = csvToBean.parse();
            return new Result<T>(list, ResultType.OK, Constants.RECORDS_FOUND);
        }
        catch (RuntimeException e) {
            flushFile(cn);
            return new Result<T>(null, ResultType.ERROR, Constants.BAD_FILE);
        }
        finally {
            close();
        }
    }

    public void flushFile(Class<?> cn) throws IOException {
        log.debug(Constants.FLUSH_FILE);
        FileWriter file = new FileWriter(getPath(cn));
        file.flush();
    }

    private void initReader(Class<?> cn) throws IOException {
        String path = getPath(cn);
        initDataSource(path);
        this.reader = new CSVReader(new FileReader(path));
    }

    private void initWriter(Class<?> cn) throws IOException {
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
