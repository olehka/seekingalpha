package com.seekingalpha.details;

import com.seekingalpha.BasePresenter;
import com.seekingalpha.BaseView;
import com.seekingalpha.data.Object;

public interface DetailsContract {

    interface View extends BaseView<Presenter> {

        void showObject(Object object);
    }

    interface Presenter extends BasePresenter {}
}
