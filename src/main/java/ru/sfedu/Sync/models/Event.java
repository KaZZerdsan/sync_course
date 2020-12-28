package ru.sfedu.Sync.models;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import ru.sfedu.Sync.utils.csvConverters.ManagerConverter;
import ru.sfedu.Sync.utils.csvConverters.ZoneTransformer;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;
import java.util.Objects;

public class Event {
    @XmlElement(type = Long.class)
    @CsvBindByName(required = true)
    private long id;

    @CsvBindByName(required = true)
    private String name;

    @CsvCustomBindByName(required = true, converter = ManagerConverter.class)
    private Manager manager;
    @CsvCustomBindByName(required = true, converter = ZoneTransformer.class)
    private List<Zone> zoneList;

    public Event() {}

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

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }


    public List<Zone> getZoneList() {
        return zoneList;
    }

    public void setZoneList(List<Zone> zoneList) {
        this.zoneList = zoneList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(manager, event.manager) &&
                Objects.equals(zoneList, event.zoneList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(manager, zoneList);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", manager=" + manager +
                ", zoneList=" + zoneList +
                '}';
    }
}
