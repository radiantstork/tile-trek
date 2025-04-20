package org.example.mazeproject;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class MusicPlayer {
    private static MediaPlayer mediaPlayer;

    public static void playMusic() {
        try {
            if (mediaPlayer == null) {
                String path;
                Media media;

                path = "/audio/background.mp3";
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
        }
    }
}
