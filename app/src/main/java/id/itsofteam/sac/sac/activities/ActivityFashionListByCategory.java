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
import id.itsofteam.sac.sac.adapters.AdapterFashionByCategory;
import id.itsofteam.sac.sac.json.Configuration;
import id.itsofteam.sac.sac.json.JsonUtils;
import id.itsofteam.sac.sac.models.ItemFashionList;

/**
 * Created by ferdy on 04/12/2016.
 */

public class ActivityFashionListByCategory extends AppCompatActivity {

    RecyclerView recyclerView;
    List<ItemFashionList> arrayitemFashionList;
    AdapterFashionByCategory byKategoriFashionAdapter;
    ArrayList<String> array_byfashion, array_byfashion_item_id, array_byfashion_item_nama, array_byfashion_item_harga, array_byfashion_item_image;
    String[] str_byfashion, str_byfashion_item_id, str_byfashion_item_nama, str_byfashion_item_harga, str_fashion_item_image;
    ItemFashionList itemFashionList;
    JsonUtils util;
    int textLength = 0;
    SwipeRefreshLayout swipeRefreshLayout = null;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fashion_list_by_category);

        final Typeface font1 = Typeface.createFromAsset(getBaseContext().getAssets(), "fonts/saff-bold.ttf");

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#1abc9c"),Color.parseColor("#f1c40f"),Color.parseColor("#379adc"),Color.parseColor("#e74c3c"),Color.parseColor("#34495e"));
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.addItemDecoration(new ActivityFashionListByCategory.GridSpacingItemDecoration(1, dpToPx(3), true));
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
            getSupportActionBar().setTitle("Fashion");
        }


        arrayitemFashionList = new ArrayList<ItemFashionList>();
        array_byfashion= new ArrayList<String>();
        array_byfashion_item_id = new ArrayList<String>();
        array_byfashion_item_nama = new ArrayList<String>();
        array_byfashion_item_harga = new ArrayList<String>();
        array_byfashion_item_image = new ArrayList<String>();


        str_byfashion = new String[array_byfashion.size()];
        str_byfashion_item_id = new String[array_byfashion_item_id.size()];
        str_byfashion_item_nama = new String[array_byfashion_item_nama.size()];
        str_byfashion_item_harga = new String[array_byfashion_item_harga.size()];
        str_fashion_item_image = new String[array_byfashion_item_image.size()];


        util = new JsonUtils(getApplicationContext());

        if (JsonUtils.isNetworkAvailable(ActivityFashionListByCategory.this)) {
            new ActivityFashionListByCategory.MyTaskBeritaCat().execute(Configuration.API + "?fashion_id=" + Configuration.FASHION_DETAIL);
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
                        new ActivityFashionListByCategory.MyTaskBeritaCat().execute(Configuration.API + "?fashion_id=" + Configuration.FASHION_DETAIL);
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
        int size = this.arrayitemFashionList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.arrayitemFashionList.remove(0);
            }

            byKategoriFashionAdapter.notifyItemRangeRemoved(0, size);
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

                        ItemFashionList objItem = new ItemFashionList();

                        objItem.setItem_f_id(objJson.getInt(Configuration.ITEM_F_ID));
                        objItem.setItem_f_name(objJson.getString(Configuration.ITEM_F_NAME));
                        objItem.setHarga_f(objJson.getInt(Configuration.HARGA_F));
                        objItem.setItem_f_image(objJson.getString(Configuration.ITEM_F_IMAGE));


                        arrayitemFashionList.add(objItem);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int j = 0; j < arrayitemFashionList.size(); j++) {

                    itemFashionList = arrayitemFashionList.get(j);

                    array_byfashion_item_id.add(String.valueOf(itemFashionList.getItem_f_id()));
                    str_byfashion_item_id = array_byfashion_item_id.toArray(str_byfashion_item_id);

                    array_byfashion_item_nama.add(itemFashionList.getItem_f_name());
                    str_byfashion_item_nama = array_byfashion_item_nama.toArray(str_byfashion_item_nama);

                    array_byfashion_item_harga.add(String.valueOf(itemFashionList.getHarga_f()));
                    str_byfashion_item_harga= array_byfashion_item_harga.toArray(str_byfashion_item_harga);

                    array_byfashion_item_image.add(String.valueOf(itemFashionList.getItem_f_image()));
                    str_fashion_item_image = array_byfashion_item_image.toArray(str_fashion_item_image);

                }

                setAdapterToRecyclerView();
            }
        }
    }

    public void setAdapterToRecyclerView() {
        byKategoriFashionAdapter= new AdapterFashionByCategory(this, arrayitemFashionList);
        recyclerView.setAdapter(byKategoriFashionAdapter);
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
