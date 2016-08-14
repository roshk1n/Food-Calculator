package com.example.roshk1n.foodcalculator.fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.adapters.RecyclerFavoriteAdapter;
import com.example.roshk1n.foodcalculator.presenters.FavoritePresenterImpl;
import com.example.roshk1n.foodcalculator.Views.FavoriteView;
import com.example.roshk1n.foodcalculator.responseAdapter.CallbackFavoriteAdapter;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;
import com.example.roshk1n.foodcalculator.utils.Utils;

import java.util.ArrayList;

public class FavoriteFragment extends Fragment implements FavoriteView, CallbackFavoriteAdapter {

    private FavoritePresenterImpl favoritePresenter;
    private ArrayList<Food> favoriteList = new ArrayList<>();
    private OnFavoriteListener mFavoriteListener;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerFavoriteAdapter mAdapter;

    private View view;
    private CoordinatorLayout favoriteCoordinatorLayout;

    public FavoriteFragment() {}

    public static Fragment newInstance() {
        return new FavoriteFragment();
    }

    public interface OnFavoriteListener {
        void setDrawerMenu();
        void enableMenuSwipe();
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

        if(mFavoriteListener != null) {
            mFavoriteListener.setDrawerMenu();
            mFavoriteListener.enableMenuSwipe();
        }

        favoriteList = favoritePresenter.getFavoriteList();

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerFavoriteAdapter(favoriteList,this);
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                Food removedFood = favoriteList.remove(viewHolder.getAdapterPosition());
                makeSnackBarAction(viewHolder.getAdapterPosition(),removedFood);
                mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFavoriteListener) {
            mFavoriteListener = (OnFavoriteListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFavoriteListener = null;
    }

    @Override
    public void makeSnackBar(String text) {
        Snackbar.make(favoriteCoordinatorLayout,text,Snackbar.LENGTH_SHORT).show();
    }

    private void makeSnackBarAction(final int position, final Food removed) {
        Snackbar snackbar = Snackbar.make(favoriteCoordinatorLayout,
                "Item was removed successfully.",
                Snackbar.LENGTH_LONG).setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                super.onDismissed(snackbar, event);
                if(event!=Snackbar.Callback.DISMISS_EVENT_ACTION) {
                    favoritePresenter.removeFavoriteFoodDB(position);
                }
            }
        }).setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favoriteList.add(position,removed);
                mAdapter.notifyItemInserted(position);

            }
        }).setActionTextColor(Color.YELLOW);
        snackbar.show();
    }

    private void initUI() {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_favorite);
        favoriteCoordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.favorite_coordinator_layout);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Favorite");
    }

    @Override
    public void navigateToInfoFood(Food food) {
        Utils.navigateToFragment(getActivity().getSupportFragmentManager(),
                R.id.fragment_conteiner,
                InfoFoodFragment.newInstance(food),
                FragmentTransaction.TRANSIT_FRAGMENT_OPEN,
                true);
    }
}
