package com.example.roshk1n.foodcalculator.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.Session;
import com.example.roshk1n.foodcalculator.login.LoginActivity;
import com.example.roshk1n.foodcalculator.main.fragments.diary.DiaryFragment;
import com.example.roshk1n.foodcalculator.main.fragments.favorite.FavoriteFragment;
import com.example.roshk1n.foodcalculator.main.fragments.remiders.RemindersFragment;
import com.example.roshk1n.foodcalculator.main.fragments.profile.ProfileFragment;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NavigationView.OnClickListener{

    private View mHeader;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawer;
    private CircleImageView icoUserDrawerIv;
    private TextView fullNameDrawerTv;
    private FloatingActionButton addFoodFab;
    private FragmentManager fragmentManager;
    private CoordinatorLayout coordinatorHintAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();

        setSupportActionBar(mToolbar);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragment_conteiner, DiaryFragment.newInstance())
                .commit();
        icoUserDrawerIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.closeDrawers();
                Fragment fr = getSupportFragmentManager().findFragmentById(R.id.fragment_conteiner);
                if(!(fr instanceof ProfileFragment)) {
                    coordinatorHintAdd.setVisibility(View.INVISIBLE);
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_conteiner, ProfileFragment.newInstance())
                            .addToBackStack(null)
                            .commit();
                    addFoodFab.hide();
                }
            }
        });
        //Firebase
/*        fullNameDrawerTv.setText(FirebaseHelper.getmFirebaseUser().getDisplayName());
        Glide.with(this).load(FirebaseHelper.getmFirebaseUser().getPhotoUrl().toString()).into(icoUserDrawerIv);*/

//Realm
        updateDrawer();
////////////

        setUpDrawerMenu();
        enableMenuSwipe();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            addFoodFab.hide();
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case R.id.action_logout:
            {
                FirebaseAuth.getInstance().signOut();
                Session.destroy();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();
            } break;

        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        Fragment fr = getSupportFragmentManager().findFragmentById(R.id.fragment_conteiner);

        if (id == R.id.nav_diary) {
            if(!(fr instanceof DiaryFragment)) {
                fragmentManager.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.fragment_conteiner, DiaryFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
            }

        }  else if (id == R.id.nav_favorites) {
            if(!(fr instanceof FavoriteFragment)) {
                fragmentManager.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.fragment_conteiner, FavoriteFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                addFoodFab.hide();
                coordinatorHintAdd.setVisibility(View.INVISIBLE);
            }

        } else if (id == R.id.nav_statistic) {

        } else if (id == R.id.nav_reminders) {
            if(!(fr instanceof RemindersFragment)) {
                fragmentManager.beginTransaction()
                        /*.setCustomAnimations(R.anim.slide_in_left_enter, R.anim.slide_in_left_exit)*/ //TODO anim slide
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.fragment_conteiner, RemindersFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                addFoodFab.hide();
                coordinatorHintAdd.setVisibility(View.INVISIBLE);
            }

        } else if (id == R.id.nav_profile) {
            if(!(fr instanceof ProfileFragment)) {
                fragmentManager.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.fragment_conteiner, ProfileFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                addFoodFab.hide();
                coordinatorHintAdd.setVisibility(View.INVISIBLE);
            }

        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            Session.destroy();
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }

    private void initUI() {
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mHeader=mNavigationView.getHeaderView(0);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        icoUserDrawerIv = (CircleImageView) mHeader.findViewById(R.id.imageView);
        fullNameDrawerTv = (TextView) mHeader.findViewById(R.id.tvNameDrawer);
        addFoodFab = (FloatingActionButton) findViewById(R.id.addFood_fab);
        coordinatorHintAdd = (CoordinatorLayout) findViewById(R.id.hint_add_food_coordinator);

    }

    public void setUpToolbarArrow() {
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_mtrl_am_alpha));
        mToolbar.setNavigationOnClickListener(this);
    }

    public void disableMenuSwipe() {
        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void enableMenuSwipe() {
        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    public void setUpDrawerMenu() {

        mNavigationView.setItemIconTintList(null);
        mNavigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                mDrawer,
                mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();
        mNavigationView.getMenu().getItem(0).setChecked(true);
    }

    public void updateDrawer() {
        fullNameDrawerTv.setText(Session.getInstance().getFullname());
        Bitmap imageUser = null;
        try {
            byte [] encodeByte=Base64.decode(Session.getInstance().getUrlPhoto(), Base64.DEFAULT);
            imageUser = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch(Exception e) {
            e.getMessage();
        }
        icoUserDrawerIv.setImageBitmap(imageUser);
    }
}
