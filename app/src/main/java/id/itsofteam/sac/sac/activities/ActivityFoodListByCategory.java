package id.itsofteam.sac.sac.activities;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.itsofteam.sac.sac.R;
import id.itsofteam.sac.sac.adapters.AdapterFoodByCategory;
import id.itsofteam.sac.sac.adapters.AdapterFoodCategory;
import id.itsofteam.sac.sac.json.Configuration;
import id.itsofteam.sac.sac.json.JsonUtils;
import id.itsofteam.sac.sac.models.ItemFoodCategory;
import id.itsofteam.sac.sac.models.ItemFoodList;

/**
 * Created by ferdy on 30/11/2016.
 */

public class ActivityFoodListByCategory extends AppCompatActivity {

    RecyclerView recyclerView;
    List<ItemFoodList> arrayitemFoodList;
    AdapterFoodByCategory byKategoriFoodAdapter;
    ArrayList<String> array_byfood, array_byfood_item_id, array_byfood_item_nama, array_byfood_item_harga, array_byfood_item_image;
    String[] str_byfood, str_byfood_item_id, str_byfood_item_nama, str_byfood_item_harga, str_food_item_image;
    ItemFoodList itemFoodList;
    JsonUtils util;
    int textLength = 0;
    SwipeRefreshLayout swipeRefreshLayout = null;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list_by_category);

        final Typeface font1 = Typeface.createFromAsset(getBaseContext().getAssets(), "fonts/saff-bold.ttf");

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#1abc9c"),Color.parseColor("#f1c40f"),Color.parseColor("#379adc"),Color.parseColor("#e74c3c"),Color.parseColor("#34495e"));
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(3), true));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());



        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.left_arrow);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        centerToolbarTitle(toolbar);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Food");
        }

        TextView toolbarTitle = null;
        for (int i = 0; i < toolbar.getChildCount(); ++i) {
            View child = toolbar.getChildAt(i);

            // assuming that the title is the first instance of TextView
            // you can also check if the title string matches
            if (child instanceof TextView) {
                toolbarTitle = (TextView)child;
                toolbarTitle.setTypeface(font1);
                break;
            }
        }

        arrayitemFoodList = new ArrayList<ItemFoodList>();
        array_byfood= new ArrayList<String>();
        array_byfood_item_id = new ArrayList<String>();
        array_byfood_item_nama = new ArrayList<String>();
        array_byfood_item_harga = new ArrayList<String>();
        array_byfood_item_image = new ArrayList<String>();


        str_byfood = new String[array_byfood.size()];
        str_byfood_item_id = new String[array_byfood_item_id.size()];
        str_byfood_item_nama = new String[array_byfood_item_nama.size()];
        str_byfood_item_harga = new String[array_byfood_item_harga.size()];
        str_food_item_image = new String[array_byfood_item_image.size()];


        util = new JsonUtils(getApplicationContext());

        if (JsonUtils.isNetworkAvailable(ActivityFoodListByCategory.this)) {
            new MyTaskBeritaCat().execute(Configuration.API + "?food_id=" + Configuration.FOOD_ID);
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.failed_connect_network), Toast.LENGTH_SHORT).show();
        }

        // Using to refresh webpage when user swipes the screen
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        clearData();
                        new MyTaskBeritaCat().execute(Configuration.API + "?food_id=" + Configuration.FOOD_ID);
                    }
                }, 3000);
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
            titleView.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
            final Toolbar.LayoutParams layoutParams = (Toolbar.LayoutParams) titleView.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            titleView.setText("SAC");
            titleView.setTypeface(font2);
            titleView.setTextColor(Color.parseColor("#ffffff"));
            toolbar.requestLayout();
            //also you can use titleView for changing font: titleView.setTypeface(Typeface);
        }
    }

    public void clearData() {
        int size = this.arrayitemFoodList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.arrayitemFoodList.remove(0);
            }

            byKategoriFoodAdapter.notifyItemRangeRemoved(0, size);
        }
    }

    private class MyTaskBeritaCat extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            return JsonUtils.getJSONString(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressBar.setVisibility(View.GONE);

            if (null == result || result.length() == 0) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.failed_connect_network), Toast.LENGTH_SHORT).show();
            } else {

                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(Configuration.ARRAY);
                    JSONObject objJson = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);

                        ItemFoodList objItem = new ItemFoodList();

                        objItem.setItem_m_id(objJson.getInt(Configuration.ITEM_M_ID));
                        objItem.setItem_m_name(objJson.getString(Configuration.ITEM_M_NAME));
                        objItem.setHarga_m(objJson.getInt(Configuration.HARGA_M));
                        objItem.setItem_m_image(objJson.getString(Configuration.ITEM_M_IMAGE));


                        arrayitemFoodList.add(objItem);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int j = 0; j < arrayitemFoodList.size(); j++) {

                    itemFoodList = arrayitemFoodList.get(j);

                    array_byfood_item_id.add(String.valueOf(itemFoodList.getItem_m_id()));
                    str_byfood_item_id = array_byfood_item_id.toArray(str_byfood_item_id);

                    array_byfood_item_nama.add(itemFoodList.getItem_m_name());
                    str_byfood_item_nama = array_byfood_item_nama.toArray(str_byfood_item_nama);

                    array_byfood_item_harga.add(String.valueOf(itemFoodList.getHarga_m()));
                    str_byfood_item_harga= array_byfood_item_harga.toArray(str_byfood_item_harga);

                    array_byfood_item_image.add(String.valueOf(itemFoodList.getItem_m_image()));
                    str_food_item_image = array_byfood_item_image.toArray(str_food_item_image);


                }

                setAdapterToRecyclerView();
            }
        }
    }

    public void setAdapterToRecyclerView() {
        byKategoriFoodAdapter= new AdapterFoodByCategory(this, arrayitemFoodList);
        recyclerView.setAdapter(byKategoriFoodAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
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
    protected void onDestroy() {

        super.onDestroy();
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % spanCount;

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount;
                outRect.right = (column + 1) * spacing / spanCount;

                if (position < spanCount) {
                    outRect.top = spacing;
                }
                outRect.bottom = spacing;
            } else {
                outRect.left = column * spacing / spanCount;
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                if (position >= spanCount) {
                    outRect.top = spacing;
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }



}
