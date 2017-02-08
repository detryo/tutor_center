package technologies.troubleshoot.easytution;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by kaizer on 12/3/16.
 */

public class TeacherProfileTabAdapter extends FragmentStatePagerAdapter {

    private Context context;

    public TeacherProfileTabAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            return new TeacherEducationInfo();
        }
        else {
            return new TeacherPersonalInfo();
        }

    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return context.getString(R.string.eduInfo);
        }
        else {
            return context.getString(R.string.personalInfo);
        }

    }

    @Override
    public int getCount() {
        return 2;
    }
}
