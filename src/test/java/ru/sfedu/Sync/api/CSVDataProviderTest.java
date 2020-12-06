package ru.sfedu.Sync.api;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import ru.sfedu.Sync.api.generators.GenerateEntity;
import ru.sfedu.Sync.models.*;
import ru.sfedu.Sync.utils.Result;
import ru.sfedu.Sync.utils.ResultType;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class CSVDataProviderTest {

    private CSVDataProvider dp = new CSVDataProvider();

    private Logger log = LogManager.getLogger(this.getClass());

    public CSVDataProviderTest() throws IOException {
    }

    private void showMethodName() {
        log.info("\n*** " + Thread.currentThread().getStackTrace()[2].getMethodName() + " ***");
    }

    @Test
    public void insertRecord() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        Result res;
        File file;

        showMethodName();

        log.debug("Inserting Admins");
        List<Admin> adminList = GenerateEntity.generateAdmins(10);
        res = dp.insertRecord(adminList, true, Admin.class);
        file = new File(dp.getPath(Admin.class));
        log.info(res.getMessage());
        Assert.assertTrue(file.exists());
        Assert.assertNotNull(res);

        log.debug("Inserting Managers");
        List<Manager> managerList = GenerateEntity.generateManagers(10);
        res = dp.insertRecord(managerList, true, Manager.class);
        file = new File(dp.getPath(Manager.class));
        log.info(res.getMessage());
        Assert.assertTrue(file.exists());
        Assert.assertNotNull(res);

        log.debug("Inserting Speakers");
        List<Speaker> speakerList = GenerateEntity.generateSpeakers(10);
        res = dp.insertRecord(speakerList, true, Speaker.class);
        file = new File(dp.getPath(Speaker.class));
        log.info(res.getMessage());
        Assert.assertTrue(file.exists());
        Assert.assertNotNull(res);

        log.debug("Inserting Channels");
        List<Channel> channelList = GenerateEntity.generateChannels(10, 5);
        res = dp.insertRecord(channelList, true, Channel.class);
        file = new File(dp.getPath(Channel.class));
        log.info(res.getMessage());
        Assert.assertTrue(file.exists());
        Assert.assertNotNull(res);

        log.debug("Inserting Zones");
        List<Zone> zoneList = GenerateEntity.generateZones(10, 5);
        res = dp.insertRecord(zoneList, true, Zone.class);
        file = new File(dp.getPath(Zone.class));
        log.info(res.getMessage());
        Assert.assertTrue(file.exists());
        Assert.assertNotNull(res);

        log.debug("Inserting Event");
        List<Event> eventList = GenerateEntity.generateEvents(10, 5);
        res = dp.insertRecord(eventList, true, Event.class);
        file = new File(dp.getPath(Event.class));
        log.info(res.getMessage());
        Assert.assertTrue(file.exists());
        Assert.assertNotNull(res);
    }

    @Test
    public void getRecords() throws IOException {
        Result res;

        showMethodName();

        log.debug("Get Administrators");
        res = dp.getRecords(Admin.class);
        log.info(res.getMessage());
        log.info("\n" + res.getData() + "\n");
        Assert.assertNotNull(res);

        log.debug("Get Managers");
        res = dp.getRecords(Manager.class);
        log.info(res.getMessage());
        log.info("\n" + res.getData() + "\n");
        Assert.assertNotNull(res);
        log.info(res.getMessage());

        log.debug("Get Speakers");
        res = dp.getRecords(Speaker.class);
        log.info(res.getMessage());
        log.info("\n" + res.getData() + "\n");
        Assert.assertNotNull(res);

        log.debug("Get Channels");
        res = dp.getRecords(Channel.class);
        log.info(res.getMessage());
        log.info("\n" + res.getData() + "\n");
        Assert.assertNotNull(res);

        log.debug("Get Zones");
        res = dp.getRecords(Zone.class);
        log.info(res.getMessage());
        log.info("\n" + res.getData() + "\n");
        Assert.assertNotNull(res);

        log.debug("Get Events");
        res = dp.getRecords(Event.class);
        log.info(res.getMessage());
        log.info("\n" + res.getData() + "\n");
        Assert.assertNotNull(res);
    }

    @Test
    public void getRecordById() throws IOException {
        int id;
        Result res;
        showMethodName();

        id = 1;
        log.info("Id to found Admin: " + id);
        res = dp.getRecordById(id, Admin.class);
        log.info(res.getData());
        Assert.assertNotNull(res);

        id = 1;
        log.info("Id to found Manager: " + id);
        res = dp.getRecordById(id, Manager.class);
        log.info(res.getData());
        Assert.assertNotNull(res);

        id = 1;
        log.info("Id to found Speaker: " + id);
        res = dp.getRecordById(id, Speaker.class);
        log.info(res.getData());
        Assert.assertNotNull(res);

        id = 1;
        log.info("Id to found Channel: " + id);
        res = dp.getRecordById(id, Channel.class);
        log.info(res.getData());
        Assert.assertNotNull(res);

        id = 1;
        log.info("Id to found Zone: " + id);
        res = dp.getRecordById(id, Zone.class);
        log.info(res.getData());
        Assert.assertNotNull(res);

        id = 1;
        log.info("Id to found Event: " + id);
        res = dp.getRecordById(id, Event.class);
        log.info(res.getData());
        Assert.assertNotNull(res);
    }

    @Test
    public void deleteRecord() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        showMethodName();
        int id;
        Result res;

        id = 8;
        log.info("Id to delete Admin: " + id);
        res = dp.deleteRecord(id, Admin.class);
        log.info(res.getMessage());
        Assert.assertNotNull(res);

        id = 8;
        log.info("Id to delete Manager: " + id);
        res = dp.deleteRecord(id, Manager.class);
        log.info(res.getMessage());
        Assert.assertNotNull(res);

        id = 8;
        log.info("Id to delete Speaker: " + id);
        res = dp.deleteRecord(id, Speaker.class);
        log.info(res.getMessage());
        Assert.assertNotNull(res);

        id = 8;
        log.info("Id to delete Channel: " + id);
        res = dp.deleteRecord(id, Channel.class);
        log.info(res.getMessage());
        Assert.assertNotNull(res);

        id = 8;
        log.info("Id to delete Zone: " + id);
        res = dp.deleteRecord(id, Zone.class);
        log.info(res.getMessage());
        Assert.assertNotNull(res);

        id = 8105187;
        log.info("Id to delete Event: " + id);
        res = dp.deleteRecord(id, Event.class);
        log.info(res.getMessage());
        Assert.assertNotNull(res);
    }

    @Test
    public <T extends User> void updateRecord() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        Result res;

        showMethodName();

        log.info("Updating Admin");
        Admin admin = GenerateEntity.generateAdmins(1).get(0);
        res = dp.updateRecord(admin);
        log.info(res.getMessage());
        if (res.getResultType() == ResultType.ERROR) {
            Assert.assertEquals(res.getResultType(), ResultType.ERROR);
            return;
        }
        Assert.assertEquals(res.getResultType(), ResultType.OK);

        log.info("Updating Manager");
        Manager manager = GenerateEntity.generateManagers(1).get(0);
        res = dp.updateRecord(manager);
        log.info(res.getMessage());
        if (res.getResultType() == ResultType.ERROR) {
            Assert.assertEquals(res.getResultType(), ResultType.ERROR);
            return;
        }
        Assert.assertEquals(res.getResultType(), ResultType.OK);

        log.info("Updating Speaker");
        Speaker speaker = GenerateEntity.generateSpeakers(1).get(0);
        res = dp.updateRecord(speaker);
        log.info(res.getMessage());
        if (res.getResultType() == ResultType.ERROR) {
            Assert.assertEquals(res.getResultType(), ResultType.ERROR);
            return;
        }
        Assert.assertEquals(res.getResultType(), ResultType.OK);

        log.info("Updating Channel");
        Channel channel = GenerateEntity.generateChannels(1, 2).get(0);
        res = dp.updateRecord(channel);
        log.info(res.getMessage());
        if (res.getResultType() == ResultType.ERROR) {
            Assert.assertEquals(res.getResultType(), ResultType.ERROR);
            return;
        }
        Assert.assertEquals(res.getResultType(), ResultType.OK);

        log.info("Updating Zone");
        Zone zone = GenerateEntity.generateZones(1, 5).get(0);
        res = dp.updateRecord(zone);
        log.info(res.getMessage());
        if (res.getResultType() == ResultType.ERROR) {
            Assert.assertEquals(res.getResultType(), ResultType.ERROR);
            return;
        }
        Assert.assertEquals(res.getResultType(), ResultType.OK);

        log.info("Updating Event");
        Event event = GenerateEntity.generateEvents(1, 5).get(0);
        res = dp.updateRecord(event);
        log.info(res.getMessage());
        if (res.getResultType() == ResultType.ERROR) {
            Assert.assertEquals(res.getResultType(), ResultType.ERROR);
            return;
        }
        Assert.assertEquals(res.getResultType(), ResultType.OK);
    }

    @Test
    public void changeZoneStatus() throws NoSuchMethodException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException {
        boolean status = true;
        int id = 9348903;
        dp.changeZoneStatus(id, status);
    }
}