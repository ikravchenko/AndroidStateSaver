package com.ikravchenko.instancesaver;

import android.app.Activity;
import android.os.Bundle;

import com.ikravchenko.library.DefaultSaver;
import com.ikravchenko.library.SaveState;

public class MultipleFragmentsActivity extends Activity {

    @SaveState
    public SimpleObject simpleObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiple_fragments_activity);
        new DefaultSaver().restore(this, savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        new DefaultSaver().save(this, outState);
    }
}