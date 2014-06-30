package com.yooiistudios.stevenkim.alarmsound;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by StevenKim in SKAlarmSoundSample from Yooii Studios Co., LTD. on 2014. 1. 3.
 * <p/>
 * SKAlarmSoundPlayer
 * 알람 사운드 재생을 담당
 */
public class SKAlarmSoundPlayer {

    /**
     * Singleton
     */
    private volatile static SKAlarmSoundPlayer instance;
    private MediaPlayer mediaPlayer;

    public static MediaPlayer getMediaPlayer() {
        return getInstance().mediaPlayer;
    }

    private SKAlarmSoundPlayer() {
    }

    public static SKAlarmSoundPlayer getInstance() {
        if (instance == null) {
            synchronized (SKAlarmSoundManager.class) {
                if (instance == null) {
                    instance = new SKAlarmSoundPlayer();
                    instance.mediaPlayer = new MediaPlayer();
                }
            }
        }
        return instance;
    }

    public static void play(final Uri uri, final Context context) throws IOException {
        getMediaPlayer().reset();
        getMediaPlayer().setDataSource(context, uri);
        play();
    }

    public static void stop() {
        getMediaPlayer().stop();
    }

    public static void playAppMusic(final int rawInt, final Context context) throws IOException {
        AssetFileDescriptor afd = context.getResources().openRawResourceFd(rawInt);
        if (afd != null) {
            getMediaPlayer().reset();
            getMediaPlayer().setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            play();
        }
    }

    public static void playAppMusic(final int rawInt, int volume, final Context context) throws IOException {
        AssetFileDescriptor afd = context.getResources().openRawResourceFd(rawInt);
        if (afd != null) {
            getMediaPlayer().reset();
            getMediaPlayer().setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            play(volume, context);
        }
    }

    public static void playAlarmSound(final SKAlarmSound alarmSound, int volume, final Context context) throws IOException {
        if (alarmSound != null) {
            switch (alarmSound.getAlarmSoundType()) {
                case APP_MUSIC:
                    int appSoundRawInt = Integer.valueOf(alarmSound.getSoundPath());
                    if (appSoundRawInt != -1) {
                        SKAlarmSoundPlayer.playAppMusic(appSoundRawInt, volume, context);
                    } else {
                        Toast.makeText(context, "Invalid Alarm Sound", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case RINGTONE:
                case MUSIC:
                    getMediaPlayer().reset();
                    Uri uri = Uri.parse(alarmSound.getSoundPath());
                    getMediaPlayer().setDataSource(context, uri);
                    play(volume, context);
                    break;

                default:
                    break;
            }
        } else {
            Toast.makeText(context, "No Alarm Sound", Toast.LENGTH_SHORT).show();
        }
    }

    private static void play() throws IOException {
        getMediaPlayer().prepare();
        getMediaPlayer().setLooping(true);
        getMediaPlayer().start();
    }

    private static void play(final int volume, final Context context) throws IOException {
        getMediaPlayer().prepare();
        getMediaPlayer().setLooping(true);
        getMediaPlayer().setAudioStreamType(AudioManager.STREAM_MUSIC);
        getMediaPlayer().start();

        // 천천히 음량을 높여줌
        new Thread(new Runnable() {
            @Override
            public void run() {
                AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                int targetVolume = (int) (volume * (maxVolume / 100.0f)); // AudioManager의 볼륨으로 환산
                int currentVolume = 0;
                int OFFSET = 1;

                // 볼륨 0에서 시작
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);

                // 천천히 음량을 높임
                while (currentVolume < targetVolume) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    currentVolume += OFFSET;
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
                }
            }
        }).start();
    }
}
