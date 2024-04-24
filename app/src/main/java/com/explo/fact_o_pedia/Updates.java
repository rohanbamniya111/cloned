package com.explo.fact_o_pedia;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;


import com.explo.fact_o_pedia.Models.UpdatesItems;
import com.explo.fact_o_pedia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;


public class Updates extends AppCompatActivity{
    private UpdatesAdapter adapter;//
    private final ArrayList<UpdatesItems> listClasses = new ArrayList<>();//
    private final ArrayList<String> imglinks = new ArrayList<>();//
    private final ArrayList<String> doc3_imglinks = new ArrayList<>();
    private final ArrayList<String> doc2_imglinks = new ArrayList<>();
    private final ArrayList<String> doc2_times = new ArrayList<>();
    private final ArrayList<String> doc3_times = new ArrayList<>();

    private int turn=5;
    private int iterations = 0;
    private int count=0;
    private ProgressBar progressBar;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updates);
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        turn = bundle.getInt("turn");
        progressBar = findViewById(R.id.progressBar);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UpdatesAdapter(listClasses,this,this::OnUpdateClicked,this::addComment);
        recyclerView.setAdapter(adapter);
        description_extract de = new description_extract();
        de.execute();
        //new DescriptionExtractTask().execute;
    }
    private class description_extract extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            progressBar.startAnimation(AnimationUtils.loadAnimation(Updates.this, android.R.anim.fade_in));
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void onPostExecute(Void aVoid){
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            progressBar.startAnimation(AnimationUtils.loadAnimation(Updates.this, android.R.anim.fade_out));
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onCancelled(){
            super.onCancelled();
        }

        @Override
        protected Void doInBackground(Void... voids){
            Document doc=null;
            Document doc2 = null;
            Document doc3 = null;
            Document doc4 = null;
            try{
                if(turn==1){
                    doc = Jsoup.connect("https://www.hindustantimes.com/topic/fact-check/news").get();
                }
                if(turn==2){
                    doc2 = Jsoup.connect("https://timesofindia.indiatimes.com/times-fact-check").get();
                }
                if(turn==3){
                    doc3 = Jsoup.connect("https://www.thehindu.com/topic/fact-check/").get();
                }
                if(turn==4){
                    doc4 = Jsoup.connect("https://indianexpress.com/about/express-fact-check/").get();
                }

            } catch (IOException e){
                e.printStackTrace();
            }
            Elements headlines = null;
            Elements times=null;
            Elements categories=null;
            Elements doc2_headlines=null;
            Elements doc3_headlines=null;
            Elements doc4_headlines=null;
            if(turn==1){
                assert doc != null;
                headlines = doc.getElementsByClass("hdg3");
                iterations = headlines.size();
                times = doc.getElementsByClass("dateTime");
                categories = doc.getElementsByClass("secName");
                for(int i=0;i<headlines.size();i++){
                    String link = headlines.get(i).child(0).attr("href");
                    link = "https://www.hindustantimes.com"+link;
                    Document doc1=null;
                    try{
                        doc = Jsoup.connect(link).get();

                    } catch (IOException e){
                        e.printStackTrace();
                    }
                    Elements images = doc.getElementsByClass("storyParagraphFigure");
                    imglinks.add(images.get(0).child(1).child(0).child(0).child(1).attr("src"));
                }
            }
            if(turn==2){
                assert doc2 != null;
                doc2_headlines = doc2.getElementsByClass("w_tle");
                iterations = doc2_headlines.size();
                for(int i=0;i<doc2_headlines.size();i++){
                    String link = doc2_headlines.get(i).child(0).attr("href");
                    link = "https://timesofindia.indiatimes.com"+link;
                    Document doc1=null;
                    try{
                        doc1 = Jsoup.connect(link).get();

                    } catch (IOException e){
                        e.printStackTrace();
                    }
                    assert doc1 != null;
                    Elements images = doc1.getElementsByClass("wJnIp");
                    Elements times2 = doc1.getElementsByClass("xf8Pm");

                    doc2_times.add(times2.get(0).child(0).text());
                    doc2_imglinks.add(images.get(0).child(0).attr("src"));
                }
            }
            if (turn == 3) {
                assert doc3 != null;
                doc3_headlines = doc3.getElementsByClass("title");
                if (doc3_headlines.size() > 0) {
                    iterations = doc3_headlines.size();
                    for (int i = 0; i < iterations; i++) {
                        String link = doc3_headlines.get(i).child(0).attr("href");
                        if (link.equals("")) {
                            count++;
                            continue;
                        }
                        Document doc1 = null;
                        try {
                            doc1 = Jsoup.connect(link).get();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (doc1 != null) {
                            Elements images = doc1.getElementsByClass("lead-img").prev().prev().prev();
                            Elements times2 = doc1.getElementsByClass("publish-time");
                            if (times2.size() > 0) {
                                doc3_times.add(times2.get(0).text());
                            }
                            if (images.size() == 0) {
                                doc3_imglinks.add("https://www.thermaxglobal.com/wp-content/uploads/2020/05/image-not-found.jpg");
                            } else {
                                doc3_imglinks.add(images.get(0).attr("srcset"));
                            }
                        }
                    }
                    iterations -= count;
                }
            }


            if(turn==4){
                assert doc4 != null;
                doc4_headlines = doc4.getElementsByClass("about-thumb");
                iterations = doc4_headlines.size();
            }


            Elements finalHeadlines = headlines;
            Elements finalTimes = times;
            Elements finalCategories = categories;
            Elements finalDoc2_headlines = doc2_headlines;
            Elements finalDoc3_headlines = doc3_headlines;
            Elements finalDoc4_headlines = doc4_headlines;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    for(int i=0;i<iterations;i++){
                        String headline;
                        String image;
                        String factchecker;
                        String time;
                        String category;
                        String link;
                        if(turn==1){
                            headline = finalHeadlines.get(i).child(0).text();
                            image = imglinks.get(i);
                            factchecker = "Hindustan Times";
                            time = finalTimes.get(i).text();
                            category = finalCategories.get(i).child(0).text();
                            link = finalHeadlines.get(i).child(0).attr("href");
                            link = "https://www.hindustantimes.com"+link;
                            listClasses.add(new UpdatesItems(headline,image,factchecker,time,category,link));
                        }
                        if(turn==2){
                            headline = finalDoc2_headlines.get(i).child(0).attr("title");
                            image = doc2_imglinks.get(i);
                            factchecker = "The Times of India";
                            time = doc2_times.get(i);
                            category="";
                            link = finalDoc2_headlines.get(i).child(0).attr("href");
                            link = "https://timesofindia.indiatimes.com"+link;
                            listClasses.add(new UpdatesItems(headline,image,factchecker,time,category,link));
                        }
                        if(turn==3){
                            headline = finalDoc3_headlines.get(i).child(0).text();
                            image = doc3_imglinks.get(i);
                            factchecker = "The Hindu";
                            time = doc3_times.get(i);
                            category="";
                            link = finalDoc3_headlines.get(i).child(0).attr("href");
                            link = "https://www.thehindu.com"+link;
                            listClasses.add(new UpdatesItems(headline,image,factchecker,time,category,link));
                        }
                        if(turn==4){
                            image = finalDoc4_headlines.get(i).child(0).child(0).attr("srcset");
                            headline = finalDoc4_headlines.get(i).nextElementSibling().child(0).text();
                            factchecker = "The Indian Express";
                            time = finalDoc4_headlines.get(i).nextElementSibling().nextElementSibling().text();
                            category = "";
                            link = finalDoc4_headlines.get(i).nextElementSibling().child(0).attr("href");
                            // link = "https://indianexpress.com"+link;
                            listClasses.add(new UpdatesItems(headline,image,factchecker,time,category,link));
                        }
                    }
                }

            });


            return null;
        }
    }

    public void OnUpdateClicked(String updateLink) {
        Intent intent = new Intent(Updates.this, UpdateDetailsActivity.class);
        intent.putExtra("data", updateLink);
        startActivity(intent);
    }
    public void addComment(String claim_text){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser==null){
            Intent intent = new Intent(Updates.this, UserSection.class);
            Bundle bundle = new Bundle();
            bundle.putString("claim_text", claim_text);
            bundle.putString("search_query", "query");
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else if(Objects.equals(currentUser.getEmail(), "mjmanas54@gmail.com")){
            Intent intent = new Intent(Updates.this,AdminActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("claim_text", claim_text);
            bundle.putString("search_query", "query");
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(Updates.this, UserSection.class);
            Bundle bundle = new Bundle();
            bundle.putString("claim_text", claim_text);
            bundle.putString("search_query", "query");
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }
}