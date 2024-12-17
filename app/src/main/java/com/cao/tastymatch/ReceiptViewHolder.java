package com.cao.tastymatch;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ReceiptViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView receiptImage;
    public TextView receiptTitle, receiptKitchen;
    public ItemClickListener listener;

    public ReceiptViewHolder(View itemView) {
        super(itemView);

        receiptImage = itemView.findViewById(R.id.receiptImage);
        receiptTitle = itemView.findViewById(R.id.receiptTitle);
        receiptKitchen = itemView.findViewById(R.id.receiptKitchen);
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view, getAbsoluteAdapterPosition(), false);
    }
}
