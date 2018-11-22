package id.itsofteam.sac.sac.activities;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;




import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import id.itsofteam.sac.sac.R;

import id.itsofteam.sac.sac.json.DBHelper;




/**
 * Created by ferdy on 03/12/2016.
 */

public class ActivityCheckout extends AppCompatActivity {

    Button btnSend;
    EditText edtName, edtPhone, edtOrderList, edtComment, edtAlamat, edtEmail, edtKota;
    ScrollView sclDetail;
    ProgressBar prgLoading;
    TextView txtAlert;
    Spinner spinner;

    // declare dbhelper object
    static DBHelper dbhelper;
    ArrayList<ArrayList<Object>> data;

    // declare string variables to store data
    String Name, Phone, Alamat, Email, Kota;
    String OrderList = "";
    String Comment = "";

    // declare static int variables to store date and time
    private static int mYear;
    private static int mMonth;
    private static int mDay;
    private static int mHour;
    private static int mMinute;

    // declare static variables to store tax and currency data
    static double Tax;
    static String Currency;

    static final String TIME_DIALOG_ID = "timePicker";
    static final String DATE_DIALOG_ID = "datePicker";

    // create price format
    DecimalFormat formatData = new DecimalFormat("#.##");

    String Result;
    String TaxCurrencyAPI;
    int IOConnect = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        centerToolbarTitle(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.left_arrow);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Checkout");
        }

        edtName = (EditText) findViewById(R.id.edtName);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPhone = (EditText) findViewById(R.id.edtPhone);
        edtOrderList = (EditText) findViewById(R.id.edtOrderList);
        edtComment = (EditText) findViewById(R.id.edtComment);
        btnSend = (Button) findViewById(R.id.btnSend);
        sclDetail = (ScrollView) findViewById(R.id.sclDetail);
        prgLoading = (ProgressBar) findViewById(R.id.prgLoading);
        txtAlert = (TextView) findViewById(R.id.txtAlert);
        edtAlamat = (EditText) findViewById(R.id.edtAlamat);
        edtKota = (EditText) findViewById(R.id.edtKota);
        // tax and currency API url
        //TaxCurrencyAPI = Configuration.TaxCurrencyAPI+"?accesskey="+Configuration.AccessKey;

        dbhelper = new DBHelper(this);
        // open database
        try{
            dbhelper.openDataBase();
        }catch(SQLException sqle){
            throw sqle;
        }

        // call asynctask class to request tax and currency data from server
        new getTaxCurrency().execute();

        // event listener to handle send button when pressed
        btnSend.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                // get data from all forms and send to server
                Name = edtName.getText().toString();
                Alamat = edtAlamat.getText().toString();
                Kota = edtKota.getText().toString();
                Email = edtEmail.getText().toString();
                Phone = edtPhone.getText().toString();
                Comment = edtComment.getText().toString();
                if(Name.equalsIgnoreCase("") || Email.equalsIgnoreCase("") || Alamat.equalsIgnoreCase("") || Kota.equalsIgnoreCase("") ||
                        Phone.equalsIgnoreCase("")){
                    Toast.makeText(ActivityCheckout.this, R.string.form_alert, Toast.LENGTH_SHORT).show();
                }else if((data.size() == 0)){
                    Toast.makeText(ActivityCheckout.this, R.string.order_alert, Toast.LENGTH_SHORT).show();
                }else{
                    new sendData().execute();
                }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case android.R.id.home:
                // app icon in action bar clicked; go home
                this.finish();
                //overridePendingTransition(R.anim.open_main, R.anim.close_next);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // asynctask class to handle parsing json in background
    public class getTaxCurrency extends AsyncTask<Void, Void, Void> {

        // show progressbar first
        getTaxCurrency(){
            if(!prgLoading.isShown()){
                prgLoading.setVisibility(0);
                txtAlert.setVisibility(8);
            }
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            // parse json data from server in background
            // parseJSONDataTax();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            // when finish parsing, hide progressbar
            prgLoading.setVisibility(8);
            // if internet connection and data available request menu data from server
            // otherwise, show alert text
            if(IOConnect == 0){
                new getDataTask().execute();
            }else{
                txtAlert.setVisibility(0);
            }
        }
    }

    // method to parse json data from server
    /*public void parseJSONDataTax(){

        try {
            // request data from tax and currency API
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 15000);
            HttpConnectionParams.setSoTimeout(client.getParams(), 15000);
            HttpUriRequest request = new HttpGet(TaxCurrencyAPI);
            HttpResponse response = client.execute(request);
            InputStream atomInputStream = response.getEntity().getContent();


            BufferedReader in = new BufferedReader(new InputStreamReader(atomInputStream));

            String line;
            String str = "";
            while ((line = in.readLine()) != null){
                str += line;
            }

            // parse json data and store into tax and currency variables
            JSONObject json = new JSONObject(str);
            JSONArray data = json.getJSONArray("data"); // this is the "items: [ ] part


            JSONObject object_tax = data.getJSONObject(0);
            JSONObject tax = object_tax.getJSONObject("tax_n_currency");

            Tax = Double.parseDouble(tax.getString("Value"));

            JSONObject object_currency = data.getJSONObject(1);
            JSONObject currency = object_currency.getJSONObject("tax_n_currency");

            Currency = currency.getString("Value");

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            IOConnect = 1;
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }*/

    // asynctask class to get data from database in background
    public class getDataTask extends AsyncTask<Void, Void, Void>{


        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            getDataFromDatabase();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            // hide progressbar and show reservation form
            prgLoading.setVisibility(8);
            sclDetail.setVisibility(0);

        }
    }

    // asynctask class to send data to server in background
    public class sendData extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog;

        // show progress dialog
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            dialog= ProgressDialog.show(ActivityCheckout.this, "",
                    getString(R.string.sending_alert), true);

        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            // send data to server and store result to variable
            Result = getRequest(Name, Alamat, Kota, Email, Phone, OrderList, Comment);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            // if finish, dismis progress dialog and show toast message
            dialog.dismiss();
            resultAlert(Result);


        }
    }

    // method to show toast message
    public void resultAlert(String HasilProses){
        if(HasilProses.trim().equalsIgnoreCase("OK")){
            Toast.makeText(ActivityCheckout.this, R.string.ok_alert, Toast.LENGTH_SHORT).show();
            Intent i = new Intent(ActivityCheckout.this, ActivityConfirmMessage.class);
            startActivity(i);
            //overridePendingTransition (R.anim.open_next, R.anim.close_next);
            finish();
        }else if(HasilProses.trim().equalsIgnoreCase("Failed")){
            Toast.makeText(ActivityCheckout.this, R.string.failed_alert, Toast.LENGTH_SHORT).show();
        }else{
            Log.d("HasilProses", HasilProses);
        }
    }

    // method to post data to server
    public String getRequest(String name, String alamat, String kota, String email, String phone, String orderlist, String comment){
        String result = "";

        HttpClient client = new DefaultHttpClient();
        HttpPost request = new HttpPost(id.itsofteam.sac.sac.json.Configuration.SendDataAPI);

        try{
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
            nameValuePairs.add(new BasicNameValuePair("name", name));
            nameValuePairs.add(new BasicNameValuePair("alamat", alamat));
            nameValuePairs.add(new BasicNameValuePair("kota", kota));
            nameValuePairs.add(new BasicNameValuePair("email", email));
            nameValuePairs.add(new BasicNameValuePair("phone", phone));
            nameValuePairs.add(new BasicNameValuePair("order_list", orderlist));
            nameValuePairs.add(new BasicNameValuePair("comment", comment));
            request.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));
            HttpResponse response = client.execute(request);
            result = request(response);
        }catch(Exception ex){
            result = "Unable to connect.";
        }
        return result;
    }

    public static String request(HttpResponse response){
        String result = "";
        try{
            InputStream in = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder str = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) != null){
                str.append(line + "\n");
            }
            in.close();
            result = str.toString();
        }catch(Exception ex){
            result = "Error";
        }
        return result;
    }

    // method to get data from database
    public void getDataFromDatabase(){

        data = dbhelper.getAllData();

        double Order_price = 0;
        double Total_price = 0;
        double tax = 0;

        // store all data to variables
        for(int i=0;i<data.size();i++){
            ArrayList<Object> row = data.get(i);

            String Menu_name = row.get(1).toString();
            String Quantity = row.get(2).toString();
            double Sub_total_price = Double.parseDouble(formatData.format(Double.parseDouble(row.get(3).toString())));
            Order_price += Sub_total_price;

            // calculate order price
            OrderList += (Quantity+" "+Menu_name+" "+Sub_total_price+",\n");
        }

        if(OrderList.equalsIgnoreCase("")){
            OrderList += getString(R.string.no_order_menu);
        }

        //tax = Double.parseDouble(formatData.format(Order_price *(Tax /100)));
        Total_price = Double.parseDouble(formatData.format(Order_price - tax));
        OrderList += "\nOrder: "+Order_price+
                //"\nTax: "+Tax+"%: "+tax+" "+Currency+
                "\nTotal: "+Total_price;
        edtOrderList.setText(OrderList);
    }

    // method to format date
    private static String pad(int c) {
        if (c >= 10){
            return String.valueOf(c);
        }else{
            return "0" + String.valueOf(c);
        }
    }

    // when back button pressed close database and back to previous page
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        dbhelper.close();
        finish();
        //overridePendingTransition(R.anim.open_main, R.anim.close_next);
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig)
    {
        // Ignore orientation change to keep activity from restarting
        super.onConfigurationChanged(newConfig);
    }
}
