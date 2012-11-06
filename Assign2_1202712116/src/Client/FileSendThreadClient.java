package Client;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import Extras.Popup;

/**
 * Purpose of this class is to handle sending songs to the server to store them.
 * This make the gui able to function while uploading
 * 
 * @author James Harris
 * @version November 2 2012
 */

public class FileSendThreadClient extends Thread {

	private Socket socket;
	private DataOutputStream out;
	private String fileName;
	private MusicApp parent;

	public FileSendThreadClient(String file, MusicApp parent, Socket socket) {

		try {
			this.fileName = file;
			this.setParent(parent);
			this.socket = socket;
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public MusicApp getParent() {
		return parent;
	}

	public void setParent(MusicApp parent) {
		this.parent = parent;
	}

	public DataOutputStream getOut() {
		return out;
	}

	public void setOut(DataOutputStream out) {
		this.out = out;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void run() {
		try {
			streamFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void streamFile() throws IOException {
		File file = new File(fileName);
		System.out.print("Keep Going....");
		FileInputStream fileStream = new FileInputStream(file);
		byte[] buffer = new byte[4 * 1024];
		int size;
		while (true) {
			System.out.println("Before =");
			size = fileStream.read(buffer);
			if (!(size > 0)) {
				break;
			}
			out.write(buffer, 0, size);
			System.out.print(" =After ");
		}
		fileStream.close();
		this.socket.close();
		System.out.print("Done....");

	}

}
