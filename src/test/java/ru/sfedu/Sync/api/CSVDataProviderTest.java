package ru.sfedu.Sync.api;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.junit.Assert;
import org.junit.Test;
import ru.sfedu.Sync.models.User;
import ru.sfedu.Sync.utils.Result;
import ru.sfedu.Sync.utils.ResultType;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.List;

public class CSVDataProviderTest {

    private CSVDataProvider dp = new CSVDataProvider();

    public CSVDataProviderTest() throws IOException {
    }

    private void getMethodName() {
        System.out.println("\n*** " + Thread.currentThread().getStackTrace()[2].getMethodName() + " ***");
    }

    @Test
    public void insertRecord() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        getMethodName();
        List<User> listUser = GenerateUser.generate(1);
        Result res = dp.insertRecord(listUser, true, User.class);
        File file = new File(dp.getPath(User.class));
        System.out.println(res.message);
        Assert.assertTrue(file.exists());
        Assert.assertNotNull(res);
    }

    @Test
    public void getRecords() throws IOException {
        getMethodName();
        Result res = dp.getRecords(User.class);
        System.out.println(res.message);
        System.out.println(res.data);
        Assert.assertNotNull(res);
    }

    @Test
    public void getRecordById() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        getMethodName();
        int id = 69;
        System.out.println("Id to found: " + id);
        Result res = dp.getRecordById(id, User.class);
        System.out.println(res.message);
        System.out.println(res.data);
        Assert.assertNotNull(res);
    }

    @Test
    public void deleteRecord() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        getMethodName();
        int id = 35;
        Result res = dp.deleteRecord(id, User.class);
        System.out.println(res.message);
        Assert.assertNotNull(res);
    }

    @Test
    public <T extends User> void updateRecord() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        getMethodName();
        int id = 77;
        Result<User> oldRes = dp.getRecordById(id, User.class);
        if (oldRes.resultType == ResultType.ERROR) {
            System.out.println(oldRes.message);
            Assert.assertNotNull(oldRes);
            return;
        }
        User user = oldRes.data;
        user.setName(user.getName() + "_New");
        Result res = dp.updateRecord(user);
        System.out.println(res.message);
        Assert.assertNotNull(res);
    }
    @Test
    public void sandbox() {
        List<User> listUser = GenerateUser.generate(10);
        int id = listUser.get(0).getId();
        System.out.println(listUser.stream().anyMatch(el -> el.getId() == id));
        System.out.println(listUser.stream().anyMatch(el -> el.getId() == 123123));
    }
}