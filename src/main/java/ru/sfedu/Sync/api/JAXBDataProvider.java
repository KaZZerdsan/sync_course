package ru.sfedu.Sync.api;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.Sync.models.*;
import ru.sfedu.Sync.utils.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JAXBDataProvider implements IDataProvider {

    private final String PATH = ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_XML);

    private final String FILE_EXTENSION = Constants.XML_FILE_EXTENSION;

    private Logger log = LogManager.getLogger(JAXBDataProvider.class);

    private Marshaller marshaller;
    private Unmarshaller unmarshaller;

    public JAXBDataProvider() throws IOException {
    }

    /**
     * Creates DataSource if not exist
     * @param path path to DataSource
     * @throws IOException in case if no permission
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
    public Result<?> changeZoneStatus(Long id, boolean status) throws Exception {
        Result<Zone> res = getZoneById(id);
        if (res.getResultType() == ResultType.ERROR) {
            return res;
        }
        Zone zone = res.getData().get(0);
        zone.setStatus(status);
        zone.getChannelList().stream().forEach(channel -> channel.setStatus(status));
        return new Result(Collections.singletonList(zone), ResultType.OK, Constants.STATUS_CHANGED);
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
    public String getPath(Class cn) throws IOException {
        return PATH + cn.getSimpleName().toLowerCase() + Constants.JAXB_SUFFIX + ConfigurationUtil.getConfigurationEntry(FILE_EXTENSION);
    }

    @Override
    public <T> Result<T> insertRecord(List<T> listRecord, boolean append, Class<T> cn) throws Exception {
        String path = getPath(cn);
        if (append) {
            FileReader fr = new FileReader(path);
            Result<T> res = get(cn, fr);
            fr.close();
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
        File f = new File(path);
        XMLWrapper<T> wrapper = new XMLWrapper<>(listRecord);
        log.info(Constants.LOG_RECORDS + wrapper.getList().toString());
        return add(wrapper, cn, f);
    }

    @Override
    public <T> Result<T> getRecords(Class cn) {
        try {
            String path = getPath(cn);
            FileReader fr = new FileReader(path);
            Result<T> res = get(cn, fr);
            if (res.getResultType() == ResultType.ERROR) {
                return res;
            } else if (res.getData().size() == 0) {
                return new Result<T>(res.getData(), ResultType.ERROR, Constants.LIST_EMPTY);
            }
            return res;
        }
        catch (IOException e) {
            log.error(e);
            return new Result(new ArrayList<T>(), ResultType.ERROR, Constants.BAD_FILE);
        }
    }

    @Override
    public <T> Result<T> getRecordById(Long id, Class<T> cn) throws Exception {
            Result<T> res = getRecords(cn);
            if (res.getResultType() == ResultType.ERROR) {
                return res;
            }
            return AdditionalMethods.getById(res.getData(), id, cn);
    }

    @Override
    public <T> Result<T> deleteRecord(Long id, Class<T> cn) throws Exception {
        Result<T> res;
        res = getRecordById(id, cn);
        if (res.getResultType() == ResultType.ERROR) {
            return res;
        }
        T recordToDelete = res.getData().get(0);
        res = getRecords(cn);
        if (res.getResultType() == ResultType.ERROR) {
            return res;
        }
        List<T> list = res.getData();
        list.remove(recordToDelete);
        res = insertRecord(res.getData(), false, cn);
        if (res.getResultType() == ResultType.ERROR) {
            return res;
        }
        return new Result<>(res.getData(), ResultType.OK, Constants.DELETED_SUCCESSFULLY);
    }

    @Override
    public <T> Result<T> updateRecord(Long id, T record) throws Exception {
        Class<T> cn = (Class<T>) record.getClass();
        Result<T> res = this.deleteRecord(id, cn);
        if (res.getResultType() == ResultType.ERROR) {
            return res;
        }
        List<T> list = res.getData();
        list.add(record);
        return insertRecord(list, false, cn);
    }

    /**
     * Generic method which only inserting records
     * @param wrapper wrapper with records list
     * @param cn ClassName which related to table name
     * @param f Datasource file
     * @param <T> Generic Type (any Class or type)
     * @return structure with status, data(if not error) and message
     * @throws SQLException if file already in use
     */
    private <T> Result<T> add(XMLWrapper<T> wrapper, Class<?> cn, File f) throws JAXBException, IOException {
        String path = getPath(cn);
        initDataSource(path);
        initWriter(cn);
        log.debug(Constants.LOG_WRITER_INIT);
        this.marshaller.marshal(wrapper, f);
        return new Result<T>(wrapper.getList(), ResultType.OK, Constants.INSERTED_SUCCESSFULLY);
    }

    /**
     * Generic method which only read from DataSource
     * @param cn ClassName which related to table name
     * @param f Datasource reader
     * @param <T> Generic Type (any Class or type)
     * @return structure with status, data(if not error) and message
     * @throws IOException in case if file already in use or it's doesn't exist
     */
    private <T> Result<T> get(Class<?> cn, FileReader f) {
        try {
            initReader(cn);
            log.debug(Constants.LOG_READER_INIT);
            XMLWrapper<T> result = (XMLWrapper<T>) this.unmarshaller.unmarshal(f);
            return new Result<T>(result.getList(), ResultType.OK, Constants.RECORDS_FOUND);
        }
        catch(JAXBException e) {
            log.error(e);
            return new Result<>(new ArrayList<>(), ResultType.ERROR, Constants.BAD_FILE);
        }
    }


    /**
     * Marshaller instance init
     * @param cn records class
     * @throws IOException in case if file already in use
     */
    private void initWriter(Class<?> cn) throws JAXBException, IOException {
        String path = getPath(cn);
        initDataSource(path);
        log.info(cn);
        JAXBContext context = JAXBContext.newInstance(XMLWrapper.class, cn);
        try {
            this.marshaller = context.createMarshaller();
            this.marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        } catch (JAXBException e) {
            log.error(e);
        }
    }

    /**
     * Unmarshaller instance init
     * @param cn records class
     * @throws IOException in case if file already in use
     */
    private void initReader(Class<?> cn) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(XMLWrapper.class, cn);
        this.unmarshaller = context.createUnmarshaller();
    }

}
