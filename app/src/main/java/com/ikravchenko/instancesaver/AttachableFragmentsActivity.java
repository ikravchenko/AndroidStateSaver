package com.ikravchenko.instancesaver;

import android.app.Activity;
import android.os.Bundle;

import com.ikravchenko.library.Saver;
import com.ikravchenko.library.SaveState;

public class AttachableFragmentsActivity extends Activity {

    @SaveState
    public SimpleObject simpleObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attachable_fragments_activity);
        if (getFragmentManager().findFragmentById(R.id.first) == null) {
            getFragmentManager().beginTransaction().replace(R.id.first, new SimpleFragment()).commit();
        }
        new Saver().restore(this, savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        new Saver().save(this, outState);
    }
}
