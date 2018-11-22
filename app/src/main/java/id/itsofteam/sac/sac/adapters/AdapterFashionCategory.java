package id.itsofteam.sac.sac.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import id.itsofteam.sac.sac.R;
import id.itsofteam.sac.sac.activities.ActivityFashionListByCategory;
import id.itsofteam.sac.sac.json.Configuration;
import id.itsofteam.sac.sac.models.ItemFashionCategory;

/**
 * Created by ferdy on 04/12/2016.
 */

public class AdapterFashionCategory extends RecyclerView.Adapter<AdapterFashionCategory.ViewHolder> {

    private Context context;
    private List<ItemFashionCategory> arrayItemKategorisList;
    ItemFashionCategory itemKategoriList;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image_kategori;
        public TextView kategori;
        public LinearLayout linearLayout;
        public View v;

        public ViewHolder(View view) {
            super(view);

            kategori = (TextView) view.findViewById(R.id.kategori_fashion);
            image_kategori = (ImageView) view.findViewById(R.id.kategori_fashion_image);
            linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
            v = (View)view.findViewById(R.id.view);

        }

    }

    public AdapterFashionCategory(Context context, List<ItemFashionCategory> arrayItemKategorisList) {
        this.context = context;
        this.arrayItemKategorisList = arrayItemKategorisList;
    }

    @Override
    public AdapterFashionCategory.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fashion_category, parent, false);

        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final AdapterFashionCategory.ViewHolder holder, final int position) {

        itemKategoriList = arrayItemKategorisList.get(position);

        String fontPath1 = "fonts/saff-bold.ttf";

        holder.kategori.setText(itemKategoriList.getCat_f_name());
        holder.kategori.setTextColor(Color.parseColor("#FDD835"));
        holder.kategori.setTypeface(Typeface.createFromAsset(context.getAssets(), fontPath1));



        holder.v.setBackgroundColor(Color.parseColor("#f1c40f"));

        Picasso.with(context).load(Configuration.API_IMAGE +
                itemKategoriList.getCat_f_image()).placeholder(R.drawable.no_image).into(holder.image_kategori);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemKategoriList = arrayItemKategorisList.get(position);

                int pos = itemKategoriList.getCat_f_id();

                Intent intent = new Intent(context, ActivityFashionListByCategory.class);
                intent.putExtra("POSITION", pos);
                Configuration.FASHION_DETAIL = itemKategoriList.getCat_f_id();

                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayItemKategorisList.size();
    }

}
