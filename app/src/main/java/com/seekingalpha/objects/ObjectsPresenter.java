package com.seekingalpha.objects;

import com.seekingalpha.data.Object;
import com.seekingalpha.data.source.ErrorReason;
import com.seekingalpha.data.source.ObjectsSource;

import java.util.List;

public class ObjectsPresenter implements ObjectsContract.Presenter {

    public static final int START_PAGE = 0;
    private final ObjectsSource source;
    private final ObjectsContract.View view;

    private ObjectsSource.LoadObjectsCallback loadObjectsCallback = new ObjectsSource.LoadObjectsCallback() {

        @Override
        public void onObjectsLoaded(List<Object> objects) {
            view.setLoading(false);
            if (objects.isEmpty()) {
                view.showNoObjects();
            } else {
                view.showObjects(objects);
            }
        }

        @Override
        public void onObjectsNotLoaded(ErrorReason reason) {
            view.setLoading(false);
            if (reason == ErrorReason.LAST_PAGE) {
                view.setLastPage(true);
            } else {
                view.showNoObjects();
            }
        }
    };

    ObjectsPresenter(ObjectsSource source, ObjectsContract.View view) {
        this.source = source;
        this.view = view;
    }

    @Override
    public void start() {
        loadNextPage(START_PAGE);
    }

    @Override
    public void loadNextPage(int pageNumber) {
        view.setLoading(true);
        source.getObjects(loadObjectsCallback, pageNumber);
    }
}
