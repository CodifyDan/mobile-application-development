package com.moschd002.bucketlist;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {BucketListItem.class}, version = '1', exportSchema = false)
public abstract class BucketListDatabase extends RoomDatabase {
    private static BucketListDatabase INSTANCE;

    public abstract BucketListDao BucketListDao();

    public static BucketListDatabase getDatabase(final Context context) {

        if (INSTANCE == null) {
            synchronized (BucketListDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), BucketListDatabase.class, "BucketListDB").build();
                }
            }
        }

        return INSTANCE;
    }
}