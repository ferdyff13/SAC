package id.itsofteam.sac.sac;

import android.content.Intent;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;


import com.universum_english.floatinglibrary.FloatingActionMenu;

import java.io.IOException;
import java.util.ArrayList;


import id.itsofteam.sac.sac.activities.ActivityAbout;
import id.itsofteam.sac.sac.activities.ActivityCaraPembayaran;
import id.itsofteam.sac.sac.activities.ActivityCart;
import id.itsofteam.sac.sac.activities.ActivityCheckout;
import id.itsofteam.sac.sac.activities.ActivityConfirmMessage;
import id.itsofteam.sac.sac.activities.ActivityTutorial;
import id.itsofteam.sac.sac.json.DBHelper;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Fashion","Food"};
    int Numboftabs =2;
    LayoutParams layoutparams;
    static DBHelper dbhelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        centerToolbarTitle(toolbar);
        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);

        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorPrimaryDark);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

        dbhelper = new DBHelper(this);

        // create database
        try {
            dbhelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        // then, the database will be open to use
        try {
            dbhelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }

        FloatingActionMenu menu = (FloatingActionMenu)findViewById(R.id.fab_menu_circle);
        menu.setMultipleOfFB(2.2f);
        menu.setIsCircle(true);

        menu.setOnMenuItemClickListener(new FloatingActionMenu.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(FloatingActionMenu fam, int index, FloatingActionButton item) {
                String str = "";
                switch (index) {
                    case 0:
                        str = "Cart";
                        startActivity(new Intent(MainActivity.this, ActivityCart.class));
                        break;
                    case 1:
                        str = "Checkout";
                        startActivity(new Intent(MainActivity.this,ActivityCheckout.class));
                        break;
                    case 2:
                        str = "Information";
                        startActivity(new Intent(MainActivity.this, ActivityCaraPembayaran.class));
                        break;
                    case 3:
                        str = "About";
                        startActivity(new Intent(MainActivity.this, ActivityAbout.class));
                        break;
                    default:
                }
                Toast.makeText(getBaseContext().getApplicationContext(), str, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void centerToolbarTitle(@NonNull final Toolbar toolbar) {


        Typeface font2 = Typeface.createFromAsset(getBaseContext().getAssets(), "fonts/saff-bold.ttf");
        final CharSequence title = toolbar.getTitle();
        final ArrayList<View> outViews = new ArrayList<>(1);
        toolbar.findViewsWithText(outViews, title, View.FIND_VIEWS_WITH_TEXT);
        if (!outViews.isEmpty()) {
            final TextView titleView = (TextView) outViews.get(0);
            titleView.setGravity(Gravity.CENTER);
            final Toolbar.LayoutParams layoutParams = (Toolbar.LayoutParams) titleView.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            titleView.setText("SAC");
            titleView.setTypeface(font2);
            titleView.setTextColor(Color.parseColor("#ffffff"));
            toolbar.requestLayout();
            //also you can use titleView for changing font: titleView.setTypeface(Typeface);
        }
    }

}
