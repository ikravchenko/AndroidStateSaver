package com.ikravchenko.library;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
/**
 * Annotates the fields that should be saved in a {@link android.app.Activity} or {@link android.app.Fragment} lifecycle
 * throws {@link java.lang.RuntimeException} when annotating final fields.
 *
 * @see com.ikravchenko.library.Saver
 */
public @interface SaveState {}
