package Client;

import java.io.IOException;

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
			byte[] bytesRead = new byte[1024];
			String read;
			int size;
			while (!parent.getSocket().isClosed()) {
				size = parent.getIn().read(bytesRead);
				read = new String(bytesRead, 0, size);
				if (read.equalsIgnoreCase("notify")) {
					parent.treeRefresh();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
