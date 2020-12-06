package ru.sfedu.Sync.api;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import ru.sfedu.Sync.models.BaseClass;
import ru.sfedu.Sync.utils.Result;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public interface IDataProvider {
    public String getPath(Class cn) throws IOException;
    public <T extends BaseClass> Result<T> insertRecord(List <T> listRecord, boolean append, Class cn) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, Exception;
    public <T extends BaseClass> Result<T> getRecords (Class cn) throws Exception;
    public <T extends BaseClass> Result getRecordById(int id, Class cn) throws Exception;
    public <T extends BaseClass> Result deleteRecord(int id, Class cn) throws Exception;
    public <T extends BaseClass> Result updateRecord(T record) throws Exception;
}
