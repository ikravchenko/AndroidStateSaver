package com.dorma.instancesaver;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.EditText;
import android.widget.TextView;

public class SaveTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public SaveTest() {
        super(MainActivity.class);
    }

    @UiThreadTest
    public void testCheckTextAssignment() {
        String text = "something";
        EditText input = (EditText) getActivity().findViewById(R.id.input);
        assertNotNull(input);
        input.setText(text);
        getActivity().findViewById(R.id.submit).callOnClick();
        assertEquals(text, ((TextView) getActivity().findViewById(R.id.title)).getText());
    }

    @UiThreadTest
    public void testStringCheckStateSaving() {
        String text = "something";
        EditText input = (EditText) getActivity().findViewById(R.id.input);
        assertNotNull(input);
        input.setText(text);
        getActivity().findViewById(R.id.submit).callOnClick();
        getActivity().recreate();
        assertEquals(text, ((TextView) getActivity().findViewById(R.id.title)).getText().toString());
    }

    @UiThreadTest
    public void testPOJOCheckStateSaving() {
        final SimpleObject simpleObject = new SimpleObject(1, 5, 4.0);
        getActivity().simpleObject = simpleObject;
        getActivity().recreate();
        assertEquals(simpleObject, getActivity().simpleObject);
    }
}
