package com.ty_yak.auth.util;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;
import java.util.Random;

@UtilityClass
public class SecureRandomUtil {

    public static int getRandom(int bound) {
        return new SecureRandom().nextInt(bound);
    }

    public static int getRandomInRange(int min, int maxInclusive){

        Random random = new Random();

        return random.nextInt(maxInclusive + 1 - min) + min;
    }
}
