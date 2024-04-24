package com.explo.fact_o_pedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.explo.fact_o_pedia.Models.Comments;
import com.explo.fact_o_pedia.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {
    String claim_text;
    String search_query;
    RecyclerView recview;
    ArrayList<Comments> comments;
    FirebaseFirestore db;
    AdminAdapter adapter;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        getSupportActionBar().hide();

        recview = (RecyclerView) findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));
        comments = new ArrayList<Comments>();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Fetching comments...");
        progressDialog.show();
        claim_text = "";

        Bundle bundle = getIntent().getExtras();
        claim_text = bundle.getString("claim_text");
        search_query = bundle.getString("search_query");

        db = FirebaseFirestore.getInstance();
        db.collection("data").document(claim_text.toLowerCase().trim()).collection("comments")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        comments.clear();

                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d:list){
                            Comments obj = d.toObject(Comments.class);
                            if(obj.getChecked().equals("0")){
                                comments.add(obj);
                            }
                        }
                        if(comments.size()==0){
                            Toast.makeText(AdminActivity.this, "No comments to be checked !!", Toast.LENGTH_SHORT).show();
                        }
                        adapter = new AdminAdapter(comments,AdminActivity.this,AdminActivity.this::add,AdminActivity.this::delete);
                        recview.setAdapter(adapter);
                        progressDialog.dismiss();
                    }
                });
    }
    public void add(String name,String comment){
        db = FirebaseFirestore.getInstance();
        Bundle bundle = getIntent().getExtras();
        claim_text = bundle.getString("claim_text");
        db.collection("data").document(claim_text.toLowerCase().trim())
                .collection("comments").document(name+comment)
                .update("checked","1")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(AdminActivity.this, "This comment is added..", Toast.LENGTH_SHORT).show();
                        recreate();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AdminActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void delete(String name,String comment){
        db.collection("data").document(claim_text.toLowerCase().trim())
                .collection("comments").document(name+comment)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(AdminActivity.this, "deleted", Toast.LENGTH_SHORT).show();
                        recreate();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AdminActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}