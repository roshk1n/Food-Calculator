package com.example.roshk1n.foodcalculator.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.interfaces.OnFragmentListener;

import java.util.ArrayList;
import java.util.List;

public class TabSearchFragment extends Fragment {

    private OnFragmentListener mFragmentListener;
    private long date = 0;

    private View view;
    private Toolbar mToolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private MenuItem searchItem;
    private SearchView searchView;
    private String mQuery;

    private ViewPagerAdapter adapter;

    public TabSearchFragment() {
    }

    public static TabSearchFragment newInstance() {
        return new TabSearchFragment();
    }

    public static TabSearchFragment newInstance(long date) {
        TabSearchFragment tabSearchFragment = new TabSearchFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("date", date);
        tabSearchFragment.setArguments(bundle);
        return tabSearchFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab_search, container, false);
        mFragmentListener.hideToolbar();

        if (getArguments() != null) {
            date = getArguments().getLong("date");
        }

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);

        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbarSearchTab);

       mToolbar.setTitle("Search");

        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(getContext(), R.drawable.abc_ic_ab_back_mtrl_am_alpha));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
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
    public void onStop() {
        super.onStop();
        mFragmentListener.showToolbar();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);

        searchItem = menu.findItem(R.id.action_search);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
            searchItem.setVisible(true);

            searchItem.expandActionView();
            searchView.setQuery(mQuery, false);
            searchView.clearFocus();
        }
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    adapter.removeFragment(0);
                    adapter.addFragment(SearchFragment.newInstance(date, query),"All",0);
                    viewPager.setAdapter(adapter);
                    mQuery =  query;
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(SearchFragment.newInstance(), "All");
        adapter.addFragment(FavoriteFragment.newInstance(date,true), "Favorites");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> mFragmentList = new ArrayList<>();
        private List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container,  position,  object);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        public void addFragment(Fragment fragment, String title, int position) {
            mFragmentList.add(position,fragment);
            mFragmentTitleList.add(position,title);
        }

        public void removeFragment(int position) {
            adapter.mFragmentList.remove(position);
            adapter.mFragmentTitleList.remove(position);
        }
    }
}