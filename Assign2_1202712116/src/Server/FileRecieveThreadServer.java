package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.Socket;

import Library.Library;

/**
 * Purpose of this class is to receive the songs from a client.
 * 
 * @author James Harris
 * @version November 2 2012
 */

public class FileRecieveThreadServer extends Thread {

	private Socket socket;
	private DataInputStream in;
	private int idForClient;
	private Library userLibrary;
	private String title;
	private ClientThread parent;

	public FileRecieveThreadServer(Socket sock, String title, String author, String album, Library lib, ClientThread parent) {
		socket = sock;
		userLibrary = lib;
		lib.addSong(title, author, album, "");
		this.setTitle(title);
		this.setParent(parent);

	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ClientThread getParent() {
		return parent;
	}

	public void setParent(ClientThread parent) {
		this.parent = parent;
	}

	public int getIdForClient() {
		return idForClient;
	}

	public void setIdForClient(int idForClient) {
		this.idForClient = idForClient;
	}

	public DataInputStream getIn() {
		return in;
	}

	public void setIn(DataInputStream in) {
		this.in = in;
	}

	public Library getUserLibrary() {
		return userLibrary;
	}

	public void setUserLibrary(Library userLibrary) {
		this.userLibrary = userLibrary;
	}

	public void run()  {
		try {
			in = new DataInputStream(socket.getInputStream());
			download();
			in.close();
			socket.close();
			parent.changeNotify();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void download() {
		try {
			File theDir = new File(System.getProperty("user.dir") + "/Library/");
			if (!theDir.exists()) {
				System.out.println("creating directory: " + System.getProperty("user.dir") + "/Library/");
				theDir.mkdir();
			}
			String fileName = System.getProperty("user.dir") + "/Library/" + title+ ".wav";
			FileOutputStream outStream = new FileOutputStream(fileName);
			byte[] buffer = new byte[1024];
			int size = in.read(buffer);
			while (size > 0) {
				outStream.write(buffer, 0, size);
				size = in.read(buffer);
			}
			outStream.close();
			System.out.println("Download Successfully!");
			userLibrary.findSong(title).setFile(fileName);
			userLibrary.save(System.getProperty("user.dir") + "/Library/" + "serverLib.xml");

		} catch (Exception e) {
			System.out.println("Error on downloading file!");
		}
	}

}
