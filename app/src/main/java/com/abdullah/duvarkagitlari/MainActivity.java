package com.abdullah.duvarkagitlari;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
    private Toolbar toolbar_main;

    private int checkedItem = 3;
    private String kategori = "Diğer";
    private String[] kategoriler = new String[] {"Hayvanlar", "Renkler", "Siyah", "Diğer"};

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

        toolbar_main = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

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

                Toast.makeText(MainActivity.this, "HATA!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Kategoriler");

        builder.setSingleChoiceItems(kategoriler, checkedItem, (dialogInterface, i) -> {

            switch (i) {

                case 0:
                    kategori = "Hayvanlar";
                    loadImages(kategori);
                    checkedItem = 0;
                    break;

                case 1:
                    kategori = "Renkler";
                    loadImages(kategori);
                    checkedItem = 1;
                    break;

                case 2:
                    kategori = "Siyah";
                    loadImages(kategori);
                    checkedItem = 2;
                    break;

                case 3:
                    kategori = "Diğer";
                    loadImages(kategori);
                    checkedItem = 3;
                    break;
            }
        });

        builder.setPositiveButton("Tamam", (dialogInterface, i) -> dialogInterface.dismiss());

        builder.create();
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.nav_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_kategori) {

            showAlertDialog();
        }

        return true;
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
    }
}