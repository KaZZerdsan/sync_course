package ru.sfedu.Sync.api;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import ru.sfedu.Sync.models.*;
import ru.sfedu.Sync.utils.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimpleXMLDataProvider implements IDataProvider {

    private final String PATH = ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_XML);

    private final String FILE_EXTENSION = Constants.XML_FILE_EXTENSION;

    private Logger log = LogManager.getLogger(SimpleXMLDataProvider.class);

    public SimpleXMLDataProvider() throws IOException {
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


    public String getPath(Class cn) throws IOException {
        return PATH + cn.getSimpleName().toLowerCase() + ConfigurationUtil.getConfigurationEntry(FILE_EXTENSION);
    }

    @Override
    public <T> Result<T> insertRecord(List<T> listRecord, boolean append, Class<T> cn) throws Exception {
        String path = getPath(cn);
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
        XMLWrapper<T> wrapper = new XMLWrapper<>(listRecord);
        log.info(Constants.LOG_RECORDS + wrapper.getList().toString());
        return add(wrapper, cn);
    }

    @Override
    public <T> Result<T> getRecords(Class cn) throws Exception {
        try {
            String path = getPath(cn);
            Result<T> res = get(cn);
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

    private <T> Result<T> add(XMLWrapper<T> wrapper, Class<?> cn) throws Exception {
        try {
            String path = getPath(cn);
            initDataSource(path);
            FileWriter fw = new FileWriter(path);
            Serializer serializer = new Persister();
            log.debug(Constants.LOG_WRITER_INIT);
            serializer.write(wrapper, fw);
            fw.close();
            return new Result<T>(wrapper.getList(), ResultType.OK, Constants.INSERTED_SUCCESSFULLY);
        } catch (Exception e) {
            log.error(e);
            return new Result<T>(null, ResultType.ERROR, Constants.BAD_FILE);
        }
    }

    private <T> Result<T> get(Class<?> cn) throws Exception {
        try {
            String path = getPath(cn);
            initDataSource(path);
            FileReader reader = new FileReader(path);
            Serializer serializer = new Persister();
            log.debug(Constants.LOG_READER_INIT);
            XMLWrapper<T> result = (XMLWrapper<T>) serializer.read(cn, reader);
            reader.close();
            return new Result<T>(result.getList(), ResultType.OK, Constants.RECORDS_FOUND);
        }
        catch (Exception e) {
            log.error(e);
            return new Result<>(null, ResultType.ERROR, Constants.BAD_FILE);
        }
    }
}
