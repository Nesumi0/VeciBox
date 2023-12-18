package com.example.vecibox.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.vecibox.R;
import com.example.vecibox.credenciales.LoginUserActivity;
import com.example.vecibox.databinding.FragmentGalleryBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class GalleryFragment extends Fragment {
    private FirebaseFirestore bd = FirebaseFirestore.getInstance();
    private FragmentGalleryBinding binding;
    FirebaseAuth mAuth;
    private TextView txt_nombre,txt_gmail;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        txt_nombre = binding.textNombre;
        txt_gmail = binding.textGmail;

        Button btnLogout = root.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(v -> logout());

        obtenerInformacionUsuario();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(requireContext(), LoginUserActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        requireActivity().finish();
    }

    private void obtenerInformacionUsuario() {
        String gmail = mAuth.getCurrentUser().getEmail();
        bd.collection("Usuarios").whereEqualTo("gmail", gmail).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String nombreUser = document.getString("name");
                    String gmailUser = document.getString("gmail");
                    txt_nombre.setText("Nombre: "+nombreUser);
                    txt_gmail.setText("Gmail: "+gmailUser);
                }
            } else {
                Toast.makeText(requireContext(), "Error al obtener información del usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

