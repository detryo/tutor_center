package technologies.troubleshoot.easytution;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.net.InetAddress;

/**
 * Created by kaizer on 2/6/17.
 */



public class TeacherProfileFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.teacher_profile_tab_layout, container, false);

        ((DashBoard) getActivity()).setActionBarTitle(getResources().getString(R.string.profile));

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        TeacherProfileTabAdapter adapter = new TeacherProfileTabAdapter(getContext(), getFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Find the tab layout that shows the tabs
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);

        // Connect the tab layout with the view pager. This will
        //   1. Update the tab layout when the view pager is swiped
        //   2. Update the view pager when a tab is selected
        //   3. Set the tab layout's tab names with the view pager's adapter's titles
        //      by calling onPageTitle()
        tabLayout.setupWithViewPager(viewPager);

        return rootView;

    }

}
