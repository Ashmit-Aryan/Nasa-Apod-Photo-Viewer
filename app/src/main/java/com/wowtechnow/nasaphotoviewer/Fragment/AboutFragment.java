package com.wowtechnow.nasaphotoviewer.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.Telephony;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wowtechnow.nasaphotoviewer.R;


public class AboutFragment extends Fragment {


    public AboutFragment() {

    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_about, container, false);
        TextView FeedBack = view.findViewById(R.id.tvFeedBack);
        FeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL,new String[]{"developerwowtechnow@gmail.com"});
                emailIntent.setType("message/rfc822");
                startActivity(Intent.createChooser(emailIntent,"Select Email Client :"));
            }
        });
        return view;
    }
}