package com.yooiistudios.morningkit.common.sound;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 1. 20.
 *
 * MNSoundEffectsPlayer
 * 알람 사운드 재생을 담당
 */
public class MNSoundEffectsPlayer {

    /**
     * Singleton
     */
    private volatile static MNSoundEffectsPlayer instance;
    private volatile MediaPlayer mediaPlayer;

    public static MediaPlayer getMediaPlayer() {
        return getInstance().mediaPlayer;
    }

    private MNSoundEffectsPlayer() {
    }

    public static MNSoundEffectsPlayer getInstance() {
        if (instance == null) {
            synchronized (MNSoundEffectsPlayer.class) {
                if (instance == null) {
                    instance = new MNSoundEffectsPlayer();
                    instance.mediaPlayer = new MediaPlayer();
                }
            }
        }
        return instance;
    }

    public static void play(final int rawInt, final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AssetFileDescriptor afd = context.getResources().openRawResourceFd(rawInt);
                if (afd != null) {
                    try {
                        getMediaPlayer().reset();
                        getMediaPlayer().setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                        afd.close();
                        getMediaPlayer().prepare();
                        getMediaPlayer().start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public static void stop() {
        getMediaPlayer().stop();
    }

    /*
    public static void play(final Uri uri, final Context context) throws IOException {
        getMediaPlayer().reset();
        getMediaPlayer().setDataSource(context, uri);
        getMediaPlayer().prepare();
        getMediaPlayer().start();
    }
    */
}
