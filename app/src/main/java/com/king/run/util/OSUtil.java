package com.king.run.util;

import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by Zhang Zhen on 2017/2/27.
 */

public class OSUtil {
    private static final String KEY_EMUI_VERSION_CODE = "ro.build.version.emui";
    private static final String KEY_EMUI_VERSION_LEVEL = "ro.build.hw_emui_api_level";
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";

    private static boolean miui = false;
    private static boolean hadMiui = false;
    private static boolean emui = false;
    private static boolean hadEmui = false;
    private static boolean flyme = false;
    private static boolean hadFlyme = false;

    private static boolean isPropertiesExist(String... keys) {
        try {
            BuildProperties prop = new BuildProperties();
            for (String key : keys) {
                String str = prop.getProperty(key);
                if (str != null)
                    return true;
            }
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean isEMUI() {
        if (!hadEmui) {
            hadEmui = true;
            emui = isPropertiesExist(KEY_EMUI_VERSION_CODE, KEY_EMUI_VERSION_LEVEL);
        }
        return emui;
    }

    public static boolean isMIUI() {
        if (!hadMiui) {
            hadMiui = true;
            miui = isPropertiesExist(KEY_MIUI_VERSION_CODE, KEY_MIUI_VERSION_NAME);
        }
        return miui;
    }

    public static boolean isFlyme() {
        if (!hadFlyme) {
            hadFlyme = true;
            try {
                final Method method = Build.class.getMethod("hasSmartBar");
                flyme = method != null;
            } catch (final Exception e) {
                flyme = false;
            }
        }
        return flyme;
    }

    public static class BuildProperties {

        private final Properties properties;

        private BuildProperties() throws IOException {
            properties = new Properties();
            properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
        }

        public boolean containsKey(final Object key) {
            return properties.containsKey(key);
        }

        public boolean containsValue(final Object value) {
            return properties.containsValue(value);
        }

        public Set<Map.Entry<Object, Object>> entrySet() {
            return properties.entrySet();
        }

        public String getProperty(final String name) {
            return properties.getProperty(name);
        }

        public String getProperty(final String name, final String defaultValue) {
            return properties.getProperty(name, defaultValue);
        }

        public boolean isEmpty() {
            return properties.isEmpty();
        }

        public Enumeration<Object> keys() {
            return properties.keys();
        }

        public Set<Object> keySet() {
            return properties.keySet();
        }

        public int size() {
            return properties.size();
        }

        public Collection<Object> values() {
            return properties.values();
        }
    }
}
