package com.moschd002.studentportal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener {

    private List<PortalItem> mPortals;
    private PortalAdapter mPortalAdapter;
    private RecyclerView mPortalRecyclerView;
    public static final int REQUEST_CODE_CREATE = 1001;
    public static final int REQUEST_CODE_SELECT = 1002;
    public static final String CREATED_PORTAL = "Create portal";
    public static final String PORTAL_SELECT = "Portal selected";
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPortals = new ArrayList<>();
        setupRecyclerView();
        setupGestureDetection();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateActivity.class);
                startActivityForResult(intent, REQUEST_CODE_CREATE);
            }
        });

        RefreshRecyclerView();
    }

    private void setupRecyclerView() {
        mPortalRecyclerView = findViewById(R.id.portalRecyclerView);
        mPortalRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL));
    }

    private void RefreshRecyclerView() {
        if (mPortalAdapter == null) {
            mPortalAdapter = new PortalAdapter(mPortals);
            mPortalRecyclerView.setAdapter(mPortalAdapter);
        } else {
            mPortalAdapter.notifyDataSetChanged();
        }
    }

    private void setupGestureDetection() {

        // add touch listener to RecycleView
        mPortalRecyclerView.addOnItemTouchListener(this);

        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CREATE) {
            if (resultCode == RESULT_OK) {
                PortalItem createdPortalItem = (PortalItem) data.getExtras().get(MainActivity.CREATED_PORTAL);
                mPortals.add(createdPortalItem);
                RefreshRecyclerView();
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        // get x and y of where the user is touching the screen
        View viewTouchPosition = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

        if (viewTouchPosition == null) {
            return false;
        }

        // get the item the user is touching within the recyclerview by providing the child
        int touchedItem = recyclerView.getChildAdapterPosition(viewTouchPosition);

        if (touchedItem >= 0 && mGestureDetector.onTouchEvent(motionEvent)) {
            PortalItem portalItem = mPortals.get(touchedItem);

            // open up webview with the selected portal
            Intent intent = new Intent(MainActivity.this, WebActivity.class);
            intent.putExtra(PORTAL_SELECT, portalItem);
            startActivityForResult(intent, REQUEST_CODE_SELECT);
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
