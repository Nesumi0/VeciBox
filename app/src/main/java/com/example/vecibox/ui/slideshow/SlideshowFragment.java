package com.example.vecibox.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vecibox.R;
import com.example.vecibox.adapter.negocioAdapter;
import com.example.vecibox.databinding.FragmentSlideshowBinding;
import com.example.vecibox.model.Negocio;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    private RecyclerView mRecyclerView;
    private negocioAdapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mRecyclerView = root.findViewById(R.id.recycleViewSingle);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        Query query = FirebaseFirestore.getInstance().collection("Negocios");

        FirestoreRecyclerOptions<Negocio> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Negocio>().setQuery(query, Negocio.class).build();

        mAdapter = new negocioAdapter(firestoreRecyclerOptions, requireContext());
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

