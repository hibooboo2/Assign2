package Server;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JOptionPane;

import Library.Library;

/**
 * Purpose of this class is to server as the main server that listens for new
 * connections from clients.
 * 
 * @author James Harris
 * @version November 2 2012
 */

public class ConnectionListener extends Thread {

	public void run() {
		JOptionPane.showMessageDialog(null,
				"Close this box to close the server.");
		System.exit(0);
	}
	
	 public static void makeMockLib() {
		 String liblocation = System.getProperty("user.dir") + "/Library/";
		 File theDir = new File(liblocation);
		 if (!theDir.exists()) {
		 System.out.println("creating directory: " + liblocation);
		 theDir.mkdir();
		 }
		 Library lib = new Library("serverLib");
		 lib.addSong("Song 1", "Sweet", "Album 1");
		 lib.addSong("Song 2", "Coolio", "Album 1");
		 lib.addSong("Song 3", "Cool", "Album 2");
		 lib.addSong("Song 4", "Cool", "Album 2");
		 lib.addSong("Song 5", "Cool", "Album 3");
		 lib.save(liblocation + "serverLib.xml");
		 }

	@SuppressWarnings("resource")
	public static void main(String args[]) {
		int portNo;
		if (args.length >= 1) {
			portNo = Integer.parseInt(args[0]);
		} else {
			portNo = Integer.parseInt(JOptionPane.showInputDialog(null,
					"What port do you want to use?", "8888"));
		}
		System.out.println("Server Started. Listening For connections...");
		try {

			Library lib = new Library();
//			File theLib = new File(System.getProperty("user.dir") + "/Library/"
//					+ "serverLib.xml");
//			if (!theLib.exists()) {
//				System.out.println("creating Library: "
//						+ System.getProperty("user.dir") + "/Library/"
//						+ "serverLib.xml");
//				makeMockLib();
//			}
			makeMockLib();
			lib = lib.restore(System.getProperty("user.dir") + "/Library/"
					+ "serverLib.xml");
			ServerSocket serv = new ServerSocket(portNo);
			Vector<ClientThread> clients = new Vector<ClientThread>();
			clients.trimToSize();
			new ConnectionListener().start();
			int id = 0;
			while (true) {
				System.out.println("Threaded server waiting"
						+ " for connects on port " + portNo);
				Socket socket = serv.accept();
				ClientThread client = new ClientThread(socket, lib, clients,
						portNo, id);
				id++;
				clients.addElement(client);
				client.start();
				System.out.println("Threaded server connected to client-");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
