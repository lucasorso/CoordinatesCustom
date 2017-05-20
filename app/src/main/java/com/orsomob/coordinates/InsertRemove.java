package com.orsomob.coordinates;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by LucasOrso on 5/14/17.
 */

public class InsertRemove extends AppCompatActivity implements View.OnClickListener {

    EditText mEditTextCordX;
    EditText mEditTextCordY;
    Button mButtonInsert;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_remove);
        getReferences();
        setActions();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void getReferences() {
        mEditTextCordX = (EditText)findViewById(R.id.ed_x);
        mEditTextCordY = (EditText)findViewById(R.id.ed_y);
        mButtonInsert = (Button)findViewById(R.id.btn_insert);
    }
    private void setActions() {
        mButtonInsert.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_insert:
                Bundle lBundle = new Bundle();
                lBundle.putInt("x", Integer.valueOf(mEditTextCordX.getText().toString()));
                lBundle.putInt("y", Integer.valueOf(mEditTextCordY.getText().toString()));
                Intent lIntent = new Intent();
                lIntent.putExtras(lBundle);
                setResult(Activity.RESULT_OK, lIntent);
                finish();
                break;
        }
    }
}
