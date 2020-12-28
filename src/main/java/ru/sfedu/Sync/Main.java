package ru.sfedu.Sync;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.Sync.api.*;
import ru.sfedu.Sync.models.*;
import ru.sfedu.Sync.utils.Constants;
import ru.sfedu.Sync.utils.GenerateEntity;
import ru.sfedu.Sync.utils.Result;
import ru.sfedu.Sync.utils.ResultType;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Main {

    public static final Logger log = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        checkArgsCount(args);
        IDataProvider dp = getDataProvider(args[0]);
        switch(args[1].toUpperCase(Locale.ROOT)) {
            case Constants.CREATE_ADMIN: {
                Admin admin = new Admin();
                Boolean append = Boolean.valueOf(args[2]);
                admin.setId(Long.valueOf(args[3]));
                admin.setName(args[4]);
                List<Admin> list = new ArrayList<>();
                list.add(admin);
                Result<Admin> res = dp.createAdmin(list, append);
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.CREATE_MANAGER: {
                Manager manager = new Manager();
                Boolean append = Boolean.valueOf(args[2]);
                manager.setId(Long.valueOf(args[3]));
                manager.setName(args[4]);
                List<Manager> list = new ArrayList<>();
                list.add(manager);
                Result<Manager> res = dp.createManager(list, append);
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.CREATE_SPEAKER: {
                Speaker speaker = new Speaker();
                Boolean append = Boolean.valueOf(args[2]);
                speaker.setId(Long.valueOf(args[3]));
                speaker.setName(args[4]);
                List<Speaker> list = new ArrayList<>();
                list.add(speaker);
                Result<Speaker> res = dp.createSpeaker(list, append);
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.CREATE_EVENT: {
                Event event = new Event();
                Boolean append = Boolean.valueOf(args[2]);
                event.setId(Long.valueOf(args[3]));
                event.setName(args[4]);
                event.setManager(dp.getManagerById(Long.parseLong(args[5])).getData().get(0));
                List<Zone> zoneList = new ArrayList<>();
                if (args.length > 6) {
                    for (String arg : Arrays.copyOfRange(args, 6, args.length)) {
                        Result<Zone> res = dp.getZoneById(Long.valueOf(arg));
                        if (res.getResultType() == ResultType.OK) {
                            zoneList.addAll(res.getData());
                        }
                    }
                }
                event.setZoneList(zoneList);
                List<Event> eventList = new ArrayList<>();
                Result<Event> res = dp.createEvent(eventList, append);
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.CREATE_ZONE: {
                Zone zone = new Zone();
                Boolean append = Boolean.valueOf(args[2]);
                zone.setId(Long.valueOf(args[3]));
                zone.setName(args[4]);
                zone.setStatus(Boolean.valueOf(args[5]));
                zone.setDateStart(Long.valueOf(args[6]));
                zone.setDateEnd(Long.valueOf(args[7]));
                List<Channel> channelList = new ArrayList<>();
                if (args.length > 8) {
                    for (String arg : Arrays.copyOfRange(args, 8, args.length)) {
                        Result<Channel> res = dp.getChannelById(Long.parseLong(arg));
                        if (res.getResultType() == ResultType.OK) {
                            channelList.addAll(res.getData());
                        }
                    }
                }
                zone.setChannelList(channelList);
                List<Zone> zoneList = new ArrayList<>();
                zoneList.add(zone);
                Result<Zone> res = dp.createZone(zoneList, append);
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.CREATE_CHANNEL: {
                Channel channel = new Channel();
                Boolean append = Boolean.valueOf(args[2]);
                channel.setId(Long.valueOf(args[3]));
                channel.setName(args[4]);
                channel.setStatus(Boolean.valueOf(args[5]));
                channel.setLanguage(args[6]);
                List<Speaker> speakerList = new ArrayList<>();
                if (args.length > 6) {
                    for (String arg : Arrays.copyOfRange(args, 6, args.length)) {
                        Result<Speaker> res = dp.getSpeakerById(Long.valueOf(arg));
                        if (res.getResultType() == ResultType.OK) {
                            speakerList.addAll(res.getData());
                        }
                    }
                }
                channel.setSpeakers(speakerList);
                List<Channel> channelList = new ArrayList<>();
                channelList.add(channel);
                Result<Channel> res = dp.createChannel(channelList, append);
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.GET_ADMINS: {
                Result<Admin> res = dp.getAdmins();
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.GET_MANAGERS: {
                Result<Manager> res = dp.getManagers();
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.GET_SPEAKERS: {
                Result<Speaker> res = dp.getSpeakers();
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.GET_EVENTS: {
                Result<Event> res = dp.getEvents();
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.GET_ZONES: {
                Result<Zone> res = dp.getZones();
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.GET_CHANNELS: {
                Result<Channel> res = dp.getChannels();
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.GET_ADMIN_BY_ID: {
                Result<Admin> res = dp.getAdminById(Long.valueOf(args[2]));
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.GET_MANAGER_BY_ID: {
                Result<Manager> res = dp.getManagerById(Long.valueOf(args[2]));
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.GET_SPEAKER_BY_ID: {
                Result<Speaker> res = dp.getSpeakerById(Long.valueOf(args[2]));
                log.info(res.getMessage());
                log.info(res.getData());

                break;
            }
            case Constants.GET_EVENT_BY_ID: {
                Result<Event> res = dp.getEventById(Long.valueOf(args[2]));
                log.info(res.getMessage());
                log.info(res.getData());

                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.GET_ZONE_BY_ID: {
                Result<Zone> res = dp.getZoneById(Long.valueOf(args[2]));
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.GET_CHANNEL_BY_ID: {
                Result<Channel> res = dp.getChannelById(Long.valueOf(args[2]));
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.DELETE_ADMIN: {
                Result<Admin> res = dp.deleteAdmin(Long.valueOf(args[2]));
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.DELETE_MANAGER: {
                Result<Manager> res = dp.deleteManager(Long.valueOf(args[2]));
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.DELETE_SPEAKER: {
                Result<Speaker> res = dp.deleteSpeaker(Long.valueOf(args[2]));
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.DELETE_EVENT: {
                Result<Event> res = dp.deleteEvent(Long.valueOf(args[2]));
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.DELETE_ZONE: {
                Result<Zone> res = dp.deleteZone(Long.valueOf(args[2]));
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.DELETE_CHANNEL: {
                Result<Channel> res = dp.deleteChannel(Long.valueOf(args[2]));
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.UPDATE_ADMIN: {
                Admin admin = new Admin();
                admin.setId(Long.valueOf(args[2]));
                admin.setName(args[3]);
                Result<Admin> res = dp.updateAdmin(admin.getId(), admin);
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.UPDATE_MANAGER: {
                Manager manager = new Manager();
                manager.setId(Long.valueOf(args[2]));
                manager.setName(args[3]);
                Result<Manager> res = dp.updateManager(manager.getId(), manager);
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.UPDATE_SPEAKER: {
                Speaker speaker = new Speaker();
                speaker.setId(Long.valueOf(args[2]));
                speaker.setName(args[3]);
                Result<Speaker> res = dp.updateSpeaker(speaker.getId(), speaker);
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.UPDATE_EVENT: {
                Event event = new Event();
                event.setId(Long.valueOf(args[2]));
                event.setName(args[3]);
                event.setManager(dp.getManagerById(Long.parseLong(args[4])).getData().get(0));
                List<Zone> zoneList = new ArrayList<>();
                if (args.length > 5) {
                    for (String arg : Arrays.copyOfRange(args, 5, args.length)) {
                        Result<Zone> res = dp.getZoneById(Long.valueOf(arg));
                        if (res.getResultType() == ResultType.OK) {
                            zoneList.addAll(res.getData());
                        }
                    }
                }
                event.setZoneList(zoneList);
                Result<Event> res = dp.updateEvent(event.getId(), event);
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.UPDATE_ZONE: {
                Zone zone = new Zone();
                zone.setId(Long.valueOf(args[2]));
                zone.setName(args[3]);
                zone.setStatus(Boolean.valueOf(args[4]));
                zone.setDateStart(Long.valueOf(args[5]));
                zone.setDateEnd(Long.valueOf(args[6]));
                List<Channel> channelList = new ArrayList<>();
                if (args.length > 7) {
                    for (String arg : Arrays.copyOfRange(args, 7, args.length)) {
                        Result<Channel> res = dp.getChannelById(Long.parseLong(arg));
                        if (res.getResultType() == ResultType.OK) {
                            channelList.addAll(res.getData());
                        }
                    }
                }
                zone.setChannelList(channelList);
                Result<Zone> res = dp.updateZone(zone.getId(), zone);
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.UPDATE_CHANNEL: {
                Channel channel = new Channel();
                channel.setId(Long.valueOf(args[2]));
                channel.setName(args[3]);
                channel.setStatus(Boolean.valueOf(args[4]));
                channel.setLanguage(args[5]);
                List<Speaker> speakerList = new ArrayList<>();
                if (args.length > 6) {
                    for (String arg : Arrays.copyOfRange(args, 6, args.length)) {
                        Result<Speaker> res = dp.getSpeakerById(Long.valueOf(arg));
                        if (res.getResultType() == ResultType.OK) {
                            speakerList.addAll(res.getData());
                        }
                    }
                }
                channel.setSpeakers(speakerList);
                Result<Channel> res = dp.updateChannel(channel.getId(), channel);
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            case Constants.CHANGE_ZONE_STATUS: {
                Long id = Long.valueOf(args[2]);
                Boolean status = Boolean.valueOf(args[3]);
                Result<?> res = dp.changeZoneStatus(id, status);
                log.info(res.getMessage());
                log.info(res.getData());
                break;
            }
            default: {
                log.error(Constants.INVALID_METHOD_NAME);
                System.exit(0);
            }
        }

    }

    private static IDataProvider getDataProvider(String dpType) throws IOException {
        switch(dpType.toUpperCase(Locale.ROOT)) {
            case(Constants.CSV): {
                return new CSVDataProvider();
            }
            case(Constants.XML): {
                return new SimpleXMLDataProvider();
            }
            case(Constants.JAXB): {
                return new JAXBDataProvider();
            }
            case(Constants.DB): {
                return new JDBCDataProvider();
            }
            default: {
                log.error(Constants.INVALID_DATA_PROVIDER);
                System.exit(0);
            }
        }
        return null;
    }

    private static void checkArgsCount(String[] args) {
        if (args.length < 2) {
            log.error(Constants.FEW_ARGUMENTS);
            System.exit(0);
        }
    }

}
