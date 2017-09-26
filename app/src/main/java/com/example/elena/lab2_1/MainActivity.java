package com.example.elena.lab2_1;

import android.content.SharedPreferences;
import android.icu.lang.UCharacterEnums;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView tvInfo, tvError, tvTries, tvRecord;
    EditText etChoice;
    Button btEnter, btRestart;

    int randNumber, numTries;
    int record;
    Random rnd;
    SharedPreferences sp;

    private String rec_str = "record";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvInfo = (TextView)findViewById(R.id.tvInfo);
        tvError = (TextView)findViewById(R.id.tvError);
        tvTries = (TextView)findViewById(R.id.tvTries);
        tvRecord = (TextView)findViewById(R.id.tvRecord);
        etChoice = (EditText)findViewById(R.id.etChoice);
        btEnter = (Button)findViewById(R.id.btEnter);
        btRestart = (Button)findViewById(R.id.btRestart);

        rnd = new Random();
        sp = getPreferences(MODE_PRIVATE);
        //initializePreferences();
        record = sp.getInt(rec_str, 0);
        setValues();
    }

    public void onClickEnter(View v) {
        String userChoiceStr = etChoice.getText().toString();
        if (userChoiceStr.equals("")) {
            tvError.setText(getResources().getString(R.string.error));
            return;
        }
        int userChoice = Integer.parseInt(userChoiceStr);
        tvError.setText("");
        tvTries.setText(getResources().getString(R.string.number_attempts)
                + Integer.toString(++numTries));
        if (userChoice < 1 || userChoice > 100) {
            tvInfo.setText(getResources().getString(R.string.try_to_guess));
            tvError.setText(getResources().getString(R.string.error));
        }
        else if (userChoice > randNumber)
            tvInfo.setText(getResources().getString(R.string.more));
        else if (userChoice < randNumber)
            tvInfo.setText(getResources().getString(R.string.less));
        else {
            tvInfo.setText(getResources().getString(R.string.hit));
            if (record == 0 || numTries < record) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt(rec_str, numTries);
                record = numTries;
                editor.commit();
            }
            btEnter.setEnabled(false);
        }
    }

    public void onClickRestart(View v) {
        setValues();
        tvError.setText("");
        btEnter.setEnabled(true);
    }

    public void onClickReset(View v) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(rec_str, 0);
        editor.commit();
        record = 0;
        tvRecord.setText(getResources().getString(R.string.record) + " -");
    }

    private void setValues() {
        randNumber = rnd.nextInt(99) + 1;
        numTries = 0;
        tvTries.setText(getResources().getString(R.string.number_attempts)
                + Integer.toString(numTries));
        tvInfo.setText(getResources().getString(R.string.try_to_guess));
        tvRecord.setText(getResources().getString(R.string.record));
        tvRecord.append(record == 0 ? " -" : Integer.toString(record));
    }

    private void initializePreferences() {
        if (!sp.contains(rec_str)) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt(rec_str, 0);
            editor.commit();
        }
    }
}
