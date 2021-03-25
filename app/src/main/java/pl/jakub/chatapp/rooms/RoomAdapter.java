package pl.jakub.chatapp.rooms;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pl.jakub.chatapp.R;

/**
 * Adapter for the recycler view.
 *
 * @author Jakub Zelmanowicz
 */
public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    /**
     * Represents the UI part of the adapter.
     * How should the single room card look like.
     */
    public static class RoomViewHolder extends RecyclerView.ViewHolder {

        private final CardView roomCardView;
        private final TextView roomNameTextView;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            roomCardView = itemView.findViewById(R.id.roomCardView);
            roomNameTextView = itemView.findViewById(R.id.roomNameTextView);
        }

        public CardView getRoomCardView() {
            return roomCardView;
        }

        public TextView getRoomNameTextView() {
            return roomNameTextView;
        }
    }

    /**
     * Room selection event interface.
     */
    public interface OnRoomSelect {
        void onSelect(Room room);
    }

    /**
     * List of all the rooms in the adapter.
     */
    private final List<Room> rooms;

    /**
     * Room select action.
     */
    private OnRoomSelect onRoomSelect;

    public RoomAdapter(List<Room> rooms) {
        this.rooms = rooms;
    }

    public void setOnRoomSelect(OnRoomSelect onRoomSelect) {
        this.onRoomSelect = onRoomSelect;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = rooms.get(position);
        holder.getRoomCardView().setOnClickListener( c -> onRoomSelect.onSelect(room) );
        holder.getRoomNameTextView().setText(room.getName());
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

}
