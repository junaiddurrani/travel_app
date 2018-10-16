package com.android.app.travelapp;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class Home extends Fragment {

    View mView;
    private RecyclerView mPostList;
    private DatabaseReference mPostDatabase;
    private LinearLayoutManager llm;

    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_home, container, false);

        mPostList = mView.findViewById(R.id.postList);
        llm = new LinearLayoutManager(getActivity());
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);

        mPostList.setHasFixedSize(true);
        mPostList.setLayoutManager(llm);

        mPostDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");
        mPostDatabase.keepSynced(true);

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Posts, PostViewHolder> adapter = new FirebaseRecyclerAdapter<Posts, PostViewHolder>(
                Posts.class,
                R.layout.travel_single_item,
                PostViewHolder.class,
                mPostDatabase
        ) {
            @Override
            protected void populateViewHolder(PostViewHolder viewHolder, Posts model, int position) {

                viewHolder.setName(model.getName());
                viewHolder.setPlace(model.getPlace());
                viewHolder.setPrice(model.getPrice());
                viewHolder.setImage(model.getImage(), getActivity());

            }
        };
        mPostList.setAdapter(adapter);
    }

    private static class PostViewHolder extends RecyclerView.ViewHolder {

        View mView;
        ImageView imageView;
        TextView name, place, price;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            imageView = mView.findViewById(R.id.postImage);
            name = mView.findViewById(R.id.name);
            place = mView.findViewById(R.id.place);
            price = mView.findViewById(R.id.price);
        }

        public void setName(String n) {
            name.setText(n);
        }

        public void setPlace(String p) {
            place.setText(p);
        }

        public void setPrice(String pr) {
            price.setText("$" + pr);
        }

        public void setImage(String image, Context ctx) {
            Picasso.with(ctx).load(image).into(imageView);
        }

    }
}
