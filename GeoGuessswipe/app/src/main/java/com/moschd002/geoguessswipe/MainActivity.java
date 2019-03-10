package com.moschd002.geoguessswipe;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener {

    private List<GeoGuessItem> mGeoGuessItems;
    private RecyclerView mGeoGuessRecyclerView;
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGeoGuessItems = new ArrayList<>();
        mGeoGuessRecyclerView = findViewById(R.id.geoGuessRecyclerView);

        // create the list of items
        createGeoGuessItems();
        // setup the recycler view to display the items
        setupRecyclerView();
        // setup the gesture detection for swipe and tap
        setupGestureDetection();
    }

    private void createGeoGuessItems() {
        for (int i = 0; i < GeoGuessItem.GEO_GUESS_COUNTRY_NAMES.length; i++) {
            mGeoGuessItems.add(new GeoGuessItem(GeoGuessItem.GEO_GUESS_COUNTRY_NAMES[i], GeoGuessItem.GEO_GUESS_IMAGES[i], GeoGuessItem.GEO_GUESS_INEUROPE[i]));
        }
    }

    private void setupRecyclerView() {

        // set the layout to vertical and make the size fixed
        RecyclerView.LayoutManager mLayout = new GridLayoutManager(this, LinearLayoutManager.VERTICAL);
        mGeoGuessRecyclerView.setLayoutManager(mLayout);
        mGeoGuessRecyclerView.setHasFixedSize(true);

        // set the adapter for the recyclerview
        final GeoGuessItemAdapter mAdapter = new GeoGuessItemAdapter(mGeoGuessItems);
        mGeoGuessRecyclerView.setAdapter(mAdapter);
    }

    private void setupGestureDetection() {

        // add touch listener to RecycleView
        mGeoGuessRecyclerView.addOnItemTouchListener(this);

        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        ItemTouchHelper.SimpleCallback geoItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int swipeDirection) {
                int currentItem = (viewHolder.getAdapterPosition());

                if (swipeDirection == ItemTouchHelper.LEFT && mGeoGuessItems.get(currentItem).ismGeoGuessInEurope()) {
                    Toast.makeText(MainActivity.this, "Your answer is correct! " + mGeoGuessItems.get(currentItem).getmGeoGuessName() + " is part of europe.", Toast.LENGTH_SHORT).show();
                } else if (swipeDirection == ItemTouchHelper.RIGHT && !mGeoGuessItems.get(currentItem).ismGeoGuessInEurope()) {
                    Toast.makeText(MainActivity.this, "Your answer is correct! " + mGeoGuessItems.get(currentItem).getmGeoGuessName() + " is not part of europe.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Incorrect answer", Toast.LENGTH_SHORT).show();
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(geoItemTouchCallback);

        // attach the swipe helper to the recyclerview
        itemTouchHelper.attachToRecyclerView(mGeoGuessRecyclerView);
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        // get x and y of where the user is touching the screen
        View viewTouchPosition = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

        // get the item the user is touching within the recyclerview by providing the child
        int touchedItem = recyclerView.getChildAdapterPosition(viewTouchPosition);

        if (touchedItem >= 0 && mGestureDetector.onTouchEvent(motionEvent)) {
            Toast.makeText(this, mGeoGuessItems.get(touchedItem).getmGeoGuessName(), Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }
}
