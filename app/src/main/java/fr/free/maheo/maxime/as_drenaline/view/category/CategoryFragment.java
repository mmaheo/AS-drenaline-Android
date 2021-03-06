package fr.free.maheo.maxime.as_drenaline.view.category;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import fr.free.maheo.maxime.as_drenaline.R;
import fr.free.maheo.maxime.as_drenaline.data.model.Category;
import fr.free.maheo.maxime.as_drenaline.view.actualityPreview.ActualityPreviewActivity;

/**
 * Created by mmaheo on 21/06/2017.
 */

public class CategoryFragment extends Fragment implements CategoryContract.View {

    public static final String TAG = CategoryFragment.class.getSimpleName();

    public static final String EXTRA_CATEGORY_NAME = CategoryFragment.class.getPackage().getName() + ".CATEGORY_NAME";

    public static final String EXTRA_CATEGORY_ID = CategoryFragment.class.getPackage().getName() + ".CATEGORY_ID";

    private CategoryContract.Presenter presenter;

    private Unbinder unbinder;

    private CategoryAdapter adapter;

    @BindView(R.id.category_recycler)
    RecyclerView categoryRecyclerView;

    @BindView(R.id.category_refresh)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.category_empty)
    TextView categoryEmpty;

    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_categories, container, false);

        unbinder = ButterKnife.bind(this, root);

        adapter = new CategoryAdapter(new ArrayList<>());
        adapter.setOnItemClickListener((view, position) -> presenter.getCategory(position));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        categoryRecyclerView.setLayoutManager(layoutManager);
        categoryRecyclerView.setAdapter(adapter);
        categoryRecyclerView.setItemAnimator(new DefaultItemAnimator());

        refreshLayout.setOnRefreshListener(() -> presenter.subscribe());

        categoryEmpty.setVisibility(View.GONE);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unsubscribe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void setPresenter(CategoryContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setCategories(List<Category> categories) {
        if (categories.size() == 0) {
            categoryEmpty.setVisibility(View.VISIBLE);
        } else {
            categoryEmpty.setVisibility(View.GONE);
            adapter.replaceData(categories);
        }

    }

    @Override
    public void error() {
        Toast.makeText(getContext(), "Une erreur est survenue, réessayez plur tard", Toast.LENGTH_LONG).show();
    }

    @Override
    public void startLoadingIndicator() {
        if (!refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void stopLoadingIndicator() {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showActualitiesOfCategory(Category category) {
        Intent intent = new Intent(getContext(), ActualityPreviewActivity.class);
        intent.putExtra(CategoryFragment.EXTRA_CATEGORY_NAME, category.getName());
        intent.putExtra(CategoryFragment.EXTRA_CATEGORY_ID, String.valueOf(category.getId()));
        startActivity(intent);
    }

}
