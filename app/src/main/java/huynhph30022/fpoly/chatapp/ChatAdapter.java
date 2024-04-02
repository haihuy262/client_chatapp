package huynhph30022.fpoly.chatapp;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_SELF = 1;
    private static final int ITEM_OTHER = 2;

    private final AsyncListDiffer<Chat> differ;

    public ChatAdapter() {
        DiffUtil.ItemCallback<Chat> diffcallback = new DiffUtil.ItemCallback<Chat>() {
            @Override
            public boolean areItemsTheSame(@NonNull Chat oldItem, @NonNull Chat newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @SuppressLint("DiffUtilEquals")
            @Override
            public boolean areContentsTheSame(@NonNull Chat oldItem, @NonNull Chat newItem) {
                return oldItem.equals(newItem);
            }
        };

        differ = new AsyncListDiffer<>(this, diffcallback);
    }

    public void submitChat(List<Chat> chats) {
        differ.submitList(chats);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (viewType == ITEM_SELF) {
            View itemView = layoutInflater.inflate(R.layout.item_chat_self, parent, false);
            return new SelfChatItemViewHolder(itemView);
        } else {
            View itemView = layoutInflater.inflate(R.layout.item_chat_other, parent, false);
            return new OtherChatItemViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Chat chat = differ.getCurrentList().get(position);
        if (chat.isSelf()) {
            ((SelfChatItemViewHolder) holder).bind(chat);
        } else {
            ((OtherChatItemViewHolder) holder).bind(chat);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Chat chat = differ.getCurrentList().get(position);
        return chat.isSelf() ? ITEM_SELF : ITEM_OTHER;
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }

    public static class OtherChatItemViewHolder extends RecyclerView.ViewHolder {
        TextView name, msg;

        public OtherChatItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            msg = itemView.findViewById(R.id.msg);
        }

        void bind(Chat chat) {
            name.setText(chat.getUsername());
            msg.setText(chat.getText());
        }
    }

    public static class SelfChatItemViewHolder extends RecyclerView.ViewHolder {
        TextView name, msg;

        public SelfChatItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            msg = itemView.findViewById(R.id.msg);
        }

        void bind(Chat chat) {
            name.setText("You");
            msg.setText(chat.getText());
        }
    }
}
