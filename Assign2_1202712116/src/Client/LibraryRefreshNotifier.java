package Client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Purpose of this class is to allow the client to auto refresh when the library
 * is changed by another client. ?
 * 
 * @author James Harris
 * @version November 2 2012
 */
public class LibraryRefreshNotifier extends Thread {

	private MusicApp parent;

	public LibraryRefreshNotifier(MusicApp parent) {
		this.parent = parent;
	}

	public void run() {
		try {
			@SuppressWarnings("resource")
			Socket notifySocket = new Socket(parent.getHost(),
					(parent.getPort() + 3));
			DataInputStream in = new DataInputStream(
					notifySocket.getInputStream());
			byte[] bytesRead = new byte[1024];
			String read;
			int size;
			while (!notifySocket.isClosed()) {
				size = in.read(bytesRead);
				read = new String(bytesRead, 0, size);
				if (read.equals("notify")) {
					parent.treeRefresh();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
