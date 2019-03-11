package com.moschd002.gamebacklog;

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

public class MainActivity extends AppCompatActivity implements GameBacklogListAdapter.ButtonClickListener, RecyclerView.OnItemTouchListener {

    private RecyclerView mGameBacklogRecyclerView;
    private GameBacklogListAdapter mGameBacklogAdapter;
    private List<GameBacklogItem> mGameBacklogItems;
    private GameBacklogDatabase db;
    private Executor mExecutor;
    private GestureDetector mGestureDetector;
    public static final String CREATED_GAME_BACKLOG_ITEM = "create";
    public static final int REQUEST_CODE_CREATE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db = GameBacklogDatabase.getDatabase(this);
        mExecutor = Executors.newSingleThreadExecutor();

        mGameBacklogItems = new ArrayList<>();
        setupRecyclerView();
        getAllGameBacklogItems();
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
        mGameBacklogRecyclerView = findViewById(R.id.bucketListRecyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);

        mGameBacklogRecyclerView.setLayoutManager(mLayoutManager);
        mGameBacklogRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        mGameBacklogAdapter = new GameBacklogListAdapter(mGameBacklogItems, this);
        mGameBacklogRecyclerView.setAdapter(mGameBacklogAdapter);
    }

    private void setupGestures() {
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                View child = mGameBacklogRecyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null) {
                    int adapterPosition = mGameBacklogRecyclerView.getChildAdapterPosition(child);
                    deleteGameBacklogItem(mGameBacklogItems.get(adapterPosition));
                }
            }
        });
        mGameBacklogRecyclerView.addOnItemTouchListener(this);
    }

    private void getAllGameBacklogItems() {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final List<GameBacklogItem> bucketListItems = db.GameBacklogDao().getGameBacklogItems();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUI(bucketListItems);
                    }
                });
            }
        });
    }

    private void insertGameBacklogItem(final GameBacklogItem gameBacklogItem) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                db.GameBacklogDao().insert(gameBacklogItem);
                getAllGameBacklogItems();
            }
        });
    }

    private void updateGameBacklogItem(final GameBacklogItem gameBacklogItem) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                db.GameBacklogDao().update(gameBacklogItem);
                getAllGameBacklogItems();
            }
        });
    }

    private void deleteGameBacklogItem(final GameBacklogItem gameBacklogItem) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                db.GameBacklogDao().delete(gameBacklogItem);
                getAllGameBacklogItems();
            }
        });
    }

    private void deleteAllGameBacklogItems(final List<GameBacklogItem> gameBacklogItems) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                db.GameBacklogDao().delete(gameBacklogItems);
                getAllGameBacklogItems();
            }
        });
    }

    private void updateUI(List<GameBacklogItem> gameBacklogItems) {
        mGameBacklogItems.clear();
        mGameBacklogItems.addAll(gameBacklogItems);
        mGameBacklogAdapter.notifyDataSetChanged();
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
            deleteAllGameBacklogItems(mGameBacklogItems);
            View view = findViewById(android.R.id.content);
            Snackbar.make(view, "Removed all game backlog items!", Snackbar.LENGTH_LONG).show();
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
    public void onSelect(GameBacklogItem gameBacklogItem) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CREATE) {
            if (resultCode == RESULT_OK) {
                GameBacklogItem createdBucketLitItem = (GameBacklogItem) data.getExtras().get(MainActivity.CREATED_GAME_BACKLOG_ITEM);
                insertGameBacklogItem(createdBucketLitItem);
            }
        }
    }
}
