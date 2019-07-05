package com.example.soundexample;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private SoundPool soundPool;
    private AudioManager audioManager;
    private MediaPlayer mediaPlayer;
    private boolean loaded;
    private int music;
    private float volume;

    private static final int MAX_STREAMS = 5;
    private static final int streamType = AudioManager.STREAM_MUSIC;
    private static final int REQUEST_CODE_MUSIC =2307;
    private static final int REQUEST_CODE_CHOOSE_MUSIC =0405;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        float currentVolumeIndex = audioManager.getStreamVolume(streamType);
        float maxVolumeIndex = audioManager.getStreamMaxVolume(streamType);
        this.volume = currentVolumeIndex/maxVolumeIndex;

        this.setVolumeControlStream(streamType);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        SoundPool.Builder builder = new SoundPool.Builder();
        builder.setAudioAttributes(audioAttributes).setMaxStreams(MAX_STREAMS);
        this.soundPool = builder.build();

        this.soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                loaded = true;
            }
        });

        Button btnRecord = findViewById(R.id.btnRecord);
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION); // mo ghi am
                startActivityForResult(intent,REQUEST_CODE_MUSIC);
            }
        });

        Button btnRecordVideo = findViewById(R.id.btnRecordVideo);
        btnRecordVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE); // mo quay video
                startActivityForResult(intent,REQUEST_CODE_MUSIC);
            }
        });

        Button btnOpenAppMusic = findViewById(R.id.btnOpenAppMusic);
        btnOpenAppMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH); // chon 1 app nghe nhac
                startActivityForResult(intent,REQUEST_CODE_MUSIC);
            }
        });
        Button btnChooseMusic = findViewById(R.id.btnChooseMusic);
        btnChooseMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();// mo bo nho va chon file nhac
                intent.setType("audio/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"select mp3"),REQUEST_CODE_CHOOSE_MUSIC);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK && data != null){
            if(requestCode == REQUEST_CODE_CHOOSE_MUSIC){
                this.mediaPlayer = MediaPlayer.create(this,data.getData());
                mediaPlayer.start();
            }
        }
    }
}
