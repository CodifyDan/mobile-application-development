package com.moschd002.bucketlist;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface BucketListDao {

    @Insert
    void insert(BucketListItem product);

    @Delete
    void delete(BucketListItem product);

    @Update
    void update(BucketListItem product);

    @Delete
    void delete(List<BucketListItem> products);

    /**
     * @return
     */
    @Query("SELECT * from bucket_list_table")
    List<BucketListItem> getBucketListItems();

}