package com.ikravchenko.instancesaver;

import android.app.Instrumentation;
import android.os.Parcelable;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class SingleActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Instrumentation.ActivityMonitor mainActivityMonitor;

    public SingleActivityTest() {
        super(MainActivity.class);
    }

    @UiThreadTest
    public void testTextAssignment() {
        String text = "something";
        EditText input = (EditText) getActivity().findViewById(R.id.input);
        assertNotNull(input);
        input.setText(text);
        getActivity().findViewById(R.id.submit).callOnClick();
        assertEquals(text, ((TextView) getActivity().findViewById(R.id.title)).getText());
    }

    public void testStringStateSaving() throws Throwable {

        final String text = "something";
        final MainActivity activity = getActivity();
        final EditText input = (EditText) activity.findViewById(R.id.input);
        mainActivityMonitor.waitForActivityWithTimeout(1000);
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                input.setText(text);
                activity.findViewById(R.id.submit).callOnClick();
                activity.recreate();
            }
        });

        getInstrumentation().waitForIdleSync();
        MainActivity lastActivity = (MainActivity) mainActivityMonitor.getLastActivity();

        String result = ((TextView) lastActivity.findViewById(R.id.title)).getText().toString();
        assertEquals(text, result);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mainActivityMonitor = new Instrumentation.ActivityMonitor(MainActivity.class.getName(), null, false);
        getInstrumentation().addMonitor(mainActivityMonitor);
    }

    public void testPOJOStateSaving() throws Throwable {
        final MainActivity activity = getActivity();
        final SimpleObject simpleObject = new SimpleObject(1, 5, 4.0);
        activity.simpleObject = simpleObject;
        mainActivityMonitor.waitForActivityWithTimeout(1000);
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.recreate();
            }
        });

        getInstrumentation().waitForIdleSync();
        MainActivity lastActivity = (MainActivity) mainActivityMonitor.getLastActivity();
        assertEquals(simpleObject, lastActivity.simpleObject);
    }

    public void testPrimitiveStateSaving() throws Throwable {
        final MainActivity activity = getActivity();
        final int expected = 5;
        activity.primitive = expected;
        mainActivityMonitor.waitForActivityWithTimeout(1000);
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.recreate();
            }
        });

        getInstrumentation().waitForIdleSync();
        MainActivity lastActivity = (MainActivity) mainActivityMonitor.getLastActivity();
        assertEquals(expected, lastActivity.primitive);
    }

    public void testArrayStateSaving() throws Throwable {
        final MainActivity activity = getActivity();
        final int[] expected = {1, 2, 3};
        activity.primitiveArray = expected;
        mainActivityMonitor.waitForActivityWithTimeout(1000);
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.recreate();
            }
        });

        getInstrumentation().waitForIdleSync();
        MainActivity lastActivity = (MainActivity) mainActivityMonitor.getLastActivity();
        assertEquals(expected, lastActivity.primitiveArray);
    }

    public void testParcelablesArrayListStateSaving() throws Throwable {
        final MainActivity activity = getActivity();
        ArrayList<Parcelable> expected = new ArrayList<Parcelable>(
                Arrays.asList(
                        new SimpleObject(0, 0, 0),
                        new SimpleObject(1, 1, 1)
                )
        );
        activity.parcelablesArrayList = expected;

        mainActivityMonitor.waitForActivityWithTimeout(1000);
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.recreate();
            }
        });

        getInstrumentation().waitForIdleSync();
        MainActivity lastActivity = (MainActivity) mainActivityMonitor.getLastActivity();
        assertEquals(expected, lastActivity.parcelablesArrayList);
    }

}
