package com.seekingalpha.objects;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seekingalpha.R;
import com.seekingalpha.data.Object;
import com.seekingalpha.details.DetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class ObjectsFragment extends Fragment implements ObjectsContract.View {

    public static final String KEY_OBJECT = "KEY_OBJECT";
    public static final String KEY_TRANSITION_IMAGE = "KYE_TRANSITION_IMAGE";
    public static final String KEY_TRANSITION_NAME = "KYE_TRANSITION_NAME";
    public static final String KEY_TRANSITION_DESCRIPTION = "KYE_TRANSITION_DESCRIPTION";

    private boolean isLoading = false, isLastPage = false;
    private int pageNumber = 1;
    private ObjectsContract.Presenter presenter;
    private ObjectsAdapter adapter;
    private LinearLayoutManager layoutManager;

    private ObjectsAdapter.OnItemClickListener onItemClickListener = new ObjectsAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(ObjectsAdapter.ViewHolder viewHolder, int position, Object item) {
            Intent intent = new Intent(getActivity(), DetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(KEY_OBJECT, item);
            intent.putExtras(bundle);

            // https://github.com/codepath/android_guides/wiki/Shared-Element-Activity-Transition
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                intent.putExtra(KEY_TRANSITION_IMAGE, viewHolder.image.getTransitionName());
                intent.putExtra(KEY_TRANSITION_NAME, viewHolder.name.getTransitionName());
                intent.putExtra(KEY_TRANSITION_DESCRIPTION, viewHolder.description.getTransitionName());
                Pair<View, String> p1 = Pair.create((View)viewHolder.image, viewHolder.image.getTransitionName());
                Pair<View, String> p2 = Pair.create((View)viewHolder.name, viewHolder.name.getTransitionName());
                Pair<View, String> p3 = Pair.create((View)viewHolder.description, viewHolder.description.getTransitionName());
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), p1, p2, p3);
                startActivity(intent, options.toBundle());
            } else {
                startActivity(intent);
            }
        }
    };

    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            // https://medium.com/@etiennelawlor/pagination-with-recyclerview-1cb7e66a502b#.xu2swewbc
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            if (!isLoading && !isLastPage) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    presenter.loadNextPage(pageNumber++);
                }
            }
        }
    };
    private RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ObjectsAdapter(new ArrayList<Object>(), onItemClickListener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_objects, container, false);
        recyclerView = (RecyclerView)root.findViewById(R.id.rv_objects);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.addOnScrollListener(recyclerViewOnScrollListener);

        presenter.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerView.removeOnScrollListener(recyclerViewOnScrollListener);
        pageNumber = 1;
    }

    public static ObjectsFragment newInstance() {
        return new ObjectsFragment();
    }

    @Override
    public void setPresenter(ObjectsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showObjects(List<Object> objects) {
        adapter.addAll(objects);
    }

    @Override
    public void showNoObjects() {

    }

    @Override
    public void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    @Override
    public void setLastPage(boolean isLastPage) {
        this.isLastPage = isLastPage;
    }
}
