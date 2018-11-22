package id.itsofteam.sac.sac.json;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * Created by ferdy on 03/12/2016.
 */

public class Configuration implements Serializable {
    private static final long serialVersionUID = 1L;

    // untuk
    public static final String ARRAY="sac";

    // untuk cat makanan
    public static final String ID_CAT_M="cat_m_id";
    public static final String NAMA_CAT_M="cat_m_name";
    public static final String IMAGE_CAT_M="cat_m_image";

    //untuk item makanan
    public static final String ITEM_M_ID="item_m_id";
    public static final String ITEM_M_NAME="item_m_name";
    public static final String CAT_M_ID="cat_m_id";
    public static final String HARGA_M="harga_m";
    public static final String STATUS_M="status_m";
    public static final String ITEM_M_IMAGE="item_m_image";
    public static final String STOK_M="stok_m";

    // untuk cat makanan
    public static final String ID_CAT_F="cat_f_id";
    public static final String NAMA_CAT_F="cat_f_name";
    public static final String IMAGE_CAT_F="cat_f_image";

    //untuk item makanan
    public static final String ITEM_F_ID="item_f_id";
    public static final String ITEM_F_NAME="item_f_name";
    public static final String CAT_F_ID="cat_f_id";
    public static final String HARGA_F="harga_f";
    public static final String STATUS_F="status_f";
    public static final String ITEM_F_IMAGE="item_f_image";
    public static final String KETERANGAN_F="keterangan_f";
    public static final String STOK_F="stok_f";

    //untuk link api
    public static final String API="http://sac.itsofteam.id/api/api.php";
    public static final String API_IMAGE="http://sac.itsofteam.id/";
    public static final String SendDataAPI="http://sac.itsofteam.id/api/add-reservation.php";
    public static final String TaxCurrencyAPI = "http://sac.itsofteam.id/api/get-tax-and-currency.php";

    // change this access similar with accesskey in admin panel for security reason
    public static final String AccessKey = "12345";

    // database path configuration
    static String DBPath = "/data/data/id.itsofteam.sac.sac/databases/";

    // method to check internet connection
    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivity = (ConnectivityManager) activity
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // method to handle images from server
    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
                int count=is.read(bytes, 0, buffer_size);
                if(count==-1)
                    break;
                os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }


    // untuk Save Data ID
    public static int FOOD_ID;
    public static int FOOD_DETAIL;

    // untuk Save Data ID
    public static int FASHION_ID;
    public static int FASHION_DETAIL;

    //untuk notification message
    public static String TITLE_MASSAGE;
    public static String MASSAGE;

}
