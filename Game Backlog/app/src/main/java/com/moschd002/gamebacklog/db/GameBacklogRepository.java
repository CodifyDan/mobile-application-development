package com.moschd002.gamebacklog.db;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.moschd002.gamebacklog.db.dao.GameBacklogDao;
import com.moschd002.gamebacklog.db.model.GameBacklogItem;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GameBacklogRepository {

    private GameBacklogDatabase mAppDatabase;
    private GameBacklogDao mGameBacklogDao;
    private LiveData<List<GameBacklogItem>> mGameBacklogs;
    private Executor mExecutor = Executors.newSingleThreadExecutor();

    public GameBacklogRepository(Context context) {
        mAppDatabase = GameBacklogDatabase.getDatabase(context);
        mGameBacklogDao = mAppDatabase.GameBacklogDao();
        mGameBacklogs = mGameBacklogDao.getGameBacklogItems();
    }

    public LiveData<List<GameBacklogItem>> getAllGameBacklogs() {
        return mGameBacklogs;
    }

    public void insert(final GameBacklogItem gameBacklogItem) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mGameBacklogDao.insert(gameBacklogItem);
            }
        });
    }

    public void update(final GameBacklogItem gameBacklogItem) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mGameBacklogDao.update(gameBacklogItem);
            }
        });
    }


    public void delete(final GameBacklogItem gameBacklogItem) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mGameBacklogDao.delete(gameBacklogItem);
            }
        });
    }

    public void deleteAll() {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mGameBacklogDao.deleteAll();
            }
        });
    }
}
