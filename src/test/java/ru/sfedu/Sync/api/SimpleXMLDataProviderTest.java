package ru.sfedu.Sync.api;

import org.junit.Assert;
import org.junit.Test;
import ru.sfedu.Sync.models.User;
import ru.sfedu.Sync.utils.Result;
import ru.sfedu.Sync.utils.ResultType;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SimpleXMLDataProviderTest {

    private IDataProvider dp = new SimpleXMLDataProvider();

    public SimpleXMLDataProviderTest() throws IOException {
    }

    private void getMethodName() {
        System.out.println("\n*** " + Thread.currentThread().getStackTrace()[2].getMethodName() + " ***");
    }

    @Test
    public void insertRecord() throws Exception {
        getMethodName();
        List<User> listUser = GenerateUser.generate(100);
        System.out.println(listUser.size());
        Result res = dp.insertRecord(listUser, false, User.class);
        File file = new File(dp.getPath(User.class));
        System.out.println(res.message);
        Assert.assertTrue(file.exists());
        Assert.assertNotNull(res);
    }

    @Test
    public void getRecords() throws Exception {
        getMethodName();
        Result res = dp.getRecords(User.class);
        System.out.println(res.message);
        System.out.println(res.data);
        Assert.assertNotNull(res);
    }

    @Test
    public void getRecordById() throws Exception {
        getMethodName();
        Result res = dp.getRecordById(69, User.class);
        System.out.println(res.message);
        System.out.println(res.data);
        Assert.assertNotNull(res);
    }

    @Test
    public void deleteRecord() throws Exception {
        getMethodName();
        int id = 35;
        Result res = dp.deleteRecord(id, User.class);
        System.out.println(res.message);
        System.out.println(res.data);
        Assert.assertNotNull(res);
    }

    @Test
    public <T extends User> void updateRecord() throws Exception {
        getMethodName();
        int id = 2;
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
}