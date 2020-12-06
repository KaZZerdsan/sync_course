package ru.sfedu.Sync.models;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import ru.sfedu.Sync.utils.csvConverters.SpeakerTransformer;

import java.util.List;
import java.util.Objects;

public class Channel extends BaseClass {
    @CsvBindByName
    private String language;

    @CsvBindByName
    private boolean status;

    @CsvCustomBindByName(converter = SpeakerTransformer.class)
    private List<Speaker> speakers;

    public Channel() {
        super();
    }


    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<Speaker> getSpeakers() {
        return speakers;
    }

    public void setSpeakers(List<Speaker> speakers) {
        this.speakers = speakers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Channel channel = (Channel) o;
        return Objects.equals(language, channel.language) &&
                Objects.equals(speakers, channel.speakers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(language, speakers);
    }

    @Override
    public String toString() {
        return "Channel{" +
                super.toString() +
                "language='" + language + '\'' +
                ", speakers=" + speakers +
                '}';
    }
}
