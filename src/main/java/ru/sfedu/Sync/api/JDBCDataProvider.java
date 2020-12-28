package ru.sfedu.Sync.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.Sync.models.*;
import ru.sfedu.Sync.utils.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class JDBCDataProvider implements IDataProvider {

    private final String DB_USER = ConfigurationUtil.getConfigurationEntry(Constants.DB_USER);
    private final String DB_PASSWORD = ConfigurationUtil.getConfigurationEntry(Constants.DB_PASSWORD);

    private final String DB_PROTOCOL = ConfigurationUtil.getConfigurationEntry(Constants.DB_PROTOCOL);
    private final String PATH_TO_DB = ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_DB);
    private final String DB_NAME = ConfigurationUtil.getConfigurationEntry(Constants.DB_NAME);

    private final String PATH_TO_SCHEMA = ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_SQL_SCHEMA);

    private Connection connection;
    private Statement statement;

    private final Logger log = LogManager.getLogger(JDBCDataProvider.class);

    public JDBCDataProvider() throws IOException {}

    /**
     * Initialize connection to DataBase
     * @throws SQLException in case if connection can't be established
     */
    private void initConnection() throws SQLException {
        connection = DriverManager.getConnection(DB_PROTOCOL + PATH_TO_DB + DB_NAME, DB_USER, DB_PASSWORD);
        statement = connection.createStatement();
    }

    /**
     * Initialize DataBase with given schema
     * @throws SQLException in case if schema is corrupted or connection can't be established
     */
    private void initDataSource() throws SQLException {
        log.debug(Constants.LOG_CREATING_FILE);
        File file = new File(PATH_TO_SCHEMA);
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                byte[] data = new byte[(int) file.length()];
                fis.read(data);
                fis.close();
                String query = new String(data, StandardCharsets.UTF_8);
                initConnection();
                log.debug(query);
                statement.executeUpdate(query);
            } catch (IOException e) {
                log.error(e);
            } catch (SQLException e) {
                log.error(e);
            }
        }
        initConnection();
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
        Result<Event> res = insertRecord(list, append, Event.class);
        if (res.getResultType() == ResultType.ERROR) {
            return res;
        }
        for (Event event : list) {
            insertRecord(Collections.singletonList(event.getManager()), true, Manager.class);
            insertRecord(event.getZoneList(), true, Zone.class);
            setParent(Event.class, Manager.class, event.getManager().getId(), event.getId());
            List<Long> zoneIds = AdditionalMethods.getIds(event.getZoneList());
            for (Long id : zoneIds) {
                setParent(Zone.class, Event.class, event.getId(), id);
            }
        }
        return res;
    }

    @Override
    public Result<Zone> createZone(List<Zone> list, boolean append) throws Exception {
        Result<Zone> res = insertRecord(list, append, Zone.class);
        if (res.getResultType() == ResultType.ERROR) {
            return res;
        }
        for (Zone zone : list) {
            insertRecord(zone.getChannelList(), true, Channel.class);
            List<Channel> channelList = zone.getChannelList();
            List<Long> channelIds = AdditionalMethods.getIds(channelList);
            for (Long id : channelIds) {
                setParent(Channel.class, Zone.class, id, zone.getId());
            }
        }
        return res;
    }

    @Override
    public Result<Channel> createChannel(List<Channel> list, boolean append) throws Exception {
        Result<Channel> res = insertRecord(list, append, Channel.class);
        if (res.getResultType() == ResultType.ERROR) {
            return res;
        }
        for (Channel channel : list) {
            List<Speaker> speakerList = channel.getSpeakers();
            insertRecord(speakerList, true, Speaker.class);
            List<Long> speakerIds = AdditionalMethods.getIds(speakerList);
            for (Long id : speakerIds) {
                setParent( Speaker.class, Channel.class, id, channel.getId());
            }
        }
        return res;
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
        Result<Channel> res = getRecords(Channel.class);
        if (res.getResultType() == ResultType.ERROR) {
            return res;
        }
        List<Channel> channelList = res.getData();
        channelList.stream().forEach(channel -> {
            List<Speaker> speakerList = new ArrayList<>();
            Result<Speaker> speakerRes = getByParent(channel.getId(), Speaker.class, Channel.class);
            if (speakerRes.getResultType() == ResultType.OK) {
                speakerList = speakerRes.getData();
            }
            channel.setSpeakers(speakerList);
        });
        return res;
    }

    @Override
    public Result<Zone> getZones() throws Exception {
        Result<Zone> res = getRecords(Zone.class);
        if (res.getResultType() == ResultType.ERROR) {
            return res;
        }
        List<Zone> zoneList = res.getData();
        zoneList.stream().forEach(zone -> {
            List<Channel> channelList = new ArrayList<>();
            Result<Channel> channelRes = getByParent(zone.getId(), Channel.class, Zone.class);
            if (channelRes.getResultType() == ResultType.OK) {
                channelList = channelRes.getData();
            }
            zone.setChannelList(channelList);
        });
        return res;
    }

    @Override
    public Result<Event> getEvents() throws Exception {
        Result<Event> res = getRecords(Event.class);
        if (res.getResultType() == ResultType.ERROR) {
            return res;
        }
        List<Event> eventList = res.getData();
        eventList.stream().forEach(event -> {
            Manager manager = event.getManager();
            Result<Manager> managerRes = getManagerById(manager.getId());
            if (managerRes.getResultType() == ResultType.OK) {
                log.info(managerRes.getData());
                manager = managerRes.getData().get(0);
            }
            event.setManager(manager);
            List<Zone> zoneList = new ArrayList<>();
            Result<Zone> zoneRes = getByParent(event.getId(), Zone.class, Event.class);
            if (zoneRes.getResultType() == ResultType.OK) {
                zoneList = zoneRes.getData();
            }
            event.setZoneList(zoneList);
        });
        res.setData(eventList);
        return res;
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
    public Result<Admin> getAdminById(Long id) {
        return getRecordById(id, Admin.class);
    }

    @Override
    public Result<Manager> getManagerById(Long id) {
        return getRecordById(id, Manager.class);
    }

    @Override
    public Result<Speaker> getSpeakerById(Long id) {
        return getRecordById(id, Speaker.class);
    }

    @Override
    public Result<Channel> getChannelById(Long id) {
        return getRecordById(id, Channel.class);
    }

    @Override
    public Result<Zone> getZoneById(Long id) {
        return getRecordById(id, Zone.class);
    }

    @Override
    public Result<Event> getEventById(Long id) {
        return getRecordById(id, Event.class);
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

    @Override
    public Result<?> changeZoneStatus(Long id, boolean status) throws Exception {
        try {
            initDataSource();
            Result<Zone> res = getRecordById(id, Zone.class);
            if (res.getResultType() == ResultType.ERROR) {
                return res;
            }
            log.info(res.getData());
            statement.executeUpdate(String.format(Constants.SQL_CHANGE_STATUS, Zone.class.getSimpleName(), status, id));
            List<Long> channelIds = AdditionalMethods.getIds(res.getData().get(0).getChannelList());
            for (Long channelId : channelIds) {
                statement.executeUpdate(String.format(Constants.SQL_CHANGE_STATUS, Channel.class.getSimpleName(), status, channelId));
            }
            close();
            return new Result<>(null, ResultType.OK, Constants.STATUS_CHANGED);
        }
        catch (SQLException e) {
            log.error(e);
            return new Result<>(null, ResultType.ERROR, Constants.SQL_FAIL);
        }
    }

    @Override
    public <T> Result<T> insertRecord(List<T> listRecord, boolean append, Class<T> cn) throws Exception {
        initDataSource();
        if (!append) {
            flushTable(cn);
        }
        return add(listRecord, cn);
    }

    @Override
    public <T> Result<T> getRecords(Class cn) throws Exception {
        initDataSource();
        Result<T> res = get(cn);
        if (res.getResultType() == ResultType.ERROR) {
            return res;
        }
        List<T> listRecords = res.getData();
        if (listRecords.size() == 0) {
            return new Result<>(listRecords, ResultType.ERROR, Constants.LIST_EMPTY);
        }
        return res;
    }

    @Override
    public <T> Result<T> getRecordById(Long id, Class<T> cn) {
        try {
            initDataSource();
            ResultSet rs = statement.executeQuery(String.format(Constants.DB_BY_ID, cn.getSimpleName(), id));
            List<T> res = getDataFormResultSet(rs, cn);
            if (res.size() > 0) {
                return new Result<>(res, ResultType.OK, Constants.FOUND_ELEMENT);
            }
            return new Result<>(null, ResultType.ERROR, Constants.NOT_FOUND);
        } catch (Exception e) {
            log.error(e);
            return new Result<>(null, ResultType.ERROR, Constants.SQL_FAIL);
        }
    }

    @Override
    public <T> Result<T> deleteRecord(Long id, Class<T> cn) throws Exception {
        initDataSource();
        int rowCount = statement.executeUpdate(
                String.format(
                        Constants.DELETE_BY_ID,
                        cn.getSimpleName(),
                        id));
        close();
        log.debug(rowCount);
        if (rowCount > 0) {
            return new Result<>(null, ResultType.OK, Constants.DELETED_SUCCESSFULLY);
        }
        return new Result<>(null, ResultType.ERROR, Constants.NOT_FOUND);
    }

    @Override
    public <T> Result<T> updateRecord(Long id, T record) throws Exception {
        initDataSource();
        int rowCount = statement.executeUpdate(
                String.format(
                        Constants.UPDATE,
                        record.getClass().getSimpleName(),
                        String.join(Constants.PARAM_DELIMITER, getSqlUpdateString(record)), id));
        close();
        log.debug(rowCount);
        if (rowCount > 0) {
            return new Result<>(null, ResultType.OK, Constants.UPDATED_SUCCESSFULLY);
        }
        return new Result<>(null, ResultType.ERROR, Constants.NOT_FOUND);
    }

    /**
     * Parse record to a String which can be used in INSERT query
     * @param record which will be parsed
     * @param <T> Generic Type (any Class or type)
     * @return Array with column names and its values
     * @throws InvocationTargetException in case if there is no id getter for given class
     * @throws IllegalAccessException in case if method can't invoke method on given object
     */
    public <T> String[] getSqlString (T record) throws InvocationTargetException, IllegalAccessException {
        Class<?> cn = record.getClass();
        List<Method> methods = Arrays.stream(cn.getMethods())
                .filter(method -> method.getName().matches(Constants.GET_METHODS_PATTERN))
                .filter(method -> !method.getName().equals(Constants.GET_CLASS))
                .collect(Collectors.toList());
        log.debug(methods);
        List<String> columns = new ArrayList<>();
        List<String> values = new ArrayList<>();
        for (Method method : methods) {
            if (Constants.PRIMITIVE_CLASSES.contains(method.getReturnType())) {
                columns.add(method.getName().replace(Constants.GET, Constants.EMPTY_STRING).toLowerCase(Locale.ROOT));
                if (method.getReturnType() == String.class) {
                    values.add(String.format(Constants.SQL_STRING,method.invoke(record)));
                } else {
                    values.add(String.valueOf(method.invoke(record)));

                }
            }
        }
        log.debug(columns);
        log.debug(values);
        return new String[]{String.join(Constants.PARAM_DELIMITER, columns), String.join(Constants.PARAM_DELIMITER, values)};
    }

    /**
     * Parse record into string for UPDATE query
     * @param record which will be parsed
     * @param <T> Generic Type (any Class or type)
     * @return List of Strings (ex. parameter=value)
     * @throws InvocationTargetException in case if there is no id getter for given class
     * @throws IllegalAccessException in case if method can't invoke method on given object
     */
    private <T> List<String> getSqlUpdateString (T record) throws InvocationTargetException, IllegalAccessException {
        String[] data = getSqlString(record);
        List<String> columns = Arrays.asList(data[0].split(Constants.PARAM_DELIMITER));
        List<String> values = Arrays.asList(data[1].split(Constants.PARAM_DELIMITER));
        List<String> res = IntStream
                .range(0, columns.size())
                .mapToObj(index -> columns.get(index) + Constants.SINGLE_EQUAL + values.get(index))
                .collect(Collectors.toList());
        return res;
    }

    /**
     * Generic method which only inserting records
     * @param listRecord list to write
     * @param cn ClassName which related to table name
     * @param <T> Generic Type (any Class or type)
     * @return structure with status, data(if not error) and message
     * @throws SQLException in case if connection is not established or query is corrupted
     */
    private <T> Result<T> add(List<T> listRecord, Class<?> cn) throws SQLException {
        initDataSource();
        try {
            List<String> query = new ArrayList<>();
            for (T record : listRecord) {
                String[] data = getSqlString(record);
                log.debug(String.format(Constants.DB_INSERT,cn.getSimpleName().toUpperCase(Locale.ROOT),data[0], data[1]));
                query.add(
                        String.format(
                                Constants.DB_INSERT,
                                cn.getSimpleName().toUpperCase(Locale.ROOT),
                                data[0], data[1]));
            }
            log.info(String.join(Constants.EMPTY_STRING, query));
            statement.executeUpdate(String.join(Constants.EMPTY_STRING, query));
            return new Result<>(listRecord, ResultType.OK, Constants.INSERTED_SUCCESSFULLY);
        } catch( Exception e) {
            log.error(e);
            return new Result<>(null, ResultType.ERROR, Constants.SQL_FAIL);
        }
        finally {
            close();
        }
    }

    /**
     * Generic method which only read from DataSource
     * @param cn ClassName which related to table name
     * @param <T> Generic Type (any Class or type)
     * @return structure with status, data(if not error) and message
     * @throws SQLException in case if connection is not established or query is corrupted
     */
    private <T> Result<T> get(Class<?> cn) throws SQLException {
        initDataSource();
        try {
            ResultSet rs = statement.executeQuery(String.format(Constants.DB_SELECT, cn.getSimpleName()));
            List<T> res = getDataFormResultSet(rs, cn);
            log.info(res);
            return new Result<T>(res, ResultType.OK, Constants.RECORDS_FOUND);
        }
        catch (SQLException e) {
            log.error(e);
            return new Result<>(null, ResultType.ERROR, Constants.SQL_FAIL);
        }
        finally {
            close();
        }
    }

    /**
     * Close current connection
     * @throws SQLException in case if transaction is not over or it's closed already
     */
    private void close() throws SQLException {
        connection.close();
        statement.close();
    }

    /**
     * Links two records
     * @param cn class which related to child table name
     * @param parentCn class which related to parent table name
     * @param parentId id of parent record
     * @param id id of child record
     * @param <T> Generic Type (any Class or type)
     * @throws SQLException in case if connection is not established or query is corrupted
     */
    private <T> void setParent(Class<?> cn, Class<?> parentCn, Long parentId, Long id)  throws SQLException {
        initDataSource();
        try {
            log.info(String.format(Constants.CHANGE_PARENT_ID, cn.getSimpleName(), parentCn.getSimpleName(), parentId, id));
            statement.executeUpdate(String.format(Constants.CHANGE_PARENT_ID, cn.getSimpleName(), parentCn.getSimpleName(), parentId, id));
        }
        catch (SQLException e) {
            log.error(e);
        }
        finally {
            close();
        }
    }

    /**
     * Get all child by Foreign key
     * @param parentId foreign key
     * @param cn table name from where you want to get records
     * @param parentCn table name from where to find key
     * @param <T> Generic Type (any Class or type)
     * @return structure with status, data(if not error) and message
     */
    private <T> Result<T> getByParent(Long parentId, Class<?> cn, Class<?> parentCn){
        try {
            initDataSource();
            log.info(String.format(Constants.GET_BY_PARENT_ID, cn.getSimpleName(), parentCn.getSimpleName(), parentId));
            ResultSet rs = statement.executeQuery(String.format(Constants.GET_BY_PARENT_ID, cn.getSimpleName(), parentCn.getSimpleName(), parentId));
            List<T> res = getDataFormResultSet(rs, cn);
            return new Result<>(res, ResultType.OK, Constants.RECORDS_FOUND);
        } catch(Exception e) {
            log.error(e);
            return new Result<>(null, ResultType.ERROR, Constants.SQL_FAIL);
        }
    }

    /**
     * Remove all data from table
     * @param cn table name
     * @throws SQLException in case if connection is not established or query is corrupted
     */
    public void flushTable(Class<?> cn) throws SQLException {
        initDataSource();
        log.info(Constants.FLUSH_FILE + cn.getSimpleName().toUpperCase(Locale.ROOT));
        statement.executeUpdate(String.format(Constants.DELETE_ALL, cn.getSimpleName().toUpperCase(Locale.ROOT)));
        close();
    }

    /**
     * Parse raw data into JavaBeans
     * @param rs raw data
     * @param cn instance of the class we need
     * @param <T> Generic Type (any Class or type)
     * @param <T> Generic Type (any Class or type)
     * @throws SQLException in case if ResultSet is corrupted
     */
    private <T> List<T> getDataFormResultSet(ResultSet rs, Class cn) throws SQLException {
        List<T> res = new ArrayList<>();
        if (cn == Admin.class) {
            res = (List<T>) AdditionalMethods.getAdminFromResultSet(rs);
        }
        if (cn == Manager.class) {
            res = (List<T>) AdditionalMethods.getManagerFromResultSet(rs);
        }
        if (cn == Speaker.class) {
            res = (List<T>) AdditionalMethods.getSpeakerFromResultSet(rs);
        }
        if (cn == Channel.class) {
            res = (List<T>) AdditionalMethods.getChannelFromResultSet(rs);
        }
        if (cn == Zone.class) {
            res = (List<T>) AdditionalMethods.getZoneFromResultSet(rs);
        }
        if (cn == Event.class) {
            res = (List<T>) AdditionalMethods.getEventFromResultSet(rs);
        }
        return res;
    }
}
