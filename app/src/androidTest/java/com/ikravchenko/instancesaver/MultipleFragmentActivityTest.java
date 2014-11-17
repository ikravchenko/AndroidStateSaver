package com.ikravchenko.instancesaver;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;

public class MultipleFragmentActivityTest extends ActivityInstrumentationTestCase2<MultipleFragmentsActivity> {

    private static final String FIRST_FRAGMENT_TAG = "first";
    private static final String SECOND_FRAGMENT_TAG = "second";
    private Instrumentation.ActivityMonitor mainActivityMonitor;

    public MultipleFragmentActivityTest() {
        super(MultipleFragmentsActivity.class);
    }

    public void testShouldSaveActivityAndFragmentStates() throws Throwable {
        SimpleObject activityObject = new SimpleObject(100, 100, 100.0);
        final MultipleFragmentsActivity activity = getActivity();
        activity.simpleObject = activityObject;
        SimpleFragment first = (SimpleFragment) activity.getFragmentManager().findFragmentByTag(FIRST_FRAGMENT_TAG);
        SimpleObject firstFragmentObject = new SimpleObject(1, 1, 1.0);
        first.simpleObject = firstFragmentObject;
        SimpleFragment second = (SimpleFragment) activity.getFragmentManager().findFragmentByTag(SECOND_FRAGMENT_TAG);
        SimpleObject secondFragmentObject = new SimpleObject(2, 2, 2.0);
        second.simpleObject = secondFragmentObject;

        mainActivityMonitor.waitForActivityWithTimeout(1000);
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.recreate();
            }
        });
        getInstrumentation().waitForIdleSync();

        MultipleFragmentsActivity lastActivity = (MultipleFragmentsActivity) mainActivityMonitor.getLastActivity();
        assertEquals(activityObject, lastActivity.simpleObject);
        assertEquals(firstFragmentObject, ((SimpleFragment) lastActivity.getFragmentManager().findFragmentByTag(FIRST_FRAGMENT_TAG)).simpleObject);
        assertEquals(secondFragmentObject, ((SimpleFragment) lastActivity.getFragmentManager().findFragmentByTag(SECOND_FRAGMENT_TAG)).simpleObject);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mainActivityMonitor = new Instrumentation.ActivityMonitor(MultipleFragmentsActivity.class.getName(), null, false);
        getInstrumentation().addMonitor(mainActivityMonitor);
    }
}
