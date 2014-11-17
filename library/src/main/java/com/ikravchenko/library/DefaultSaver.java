package com.ikravchenko.library;

import android.os.Bundle;
import android.os.Parcelable;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DefaultSaver implements Saver<Object> {

    private static final HashMap<String, String> id2Name = new HashMap<String, String>();

    public void save(Object object, Bundle outState) {
        if (object == null) {
            throw new RuntimeException("Activity should not be null!");
        }
        if (outState == null) {
            throw new RuntimeException("outState bundle should not be empty!");
        }
        id2Name.clear();
        try {
            Class<?> clazz = object.getClass();
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields) {
                if (field.isAnnotationPresent(SaveState.class)) {
                    if (Modifier.isFinal(field.getModifiers())) {
                        throw new RuntimeException("final field should not be annotated to save state!");
                    }
                    field.setAccessible(true);
                    String id = UUID.randomUUID().toString();
                    id2Name.put(id, field.getName());
                    Object value = field.get(object);
                    if (value instanceof Parcelable) {
                        outState.putParcelable(id, (Parcelable) value);
                    } else if (value instanceof Serializable) {
                        outState.putSerializable(id, (Serializable) value);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void restore(Object object, Bundle inState) {
        if (object == null) {
            throw new RuntimeException("Activity should not be null!");
        }
        if (inState == null) {
            return;
        }

        try {
            Class<?> clazz = object.getClass();
            for (Map.Entry<String, String> savedEntry : id2Name.entrySet()) {
                try {
                    Field declaredField = clazz.getDeclaredField(savedEntry.getValue());
                    declaredField.set(object, inState.get(savedEntry.getKey()));
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
