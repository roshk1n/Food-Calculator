package com.example.roshk1n.foodcalculator.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.adapters.RecyclerSearchAdapter;
import com.example.roshk1n.foodcalculator.interfaces.responseAdapter.CallbackSearchAdapter;
import com.example.roshk1n.foodcalculator.presenters.SearchPresenterImpl;
import com.example.roshk1n.foodcalculator.views.SearchView;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;
import com.example.roshk1n.foodcalculator.utils.Utils;

import java.util.ArrayList;

public class SearchFragment extends Fragment implements SearchView, CallbackSearchAdapter {
    private final static String DATE_KEY = "date";
    private final static String QUERY_KEY = "query";

    private SearchPresenterImpl searchPresenter;
    private RecyclerView mRecyclerView;
    private RecyclerSearchAdapter mAdapter;
    private ArrayList<Food> foods = new ArrayList<>();
    private long mdate=0;
    private String querySearch;
    private ProgressDialog searchProgress;

    private View view;

    public  static SearchFragment newInstance() {
        return new SearchFragment();
    }

    public static SearchFragment newInstance(long date, String query) {
        SearchFragment searchFragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(DATE_KEY, date);
        bundle.putString(QUERY_KEY, query);
        searchFragment.setArguments(bundle);
        return searchFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        searchPresenter = new SearchPresenterImpl();
        searchPresenter.setView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        initUI();

        if(getArguments() != null) {
            mdate = getArguments().getLong(DATE_KEY);
            querySearch = getArguments().getString(QUERY_KEY);
        }

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerSearchAdapter(foods, mdate,this);
        mRecyclerView.setAdapter(mAdapter);

        if(querySearch != null) {
            foods.clear();
            mAdapter.notifyDataSetChanged();
            searchProgress.setMessage(getString(R.string.wait_please));
            searchProgress.show();
            searchPresenter.searchFood(querySearch);
            Utils.hideKeyboard(getContext(),getActivity().getCurrentFocus());
        }

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        mRecyclerView.setItemAnimator(itemAnimator);
        return view;
    }

    @Override
    public void onDetach() {
        searchPresenter.destroy();
        super.onDetach();
    }

    @Override
    public void setFoodNutrients(Food foods) {
        this.foods.add(foods);
        mAdapter.notifyItemInserted(this.foods.size());
        searchProgress.dismiss();
    }

    @Override
    public void showSnackBar(String message) {
        Snackbar.make(view,message,Snackbar.LENGTH_SHORT).show();
        searchProgress.dismiss();
    }

    @Override
    public void navigateToAddFood(Food food) {
        Utils.navigateToFragment(getActivity().getSupportFragmentManager(),
                R.id.fragment_conteiner,
                AddFoodFragment.newInstance(food),
                FragmentTransaction.TRANSIT_FRAGMENT_OPEN,
                true);
    }

    private void initUI() {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_search);
        searchProgress = new ProgressDialog(getActivity());
        searchProgress.setCanceledOnTouchOutside(false);
        searchProgress.setCancelable(false);
    }
}
