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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JAXBDataProvider {

    private final String PATH = ConfigurationUtil.getConfigurationEntry("PATH_TO_XML");

    private final String FILE_EXTENSION = "XML_FILE_EXTENSION";

    private Logger log = LogManager.getLogger(JAXBDataProvider.class);

    private Marshaller marshaller;
    private Unmarshaller unmarshaller;

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


    public Result<Admin> createAdmin(List<Admin> list, boolean append) throws Exception {
        return insertRecord(list, append, Admin.class);
    }
    public Result<Manager> createManager(List<Manager> list, boolean append) throws Exception {
        return insertRecord(list, append, Manager.class);
    }
    public Result<Speaker> createSpeaker(List<Speaker> list, boolean append) throws Exception {
        return insertRecord(list, append, Speaker.class);
    }
    public Result<Event> createEvent(List<Event> list, boolean append) throws Exception {
        return insertRecord(list, append, Event.class);
    }
    public Result<Zone> createZone(List<Zone> list, boolean append) throws Exception {
        return insertRecord(list, append, Zone.class);
    }
    public Result<Channel> createChannel(List<Channel> list, boolean append) throws Exception {
        return insertRecord(list, append, Channel.class);
    }


    public String getPath(Class cn) throws IOException {
        return PATH + cn.getSimpleName().toLowerCase() + Constants.JAXB_SUFFIX + ConfigurationUtil.getConfigurationEntry(FILE_EXTENSION);
    }


    public <T> Result<T> insertRecord(List<T> listRecord, boolean append, Class cn) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, Exception {
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


    public <T> Result<T> getRecordById(Long id, Class<T> cn) throws Exception {
            Result<T> res = getRecords(cn);
            if (res.getResultType() == ResultType.ERROR) {
                return res;
            }
            return AdditionalMethods.getById(res.getData(), id, cn);
    }


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


    public <T> Result<T> updateRecord(Long id, T record) throws Exception {
        Class<T> cn = (Class<T>) record.getClass();
        Result<T> res = this.deleteRecord(id, cn);
        if (res.getResultType() == ResultType.ERROR) {
            return res;
        }
        List<T> list = res.getData();
        list.add(record);
        return insertRecord(list, false, record.getClass());
    }

    private <T> Result<T> add(XMLWrapper<T> wrapper, Class<?> cn, File f) throws JAXBException {
        initWriter(cn);
        log.debug(Constants.LOG_WRITER_INIT);
        this.marshaller.marshal(wrapper, f);
        return new Result<T>(wrapper.getList(), ResultType.OK, Constants.INSERTED_SUCCESSFULLY);
    }

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

    private void initWriter(Class<?> cn) throws JAXBException {
        log.info(cn);
        JAXBContext context = JAXBContext.newInstance(XMLWrapper.class, cn);
        try {
            this.marshaller = context.createMarshaller();
            this.marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        } catch (JAXBException e) {
            log.error(e);
        }
    }

    private void initReader(Class<?> cn) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(XMLWrapper.class, cn);
        this.unmarshaller = context.createUnmarshaller();
    }

}
