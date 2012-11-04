package Server;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JOptionPane;

import Library.Album;
import Library.Library;
import Library.Song;

/**
 * Purpose of this class is to server as the main server that listens for new
 * connections from clients.
 * 
 * 
 * @author James Harris
 * @version November 2 2012
 */

public class ConnectionListener extends Thread {
	
	public void run(){
		JOptionPane.showMessageDialog(null, "Close this box to close the server.");
		System.exit(0);
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
		File theDir = new File(System.getProperty("user.dir") + "/Library/");
		if (!theDir.exists()) {
			System.out.println("creating directory: "
					+ System.getProperty("user.dir") + "/Library/");
			theDir.mkdir();
		}
		System.out.println("Server Started. Listening For connections...");
		int id = 0;
		try {

			Library lib = new Library();
			File theLib = new File(System.getProperty("user.dir") + "/Library/"
					+ "serverLib.xml");
			if (!theLib.exists()) {
				System.out.println("creating Library: "
						+ System.getProperty("user.dir") + "/Library/"
						+ "serverLib.xml");
				lib.setLibTitle("serverLib");
				lib.addSong("TEST", "TEST AUTHOR", "ALBUM", "NONE");
				lib.save(System.getProperty("user.dir") + "/Library/"
						+ "serverLib.xml");
			}
			lib = lib.restore(System.getProperty("user.dir") + "/Library/"
					+ "serverLib.xml");
			ServerSocket serv = new ServerSocket(portNo);
			System.out.println(portNo);
			Vector<ClientThread> clients = new Vector<ClientThread>();
			new ConnectionListener().start();
			while (true) {
				System.out
						.println("Threaded server waiting for connects on port "
								+ portNo);
				clients.add(new ClientThread(serv.accept(), id++, lib, clients,
						portNo));
				clients.lastElement().start();
				System.out.println("Threaded server connected to client-" + id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
