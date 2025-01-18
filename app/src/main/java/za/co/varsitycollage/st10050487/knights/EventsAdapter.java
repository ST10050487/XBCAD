package za.co.varsitycollage.st10050487.knights;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventViewHolder> {
    private List<EventModel> events;
    private Context context;

    public EventsAdapter(Context context, List<EventModel> events) {
        this.context = context;
        this.events = events;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_upcoming_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        EventModel event = events.get(position);
        holder.eventName.setText(event.getEventName());
        if (event.getEventPicture() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(event.getEventPicture(), 0, event.getEventPicture().length);
            holder.eventImage.setImageBitmap(bitmap);
        } else {
            holder.eventImage.setImageResource(R.drawable.ic_no_image_icon);
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        ImageView eventImage;
        TextView eventName;

        public EventViewHolder(View itemView) {
            super(itemView);
            eventImage = itemView.findViewById(R.id.event_image);
            eventName = itemView.findViewById(R.id.event_name);
        }
    }
}
