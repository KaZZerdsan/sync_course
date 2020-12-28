package ru.sfedu.Sync.utils.csvConverters;

import com.opencsv.bean.AbstractBeanField;
import ru.sfedu.Sync.models.Speaker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class SpeakerTransformer extends AbstractBeanField {

    private String fieldsDelimiter = "@";
    private String elemDelimiter = "#";

    @Override
    protected Object convert(String value) {
        List<Speaker> speakerList = new ArrayList<>();
        List<String> stringList = Arrays.asList(value.split(elemDelimiter));
        stringList.stream().forEach(el -> {
            Speaker speaker = new Speaker();
            String[] data = el.split(fieldsDelimiter);
            speaker.setId(Long.parseLong(data[0]));
            speaker.setName(data[1]);
            speakerList.add(speaker);
        });
        return speakerList;
    }

    @Override
    public String convertToWrite(Object value) {
        List<Speaker> speakerList = (List<Speaker>) value;
        List<String> stringList = speakerList
                .stream()
                .map(el -> String.format("%d" + fieldsDelimiter + "%s", el.getId(), el.getName()))
                .collect(Collectors.toList()
                );
        return String.join(elemDelimiter, stringList);
    }

}
