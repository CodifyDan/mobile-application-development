package com.moschd002.logicaapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TRUE_VALUE = "T";
    private static final String FALSE_VALUE = "F";
    private static final String CORRECT_TOAST_MESSAGE = "Correct";
    private static final String INCORRECT_TOAST_MESSAGE = "Try again";

    private Button mSubmitBtn;
    private EditText Input1;
    private EditText Input2;
    private EditText Input3;
    private EditText Input4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Input1 = findViewById(R.id.inputField1);
        Input2 = findViewById(R.id.inputField2);
        Input3 = findViewById(R.id.inputField3);
        Input4 = findViewById(R.id.inputField4);

        mSubmitBtn = findViewById(R.id.submitButton);
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // this verifies the input of the user when the submit button is pressed
                VerifyInput();
            }
        });
    }

    private void VerifyInput() {
        boolean Input1Success = CompareValues(Input1, TRUE_VALUE);
        boolean Input2Success = CompareValues(Input2, FALSE_VALUE);
        boolean Input3Success = CompareValues(Input3, FALSE_VALUE);
        boolean Input4Success = CompareValues(Input4, FALSE_VALUE);

        if (Input1Success && Input2Success && Input3Success && Input4Success) {
            Toast.makeText(MainActivity.this, CORRECT_TOAST_MESSAGE, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MainActivity.this, INCORRECT_TOAST_MESSAGE, Toast.LENGTH_LONG).show();
        }
    }

    private boolean CompareValues(EditText textField, String compareTo) {

        if (textField.getText().toString().toUpperCase().equals(compareTo)) {
            return true;
        }

        return false;
    }

}
