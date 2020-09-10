package com.wowtechnow.nasaphotoviewer.Fragment;

import android.app.AlertDialog;
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


public class FragmentOf2 extends Fragment implements FragmentLifecycle{

    private TextView Title2,date2,Description2 ,Copyright2;
    private ImageView AstronomicPicture2;
    private ProgressBar progressBar;
    private NestedScrollView nestedScrollView;
    private CompositeDisposable compositeDisposable;
    private INasa iNasa;
    private Common common;
    private static FragmentOf2 instance ;


    public static FragmentOf2 getInstance() {
        if(instance == null){
            instance = new FragmentOf2();
        }
        return instance;
    }

    public FragmentOf2() {
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        iNasa = retrofit.create(INasa.class);
        common = new Common();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_of2, container, false);
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        calendar.add(Calendar.DATE,-2);
        common.setDate(dateFormate.format(calendar.getTime()));
        Title2 = view.findViewById(R.id.tvTitle2);
        date2 = view.findViewById(R.id.tvDate2);
        progressBar = view.findViewById(R.id.progressCircular2);
        nestedScrollView = view.findViewById(R.id.nestedScrollView2);
        Description2 = view.findViewById(R.id.tvDescription2);
        AstronomicPicture2 = view.findViewById(R.id.pic_of_2);
        Copyright2 = view.findViewById(R.id.tvCopyRight2);

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
                                       }) .setNegativeButton("Cancel!!", new DialogInterface.OnClickListener() {
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
                                           Glide.with(getContext()).load(apodModel.getHdurl()).into(AstronomicPicture2);

                                   }
                                   Title2.setText(new StringBuilder("Title:-").append(apodModel.getTitle()));
                                   date2.setText(new StringBuilder("Date:-").append(apodModel.getDate()));
                                   Description2.setText(apodModel.getExplanation());
                                   if(apodModel.getCopyright() != null){
                                       Copyright2.setText(new StringBuilder("©Copyright To ").append(apodModel.getCopyright()));
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