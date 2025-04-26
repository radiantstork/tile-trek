package org.example.mazeproject;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class MusicPlayer {
    private static MediaPlayer mediaPlayer;
    private static final int trackCount = 4;
    private static double currentVolume = 0.3;

    public static void playMusic(int trackIndex) {
        try {
            if (mediaPlayer == null) {
                String path;
                Media media;

                path = "/audio/background" + trackIndex + ".mp3";
                URL url = MusicPlayer.class.getResource(path);
                assert url != null;
                media = new Media(url.toExternalForm());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                mediaPlayer.setVolume(0.3);
            }
            mediaPlayer.play();
        } catch (Exception e) {
            System.out.println("Failed to play music: " + e.getMessage());
        }
    }

    public static void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
    }

    public static int getTrackCount() {
        return trackCount;
    }

    public static void increaseVolume() {
        if (currentVolume < 1) {
            currentVolume += 0.1;
            mediaPlayer.setVolume(currentVolume);
        }
    }

    public static void decreaseVolume() {
        if (currentVolume > 0) {
            currentVolume -= 0.1;
            mediaPlayer.setVolume(currentVolume);
        }
    }
}
