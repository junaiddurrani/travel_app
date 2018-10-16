package com.android.app.travelapp;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;


public class Tour extends Fragment {

    View mView;
    private RecyclerView mPostList;
    private DatabaseReference mTourDatabase;
    private LinearLayoutManager llm;

    public Tour() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_tour, container, false);

        mPostList = mView.findViewById(R.id.postList);
        llm = new LinearLayoutManager(getActivity());
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);

        mPostList.setHasFixedSize(true);
        mPostList.setLayoutManager(llm);

        mTourDatabase = FirebaseDatabase.getInstance().getReference().child("Tours");
        mTourDatabase.keepSynced(true);

        return mView;
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Tours, PostViewHolder> adapter = new FirebaseRecyclerAdapter<Tours, PostViewHolder>(
                Tours.class,
                R.layout.tour_single_item,
                PostViewHolder.class,
                mTourDatabase
        ) {
            @Override
            protected void populateViewHolder(PostViewHolder viewHolder, Tours model, int position) {

                viewHolder.setPlace(model.getPlace());
                viewHolder.setImage(model.getImage(), getActivity());

            }
        };
        mPostList.setAdapter(adapter);
    }

    private static class PostViewHolder extends RecyclerView.ViewHolder {

        View mView;
        ImageView imageView;
        TextView place;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            imageView = mView.findViewById(R.id.tourImage);
            place = mView.findViewById(R.id.place_name);
        }

        public void setPlace(String p) {
            place.setText(p);
        }
        public void setImage(String image, Context ctx) {
            Picasso.with(ctx).load(image).into(imageView);
        }
    }
}
