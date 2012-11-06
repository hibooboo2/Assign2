package Client;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Purpose of this class is to handle recieving songs from the server to play
 * them. This make the gui able to function while downloading
 * Really its ok.
 * @author James Harris
 * @version November 2 2012
 */

public class FileRecieveThreadClient extends Thread {

	private Socket socket;
	private DataInputStream in;
	private String fileName;
	private MusicApp parent;

	public FileRecieveThreadClient(Socket sock, String nodelabel,
			MusicApp parent) throws IOException {
		socket = sock;
		this.setParent(parent);
		setIn(new DataInputStream(socket.getInputStream()));

	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public DataInputStream getIn() {
		return in;
	}

	public void setIn(DataInputStream in) {
		this.in = in;
	}

	public MusicApp getParent() {
		return parent;
	}

	public void setParent(MusicApp parent) {
		this.parent = parent;
	}

	public void run() {
		try {
			download();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void download() {
		try {
			File theDir = new File(System.getProperty("user.dir") + "/Temp"+parent.getClientID()+"/" );
			boolean recieve = false;
			if (!theDir.exists()) {
				System.out.println("creating directory: "
						+ System.getProperty("user.dir") + "/Temp"+parent.getClientID()+"/" );
				theDir.mkdir();
			}
			fileName = System.getProperty("user.dir") + "/Temp"+parent.getClientID()+"/" + "temp.wav";
			FileOutputStream outStream = new FileOutputStream(fileName);
			byte[] buffer = new byte[1024];
			int size = in.read(buffer);
			while (size > 0) {
				outStream.write(buffer, 0, size);
				size = in.read(buffer);
				recieve = true;

			}
			outStream.close();
			in.close();
			socket.close();
			System.out.println("Download Successfully!");
			if (recieve) {
				parent.setPlayer(new PlayWavThread(fileName, parent));
				parent.getPlayer().start();
			}

		} catch (Exception e) {
			System.out.println("Error on downloading file!");
		}
	}

}
