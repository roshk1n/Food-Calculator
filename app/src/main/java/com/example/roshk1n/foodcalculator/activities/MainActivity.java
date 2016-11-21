package com.example.roshk1n.foodcalculator.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.roshk1n.foodcalculator.Localization;
import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.Session;
import com.example.roshk1n.foodcalculator.fragments.ChartFragment;
import com.example.roshk1n.foodcalculator.fragments.DiaryFragment;
import com.example.roshk1n.foodcalculator.fragments.FavoriteFragment;
import com.example.roshk1n.foodcalculator.fragments.ProfileFragment;
import com.example.roshk1n.foodcalculator.fragments.RemindersFragment;
import com.example.roshk1n.foodcalculator.interfaces.OnFragmentListener;
import com.example.roshk1n.foodcalculator.presenters.MainPresenterImpl;
import com.example.roshk1n.foodcalculator.utils.Utils;
import com.example.roshk1n.foodcalculator.views.MainView;
import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NavigationView.OnClickListener,
        OnFragmentListener, MainView, View.OnFocusChangeListener {

    private MainPresenterImpl presenter;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawer;
    private CircleImageView icoUserDrawerIv;
    private TextView fullNameDrawerTv;
    private FloatingActionButton addFoodFab;
    private CoordinatorLayout coordinatorHintAdd;
    private LinearLayout ukLayout;
    private LinearLayout enLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();

        presenter = new MainPresenterImpl();
        presenter.setView(this);

        setSupportActionBar(mToolbar);

        updateDrawer();

        Utils.navigateToFragment(getSupportFragmentManager(),
                R.id.fragment_conteiner,
                DiaryFragment.newInstance(),
                FragmentTransaction.TRANSIT_FRAGMENT_OPEN,
                false);

        icoUserDrawerIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.closeDrawers();
                Fragment fr = getSupportFragmentManager().findFragmentById(R.id.fragment_conteiner);
                if (!(fr instanceof ProfileFragment)) {
                    coordinatorHintAdd.setVisibility(View.INVISIBLE);
                    Utils.navigateToFragment(getSupportFragmentManager(),
                            R.id.fragment_conteiner,
                            ProfileFragment.newInstance(),
                            FragmentTransaction.TRANSIT_FRAGMENT_OPEN,
                            false);
                    addFoodFab.hide();
                }
            }
        });
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
    protected void onDestroy() {
        presenter.destroy();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        Fragment fr = getSupportFragmentManager().findFragmentById(R.id.fragment_conteiner);

        if (id == R.id.nav_diary) {
            if (!(fr instanceof DiaryFragment)) {
                Utils.navigateToFragment(getSupportFragmentManager(),
                        R.id.fragment_conteiner,
                        DiaryFragment.newInstance(),
                        FragmentTransaction.TRANSIT_FRAGMENT_OPEN,
                        false);
            }

        } else if (id == R.id.nav_favorites) {
            if (!(fr instanceof FavoriteFragment)) {
                Utils.navigateToFragment(getSupportFragmentManager(),
                        R.id.fragment_conteiner,
                        FavoriteFragment.newInstance(),
                        FragmentTransaction.TRANSIT_FRAGMENT_OPEN,
                        false);
                addFoodFab.hide();
                coordinatorHintAdd.setVisibility(View.INVISIBLE);
            }

        } else if (id == R.id.nav_chart) {
            if (!(fr instanceof ChartFragment)) {
                Utils.navigateToFragment(getSupportFragmentManager(),
                        R.id.fragment_conteiner,
                        ChartFragment.newInstance(),
                        FragmentTransaction.TRANSIT_FRAGMENT_OPEN,
                        false);
                addFoodFab.hide();
                coordinatorHintAdd.setVisibility(View.INVISIBLE);
            }
        } else if (id == R.id.nav_reminders) {
            if (!(fr instanceof RemindersFragment)) {
                Utils.navigateToFragment(getSupportFragmentManager(),
                        R.id.fragment_conteiner,
                        RemindersFragment.newInstance(),
                        FragmentTransaction.TRANSIT_FRAGMENT_OPEN,
                        false);

                addFoodFab.hide();
                coordinatorHintAdd.setVisibility(View.INVISIBLE);
            }

        } else if (id == R.id.nav_profile) {
            if (!(fr instanceof ProfileFragment)) {
                Utils.navigateToFragment(getSupportFragmentManager(),
                        R.id.fragment_conteiner,
                        ProfileFragment.newInstance(),
                        FragmentTransaction.TRANSIT_FRAGMENT_OPEN,
                        false);

                addFoodFab.hide();
                coordinatorHintAdd.setVisibility(View.INVISIBLE);
            }

        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            if (AccessToken.getCurrentAccessToken() != null && Profile.getCurrentProfile() != null)
                LoginManager.getInstance().logOut();
            Session.destroy();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();

        } else if (id == R.id.nav_about) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setView(R.layout.about_app);
            builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            final AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        } else if (id == R.id.nav_languages) {
            final LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.language_alert, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.choose_language));
            builder.setView(view);
            builder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Localization.setLocale(getContext());
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            });
            builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            final AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            ukLayout = (LinearLayout) view.findViewById(R.id.ukraine_liner);
            enLayout = (LinearLayout) view.findViewById(R.id.english_liner);

            if (Localization.getLanguage().equals("uk")) {
                ukLayout.requestFocus();
                enLayout.setSelected(true);
            } else {
                enLayout.requestFocus();
                enLayout.setSelected(true);
            }
            ukLayout.setOnFocusChangeListener(this);
            enLayout.setOnFocusChangeListener(this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) { // back for arrow item menu
        onBackPressed();
    }

    @Override
    public void setDrawerMenu() {
        mNavigationView.setItemIconTintList(null);
        mNavigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                mDrawer,
                mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {
        };
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void enableMenuSwipe() {
        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    @Override
    public void updateDrawer() {
        fullNameDrawerTv.setText(Session.getInstance().getFullname());
        if (Utils.isConnectNetwork(getApplicationContext())) {
            Glide
                    .with(getApplicationContext())
                    .load(Session.getInstance().getUrlPhoto())
                    .asBitmap()
                    .fitCenter()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            icoUserDrawerIv.setImageBitmap(resource);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    presenter.updateInfoUser(resource);
                                }
                            }).start();
                        }
                    });
        } else {
            Bitmap imageUser = presenter.getLocalImage();
            icoUserDrawerIv.setImageBitmap(imageUser);
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v == ukLayout) {
            Localization.setLanguage("uk");
        } else if (v == enLayout) {
            Localization.setLanguage("en");
        }
    }

    @Override
    public void updateDrawerLight() {
        fullNameDrawerTv.setText(Session.getInstance().getFullname());
        Log.d("My", Session.getInstance().getUrlPhoto());
        Glide
                .with(getApplicationContext())
                .load(Session.getInstance().getUrlPhoto())
                .into(icoUserDrawerIv);
    }

    @Override
    public void hideToolbar() {
        mToolbar.setVisibility(View.GONE);
    }

    @Override
    public void showToolbar() {
        mToolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void setTitle(String title) {
        mToolbar.setTitle(title);
    }

    @SuppressLint("PrivateResource")
    @Override
    public void setArrowToolbar() {
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.arrow_back_white));
        mToolbar.setNavigationOnClickListener(this);
    }

    @Override
    public void disabledMenuSwipe() {
        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    private void initUI() {
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        View mHeader = mNavigationView.getHeaderView(0);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        icoUserDrawerIv = (CircleImageView) mHeader.findViewById(R.id.imageView);
        fullNameDrawerTv = (TextView) mHeader.findViewById(R.id.tvNameDrawer);
        addFoodFab = (FloatingActionButton) findViewById(R.id.addFood_fab);
        coordinatorHintAdd = (CoordinatorLayout) findViewById(R.id.hint_add_food_coordinator);
        mToolbar.setTitle(getString(R.string.diary));
    }
}
