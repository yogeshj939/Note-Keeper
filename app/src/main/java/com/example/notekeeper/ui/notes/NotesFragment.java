package com.example.notekeeper.ui.notes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notekeeper.DataManager;
import com.example.notekeeper.NoteInfo;
import com.example.notekeeper.NoteRecyclerAdapter;
import com.example.notekeeper.R;

import java.util.List;

public class NotesFragment extends Fragment {

    private NoteRecyclerAdapter mNoteRecyclerAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        initializeDisplayContent(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mNoteRecyclerAdapter.notifyDataSetChanged();

    }

    private void initializeDisplayContent(View root) {

        final RecyclerView recyclerNotes = root.findViewById(R.id.course_list_items);
        final LinearLayoutManager noteLayoutManager = new LinearLayoutManager(getActivity());
        recyclerNotes.setLayoutManager(noteLayoutManager);

        List<NoteInfo> noteList = DataManager.getInstance().getNotes();
        mNoteRecyclerAdapter = new NoteRecyclerAdapter(getActivity(),noteList);
        recyclerNotes.setAdapter(mNoteRecyclerAdapter);
    }
}
