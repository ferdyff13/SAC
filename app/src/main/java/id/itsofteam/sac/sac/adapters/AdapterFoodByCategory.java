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
import id.itsofteam.sac.sac.activities.ActivityFoodDetails;
import id.itsofteam.sac.sac.json.Configuration;
import id.itsofteam.sac.sac.models.ItemFoodList;

/**
 * Created by ferdy on 30/11/2016.
 */

public class AdapterFoodByCategory extends RecyclerView.Adapter<AdapterFoodByCategory.ViewHolder> {

    private Context context;
    private List<ItemFoodList> arrayItemFoodList;
    ItemFoodList itemFoodList;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView nama;
        public TextView harga;
        public LinearLayout linearLayout;

        public ViewHolder(View view) {
            super(view);

            nama = (TextView) view.findViewById(R.id.item_m_name);
            harga = (TextView) view.findViewById(R.id.harga_m);
            image = (ImageView) view.findViewById(R.id.item_m_image);
            linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);

        }
    }

    public AdapterFoodByCategory(Context mContext, List<ItemFoodList> arrayItemFoodList) {
        this.context = mContext;
        this.arrayItemFoodList = arrayItemFoodList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_list, parent, false);

        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        itemFoodList = arrayItemFoodList.get(position);

        Typeface font1 = Typeface.createFromAsset(context.getAssets(), "fonts/saff-bold.ttf");
        Typeface font2 = Typeface.createFromAsset(context.getAssets(), "fonts/saff-regular.ttf");
        holder.nama.setTypeface(font1);
        holder.harga.setTypeface(font2);

        holder.nama.setText(itemFoodList.getItem_m_name());
        holder.harga.setText("Rp." + String.valueOf(itemFoodList.getHarga_m()));

        Picasso.with(context).load(Configuration.API_IMAGE +
                itemFoodList.getItem_m_image()).placeholder(R.drawable.no_image).into(holder.image);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                itemFoodList = arrayItemFoodList.get(position);
                int pos = itemFoodList.getCat_m_id();

                Intent intent = new Intent(context, ActivityFoodDetails.class);
                intent.putExtra("POSITION", pos);
                Configuration.FOOD_DETAIL = itemFoodList.getItem_m_id();

                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayItemFoodList.size();
    }

}
