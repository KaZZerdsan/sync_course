package ru.sfedu.Sync.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.sfedu.Sync.models.*;
import ru.sfedu.Sync.utils.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class JDBCDataProviderTest {

    private Logger log = LogManager.getLogger(JDBCDataProviderTest.class);
    private JDBCDataProvider dp = new JDBCDataProvider();

    public JDBCDataProviderTest() throws IOException {
    }

    @Before
    public void flushData() throws IOException, SQLException {
        dp.flushTable(Event.class);
    }

    @Test
    public void createAdmin() throws Exception {
        List<Admin> adminList = GenerateEntity.generateAdmins(10);
        dp.createAdmin(adminList, false);
        Result<Admin> res = dp.getRecords(Admin.class);
        log.info(res.getMessage());
        Assert.assertEquals(adminList.size(), res.getData().size());
    }

    @Test
    public void createManager() throws Exception {
        List<Manager> managerList = GenerateEntity.generateManagers(10);
        dp.createManager(managerList, false);
        Result<Manager> res = dp.getRecords(Manager.class);
        Assert.assertEquals(managerList.size(), res.getData().size());
    }

    @Test
    public void createSpeaker() throws Exception {
        List<Speaker> speakerList = GenerateEntity.generateSpeakers(10);
        dp.createSpeaker(speakerList, false);
        Result<Manager> res = dp.getRecords(Speaker.class);
        Assert.assertEquals(speakerList.size(), res.getData().size());
    }

    @Test
    public void createEvent() throws Exception {
        List<Event> eventList = GenerateEntity.generateEvents(10, 5);
        dp.createEvent(eventList, false);
        Result<Event> res = dp.getRecords(Event.class);
        Assert.assertEquals(eventList.size(), res.getData().size());
    }

    @Test
    public void createZone() throws Exception {
        List<Zone> zoneList = GenerateEntity.generateZones(10, 5);
        dp.createZone(zoneList, false);
        Result<Zone> res = dp.getRecords(Zone.class);
        Assert.assertEquals(zoneList.size(), res.getData().size());
    }

    @Test
    public void createChannel() throws Exception {
        List<Channel> channelList = GenerateEntity.generateChannels(2, 2);
        dp.createChannel(channelList, false);
        Result<Channel> res = dp.getRecords(Channel.class);
        Assert.assertEquals(channelList.size(), res.getData().size());
    }

    @Test
    public void getAdmins() throws Exception {
        createAdmin();
        Assert.assertEquals(dp.getAdmins().getResultType(), ResultType.OK);
    }

    @Test
    public void getManagers() throws Exception {
        createManager();
        Assert.assertEquals(dp.getManagers().getResultType(), ResultType.OK);
    }

    @Test
    public void getSpeakers() throws Exception {
        createSpeaker();
        Assert.assertEquals(dp.getSpeakers().getResultType(), ResultType.OK);
    }

    @Test
    public void getChannels() throws Exception {
        createChannel();
        Assert.assertEquals(dp.getChannels().getResultType(), ResultType.OK);
    }

    @Test
    public void getZones() throws Exception {
        createZone();
        Assert.assertEquals(dp.getZones().getResultType(), ResultType.OK);
    }

    @Test
    public void getEvents() throws Exception {
        createEvent();
        Result<Event> res = dp.getEvents();
        log.info(res.getData());
        Assert.assertEquals(res.getResultType(), ResultType.OK);
    }

    @Test
    public void getAdminById() throws Exception {
        Admin record = GenerateEntity.generateAdmins(1).get(0);
        dp.createAdmin(Collections.singletonList(record), false);
        Assert.assertEquals(dp.getAdminById(record.getId()).getResultType(), ResultType.OK);
    }

    @Test
    public void getManagerById() throws Exception {
        Manager record = GenerateEntity.generateManagers(1).get(0);
        dp.createManager(Collections.singletonList(record), false);
        Assert.assertEquals(dp.getManagerById(record.getId()).getResultType(), ResultType.OK);
    }

    @Test
    public void getSpeakerById() throws Exception {
        Speaker record = GenerateEntity.generateSpeakers(1).get(0);
        dp.createSpeaker(Collections.singletonList(record), false);
        Assert.assertEquals(dp.getSpeakerById(record.getId()).getResultType(), ResultType.OK);
    }

    @Test
    public void getChannelById() throws Exception {
        Channel record = GenerateEntity.generateChannels(1, 2).get(0);
        dp.createChannel(Collections.singletonList(record), false);
        Assert.assertEquals(dp.getChannelById(record.getId()).getResultType(), ResultType.OK);
    }

    @Test
    public void getZoneById() throws Exception {
        Zone record = GenerateEntity.generateZones(1, 2).get(0);
        dp.createZone(Collections.singletonList(record), false);
        Assert.assertEquals(dp.getZoneById(record.getId()).getResultType(), ResultType.OK);
    }

    @Test
    public void getEventById() throws Exception {
        Event record = GenerateEntity.generateEvents(1, 2).get(0);
        dp.createEvent(Collections.singletonList(record), false);
        Assert.assertEquals(dp.getEventById(record.getId()).getResultType(), ResultType.OK);
    }

    @Test
    public void deleteAdmin() throws Exception {
        Admin record = GenerateEntity.generateAdmins(1).get(0);
        dp.createAdmin(Collections.singletonList(record), false);
        Assert.assertEquals(dp.deleteAdmin(record.getId()).getResultType(), ResultType.OK);
    }

    @Test
    public void deleteManager() throws Exception {
        Manager record = GenerateEntity.generateManagers(1).get(0);
        dp.createManager(Collections.singletonList(record), false);
        Assert.assertEquals(dp.deleteManager(record.getId()).getResultType(), ResultType.OK);
    }

    @Test
    public void deleteSpeaker() throws Exception {
        Speaker record = GenerateEntity.generateSpeakers(1).get(0);
        log.info(record.getId());
        dp.createSpeaker(Collections.singletonList(record), false);
        Assert.assertEquals(dp.deleteSpeaker(record.getId()).getResultType(), ResultType.OK);
    }

    @Test
    public void deleteChannel() throws Exception {
        Channel record = GenerateEntity.generateChannels(1, 2).get(0);
        dp.createChannel(Collections.singletonList(record), false);
        Assert.assertEquals(dp.deleteChannel(record.getId()).getResultType(), ResultType.OK);
    }

    @Test
    public void deleteZone() throws Exception {
        Zone record = GenerateEntity.generateZones(1, 2).get(0);
        dp.createZone(Collections.singletonList(record), false);
        Assert.assertEquals(dp.deleteZone(record.getId()).getResultType(), ResultType.OK);
    }

    @Test
    public void deleteEvent() throws Exception {
        Event record = GenerateEntity.generateEvents(1, 2).get(0);
        dp.createEvent(Collections.singletonList(record), false);
        Assert.assertEquals(dp.deleteEvent(record.getId()).getResultType(), ResultType.OK);
    }

    @Test
    public void changeZoneStatus() throws Exception {
        List<Zone> zoneList = GenerateEntity.generateZones(1, 2);
        dp.insertRecord(zoneList, false,  Zone.class);
        Zone zone = zoneList.get(0);
        log.info(zone.getStatus());
        log.info(zone);
        Assert.assertEquals(dp.changeZoneStatus(zone.getId(), !zone.getStatus()).getResultType(), ResultType.OK);
    }

    @Test
    public void updateAdmin() throws Exception {
        Long id = 120L;
        List<Admin> adminList = GenerateEntity.generateAdmins(2);
        adminList.get(0).setId(id);
        dp.createAdmin(adminList, false);
        Admin admin = adminList.get(0);
        admin.setName(admin.getName() + Constants.NEW);
        Assert.assertEquals(dp.updateAdmin(id, admin).getResultType(), ResultType.OK);
    }

    @Test
    public void updateManager() throws Exception {
        Long id = 120L;
        List<Manager> managerList = GenerateEntity.generateManagers(2);
        managerList.get(0).setId(id);
        dp.createManager(managerList, false);
        Manager manager = managerList.get(0);
        manager.setName(manager.getName() + Constants.NEW);
        Assert.assertEquals(dp.updateManager(id, manager).getResultType(), ResultType.OK);
    }

    @Test
    public void updateSpeaker() throws Exception {
        Long id = 120L;
        List<Speaker> speakerList = GenerateEntity.generateSpeakers(2);
        speakerList.get(0).setId(id);
        dp.createSpeaker(speakerList, false);
        Speaker speaker = speakerList.get(0);
        speaker.setName(speaker.getName() + Constants.NEW);
        Assert.assertEquals(dp.updateSpeaker(id, speaker).getResultType(), ResultType.OK);
    }

    @Test
    public void updateChannel() throws Exception {
        Long id = 120L;
        List<Channel> channelList = GenerateEntity.generateChannels(2, 1);
        channelList.get(0).setId(id);
        dp.createChannel(channelList, false);
        Channel channel = channelList.get(0);
        channel.setName(channel.getName() + Constants.NEW);
        Assert.assertEquals(dp.updateChannel(id, channel).getResultType(), ResultType.OK);
    }

    @Test
    public void updateZone() throws Exception {
        Long id = 120L;
        List<Zone> zoneList = GenerateEntity.generateZones(2, 1);
        zoneList.get(0).setId(id);
        dp.createZone(zoneList, false);
        Zone zone = zoneList.get(0);
        zone.setName(zone.getName() + Constants.NEW);
        Assert.assertEquals(dp.updateZone(id, zone).getResultType(), ResultType.OK);
    }

    @Test
    public void updateEvent() throws Exception {
        Long id = 120L;
        List<Event> eventList = GenerateEntity.generateEvents(2, 1);
        eventList.get(0).setId(id);
        dp.createEvent(eventList, false);
        Event event = eventList.get(0);
        event.setName(event.getName() + Constants.NEW);
        Assert.assertEquals(dp.updateEvent(id, event).getResultType(), ResultType.OK);
    }
}