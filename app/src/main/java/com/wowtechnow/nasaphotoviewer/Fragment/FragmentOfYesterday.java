package com.wowtechnow.nasaphotoviewer.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import com.wowtechnow.nasaphotoviewer.Common.Common;
import com.wowtechnow.nasaphotoviewer.Model.APODModel;
import com.wowtechnow.nasaphotoviewer.R;
import com.wowtechnow.nasaphotoviewer.Retrofilt.INasa;
import com.wowtechnow.nasaphotoviewer.Retrofilt.RetrofitClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


public class FragmentOfYesterday extends Fragment implements FragmentLifecycle {
    private TextView Yesterday_Title,Yesterday_date,Yesterday_Description,Yesterdat_Copyright;
    private ProgressBar progressBar;
    private NestedScrollView nestedScrollView;
    private ImageView Yesterday_AstronomicPicture;

    CompositeDisposable compositeDisposable;
    INasa iNasa;
    Common common;

    static FragmentOfYesterday instance;


    public static FragmentOfYesterday getInstance() {
        if(instance == null)
        {
            instance = new FragmentOfYesterday();
        }
        return instance;
    }

    public FragmentOfYesterday() {
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        iNasa = retrofit.create(INasa.class);
        common = new Common();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_of_yesterday, container, false);
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        calendar.add(Calendar.DATE,-1);
        common.setDate(dateFormate.format(calendar.getTime()));
        Yesterday_Title = view.findViewById(R.id.tvTitleYesterday);
        progressBar = view.findViewById(R.id.progressCircularYesterday);
        nestedScrollView = view.findViewById(R.id.nestedScrollViewYesterday);
        Yesterday_date = view.findViewById(R.id.tvDateYesterday);
        Yesterday_Description = view.findViewById(R.id.tvDescriptionYesterday);
        Yesterday_AstronomicPicture = view.findViewById(R.id.pic_of_yesterday);
        Yesterdat_Copyright = view.findViewById(R.id.tvCopyRightYesterday);

        return view;
    }


    private void getAstronomicData() {
        compositeDisposable.add(iNasa.getAPOD(common.getDate(),common.isHD(), Common.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<APODModel>() {
                               @Override
                               public void accept(final APODModel apodModel) throws Exception {
                                   progressBar.setVisibility(View.GONE);
                                   nestedScrollView.setVisibility(View.VISIBLE);
                                   if(apodModel.getMedia_type().equals("video")){
                                       AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                       builder.setCancelable(false);
                                       builder.setTitle("Want To See Video?");
                                       builder.setMessage("This Content Is Not Image So We Cannot Display in Our App \n If You Want To See It Click Ok");
                                       builder.setPositiveButton("OK !!", new DialogInterface.OnClickListener() {
                                           @Override
                                           public void onClick(DialogInterface dialog, int which) {
                                               Intent iWebIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(apodModel.getUrl()));
                                               startActivity(iWebIntent);
                                           }
                                       }) .setNegativeButton("Cancel !!", new DialogInterface.OnClickListener() {
                                           @Override
                                           public void onClick(DialogInterface dialog, int which) {
                                               dialog.dismiss();
                                           }
                                       });
                                       builder.setIcon(getResources().getDrawable(R.drawable.ic_twotone_error_outline_24));
                                       builder.create().show();
                                   }
                                       if(apodModel.getMedia_type().equals("image")){
                                           assert getContext() != null;
                                           Glide.with(getContext()).load(apodModel.getHdurl()).into(Yesterday_AstronomicPicture);

                                   }
                                   Yesterday_Title.setText(new StringBuilder("Title:-").append(apodModel.getTitle()));
                                   Yesterday_date.setText(new StringBuilder("Date:-").append(apodModel.getDate()));
                                   Yesterday_Description.setText(apodModel.getExplanation());
                                   if (apodModel.getCopyright() != null){
                                       Yesterdat_Copyright.setText(new StringBuilder("©Copyright To ").append(apodModel.getCopyright()));
                                   }
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Toast.makeText(getActivity(), throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                               }
                           }
                ));
    }

    @Override
    public void onPauseFragment() {
        progressBar.setVisibility(View.VISIBLE);
        nestedScrollView.setVisibility(View.GONE);
    }

    @Override
    public void onResumeFragment() {
        getAstronomicData();
    }
}