package org.ravenest.octify.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.ravenest.octify.Fragments.ChartFragment;
import org.ravenest.octify.Fragments.TodayFragment;

public class MainPagerAdapter extends FragmentPagerAdapter {
    private CharSequence[] tabTitles = {"Today", "Chart"};

    public MainPagerAdapter(FragmentManager f) {
        super(f);
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new TodayFragment();
            case 1:
                return new ChartFragment();
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return tabTitles.length;
    }
}
