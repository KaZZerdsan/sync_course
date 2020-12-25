package ru.sfedu.Sync.models;

import com.opencsv.bean.CsvBindByName;

import javax.xml.bind.annotation.XmlElement;

public abstract class BaseClass {
    @CsvBindByName
    @XmlElement(name="id")
    private Long id;

    @CsvBindByName
    @XmlElement(name="name")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
        return  "id=" + id + ", name='" + name + '\'';
    }
}
