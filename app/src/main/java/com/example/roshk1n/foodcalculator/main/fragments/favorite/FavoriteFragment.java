package com.example.roshk1n.foodcalculator.main.fragments.favorite;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.main.adapters.RecyclerFavoriteAdapter;
import com.example.roshk1n.foodcalculator.realm.FavoriteListRealm;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;

public class FavoriteFragment extends Fragment implements FavoriteView{

    private FavoritePresenterImpl favoritePresenter;
    private FavoriteListRealm favoriteListRealm;
    private View view;
    private CoordinatorLayout favoriteCoordinatorLayout;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerFavoriteAdapter mAdapter;

    public FavoriteFragment() {
    }

    public static Fragment newInstance() {
        return new FavoriteFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favoritePresenter  = new FavoritePresenterImpl();
        favoritePresenter.setView(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorite, container, false);

        initUI();

        favoriteListRealm = favoritePresenter.getFavoriteList();

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerFavoriteAdapter(favoriteListRealm);
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                favoritePresenter.removeFood(viewHolder.getAdapterPosition());
                mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        return view;
    }

    @Override
    public void makeSnackBar(final int position , final Food deleteFood) {
        Snackbar snackbar = Snackbar.make(favoriteCoordinatorLayout,
                "Item was removed successfully.",
                Snackbar.LENGTH_LONG).setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                super.onDismissed(snackbar, event);

            }
        }).setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favoritePresenter.addFood(position,deleteFood);
                mAdapter.notifyDataSetChanged();
            }
        }).setActionTextColor(Color.YELLOW);
        snackbar.show();
    }

    private void initUI() {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_favorite);
        favoriteCoordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.favorite_coordinator_layout);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Favorite");
    }
}
