package com.ikravchenko.instancesaver;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;

public class AttachableFragmentsActivityTest extends ActivityInstrumentationTestCase2<AttachableFragmentsActivity> {

    public AttachableFragmentsActivityTest() {
        super(AttachableFragmentsActivity.class);
    }

    @UiThreadTest
    public void testShouldSaveActivityAndFragmentStates() {
        getActivity();
        SimpleObject activityObject = new SimpleObject(100, 100, 100.0);
        getActivity().simpleObject = activityObject;
        SimpleFragment first = (SimpleFragment) getActivity().getFragmentManager().findFragmentById(R.id.first);
        assertNotNull(first);
        SimpleObject firstFragmentObject = new SimpleObject(1, 1, 1.0);
        first.simpleObject = firstFragmentObject;
        getActivity().recreate();
        assertEquals(activityObject, getActivity().simpleObject);
        assertEquals(firstFragmentObject, ((SimpleFragment) getActivity().getFragmentManager().findFragmentById(R.id.first)).simpleObject);
    }
}