package com.dorma.instancesaver;

import android.annotation.TargetApi;
import android.os.Build;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.EditText;
import android.widget.TextView;

public class SaveTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public SaveTest() {
        super(MainActivity.class);
    }

    @UiThreadTest
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void testCheckTextAssignment() {
        String text = "something";
        EditText input = (EditText) getActivity().findViewById(R.id.input);
        assertNotNull(input);
        input.setText(text);
        getActivity().findViewById(R.id.submit).callOnClick();
        assertEquals(text, ((TextView) getActivity().findViewById(R.id.title)).getText());
    }
}
