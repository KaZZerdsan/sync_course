package ru.sfedu.Sync.utils.csvConverters;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import ru.sfedu.Sync.models.Manager;
import ru.sfedu.Sync.models.Zone;

import java.util.List;
import java.util.stream.Collectors;

public class ManagerConverter extends AbstractBeanField {

    private String fieldsDelimiter = "::";

    @Override
    protected Object convert(String value) {
        Manager manager = new Manager();
        String[] parsedArgs = value.split(fieldsDelimiter);
        manager.setId(Long.parseLong(parsedArgs[0]));
        manager.setName(parsedArgs[1]);
        return manager;
    }

    @Override
    public String convertToWrite(Object value) {
        Manager manager = (Manager) value;
        return String.format("%d" + fieldsDelimiter + "%s", manager.getId(), manager.getName());
    }
}
