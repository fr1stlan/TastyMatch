package com.cao.tastymatch;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.cao.tastymatch.databinding.FragmentHomeBinding;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ReceiptAdapter adapter;
    private List<DataSnapshot> recipeSnapshots;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        recipeSnapshots = new ArrayList<>();
        binding.homeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.swipeRefreshLayout.setOnRefreshListener(this::loadReceipts);

        loadReceipts();

        binding.homeSearchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() >= 3) {
                    performSearch(newText);
                } else {
                    resetSearch();
                }
                return true;
            }
        });

        return binding.getRoot();
    }

    private void loadReceipts() {
        binding.swipeRefreshLayout.setRefreshing(true);

        FirebaseDatabase.getInstance().getReference("Receipts")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        recipeSnapshots.clear();
                        for (DataSnapshot recipeSnapshot : snapshot.getChildren()) {
                            recipeSnapshots.add(recipeSnapshot);
                        }

                        Collections.shuffle(recipeSnapshots);

                        List<DataSnapshot> limitedSnapshots = recipeSnapshots.subList(0, Math.min(40, recipeSnapshots.size()));

                        if (adapter == null) {
                            adapter = new ReceiptAdapter(limitedSnapshots);
                            binding.homeRecyclerView.setAdapter(adapter);
                        } else {
                            adapter.updateData(limitedSnapshots);
                        }

                        binding.swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        binding.swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void performSearch(String query) {
        FirebaseDatabase.getInstance().getReference("Receipts")
                .orderByChild("title")
                .startAt(query)
                .endAt(query + "\uf8ff")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<DataSnapshot> filteredSnapshots = new ArrayList<>();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            filteredSnapshots.add(data);
                        }

                        adapter.updateData(filteredSnapshots);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void resetSearch() {
        loadReceipts();
    }
}