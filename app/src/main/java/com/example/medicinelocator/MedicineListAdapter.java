package com.example.medicinelocator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicinelocator.dataModels.Medicine;

import java.util.List;
import java.util.Locale;



public abstract class MedicineListAdapter extends RecyclerView.Adapter<MedicineListAdapter.itemViewHolder> {
    List<Medicine> medicines;
    Context context;
    int lastPosition;
    boolean edit;
    int pharmID;

    public MedicineListAdapter(List<Medicine> mMedicicnes, Context mContext, boolean mEdit, int mPharmID) {
        medicines = mMedicicnes;
        context = mContext;
        lastPosition = -1;
        edit = mEdit;
        pharmID = mPharmID;
    }

    @NonNull
    @Override
    public itemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem;
        if (edit){
            listItem = layoutInflater.inflate(R.layout.medicine_list_item_edit, parent, false);
        }
        else {
            listItem = layoutInflater.inflate(R.layout.medicine_list_item, parent, false);
        }
        itemViewHolder viewHolder = new itemViewHolder(listItem, edit);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull itemViewHolder holder, int position) {
        String id = medicines.get(position).getId();
        String name_en = medicines.get(position).getName_en();
        String name_ar = medicines.get(position).getName_ar();
        String price = medicines.get(position).getPrice();
        String avail = medicines.get(position).getAvailability();
        holder.drugID = id;
        holder.nameTxt.setText(Locale.getDefault().getLanguage().equals("ar") ? name_ar:name_en);
        holder.priceTxt.setText(context.getString(R.string.price, price));
        holder.availabilityTxt.setText(avail);
        if (edit){
            holder.pharmAvailability.setChecked(avail.contains(String.valueOf(pharmID)));
        }


//        Animation animation = AnimationUtils.loadAnimation(context,
//                (position > lastPosition) ? R.anim.up_from_bottom
//                        : R.anim.down_from_top);
//        holder.itemView.startAnimation(animation);
//        lastPosition = position;


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick(holder, view);


            }
        });


    }

    @Override
    public void onViewDetachedFromWindow(@NonNull itemViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public int getItemCount() {
        return medicines.size();
    }

    public abstract void onItemClick(itemViewHolder item, View clickedView);

    protected class itemViewHolder extends RecyclerView.ViewHolder {

        public String drugID;
        public final TextView nameTxt;
        public final TextView priceTxt;
        public final TextView availabilityTxt;
        public final Switch pharmAvailability;

        itemViewHolder(View itemView, boolean edit) {
            super(itemView);
            if (edit){
                nameTxt = itemView.findViewById(R.id.itemNameEdit);
                priceTxt = itemView.findViewById(R.id.itemPriceEdit);
                availabilityTxt = itemView.findViewById(R.id.itemAvailibilityEdit);
                pharmAvailability = itemView.findViewById(R.id.itemPharmAvailibilityEdit);
            }
            else {
                nameTxt = itemView.findViewById(R.id.itemName);
                priceTxt = itemView.findViewById(R.id.itemPrice);
                availabilityTxt = itemView.findViewById(R.id.itemAvailibility);
                pharmAvailability = null;
            }
        }
    }



}
