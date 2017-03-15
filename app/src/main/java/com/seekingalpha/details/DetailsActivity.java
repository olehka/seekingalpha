package com.seekingalpha.details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.seekingalpha.R;
import com.seekingalpha.data.Object;
import com.seekingalpha.objects.ObjectsFragment;
import com.seekingalpha.utils.Utils;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame);

        Object object = getIntent().getExtras().getParcelable(ObjectsFragment.KEY_OBJECT);

        DetailsFragment detailsFragment = (DetailsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (detailsFragment == null) {
            detailsFragment = DetailsFragment.newInstance();
            Utils.addFragmentToActivity(getSupportFragmentManager(), detailsFragment, R.id.contentFrame);
        }

        DetailsContract.Presenter presenter = new DetailsPresenter(object, detailsFragment);
        detailsFragment.setPresenter(presenter);
    }
}
