package com.moschd002.gamebacklog.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.moschd002.gamebacklog.db.model.GameBacklogItem;

import java.util.List;

@Dao
public interface GameBacklogDao {

    @Insert
    void insert(GameBacklogItem backlogItem);

    @Delete
    void delete(GameBacklogItem backlogItem);

    @Update
    void update(GameBacklogItem backlogItem);

    @Query("DELETE FROM game_backlog_table")
    void deleteAll();

    /**
     * @return
     */
    @Query("SELECT * from game_backlog_table")
    public LiveData<List<GameBacklogItem>> getGameBacklogItems();

}