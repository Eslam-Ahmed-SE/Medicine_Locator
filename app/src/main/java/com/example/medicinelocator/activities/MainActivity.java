package com.example.medicinelocator.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.medicinelocator.FillCityListTask;
import com.example.medicinelocator.FillGovListTask;
import com.example.medicinelocator.FillMedicineListTask;
import com.example.medicinelocator.MedicineListAdapter;
import com.example.medicinelocator.R;
import com.example.medicinelocator.dataModels.*;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private RecyclerView medicineList;
    private ConstraintLayout item_container;
    private ConstraintLayout header_layout;
    private ShimmerFrameLayout medicines_loading;
    private NestedScrollView scrollContainer;
    private EditText hidden_search_bar;
    private EditText search_bar;
    private FloatingActionButton back_to_top;
    private ImageButton navigation_menu_icon;

    private Spinner govSpinner;
    private Spinner citySpinner;

    private List<Medicine> medicines;
    private Cities[] cities;

    private List<Medicine> filteredMedicine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();

        startFillMedicineList();

        FillGovListTask govTask = new FillGovListTask() {
            @Override
            public void onResponseReceived(Govs[] mGovs) {
                if (mGovs!= null) {
                    String[] govNames = new String[mGovs.length];
                    for (int i = 0; i<mGovs.length; i++) {
                        govNames[i] = Locale.getDefault().getLanguage().equals("ar") ? mGovs[i].getName_ar():mGovs[i].getName_en();
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, govNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    govSpinner.setAdapter(adapter);


                }
            }
        };
        govTask.execute();

        FillCityListTask cityTask = new FillCityListTask() {
            @Override
            public void onResponseReceived(Cities[] mCities) {
                if (mCities!= null) {
                    cities = mCities;
                    String[] cityNames = new String[mCities.length];
                    for (int i = 0; i<mCities.length; i++) {
                        cityNames[i] = Locale.getDefault().getLanguage().equals("ar") ? mCities[i].getName_ar():mCities[i].getName_en();
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, cityNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(adapter);

                    citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    govSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            filterCities();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    filterCities();
                }
            }
        };
        cityTask.execute();




        final Rect scrollBounds = new Rect();
        scrollContainer.getHitRect(scrollBounds);
        initOnScrollListener();



    }

    @Override
    protected void onResume() {
        super.onResume();
        startFillMedicineList();
    }

    private void startFillMedicineList() {
        FillMedicineListTask medicineListTask =  new FillMedicineListTask()
        {
            @Override
            public void onResponseReceived(List<Medicine> mMedicine) {
                medicines = mMedicine;
                Log.i("inf", "task response");
                if (medicines != null) {
                    Log.i("inf", "not null");
                    setMedicineAdapter(medicines);

                    showItemContainer();
                } else {
                    Log.i("inf", "null");

                }
            }
        };
        medicineListTask.execute();
    }

    private void setMedicineAdapter(List<Medicine> medicines) {
        MedicineListAdapter mAdapter = new MedicineListAdapter(medicines, MainActivity.this, false, 0) {
            @Override
            public void onItemClick(MedicineListAdapter.itemViewHolder item, View clickedView) {
                clickedView.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.medicine_item_clicked));

                Intent i = new Intent(item.itemView.getContext(), MedicineItemViewActivity.class);
                i.putExtra("name", item.nameTxt.getText().toString());
                i.putExtra("price", item.priceTxt.getText().toString());
                i.putExtra("availability", item.availabilityTxt.getText().toString());

                i.putExtra("gov", String.valueOf(govSpinner.getSelectedItemId()+1));
                for (Cities city : cities) {
                    if (citySpinner.getSelectedItem().toString().equals(Locale.getDefault().getLanguage().equals("ar") ? city.getName_ar():city.getName_en())){
                        i.putExtra("city", String.valueOf(city.getId()));
                    }
                }


                Pair<View, String> p1 = Pair.create(item.nameTxt, item.nameTxt.getTransitionName());
                Pair<View, String> p2 = Pair.create(item.priceTxt, item.priceTxt.getTransitionName());
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) item.itemView.getContext(), p1, p2);
                item.itemView.getContext().startActivity(i, options.toBundle());

            }
        };
        medicineList.setAdapter(mAdapter);
    }

    private void filterCities() {

        List<String> availCitiesNames = new ArrayList<String>();
        for (int i=0; i<cities.length; i++){
            if (cities[i].getGov_id() == govSpinner.getSelectedItemPosition()+1){
                availCitiesNames.add(Locale.getDefault().getLanguage().equals("ar") ? cities[i].getName_ar():cities[i].getName_en());
            }
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, availCitiesNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(adapter);

    }

    private void initOnScrollListener() {
        scrollContainer.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                Log.i("scroll", "int scrollX: " + scrollX + " int scrollY: " + scrollY + " int oldScrollX: " + oldScrollX + " int oldScrollY: " + oldScrollY);
                Rect scrollBounds = new Rect();
                scrollContainer.getHitRect(scrollBounds);

                if (search_bar.getLocalVisibleRect(scrollBounds)) {
                    Log.i("scroll", "true");
                    if (hidden_search_bar.isFocused()){
                        search_bar.requestFocus();
                        search_bar.setText(hidden_search_bar.getText());
                        search_bar.setSelection(search_bar.getText().length());
                    }
                    hidden_search_bar.setVisibility(View.INVISIBLE);
                    back_to_top.setVisibility(View.GONE);
                    header_layout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    hidden_search_bar.setBackground(getDrawable(R.drawable.custom_search));
                    header_layout.setElevation(0f);


                } else {
                    if (search_bar.isFocused()){
                        hidden_search_bar.requestFocus();
                        hidden_search_bar.setText(search_bar.getText());
                        hidden_search_bar.setSelection(hidden_search_bar.getText().length());
                    }
                    Log.i("scroll", "false");
                    hidden_search_bar.setVisibility(View.VISIBLE);
                    back_to_top.setVisibility(View.VISIBLE);
                    header_layout.setBackgroundColor(getResources().getColor(R.color.white));
                    hidden_search_bar.setBackground(getDrawable(R.drawable.custom_search));
                    header_layout.setElevation(8f);

                }


            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation, menu);
//        menu.setHeaderTitle("Select The Action");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        if(item.getItemId()==R.id.navigation_menu_pharmSignIn){
            Intent i = new Intent(this, PharmacySignInActivity.class);
            startActivity(i);
        }else{
            return false;
        }
        return true;
    }

    private void initComponents() {
        medicineList = findViewById(R.id.medList);
        item_container = findViewById(R.id.recent_container);
        header_layout = findViewById(R.id.headerLayout);
        medicines_loading = findViewById(R.id.medicines_loading);
        scrollContainer = findViewById(R.id.scrollContainer);
        hidden_search_bar = findViewById(R.id.hiddenSearchBar);
        search_bar = findViewById(R.id.searchBar);
        back_to_top = findViewById(R.id.backToTopFAB);
        navigation_menu_icon = findViewById(R.id.navigation_menu_icon);

        govSpinner = findViewById(R.id.govSpinner);
        citySpinner = findViewById(R.id.citySpinner);

        registerForContextMenu(navigation_menu_icon);

        medicineList.setLayoutManager(new LinearLayoutManager(this));
        medicineList.setItemAnimator(new DefaultItemAnimator());


        hidden_search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filteredMedicine = new ArrayList<>();
                if (charSequence.toString() != "" ){
                    for (Medicine medicine : medicines) {
                        if (medicine.getName_en().toLowerCase().contains(charSequence.toString().toLowerCase()) ||
                            medicine.getName_ar().toLowerCase().contains(charSequence.toString().toLowerCase()) ) {
                            filteredMedicine.add(medicine);
                        }
                    }
                    setMedicineAdapter(filteredMedicine);
                }
                else
                    setMedicineAdapter(medicines);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    public void showItemContainer(){
        item_container.setVisibility(View.VISIBLE);
        medicines_loading.setVisibility(View.GONE);
    }

    public void backToTop(View view) {
        scrollContainer.smoothScrollTo(0,0);
    }

    public void showMenu(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            view.showContextMenu(0f,150f);
        }
        else {
            view.showContextMenu();
        }
    }
}