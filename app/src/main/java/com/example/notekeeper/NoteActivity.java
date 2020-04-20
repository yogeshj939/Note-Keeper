package com.example.notekeeper;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class NoteActivity extends AppCompatActivity {

    private static final String TAG = NoteActivity.class.getSimpleName();
    public static final String NOTE_INFO = "com.example.notekeeper.NOTE_INFO";
    public static final String NOTE_POSITION = "com.example.notekeeper.NOTE_POSITION";
    public static final int POSITION_NOT_SET = -1;
    private NoteInfo mNote;
    private boolean mIsNewNote;
    private Spinner spinner_courses;
    private EditText mTextNoteTitle;
    private EditText mTextNoteText;
    private int mNotePosition;
    private boolean mIsCancelling;
    private NoteActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewModelProvider viewModelProvider = new ViewModelProvider(getViewModelStore(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()));
        mViewModel = viewModelProvider.get(NoteActivityViewModel.class);

        if(mViewModel.mIsNewlyCreated && savedInstanceState != null)
            mViewModel.restoreState(savedInstanceState);

        mViewModel.mIsNewlyCreated = false;

        List<CourseInfo> courseInfoList = DataManager.getInstance().getCourses();
        ArrayAdapter<CourseInfo> adapterCourses = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courseInfoList);
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_courses = findViewById(R.id.spinner_courses);
        spinner_courses.setAdapter(adapterCourses);

        readDisplayStateValues();
        if(!mIsNewNote)
            saveOriginalNoteValues();

        mTextNoteTitle = findViewById(R.id.note_text_title);
        mTextNoteText = findViewById(R.id.note_text_text);

        if(!mIsNewNote)
            displayNote(spinner_courses,mTextNoteTitle,mTextNoteText);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(outState != null)
            mViewModel.saveState(outState);
    }

    private void saveOriginalNoteValues() {
        mViewModel.mOriginalCourseId = mNote.getCourse().getCourseId();
        mViewModel.mOriginalNoteTitle = mNote.getTitle();
        mViewModel.mOriginalNoteText = mNote.getText();
    }

    private void displayNote(Spinner spinner_courses, EditText textNoteTitle, EditText textNoteText) {
        List<CourseInfo> courseList = DataManager.getInstance().getCourses();
        int courseIndex = courseList.indexOf(mNote.getCourse());
        spinner_courses.setSelection(courseIndex);
        textNoteTitle.setText(mNote.getTitle());
        textNoteText.setText(mNote.getText());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        if(mIsCancelling){
            Log.d(TAG, "Cancelling");
            if(mIsNewNote){
                DataManager.getInstance().removeNote(mNotePosition);
            } else {
                setOriginalNoteValues();
            }
        } else {
            saveNoteChanges();
        }
    }

    private void setOriginalNoteValues() {
        mNote.setCourse((CourseInfo) DataManager.getInstance().getCourse(mViewModel.mOriginalCourseId));
        mNote.setTitle(mViewModel.mOriginalNoteTitle);
        mNote.setText(mViewModel.mOriginalNoteText);
    }

    private void saveNoteChanges() {
        mNote.setCourse((CourseInfo) spinner_courses.getSelectedItem());
        mNote.setTitle(mTextNoteTitle.getText().toString());
        mNote.setText(mTextNoteText.getText().toString());
    }

    private void readDisplayStateValues() {
        Intent intent = getIntent();
        mNotePosition = intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET);
        mIsNewNote = mNotePosition == POSITION_NOT_SET;
        if(mIsNewNote){
           createNewNote();
        }
        Log.i(TAG, "mNotePosition : "+mNotePosition);
        mNote = DataManager.getInstance().getNotes().get(mNotePosition);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void createNewNote() {
        DataManager dm = DataManager.getInstance();
        mNotePosition = dm.createNewNote();
        //mNote = dm.getNotes().get(mNotePosition);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send_email) {
            sendEmail();
        } else if(id == R.id.action_cancel){
            mIsCancelling = true;
            finish();
        } else if(id == R.id.action_next){
            moveNext();
        }

        return super.onOptionsItemSelected(item);
    }

    //Method gets called each time the noteActivity is created to prepare menu options.
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_next);
        int lastIndex = DataManager.getInstance().getNotes().size()-1;
        item.setEnabled(mNotePosition < lastIndex);
        return super.onPrepareOptionsMenu(menu);
    }

    private void moveNext() {
        saveNoteChanges();
        ++mNotePosition;
        mNote = DataManager.getInstance().getNotes().get(mNotePosition);
        saveOriginalNoteValues();
        displayNote(spinner_courses,mTextNoteTitle,mTextNoteText);
        //Invalidate Options menu after each time user clicks next
        invalidateOptionsMenu();
    }

    private void sendEmail() {
        CourseInfo course = (CourseInfo) spinner_courses.getSelectedItem();
        String subject = mTextNoteTitle.getText().toString();
        String text = "Checkout what I found on the pluralsight course on \" "+course+"\".\n"+mTextNoteText.getText().toString();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc2822");
        intent.putExtra(Intent.EXTRA_SUBJECT,subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(intent);
    }
}
