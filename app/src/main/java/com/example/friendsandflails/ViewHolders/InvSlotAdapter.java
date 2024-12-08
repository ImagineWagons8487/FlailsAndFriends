package edu.csumb.flailsandfriends.ViewHolders;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import edu.csumb.flailsandfriends.R;
import edu.csumb.flailsandfriends.entities.Equipment;

public class InvSlotAdapter extends RecyclerView.Adapter<edu.csumb.flailsandfriends.ViewHolders.InvSlotAdapter.ViewHolder> {

    private int itemCount;

    public InvSlotAdapter(int itemCount) {
        this.itemCount = itemCount;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageButton imageButton;
        public ViewHolder(View view) {
            super(view);
            imageButton = view.findViewById(R.id.invSlotRecyclerItemImageButton);
        }
    }

    @Override
    public edu.csumb.flailsandfriends.ViewHolders.InvSlotAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inv_slot_recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int pos) {
        // Get the screen width
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        int numberOfColumns = 5;
        // Calculate the size of each item
        int itemSize = screenWidth / numberOfColumns;

        // Set the height of the item to match its width
        holder.imageButton.getLayoutParams().height = itemSize;
        holder.imageButton.getLayoutParams().width = itemSize;

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display a Toast message
                Toast.makeText(v.getContext(), "Button " + holder.getAdapterPosition() + " clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public int getItemCount() {
        return itemCount;
    }
}

