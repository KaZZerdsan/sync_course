package ru.sfedu.Sync.models;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import com.opencsv.bean.CsvCustomBindByPosition;
import ru.sfedu.Sync.utils.csvConverters.ChannelTransformer;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;
import java.util.Objects;

public class Zone {
    @XmlElement(type = Long.class)
    @CsvBindByName(required = true)
    private long id;

    @CsvBindByName(required = true)
    private String name;

    @XmlElement(type = Long.class)
    @CsvBindByName(required = true)
    private Long dateStart;

    @XmlElement(type = Long.class)
    @CsvBindByName(required = true)
    private Long dateEnd;

    @CsvCustomBindByName(required = true, converter = ChannelTransformer.class)
    private List<Channel> channelList;

    @CsvBindByName(required = true)
    private Boolean status;

    public Zone() {}

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

    public Long getDateStart() {
        return dateStart;
    }

    public void setDateStart(Long dateStart) {
        this.dateStart = dateStart;
    }

    public Long getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Long dateEnd) {
        this.dateEnd = dateEnd;
    }

    public List<Channel> getChannelList() {
        return channelList;
    }

    public void setChannelList(List<Channel> channelList) {
        this.channelList = channelList;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Zone zone = (Zone) o;
        return dateStart == zone.dateStart &&
                dateEnd == zone.dateEnd &&
                status == zone.status &&
                Objects.equals(channelList, zone.channelList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateStart, dateEnd, channelList, status);
    }

    @Override
    public String toString() {
        return "Zone{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dateStart=" + dateStart +
                ", dateEnd=" + dateEnd +
                ", channelList=" + channelList +
                ", status=" + status +
                '}';
    }
}
