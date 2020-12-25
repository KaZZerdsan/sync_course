package ru.sfedu.Sync.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdditionalMethods {
    private static Logger log = LogManager.getLogger(AdditionalMethods.class);

    public static <T> Result<T> getById(List<T> records, Long id, Class<T> cn) {
        try {
            Method getter = cn.getMethod(Constants.METHOD_GET_ID);
            for (T element : records) {
                Long recordId = (Long) getter.invoke(element);
                if (recordId.equals(id)) {
                    return new Result<>(Collections.singletonList(element), ResultType.OK, Constants.FOUND_ELEMENT);
                }
            }
            return new Result<>(null, ResultType.ERROR, Constants.NOT_FOUND);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error(e);
            return new Result<>(null, ResultType.ERROR, Constants.NO_METHOD);
        }
    }

    public static <T> Boolean hasDuplicates(List<T> newRecords, List<T> oldRecords) throws IllegalAccessException {
        Class<?> cn = newRecords.get(0).getClass();
        log.debug(cn);
        try {
            Method idGetter = cn.getMethod(Constants.METHOD_GET_ID);
            List<Long> newIds = new ArrayList<>();
            for (T el: newRecords) {
                newIds.add((Long) idGetter.invoke(el));
            }
            List<Long> oldIds = new ArrayList<>();
            log.debug(oldRecords.toString());
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
}
