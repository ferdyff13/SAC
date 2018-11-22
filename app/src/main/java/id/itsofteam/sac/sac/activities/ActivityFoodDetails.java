package id.itsofteam.sac.sac.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Config;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Callback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import id.itsofteam.sac.sac.R;
import id.itsofteam.sac.sac.json.Configuration;
import id.itsofteam.sac.sac.json.DBHelper;
import id.itsofteam.sac.sac.json.JsonUtils;
import id.itsofteam.sac.sac.models.ItemFoodList;

import static id.itsofteam.sac.sac.activities.ActivityCheckout.dbhelper;

/**
 * Created by ferdy on 30/11/2016.
 */

public class ActivityFoodDetails extends AppCompatActivity {

    String str_cat_m_name, str_item_m_id, str_item_m_nama, str_harga_m, str_status_m, str_item_m_image, str_stok_m;
    TextView food_name;
    TextView food_harga;
    TextView food_stok;
    WebView food_desc;
    ImageView img_food, img_fav;
    LinearLayout linearLayout;
    //DatabaseHandler databaseHandler;
    List<ItemFoodList> arrayItemFoodList;
    ItemFoodList itemFoodList;
    CollapsingToolbarLayout collapsingToolbarLayout;
    ProgressBar progressBar;
    CoordinatorLayout coordinatorLayout;
    private AppBarLayout appBarLayout;

    // declare dbhelper object
    static DBHelper dbhelper;

    // declare ImageLoader object
    //ImageLoader imageLoader;

    // declare variables to store menu data
    String Menu_image, Menu_name, Menu_serve, Menu_description;
    double Menu_price;
    int Menu_quantity;
    long Menu_ID;
    String MenuDetailAPI;
    int IOConnect = 0;

    // create price format
    DecimalFormat formatData = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_detail);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
        dbhelper = new DBHelper(this);

        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(itemFoodList.getCat_m_name());
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle("");
                    isShow = false;
                }
            }
        });

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        img_food = (ImageView) findViewById(R.id.image);
        img_fav = (FloatingActionButton) findViewById(R.id.img_fav);

        food_name = (TextView) findViewById(R.id.title);
        food_harga = (TextView) findViewById(R.id.harga);
        food_stok = (TextView) findViewById(R.id.stok);
        food_desc = (WebView) findViewById(R.id.desc);

        //databaseHandler = new DatabaseHandler(ActivityNewsDetail.this);

        arrayItemFoodList = new ArrayList<ItemFoodList>();

        if (JsonUtils.isNetworkAvailable(ActivityFoodDetails.this)) {
            new MyTask().execute(Configuration.API + "?food_detail=" + Configuration.FOOD_DETAIL);
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.failed_connect_network), Toast.LENGTH_SHORT).show();
        }

    }

    private class MyTask extends AsyncTask<String, Void, String> {

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
            coordinatorLayout.setVisibility(View.VISIBLE);

            if (null == result || result.length() == 0) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.failed_connect_network), Toast.LENGTH_SHORT).show();
                coordinatorLayout.setVisibility(View.GONE);
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
                        objItem.setStatus_m(objJson.getString(Configuration.STATUS_M));
                        objItem.setItem_m_image(objJson.getString(Configuration.ITEM_M_IMAGE));
                        objItem.setStok_m(objJson.getInt(Configuration.STOK_M));
                        objItem.setCat_m_name(objJson.getString(Configuration.NAMA_CAT_M));

                        arrayItemFoodList.add(objItem);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                setAdapterToRecyclerView();
            }

        }
    }

    public void setAdapterToRecyclerView() {

            itemFoodList = arrayItemFoodList.get(0);
            str_item_m_id = String.valueOf(itemFoodList.getItem_m_id());
            str_item_m_nama = itemFoodList.getItem_m_name();
            str_harga_m = String.valueOf(itemFoodList.getHarga_m());
            str_status_m = itemFoodList.getStatus_m();
            str_item_m_image = itemFoodList.getItem_m_image();
            str_stok_m = String.valueOf(itemFoodList.getStok_m());
            str_cat_m_name = itemFoodList.getCat_m_name();

            food_name.setText("Nama : " + str_item_m_nama);
        food_harga.setText("Harga : Rp." + str_harga_m);
        food_stok.setText("Stock : " + str_stok_m);

            food_desc.setBackgroundColor(Color.parseColor("#ffffff"));
            food_desc.setFocusableInTouchMode(false);
            food_desc.setFocusable(false);
            food_desc.getSettings().setDefaultTextEncodingName("UTF-8");

            WebSettings webSettings = food_desc.getSettings();
            Resources res = getResources();
            int fontSize = res.getInteger(R.integer.font_size_m);
            webSettings.setDefaultFontSize(fontSize);
            webSettings.setJavaScriptEnabled(true);

            /*String mimeType = "text/html; charset=UTF-8";
            String encoding = "utf-8";
            String htmlText = str_keterangan_m;

            String text = "<html><head>"
                    + "<style type=\"text/css\">body{color: #525252;}"
                    + "</style></head>"
                    + "<body>"
                    + htmlText
                    + "</body></html>";

            food_desc.loadData(text, mimeType, encoding);*/

            Picasso.with(this).load(Configuration.API_IMAGE + itemFoodList.getItem_m_image()).placeholder(R.drawable.no_image).into(img_food, new Callback() {
                @Override
                public void onSuccess() {
                    Bitmap bitmap = ((BitmapDrawable) img_food.getDrawable()).getBitmap();
                    Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(Palette palette) {
                        }
                    });
                }

                @Override
                public void onError() {

                }
            });



            img_fav.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    inputDialog();

                }
            });
        }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                dbhelper.close();
                onBackPressed();
                break;

          case R.id.menu_share:
              // refresh action
              Intent iMyOrder = new Intent(ActivityFoodDetails.this, ActivityCart.class);
              startActivity(iMyOrder);
              break;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }

    // method to show number of order form
    void inputDialog(){

        // open database first
        try{
            dbhelper.openDataBase();
        }catch(SQLException sqle){
            throw sqle;
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(R.string.order);
        alert.setMessage(R.string.number_order);
        alert.setCancelable(false);
        final EditText edtQuantity = new EditText(this);
        int maxLength = 3;
        edtQuantity.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
        edtQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(edtQuantity);

        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String temp = edtQuantity.getText().toString();
                int stok= Integer.parseInt(temp);
                int quantity = 0;

                // when add button clicked add menu to order table in database
                if(!temp.equalsIgnoreCase("") && stok > itemFoodList.getStok_m()){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.peringatan), Toast.LENGTH_SHORT).show();
                } else
                if(!temp.equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.berhasil), Toast.LENGTH_SHORT).show();
                    quantity = Integer.parseInt(temp);
                    Menu_price= itemFoodList.getHarga_m();
                    if(dbhelper.isDataExist(itemFoodList.getItem_m_id())){
                        dbhelper.updateData(itemFoodList.getItem_m_id(), quantity, (Menu_price*quantity));
                    }else{
                        dbhelper.addData(itemFoodList.getItem_m_id(), itemFoodList.getItem_m_name(), quantity, (Menu_price*quantity));
                    }
                }else{
                    dialog.cancel();
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                // when cancel button clicked close dialog
                dialog.cancel();
            }
        });

        alert.show();
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

}
