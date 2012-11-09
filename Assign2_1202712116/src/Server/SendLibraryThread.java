package Server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SendLibraryThread extends Thread {

	private ClientThread parent;
	private boolean isConnected, isSend;

	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

	public boolean isSend() {
		return isSend;
	}

	public boolean setSend(boolean send) {
		this.isSend = send;
		return isSend;
	}

	public SendLibraryThread(ClientThread parent) {
		this.parent = parent;
		isConnected = true;
		isSend = false;
	}

	public void run() {
		ServerSocket tempServer;
		try {
			tempServer = new ServerSocket(
					(parent.getPort() + 100 + parent.getClientID()));
			Socket tempSocket = tempServer.accept();
			//tempServer.close();
			DataOutputStream outPut = new DataOutputStream(
					tempSocket.getOutputStream());
			System.out.println("SongSendThread started");
			while (isConnected()) {
				// System.out.println("SongSendThread Test " + isSend);
				if (isSend) {
					System.out.println("SongSendThread Passed");
					for (String song : parent.getLib().getAllSongs()) {
						outPut.write(song.getBytes());
						outPut.flush();
					//	Thread.sleep(50);
						System.out.println("SongSendThread write " + song);
					}
					Thread.sleep(300);
					outPut.write("end".getBytes());
					outPut.flush();
					isSend = false;
				}
			}
			System.out.println("SongSendThread ended");
			tempSocket.close();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			System.out.println("SongSendThread error");
		}

	}

}
