package com.example.BloodCare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostViewHolder> {

    private List<Object> postsList;
    private boolean isDonorSearch;

    public PostsAdapter(List<Object> postsList, boolean isDonorSearch) {
        this.postsList = postsList;
        this.isDonorSearch = isDonorSearch;
    }

    public void setDonorSearch(boolean donorSearch) {
        isDonorSearch = donorSearch;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        if (isDonorSearch) {
            DonorPost post = (DonorPost) postsList.get(position);
            holder.bindDonorPost(post);
        } else {
            ReceiverPost post = (ReceiverPost) postsList.get(position);
            holder.bindReceiverPost(post);
        }
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView detailsTextView;
        private TextView locationTextView;
        private TextView phoneTextView;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.post_name);
            detailsTextView = itemView.findViewById(R.id.post_details);
            locationTextView = itemView.findViewById(R.id.post_location);
            phoneTextView = itemView.findViewById(R.id.post_phone);
        }

        public void bindDonorPost(DonorPost post) {
            nameTextView.setText(post.getName());
            detailsTextView.setText("Blood Group: " + post.getBloodGroup());
            locationTextView.setText("Location: " + post.getLocation());
            phoneTextView.setText("Phone: " + post.getPhone());
        }

        public void bindReceiverPost(ReceiverPost post) {
            nameTextView.setText(post.getName());
            detailsTextView.setText("Blood Group: " + post.getBloodGroup() + " | Purpose: " + post.getPurpose());
            locationTextView.setText("Location: " + post.getLocation());
            phoneTextView.setText("Phone: " + post.getPhone());
        }
    }
}