package ru.sfedu.Sync.models;

import com.opencsv.bean.CsvBindByName;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class User implements Serializable, BaseClass {

    @CsvBindByName
    @XmlElement(name="id")
    private int id;

    @CsvBindByName
    @XmlElement(name="name")
    private String name;

    @CsvBindByName
    @XmlElement(name="age")
    private int age;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                age == user.age &&
                name.equals(user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age);
    }

    @Override
    public String toString() {
        return "\n\t" +
                "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}' +
                '\n';
    }
}


