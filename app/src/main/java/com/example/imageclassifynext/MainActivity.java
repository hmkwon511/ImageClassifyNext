package com.example.imageclassifynext;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button selectButton, captureButton;
    ImageView imageView;
    TextView resultTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.CAMERA}, 1);
        }

        selectButton = findViewById(R.id.btnSelectImage);
        captureButton = findViewById(R.id.btnCaptureImage);
        imageView = findViewById(R.id.iV1);
        resultTv = findViewById(R.id.tvResult);



        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 10);
            }
        });

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 12);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if ((grantResults.length > 0) && (grantResults[0] != PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(getApplicationContext(), "카메라 권한 필요함...", Toast.LENGTH_SHORT).show();
                captureButton.setEnabled(false);
            }
        }
        
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Bitmap bitmap = null;

        if (data != null) {
            if (requestCode == 10) {
                Uri uri = data.getData();

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            else if (requestCode == 12) {
                bitmap = (Bitmap) data.getExtras().get("data");
            }
        }

        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);

        }

        super.onActivityResult(requestCode, resultCode, data);

    }

}