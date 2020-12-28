package ru.sfedu.Sync.models;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvNumber;
import org.simpleframework.xml.Element;

import javax.xml.bind.annotation.XmlElement;

public class Manager {
    @XmlElement(type = Long.class)
    @CsvBindByName(required = true)
    private long id;

    @CsvBindByName(required = true)
    private String name;

    public Long getId() {
        return id;
    }

    public Manager() {}

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Manager{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
