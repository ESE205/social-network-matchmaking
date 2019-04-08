package com.example.cutetogether.matchFiles;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cutetogether.R;

import java.util.ArrayList;

public class MatchListAdapter extends RecyclerView.Adapter {

    private static final String TAG = "MatchListAdapter";
    private ArrayList<String> mMatchNames = new ArrayList<>();
    private ArrayList<String> mMatchIds = new ArrayList<>();
    private Context mContext;
    private EventListener mListener;

    public MatchListAdapter (ArrayList<String> namesList, ArrayList<String> idList, Context context, EventListener listener){
        mMatchNames = namesList;
        mMatchIds = idList;
        mContext = context;
        mListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        ImageView mImage;
        TextView mName;
        RelativeLayout parentLayout;
        ImageButton mAdd;
        ImageButton mDeny;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.match_accept_item_image);
            mName = itemView.findViewById(R.id.match_accept_item_name);
            parentLayout = itemView.findViewById(R.id.match_accept_item_layout);
            mAdd = itemView.findViewById(R.id.match_accept_item_btn);
            mDeny = itemView.findViewById(R.id.match_reject_item_btn);

        }
    }

    public interface EventListener {
        public void matchClicked(String id, String name);
    }
}
