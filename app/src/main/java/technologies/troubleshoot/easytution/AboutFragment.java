package technologies.troubleshoot.easytution;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by kaizer on 2/24/17.
 */

public class AboutFragment extends Fragment {

    TextView hyperLink;

    public AboutFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.about_layout_activity, container, false);

        hyperLink = (TextView) rootView.findViewById(R.id.hyper_link_trouble_shoot_id);

        hyperLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://www.troubleshoot-tech.com/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        ((DashBoard) getActivity()).setActionBarTitle("About");

        return rootView;
    }
}
