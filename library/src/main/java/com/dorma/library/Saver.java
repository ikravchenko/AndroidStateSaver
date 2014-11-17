package com.dorma.library;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Saver {

    private static final HashMap<String, String> id2Name = new HashMap<String, String>();


    public static void save(Activity activity, Bundle outState) {
        if (activity == null) {
            throw new RuntimeException("Activity should not be null!");
        }
        if (outState == null) {
            throw new RuntimeException("outState bundle should not be empty!");
        }
        id2Name.clear();
        try {
            Class<?> clazz = activity.getClass();
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields) {
                if (field.isAnnotationPresent(SaveState.class)) {
                    if (Modifier.isFinal(field.getModifiers())) {
                        throw new RuntimeException("final field should not be annotated to save state!");
                    }
                    field.setAccessible(true);
                    String id = UUID.randomUUID().toString();
                    id2Name.put(id, field.getName());
                    Object value = field.get(activity);
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

    public static void restore(Activity activity, Bundle inState) {
        if (activity == null) {
            throw new RuntimeException("Activity should not be null!");
        }
        if (inState == null) {
            return;
        }

        try {
            Class<?> clazz = activity.getClass();
            for (Map.Entry<String, String> savedEntry : id2Name.entrySet()) {
                try {
                    Field declaredField = clazz.getDeclaredField(savedEntry.getValue());
                    declaredField.set(activity, inState.get(savedEntry.getKey()));
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
