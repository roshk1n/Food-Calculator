package com.example.roshk1n.foodcalculator.main;




import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.roshk1n.foodcalculator.R;
import com.example.roshk1n.foodcalculator.login.LoginActivity;
import com.example.roshk1n.foodcalculator.main.fragments.diary.DiaryFragment;
import com.example.roshk1n.foodcalculator.main.fragments.infoFood.InfoFoodFragment;
import com.example.roshk1n.foodcalculator.main.fragments.remiders.RemindersFragment;
import com.example.roshk1n.foodcalculator.main.fragments.search.SearchFragment;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static String TAG = "MyLog";

    private View mHeader;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawer;
    private CircleImageView mImageViewUserIco;
    private TextView mTextViewName;

    private FragmentManager fragmentManager;
    private SearchFragment searchFragment;
    private DiaryFragment diaryFragment;
    private InfoFoodFragment infoFoodFragment;
    private RemindersFragment remindersFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        mToolbar.setTitle("Main");
        setSupportActionBar(mToolbar);


//        searchFragment = new SearchFragment();
//        diaryFragment = new DiaryFragment();
//        infoFoodFragment = new InfoFoodFragment();
//        remindersFragment = new RemindersFragment();

      //  mTextViewName.setText(FirebaseHelper.getmFirebaseUser().getDisplayName());
        // Glide.with(this).load(FirebaseHelper.getmFirebaseUser().getPhotoUrl().toString()).into(mImageViewUserIco);



   /*     FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
            /*         menuItem = menu.findItem(R.id.action_username);
         menuItem.setTitle(username);*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id)
        {
            case R.id.action_logout:
            {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
            break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
//        Fragment fr = getSupportFragmentManager().findFragmentById(R.id.frame_container);
//        Fragment introFragment = getSupportFragmentManager().findFragmentById(R.id.intro_content);

        fragmentManager = getFragmentManager(); //TODO: make normal manager for fragments
        if (id == R.id.nav_diary) {
//            (fr instanceof DiaryFragment) {
//
//            }
            fragmentManager.beginTransaction()
                    .remove(infoFoodFragment)
                    .remove(searchFragment)
                    .add(R.id.fragment_conteiner,diaryFragment).addToBackStack(null).commit();

        } else if (id == R.id.nav_diets) {
//            fragmentManager.beginTransaction()
//                    .replace(R.id.fragment_conteiner, InfoFoodFragment.newInstance())
//                    .addToBackStack(null)
//                    .commit();

        } else if (id == R.id.nav_favorites) {
            fragmentManager.beginTransaction()
                    .remove(searchFragment)
                    .remove(diaryFragment)
                    .add(R.id.fragment_conteiner,infoFoodFragment).addToBackStack(null).commit();

        } else if (id == R.id.nav_history) {

        } else if (id == R.id.nav_remonders) {
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_conteiner,remindersFragment).addToBackStack(null).commit();

        } else if (id == R.id.nav_settings) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void initUI() {
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mHeader=mNavigationView.getHeaderView(0);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mImageViewUserIco = (CircleImageView) mHeader.findViewById(R.id.imageView);
        mTextViewName = (TextView) mHeader.findViewById(R.id.tvNameDrawer);
    }
}
