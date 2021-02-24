package com.iclaim.adapter;



import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import static com.google.common.io.ByteStreams.copy;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    private List<Chat> chatList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView sendTextView, sendTimeTextView;
        LinearLayout linearLayout, parentLayout;
        ImageView sentImage;

        public MyViewHolder(View view) {
            super(view);

            sendTextView = view.findViewById(R.id.sentMsg);
            sentImage = view.findViewById(R.id.leximageView);
            sendTimeTextView = view.findViewById(R.id.sentTime);
            linearLayout = view.findViewById(R.id.linear_layout);
            parentLayout = view.findViewById(R.id.parent_layout);
        }
    }


    public ChatAdapter(List<Chat> chatList) {
        this.chatList = chatList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_chat_sent_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Chat chat = chatList.get(position);
         //TODO
        }
       else {
           //TODO

            }

        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }
}