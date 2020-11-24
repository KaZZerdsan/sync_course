package ru.sfedu.Sync.utils;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLWrapper <T> {

    @XmlElement
    private List<T> list;

    public XMLWrapper() {};

    public XMLWrapper(List<T> list) {
        this.list = list;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
