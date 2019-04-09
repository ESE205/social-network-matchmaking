package com.example.cutetogether.chatFiles;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.cutetogether.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;

//source https://blog.sendbird.com/android-chat-tutorial-building-a-messaging-ui
public class ChatAdapter extends RecyclerView.Adapter {
    private static final String TAG = "ChatAdapter";
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private Context mContext;
    private ArrayList<MessageItem> mMessageItems;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();


    public ChatAdapter(ArrayList<MessageItem> messageItems, Context context) {
        Log.d(TAG, "ChatAdapter: ");
        mContext = context;
        mMessageItems = messageItems;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG, "onCreateViewHolder: ");
        View view;
        if (i == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.layout_chat_message_sent, viewGroup, false);
            return new SentMessageViewholder(view);
        } else if (i == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.layout_chat_message_recieved, viewGroup, false);
            return new RecievedMessageViewholder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Log.d(TAG, "onBindViewHolder: ");
        MessageItem messageItem = mMessageItems.get(i);

        switch (viewHolder.getItemViewType()){
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageViewholder) viewHolder).bind(messageItem);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((RecievedMessageViewholder) viewHolder).bind(messageItem);
        }
    }

    @Override
    public int getItemViewType(int i) {
        Log.d(TAG, "getItemViewType: ");
        MessageItem message = mMessageItems.get(i);

        if (message.getId().equals(user.getUid())) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: ");
        return mMessageItems.size();
    }

    public interface EventListener{

    }

    private class SentMessageViewholder extends RecyclerView.ViewHolder{
        TextView messageBody;
        TextView messageTime;


        public SentMessageViewholder(@NonNull View itemView) {
            super(itemView);
            messageBody = itemView.findViewById(R.id.text_message_body);
            messageTime = itemView.findViewById(R.id.text_message_time);

        }

        void bind(MessageItem messageItem){
            Log.d(TAG, "bind: ");
            messageBody.setText(messageItem.getMessage());
            messageTime.setText(messageItem.getTime());
        }
    }

    private class RecievedMessageViewholder extends RecyclerView.ViewHolder{

        TextView messageBody;
        TextView messageName;
        TextView messageTime;
        ImageView mImageView;

        public RecievedMessageViewholder(@NonNull View itemView) {
            super(itemView);
            messageBody = itemView.findViewById(R.id.text_message_body);
            messageName = itemView.findViewById(R.id.text_message_name);
            messageTime = itemView.findViewById(R.id.text_message_time);
            mImageView = itemView.findViewById(R.id.image_message_profile);
        }

        void bind(MessageItem messageItem){
            Log.d(TAG, "bind: ");
            messageBody.setText(messageItem.getMessage());
            messageName.setText(messageItem.getName());
            messageTime.setText(messageItem.getTime());

            //get profile images
            StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();
            StorageReference img = mStorageReference.child("img/image.jpg");

            //load profile images
            Glide.with(mContext)
                    .load(img)
                    .apply(new RequestOptions().override(300,300))
                    .into(mImageView);
        }
    }
}
