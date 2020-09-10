package com.wowtechnow.nasaphotoviewer.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import com.wowtechnow.nasaphotoviewer.Adapter.ViewPagerAdapter;
import com.wowtechnow.nasaphotoviewer.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class ImageFragment extends Fragment {

    public ImageFragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_image, container, false);
        TabLayout tabs = view.findViewById(R.id.tabs);
        ViewPager viewPager = view.findViewById(R.id.view_pager);
        tabs.setupWithViewPager(viewPager);
        setUpViewPager(viewPager);
        return view;
    }


    private void setUpViewPager(ViewPager viewPager){
        assert getFragmentManager() != null;
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.setUpFragment( FragmentPicOfTheday.getInstance() ,"Today's Pic");
        adapter.setUpFragment(FragmentOfYesterday.getInstance(),"Yesterday's Pic");
        adapter.setUpFragment(FragmentOf2.getInstance(),getDate(2));
        adapter.setUpFragment(FragmentOf3.getInstance(),getDate(3));
        adapter.setUpFragment(FragmentOf4.getInstance(),getDate(4));
        adapter.setUpFragment(FragmentOf5.getInstance(),getDate(5));
        adapter.setUpFragment(FragmentOf6.getInstance(),getDate(6));

        ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            int currentPosition = 0;

            @Override
            public void onPageSelected(int position) {
                FragmentLifecycle fragmentToShow = (FragmentLifecycle) adapter.getItem(position);
                fragmentToShow.onResumeFragment();
                FragmentLifecycle fragmentToHide =(FragmentLifecycle) adapter.getItem(currentPosition);
                fragmentToHide.onPauseFragment();
                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        };

        viewPager.setOnPageChangeListener(pageChangeListener);
        viewPager.setAdapter(adapter);

    }
    private String getDate(int DateToLess){
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        calendar.add(Calendar.DATE,-DateToLess);
        return dateFormate.format(calendar.getTime());
    }
}