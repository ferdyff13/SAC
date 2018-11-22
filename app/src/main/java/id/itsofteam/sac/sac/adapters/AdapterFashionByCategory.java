package id.itsofteam.sac.sac.adapters;

import android.content.Context;
import android.content.Intent;
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
import id.itsofteam.sac.sac.activities.ActivityFashionDetails;
import id.itsofteam.sac.sac.json.Configuration;
import id.itsofteam.sac.sac.models.ItemFashionList;

/**
 * Created by ferdy on 04/12/2016.
 */

public class AdapterFashionByCategory extends RecyclerView.Adapter<AdapterFashionByCategory.ViewHolder> {

    private Context context;
    private List<ItemFashionList> arrayItemFashionList;
    ItemFashionList itemFashionList;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView nama;
        public TextView harga;
        public LinearLayout linearLayout;

        public ViewHolder(View view) {
            super(view);

            nama = (TextView) view.findViewById(R.id.item_f_name);
            harga = (TextView) view.findViewById(R.id.harga_f);
            image = (ImageView) view.findViewById(R.id.item_f_image);
            linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);

        }
    }

    public AdapterFashionByCategory(Context mContext, List<ItemFashionList> arrayItemFashionList) {
        this.context = mContext;
        this.arrayItemFashionList = arrayItemFashionList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fashion_list, parent, false);

        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        itemFashionList = arrayItemFashionList.get(position);

        Typeface font1 = Typeface.createFromAsset(context.getAssets(), "fonts/saff-bold.ttf");
        Typeface font2 = Typeface.createFromAsset(context.getAssets(), "fonts/saff-regular.ttf");
        holder.nama.setTypeface(font1);
        holder.harga.setTypeface(font2);

        holder.nama.setText(itemFashionList.getItem_f_name());
        holder.harga.setText("Rp." + String.valueOf(itemFashionList.getHarga_f()));

        Picasso.with(context).load(Configuration.API_IMAGE +
                itemFashionList.getItem_f_image()).placeholder(R.drawable.no_image).into(holder.image);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                itemFashionList = arrayItemFashionList.get(position);
                int pos = itemFashionList.getCat_f_id();

                Intent intent = new Intent(context, ActivityFashionDetails.class);
                intent.putExtra("POSITION", pos);
                Configuration.FASHION_DETAIL = itemFashionList.getItem_f_id();

                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayItemFashionList.size();
    }

}
