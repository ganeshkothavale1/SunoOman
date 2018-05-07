package com.example.oem.myapplication;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.oem.myapplication.R;

import static android.content.ContentValues.TAG;
import static android.support.v4.content.ContextCompat.getDrawable;


public class OneFragment extends Fragment{
    ImageButton btn;
    boolean playPause;
    MediaPlayer mediaPlayer;
    ProgressDialog progressDialog;
    boolean initialStage = true;
    String stream ="http://sunooman.out.airtime.pro:8000/sunooman_a";
    boolean prepared=false,started=false;
    View view;
    public OneFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_one, container, false);
        btn = (ImageButton) view.findViewById(R.id.audioStreamBtn);
        btn.setEnabled(false);
        btn.setImageResource(R.drawable.play);
//        ImageView logo =(ImageView) view.findViewById(R.id.logo);
//        logo.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), WebActivity.class);
//                intent.putExtra("webLink", "https://www.youtube.com/embed/C0DPdy98e4c?autoplay=1&modestbranding=1&showinfo=0&rel=0&loop=1");
//                startActivity(intent);
//            }
//        });
        mediaPlayer = new MediaPlayer();
        progressDialog = new ProgressDialog(getContext());
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        new Player().execute(stream);
        started=true;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!started){
//                    if (initialStage) {
//                        new Player().execute(stream);
//                    } else {
                    mediaPlayer = new MediaPlayer();
                    progressDialog = new ProgressDialog(getContext());
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    new Player().execute(stream);
//                            mediaPlayer.start();
//                    }
                    btn.setImageResource(R.drawable.stop);

                    started=true;


                }else{
//                    mediaPlayer.pause();
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    btn.setImageResource(R.drawable.play);

                    started=false;

//                    mediaPlayer.reset();
                }
//                if (!playPause) {
//                    btn.setImageResource(R.drawable.stop);
//
//                    if (initialStage) {
//                        new Player().execute("https://www.ssaurel.com/tmp/mymusic.mp3");
//                    } else {
//                        if (!mediaPlayer.isPlaying())
//                            mediaPlayer.start();
//                    }
//
//                    playPause = true;
//
//                } else {
//                    btn.setImageResource(R.drawable.play);
//                    if (mediaPlayer.isPlaying()) {
//                        mediaPlayer.pause();
//                    }
//
//                    playPause = false;
//                }
            }
        });
        return view;
    }
    @Override
    public void onPause() {
        super.onPause();
//        if(started){
//            mediaPlayer.pause();
//        }
//        btn.setImageResource(R.drawable.play);

//        if (mediaPlayer != null) {
//            mediaPlayer.reset();
//            mediaPlayer.release();
//            mediaPlayer = null;
//        }
    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if(started){
//            mediaPlayer.start();
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if(prepared){
//            mediaPlayer.release();
//        }
//    }

    class Player extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                mediaPlayer.setDataSource(strings[0]);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        initialStage = true;
                        started = false;
                        btn.setImageResource(R.drawable.play);
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                    }
                });
                mediaPlayer.prepare();
                prepared = true;

            } catch (Exception e) {
                Log.e("MyAudioStreamingApp", e.getMessage());
                prepared = false;
            }

            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }
            progressDialog.dismiss();

            mediaPlayer.start();
            initialStage = false;
            btn.setEnabled(true);
            btn.setImageResource(R.drawable.stop);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setMessage("Buffering...");
            progressDialog.show();
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
        }
    }

}