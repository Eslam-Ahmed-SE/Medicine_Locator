package com.example.medicinelocator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicinelocator.activities.MedicineItemViewActivity;
import com.example.medicinelocator.dataModels.Medicine;

public class MedicineListAdapter extends RecyclerView.Adapter<MedicineListAdapter.itemViewHolder>  {
    Medicine medicines[];
    Context context;
    int lastPosition;

    public MedicineListAdapter(Medicine[] mMedicicnes, Context mContext) {
        medicines = mMedicicnes;
        context = mContext;
        lastPosition = -1;
    }

    @NonNull
    @Override
    public itemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.medicine_list_item, parent, false);
        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.medicine_item_clicked));

                Intent i = new Intent(parent.getContext(), MedicineItemViewActivity.class);
                i.putExtra("name", ((TextView)listItem.findViewById(R.id.itemName)).getText().toString());
                i.putExtra("price", ((TextView)listItem.findViewById(R.id.itemPrice)).getText().toString());
                i.putExtra("availability", ((TextView)listItem.findViewById(R.id.itemAvailibility)).getText().toString());

                Pair<View, String> p1 = Pair.create((View)listItem.findViewById(R.id.itemName), listItem.findViewById(R.id.itemName).getTransitionName());
                Pair<View, String> p2 = Pair.create((View)listItem.findViewById(R.id.itemPrice), listItem.findViewById(R.id.itemPrice).getTransitionName());
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) parent.getContext(), p1, p2);
                parent.getContext().startActivity(i, options.toBundle());

                Toast.makeText(parent.getContext(), "clicked", Toast.LENGTH_SHORT).show();
            }
        });
        itemViewHolder viewHolder = new itemViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull itemViewHolder holder, int position) {
        String name = medicines[position].getName();
        String desc = medicines[position].getPrice();
        String avail = medicines[position].getAvailability();
        holder.nameTxt.setText(name);
        holder.priceTxt.setText(desc+" EGP");
        holder.availibilityTxt.setText(avail);


        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.up_from_bottom
                        : R.anim.down_from_top);
        holder.itemView.startAnimation(animation);
        lastPosition = position;

    }

    @Override
    public void onViewDetachedFromWindow(@NonNull itemViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public int getItemCount() {
        return medicines.length;
    }

    class itemViewHolder extends RecyclerView.ViewHolder {

        public final TextView nameTxt;
        public final TextView priceTxt;
        public final TextView availibilityTxt;

        itemViewHolder(View itemView) {
            super(itemView);
            nameTxt = itemView.findViewById(R.id.itemName);
            priceTxt = itemView.findViewById(R.id.itemPrice);
            availibilityTxt = itemView.findViewById(R.id.itemAvailibility);
        }
    }


}
