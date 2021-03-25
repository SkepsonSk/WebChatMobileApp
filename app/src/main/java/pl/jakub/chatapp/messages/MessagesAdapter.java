package pl.jakub.chatapp.messages;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pl.jakub.chatapp.R;

/**
 * Adapter for the recycler view.
 *
 * @author Jakub Zelmanowicz
 */
public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {

    private final List<Message> messages;

    /**
     * Represents the UI part of the adapter.
     * How should the single message card look like.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView senderTextView;
        private final TextView contentTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            senderTextView = itemView.findViewById(R.id.messageSender);
            contentTextView = itemView.findViewById(R.id.messageContent);
        }

        public TextView getSenderTextView() {
            return senderTextView;
        }

        public TextView getContentTextView() {
            return contentTextView;
        }
    }

    public MessagesAdapter() {
        messages = new ArrayList<>();

        // Default encourage message sent every time a user joins a chat room.
        messages.add(new Message("Mr. Smith", "Hey! Welcome to the chat!"));
    }

    /**
     * Adds a message to the adapter making
     * it visible to the user.
     *
     * @param sender - sender of the message,
     * @param content - text content of the message.
     */
    public void pushMessage(String sender, String content) {
        Message message = new Message(sender, content);
        messages.add(message);
        notifyItemInserted(messages.size() - 1);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cardview_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Message message = messages.get(position);

        holder.getSenderTextView().setText(message.getSender());
        holder.getContentTextView().setText(message.getContent());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
