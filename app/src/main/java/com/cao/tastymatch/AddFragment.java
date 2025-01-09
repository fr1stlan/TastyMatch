package com.cao.tastymatch;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cao.tastymatch.databinding.FragmentAddBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddFragment extends Fragment {

    private FragmentAddBinding binding;
    private Uri filePath;
    private boolean isImageUploaded;
    private String base64Image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddBinding.inflate(inflater, container, false);

        binding.addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        binding.btnAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newIngredient = binding.etNewIngredient.getText().toString().trim();
                if (!newIngredient.isEmpty()) {
                    addChip(newIngredient);
                    binding.etNewIngredient.setText("");
                }
            }
        });

        binding.addSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.addEditTitle.getText().toString().isEmpty() || binding.addDescription.getText().toString().isEmpty() || binding.addEditTime.getText().toString().isEmpty()
                        || binding.addReceiptEt.getText().toString().isEmpty() || isImageUploaded == false || binding.chipGroupIngredients.getChildCount() == 0 || binding.addSpinnerKitchen.getSelectedItemPosition() == 0
                        || binding.addSpinnerDifficult.getSelectedItemPosition() == 0) {
                    Toast.makeText(getContext(), "Поля не могут быть пустыми", Toast.LENGTH_SHORT).show();
                } else {
                    addNewReceipt(binding.addEditTitle.getText().toString(), binding.addDescription.getText().toString(), binding.addSpinnerKitchen.getSelectedItem().toString(),
                            binding.addSpinnerDifficult.getSelectedItem().toString(), binding.addEditTime.getText().toString(), binding.addReceiptEt.getText().toString(), base64Image,
                            getIngredients(binding.chipGroupIngredients));
                }
            }
        });

        binding.addResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.addImage.setImageResource(R.drawable.baseline_image_24);
                binding.addEditTitle.setText("");
                binding.addDescription.setText("");
                binding.addSpinnerKitchen.setSelection(0);
                binding.addEditTime.setText("");
                binding.addSpinnerDifficult.setSelection(0);
                binding.chipGroupIngredients.removeAllViews();
                binding.etNewIngredient.setText("");
                binding.addReceiptEt.setText("");
            }
        });

        return binding.getRoot();
    }

    private List<String> getIngredients(ChipGroup chipGroup) {
        List<String> ingredients = new ArrayList<>();

        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            ingredients.add(chip.getText().toString());
        }

        return ingredients;
    }

    private void addChip(String ingredient) {
        Chip chip = new Chip(requireContext());
        chip.setText(ingredient);
        chip.setCloseIconVisible(true);
        chip.setOnCloseIconClickListener(v -> binding.chipGroupIngredients.removeView(chip));
        binding.chipGroupIngredients.addView(chip);
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        pickImageActivityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> pickImageActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null && result.getData().getData() != null) {
                        filePath = result.getData().getData();

                        try {
                            Bitmap bitmap = MediaStore.Images.Media
                                    .getBitmap(
                                            requireContext().getContentResolver(),
                                            filePath
                                    );
                            binding.addImage.setImageBitmap(bitmap);

                            base64Image = ImageUtils.bitmapToBase64(bitmap);

                            isImageUploaded = true;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    private void addNewReceipt(String title, String description, String kitchen, String difficulty, String time, String receipt, String image, List<String> ingredients) {
        DatabaseReference receiptsRef = FirebaseDatabase.getInstance().getReference("Receipts");

        String receiptId = receiptsRef.push().getKey();

        if (receiptId != null) {
            Map<String, Object> receiptData = new HashMap<>();
            receiptData.put("title", title);
            receiptData.put("image", image);
            receiptData.put("description", description);
            receiptData.put("kitchen", kitchen);
            receiptData.put("difficulty", difficulty);
            receiptData.put("time", time);
            receiptData.put("receipt", receipt);
            receiptData.put("ingredients", ingredients);

            receiptsRef.child(receiptId).setValue(receiptData).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Рецепт успешно сохранён!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Произошла ошибка при сохранении рецепта", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}