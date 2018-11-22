package id.itsofteam.sac.sac.models;

/**
 * Created by ferdy on 30/11/2016.
 */

public class ItemFoodList {

    private int cat_m_id;
    private int item_m_id;
    private String cat_m_name;
    private String cat_m_image;
    private String item_m_name;
    private String item_m_image;
    private String status_m;
    private int harga_m;
    private int stok_m;


    public int getCat_m_id() {
        return cat_m_id;
    }

    public void setCat_m_id(int cat_m_id) {
        this.cat_m_id = cat_m_id;
    }

    public String getCat_m_name() {
        return cat_m_name;
    }

    public void setCat_m_name(String cat_m_name) {
        this.cat_m_name = cat_m_name;
    }

    public String getCat_m_image() {
        return cat_m_image;
    }

    public void setCat_m_image(String cat_m_image) {
        this.cat_m_image = cat_m_image;
    }

    public String getItem_m_name() {
        return item_m_name;
    }

    public void setItem_m_name(String item_m_name) {
        this.item_m_name = item_m_name;
    }

    public String getItem_m_image() {
        return item_m_image;
    }

    public void setItem_m_image(String item_m_image) {
        this.item_m_image = item_m_image;
    }

    public int getHarga_m() {
        return harga_m;
    }

    public void setHarga_m(int harga_m) {
        this.harga_m = harga_m;
    }

    public int getStok_m() {
        return stok_m;
    }

    public void setStok_m(int stok_m) {
        this.stok_m = stok_m;
    }

    public int getItem_m_id() {
        return item_m_id;
    }

    public void setItem_m_id(int item_m_id) {
        this.item_m_id = item_m_id;
    }

    public String getStatus_m() {
        return status_m;
    }

    public void setStatus_m(String status_m) {
        this.status_m = status_m;
    }
}
