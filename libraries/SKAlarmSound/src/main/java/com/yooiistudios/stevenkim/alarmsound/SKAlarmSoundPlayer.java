package com.yooiistudios.stevenkim.alarmsound;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.io.File;
import java.io.FileInputStream;
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
        Uri systemRingtoneUri = Settings.System.DEFAULT_RINGTONE_URI;
        getMediaPlayer().setDataSource(context, systemRingtoneUri);
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
        setDataSource(context, alarmSound);

        try {
            getMediaPlayer().prepare();
            play(context, volume);
        } catch (IOException e) {
            try {
                playDefaultRingtone(context, volume);
            } catch (Exception e1) {
                reportAlarmSoundExceptionToCrashlytics(alarmSound, e);
            }
        } catch (IllegalStateException e) {
            try {
                playDefaultRingtone(context, volume);
            } catch (Exception e1) {
                reportAlarmSoundExceptionToCrashlytics(alarmSound, e1);
            }
        }
    }

    private static void setDataSource(Context context, SKAlarmSound alarmSound) throws IOException {
        // 재생 직전 알람 사운드 validation 다시 체크 (크래시 대비)
        if (!SKAlarmSoundManager.isValidAlarmSoundPath(alarmSound.getSoundPath(), context)) {
            alarmSound = SKAlarmSoundFactory.makeDefaultAlarmSound(context);
        }

        String fileInfo;
        if (Settings.System.DEFAULT_RINGTONE_URI.equals(Uri.parse(alarmSound.getSoundPath()))) {
            fileInfo  = RingtoneUtils.getSystemRingtoneMediaUri(context).toString();
        } else {
            fileInfo = alarmSound.getSoundPath();
        }

        if (fileInfo.startsWith("content://")) {
            Uri uri = Uri.parse(fileInfo);
            try {
                fileInfo = getSoundPathFromContentUri(context, uri);
            } catch (Exception ignored) {
                fileInfo = alarmSound.getSoundPath();
            }
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                setDataSourcePostHoneyComb(context, getMediaPlayer(), fileInfo);
            } else {
                try {
                    setDataSourcePreHoneyComb(getMediaPlayer(), fileInfo);
                } catch (Exception ignored) {
                    setDataSourcePostHoneyComb(context, getMediaPlayer(), fileInfo);
                }
            }
        } catch (Exception e) {
            reportAlarmSoundExceptionToCrashlytics(alarmSound, e);
            try {
                setDataSourceUsingFileDescriptor(getMediaPlayer(), fileInfo);
            } catch (Exception e1) {
                reportAlarmSoundExceptionToCrashlytics(alarmSound, e1);

                String uri = getSoundUriFromPath(context, fileInfo);
                getMediaPlayer().reset();
                getMediaPlayer().setDataSource(uri);
            }
        }
    }

    private static void reportAlarmSoundExceptionToCrashlytics(SKAlarmSound alarmSound, Exception e) {
        Crashlytics.getInstance().core.log("alarmSoundType: " + alarmSound.getAlarmSoundType());
        Crashlytics.getInstance().core.log("alarmSoundTitle: " + alarmSound.getSoundTitle());
        Crashlytics.getInstance().core.log("alarmSoundPath: " + alarmSound.getSoundPath());
        Crashlytics.getInstance().core.logException(e);
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
            // 재생 전 볼륨 기억하고 0으로 설정
            getInstance().previousVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);

            // 미디어 플레이어 준비
            getMediaPlayer().setLooping(true);
            getMediaPlayer().setAudioStreamType(AudioManager.STREAM_MUSIC);
            getMediaPlayer().start();

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

                    // 천천히 음량을 높임 (재생 중일 경우만)
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

    /**
     * setDataSource 관련 외부 코드
     * http://stackoverflow.com/questions/16395559/mediaplayer-setdatasource-failed-with-status-0x80000000-for-ringtone-set-by-file
     */
    private static void setDataSourcePreHoneyComb(MediaPlayer mp, String fileInfo) throws Exception {
        mp.reset();
        mp.setDataSource(fileInfo);
    }

    private static void setDataSourcePostHoneyComb(Context context, MediaPlayer mp, String fileInfo) throws Exception {
        mp.reset();
        mp.setDataSource(context, Uri.parse(Uri.encode(fileInfo)));
    }

    private static void setDataSourceUsingFileDescriptor(MediaPlayer mp, String fileInfo) throws Exception {
        File file = new File(fileInfo);
        FileInputStream inputStream = new FileInputStream(file);
        mp.reset();
        mp.setDataSource(inputStream.getFD());
        inputStream.close();
    }

    private static String getSoundUriFromPath(Context context, String path) {
        Uri soundUri = MediaStore.Audio.Media.getContentUriForPath(path);
        Cursor soundCursor = context.getContentResolver().query(soundUri, null,
                MediaStore.Audio.Media.DATA + "='" + path + "'", null, null);

        String soundUrlString = soundUri.toString();
        if (soundCursor.moveToFirst()) {
            long id = soundCursor.getLong(soundCursor.getColumnIndex(MediaStore.Audio.Media._ID));

            if (!soundUri.toString().endsWith(String.valueOf(id))) {
                soundUrlString = soundUri + "/" + id;
            }
        }
        soundCursor.close();
        return soundUrlString;
    }

    public static String getSoundPathFromContentUri(Context context, Uri contentUri) {
        String[] proj = { MediaStore.Audio.Media.DATA };
        Cursor soundCursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        soundCursor.moveToFirst();
        String path = soundCursor.getString(soundCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
        soundCursor.close();

        return path;
    }
}
