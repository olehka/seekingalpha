package com.seekingalpha.objects;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.seekingalpha.R;
import com.seekingalpha.data.source.ObjectsProvider;
import com.seekingalpha.utils.Utils;

public class ObjectsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame);

        ObjectsFragment objectsFragment = (ObjectsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (objectsFragment == null) {
            objectsFragment = ObjectsFragment.newInstance();
            Utils.addFragmentToActivity(getSupportFragmentManager(), objectsFragment, R.id.contentFrame);
        }

        ObjectsContract.Presenter presenter = new ObjectsPresenter(ObjectsProvider.getInstance(getApplicationContext()), objectsFragment);
        objectsFragment.setPresenter(presenter);
    }
}
