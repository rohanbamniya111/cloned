package com.exploratory.fact_o_pedia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.exploratory.fact_o_pedia.FactListActivity;
import com.exploratory.fact_o_pedia.LoginActivity;
import com.exploratory.fact_o_pedia.OnboardingActivity;
import com.exploratory.fact_o_pedia.Updates;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    String item = "All Languages";
    String[] items = {"All Languages", "English", "Hindi"};
    AutoCompleteTextView autoCompleteTxt;
    ArrayAdapter<String> adapterItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // Check if onboarding is needed
        SharedPreferences sharedPreferences = getSharedPreferences("onboarding", MODE_PRIVATE);
        boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);

        if (isFirstRun) {
            // Launch onboarding activity
            Intent intent = new Intent(this, OnboardingActivity.class);
            startActivity(intent);
            finish(); // Finish MainActivity to prevent going back
        } else {
            // Proceed to the main activity layout
            setContentView(R.layout.activity_main);

            // Initialize views and set listeners
            editText = findViewById(R.id.editText);
            autoCompleteTxt = findViewById(R.id.auto_complete_txt);
            adapterItems = new ArrayAdapter<>(this, R.layout.list_item, items);
            autoCompleteTxt.setAdapter(adapterItems);
            autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    item = parent.getItemAtPosition(position).toString();
                }
            });
        }
    }

    public void search(View view) {
        Intent intent = new Intent(this, FactListActivity.class);

        String query = editText.getText().toString();
        if (query.length() == 0) {
            Toast.makeText(this, "Enter the query before search", Toast.LENGTH_LONG).show();
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("query", query);
            bundle.putString("item", item);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    // Methods for handling button clicks remain unchanged...
    public void hidustantimesFunc(View view){
        Intent intent = new Intent(this, Updates.class);
        Bundle bundle = new Bundle();
        bundle.putInt("turn",1);
        intent.putExtras(bundle);
        startActivity(intent);
//      toast.makeText(this,"The button is clicked",Toast.LENGTH_LONG).show();
    }
    public void thetimesofindiaFunc(View view){
        Intent intent = new Intent(this, Updates.class);
        Bundle bundle = new Bundle();
        bundle.putInt("turn",2);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void theindianexpressFunc(View view){
        Intent intent = new Intent(this, Updates.class);
        Bundle bundle = new Bundle();
        bundle.putInt("turn",3);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void thehinduFunc(View view){
        Intent intent = new Intent(this, Updates.class);
        Bundle bundle = new Bundle();
        bundle.putInt("turn",4);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void login(View view) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
