package edu.csumb.flailsandfriends.ViewHolders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.recyclerview.widget.RecyclerView;

import edu.csumb.flailsandfriends.R;

public class InvSlotViewHolder extends RecyclerView.ViewHolder{

    private final ImageButton invSlotViewItem;

    private InvSlotViewHolder(View invSlotView){
        super(invSlotView);
        invSlotViewItem = invSlotView.findViewById(R.id.invSlotRecyclerItemImageButton);
    }

    public void bind(int src){ //pass in R.drawable
        invSlotViewItem.setImageResource(src);
    }

    static InvSlotViewHolder create(ViewGroup parent){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inv_slot_recycler_item, parent, false);
        return new InvSlotViewHolder(view);
    }
}
