package Client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * Purpose of this class is to allow the client 
 * to auto refresh when the library is changed by 
 * another client.
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
			Socket notifySocket = new Socket(parent.getHost(),(parent.getPort()+3));
			DataInputStream  in = new DataInputStream(notifySocket.getInputStream());
			while (!notifySocket.isClosed()) {
				if (in.readUTF().equals("notify"))
				parent.getTitleJTF().setText("notify");
				parent.treeRefresh();

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
