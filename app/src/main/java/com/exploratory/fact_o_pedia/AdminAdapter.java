package com.exploratory.fact_o_pedia;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.exploratory.fact_o_pedia.Models.Comments;

import java.util.ArrayList;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.ViewHolder> {

    private ArrayList<Comments> list;
    private Context context;
    private AddListener listener;
    private DeleteListener listener2;


    public AdminAdapter(ArrayList<Comments> list,Context context,AddListener listener,DeleteListener listener2) {
        this.list = list;
        this.context = context;
        this.listener = listener;     //
        this.listener2 = listener2;   //
    }

    @NonNull
    @Override
    public AdminAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_row_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminAdapter.ViewHolder holder, int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Comments listitem = list.get(position);
        holder.name.setText(listitem.getName());
        holder.comment.setText(listitem.getComment());
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.add(listitem.getName(),listitem.getComment());
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener2.delete(listitem.getName(),listitem.getComment());
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView comment;
        private Button add;
        private Button delete;

        public ViewHolder(@NonNull View itemview) {
            super(itemview);

            name = itemview.findViewById(R.id.name_to_admin);
            comment = itemview.findViewById(R.id.comment_to_admin);
            add = itemview.findViewById(R.id.add);
            delete = itemview.findViewById(R.id.delete);
        }

    }


}

