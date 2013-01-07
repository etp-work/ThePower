package org.etp.portalKit.common.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.etp.portalKit.fw.annotation.MarkinFile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * The purpose of this class is
 */
@Component(value = "propertiesManager")
@EnableScheduling
public class PropertiesManager {

    private File configFile;
    private Properties prop;
    private long lastModified;
    private boolean isDirty;
    @Resource(name = "configSource")
    private Source source;

    @PostConstruct
    private void init() {
        isDirty = false;
        configFile = source.getSource();
        if (!configFile.exists())
            create();
        prop = new Properties();
        read();
    }

    private void create() {
        try {
            configFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read content from %HOME%/.protalkit. If config file doesn't
     * exist, create it first.
     */
    @Scheduled(fixedDelay = 300000)
    public void read() {
        long last = configFile.lastModified();
        if (last > lastModified) {
            lastModified = last;
            InputStreamReader reader = null;
            FileInputStream in = null;
            try {
                in = new FileInputStream(configFile);
                reader = new InputStreamReader(in, Charsets.UTF_8);
                prop.load(reader);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                create();
                read();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(reader);
                IOUtils.closeQuietly(in);
            }
        }
    }

    /**
     * Make properties persisted in physical file.
     */
    @Scheduled(fixedDelay = 360000)
    public void write() {
        if (!isDirty)
            return;
        FileOutputStream out = null;
        OutputStreamWriter writer = null;
        try {
            out = new FileOutputStream(configFile);
            writer = new OutputStreamWriter(out, Charsets.UTF_8);
            prop.store(writer, "");
            isDirty = false;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(writer);
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * Set properties.
     * 
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        if (!prop.containsKey(key))
            isDirty = true;
        else if (!prop.getProperty(key).equals(value))
            isDirty = true;
        if (isDirty)
            prop.setProperty(key, value);
    }

    /**
     * set properties from specified javabean to properties. Note:
     * Fields in javabean that need to stored in file, should have an
     * annotation "@MarkinFile", which can be used to indicate what
     * the name is of this property stored in file.
     * 
     * @param obj
     */
    public void fromBean(Object obj) {
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            if (Modifier.isStatic(field.getModifiers()))
                continue;
            Annotation[] declaredAnnotations = field.getDeclaredAnnotations();
            for (Annotation annotation : declaredAnnotations) {
                if (annotation.annotationType() == MarkinFile.class) {
                    String nameInFile = ((MarkinFile) annotation).name();
                    try {
                        this.set(nameInFile, (String) PropertyUtils.getSimpleProperty(obj, field.getName()));
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * @param t
     */
    public <T> void toBean(T t) {
        Field[] declaredFields = t.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            if (Modifier.isStatic(field.getModifiers()))
                continue;
            Annotation[] declaredAnnotations = field.getDeclaredAnnotations();
            for (Annotation annotation : declaredAnnotations) {
                if (annotation.annotationType() == MarkinFile.class) {
                    String nameInFile = ((MarkinFile) annotation).name();
                    try {
                        String value = this.get(nameInFile);
                        PropertyUtils.setProperty(t, field.getName(), value);
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Searches for the property with the specified key in this
     * property list. If the key is not found in this property list,
     * the default property list, and its defaults, recursively, are
     * then checked. The method returns null if the property is not
     * found.
     * 
     * @param key the property key.
     * @return value the value in this property list with the
     *         specified key value.
     */
    public String get(String key) {
        return prop.getProperty(key);
    }

    /**
     * Destroy instances that generated this obj.
     */
    @PreDestroy
    public void destroy() {
        this.write();
    }
}
