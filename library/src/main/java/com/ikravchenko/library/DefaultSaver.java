package com.ikravchenko.library;

import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DefaultSaver implements Saver<Object> {

    private static final String SAVED_OBJECTS_MAPPING_KEY = "SAVED_OBJECTS_MAPPING";

    private HashMap<String, String> id2Name = new HashMap<String, String>();

    public void save(Object object, Bundle outState) {
        if (object == null) {
            throw new RuntimeException("Object should not be null!");
        }
        if (outState == null) {
            throw new RuntimeException("outState bundle should not be empty!");
        }
        Log.i("DefaultInstanceSaver", "Size: " + id2Name.size() + ", class: " + object.getClass().getSimpleName() + " " + TextUtils.join("; ",  id2Name.entrySet()));
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
        } finally {
            if (!id2Name.isEmpty()) {
                outState.putSerializable(SAVED_OBJECTS_MAPPING_KEY, id2Name);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void restore(Object object, Bundle inState) {
        if (object == null) {
            throw new RuntimeException("Object should not be null!");
        }
        if (inState == null) {
            return;
        }

        if (!inState.containsKey(SAVED_OBJECTS_MAPPING_KEY)) {
            Log.i("Saver", "nothing was saved");
            return;
        }

        id2Name = (HashMap<String, String>) inState.getSerializable(SAVED_OBJECTS_MAPPING_KEY);

        try {
            Class<?> clazz = object.getClass();
            for (Map.Entry<String, String> savedEntry : id2Name.entrySet()) {
                try {
                    Field declaredField = clazz.getDeclaredField(savedEntry.getValue());
                    declaredField.setAccessible(true);
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
