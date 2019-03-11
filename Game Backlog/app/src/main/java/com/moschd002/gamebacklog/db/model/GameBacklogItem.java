package com.moschd002.gamebacklog.db.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.moschd002.gamebacklog.utils.DateConverter;

import java.io.Serializable;
import java.sql.Date;

@TypeConverters(DateConverter.class)
@Entity(tableName = "game_backlog_table")
public class GameBacklogItem implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "platform")
    private String platform;
    @ColumnInfo(name = "date")
    private Date date;
    @ColumnInfo(name = "status")
    private String status;

    public GameBacklogItem(String title, String platform, Date date, String status) {
        this.title = title;
        this.platform = platform;
        this.date = date;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
