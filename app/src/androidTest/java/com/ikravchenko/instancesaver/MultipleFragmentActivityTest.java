package com.ikravchenko.instancesaver;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;

public class MultipleFragmentActivityTest extends ActivityInstrumentationTestCase2<MultipleFragmentsActivity> {

    private static final String FIRST_FRAGMENT_TAG = "first";
    private static final String SECOND_FRAGMENT_TAG = "second";

    public MultipleFragmentActivityTest() {
        super(MultipleFragmentsActivity.class);
    }

    @UiThreadTest
    public void testShouldSaveFragmentStates() {
        getActivity();
        SimpleObject activityObject = new SimpleObject(100, 100, 100.0);
        getActivity().simpleObject = activityObject;
        SimpleFragment first = (SimpleFragment) getActivity().getFragmentManager().findFragmentByTag(FIRST_FRAGMENT_TAG);
        assertNotNull(first);
        SimpleObject firstFragmentObject = new SimpleObject(1, 1, 1.0);
        first.simpleObject = firstFragmentObject;
        SimpleFragment second = (SimpleFragment) getActivity().getFragmentManager().findFragmentByTag(SECOND_FRAGMENT_TAG);
        assertNotNull(second);
        SimpleObject secondFragmentObject = new SimpleObject(2, 2, 2.0);
        second.simpleObject = secondFragmentObject;

        getActivity().recreate();
        assertEquals(activityObject, getActivity().simpleObject);
        assertEquals(firstFragmentObject, ((SimpleFragment) getActivity().getFragmentManager().findFragmentByTag(FIRST_FRAGMENT_TAG)).simpleObject);
        assertEquals(secondFragmentObject, ((SimpleFragment) getActivity().getFragmentManager().findFragmentByTag(SECOND_FRAGMENT_TAG)).simpleObject);
    }
}
