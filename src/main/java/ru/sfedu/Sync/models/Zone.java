package ru.sfedu.Sync.models;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import com.opencsv.bean.CsvCustomBindByPosition;
import ru.sfedu.Sync.utils.csvConverters.ChannelTransformer;

import java.util.List;
import java.util.Objects;

public class Zone extends BaseClass {
    @CsvBindByName
    private int dateStart;

    @CsvBindByName
    private int dateEnd;

    @CsvCustomBindByName(converter = ChannelTransformer.class)
    private List<Channel> channelList;

    @CsvBindByName
    private boolean status;

    public int getDateStart() {
        return dateStart;
    }

    public void setDateStart(int dateStart) {
        this.dateStart = dateStart;
    }

    public int getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(int dateEnd) {
        this.dateEnd = dateEnd;
    }

    public List<Channel> getChannelList() {
        return channelList;
    }

    public void setChannelList(List<Channel> channelList) {
        this.channelList = channelList;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
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
                super.toString() +
                ", dateStart=" + dateStart +
                ", dateEnd=" + dateEnd +
                ", channelList=" + channelList +
                ", status=" + status +
                '}';
    }
}
