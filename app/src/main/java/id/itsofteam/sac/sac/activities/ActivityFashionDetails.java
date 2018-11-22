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

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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
import id.itsofteam.sac.sac.models.ItemFashionList;

/**
 * Created by ferdy on 04/12/2016.
 */

public class ActivityFashionDetails extends AppCompatActivity {

    String str_cat_f_name, str_item_f_id, str_item_f_nama, str_harga_f, str_status_f, str_item_f_image, str_keterangan_f, str_stok_f;
    TextView fashion_name;
    TextView fashion_stok;
    TextView fashion_harga;
    WebView fashion_desc;
    ImageView img_fashion, img_fav;
    LinearLayout linearLayout;
    //DatabaseHandler databaseHandler;
    List<ItemFashionList> arrayItemFashionList;
    ItemFashionList itemFashionList;
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
        setContentView(R.layout.fashion_detail);

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
                    collapsingToolbarLayout.setTitle(itemFashionList.getCat_f_name());
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

        img_fashion = (ImageView) findViewById(R.id.image);
        img_fav = (FloatingActionButton) findViewById(R.id.img_fav);

        fashion_name = (TextView) findViewById(R.id.title);
        fashion_harga = (TextView) findViewById(R.id.harga);
        fashion_stok = (TextView) findViewById(R.id.stok);
        fashion_desc = (WebView) findViewById(R.id.desc);

        //databaseHandler = new DatabaseHandler(ActivityNewsDetail.this);

        arrayItemFashionList = new ArrayList<ItemFashionList>();

        if (JsonUtils.isNetworkAvailable(ActivityFashionDetails.this)) {
            new MyTask().execute(Configuration.API + "?fashion_detail=" + Configuration.FASHION_DETAIL);
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

                        ItemFashionList objItem = new ItemFashionList();

                        objItem.setItem_f_id(objJson.getInt(Configuration.ITEM_F_ID));
                        objItem.setItem_f_name(objJson.getString(Configuration.ITEM_F_NAME));
                        objItem.setHarga_f(objJson.getInt(Configuration.HARGA_F));
                        objItem.setStatus_f(objJson.getString(Configuration.STATUS_F));
                        objItem.setItem_f_image(objJson.getString(Configuration.ITEM_F_IMAGE));
                        objItem.setKeterangan_f(objJson.getString(Configuration.KETERANGAN_F));
                        objItem.setStok_f(objJson.getInt(Configuration.STOK_F));
                        objItem.setCat_f_name(objJson.getString(Configuration.NAMA_CAT_F));

                        arrayItemFashionList.add(objItem);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                setAdapterToRecyclerView();
            }

        }
    }

    public void setAdapterToRecyclerView() {

        itemFashionList = arrayItemFashionList.get(0);
        str_item_f_id = String.valueOf(itemFashionList.getItem_f_id());
        str_item_f_nama = itemFashionList.getItem_f_name();
        str_harga_f = String.valueOf(itemFashionList.getHarga_f());
        str_status_f = itemFashionList.getStatus_f();
        str_item_f_image = itemFashionList.getItem_f_image();
        str_keterangan_f = itemFashionList.getKeterangan_f();
        str_stok_f = String.valueOf(itemFashionList.getStok_f());
        str_cat_f_name = itemFashionList.getCat_f_name();

        fashion_name.setText("Nama :" + str_item_f_nama);
        fashion_harga.setText("Harga : Rp." + str_harga_f);
        fashion_stok.setText("Stock :" + str_stok_f);

        fashion_desc.setBackgroundColor(Color.parseColor("#ffffff"));
        fashion_desc.setFocusableInTouchMode(false);
        fashion_desc.setFocusable(false);
        fashion_desc.getSettings().setDefaultTextEncodingName("UTF-8");

        WebSettings webSettings = fashion_desc.getSettings();
        Resources res = getResources();
        int fontSize = res.getInteger(R.integer.font_size_m);
        webSettings.setDefaultFontSize(fontSize);
        webSettings.setJavaScriptEnabled(true);

            String mimeType = "text/html; charset=UTF-8";
            String encoding = "utf-8";
            String ket = "Keterangan :";
            String htmlText = str_keterangan_f;

            String text = "<html><head>"
                    + "<style type=\"text/css\">body{color: #525252;}"
                    + "</style></head>"
                    + "<body>"
                    + ket
                    + htmlText
                    + "</body></html>";

            fashion_desc.loadData(text, mimeType, encoding);

        Picasso.with(this).load(Configuration.API_IMAGE + itemFashionList.getItem_f_image()).placeholder(R.drawable.no_image).into(img_fashion, new Callback() {
            @Override
            public void onSuccess() {
                Bitmap bitmap = ((BitmapDrawable) img_fashion.getDrawable()).getBitmap();
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
            getMenuInflater().inflate(R.menu.menu_news, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.menu_share:
                // refresh action
                Intent iMyOrder = new Intent(ActivityFashionDetails.this, ActivityCart.class);
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
                if(!temp.equalsIgnoreCase("") && stok > itemFashionList.getStok_f()){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.peringatan), Toast.LENGTH_SHORT).show();
                }
                else if(!temp.equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.berhasil), Toast.LENGTH_SHORT).show();
                    quantity = Integer.parseInt(temp);
                    Menu_price= itemFashionList.getHarga_f();
                    if(dbhelper.isDataExist(itemFashionList.getItem_f_id())){
                        dbhelper.updateData(itemFashionList.getItem_f_id(), quantity, (Menu_price*quantity));
                    }else{
                        dbhelper.addData(itemFashionList.getItem_f_id(), itemFashionList.getItem_f_name(), quantity, (Menu_price*quantity));
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
