package com.example.notekeeper;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class DataManagerTest {

    static DataManager sDataManager;

    @BeforeClass
    public static void classSetup(){
        sDataManager = DataManager.getInstance();
    }

    @Test
    public void createNewNote() {
        final CourseInfo course = sDataManager.getCourse("android_async");
        final String title = "This is test note title";
        final String text = "This is test note text";

        int noteIndex = sDataManager.createNewNote();
        NoteInfo note = sDataManager.getNotes().get(noteIndex);
        note.setCourse(course);
        note.setTitle(title);
        note.setText(text);

        NoteInfo compareNote = sDataManager.getNotes().get(noteIndex);
        assertEquals(course,compareNote.getCourse());
        assertEquals(title,compareNote.getTitle());
        assertEquals(text,compareNote.getText());
    }

    @Test
    public void createNewNoteOneStepCreation(){
        final CourseInfo course = sDataManager.getCourse("android_async");
        final String title = "This is test note title";
        final String text = "This is test note text";

        int noteIndex = sDataManager.createNewNote(course,title,text);

        NoteInfo compareNote = sDataManager.getNotes().get(noteIndex);
        List<NoteInfo> noteList = sDataManager.getNotes();
        assertEquals(course,compareNote.getCourse());
        assertEquals(title,compareNote.getTitle());
        assertEquals(text,compareNote.getText());

        for(NoteInfo note : noteList){
            System.out.println(note);
        }
    }
}