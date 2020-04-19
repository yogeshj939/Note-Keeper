package com.example.notekeeper;

import android.util.Log;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;
import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static org.hamcrest.Matchers.*;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.assertion.ViewAssertions.*;

@RunWith(AndroidJUnit4.class)
public class NoteCreationTest {
   private DataManager sDataManager;

   @Before
   public void classSetup(){
      sDataManager = DataManager.getInstance();
   }

   @Rule
   public ActivityTestRule<NoteListActivity> mNoteListActivityRule =
           new ActivityTestRule<>(NoteListActivity.class);

   @Test
   public void createNewNode(){
      final CourseInfo course = sDataManager.getCourse("java_lang");
      final String title = "Test note title";
      final String text = "Test note text content";
//      ViewInteraction fabNewNote = onView(withId(R.id.fab));
//      fabNewNote.perform(click());
      onView(withId(R.id.fab)).perform(click());

      onView(withId(R.id.spinner_courses)).perform(click());
      onData(allOf(instanceOf(CourseInfo.class),equalTo(course))).perform(click());
      onView(withId(R.id.spinner_courses)).check(matches(withSpinnerText(containsString(course.getTitle()))));

      onView(withId(R.id.note_text_title)).perform(typeText(title));
      onView(withId(R.id.note_text_title)).check(matches(withText(containsString(title))));
      onView(withId(R.id.note_text_text)).perform(typeText(text));
      onView(withId(R.id.note_text_text)).check(matches(withText(containsString(text))));
      closeSoftKeyboard();
      pressBack();
      pressBack();

      int noteIndex = sDataManager.getNotes().size()-1;
      List<NoteInfo> noteList = sDataManager.getNotes();
      NoteInfo demoNote = noteList.get(8);
//      for(NoteInfo note : noteList){
//          Log.i("Note : ", String.valueOf(note));
//      }
      NoteInfo note = sDataManager.getNotes().get(noteIndex);
      assertEquals(course,note.getCourse());
      assertEquals(title,note.getTitle());
      assertEquals(text,note.getText());
   }

}