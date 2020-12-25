package ru.sfedu.Sync.utils;

import ru.sfedu.Sync.models.Channel;
import ru.sfedu.Sync.models.Event;
import ru.sfedu.Sync.models.Zone;

public class Constants {
    public static final String PATH_TO_CSV = "PATH_TO_CSV";
    public static final String CSV_FILE_EXTENSION = "CSV_FILE_EXTENSION";

    public static final String PATH_TO_XML = "PATH_TO_XML";
    public static final String XML_FILE_EXTENSION = "XML_FILE_EXTENSION";

    public static final String LOG_CREATING_FILE = "Creating datasource...";

    public static final String METHOD_GET_ID = "getId";
    public static final String NO_METHOD = "Given class doesn't have id getter";

    public static final String BAD_FILE = "File content is not valid";
    public static final String FLUSH_FILE = "File content is corrupted. Flushing File...";
    public static final String NOT_FOUND = "There is no element with given id";
    public static final String FOUND_ELEMENT = "Found element";
    public static final String ALREADY_EXIST = "Record with given id already exists";
    public static final String INSERTED_SUCCESSFULLY = "Record was inserted successfully";
    public static final String LIST_EMPTY = "List is empty";
    public static final String RECORDS_FOUND = "Found records";
    public static final String DELETED_SUCCESSFULLY = "Record was deleted successfully";
    public static final String STATUS_CHANGED = "Status was changed successfully";
    public static final String STREAM_IS_CLOSED = "Stream already closed";
}
