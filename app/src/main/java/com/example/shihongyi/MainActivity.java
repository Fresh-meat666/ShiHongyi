package com.example.shihongyi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    String[] unitArray;
    EditText vLength,vMeter,vYard,vNautical;
    int unit;
    Spinner mSpinner;

    // save the data user inputs
    public void savePreferences(String l, int u) {
        SharedPreferences pref = getSharedPreferences("ShiHongyi", MODE_PRIVATE);
        pref.edit().putString("length", l).commit();
        pref.edit().putInt("unit", u).commit();
    }

    public void loadPreferences() {
        SharedPreferences pref = getSharedPreferences("ShiHongyi", MODE_PRIVATE);
        mSpinner.setSelection(pref.getInt("unit", 0));
        vLength.setText(pref.getString("length", "0"));
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadPreferences();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //spinner
        unitArray = getResources().getStringArray(R.array.units_array);
        mSpinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, unitArray);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0,
                                               View arg1, int arg2, long arg3) {
                        unit = arg0.getSelectedItemPosition();
                        Toast.makeText(getBaseContext(),
                                "You have selected item : " + unitArray[unit], Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });

        //conversion
        vLength = (EditText) findViewById(R.id.length);
        vMeter = (EditText) findViewById(R.id.editMeter);
        vYard = (EditText) findViewById(R.id.editYard);
        vNautical = (EditText) findViewById(R.id.editNautical);
    }

    public void convertLength(View view) {
        String length = vLength.getText().toString();

        savePreferences(length, unit);

        double meter,yard,nautical;
        if(unit==0){
            meter = Double.parseDouble(length);
            yard = 1.09361 * meter;
            nautical = meter / 1852.0;
            vMeter.setText(String.format("%f",meter));
            vYard.setText(String.format("%f",yard));
            vNautical.setText(String.format("%f",nautical));
        }
        else if(unit==1){
            yard = Double.parseDouble(length);
            meter = yard / 1.09361;
            nautical = yard / 2025.37;
            vMeter.setText(String.format("%f",meter));
            vYard.setText(String.format("%f",yard));
            vNautical.setText(String.format("%f",nautical));
        }
        else{
            nautical = Double.parseDouble(length);
            meter = nautical *1852.0;
            yard = nautical *2025.37;
            vMeter.setText(String.format("%f",meter));
            vYard.setText(String.format("%f",yard));
            vNautical.setText(String.format("%f",nautical));
        }
    }


    // --- Option Menu ---
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                openOptionsDialog();
                return true;
            case R.id.menu_exit:
                finish();
                return true;
        }
        return false;
    }

    public void openOptionsDialog() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.menu_about)
                .setMessage(R.string.about_msg)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                            }
                        }).show();
    }

}
