package com.moschd002.gamebacklog;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {GameBacklogItem.class}, version = '1', exportSchema = false)
public abstract class GameBacklogDatabase extends RoomDatabase {
    private static GameBacklogDatabase INSTANCE;

    public abstract GameBacklogDao GameBacklogDao();

    public static GameBacklogDatabase getDatabase(final Context context) {

        if (INSTANCE == null) {
            synchronized (GameBacklogDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), GameBacklogDatabase.class, "GameBacklogDB").build();
                }
            }
        }

        return INSTANCE;
    }
}