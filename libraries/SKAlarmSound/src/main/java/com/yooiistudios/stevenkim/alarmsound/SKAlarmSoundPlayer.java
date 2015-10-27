package com.yooiistudios.stevenkim.alarmsound;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.io.FileNotFoundException;
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
    private int previousVolume;
//    private int previousAudioServiceMode = -100; // setMode 를 쓰지 않게 변경(베가 아이언 유플 때문)

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

    public static void play(final Context context, final Uri uri) throws IOException {
        getMediaPlayer().reset();
        getMediaPlayer().setDataSource(context, uri);

        try {
            play();
        } catch (IllegalStateException e) {
            Crashlytics.getInstance().core.logException(e);
            Crashlytics.getInstance().core.log("Uri: " + uri.toString());
        }
    }

    public static void stop() {
        getMediaPlayer().stop();
    }

    public static void stop(Context context) {
        getMediaPlayer().stop();

        // 음악을 멈추고 예전 볼륨으로 되돌려줌
        if (context != null) {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if (audioManager != null) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, getInstance().previousVolume, 0);
                audioManager.abandonAudioFocus(null);
            }
        }
    }

    public static void playAppMusic(final Context context, final int rawInt) throws IOException {
        AssetFileDescriptor afd = context.getResources().openRawResourceFd(rawInt);
        if (afd != null) {
            getMediaPlayer().reset();
            getMediaPlayer().setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            play();
        }
    }

    public static void playAppMusic(final Context context, final int rawInt, int volume) throws IOException {
        try {
            AssetFileDescriptor afd = context.getResources().openRawResourceFd(rawInt);
            if (afd != null) {
                getMediaPlayer().reset();
                getMediaPlayer().setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                afd.close();
                getMediaPlayer().prepare();
                play(context, volume);
            }
        } catch (FileNotFoundException e) {
            playDefaultRingtone(context, volume);
        } catch (Resources.NotFoundException e){
            playDefaultRingtone(context, volume);
        }
    }

    public static void playDefaultRingtone(Context context, int volume) throws IOException {
        getMediaPlayer().reset();
        SKAlarmSound defaultRingtone = SKAlarmSoundFactory.makeDefaultAlarmSound(context);
        Uri uri = Uri.parse(defaultRingtone.getSoundPath());
        getMediaPlayer().setDataSource(context, uri);
        getMediaPlayer().prepare();
        play(context, volume);
    }

    public static void playAlarmSound(final Context context, final SKAlarmSound alarmSound, int volume) throws IOException {
        if (alarmSound != null) {
            switch (alarmSound.getAlarmSoundType()) {
                case APP_MUSIC:
                    int appSoundRawInt = Integer.valueOf(alarmSound.getSoundPath());
                    if (appSoundRawInt != -1) {
                        SKAlarmSoundPlayer.playAppMusic(context, appSoundRawInt, volume);
                    } else {
                        Toast.makeText(context, "Invalid Alarm Sound", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case RINGTONE:
                case MUSIC:
                    playRingtoneOrMusic(context, alarmSound, volume);
                    break;

                default:
                    break;
            }
        } else {
            Toast.makeText(context, "No Alarm Sound", Toast.LENGTH_SHORT).show();
        }
    }

    private static void playRingtoneOrMusic(Context context, SKAlarmSound alarmSound, int volume) throws IOException {
        // 재생 직전 알람 사운드 validation 다시 체크 (크래시 대비)
        if (!SKAlarmSoundManager.isValidAlarmSoundPath(alarmSound.getSoundPath(), context)) {
            alarmSound = SKAlarmSoundFactory.makeDefaultAlarmSound(context);
        }

        Uri uri = Uri.parse(alarmSound.getSoundPath());
        getMediaPlayer().reset();
        getMediaPlayer().setDataSource(context, uri);

        try {
            getMediaPlayer().prepare();
            play(context, volume);
        } catch (IllegalStateException e) {
            Crashlytics.getInstance().core.logException(e);
            Crashlytics.getInstance().core.log("alarmSoundType: " + alarmSound.getAlarmSoundType());
            Crashlytics.getInstance().core.log("alarmSoundTitle: " + alarmSound.getSoundTitle());
            Crashlytics.getInstance().core.log("alarmSoundPath: " + alarmSound.getSoundPath());
        }
    }

    private static void play() throws IOException {
        getMediaPlayer().prepare();
        getMediaPlayer().setLooping(true);
        getMediaPlayer().start();
    }

    private static void play(final Context context, final int volume) throws IOException {
        // 오디오 포커스 등록
        final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT); // TRANSIENT 는 45초 미만의 소리 재생 요청, 하지만 더 사용가능할듯

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            // Start playback
            // 미디어 플레이어 준비
            getMediaPlayer().setLooping(true);
            getMediaPlayer().setAudioStreamType(AudioManager.STREAM_MUSIC);
            getMediaPlayer().start();

            // 모드를 먼저 설정하고 기존의 볼륨을 얻어야 제대로 된 값을 얻을 수 있음
//            getInstance().previousAudioServiceMode = audioManager.getMode();
            // 이 코드 때문에 베가 아이언 유플러스에서 시스템 소리가 뮤트가 됨 - setMode 를 쓰지 않게 변경
//            audioManager.setMode(AudioManager.STREAM_MUSIC);

            getInstance().previousVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

            // 무조건 스피커로 출력
            audioManager.setSpeakerphoneOn(true);

            // 천천히 음량을 높여줌
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                    int targetVolume = (int) (volume * (maxVolume / 100.0f)); // AudioManager 의 볼륨으로 환산
                    int currentVolume = 0;
                    int OFFSET = 1;

                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0); // 볼륨 0에서 시작

                    // 천천히 음량을 높임 - 추가: 재생 중일 때만
                    while (currentVolume < targetVolume && getMediaPlayer().isPlaying()) {
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
}
