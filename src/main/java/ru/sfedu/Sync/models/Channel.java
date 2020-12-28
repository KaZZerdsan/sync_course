package ru.sfedu.Sync.models;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import ru.sfedu.Sync.utils.csvConverters.SpeakerTransformer;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;
import java.util.Objects;

public class Channel {
    @XmlElement(type = Long.class)
    @CsvBindByName(required = true)
    private long id;

    @CsvBindByName(required = true)
    private String name;

    @CsvBindByName(required = true)
    private String language;

    @CsvBindByName(required = true)
    private Boolean status;

    @CsvCustomBindByName(required = false, converter = SpeakerTransformer.class)
    private List<Speaker> speakers;

    public Channel() {}

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
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
                "id=" + id +
                ", name='" + name + '\'' +
                ", language='" + language + '\'' +
                ", status=" + status +
                ", speakers=" + speakers +
                '}';
    }
}
