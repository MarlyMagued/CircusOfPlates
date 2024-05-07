/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package circusofplates;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author marlymaged
 */
public class MusicPlayer implements Runnable, Observer {

    private final Object lock = new Object();
   

    @Override
    public void run() {
        AudioInputStream audioIn1 = null;
        AudioInputStream audioIn2 = null;
        try {
            File file1 = new File("backgroundMusic.wav");
            audioIn1 = AudioSystem.getAudioInputStream(file1);
            Clip backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioIn1);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundMusic.start();
            
            
            synchronized (lock) {               
                    lock.wait(); // Wait until notified               
            }
            // Stop the music and close resources when the loop exits
            backgroundMusic.stop();
            backgroundMusic.close();
            
            
            File file2 = new File("gameOverMusic.wav");
            audioIn2 = AudioSystem.getAudioInputStream(file2);
            Clip gameOverMusic = AudioSystem.getClip();
            gameOverMusic.open(audioIn2); 
            gameOverMusic.start();
            Thread.sleep(4000);//waits 4 seconds for the game over music to play
     
            gameOverMusic.stop();
            gameOverMusic.close();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                audioIn1.close();
                audioIn2.close();
            } catch (IOException ex) {
                Logger.getLogger(MusicPlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void update() {
        synchronized (lock) {
            lock.notify(); // Notify the waiting thread
        }
    }

}
