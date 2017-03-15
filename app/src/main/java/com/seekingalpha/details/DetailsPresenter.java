package com.seekingalpha.details;

import com.seekingalpha.data.Object;

public class DetailsPresenter implements DetailsContract.Presenter {

    private final DetailsContract.View view;
    private final Object object;

    DetailsPresenter(Object object, DetailsContract.View view) {
        this.object = object;
        this.view = view;
    }

    @Override
    public void start() {
        view.showObject(object);
    }
}
