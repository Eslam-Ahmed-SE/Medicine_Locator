package com.example.medicinelocator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MedicineListAdapter extends RecyclerView.Adapter<MedicineListAdapter.itemViewHolder>  {
    Medicine medicines[];

    public MedicineListAdapter(Medicine[] mMedicicnes) {
        medicines = mMedicicnes;
    }

    @NonNull
    @Override
    public itemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.medicine_list_item, parent, false);
        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        holder.nameTxt.setText(name);
        holder.priceTxt.setText(desc+" EGP");


    }

    @Override
    public int getItemCount() {
        return medicines.length;
    }

    class itemViewHolder extends RecyclerView.ViewHolder {

        public final TextView nameTxt;
        public final TextView priceTxt;

        itemViewHolder(View itemView) {
            super(itemView);
            nameTxt = itemView.findViewById(R.id.itemName);
            priceTxt = itemView.findViewById(R.id.itemPrice);
        }
    }
}
