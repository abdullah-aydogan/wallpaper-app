package com.abdullah.duvarkagitlari;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    private ImageAdapter imageAdapter;
    private DatabaseReference databaseReference;
    private List<Model> images;
    private RecyclerView recyclerView;
    private BottomNavigationView bottomNavigation;

    private String kategori = "Hayvanlar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!isConnected()) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            alertDialog.setIcon(R.drawable.internet);
            alertDialog.setTitle("İnternet Erişimi Uyarısı");
            alertDialog.setMessage("Lütfen internet bağlantınızı kontrol ediniz.");

            alertDialog.setPositiveButton("Kapat", (dialog, which) -> finish());

            AlertDialog alert = alertDialog.create();
            alert.setCanceledOnTouchOutside(false);
            alert.show();
        }

        init();
    }

    private void init() {

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        bottomNavigation = findViewById(R.id.bottomNavigation);

        bottomNavigation.setOnNavigationItemSelectedListener(item -> {

            if(item.getItemId() == R.id.action_hayvanlar) {

                kategori = "Hayvanlar";
                loadImages(kategori);
            }

            if(item.getItemId() == R.id.action_renkler) {

                kategori = "Renkler";
                loadImages(kategori);
            }

            if(item.getItemId() == R.id.action_siyah) {

                kategori = "Siyah";
                loadImages(kategori);
            }

            if(item.getItemId() == R.id.action_diger) {

                kategori = "Diğer";
                loadImages(kategori);
            }

            return true;
        });

        loadImages(kategori);
    }

    private boolean isConnected() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    private void loadImages(String kategori) {

        images = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference(kategori);

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Model image = postSnapshot.getValue(Model.class);
                    images.add(image);
                }

                imageAdapter = new ImageAdapter(MainActivity.this, images);
                recyclerView.setAdapter(imageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(MainActivity.this, "Bir hata oluştu!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
    }
}