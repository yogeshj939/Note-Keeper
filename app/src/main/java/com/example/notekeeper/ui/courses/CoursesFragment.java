package com.example.notekeeper.ui.courses;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notekeeper.CourseInfo;
import com.example.notekeeper.CourseRecyclerAdapter;
import com.example.notekeeper.DataManager;
import com.example.notekeeper.R;

import java.util.List;

public class CoursesFragment extends Fragment {

    private CourseRecyclerAdapter mCourseRecyclerAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_courses, container, false);
        initializeDisplayContent(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mCourseRecyclerAdapter.notifyDataSetChanged();

    }

    private void initializeDisplayContent(View view) {

        final RecyclerView courseRecycler = view.findViewById(R.id.course_list_items);
        final GridLayoutManager courseLayoutManager = new GridLayoutManager(getActivity(), 2);
        courseRecycler.setLayoutManager(courseLayoutManager);

        List<CourseInfo> courseList = DataManager.getInstance().getCourses();
        mCourseRecyclerAdapter = new CourseRecyclerAdapter(getActivity(), courseList);
        courseRecycler.setAdapter(mCourseRecyclerAdapter);
    }
}
