package ru.sfedu.Sync.api;

import ru.sfedu.Sync.models.*;
import ru.sfedu.Sync.utils.Result;

import java.util.List;

public interface IDataProvider {
    /**
     * Creates Admin Record in DataSource
     * @param list list of records
     * @param append flag which show, do we need to add records to the end of list
     * @return structure with status, data(if not error) and message
     * @throws Exception
     */
    public Result<Admin> createAdmin(List<Admin> list, boolean append) throws Exception;

    /**
     * Creates Manager Record in DataSource
     * @param list list of records
     * @param append flag which show, do we need to add records to the end of list
     * @return structure with status, data(if not error) and message
     * @throws Exception
     */
    public Result<Manager> createManager(List<Manager> list, boolean append) throws Exception;

    /**
     * Creates Speaker Record in DataSource
     * @param list list of records
     * @param append flag which show, do we need to add records to the end of list
     * @return structure with status, data(if not error) and message
     * @throws Exception in case if there's no access to create Datasource
     */
    public Result<Speaker> createSpeaker(List<Speaker> list, boolean append) throws Exception;

    /**
     * Creates Event Record in DataSource
     * @param list list of records
     * @param append flag which show, do we need to add records to the end of list
     * @return structure with status, data(if not error) and message
     * @throws Exception in case if there's no access to create Datasource
     */
    public Result<Event> createEvent(List<Event> list, boolean append) throws Exception;

    /**
     * Creates Zone Record in DataSource
     * @param list list of records
     * @param append flag which show, do we need to add records to the end of list
     * @return structure with status, data(if not error) and message
     * @throws Exception in case if there's no access to create Datasource
     */
    public Result<Zone> createZone(List<Zone> list, boolean append) throws Exception;

    /**
     * Creates Channel Record in DataSource
     * @param list list of records
     * @param append flag which show, do we need to add records to the end of list
     * @return structure with status, data(if not error) and message
     * @throws Exception in case if there's no access to create Datasource or Datasource is in use
     */
    public Result<Channel> createChannel(List<Channel> list, boolean append) throws Exception;

    /**
     * Retrieves collection of Admins
     * @return structure with status, data(if not error) and message
     * @throws Exception in case if there's problem with connection to DataSource
     * (DB in use, file is blocked(permission denied))
     */
    public Result<Admin> getAdmins() throws Exception;

    /**
     * Retrieves collection of Managers
     * @return structure with status, data(if not error) and message
     * @throws Exception in case if there's problem with connection to DataSource
     * (DB in use, file is blocked(permission denied))
     */
    public Result<Manager> getManagers() throws Exception;

    /**
     * Retrieves collection of Speakers
     * @return structure with status, data(if not error) and message
     * @throws Exception in case if there's problem with connection to DataSource
     * (DB in use, file is blocked(permission denied))
     */
    public Result<Speaker> getSpeakers() throws Exception;

    /**
     * Retrieves collection of Events
     * @return structure with status, data(if not error) and message
     * @throws Exception in case if there's problem with connection to DataSource
     * (DB in use, file is blocked(permission denied))
     */
    public Result<Event> getEvents() throws Exception;

    /**
     * Retrieves collection of Zones
     * @return structure with status, data(if not error) and message
     * @throws Exception in case if there's problem with connection to DataSource
     * (DB in use, file is blocked(permission denied))
     */
    public Result<Zone> getZones() throws Exception;

    /**
     * Retrieves collection of Channels
     * @return structure with status, data(if not error) and message
     * @throws Exception in case if there's problem with connection to DataSource
     * (DB in use, file is blocked(permission denied))
     */
    public Result<Channel> getChannels() throws Exception;

    /**
     * Retrieve Admin with given id
     * @param id to find
     * @return structure with status, data(if not error) and message
     * @throws Exception in case if there's problem with connection to DataSource
     * (DB in use, file is blocked(permission denied))
     */
    public Result<Admin> getAdminById(Long id) throws Exception;

    /**
     * Retrieve Manager with given id
     * @param id to find
     * @return structure with status, data(if not error) and message
     * @throws Exception in case if there's problem with connection to DataSource
     * (DB in use, file is blocked(permission denied))
     */
    public Result<Manager> getManagerById(Long id) throws Exception;

    /**
     * Retrieve Speaker with given id
     * @param id to find
     * @return structure with status, data(if not error) and message
     * @throws Exception in case if there's problem with connection to DataSource
     * (DB in use, file is blocked(permission denied))
     */
    public Result<Speaker> getSpeakerById(Long id) throws Exception;

    /**
     * Retrieve Event with given id
     * @param id to find
     * @return structure with status, data(if not error) and message
     * @throws Exception in case if there's problem with connection to DataSource
     * (DB in use, file is blocked(permission denied))
     */
    public Result<Event> getEventById(Long id) throws Exception;

    /**
     * Retrieve Zone with given id
     * @param id to find
     * @return structure with status, data(if not error) and message
     * @throws Exception in case if there's problem with connection to DataSource
     * (DB in use, file is blocked(permission denied))
     */
    public Result<Zone> getZoneById(Long id) throws Exception;

    /**
     * Retrieve Channel with given id
     * @param id to find
     * @return structure with status, data(if not error) and message
     * @throws Exception in case if there's problem with connection to DataSource
     * (DB in use, file is blocked(permission denied))
     */
    public Result<Channel> getChannelById(Long id) throws Exception;

    /**
     * Remove Admin record with given id from DataSource
     * @param id to delete
     * @return structure with status, data(if not error) and message
     * @throws Exception in case if there's problem with connection to DataSource
     * (DB in use, file is blocked(permission denied))
     */
    public Result<Admin> deleteAdmin(Long id) throws Exception;

    /**
     * Remove Manager record with given id from DataSource
     * @param id to delete
     * @return structure with status, data(if not error) and message
     * @throws Exception in case if there's problem with connection to DataSource
     * (DB in use, file is blocked(permission denied))
     */
    public Result<Manager> deleteManager(Long id) throws Exception;

    /**
     * Remove Speaker record with given id from DataSource
     * @param id to delete
     * @return structure with status, data(if not error) and message
     * @throws Exception in case if there's problem with connection to DataSource
     * (DB in use, file is blocked(permission denied))
     */
    public Result<Speaker> deleteSpeaker(Long id) throws Exception;

    /**
     * Remove Event record with given id from DataSource
     * @param id to delete
     * @return structure with status, data(if not error) and message
     * @throws Exception in case if there's problem with connection to DataSource
     * (DB in use, file is blocked(permission denied))
     */
    public Result<Event> deleteEvent(Long id) throws Exception;

    /**
     * Remove Zone record with given id from DataSource
     * @param id to delete
     * @return structure with status, data(if not error) and message
     * @throws Exception in case if there's problem with connection to DataSource
     * (DB in use, file is blocked(permission denied))
     */
    public Result<Zone> deleteZone(Long id) throws Exception;

    /**
     * Remove Channel record with given id from DataSource
     * @param id to delete
     * @return structure with status, data(if not error) and message
     * @throws Exception in case if there's problem with connection to DataSource
     * (DB in use, file is blocked(permission denied))
     */
    public Result<Channel> deleteChannel(Long id) throws Exception;

    /**
     * Updates Admin Record
     * @param id to find to update
     * @param newAdmin new record data
     * @return structure with status, data(if not error) and message
     * @throws Exception in case if there's problem with connection to DataSource
     * (DB in use, file is blocked(permission denied))
     */
    public Result<Admin> updateAdmin(Long id, Admin newAdmin) throws Exception;

    /**
     * Updates Manager Record
     * @param id to find to update
     * @param newManager new record data
     * @return structure with status, data(if not error) and message
     * @throws Exception in case if there's problem with connection to DataSource
     * (DB in use, file is blocked(permission denied))
     */
    public Result<Manager> updateManager(Long id, Manager newManager) throws Exception;

    /**
     * Updates Speaker Record
     * @param id to find to update
     * @param newSpeaker new record data
     * @return structure with status, data(if not error) and message
     * @throws Exception in case if there's problem with connection to DataSource
     * (DB in use, file is blocked(permission denied))
     */
    public Result<Speaker> updateSpeaker(Long id, Speaker newSpeaker) throws Exception;

    /**
     * Updates Event Record
     * @param id to find to update
     * @param newEvent new record data
     * @return structure with status, data(if not error) and message
     * @throws Exception in case if there's problem with connection to DataSource
     * (DB in use, file is blocked(permission denied))
     */
    public Result<Event> updateEvent(Long id, Event newEvent) throws Exception;

    /**
     * Updates Zone Record
     * @param id to find to update
     * @param newZone new record data
     * @return structure with status, data(if not error) and message
     * @throws Exception in case if there's problem with connection to DataSource
     * (DB in use, file is blocked(permission denied))
     */
    public Result<Zone> updateZone(Long id, Zone newZone) throws Exception;

    /**
     * Updates Channel Record
     * @param id to find to update
     * @param newChannel new record data
     * @return structure with status, data(if not error) and message
     * @throws Exception in case if there's problem with connection to DataSource
     * (DB in use, file is blocked(permission denied))
     */
    public Result<Channel> updateChannel(Long id, Channel newChannel) throws Exception;

    /**
     * Change Zone and it's related channels status
     * @param id Zone id which you want to change status
     * @param status new status
     * @return structure with status, data(if not error) and message
     * @throws Exception in case if there's problem with connection to DataSource
     * (DB in use, file is blocked(permission denied))
     */
    public Result<?> changeZoneStatus(Long id, boolean status) throws Exception;

    /**
     * Generic method which inserts records of any class
     * @param listRecord list of records
     * @param append flag which show, do we need to add records to the end of list
     * @param cn ClassName which help in work with DataProviders
     * @param <T> Generic type (any Class or type)
     * @return structure with status, data(if not error) and message
     * @throws Exception in case if there's problem with connection to DataSource
     * (DB in use, file is blocked(permission denied))
     */
    public <T> Result<T> insertRecord(List <T> listRecord, boolean append, Class<T> cn) throws Exception;

    /**
     * Generic method which gives you records from any DataSource
     * @param cn ClassName which lead the way, from where we need get data
     * @param <T> Generic Type (any Class or type)
     * @return structure with status, data(if not error) and message
     * @throws Exception in case if there's problem with connection to DataSource
     * (DB in use, file is blocked(permission denied))
     */
    public <T> Result<T> getRecords (Class cn) throws Exception;

    /**
     * Generic method which gives you record from any DataSource with given id
     * @param id to find
     * @param cn ClassName which lead the way, from where we need get data
     * @param <T> Generic Type (any Class or type)
     * @return structure with status, data(if not error) and message
     * @throws Exception in case if there's problem with connection to DataSource
     * (DB in use, file is blocked(permission denied))
     */
    public <T> Result getRecordById(Long id, Class<T> cn) throws Exception;

    /**
     * Generic method which delete record from any DataSource
     * @param id to find
     * @param cn ClassName which lead the way, from where we need get data
     * @param <T> Generic Type (any Class or type)
     * @return structure with status, data(if not error) and message
     * @throws Exception in case if there's problem with connection to DataSource
     * (DB in use, file is blocked(permission denied))
     */
    public <T> Result deleteRecord(Long id, Class<T> cn) throws Exception;

    /**
     * Generic Method which updates any record from any DataSource
     * @param id to find to update
     * @param record new record data
     * @param <T> Generic Type (any Class or type)
     * @return structure with status, data(if not error) and message
     * @throws Exception in case if there's problem with connection to DataSource
     * (DB in use, file is blocked(permission denied))
     */
    public <T> Result updateRecord(Long id, T record) throws Exception;
}
