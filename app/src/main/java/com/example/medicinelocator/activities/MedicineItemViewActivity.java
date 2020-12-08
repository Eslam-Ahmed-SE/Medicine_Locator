package com.example.medicinelocator.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicinelocator.FillPharmacyListTask;
import com.example.medicinelocator.PharmacyListAdapter;
import com.example.medicinelocator.R;
import com.example.medicinelocator.dataModels.Pharmacy;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

public class MedicineItemViewActivity extends AppCompatActivity {

    static final String EXTRA_ITEM_NAME = "itemNameAnim";
    static final String EXTRA_ITEM_PRICE = "itemPriceAnim";

    private TextView name;
    private TextView price;
    private TextView itemNotAvailable;
    private RecyclerView pharmList;
    private ShimmerFrameLayout pharmacies_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_item_view);

        initComponents();

        Bundle bundle = getIntent().getExtras();
        name.setText(bundle.getString("name"));
        price.setText(getString(R.string.price, bundle.getString("price")));
        String availability = bundle.getString("availability");
        Log.i("inf", "availability " + availability);
        String city = bundle.getString("city");
        String gov = bundle.getString("gov");

//        Toast.makeText(this, "city: " + city + "gov: " + gov, Toast.LENGTH_LONG).show();

        addAnimation();

        List<Pharmacy> pharmsNearby = new ArrayList<Pharmacy> ();
        FillPharmacyListTask pharmacyListTask = new FillPharmacyListTask( availability, this) {
            @Override
            public void onResponseReceived(List<Pharmacy> mPharms) {
                Log.i("inf", "post executed");
                if ( mPharms !=null ){
                    for (Pharmacy pharm : mPharms){
                        if (pharm.getCity().equals(city)  ) {
                            pharmsNearby.add(pharm);
                        }
                    }
                    if (pharmsNearby.size() == 0){
                        itemNotAvailable.setVisibility(View.VISIBLE);
                    }
                    PharmacyListAdapter mAdapter = new PharmacyListAdapter(pharmsNearby, MedicineItemViewActivity.this);
                    pharmList.setAdapter(mAdapter);
                    showPharmList();


                }
            }
        };
//
        pharmacyListTask.execute();

    }

    @Override
    public void onBackPressed() {
        name.startAnimation(AnimationUtils.loadAnimation(this, R.anim.back_from_itemview));
        price.startAnimation(AnimationUtils.loadAnimation(this, R.anim.back_from_itemview));
        finishAfterTransition();
//        Toast.makeText(this, "back", Toast.LENGTH_SHORT).show();
//        super.onBackPressed();
    }

    private void addAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.shared_element_transition);
        getWindow().setSharedElementEnterTransition(transition);
        getWindow().setSharedElementExitTransition(transition);
    }

    private void initComponents() {
        name = findViewById(R.id.itemNameView);
        price = findViewById(R.id.itemPriceView);
        itemNotAvailable = findViewById(R.id.itemNotAvailable);

        pharmList = findViewById(R.id.pharmList);
        pharmacies_loading = findViewById(R.id.pharmacies_loading);

        pharmList.setLayoutManager(new LinearLayoutManager(this));
        pharmList.setItemAnimator(new DefaultItemAnimator());
    }

    public void showPharmList(){
        pharmList.setVisibility(View.VISIBLE);
        pharmacies_loading.setVisibility(View.GONE);
    }
}