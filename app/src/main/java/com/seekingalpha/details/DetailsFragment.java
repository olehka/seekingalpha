package com.seekingalpha.details;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.seekingalpha.R;
import com.seekingalpha.data.Object;
import com.seekingalpha.objects.ObjectsFragment;
import com.seekingalpha.utils.Utils;

public class DetailsFragment extends Fragment implements DetailsContract.View {

    public static final String ERROR = "Error";

    private DetailsContract.Presenter presenter;
    private TextView nameTV;
    private TextView descriptionTV;
    private ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_details, container, false);
        nameTV = (TextView)root.findViewById(R.id.detail_name);
        descriptionTV = (TextView)root.findViewById(R.id.detail_description);
        imageView = (ImageView)root.findViewById(R.id.detail_image);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView.setTransitionName(getActivity().getIntent().getStringExtra(ObjectsFragment.KEY_TRANSITION_IMAGE));
            nameTV.setTransitionName(getActivity().getIntent().getStringExtra(ObjectsFragment.KEY_TRANSITION_NAME));
            descriptionTV.setTransitionName(getActivity().getIntent().getStringExtra(ObjectsFragment.KEY_TRANSITION_DESCRIPTION));
        }

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.start();
    }

    public static DetailsFragment newInstance() {
        return new DetailsFragment();
    }

    @Override
    public void setPresenter(DetailsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showObject(Object object) {
        Glide.with(this)
                .load(object.getImageUrl())
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(imageView);

        String name = object.getName();
        String description = object.getDescription();
        nameTV.setText(Utils.isEmpty(name) ? ERROR : name);
        descriptionTV.setText(Utils.isEmpty(description) ? ERROR : description);
    }
}
