package com.ikravchenko.instancesaver;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;

public class AttachableFragmentsActivityTest extends ActivityInstrumentationTestCase2<AttachableFragmentsActivity> {

    private Instrumentation.ActivityMonitor mainActivityMonitor;

    public AttachableFragmentsActivityTest() {
        super(AttachableFragmentsActivity.class);
    }

    public void testShouldSaveActivityAndFragmentStates() throws Throwable {
        SimpleObject activityObject = new SimpleObject(100, 100, 100.0);
        final AttachableFragmentsActivity activity = getActivity();
        activity.simpleObject = activityObject;
        SimpleFragment first = (SimpleFragment) activity.getFragmentManager().findFragmentById(R.id.first);
        SimpleObject firstFragmentObject = new SimpleObject(1, 1, 1.0);
        first.simpleObject = firstFragmentObject;

        mainActivityMonitor.waitForActivityWithTimeout(1000);
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.recreate();
            }
        });
        getInstrumentation().waitForIdleSync();

        AttachableFragmentsActivity lastActivity = (AttachableFragmentsActivity) mainActivityMonitor.getLastActivity();

        assertEquals(activityObject, lastActivity.simpleObject);
        assertEquals(firstFragmentObject, ((SimpleFragment) lastActivity.getFragmentManager().findFragmentById(R.id.first)).simpleObject);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mainActivityMonitor = new Instrumentation.ActivityMonitor(AttachableFragmentsActivity.class.getName(), null, false);
        getInstrumentation().addMonitor(mainActivityMonitor);
    }
}