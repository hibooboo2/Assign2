package Server;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Purpose of this class is to send songs to the client.
 * 
 * @author James Harris
 * @version November 2 2012
 */

public class FileSendThreadServer extends Thread {

	private Socket socket;
	private DataOutputStream out;
	private String fileName;

	public FileSendThreadServer(Socket socket) {
		this.socket = socket;
		try {
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
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
		if (!fileName.equalsIgnoreCase("none")) {
			File file = new File(fileName);
			System.out.print("Keep Going....");
			FileInputStream fileStream = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			while (fileStream.read(buffer) > 0) {
				out.write(buffer, 0, buffer.length);
			}
			out.close();
			fileStream.close();
		}
		else {
			out.close();
		}
		System.out.print("Done....");
		this.socket.close();
	}

}
