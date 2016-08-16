package com.example.roshk1n.foodcalculator.fragments;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.adapters.RecyclerSearchAdapter;
import com.example.roshk1n.foodcalculator.responseAdapter.CallbackSearchAdapter;
import com.example.roshk1n.foodcalculator.presenters.SearchPresenterImpl;
import com.example.roshk1n.foodcalculator.Views.SearchView;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;
import com.example.roshk1n.foodcalculator.utils.Utils;

import java.util.ArrayList;

public class SearchFragment extends Fragment implements SearchView, CallbackSearchAdapter {

    private SearchPresenterImpl searchPresenter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerSearchAdapter mAdapter;
    private ArrayList<Food> foods = new ArrayList<>();
    private long mdate=0;
    private OnSearchListener mListener;

    private EditText searchEt;
    private View view;

    public SearchFragment() {}

    public  static SearchFragment newInstance() {
        return new SearchFragment();
    }

    public static SearchFragment newInstance(long date) {
        SearchFragment searchFragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("date", date);
        searchFragment.setArguments(bundle);
        return searchFragment;
    }

    public interface OnSearchListener {
        void setArrowToolbar();
        void disabledMenuSwipe();
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

        if(mListener != null) {
            mListener.setArrowToolbar();
            mListener.disabledMenuSwipe();
        }

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Search");

        if(getArguments() != null) {
            mdate = getArguments().getLong("date");
        }

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerSearchAdapter(foods, mdate,this);
        mRecyclerView.setAdapter(mAdapter);

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        mRecyclerView.setItemAnimator(itemAnimator);

        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (searchEt.getText().length() != 0) {
                        foods.clear();
                        mAdapter.notifyDataSetChanged();
                        searchPresenter.searchFood(searchEt.getText().toString());
                        Utils.hideKeyboard(getContext(),getActivity().getCurrentFocus());
                    }
                    return true;
                }
                return false;
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSearchListener) {
            mListener = (OnSearchListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setFoodNutrients(Food foods) {
        this.foods.add(foods);
        mAdapter.notifyItemInserted(this.foods.size());
    }

    @Override
    public void setErrorNetwork() {
        Log.d("Myy","error network");
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
        searchEt = (EditText) view.findViewById(R.id.et_food_name);
    }
}
