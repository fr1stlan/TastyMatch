package com.cao.tastymatch;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cao.tastymatch.databinding.FragmentAddBinding;
import com.cao.tastymatch.databinding.FragmentCatalogBinding;

public class CatalogFragment extends Fragment {

    private FragmentCatalogBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCatalogBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}