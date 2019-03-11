package com.moschd002.bucketlist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateItemActivity extends AppCompatActivity {
    private EditText mTitle;
    private EditText mDescription;
    private Button mCreateBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_main);

        mTitle = findViewById(R.id.titleText);
        mDescription = findViewById(R.id.descriptionText);
        mCreateBtn = findViewById(R.id.createBtn);

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = mTitle.getText().toString();
                String description = mDescription.getText().toString();
                BucketListItem bucketListItem = new BucketListItem(title, description, false);

                if (!title.isEmpty() && !description.isEmpty()) {
                    Intent intent = new Intent();
                    intent.putExtra(MainActivity.CREATED_BUCKET_ITEM, bucketListItem);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    Snackbar.make(view, "Please fill in all the fields above", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }
}
