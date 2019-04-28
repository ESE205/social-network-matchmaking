package com.example.cutetogether.chatFiles;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.cutetogether.R;
import com.example.cutetogether.friendFiles.FriendListAdapter;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.Viewholder> {

    private ArrayList<ChatItem> mChatItems;
    private Context mContext;
    private EventListener mEventListener;

    private static final String TAG = "ChatListAdapter";


    public ChatListAdapter(ArrayList<ChatItem> chatItems, Context context, EventListener eventListener) {
        mChatItems = chatItems;
        mContext = context;
        mEventListener = eventListener;
    }

    @NonNull
    @Override
    public ChatListAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_chat_list_item, viewGroup, false);
        ChatListAdapter.Viewholder holder = new ChatListAdapter.Viewholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListAdapter.Viewholder viewholder, final int i) {
        Log.d(TAG, "onBindViewHolder: ");
        viewholder.mTextView.setText(mChatItems.get(i).getName());

        //get profile images
        StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();
        StorageReference img = mStorageReference.child("img/image.jpg");

        //load profile images
        Glide.with(mContext)
                .load(img)
                .apply(new RequestOptions().override(300,300))
                .into(viewholder.mImageView);
        
        //set friend name text
        viewholder.mTextView.setText(mChatItems.get(i).getName());

        //add listeners
        viewholder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEventListener.chatClicked(mChatItems.get(i).getChatid());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mChatItems.size();
    }

    public interface EventListener {
        public void chatClicked(String chatid);
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        private ImageView mImageView;
        private TextView mTextView;
        private RelativeLayout mRelativeLayout;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.chat_item_image);
            mTextView = itemView.findViewById(R.id.chat_item_name);
            mRelativeLayout = itemView.findViewById(R.id.chat_item_layout);
        }
    }
}
