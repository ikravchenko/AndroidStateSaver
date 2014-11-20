package com.ikravchenko.instancesaver;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.ikravchenko.library.SaveState;
import com.ikravchenko.library.Saver;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;

public class SaverTest extends TestCase {

    Saver saver;

    public void setUp() {
        saver = new Saver();
    }

    public void testSavePrimitive() throws Throwable {
        class PrivitiveCase {

            @SaveState
            int primitive = 0;

        }

        final int expected = 5;

        PrivitiveCase before = new PrivitiveCase();
        before.primitive = expected;

        Bundle state = saveState(before);

        PrivitiveCase after = new PrivitiveCase();
        saver.restore(after, state);

        assertEquals(expected, after.primitive);
    }

    public void testSaveArray() throws Throwable {
        class ArrayCase {

            @SaveState
            float[] array;

        }

        final float[] expected = {1f, 2f, 3f};

        ArrayCase before = new ArrayCase();
        before.array = expected;

        Bundle state = saveState(before);

        ArrayCase after = new ArrayCase();
        saver.restore(after, state);

        assertNotSame(expected, after.array);
        assertTrue(Arrays.equals(expected, after.array));
    }

    public void testSaveSerializable() throws Throwable {
        class SerializableCase {

            @SaveState
            String string;

        }

        final String expected = "string";

        SerializableCase before = new SerializableCase();
        before.string = expected;

        Bundle state = saveState(before);

        SerializableCase after = new SerializableCase();
        saver.restore(after, state);

        assertNotSame(expected, after.string);
        assertEquals(expected, after.string);
    }

    public void testSaveParcelable() throws Throwable {
        class ParcelableCase {

            @SaveState
            SimpleObject simpleObject;

        }

        final SimpleObject expected = new SimpleObject(1, 2, 3);

        ParcelableCase before = new ParcelableCase();
        before.simpleObject = expected;

        Bundle state = saveState(before);

        ParcelableCase after = new ParcelableCase();
        saver.restore(after, state);

        assertNotSame(expected, after.simpleObject);
        assertEquals(expected, after.simpleObject);
    }

    public void testSaveParcelableArrayList() throws Throwable {
        class ParcelableArrayListCase {

            @SaveState
            ArrayList<Parcelable> arrayList;

        }

        final ArrayList<Parcelable> expected = new ArrayList<Parcelable>(
                Arrays.asList(
                        new SimpleObject(1, 1, 1),
                        new SimpleObject(2, 2, 2)
                )
        );

        ParcelableArrayListCase before = new ParcelableArrayListCase();
        before.arrayList = expected;

        Bundle state = saveState(before);

        ParcelableArrayListCase after = new ParcelableArrayListCase();
        saver.restore(after, state);

        assertNotSame(expected, after.arrayList);
        assertEquals(expected, after.arrayList);
    }

    private Bundle saveState(Object object) {
        Bundle state = new Bundle();
        saver.save(object, state);
        Bundle unmarshalledState = marshallAndUnmarshall(state);
        assertNotSame(state, unmarshalledState);
        return unmarshalledState;
    }

    private Bundle marshallAndUnmarshall(Bundle bundle) {
        byte[] bytes = marshall(bundle);
        return unmarshall(bytes, Bundle.CREATOR);
    }

    private byte[] marshall(Parcelable parcelable) {
        Parcel parcel = Parcel.obtain();
        try {
            parcelable.writeToParcel(parcel, 0);
            return parcel.marshall();
        } finally {
            parcel.recycle();
        }
    }

    private Parcel unmarshall(byte[] bytes) {
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(bytes, 0, bytes.length);
        parcel.setDataPosition(0);
        return parcel;
    }

    private <T> T unmarshall(byte[] bytes, Parcelable.Creator<T> creator) {
        Parcel parcel = unmarshall(bytes);
        try {
            return creator.createFromParcel(parcel);
        } finally {
            parcel.recycle();
        }
    }

}
