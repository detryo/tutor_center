package technologies.troubleshoot.easytution;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by kaizer on 12/3/16.
 */

public class NewsFeedTabAdapter extends FragmentPagerAdapter {

    private Context context;

    public NewsFeedTabAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            return new JobFeed();
        }
        else if (position == 1){
            return new JobFeed();
        }
        else if (position == 2) {
            return new ProfileFragment();
        }
        else {
            return new JobFeed();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return context.getString(R.string.news);
        }
        else if (position == 1) {
            return context.getString(R.string.trending);
        }
        else {
            return context.getString(R.string.profile);
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
