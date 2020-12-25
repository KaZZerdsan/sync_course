package ru.sfedu.Sync.api;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import ru.sfedu.Sync.models.*;
import ru.sfedu.Sync.utils.Result;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public interface IDataProvider {
    public Result<Admin> createAdmin(List<Admin> list);
    public Result<Manager> createManager(List<Manager> list);
    public Result<Speaker> createSpeaker(List<Speaker> list);
    public Result<Event> createEvent(List<Event> list);
    public Result<Zone> createZone(List<Zone> list);
    public Result<Channel> createChannel(List<Channel> list);
    public Result<Admin> getAdmins();
    public Result<Manager> getManagers();
    public Result<Speaker> getSpeakers();
    public Result<Event> getEvents();
    public Result<Zone> getZones();
    public Result<Channel> getChannels();
    public Result<Admin> getAdminById(long id);
    public Result<Manager> getManagerById(long id);
    public Result<Speaker> getSpeakerById(long id);
    public Result<Event> getEventById(long id);
    public Result<Zone> getZoneById(long id);
    public Result<Channel> getChannelById(long id);
    public Result<Admin> deleteAdmin(long id);
    public Result<Manager> deleteManager(long id);
    public Result<Speaker> deleteSpeaker(long id);
    public Result<Event> deleteEvent(long id);
    public Result<Zone> deleteZone(long id);
    public Result<Channel> deleteChannel(long id);
    public Result<Admin> updateAdmin(Admin newAdmin);
    public Result<Manager> updateManager(Manager newManager);
    public Result<Speaker> updateSpeaker(Speaker newSpeaker);
    public Result<Event> updateEvent(Event newEvent);
    public Result<Zone> updateZone(Zone newZone);
    public Result<Channel> updateChannel(Channel newChannel);
    public String getPath(Class cn) throws IOException;
    public <T extends BaseClass> Result<T> insertRecord(List <T> listRecord, boolean append, Class cn) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, Exception;
    public <T extends BaseClass> Result<T> getRecords (Class cn) throws Exception;
    public <T extends BaseClass> Result getRecordById(Long id, Class cn) throws Exception;
    public <T extends BaseClass> Result deleteRecord(Long id, Class cn) throws Exception;
    public <T extends BaseClass> Result updateRecord(Long id, T record) throws Exception;
}
