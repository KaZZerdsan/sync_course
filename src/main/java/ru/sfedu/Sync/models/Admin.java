package ru.sfedu.Sync.models;

import com.opencsv.bean.CsvBindByName;

import javax.xml.bind.annotation.XmlElement;

public class Admin {
    @XmlElement(type = Long.class)
    @CsvBindByName(required = true)
    private long id;

    @CsvBindByName(required = true)
    private String name;

    public Admin() {}
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

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
