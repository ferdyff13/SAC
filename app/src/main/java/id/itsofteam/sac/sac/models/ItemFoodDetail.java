package id.itsofteam.sac.sac.models;

/**
 * Created by ferdy on 30/11/2016.
 */

public class ItemFoodDetail {

    private int item_m_id;
    private String item_m_name;
    private int cat_m_id;
    private int harga_m;
    private String status_m;
    private String item_m_image;
    private int stok_m;

    public int getItem_m_id() {
        return item_m_id;
    }

    public void setItem_m_id(int item_m_id) {
        this.item_m_id = item_m_id;
    }

    public String getItem_m_name() {
        return item_m_name;
    }

    public void setItem_m_name(String item_m_name) {
        this.item_m_name = item_m_name;
    }

    public int getCat_m_id() {
        return cat_m_id;
    }

    public void setCat_m_id(int cat_m_id) {
        this.cat_m_id = cat_m_id;
    }

    public int getHarga_m() {
        return harga_m;
    }

    public void setHarga_m(int harga_m) {
        this.harga_m = harga_m;
    }

    public String getStatus_m() {
        return status_m;
    }

    public void setStatus_m(String status_m) {
        this.status_m = status_m;
    }

    public String getItem_m_image() {
        return item_m_image;
    }

    public void setItem_m_image(String item_m_image) {
        this.item_m_image = item_m_image;
    }

    public int getStok_m() {
        return stok_m;
    }

    public void setStok_m(int stok_m) {
        this.stok_m = stok_m;
    }
}
