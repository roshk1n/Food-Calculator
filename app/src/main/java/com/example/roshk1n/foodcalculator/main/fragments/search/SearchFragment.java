package com.example.roshk1n.foodcalculator.main.fragments.search;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import com.example.roshk1n.foodcalculator.MyApplication;
import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.main.MainActivity;
import com.example.roshk1n.foodcalculator.main.adapters.RecyclerSearchAdapter;
import com.example.roshk1n.foodcalculator.main.fragments.infoFood.InfoFoodFragment;
import com.example.roshk1n.foodcalculator.rest.RestClient;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.Food;
import com.example.roshk1n.foodcalculator.rest.model.ndbApi.response.NutrientFoodResponse;
import java.util.ArrayList;

public class SearchFragment extends Fragment implements SearchView, ResponseAdapter {

    private SearchPresenterImpl searchPresenter;
    private RestClient restClient;
    private long mdate=0;

    private View view;
    private EditText searchEt;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerSearchAdapter mAdapter;
    private ArrayList<NutrientFoodResponse> nutrientFoodResponses = new ArrayList<>();

    public SearchFragment() {}

    public static Fragment newInstance() { return  new SearchFragment(); }

    public static SearchFragment newInstance(long date) {
        SearchFragment searchFragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("date", date);
        searchFragment.setArguments(bundle);
        return searchFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);

        initUI();

      //  ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Search");

        searchPresenter = new SearchPresenterImpl();
        searchPresenter.setView(this);

        Bundle bundle = getArguments();
        if(bundle != null) {
            mdate = bundle.getLong("date");
        }
        restClient = MyApplication.getRestClient();

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerSearchAdapter(nutrientFoodResponses, mdate,this);
        mRecyclerView.setAdapter(mAdapter);

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        mRecyclerView.setItemAnimator(itemAnimator);

        searchEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (searchEt.getText().length() != 0) {
                        nutrientFoodResponses.clear();
                        mAdapter.notifyDataSetChanged();
                        searchPresenter.searchFood(searchEt.getText().toString());
                        hideKeyboard();
                    }
                    return true;
                }
                return false;
            }
        });
        return view;
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
    public void updateUI(NutrientFoodResponse nutrientFoodResponses) {
        this.nutrientFoodResponses.add(nutrientFoodResponses);
        mAdapter.notifyItemInserted(this.nutrientFoodResponses.size());
    }

    @Override
    public void navigateToInfoFood(Food food) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragment_conteiner, InfoFoodFragment.newInstance(food))
                .addToBackStack(null)
                .commit();
    }

    private void initUI() {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_search);
        searchEt = (EditText) view.findViewById(R.id.et_food_name);
    }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(MainActivity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
