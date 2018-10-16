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
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Explore extends Fragment {

    View mView;
    private RecyclerView mPostList;
    private DatabaseReference mPostDatabase, mTourDatabase;
    private LinearLayoutManager llm, llm2, llm3;

    private RecyclerView mOfferList;
    private RecyclerView mDestinationList;

    public Explore() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_explore, container, false);

        // Tour List RecyclerView
        mPostList = mView.findViewById(R.id.postList);
        llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true);
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);

        mPostList.setHasFixedSize(true);
        mPostList.setLayoutManager(llm);

        mPostDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");
        mPostDatabase.keepSynced(true);
        //end

        mTourDatabase = FirebaseDatabase.getInstance().getReference().child("Tours");
        mTourDatabase.keepSynced(true);

        // OfferList RecyclerView
        mOfferList = mView.findViewById(R.id.offerList);

        llm2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true);
        llm2.setReverseLayout(true);
        llm2.setStackFromEnd(true);

        mOfferList.setHasFixedSize(true);
        mOfferList.setLayoutManager(llm2);
        //end

        // OfferList RecyclerView
        mDestinationList = mView.findViewById(R.id.tourList);

        llm3 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true);
        llm3.setReverseLayout(true);
        llm3.setStackFromEnd(true);

        mDestinationList.setHasFixedSize(true);
        mDestinationList.setLayoutManager(llm3);
        //end

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Posts, PostsViewHolder> postAdapter = new FirebaseRecyclerAdapter<Posts, PostsViewHolder>(
                Posts.class,
                R.layout.tour_offer_single_item,
                PostsViewHolder.class,
                mPostDatabase
        ) {
            @Override
            protected void populateViewHolder(PostsViewHolder viewHolder, Posts model, int position) {
                viewHolder.setName(model.getName());
                viewHolder.setPlace(model.getPlace());
                viewHolder.setImage(model.getImage(), getActivity());
            }
        };

        FirebaseRecyclerAdapter<Tours, ToursViewHolder> destinationAdapter = new FirebaseRecyclerAdapter<Tours, ToursViewHolder>(
                Tours.class,
                R.layout.tour_single_item2,
                ToursViewHolder.class,
                mTourDatabase
        ) {
            @Override
            protected void populateViewHolder(ToursViewHolder viewHolder, Tours model, int position) {
                viewHolder.setPlace(model.getPlace());
                viewHolder.setImage(model.getImage(), getActivity());
            }
        };

        mPostList.setAdapter(postAdapter);
        mOfferList.setAdapter(postAdapter);
        mDestinationList.setAdapter(destinationAdapter);

    }

    private static class PostsViewHolder extends RecyclerView.ViewHolder {

        View mView;
        ImageView imageView;
        TextView name, place;

        public PostsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            imageView = mView.findViewById(R.id.postImage);
            name = mView.findViewById(R.id.hotel_name);
            place = mView.findViewById(R.id.place_name);
        }

        public void setName(String n) {
            name.setText(n);
        }

        public void setPlace(String p) {
            place.setText(p);
        }

        public void setImage(String image, Context ctx) {
            Picasso.with(ctx).load(image).into(imageView);
        }
    }

    private static class ToursViewHolder extends RecyclerView.ViewHolder {

        View mView;
        ImageView imageView;
        TextView place;

        public ToursViewHolder(@NonNull View itemView) {
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
