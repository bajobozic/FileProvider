package com.example.bajob.fileprovider;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static final int PICTURE_REQUEST = 100;
    private static final int CAMERA_REQUEST_CODE = 5000;
    private static final String SAVED_FILE = "myfile";
    private File file;
    private AspectImageView imageView;
    private AspectImageView scrimView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(SAVED_FILE)) {
            file = (File) savedInstanceState.getSerializable(SAVED_FILE);
        }
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button);
        imageView = (AspectImageView) findViewById(R.id.image);
        scrimView = (AspectImageView) findViewById(R.id.scrim);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });
        if (file != null) {
            imageView.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
            scrimView.setVisibility(View.VISIBLE);
        }
    }

    private void takePicture() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CAMERA)) {
                showRequestCameraPermissionRationale();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
            }
        } else {
            getPicture();
        }
    }

    private void getPicture() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File filePath = new File(getFilesDir(), "images");
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        file = new File(filePath, "myimage.png");
        if (file.exists()) {
            file.delete();
        }
        Uri pictureUri = FileProvider.getUriForFile(MainActivity.this, BuildConfig.FILES_AUTHORITY, file);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }*/
        if (cameraIntent.resolveActivity(MainActivity.this.getPackageManager()) != null) {
            startActivityForResult(cameraIntent, PICTURE_REQUEST);
        }
    }

    private void showRequestCameraPermissionRationale() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Camera")
                .setMessage("please turn on camera to take photo")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
                    }
                });
        builder.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; i++) {
                    if (permissions[i].equals(Manifest.permission.CAMERA) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        getPicture();
                        break;
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICTURE_REQUEST) {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                imageView.setImageBitmap(bitmap);
                scrimView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void
    onSaveInstanceState(Bundle outState) {
        outState.putSerializable(SAVED_FILE, file);
        super.onSaveInstanceState(outState);
    }

}
