package org.example.mazeproject;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.Objects;

public class MusicPlayer {
    public static void playMusic() {
        try {
            MediaPlayer mediaPlayer;
            String path;
            Media media;

            path = Objects.requireNonNull(MediaPlayer.class.getResource("/audio/background.mp3")).toExternalForm();
            media = new Media(path);

            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setVolume(0.1);
            mediaPlayer.play();
        } catch (Exception e) {
            System.out.println("Failed to play music: " + e.getMessage());
        }
    }
}
