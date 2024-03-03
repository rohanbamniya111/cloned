package com.exploratory.fact_o_pedia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.exploratory.fact_o_pedia.Models.Claims;
import com.exploratory.fact_o_pedia.Models.FactApiResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.User;

import java.util.List;

public class FactListActivity extends AppCompatActivity implements SelectListener{
    public static String q;
    RecyclerView recyclerView;
    CustomAdaptor adaptor;
    ProgressDialog dialog;
    String query;
    TextView textView;
    String item;
    String lang;
    String claim_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_fact_list);
        getSupportActionBar().hide();

        textView = findViewById(R.id.search_query);

        dialog = new ProgressDialog(this);
        dialog.setTitle("Fetching fact-checks...");
        dialog.show();

        Bundle bundle = getIntent().getExtras();
        query = bundle.getString("query");
        item = bundle.getString("item");

        if(item.equals("English")){
            lang = "en-US";
        }
        else if(item.equals("Hindi")){
            lang = "hi-IN";
        }
        else{
            lang = "";
        }

        textView.setText(query);

        RequestManager manager = new RequestManager(this);
        manager.getClaimsList(listener, query, lang);
    }

    private void emptyDataSet() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_factcheck_request);

        Button startBtn = (Button) findViewById(R.id.sendEmail);
        startBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sendEmail(query);
            }
        });
    }

    protected void sendEmail(String query) {
        Log.i("Send email", "");
        String[] TO = {"timesfactcheck@timesinternet.in", "contact@altnews.in", "factcheck@intoday.com", "truthometer@politifact.com", "appeals@boomlive.in", "respond@factchecker.in", "editor@thequint.com", "contact@vishvasnews.com", "editor@newsmobile.in", "factcheck@thip.in", "factcheck@factly.in", "editor@newsmeter.in", "Editor@FactCheck.org", "Editor@Mediabiasfactcheck.com", "support@apnews.com"};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "❗❗ Urgent: FactCheck Request ❗❗");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "I have received this news from one of my circles and doubt whether it is correct or not. " +
                "Can you please get this fact-checked: \n\n\"" + query + "\"\n\nThank you!");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(FactListActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private final OnFetchDataListener<FactApiResponse> listener = new OnFetchDataListener<FactApiResponse>() {
        @Override
        public void onFetchData(List<Claims> claims, String message) {
            if(claims == null){
                emptyDataSet();
                dialog.dismiss();
            }
            else{
                showFacts(claims);
                dialog.dismiss();
            }
        }

        @Override
        public void onError(String message) {
            Toast.makeText(FactListActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    };

    private void showFacts(List<Claims> claims) {
        recyclerView = findViewById(R.id.recycler_main);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        adaptor = new CustomAdaptor(this, claims, this);
        recyclerView.setAdapter(adaptor);
    }

    @Override
    public void OnClaimClicked(Claims claims) {
        Intent intent = new Intent(FactListActivity.this, DetailsActivity.class);
        intent.putExtra("data", claims);
        startActivity(intent);
    }

    @Override
    public void OnButtonClicked(Claims claims) {
        claim_text = claims.getText();
        Log.d("claim_text", claim_text);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser==null){
            Intent intent = new Intent(FactListActivity.this, UserSection.class);
            Bundle bundle = new Bundle();
            bundle.putString("claim_text", claim_text);
            bundle.putString("search_query", "query");
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else if(currentUser.getEmail().equals("mjmanas54@gmail.com")){
            Intent intent = new Intent(FactListActivity.this,AdminActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("claim_text", claim_text);
            bundle.putString("search_query", "query");
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(FactListActivity.this, UserSection.class);
            Bundle bundle = new Bundle();
            bundle.putString("claim_text", claim_text);
            bundle.putString("search_query", "query");
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}