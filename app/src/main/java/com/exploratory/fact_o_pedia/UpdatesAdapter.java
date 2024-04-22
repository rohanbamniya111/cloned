package com.exploratory.fact_o_pedia;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.exploratory.fact_o_pedia.Models.UpdatesItems;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UpdatesAdapter extends RecyclerView.Adapter<UpdatesAdapter.ViewHolder> {

    private ArrayList<UpdatesItems> list;
    private Context context;
    private UpdateListener listener;
    private CommentListener listener2;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */

    public UpdatesAdapter(ArrayList<UpdatesItems> list,Context context,UpdateListener listener,CommentListener listener2) {
        this.list = list;
        this.context = context;
        this.listener = listener;
        this.listener2 = listener2;
    }

    @NonNull
    @Override
    public UpdatesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.text_row_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpdatesAdapter.ViewHolder holder, int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        UpdatesItems listitem = list.get(holder.getAdapterPosition());
        holder.headline.setText(listitem.getTitle());
        holder.factchecker.setText(listitem.getFactChecker());
        holder.time.setText(listitem.getTime());
        holder.category.setText(listitem.getCategory());
        Picasso.get().load(listitem.getImgLink()).into(holder.imageView);
        //newly added
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnUpdateClicked(list.get(holder.getAdapterPosition()).getLink());
            }
        });
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener2.addComment(list.get(holder.getAdapterPosition()).getTitle());
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView headline;
        private TextView factchecker;
        private TextView time;
        private TextView category;
        private ImageView imageView;
        private CardView cardView;  //newly added
        private Button comment;

        public ViewHolder(@NonNull View itemview) {
            super(itemview);
            // Define click listener for the ViewHolder's View

            imageView = itemview.findViewById(R.id.imageView);
            headline = itemview.findViewById(R.id.headline);
            factchecker = itemview.findViewById(R.id.factchecker);
            time = itemview.findViewById(R.id.time);
            category = itemview.findViewById(R.id.category);
            cardView = itemview.findViewById(R.id.cardView);  //newly added
            comment = itemview.findViewById(R.id.button2);
        }

//        public TextView getTextView() {
//            return textView;
//        }
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView
     */


    // Create new views (invoked by the layout manager)


    // Replace the contents of a view (invoked by the layout manager)


    // Return the size of your dataset (invoked by the layout manager)

}
