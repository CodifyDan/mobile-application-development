package com.moschd002.gamebacklog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.sql.Date;
import java.util.Calendar;

public class CreateItemActivity extends AppCompatActivity {
    private EditText mTitle;
    private EditText mPlatform;
    private Button mCreateBtn;
    private Spinner mDropdown;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_main);

        mTitle = findViewById(R.id.titleText);
        mPlatform = findViewById(R.id.platformText);

        mDropdown = findViewById(R.id.statusSelector);
        String[] items = new String[]{"Want to play", "Playing", "Stalled", "Dropped"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        mDropdown.setAdapter(adapter);

        mCreateBtn = findViewById(R.id.createBtn);

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = mTitle.getText().toString();
                String platform = mPlatform.getText().toString();
                String status = mDropdown.getSelectedItem().toString();

                Calendar calendar = Calendar.getInstance();

                GameBacklogItem gameBacklogItem = new GameBacklogItem(title, platform, new Date(calendar.getTime().getTime()), status);

                if (!title.isEmpty() && !platform.isEmpty() && !status.isEmpty()) {
                    Intent intent = new Intent();
                    intent.putExtra(MainActivity.CREATED_GAME_BACKLOG_ITEM, gameBacklogItem);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    Snackbar.make(view, "Please fill in all the fields above", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }
}
