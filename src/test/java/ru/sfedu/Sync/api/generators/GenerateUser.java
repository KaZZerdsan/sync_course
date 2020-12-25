package ru.sfedu.Sync.api.generators;

import ru.sfedu.Sync.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenerateUser {

    public static Long amount = 1L;

    public static List<User> generate(int count) {
        if (count > 0) {
            List<User> listUser = new ArrayList<User>();
            for (int i = 0; i < count; i++) {
                User user = new User();
                user.setName(generateName());
                user.setId(amount);
                user.setAge(new Random().nextInt(55));
                listUser.add(user);
                amount++;
            }
            return listUser;
        }
        return new ArrayList<User>();
    }

    private static String generateName() {
        Random random = new Random();
        char[] word = new char[random.nextInt(9) + 2];
        for (int i = 0; i < word.length; i++) {
            word[i] = (char) ('a' + random.nextInt(26));
        }
        return new String(word);
    }
}
