package com.explo.fact_o_pedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.explo.fact_o_pedia.Models.Comments;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserSection extends AppCompatActivity {
    private AlertDialog dialog;
    private EditText cmnt;
    private Button post, cancel;
    FirebaseFirestore dbroot;
    String claim_text;
    String search_query;
    TextView textView;
    RecyclerView recview;
    ArrayList<Comments> comments;
    FirebaseFirestore db;
    CommentAdapter adapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_section);
        getSupportActionBar().hide();

        recview = (RecyclerView) findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));
        comments = new ArrayList<>();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Fetching comments...");
        progressDialog.show();

        Bundle bundle = getIntent().getExtras();
        claim_text = bundle.getString("claim_text");
        search_query = bundle.getString("search_query");

        textView = findViewById(R.id.search_query);
        textView.setText(search_query);

        db = FirebaseFirestore.getInstance();
        db.collection("data").document(claim_text.toLowerCase().trim()).collection("comments")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        comments.clear();

                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        if(list.isEmpty()){
                            Toast.makeText(UserSection.this, "No comments available!", Toast.LENGTH_SHORT).show();
                        }
                        for(DocumentSnapshot d:list){
                            Comments obj = d.toObject(Comments.class);
                            if(obj.getChecked().equals("1")){
                                Log.d("ans",obj.getChecked());
                                comments.add(obj);
                            }
                        }
                        adapter = new CommentAdapter(comments);
                        recview.setAdapter(adapter);
                        progressDialog.dismiss();
                    }
                });
    }

    public void add(View view){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser!=null){
            createNewDialogBuilder();
        }
        else{
            //Intent intent = new Intent(UserSection.);
            //startActivity(intent);
        }
    }

    public void createNewDialogBuilder() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View commentPopupView = getLayoutInflater().inflate(R.layout.popup, null);
        TextView name = (TextView) commentPopupView.findViewById(R.id.name_id);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        name.setText(user.getDisplayName());
        cmnt = (EditText) commentPopupView.findViewById(R.id.comment);
        post = (Button) commentPopupView.findViewById(R.id.saveButton);
        cancel = (Button) commentPopupView.findViewById(R.id.cancelButton);
        dbroot = FirebaseFirestore.getInstance();
        dialogBuilder.setView(commentPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> commentData = new HashMap<>();
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                commentData.put("name", currentUser.getDisplayName());
                commentData.put("email", currentUser.getEmail());
                commentData.put("comment", cmnt.getText().toString().trim());
                commentData.put("checked","0");

                dbroot.collection("data").document(claim_text.toLowerCase().trim())
                        .collection("comments").document(commentData.get("name")+commentData.get("comment"))
                        .set(commentData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void onSuccess(Void unused) {

                                Toast.makeText(UserSection.this, "Comment posted! It will be shown as admin permits!", Toast.LENGTH_SHORT).show();


                                dialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UserSection.this, "Failed!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}