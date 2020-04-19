package com.example.notekeeper;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class NoteListActivity extends AppCompatActivity {

    private ArrayAdapter<NoteInfo> mNotesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NoteListActivity.this,NoteActivity.class));
            }
        });

        initializeDisplayContent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNotesAdapter.notifyDataSetChanged();
    }

    private void initializeDisplayContent() {
        final ListView noteList = findViewById(R.id.note_list);

        List<NoteInfo> noteInfoList = DataManager.getInstance().getNotes();

        mNotesAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, noteInfoList);
        noteList.setAdapter(mNotesAdapter);

        noteList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(NoteListActivity.this,NoteActivity.class);
//                NoteInfo note = (NoteInfo) noteList.getItemAtPosition(position);
                intent.putExtra(NoteActivity.NOTE_POSITION,position);
                startActivity(intent);
            }
        });
//        final RecyclerView recylclerNotes = findViewById(R.id.list_notes);
//        final LinearLayoutManager noteLayoutMannager = new LinearLayoutManager(this);
//        recylclerNotes.setLayoutManager(noteLayoutMannager);
    }

}
