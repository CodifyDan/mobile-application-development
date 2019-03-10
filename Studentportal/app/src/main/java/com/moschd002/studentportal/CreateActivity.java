package com.moschd002.studentportal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateActivity extends AppCompatActivity {
    EditText mPortalItemName;
    EditText mPortalItemLink;
    Button mAddPortalBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_portal);

        mPortalItemName = findViewById(R.id.portalItemName);
        mPortalItemLink = findViewById(R.id.portalItemUrl);
        mAddPortalBtn = findViewById(R.id.addPortalBtn);

        mAddPortalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String portalName = mPortalItemName.getText().toString();
                String portalLink = mPortalItemLink.getText().toString();
                PortalItem portalItem = new PortalItem(portalName, portalLink);

                if (!portalLink.isEmpty() && !portalName.isEmpty()) {
                    Intent intent = new Intent();
                    intent.putExtra(MainActivity.CREATED_PORTAL, portalItem);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    Snackbar.make(view, "Please fill in all the fields above", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }
}
