package id.itsofteam.sac.sac.models;

/**
 * Created by ferdy on 04/12/2016.
 */

public class ItemFashionDetail {

    private int item_f_id;
    private String item_f_name;
    private int cat_f_id;
    private int harga_f;
    private String status_f;
    private String item_f_image;
    private String keterangan_f;
    private int stok_f;

    public int getItem_f_id() {
        return item_f_id;
    }

    public void setItem_f_id(int item_f_id) {
        this.item_f_id = item_f_id;
    }

    public String getItem_f_name() {
        return item_f_name;
    }

    public void setItem_f_name(String item_f_name) {
        this.item_f_name = item_f_name;
    }

    public int getCat_f_id() {
        return cat_f_id;
    }

    public void setCat_f_id(int cat_f_id) {
        this.cat_f_id = cat_f_id;
    }

    public int getHarga_f() {
        return harga_f;
    }

    public void setHarga_f(int harga_f) {
        this.harga_f = harga_f;
    }

    public String getStatus_f() {
        return status_f;
    }

    public void setStatus_f(String status_f) {
        this.status_f = status_f;
    }

    public String getItem_f_image() {
        return item_f_image;
    }

    public void setItem_f_image(String item_f_image) {
        this.item_f_image = item_f_image;
    }

    public String getKeterangan_f() {
        return keterangan_f;
    }

    public void setKeterangan_f(String keterangan_f) {
        this.keterangan_f = keterangan_f;
    }

    public int getStok_f() {
        return stok_f;
    }

    public void setStok_f(int stok_f) {
        this.stok_f = stok_f;
    }
}
