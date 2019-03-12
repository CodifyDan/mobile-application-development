package com.moschd002.gamebacklog.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.moschd002.gamebacklog.db.GameBacklogRepository;
import com.moschd002.gamebacklog.db.model.GameBacklogItem;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private GameBacklogRepository mRepository;
    private LiveData<List<GameBacklogItem>> mGameBacklogs;

    public MainViewModel(@NonNull Application application) {
        super(application);
        mRepository = new GameBacklogRepository(application.getApplicationContext());
        mGameBacklogs = mRepository.getAllGameBacklogs();
    }

    public LiveData<List<GameBacklogItem>> getGameBacklogs() {
        return mGameBacklogs;
    }

    public void insert(GameBacklogItem gameBacklogItem) {
        mRepository.insert(gameBacklogItem);
    }

    public void update(GameBacklogItem gameBacklogItem) {
        mRepository.update(gameBacklogItem);
    }

    public void delete(GameBacklogItem gameBacklogItem) {
        mRepository.delete(gameBacklogItem);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }
}
