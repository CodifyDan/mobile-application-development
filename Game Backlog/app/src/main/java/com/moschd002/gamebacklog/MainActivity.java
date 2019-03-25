package com.moschd002.gamebacklog;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.moschd002.gamebacklog.db.model.GameBacklogItem;
import com.moschd002.gamebacklog.ui.GameBacklogAdapter;
import com.moschd002.gamebacklog.ui.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GameBacklogAdapter.ButtonClickListener, RecyclerView.OnItemTouchListener {

    private RecyclerView mGameBacklogRecyclerView;
    private GameBacklogAdapter mGameBacklogAdapter;
    private List<GameBacklogItem> mGameBacklogItems;
    private GestureDetector mGestureDetector;
    private MainViewModel mMainViewModel;

    private GameBacklogItem mRecentlyDeletedGameBacklogItem;
    private int mRecentlyDeletedGameBacklogItemPosition;

    public static final String UPDATE_GAME_BACKLOG_ITEM = "update";
    public static final int REQUEST_CODE_CREATE = 1001;
    public static final int REQUEST_CODE_UPDATE = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mGameBacklogItems = new ArrayList<>();

        setupRecyclerView();
        setupGestures();

        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mMainViewModel.getGameBacklogs().observe(this, new Observer<List<GameBacklogItem>>() {
            @Override
            public void onChanged(@Nullable List<GameBacklogItem> gameBacklogItems) {
                // if the gamebacklog item list changes, notify and update the UI
                updateUI(gameBacklogItems);
            }
        });

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
        mGameBacklogRecyclerView = findViewById(R.id.gameBacklogRecyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);

        mGameBacklogRecyclerView.setLayoutManager(mLayoutManager);
        mGameBacklogAdapter = new GameBacklogAdapter(mGameBacklogItems, this);
        mGameBacklogRecyclerView.setAdapter(mGameBacklogAdapter);
    }

    private void setupGestures() {
        mGameBacklogRecyclerView.addOnItemTouchListener(this);
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        ItemTouchHelper.SimpleCallback GameBacklogItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int swipeDirection) {
                final int currentItem = viewHolder.getAdapterPosition();
                final GameBacklogItem gameBacklogItem = mGameBacklogItems.get(currentItem);

                mRecentlyDeletedGameBacklogItem = gameBacklogItem;
                // we keep the position of the item so that we can re-add in the same place it if the deletion is cancelled
                mRecentlyDeletedGameBacklogItemPosition = currentItem;

                mGameBacklogItems.remove(gameBacklogItem);
                mGameBacklogAdapter.notifyItemRemoved(currentItem);

                // on swipe left or right, delete the gameBackLogItem
                if (swipeDirection == ItemTouchHelper.LEFT || swipeDirection == ItemTouchHelper.RIGHT) {

                    Snackbar.make(findViewById(android.R.id.content),
                            getResources().getString(R.string.deleted_item) + " " + gameBacklogItem.getTitle(), Snackbar.LENGTH_LONG)
                            .setAction(getResources().getString(R.string.undo), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            }).addCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar snackbar, int dismissType) {
                            super.onDismissed(snackbar, dismissType);
                            if (dismissType == DISMISS_EVENT_TIMEOUT || dismissType == DISMISS_EVENT_SWIPE
                                    || dismissType == DISMISS_EVENT_CONSECUTIVE || dismissType == DISMISS_EVENT_MANUAL) {
                                mMainViewModel.delete(gameBacklogItem);
                            } else {
                                mGameBacklogItems.add(mRecentlyDeletedGameBacklogItemPosition, mRecentlyDeletedGameBacklogItem);
                                mGameBacklogAdapter.notifyItemInserted(mRecentlyDeletedGameBacklogItemPosition);
                            }
                        }
                    }).show();
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(GameBacklogItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mGameBacklogRecyclerView);
    }

    private void updateUI(List<GameBacklogItem> gameBacklogItems) {
        mGameBacklogItems.clear();
        mGameBacklogItems.addAll(gameBacklogItems);
        mGameBacklogAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete_item) {

            if (mGameBacklogItems.isEmpty()) {
                return false;
            }

            View view = findViewById(android.R.id.content);
            mGameBacklogAdapter.notifyItemRangeRemoved(0, mGameBacklogItems.size()); // animates the removal
            mGameBacklogItems.clear();

            Snackbar.make(view,
                    getResources().getString(R.string.deleted_all), Snackbar.LENGTH_LONG)
                    .setAction(getResources().getString(R.string.undo), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).addCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar snackbar, int dismissType) {
                    super.onDismissed(snackbar, dismissType);

                    // if snackbar is closed
                    if (dismissType == DISMISS_EVENT_TIMEOUT || dismissType == DISMISS_EVENT_SWIPE
                            || dismissType == DISMISS_EVENT_CONSECUTIVE || dismissType == DISMISS_EVENT_MANUAL) {
                        mMainViewModel.deleteAll();
                    } else {
                        // if undo button is clicked, re-add all the items
                        mGameBacklogItems.addAll(mMainViewModel.getGameBacklogs().getValue());
                        mGameBacklogAdapter.notifyDataSetChanged();
                    }
                }
            }).show();

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
        Intent intent = new Intent(MainActivity.this, UpdateItemActivity.class);
        intent.putExtra(UPDATE_GAME_BACKLOG_ITEM, gameBacklogItem);
        startActivityForResult(intent, REQUEST_CODE_UPDATE);
    }
}
