package ru.sfedu.Sync.models;

import com.opencsv.bean.CsvBindByName;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class User extends BaseClass implements Serializable {

    @CsvBindByName
    @XmlElement(name="age")
    private int age;

    public User() {
        super();
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
        return getId() == user.getId() &&
                age == user.age &&
                getName().equals(user.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), age);
    }

    @Override
    public String toString() {
        return "\n\t" +
                "User{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", age=" + age +
                '}' +
                '\n';
    }
}


