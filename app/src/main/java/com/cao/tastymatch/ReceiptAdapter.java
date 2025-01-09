package com.cao.tastymatch;

import static com.cao.tastymatch.ImageUtils.base64ToBitmap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.ReceiptViewHolder> {

    private List<DataSnapshot> receiptSnapshots;

    public ReceiptAdapter(List<DataSnapshot> receiptSnapshots) {
        this.receiptSnapshots = receiptSnapshots;
    }

    public void updateData(List<DataSnapshot> newReceipts) {
        this.receiptSnapshots = newReceipts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReceiptViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_recycler_item, parent, false);
        return new ReceiptViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiptViewHolder holder, int position) {
        DataSnapshot snapshot = receiptSnapshots.get(position);

        String title = snapshot.child("title").getValue(String.class);
        String kitchen = snapshot.child("kitchen").getValue(String.class);
        String difficulty = snapshot.child("difficulty").getValue(String.class);
        String time = snapshot.child("time").getValue(String.class);
        String description = snapshot.child("description").getValue(String.class);
        String base64Image = snapshot.child("image").getValue(String.class);

        holder.title.setText(title);
        holder.kitchen.setText(kitchen);
        holder.difficulty.setText(difficulty);
        holder.time.setText(time);
        holder.description.setText(description);

        if (base64Image != null && !base64Image.isEmpty()) {
            byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            holder.image.setImageBitmap(bitmap);
        } else {
            holder.image.setImageResource(R.drawable.baseline_image_24);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ReceiptDetailsActivity.class);

                Bitmap bitmap = base64ToBitmap(snapshot.child("image").getValue(String.class));
                String imagePath = saveImageLocally(bitmap, view.getContext());

                if (imagePath != null) {
                    intent.putExtra("imagePath", imagePath);
                }

                intent.putExtra("title", snapshot.child("title").getValue(String.class));
                intent.putExtra("description", snapshot.child("description").getValue(String.class));
                intent.putExtra("kitchen", snapshot.child("kitchen").getValue(String.class));
                intent.putExtra("difficulty", snapshot.child("difficulty").getValue(String.class));
                intent.putExtra("time", snapshot.child("time").getValue(String.class));
                intent.putExtra("receipt", snapshot.child("receipt").getValue(String.class));
                intent.putStringArrayListExtra("ingredients", (ArrayList<String>) snapshot.child("ingredients").getValue());

                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return receiptSnapshots.size();
    }

    public static class ReceiptViewHolder extends RecyclerView.ViewHolder {
        TextView title, kitchen, difficulty, time, description;
        ImageView image;

        public ReceiptViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_receiptTitle);
            kitchen = itemView.findViewById(R.id.item_receiptKitchen);
            difficulty = itemView.findViewById(R.id.item_receiptDifficulty);
            time = itemView.findViewById(R.id.item_receiptTime);
            description = itemView.findViewById(R.id.item_receiptDescription);
            image = itemView.findViewById(R.id.item_receiptImage);
        }
    }

    private String saveImageLocally(Bitmap bitmap, Context context) {
        try {
            File tempFile = File.createTempFile("image_", ".png", context.getCacheDir());
            FileOutputStream fos = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            return tempFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
