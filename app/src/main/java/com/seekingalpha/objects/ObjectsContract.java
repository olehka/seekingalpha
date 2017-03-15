package com.seekingalpha.objects;

import com.seekingalpha.BasePresenter;
import com.seekingalpha.BaseView;
import com.seekingalpha.data.Object;

import java.util.List;

public interface ObjectsContract {

    interface View extends BaseView<Presenter> {

        void showObjects(List<Object> objects);

        void showNoObjects();

        void setLoading(boolean isLoading);

        void setLastPage(boolean isLastPage);
    }

    interface Presenter extends BasePresenter {

        void loadNextPage(int pageNumber);
    }

}
