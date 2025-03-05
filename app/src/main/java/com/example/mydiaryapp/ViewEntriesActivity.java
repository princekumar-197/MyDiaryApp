package com.example.mydiaryapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ViewEntriesActivity extends AppCompatActivity {

    private ListView lvEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_entries);

        lvEntries = findViewById(R.id.lvEntries);

        ArrayList<DiaryEntry> entries = getIntent().getParcelableArrayListExtra("entries");
        Log.d("ViewEntriesActivity", "Number of entries received: " + (entries != null ? entries.size() : "null"));

        if (entries == null || entries.isEmpty()) {
            Log.e("ViewEntriesActivity", "No entries found");
            finish();
            return;
        }

        // Create an ArrayAdapter to display the entries
        ArrayAdapter<DiaryEntry> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, entries);
        lvEntries.setAdapter(adapter);
    }
}