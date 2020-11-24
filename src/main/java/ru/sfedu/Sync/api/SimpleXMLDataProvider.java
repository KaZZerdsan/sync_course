package ru.sfedu.Sync.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import ru.sfedu.Sync.models.BaseClass;
import ru.sfedu.Sync.utils.*;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimpleXMLDataProvider implements IDataProvider {

    private Serializer serializer;

    private final String PATH = ConfigurationUtil.getConfigurationEntry("PATH_TO_XML");

    private final String FILE_EXTENSION = "XML_FILE_EXTENSION";

    private Logger log = LogManager.getLogger(SimpleXMLDataProvider.class);

    public SimpleXMLDataProvider() throws IOException {
        this.serializer = new Persister();
    }

    @Override
    public String getPath(Class cn) throws IOException {
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

    @Override
    public <T extends BaseClass> Result<T> insertRecord(List<T> listRecord, boolean append, Class cn) throws Exception {
        try {
            String path = getPath(cn);
            initDataSource(path);
            if (append) {
                Result res = getRecords(cn);
                List<T> oldList = (List<T>) res.data;
                if (oldList != null && oldList.size() > 0) {
                    List<T> finalListRecord = listRecord;
                    boolean hasSameId = oldList
                            .stream()
                            .anyMatch(el -> el.getId() == finalListRecord.get(0).getId());
                    if (hasSameId) {
                        return new Result<>(null, ResultType.ERROR, Constants.ALREADY_EXIST);
                    }
                    listRecord = Stream
                            .concat(oldList.stream(), listRecord.stream())
                            .collect(Collectors.toList());
                }
            }
            FileWriter fw = new FileWriter(path, false);
            XMLWrapper<T> beans = new XMLWrapper<T>(listRecord);
            this.serializer.write(beans, fw);
            fw.close();
            return new Result(listRecord, ResultType.OK, Constants.INSERTED_SUCCESSFULLY);
        } catch (IndexOutOfBoundsException e) {
            log.error(e);
            return new Result<>(null, ResultType.ERROR, Constants.LIST_EMPTY);
        }
    }

    @Override
    public <T> Result<T> getRecords(Class cn) throws Exception {
        try {
            String path = getPath(cn);
            System.out.println(path);
            FileReader fr = new FileReader(path);
            XMLWrapper wrapper = this.serializer.read(XMLWrapper.class, fr);
            List<T> list = wrapper.getList();
            if (list.size() > 0) {
                return new Result<>((T) list, ResultType.OK, Constants.RECORDS_FOUND);
            }
            fr.close();
            return new Result<>(null, ResultType.ERROR, Constants.LIST_EMPTY);
        } catch (IOException e) {
            log.error(e);
            return new Result<>(null, ResultType.ERROR, Constants.FILE_IN_USE);
        }
        catch (XMLStreamException e) {
            return new Result<>(null, ResultType.ERROR, Constants.LIST_EMPTY);
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
