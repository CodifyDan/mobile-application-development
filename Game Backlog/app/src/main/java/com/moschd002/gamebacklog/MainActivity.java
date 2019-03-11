package com.moschd002.gamebacklog;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.moschd002.gamebacklog.db.GameBacklogDatabase;
import com.moschd002.gamebacklog.db.model.GameBacklogItem;
import com.moschd002.gamebacklog.ui.GameBacklogAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements GameBacklogAdapter.ButtonClickListener, RecyclerView.OnItemTouchListener {

    private RecyclerView mGameBacklogRecyclerView;
    private GameBacklogAdapter mGameBacklogAdapter;
    private List<GameBacklogItem> mGameBacklogItems;
    private GameBacklogDatabase db;
    private Executor mExecutor;
    private GestureDetector mGestureDetector;
    public static final String CREATED_GAME_BACKLOG_ITEM = "create";
    public static final String UPDATE_GAME_BACKLOG_ITEM = "update";
    private final String UNDO_TEXT = "UNDO";
    public static final int REQUEST_CODE_CREATE = 1001;
    public static final int REQUEST_CODE_UPDATE = 1002;

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
                final int currentItem = (viewHolder.getAdapterPosition());
                final GameBacklogItem gameBacklogItem = mGameBacklogItems.get(currentItem);

                mGameBacklogItems.remove(gameBacklogItem);
                mGameBacklogAdapter.notifyDataSetChanged();

                if (swipeDirection == ItemTouchHelper.LEFT || swipeDirection == ItemTouchHelper.RIGHT) {

                    Snackbar.make(findViewById(android.R.id.content),
                            "Deleted: " + gameBacklogItem.getTitle(), Snackbar.LENGTH_LONG)
                            .setAction(UNDO_TEXT, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            }).addCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar snackbar, int dismissType) {
                            super.onDismissed(snackbar, dismissType);
                            if (dismissType == DISMISS_EVENT_TIMEOUT || dismissType == DISMISS_EVENT_SWIPE
                                    || dismissType == DISMISS_EVENT_CONSECUTIVE || dismissType == DISMISS_EVENT_MANUAL) {
                                deleteGameBacklogItem(gameBacklogItem);
                            } else {
                                getAllGameBacklogItems();
                            }
                        }
                    }).show();
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(GameBacklogItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mGameBacklogRecyclerView);
    }

    private void getAllGameBacklogItems() {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final List<GameBacklogItem> gameBacklogItems = db.GameBacklogDao().getGameBacklogItems();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUI(gameBacklogItems);
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

    private void deleteAllGameBacklogItems() {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                db.GameBacklogDao().deleteAll();
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete_item) {
            View view = findViewById(android.R.id.content);
            mGameBacklogItems.clear();
            mGameBacklogAdapter.notifyDataSetChanged();

            Snackbar.make(view,
                    "Deleted all games", Snackbar.LENGTH_LONG)
                    .setAction(UNDO_TEXT, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).addCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar snackbar, int dismissType) {
                    super.onDismissed(snackbar, dismissType);
                    if (dismissType == DISMISS_EVENT_TIMEOUT || dismissType == DISMISS_EVENT_SWIPE
                            || dismissType == DISMISS_EVENT_CONSECUTIVE || dismissType == DISMISS_EVENT_MANUAL) {
                        deleteAllGameBacklogItems();
                    } else {
                        getAllGameBacklogItems();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CREATE) {
            if (resultCode == RESULT_OK) {
                GameBacklogItem gameBacklogItem = (GameBacklogItem) data.getExtras().getSerializable(MainActivity.CREATED_GAME_BACKLOG_ITEM);
                insertGameBacklogItem(gameBacklogItem);
            }
        } else if (requestCode == REQUEST_CODE_UPDATE) {
            if (resultCode == RESULT_OK) {
                GameBacklogItem gameBacklogItem = (GameBacklogItem) data.getExtras().getSerializable(MainActivity.UPDATE_GAME_BACKLOG_ITEM);
                updateGameBacklogItem(gameBacklogItem);
            }
        }
    }
}
