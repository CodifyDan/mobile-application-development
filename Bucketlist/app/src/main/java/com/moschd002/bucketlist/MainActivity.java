package com.moschd002.bucketlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements BucketListAdapter.ButtonClickListener, RecyclerView.OnItemTouchListener {

    private RecyclerView mBucketListRecyclerView;
    private BucketListAdapter mBucketListAdapter;
    private List<BucketListItem> mBucketListItems;
    private BucketListDatabase db;
    private Executor mExecutor;
    private GestureDetector mGestureDetector;
    public static final String CREATED_BUCKET_ITEM = "create";
    public static final int REQUEST_CODE_CREATE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db = BucketListDatabase.getDatabase(this);
        mExecutor = Executors.newSingleThreadExecutor();

        mBucketListItems = new ArrayList<>();
        setupRecyclerView();
        getAllBucketListItems();
        setupGestures();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateItemActivity.class);
                startActivityForResult(intent, REQUEST_CODE_CREATE);
            }
        });
    }

    private void setupRecyclerView() {
        mBucketListRecyclerView = findViewById(R.id.bucketListRecyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);

        mBucketListRecyclerView.setLayoutManager(mLayoutManager);
        mBucketListRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        mBucketListAdapter = new BucketListAdapter(mBucketListItems, this);
        mBucketListRecyclerView.setAdapter(mBucketListAdapter);
    }

    private void setupGestures() {
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                View child = mBucketListRecyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null) {
                    int adapterPosition = mBucketListRecyclerView.getChildAdapterPosition(child);
                    deleteBucketListItem(mBucketListItems.get(adapterPosition));
                }
            }
        });
        mBucketListRecyclerView.addOnItemTouchListener(this);
    }

    private void getAllBucketListItems() {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final List<BucketListItem> bucketListItems = db.BucketListDao().getBucketListItems();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUI(bucketListItems);
                    }
                });
            }
        });
    }

    private void insertBucketListItem(final BucketListItem bucketListItem) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                db.BucketListDao().insert(bucketListItem);
                getAllBucketListItems();
            }
        });
    }

    private void updateBucketListItem(final BucketListItem bucketListItem) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                db.BucketListDao().update(bucketListItem);
                getAllBucketListItems();
            }
        });
    }

    private void deleteBucketListItem(final BucketListItem bucketListItem) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                db.BucketListDao().delete(bucketListItem);
                getAllBucketListItems();
            }
        });
    }

    private void deleteAllBucketListItems(final List<BucketListItem> bucketListItems) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                db.BucketListDao().delete(bucketListItems);
                getAllBucketListItems();
            }
        });
    }

    private void updateUI(List<BucketListItem> bucketListItems) {
        mBucketListItems.clear();
        mBucketListItems.addAll(bucketListItems);
        mBucketListAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete_item) {
            deleteAllBucketListItems(mBucketListItems);
            View view = findViewById(android.R.id.content);
            Snackbar.make(view, "Removed all bucket list items!", Snackbar.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        mGestureDetector.onTouchEvent(motionEvent);
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }

    @Override
    public void onSelect(BucketListItem bucketListItem) {
        bucketListItem.setChecked(!bucketListItem.isChecked());
        updateBucketListItem(bucketListItem);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CREATE) {
            if (resultCode == RESULT_OK) {
                BucketListItem createdBucketLitItem = (BucketListItem) data.getExtras().get(MainActivity.CREATED_BUCKET_ITEM);
                insertBucketListItem(createdBucketLitItem);
            }
        }
    }
}
