package com.cykulapps.lcservices.ticketing;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by CYKUL03 on 20-03-2018.
 */

class TicketingPagerAdapter extends FragmentPagerAdapter {

    int mNumOfTabs;

    public TicketingPagerAdapter(FragmentManager fm, int NumOfTabs)

    {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }



    public TicketingPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public Fragment getItem(int position) {

        switch (position) {

            case 0:
                OneTimeFragment tab1 = new OneTimeFragment();
                return tab1;

            case 1:
                MonthlyFragment tab2= new MonthlyFragment();
                return tab2;
            case 2:
                AnnualFragment tab3= new AnnualFragment();
                return tab3;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
