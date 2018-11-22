package id.itsofteam.sac.sac.fragments;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.itsofteam.sac.sac.R;
import id.itsofteam.sac.sac.adapters.AdapterFoodCategory;
import id.itsofteam.sac.sac.json.Configuration;
import id.itsofteam.sac.sac.json.JsonUtils;
import id.itsofteam.sac.sac.models.ItemFoodCategory;

/**
 * Created by ferdy on 29/11/2016.
 */

public class FragmentMakanan extends Fragment {

    RecyclerView recyclerView;
    List<ItemFoodCategory> itemFoodCategoriesList;
    AdapterFoodCategory kategoriFoodAdapter;
    ArrayList<String> array_kategori_food,array_kategori_food_id, array_kategori_food_nama, array_kategori_food_image;
    String[] str_food_kategori,str_food_kategori_nama,str_food_kategori_id, str_food_kategori_image;
    ItemFoodCategory itemKategoriFoodList;
    JsonUtils util;
    int textLength = 0;
    SwipeRefreshLayout swipeRefreshLayout = null;
    ProgressBar progressBar;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_food_category, container, false);
        setHasOptionsMenu(true);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#1abc9c"),Color.parseColor("#f1c40f"),Color.parseColor("#379adc"),Color.parseColor("#e74c3c"), Color.parseColor("#34495e"));

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(3), true));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        itemFoodCategoriesList = new ArrayList<ItemFoodCategory>();
        array_kategori_food = new ArrayList<String>();
        array_kategori_food_id = new ArrayList<String>();
        array_kategori_food_nama = new ArrayList<String>();
        array_kategori_food_image = new ArrayList<String>();



        str_food_kategori = new String[array_kategori_food.size()];
        str_food_kategori_id = new String[array_kategori_food_id.size()];
        str_food_kategori_nama = new String[array_kategori_food_nama.size()];
        str_food_kategori_image = new String[array_kategori_food_image.size()];



        util = new JsonUtils(this.getActivity());

        if (JsonUtils.isNetworkAvailable(getActivity())) {
            new MyTaskKategori().execute(Configuration.API+"?food");
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.failed_connect_network), Toast.LENGTH_SHORT).show();
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
                        new MyTaskKategori().execute(Configuration.API+"?food");
                    }
                }, 3000);
            }
        });

        return v;
    }

    public void clearData() {
        int size = this.itemFoodCategoriesList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.itemFoodCategoriesList.remove(0);
            }

            kategoriFoodAdapter.notifyItemRangeRemoved(0, size);
        }
    }

    private class MyTaskKategori extends AsyncTask<String, Void, String> {

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
                Toast.makeText(getActivity(), getResources().getString(R.string.failed_connect_network), Toast.LENGTH_SHORT).show();
            } else {

                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(Configuration.ARRAY);
                    JSONObject objJson = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);

                        ItemFoodCategory objItem = new ItemFoodCategory();

                        objItem.setCat_m_id(objJson.getInt(Configuration.ID_CAT_M));
                        objItem.setCat_m_name(objJson.getString(Configuration.NAMA_CAT_M));
                        objItem.setCat_m_image(objJson.getString(Configuration.IMAGE_CAT_M));



                        itemFoodCategoriesList.add(objItem);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int j = 0; j < itemFoodCategoriesList.size(); j++) {

                    itemKategoriFoodList = itemFoodCategoriesList.get(j);

                    array_kategori_food_id.add(String.valueOf(itemKategoriFoodList.getCat_m_id()));
                    str_food_kategori_id= array_kategori_food_id.toArray(str_food_kategori_id);

                    array_kategori_food_nama.add(itemKategoriFoodList.getCat_m_name());
                    str_food_kategori_nama = array_kategori_food_nama.toArray(str_food_kategori_nama);

                    array_kategori_food_image.add(String.valueOf(itemKategoriFoodList.getCat_m_image()));
                    str_food_kategori_image= array_kategori_food_image.toArray(str_food_kategori_image);




                }

                setAdapterToRecyclerView();
            }

        }
    }

    private class RefreshTask extends AsyncTask<String, Void, String> {

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
                Toast.makeText(getActivity(), getResources().getString(R.string.failed_connect_network), Toast.LENGTH_SHORT).show();
            } else {

                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(Configuration.ARRAY);
                    JSONObject objJson = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);

                        ItemFoodCategory objItem = new ItemFoodCategory();

                        objItem.setCat_m_id(objJson.getInt(Configuration.ID_CAT_M));
                        objItem.setCat_m_name(objJson.getString(Configuration.NAMA_CAT_M));
                        objItem.setCat_m_image(objJson.getString(Configuration.IMAGE_CAT_M));


                        itemFoodCategoriesList.add(objItem);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int j = 0; j < itemFoodCategoriesList.size(); j++) {

                    itemKategoriFoodList = itemFoodCategoriesList.get(j);

                    array_kategori_food_id.add(String.valueOf(itemKategoriFoodList.getCat_m_id()));
                    str_food_kategori_id= array_kategori_food_id.toArray(str_food_kategori_id);

                    array_kategori_food_nama.add(itemKategoriFoodList.getCat_m_name());
                    str_food_kategori_nama = array_kategori_food_nama.toArray(str_food_kategori_nama);

                    array_kategori_food_image.add(String.valueOf(itemKategoriFoodList.getCat_m_image()));
                    str_food_kategori_image= array_kategori_food_image.toArray(str_food_kategori_image);


                }

                setAdapterToRecyclerView();
            }
        }
    }

    public void setAdapterToRecyclerView() {
        kategoriFoodAdapter = new AdapterFoodCategory (getActivity(), itemFoodCategoriesList);
        recyclerView.setAdapter(kategoriFoodAdapter);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);





    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:

                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
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
