package com.moschd002.gamebacklog;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface GameBacklogDao {

    @Insert
    void insert(GameBacklogItem backlogItem);

    @Delete
    void delete(GameBacklogItem backlogItem);

    @Update
    void update(GameBacklogItem backlogItem);

    @Delete
    void delete(List<GameBacklogItem> backlogItem);

    /**
     * @return
     */
    @Query("SELECT * from game_backlog_table")
    List<GameBacklogItem> getGameBacklogItems();

}