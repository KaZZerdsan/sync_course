package ru.sfedu.Sync.utils;

import java.util.Arrays;
import java.util.HashSet;

public class Constants {

    public static final String PATH_TO_CSV = "PATH_TO_CSV";
    public static final String CSV_FILE_EXTENSION = "CSV_FILE_EXTENSION";
    public static final String JAXB_SUFFIX = "_jaxb";

    public static final String PATH_TO_XML = "PATH_TO_XML";
    public static final String XML_FILE_EXTENSION = "XML_FILE_EXTENSION";

    public static final String DB_PROTOCOL = "DB_PROTOCOL";
    public static final String PATH_TO_DB = "PATH_TO_DB";
    public static final String DB_NAME = "DB_NAME";

    public static final String LOG_CREATING_FILE = "Creating datasource...";

    public static final String METHOD_GET_ID = "getId";
    public static final String NO_METHOD = "Given class doesn't have id getter";

    public static final String BAD_FILE = "File content is not valid";
    public static final String FLUSH_FILE = "Flushing datasource...";
    public static final String NOT_FOUND = "There is no element with given id";
    public static final String FOUND_ELEMENT = "Found element";
    public static final String ALREADY_EXIST = "Record with given id already exists";
    public static final String INSERTED_SUCCESSFULLY = "Record was inserted successfully";
    public static final String LIST_EMPTY = "List is empty";
    public static final String RECORDS_FOUND = "Found records";
    public static final String DELETED_SUCCESSFULLY = "Record was deleted successfully";
    public static final String UPDATED_SUCCESSFULLY = "Record was updated successfully";
    public static final String STATUS_CHANGED = "Status was changed successfully";
    public static final String STREAM_IS_CLOSED = "Stream already closed";

    public static final String LOG_RECORDS = "Given Records: \n";
    public static final String LOG_READER_INIT = "Reader was initialized successfully";
    public static final String LOG_WRITER_INIT = "Writer was initialized successfully";

    public static final String DB_USER = "DB_USER";
    public static final String DB_PASSWORD = "DB_PASSWORD";
    public static final String PATH_TO_SQL_SCHEMA = "PATH_TO_SQL_SCHEMA";

    public static final String GET_METHODS_PATTERN = "^(get)\\w+";
    public static final String GET_CLASS = "getClass";

    public static final String DB_INSERT = "INSERT INTO %s (%s) VALUES(%s);";
    public static final String DB_SELECT = "SELECT * FROM %s;";
    public static final String DB_BY_ID = "SELECT * FROM %s WHERE id=%s;";
    public static final String DELETE_BY_ID = "DELETE FROM %s WHERE id=%s;";
    public static final String DELETE_ALL = "DELETE FROM %s;";
    public static final String UPDATE = "UPDATE %s SET %s WHERE id=%s;";
    public static final String SQL_FAIL = "Error while executing query";
    public static final String SQL_STRING = "'%s'";
    public static final String SQL_CHANGE_STATUS = "UPDATE %S SET status=%s WHERE id=%s;";
    public static final String CHANGE_PARENT_ID = "UPDATE %s SET %sId=%s WHERE id=%s;";
    public static final String GET_BY_PARENT_ID = "SELECT * FROM %s WHERE %sid=%s";

    public static final String PARAM_DELIMITER = ", ";
    public static final String GET = "get";
    public static final String EMPTY_STRING = "";
    public static final String SINGLE_EQUAL = "=";
    public static final String NEW = "_NEW";
    public static final HashSet<Class<?>> PRIMITIVE_CLASSES = new HashSet<>(Arrays.asList(Boolean.class, Long.class, String.class));

    public static final String CSV = "CSV";
    public static final String XML = "XML";
    public static final String JAXB = "JAXB";
    public static final String DB = "DB";

    public static final String INVALID_DATA_PROVIDER = "Invalid DataProvider type...";
    public static final String FEW_ARGUMENTS = "Too few arguments were given...";
    public static final String INVALID_METHOD_NAME = "Invalid method name was given...";

    public static final String CREATE_ADMIN = "CREATEADMIN";
    public static final String CREATE_MANAGER = "CREATEMANAGER";
    public static final String CREATE_SPEAKER = "CREATESPEAKER";
    public static final String CREATE_EVENT = "CREATEEVENT";
    public static final String CREATE_ZONE = "CREATEZONE";
    public static final String CREATE_CHANNEL = "CREATECHANNEL";
    public static final String GET_ADMINS = "GETADMINS";
    public static final String GET_MANAGERS = "GETMANAGERS";
    public static final String GET_SPEAKERS = "GETSPEAKERS";
    public static final String GET_EVENTS = "GETEVENTS";
    public static final String GET_ZONES = "GETZONES";
    public static final String GET_CHANNELS = "GETCHANNELS";
    public static final String GET_ADMIN_BY_ID = "GETADMINBYID";
    public static final String GET_MANAGER_BY_ID = "GETMANAGERBYID";
    public static final String GET_SPEAKER_BY_ID = "GETSPEAKERBYID";
    public static final String GET_EVENT_BY_ID = "GETEVENTBYID";
    public static final String GET_ZONE_BY_ID = "GETZONEBYID";
    public static final String GET_CHANNEL_BY_ID = "GETCHANNELBYID";
    public static final String DELETE_ADMIN = "DELETEADMIN";
    public static final String DELETE_MANAGER = "DELETEMANAGER";
    public static final String DELETE_SPEAKER = "DELETESPEAKER";
    public static final String DELETE_EVENT = "DELETEEVENT";
    public static final String DELETE_ZONE = "DELETEZONE";
    public static final String DELETE_CHANNEL = "DELETECHANNEL";
    public static final String UPDATE_ADMIN = "UPDATEADMIN";
    public static final String UPDATE_MANAGER = "UPDATEMANAGER";
    public static final String UPDATE_SPEAKER = "UPDATESPEAKER";
    public static final String UPDATE_EVENT = "UPDATEEVENT";
    public static final String UPDATE_ZONE = "UPDATEZONE";
    public static final String UPDATE_CHANNEL = "UPDATECHANNEL";
    public static final String CHANGE_ZONE_STATUS = "CHANGEZONESTATUS";

    public static final String[] I_METHODS = {"createAdmin", "createManager", "createSpeaker", "createEvent", "createZone", "createChannel", "getAdmins", "getManagers", "getSpeakers", "getEvents", "getZones", "getChannels", "getAdminById", "getManagerById", "getSpeakerById", "getEventById", "getZoneById", "getChannelById", "deleteAdmin", "deleteManager", "deleteSpeaker", "deleteEvent", "deleteZone", "deleteChannel", "updateAdmin", "updateManager", "updateSpeaker", "updateEvent", "updateZone", "updateChannel", "changeZoneStatus"};
}
