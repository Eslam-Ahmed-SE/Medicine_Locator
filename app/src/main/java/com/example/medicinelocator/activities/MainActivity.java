package com.example.medicinelocator.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.medicinelocator.FillMedicineListTask;
import com.example.medicinelocator.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();


        FillMedicineListTask medicineListTask = new FillMedicineListTask(medicineList, this);
        medicineListTask.execute();

        final Rect scrollBounds = new Rect();
        scrollContainer.getHitRect(scrollBounds);
        initOnScrollListener();




//        if(medicineListTask.getStatus() == AsyncTask.Status.PENDING){
//            // My AsyncTask has not started yet
//        }
//
//        if(medicineListTask.getStatus() == AsyncTask.Status.RUNNING){
//            // My AsyncTask is currently doing work in doInBackground()
//        }
//
//        if(medicineListTask.getStatus() == AsyncTask.Status.FINISHED){
//
//        }
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

        registerForContextMenu(navigation_menu_icon);

        medicineList.setLayoutManager(new LinearLayoutManager(this));
        medicineList.setItemAnimator(new DefaultItemAnimator());
        Log.i("inf", "init comps");
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