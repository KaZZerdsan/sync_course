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
import ru.sfedu.Sync.utils.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CSVDataProvider implements IDataProvider {

    private CSVReader reader;
    private CSVWriter writer;

    private final String PATH = ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_CSV);

    private final String FILE_EXTENSION = ConfigurationUtil.getConfigurationEntry(Constants.CSV_FILE_EXTENSION);

    private final Logger log = LogManager.getLogger(CSVDataProvider.class);

    public CSVDataProvider() throws IOException {
    }



    @Override
    public Result<Admin> createAdmin(List<Admin> list, boolean append) throws Exception {
        return insertRecord(list, append, Admin.class);
    }

    @Override
    public Result<Manager> createManager(List<Manager> list, boolean append) throws Exception {
        return insertRecord(list, append, Manager.class);
    }

    @Override
    public Result<Speaker> createSpeaker(List<Speaker> list, boolean append) throws Exception {
        return insertRecord(list, append, Speaker.class);
    }

    @Override
    public Result<Event> createEvent(List<Event> list, boolean append) throws Exception {
        return insertRecord(list, append, Event.class);
    }

    @Override
    public Result<Zone> createZone(List<Zone> list, boolean append) throws Exception {
        return insertRecord(list, append, Zone.class);
    }

    @Override
    public Result<Channel> createChannel(List<Channel> list, boolean append) throws Exception {
        return insertRecord(list, append, Channel.class);
    }

    @Override
    public Result<Admin> getAdmins() throws Exception {
        return getRecords(Admin.class);
    }

    @Override
    public Result<Manager> getManagers() throws Exception {
        return getRecords(Manager.class);
    }

    @Override
    public Result<Speaker> getSpeakers() throws Exception {
        return getRecords(Speaker.class);
    }

    @Override
    public Result<Channel> getChannels() throws Exception {
        return getRecords(Channel.class);
    }

    @Override
    public Result<Zone> getZones() throws Exception {
        return getRecords(Zone.class);
    }

    @Override
    public Result<Event> getEvents() throws Exception {
        return getRecords(Event.class);
    }

    @Override
    public Result<Admin> getAdminById(Long id) throws Exception {
        return getRecordById(id, Admin.class);
    }

    @Override
    public Result<Manager> getManagerById(Long id) throws Exception {
        return getRecordById(id, Manager.class);
    }

    @Override
    public Result<Speaker> getSpeakerById(Long id) throws Exception {
        return getRecordById(id, Speaker.class);
    }

    @Override
    public Result<Channel> getChannelById(Long id) throws Exception {
        return getRecordById(id, Channel.class);
    }

    @Override
    public Result<Zone> getZoneById(Long id) throws Exception {
        return getRecordById(id, Zone.class);
    }

    @Override
    public Result<Event> getEventById(Long id) throws Exception {
        return getRecordById(id, Event.class);
    }

    @Override
    public Result<Admin> deleteAdmin(Long id) throws Exception {
        return deleteRecord(id, Admin.class);
    }

    @Override
    public Result<Manager> deleteManager(Long id) throws Exception {
        return deleteRecord(id, Manager.class);
    }

    @Override
    public Result<Speaker> deleteSpeaker(Long id) throws Exception {
        return deleteRecord(id, Speaker.class);
    }

    @Override
    public Result<Channel> deleteChannel(Long id) throws Exception {
        return deleteRecord(id, Channel.class);
    }

    @Override
    public Result<Zone> deleteZone(Long id) throws Exception {
        return deleteRecord(id, Zone.class);
    }

    @Override
    public Result<Event> deleteEvent(Long id) throws Exception {
        return deleteRecord(id, Event.class);
    }

    @Override
    public Result<Admin> updateAdmin(Long id, Admin admin) throws Exception {
        return updateRecord(id, admin);
    }

    @Override
    public Result<Manager> updateManager(Long id, Manager manager) throws Exception {
        return updateRecord(id, manager);
    }

    @Override
    public Result<Speaker> updateSpeaker(Long id, Speaker speaker) throws Exception {
        return updateRecord(id, speaker);
    }

    @Override
    public Result<Channel> updateChannel(Long id, Channel channel) throws Exception {
        return updateRecord(id, channel);
    }

    @Override
    public Result<Zone> updateZone(Long id, Zone zone) throws Exception {
        return updateRecord(id, zone);
    }

    @Override
    public Result<Event> updateEvent(Long id, Event event) throws Exception {
        return updateRecord(id, event);
    }

    /**
     * return path to csv file
     * @param cn used for filename
     * @return path to file
     */
    public String getPath (Class<?> cn) {
        return PATH + cn.getSimpleName().toLowerCase() + FILE_EXTENSION;
    }

    /**
     * Creates DataSource if not exist
     * @param path to file
     * @throws IOException if file can't be created
     */
    public void initDataSource(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            Path dirPath = Paths.get(PATH);
            Files.createDirectories(dirPath);
            file.createNewFile();
        }
    }

    @Override
    public <T> Result<T> insertRecord(List<T> listRecord, boolean append, Class<T> cn) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (append) {
            Result<T> res = get(cn);
            List<T> oldRecords = res.getData();
            if (res.getResultType() == ResultType.ERROR) {
                oldRecords = new ArrayList<>();
            }
            if (AdditionalMethods.hasDuplicates(listRecord, oldRecords)) {
                return new Result<T>(null, ResultType.ERROR, Constants.ALREADY_EXIST);
            }
            listRecord = Stream
                    .concat(oldRecords.stream(), listRecord.stream())
                    .collect(Collectors.toList());
        }
        add(listRecord, cn);
        return new Result<T>(listRecord, ResultType.OK, Constants.INSERTED_SUCCESSFULLY);
    }

    @Override
    public <T> Result<T> getRecords (Class cn) throws IOException {
        Result<T> res = get(cn);
        if (res.getResultType() == ResultType.ERROR) {
            return res;
        } else {
            List<T> listRecords = res.getData();
            if (listRecords.size() == 0) {
                return new Result<>(null, ResultType.ERROR, Constants.LIST_EMPTY);
            }
            return new Result<>(listRecords, ResultType.OK, Constants.RECORDS_FOUND);
        }
    }

    @Override
    public <T> Result<T> getRecordById(Long id, Class<T> cn) throws IOException {
        Result<T> res = get(cn);
        if (res.getResultType() == ResultType.ERROR) {
            return res;
        }
        return AdditionalMethods.getById(res.getData(), id, cn);
    }

    @Override
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

    /**
     * Removes nested record from another DataSource
     * @param record parent record
     * @param cn parent class
     * @param <T> Generic Type (any Class or type)
     * @throws CsvRequiredFieldEmptyException
     * @throws IOException
     * @throws CsvDataTypeMismatchException
     * @throws NoSuchMethodException
     */
    private <T> void removeNestedRecords(T record, Class<?> cn) throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException, NoSuchMethodException {
        if (cn == Event.class) {
            Event event = (Event) record;
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

    @Override
    public <T> Result<T> updateRecord(Long id, T record) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Class<T> cn = (Class<T>) record.getClass();
        Result<T> res = deleteRecord(id, cn);
        if (res.getResultType() == ResultType.ERROR) {
            return res;
        }
        List<T> listRecord = res.getData();
        listRecord.add(record);
        return insertRecord(listRecord, false, cn);
    }

    @Override
    public Result<Zone> changeZoneStatus(Long id, boolean status) throws IOException, NoSuchMethodException, InvocationTargetException, CsvRequiredFieldEmptyException, InstantiationException, IllegalAccessException, CsvDataTypeMismatchException {
        Result<Zone> res = getRecordById(id, Zone.class);
        if (res.getResultType() == ResultType.ERROR) {
            return res;
        }
        Zone zone = res.getData().get(0);
        zone.setStatus(status);
        updateRecord(id, zone);
        List<Channel> channelList = zone.getChannelList();
        for (Channel channel : channelList) {
            channel.setStatus(status);
            updateRecord(channel.getId(), channel);
        }
        return new Result<>(null, ResultType.OK, Constants.STATUS_CHANGED);
    }

    /**
     * Generic method which only inserting records
     * @param list list to write
     * @param cn ClassName which related to table name
     * @param <T> Generic Type (any Class or type)
     * @return structure with status, data(if not error) and message
     * @throws SQLException if file already in use
     */
    private <T> Result<T> add(List<T> list, Class<?> cn) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        initWriter(cn);
        StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(writer)
                .withApplyQuotesToAll(false)
                .build();
        beanToCsv.write(list);
        close();
        return new Result<>(list, ResultType.OK, Constants.INSERTED_SUCCESSFULLY);
    }

    /**
     * Generic method which only read from DataSource
     * @param cn ClassName which related to table name
     * @param <T> Generic Type (any Class or type)
     * @return structure with status, data(if not error) and message
     * @throws IOException in case if file already in use or it's doesn't exist
     */
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
            log.error(e);
            return new Result<T>(null, ResultType.ERROR, Constants.BAD_FILE);
        }
        finally {
            close();
        }
    }

    /**
     * Remove data from file
     * @param cn file name
     * @throws IOException in case if file already in use
     */
    public void flushFile(Class<?> cn) throws IOException {
        log.info(Constants.BAD_FILE);
        log.info(Constants.FLUSH_FILE);
        FileWriter file = new FileWriter(getPath(cn));
        file.flush();
    }

    /**
     * CSVReader instance init
     * @param cn records class
     * @throws IOException in case if file already in use
     */
    private void initReader(Class<?> cn) throws IOException {
        String path = getPath(cn);
        initDataSource(path);
        this.reader = new CSVReader(new FileReader(path));
    }

    /**
     * CSVWriter instance init
     * @param cn records class
     * @throws IOException in case if file already in use
     */
    private void initWriter(Class<?> cn) throws IOException {
        String path = getPath(cn);
        initDataSource(path);
        this.writer = new CSVWriter(new FileWriter(path, false));
    }

    /**
     * Close all file threads
     */
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
            log.error(e);
            log.debug(Constants.STREAM_IS_CLOSED);
        }
    }
}
