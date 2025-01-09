package com.cao.tastymatch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.cao.tastymatch.databinding.ActivityReceiptDetailsBinding;

import java.io.File;
import java.util.ArrayList;

public class ReceiptDetailsActivity extends AppCompatActivity {

    private ActivityReceiptDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityReceiptDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        String kitchen = intent.getStringExtra("kitchen");
        String difficulty = intent.getStringExtra("difficulty");
        String time = intent.getStringExtra("time");
        ArrayList<String> ingredients = intent.getStringArrayListExtra("ingredients");
        String imagePath = intent.getStringExtra("imagePath");
        String receipt = intent.getStringExtra("receipt");

        binding.detailsTitle.setText(title);
        binding.detailsDescription.setText(description);
        binding.detailsKitchen.setText(kitchen);
        binding.detailsDifficulty.setText(difficulty);
        binding.detailsTime.setText(time);
        binding.detailsReceipt.setText(receipt);

        if (ingredients != null) {
            binding.detailsIngredients.setText(TextUtils.join(", ", ingredients));
        }

        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            binding.detailsImage.setImageBitmap(bitmap);
        }

        binding.backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = getIntent();
        String imagePath = intent.getStringExtra("imagePath");
        if (imagePath != null) {
            File file = new File(imagePath);
            if (file.exists()) {
                file.delete();
            }
        }
    }
}