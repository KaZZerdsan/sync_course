package ru.sfedu.Sync.utils;

import com.sun.xml.bind.v2.model.core.ID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.Sync.api.IDataProvider;
import ru.sfedu.Sync.models.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

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

    public static <T> List<Long> getIds(List<T> list) {
        List<Long> res = new ArrayList<>();
        Class<?> cn;
        if (list != null && list.size() > 0) {
            cn = list.get(0).getClass();
        } else {
            return new ArrayList<>();
        }
        try {
            Method getter = cn.getMethod(Constants.METHOD_GET_ID);
            for (T el: list) {
                res.add((Long) getter.invoke(el));
            }
        }
        catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            log.error(e);
            log.info(Constants.NO_METHOD);
        }
        return res;
    }

    public static List<Admin> getAdminFromResultSet(ResultSet rs) throws SQLException {
        List<Admin> res = new ArrayList<>();
        while (rs.next()) {
            Admin admin = new Admin();
            admin.setId(rs.getLong(1));
            admin.setName(rs.getString(2));
            res.add(admin);
        }
        return res;
    }

    public static List<Manager> getManagerFromResultSet(ResultSet rs) throws SQLException {
        List<Manager> res = new ArrayList<>();
        while (rs.next()) {
            Manager manager = new Manager();
            manager.setId(rs.getLong(1));
            manager.setName(rs.getString(2));
            res.add(manager);
        }
        return res;
    }

    public static List<Speaker> getSpeakerFromResultSet(ResultSet rs) throws SQLException {
        List<Speaker> res = new ArrayList<>();
        while (rs.next()) {
            Speaker speaker = new Speaker();
            speaker.setId(rs.getLong(1));
            speaker.setName(rs.getString(2));
            res.add(speaker);
        }
        return res;
    }

    public static List<Channel> getChannelFromResultSet(ResultSet rs) throws SQLException {
        List<Channel> res = new ArrayList<>();
        while (rs.next()) {
            Channel channel = new Channel();
            channel.setId(rs.getLong(1));
            channel.setName(rs.getString(2));
            channel.setStatus(rs.getBoolean(3));
            channel.setLanguage(rs.getString(4));
            res.add(channel);
        }
        return res;
    }

    public static List<Zone> getZoneFromResultSet(ResultSet rs) throws SQLException {
        List<Zone> res = new ArrayList<>();
        while (rs.next()) {
            Zone zone = new Zone();
            zone.setId(rs.getLong(1));
            zone.setName(rs.getString(2));
            zone.setDateStart(rs.getLong(3));
            zone.setDateEnd(rs.getLong(4));
            zone.setStatus(rs.getBoolean(5));
            res.add(zone);
        }
        return res;
    }

    public static List<Event> getEventFromResultSet(ResultSet rs) throws SQLException {
        List<Event> res = new ArrayList<>();
        while (rs.next()) {
            Event event = new Event();
            event.setId(rs.getLong(1));
            event.setName(rs.getString(2));
            event.setManager(new Manager());
            event.getManager().setId(rs.getLong(3));
            res.add(event);
        }
        return res;
    }

}
