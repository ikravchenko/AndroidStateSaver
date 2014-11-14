package com.dorma.library;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;

import java.io.Serializable;
import java.lang.reflect.Field;

public class Saver {

    public static void save(Activity activity, Bundle outState) {
        try {
            Class<?> clazz = activity.getClass();
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields) {
                if (field.isAnnotationPresent(SaveState.class)) {
                    field.setAccessible(true);
                    SaveState annotation = field.getAnnotation(SaveState.class);
                    String id = annotation.id();
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
        if (inState == null) {
            return;
        }
        try {
            Class<?> clazz = activity.getClass();
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields) {
                if (field.isAnnotationPresent(SaveState.class)) {
                    field.setAccessible(true);
                    SaveState annotation = field.getAnnotation(SaveState.class);
                    String id = annotation.id();
                    field.set(activity, inState.get(id));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
