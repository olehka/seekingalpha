package com.seekingalpha.data.source;

import com.seekingalpha.data.Object;

import java.util.List;

public interface ObjectsSource {

    interface LoadObjectsCallback {

        void onObjectsLoaded(List<Object> objects);

        void onObjectsNotLoaded(ErrorReason reason);
    }

    void getObjects(LoadObjectsCallback callback, int pageNumber);
}
