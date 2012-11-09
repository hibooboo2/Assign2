package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import Library.Library;

public class RecieveLibraryThread extends Thread {
	private MusicApp parent;
	private boolean isConnected, isRecieve;
	private DataInputStream inPut;

	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

	public boolean isRecieve() {
		return isRecieve;
	}

	public void setRecieve(boolean isRecieve) {
		this.isRecieve = isRecieve;
	}

	public RecieveLibraryThread(MusicApp parent) {
		this.parent = parent;
		isConnected = true;
		isRecieve = false;
	}

	public void run() {
		try {
			parent.getOut().write("getSongs".getBytes());
			Thread.sleep(200);
			Socket tempSocket = new Socket(parent.getHost(), parent.getPort()
					+ 100 + parent.getClientID());
			inPut = new DataInputStream(
					tempSocket.getInputStream());
			while (isConnected()) {
				if (isRecieve()) {
					
				}
			}
			tempSocket.close();

		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void songs(Library lib) throws IOException{
		byte[] bytestoRecieve = new byte[1024];
		int size = inPut.read(bytestoRecieve);
		String songs = new String();
		Library tempLib = new Library("Temp");
		while (size > 0) {
			songs += new String(bytestoRecieve, 0, size);
			size = inPut.read(bytestoRecieve);
		}
		System.out.println(songs);
		String[] songsSeperated = songs.split("\\Q#");
		for (String song : songsSeperated) {
			tempLib.addSong(song);
		}
		lib = tempLib;
	}
}
