package technologies.troubleshoot.easytution;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by kaizer on 2/6/17.
 */

public class TeacherEducationInfo extends Fragment {

    EditText lastLevelOfStudyEditText, majorEditText, instituteNameEditText, cgpaEditText, yearOfpassingEditText, curriculumEditText, fromEditText, toEditText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.teacher_educational_info_layout, container, false);

        lastLevelOfStudyEditText = (EditText) rootView.findViewById(R.id.last_level_of_study_edit_view_id);
        majorEditText = (EditText) rootView.findViewById(R.id.major_group_edit_view_id);
        instituteNameEditText = (EditText) rootView.findViewById(R.id.institute_name_edit_view_id);
        cgpaEditText = (EditText) rootView.findViewById(R.id.cgpa_edit_view_id);
        yearOfpassingEditText = (EditText) rootView.findViewById(R.id.years_of_passing_edit_view_id);
        curriculumEditText = (EditText) rootView.findViewById(R.id.curriculum_edit_view_id);
        fromEditText = (EditText) rootView.findViewById(R.id.from_edit_view_id);
        toEditText = (EditText) rootView.findViewById(R.id.to_edit_view_id);

        return rootView;

    }
}
