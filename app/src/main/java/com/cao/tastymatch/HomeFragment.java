package com.cao.tastymatch;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cao.tastymatch.databinding.FragmentAddBinding;
import com.cao.tastymatch.databinding.FragmentHomeBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    DatabaseReference ReceiptsRef;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ReceiptsRef = FirebaseDatabase.getInstance().getReference().child("Receipts");
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Receipts> options = new FirebaseRecyclerOptions.Builder<Receipts>()
                .setQuery(ReceiptsRef, Receipts.class).build();

        FirebaseRecyclerAdapter<Receipts, ReceiptViewHolder> adapter = new FirebaseRecyclerAdapter<Receipts, ReceiptViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ReceiptViewHolder holder, int position, @NonNull Receipts model) {
                holder.receiptTitle.setText(model.getTitle());
                holder.receiptKitchen.setText(model.getKitchen());
                Picasso.get().load(model.getImage()).into(holder.receiptImage);
            }

            @NonNull
            @Override
            public ReceiptViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}