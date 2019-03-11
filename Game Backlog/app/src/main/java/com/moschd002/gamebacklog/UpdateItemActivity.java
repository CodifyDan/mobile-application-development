package com.moschd002.gamebacklog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.moschd002.gamebacklog.db.model.GameBacklogItem;

import java.sql.Date;
import java.util.Calendar;

public class UpdateItemActivity extends AppCompatActivity {
    private EditText mTitle;
    private EditText mPlatform;
    private FloatingActionButton mCreateBtn;
    private Spinner mDropdown;
    private GameBacklogItem mGameBacklogItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_main);
        mGameBacklogItem = (GameBacklogItem) getIntent().getExtras().getSerializable(MainActivity.UPDATE_GAME_BACKLOG_ITEM);

        mTitle = findViewById(R.id.titleUpdateText);
        mTitle.setText(mGameBacklogItem.getTitle());

        mPlatform = findViewById(R.id.platformUpdateText);
        mPlatform.setText(mGameBacklogItem.getPlatform());

        mDropdown = findViewById(R.id.statusUpdateSelector);
        String[] items = new String[]{"Want to play", "Playing", "Stalled", "Dropped"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        mDropdown.setAdapter(adapter);
        mDropdown.setSelection(adapter.getPosition(mGameBacklogItem.getStatus()));

        mCreateBtn = findViewById(R.id.updateBtn);

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String formValidationMessage = getResources().getString(R.string.form_validation_message);
                Calendar calendar = Calendar.getInstance();

                mGameBacklogItem.setTitle(mTitle.getText().toString());
                mGameBacklogItem.setPlatform(mPlatform.getText().toString());
                mGameBacklogItem.setStatus(mDropdown.getSelectedItem().toString());
                mGameBacklogItem.setDate(new Date(calendar.getTime().getTime()));

                if (!mGameBacklogItem.getTitle().isEmpty() && !mGameBacklogItem.getPlatform().isEmpty() && !mGameBacklogItem.getStatus().isEmpty()) {
                    Intent intent = new Intent();
                    intent.putExtra(MainActivity.UPDATE_GAME_BACKLOG_ITEM, mGameBacklogItem);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    Snackbar.make(view, formValidationMessage, Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }
}
