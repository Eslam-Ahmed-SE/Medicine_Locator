package com.example.medicinelocator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private RecyclerView medicineList;
    private ConstraintLayout item_container;
    private ConstraintLayout searchContainer;
    private ConstraintLayout mainPageContainer;
    private ShimmerFrameLayout medicines_loading;
    private NestedScrollView scrollContainer;
    private EditText hidden_search_bar;
    private FloatingActionButton back_to_top;


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

                if (searchContainer.getLocalVisibleRect(scrollBounds)) {
                    Log.i("scroll", "true");
                    hidden_search_bar.setVisibility(View.GONE);
                    back_to_top.setVisibility(View.GONE);

                } else {
                    Log.i("scroll", "false");
                    hidden_search_bar.setVisibility(View.VISIBLE);
                    back_to_top.setVisibility(View.VISIBLE);
                }


            }
        });
    }

    private void initComponents() {
        medicineList = findViewById(R.id.medList);
        item_container = findViewById(R.id.recent_container);
        searchContainer = findViewById(R.id.searchContainer);
        mainPageContainer = findViewById(R.id.main_page_container);
        medicines_loading = findViewById(R.id.medicines_loading);
        scrollContainer = findViewById(R.id.scrollContainer);
        hidden_search_bar = findViewById(R.id.hiddenSearchBar);
        back_to_top = findViewById(R.id.backToTopFAB);

        medicineList.setLayoutManager(new LinearLayoutManager(this));
        medicineList.setItemAnimator(new DefaultItemAnimator());
        Log.i("inf", "init comps");
    }

    public void showItemContainer(){
        item_container.setVisibility(View.VISIBLE);
        medicines_loading.setVisibility(View.GONE);
    }

    public void backToTop(View view) {
        scrollContainer.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

            }
        });
        scrollContainer.smoothScrollTo(0,0, 300);
        back_to_top.setVisibility(View.GONE);

        (new Handler()).postDelayed(this::initOnScrollListener, 290);

    }
}