package ru.sfedu.Sync.utils.csvConverters;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.bean.AbstractCsvConverter;
import ru.sfedu.Sync.models.Channel;
import ru.sfedu.Sync.models.Speaker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ChannelTransformer extends AbstractBeanField {

    private String fieldsDelimiter = "`";
    private String elemDelimiter = "&";

    private SpeakerTransformer spkTrans = new SpeakerTransformer();

    @Override
    public Object convert(String value) {
        List<Channel> channelList = new ArrayList<>();
        List<String> elemStrings = Arrays.asList(value.split(elemDelimiter));
        elemStrings.stream().forEach(el -> {
            Channel channel = new Channel();
            String[] parsedData = el.split(fieldsDelimiter);
            channel.setId(Integer.parseInt(parsedData[0]));
            channel.setName(parsedData[1]);
            channel.setLanguage(parsedData[2]);
            channel.setStatus(Boolean.parseBoolean(parsedData[3]));
            channel.setSpeakers((List<Speaker>) spkTrans.convert(parsedData[4]));
            channelList.add(channel);
        });
        return channelList;
    }

    @Override
    public String convertToWrite(Object value) {
        List<Channel> speakerList = (List<Channel>) value;
        List<String> stringList = speakerList
                .stream()
                .map(el -> String
                        .format("%d" + fieldsDelimiter +
                                "%s" + fieldsDelimiter +
                                "%s" + fieldsDelimiter +
                                "%b" + fieldsDelimiter +
                                "%s",
                                el.getId(), el.getName(),
                                el.getLanguage(), el.getStatus(),
                                spkTrans.convertToWrite(el.getSpeakers()
                            )))
                .collect(Collectors.toList());
        return String.join(elemDelimiter, stringList);
    }
}
