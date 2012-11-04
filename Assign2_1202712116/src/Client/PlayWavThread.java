package Client;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

import Extras.Popup;

/**
 * Purpose this class is used to play the music.
 * 
 * @author James Harris
 * @version November 2 2012
 */
public class PlayWavThread extends Thread {
	private String aTitle;
	private MusicApp parent;

	public PlayWavThread(String aTitle, MusicApp parent) {
		this.parent = parent;
		this.aTitle = aTitle;
	}

	public void run() {
		int BUFFER_SIZE = 4096;
		AudioInputStream audioStream;
		AudioFormat audioFormat;
		SourceDataLine sourceLine;
		try {
			Thread.sleep(200); // wait 200 milliseconds before playing the
			// file.
			new Popup("Playing the wav file: " + aTitle).start();
			audioStream = AudioSystem.getAudioInputStream(new File(aTitle));
			audioFormat = audioStream.getFormat();
			DataLine.Info i = new DataLine.Info(SourceDataLine.class,
					audioFormat);
			sourceLine = (SourceDataLine) AudioSystem.getLine(i);
			sourceLine.open(audioFormat);
			sourceLine.start();
			int nBytesRead = 0;
			byte[] abData = new byte[BUFFER_SIZE];
			while (nBytesRead != -1) {
				try {
					if (parent.isStopPlaying()) {
						System.out
								.println("Interrupted playing: " + aTitle);
						break;
					}
					nBytesRead = audioStream.read(abData, 0, abData.length);
					if (nBytesRead >= 0) {
						@SuppressWarnings("unused")
						int nBytesWritten = sourceLine.write(abData, 0,
								nBytesRead);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			sourceLine.drain();
			sourceLine.close();
			audioStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}