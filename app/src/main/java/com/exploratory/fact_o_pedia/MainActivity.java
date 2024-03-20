package com.exploratory.fact_o_pedia;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;
import android.net.Uri;
import java.io.IOException;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private static final int REQUEST_CAMERA_PERMISSION = 102;
    private static final int REQUEST_GALLERY_PERMISSION = 103;
    private static final int REQUEST_IMAGE_GALLERY = 104;



    EditText editText;
    String item = "All Languages";
    String[] items = {"All Languages", "English", "Hindi"};
    AutoCompleteTextView autoCompleteTxt;
    ArrayAdapter<String> adapterItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // Check if onboarding is needed
        SharedPreferences sharedPreferences = getSharedPreferences("onboarding", MODE_PRIVATE);
        boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);

        if (isFirstRun) {
            // Launch onboarding activity
            Intent intent = new Intent(this, OnboardingActivity.class);
            startActivity(intent);
            finish(); // Finish MainActivity to prevent going back
        } else {
            // Proceed to the main activity layout
            setContentView(R.layout.activity_main);

            // Initialize views and set listeners
            editText = findViewById(R.id.editText);
            autoCompleteTxt = findViewById(R.id.auto_complete_txt);
            adapterItems = new ArrayAdapter<>(this, R.layout.list_item, items);
            autoCompleteTxt.setAdapter(adapterItems);
            autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    item = parent.getItemAtPosition(position).toString();
                }
            });

        }
    }

    // Method to handle image upload
    public void uploadImage(View view) {
        // Check camera permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        } else {
            // Permission has already been granted, show options for capturing or uploading image
            showImageOptions();
        }
    }

    // Method to show options for capturing or uploading image
    private void showImageOptions() {
        // Create a bottom sheet dialog to display options
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_image_options, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        // Find views in the bottom sheet layout
        View cameraOption = bottomSheetView.findViewById(R.id.camera_option);
        View galleryOption = bottomSheetView.findViewById(R.id.gallery_option);

        // Set click listeners for camera and gallery options
        cameraOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                launchCameraIntent();
            }
        });

        galleryOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                checkGalleryPermissionAndOpen();
            }
        });

        bottomSheetDialog.show();
    }

    private void openGallery() {
        // Implement code to open the device's gallery and handle image selection
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_IMAGE_GALLERY);
    }

    private void checkGalleryPermissionAndOpen() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, check if we should show the rationale
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user
                Toast.makeText(this, "Gallery permission is required to select images", Toast.LENGTH_SHORT).show();
            }

            // Request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_GALLERY_PERMISSION);
        } else {
            // Permission has already been granted, open gallery
            openGallery();
        }
    }


    // Method to launch camera intent to capture image
    private void launchCameraIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to handle the result of permissions request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, show options for capturing or uploading image
                showImageOptions();
            } else {
                // Permission denied
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_GALLERY_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open gallery
                openGallery();
            } else {
                // Permission denied
                Toast.makeText(this, "Gallery permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Handle image capture result
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            processImageWithOCR(imageBitmap);
        } else if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK && data != null) {
            // Handle image selection from gallery
            Uri selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                processImageWithOCR(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void processImageWithOCR(Bitmap imageBitmap) {
        // Convert Bitmap to FirebaseVisionImage
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(imageBitmap);

        // Get an instance of FirebaseVision
        FirebaseVision firebaseVision = FirebaseVision.getInstance();

        // Create an instance of FirebaseVisionTextRecognizer
        firebaseVision.getOnDeviceTextRecognizer()
                .processImage(image)
                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                    @Override
                    public void onSuccess(FirebaseVisionText firebaseVisionText) {
                        // Extract text from the image
                        String extractedText = firebaseVisionText.getText();

                        // Set the extracted text to the search bar
                        editText.setText(extractedText);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle errors
                        Toast.makeText(MainActivity.this, "OCR failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void search(View view) {
        Intent intent = new Intent(this, FactListActivity.class);

        String query = editText.getText().toString();
        if (query.length() == 0) {
            Toast.makeText(this, "Enter the query before search", Toast.LENGTH_LONG).show();
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("query", query);
            bundle.putString("item", item);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    // Methods for handling button clicks remain unchanged...
    public void hidustantimesFunc(View view){
        Intent intent = new Intent(this, Updates.class);
        Bundle bundle = new Bundle();
        bundle.putInt("turn",1);
        intent.putExtras(bundle);
        startActivity(intent);
//      toast.makeText(this,"The button is clicked",Toast.LENGTH_LONG).show();
    }
    public void thetimesofindiaFunc(View view){
        Intent intent = new Intent(this, Updates.class);
        Bundle bundle = new Bundle();
        bundle.putInt("turn",2);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void theindianexpressFunc(View view){
        Intent intent = new Intent(this, Updates.class);
        Bundle bundle = new Bundle();
        bundle.putInt("turn",3);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void thehinduFunc(View view){
        Intent intent = new Intent(this, Updates.class);
        Bundle bundle = new Bundle();
        bundle.putInt("turn",4);
        intent.putExtras(bundle);
        startActivity(intent);
    }
//    public void login(View view) {
//        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//        startActivity(intent);
//    }
}

