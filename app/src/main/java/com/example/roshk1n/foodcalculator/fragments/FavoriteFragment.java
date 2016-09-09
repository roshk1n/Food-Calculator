package com.example.roshk1n.foodcalculator.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.adapters.RecyclerFavoriteAddAdapter;
import com.example.roshk1n.foodcalculator.interfaces.OnFragmentListener;
import com.example.roshk1n.foodcalculator.adapters.RecyclerFavoriteAdapter;
import com.example.roshk1n.foodcalculator.presenters.FavoritePresenterImpl;
import com.example.roshk1n.foodcalculator.views.FavoriteView;
import com.example.roshk1n.foodcalculator.responseAdapter.CallbackFavoriteAdapter;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;
import com.example.roshk1n.foodcalculator.utils.Utils;

import java.util.ArrayList;

public class FavoriteFragment extends Fragment implements FavoriteView, CallbackFavoriteAdapter {
    private static final String DATE_KEY = "date";
    private static final String CHECK_KEY = "check";
    private FavoritePresenterImpl favoritePresenter;
    private ArrayList<Food> favoriteList = new ArrayList<>();
    private OnFragmentListener mFragmentListener;
    private RecyclerView mRecyclerView;
    private RecyclerFavoriteAdapter mAdapter;
    private LinearLayout noItemLayout;
    private boolean addOrInfo = false;
    private long date;

    private View view;
    private CoordinatorLayout favoriteCoordinatorLayout;

    public FavoriteFragment() {
    }

    public static Fragment newInstance() {
        return new FavoriteFragment();
    }

    public static Fragment newInstance(long date, boolean addOrInfo) {
        FavoriteFragment favoriteFragment = new FavoriteFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(CHECK_KEY, addOrInfo);
        bundle.putLong(DATE_KEY, date);
        favoriteFragment.setArguments(bundle);
        return favoriteFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favoritePresenter = new FavoritePresenterImpl();
        favoritePresenter.setView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorite, container, false);

        initUI();
        if (mFragmentListener != null) {
            mFragmentListener.setTitle(getString(R.string.favorites));
            mFragmentListener.setDrawerMenu();
            mFragmentListener.enableMenuSwipe();
        }

        if (getArguments() != null) {
            addOrInfo = getArguments().getBoolean(CHECK_KEY);
            date = getArguments().getLong(DATE_KEY);
        }

        favoritePresenter.getFavoriteList();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (!addOrInfo) {
            ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                    Food removedFood = favoriteList.remove(viewHolder.getAdapterPosition());
                    makeSnackBarAction(viewHolder.getAdapterPosition(), removedFood);
                    mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                }
            };
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
            itemTouchHelper.attachToRecyclerView(mRecyclerView);
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentListener) {
            mFragmentListener = (OnFragmentListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentListener = null;
    }

    @Override
    public void setFavoriteList(ArrayList<Food> favFoods) {
        favoriteList = favFoods;
        if (favoriteList.size() != 0) {
            if (!addOrInfo) {
                mAdapter = new RecyclerFavoriteAdapter(favoriteList, this);
                mRecyclerView.setAdapter(mAdapter);
            } else {
                RecyclerFavoriteAddAdapter mAddAdapter = new RecyclerFavoriteAddAdapter(favoriteList, this);
                mRecyclerView.setAdapter(mAddAdapter);
            }
        } else {
            noItemLayout.setVisibility(View.VISIBLE);
        }
    }

    private void makeSnackBarAction(final int position, final Food removed) {
        Snackbar snackbar = Snackbar.make(favoriteCoordinatorLayout,
                getString(R.string.item_was_removed),
                Snackbar.LENGTH_LONG).setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                super.onDismissed(snackbar, event);
                if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                    favoritePresenter.removeFavoriteFoodDB(removed.getNdbno());
                }
            }
        }).setAction(getString(R.string.undo), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favoriteList.add(position, removed);
                mAdapter.notifyItemInserted(position);

            }
        }).setActionTextColor(Color.YELLOW);
        snackbar.show();
    }

    private void initUI() {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_favorite);
        favoriteCoordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.favorite_coordinator_layout);
        noItemLayout = (LinearLayout) view.findViewById(R.id.no_favorite_container);
    }

    @Override
    public void navigateToInfoFood(Food food) {
        Utils.navigateToFragment(getActivity().getSupportFragmentManager(),
                R.id.fragment_conteiner,
                InfoFoodFragment.newInstance(food),
                FragmentTransaction.TRANSIT_FRAGMENT_OPEN,
                true);
    }

    @Override
    public void navigateToAddFood(Food food) {
        food.setTime(date);
        Utils.navigateToFragment(getActivity().getSupportFragmentManager(),
                R.id.fragment_conteiner,
                AddFoodFragment.newInstance(food),
                FragmentTransaction.TRANSIT_FRAGMENT_OPEN,
                true);
    }
}
