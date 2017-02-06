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

public class TeacherPersonalInfo extends Fragment {

    EditText additionalNumberEditText, detailAddressEditText, nidNoEditText, fbIdEditText, linkedId, fatherNameEditText, motherNameEditText, fatherNumberEditText, motherNumberEditText, refPersonNameEditText, contactNumberEditText, relationEditText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.teacher_personal_info_layout, container, false);

        additionalNumberEditText = (EditText) rootView.findViewById(R.id.additional_number_edit_view_id);
        detailAddressEditText = (EditText) rootView.findViewById(R.id.detail_address_edit_view_id);
        nidNoEditText = (EditText) rootView.findViewById(R.id.nid_edit_view_id);
        fbIdEditText = (EditText) rootView.findViewById(R.id.fb_edit_view_id);
        linkedId = (EditText) rootView.findViewById(R.id.linked_edit_view_id);
        fatherNameEditText = (EditText) rootView.findViewById(R.id.father_name_edit_view_id);
        motherNameEditText = (EditText) rootView.findViewById(R.id.mother_name_edit_view_id);
        fatherNumberEditText = (EditText) rootView.findViewById(R.id.father_number_edit_view_id);
        motherNumberEditText = (EditText) rootView.findViewById(R.id.mother_number_edit_view_id);
        refPersonNameEditText = (EditText) rootView.findViewById(R.id.ref_person_name_edit_view_id);
        contactNumberEditText = (EditText) rootView.findViewById(R.id.contact_number_edit_view_id);
        relationEditText = (EditText) rootView.findViewById(R.id.relation_edit_view_id);

        return rootView;

    }
}
