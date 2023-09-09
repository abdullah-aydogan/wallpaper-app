package com.abdullah.duvarkagitlari;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.github.clans.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;
import java.io.IOException;

public class ViewActivity extends AppCompatActivity {

    private ImageView imageView;
    private FloatingActionButton btn_set_lock, btn_set_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        init();
    }

    private void init() {

        btn_set_lock = findViewById(R.id.setLockWallpaper);
        btn_set_home = findViewById(R.id.setHomeWallpaper);

        btn_set_lock.setOnClickListener(view -> {

            setLockBackground();
        });

        btn_set_home.setOnClickListener(view -> {

            setHomeBackground();
        });

        imageView = findViewById(R.id.fullImage);

        Picasso.get().load(getIntent().getStringExtra("images")).into(imageView);
    }

    private void setLockBackground() {

        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        WallpaperManager manager = WallpaperManager.getInstance(getApplicationContext());

        try {

            manager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK);

            Toast.makeText(this, "Duvar kağıdı uygulandı.",
                    Toast.LENGTH_SHORT).show();
        }

        catch (IOException e) {

            Toast.makeText(this, "Bir şeyler yanlış gitti!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void setHomeBackground() {

        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        WallpaperManager manager = WallpaperManager.getInstance(getApplicationContext());

        try {

            manager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM);

            Toast.makeText(this, "Duvar kağıdı uygulandı.",
                    Toast.LENGTH_SHORT).show();
        }

        catch (IOException e) {

            Toast.makeText(this, "Bir şeyler yanlış gitti!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}