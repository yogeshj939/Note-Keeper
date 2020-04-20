package com.example.notekeeper;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.ViewHolder>{

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private List<NoteInfo> mNotes;

    public NoteRecyclerAdapter(Context context, List<NoteInfo> notes) {
        mContext = context;
        this.mNotes = notes;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_note_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NoteInfo note = mNotes.get(position);
        holder.mCourseText.setText(note.getCourse().getTitle());
        holder.mTitleText.setText(note.getTitle());
        holder.mNotePosition = position;
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public final TextView mCourseText;
        public final TextView mTitleText;
        public int mNotePosition;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mCourseText = itemView.findViewById(R.id.text_course);
            mTitleText = itemView.findViewById(R.id.text_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, NoteActivity.class);
                    intent.putExtra(NoteActivity.NOTE_POSITION,mNotePosition);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
