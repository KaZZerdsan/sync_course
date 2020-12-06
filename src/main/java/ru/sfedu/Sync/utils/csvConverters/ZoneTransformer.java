package ru.sfedu.Sync.utils.csvConverters;

import com.opencsv.bean.AbstractBeanField;
import ru.sfedu.Sync.models.Channel;
import ru.sfedu.Sync.models.Zone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ZoneTransformer extends AbstractBeanField {

    private String fieldsDelimiter = "~";
    private String elemDelimiter = "_";

    private ChannelTransformer channelTransformer = new ChannelTransformer();

    @Override
    protected Object convert(String value) {
        List<Zone> zoneList = new ArrayList<>();
        List<String> elemStrings = Arrays.asList(value.split(elemDelimiter));
        elemStrings
                .forEach(el -> {
                    Zone zone = new Zone();
                    String[] parsedArgs = el.split(fieldsDelimiter);
                    zone.setId(Integer.parseInt(parsedArgs[0]));
                    zone.setName(parsedArgs[1]);
                    zone.setDateStart(Integer.parseInt(parsedArgs[2]));
                    zone.setDateEnd(Integer.parseInt(parsedArgs[3]));
                    zone.setChannelList((List<Channel>) channelTransformer.convert(parsedArgs[4]));
                    zone.setStatus(Boolean.parseBoolean(parsedArgs[5]));
                    zoneList.add(zone);
                });
        return zoneList;
    }

    @Override
    public String convertToWrite(Object value) {
        List<Zone> zoneList = (List<Zone>) value;
        List<String> stringList = zoneList
                .stream()
                .map(el -> String
                        .format("%d" + fieldsDelimiter +
                                "%s" + fieldsDelimiter +
                                "%d" + fieldsDelimiter +
                                "%d" + fieldsDelimiter +
                                "%s" + fieldsDelimiter +
                                "%b",
                                el.getId(), el.getName(),
                                el.getDateStart(), el.getDateEnd(),
                                channelTransformer.convertToWrite(el.getChannelList()),
                                el.getStatus()))
                .collect(Collectors.toList());
        return String.join(elemDelimiter, stringList);
    }
}
