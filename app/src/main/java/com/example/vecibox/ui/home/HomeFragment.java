package com.example.vecibox.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vecibox.R;
import com.example.vecibox.adapter.negocioAdapter;
import com.example.vecibox.databinding.FragmentHomeBinding;
import com.example.vecibox.model.Negocio;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private RecyclerView mRecyclerView;
    private negocioAdapter mAdapter;
    private GoogleMap mMap;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mRecyclerView = root.findViewById(R.id.recycleViewHome);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        Query query = FirebaseFirestore.getInstance().collection("Negocios");

        FirestoreRecyclerOptions<Negocio> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Negocio>().setQuery(query, Negocio.class).build();

        mAdapter = new negocioAdapter(firestoreRecyclerOptions, requireContext());
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return root;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        float zoomLevel = 13.0f;

        LatLng elDominicano = new LatLng(-36.7978963, -73.0973206);
        mMap.addMarker(new MarkerOptions().position(elDominicano).title("El Dominicano"));

        LatLng otroNegocio = new LatLng(-36.7842168, -73.0843503);
        mMap.addMarker(new MarkerOptions().position(otroNegocio).title("Tienda y abarrotes Elvira"));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                showPopup(marker);
                return true;
            }
        });
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(elDominicano, zoomLevel));
    }

    private void showPopup(Marker marker) {
        LayoutInflater inflater = (LayoutInflater) requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_layout_default, null);

        if ("El Dominicano".equals(marker.getTitle())) {
            popupView = inflater.inflate(R.layout.popup_layout, null);
        } else if ("Tienda y abarrotes Elvira".equals(marker.getTitle())) {
            popupView = inflater.inflate(R.layout.layout_negocio, null);
        }

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        TextView textView = popupView.findViewById(R.id.popup_text);
        textView.setText(marker.getTitle());

        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        popupView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(requireContext(), "Ventana emergente clicada", Toast.LENGTH_SHORT).show();

                popupWindow.dismiss();
            }
        });
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



