package com.liuj.lmq.utils;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by cdliujian1 on 2016/11/18.
 */
public class PropUtil {

    public static Properties load(String filename) {
        Properties prop = new Properties();
        InputStream in = Object.class.getResourceAsStream(filename);
        try {
            prop.load(in);
        } catch (Exception ignore) {

        }
        return prop;
    }

}
