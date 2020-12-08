package com.example.medicinelocator;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicinelocator.dataModels.Pharmacy;

import java.util.List;

public class PharmacyListAdapter extends RecyclerView.Adapter<PharmacyListAdapter.itemViewHolder>  {
    List<Pharmacy> pharms;
    Context context;
    int lastPosition;

    public PharmacyListAdapter(List<Pharmacy> mPharms, Context mContext) {
        pharms = mPharms;
        context = mContext;
        lastPosition = -1;
    }

    @NonNull
    @Override
    public PharmacyListAdapter.itemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.pharmacy_list_item, parent, false);
        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent i = new Intent(parent.getContext(), medicineItemView.class);
//                i.putExtra("name", ((TextView)listItem.findViewById(R.id.itemName)).getText().toString());
//                i.putExtra("price", ((TextView)listItem.findViewById(R.id.itemPrice)).getText().toString());
//
//                Pair<View, String> p1 = Pair.create((View)listItem.findViewById(R.id.itemName), listItem.findViewById(R.id.itemName).getTransitionName());
//                Pair<View, String> p2 = Pair.create((View)listItem.findViewById(R.id.itemPrice), listItem.findViewById(R.id.itemPrice).getTransitionName());
//                ActivityOptionsCompat options = ActivityOptionsCompat.
//                        makeSceneTransitionAnimation((Activity) parent.getContext(), p1, p2);
//                parent.getContext().startActivity(i, options.toBundle());
//
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + ((TextView)listItem.findViewById(R.id.pharmPhone)).getText().toString()));
                context.startActivity(callIntent);
            }
        });
        return new itemViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull PharmacyListAdapter.itemViewHolder holder, int position) {

        String name = pharms.get(position).getName();
        String phone = pharms.get(position).getPhone();
        String address = pharms.get(position).getAddress();
        String landmark = pharms.get(position).getLandmark();
        String mail = pharms.get(position).getMail();


        holder.nameTxt.setText(name);
        holder.phoneTxt.setText(phone);
        holder.addressTxt.setText(address);
        holder.landmarkTxt.setText(landmark);
        holder.mailTxt.setText(mail);


        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.up_from_bottom
                        : R.anim.down_from_top);
        holder.itemView.startAnimation(animation);
        lastPosition = position;

    }

    @Override
    public void onViewDetachedFromWindow(@NonNull PharmacyListAdapter.itemViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public int getItemCount() {
        return pharms.size();
    }

    class itemViewHolder extends RecyclerView.ViewHolder {

        public final TextView nameTxt;
        public final TextView phoneTxt;
        public final TextView addressTxt;
        public final TextView landmarkTxt;
        public final TextView mailTxt;

        itemViewHolder(View itemView) {
            super(itemView);
            nameTxt = itemView.findViewById(R.id.pharmName);
            phoneTxt = itemView.findViewById(R.id.pharmPhone);
            addressTxt = itemView.findViewById(R.id.pharmAddress);
            landmarkTxt = itemView.findViewById(R.id.pharmLandmark);
            mailTxt = itemView.findViewById(R.id.pharmMail);

        }
    }
}
