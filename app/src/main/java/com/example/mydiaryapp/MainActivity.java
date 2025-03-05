package com.example.mydiaryapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_CODE = 1;
    private static final String FILE_NAME = "diary_entries.dat";
    private EditText etTitle, etContent;
    private Button btnSave, btnViewEntries;
    private ArrayList<DiaryEntry> diaryEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);
        btnSave = findViewById(R.id.btnSave);
        btnViewEntries = findViewById(R.id.btnViewEntries);

        // Initialize the diaryEntries list
        diaryEntries = new ArrayList<>();

        // Load entries from file when the app starts
        loadEntriesFromFile();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEntry();
            }
        });

        btnViewEntries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewEntries();
            }
        });

        // Check and request storage permission
        checkStoragePermission();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Save entries to file when the app goes into the background
        saveEntriesToFile();
        Log.d("MainActivity", "App paused. Entries saved to file.");
    }

    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveEntry() {
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
            return;
        }

        DiaryEntry entry = new DiaryEntry(title, content);
        diaryEntries.add(entry);

        // Save entries to file immediately after adding a new entry
        saveEntriesToFile();

        etTitle.setText("");
        etContent.setText("");

        Toast.makeText(this, "Entry saved", Toast.LENGTH_SHORT).show();
    }

    private void viewEntries() {
        Intent intent = new Intent(this, ViewEntriesActivity.class);
        intent.putParcelableArrayListExtra("entries", new ArrayList<>(diaryEntries));
        Log.d("MainActivity", "Number of entries: " + diaryEntries.size());
        startActivity(intent);
    }

    private void saveEntriesToFile() {
        try {
            FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(diaryEntries);
            oos.close();
            fos.close();
            Log.d("MainActivity", "Entries saved to file");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MainActivity", "Error saving entries to file: " + e.getMessage());
        }
    }

    private void loadEntriesFromFile() {
        try {
            FileInputStream fis = openFileInput(FILE_NAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            diaryEntries = (ArrayList<DiaryEntry>) ois.readObject();
            ois.close();
            fis.close();
            Log.d("MainActivity", "Entries loaded from file. Number of entries: " + diaryEntries.size());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MainActivity", "Error loading entries from file: " + e.getMessage());
            // Initialize an empty list if the file doesn't exist or is corrupted
            diaryEntries = new ArrayList<>();
        }
    }
}