package Server;

import java.io.File;

import Client.Popup;
import Library.Library;

public class MakeMockLibrary {

	public static void main(String noargs[]) {
		File theDir = new File(System.getProperty("user.dir") + "/Library/");
		if (!theDir.exists()) {
			System.out.println("creating directory: "
					+ System.getProperty("user.dir") + "/Library/");
			theDir.mkdir();
		}
		Library lib = new Library("serverLib");
		lib.addSong("Song 1", "Sweet", "Album 1", "NONE");
		lib.addSong("Song 2", "Coolio", "Album 1", "NONE");
		lib.addSong("Song 3", "Cool", "Album 2", "NONE");
		lib.addSong("Song 4", "Cool", "Album 2", "NONE");
		lib.addSong("Song 5", "Cool", "Album 3", "NONE");
		lib.save(System.getProperty("user.dir") + "/Library/" + "serverLib.xml");
		lib = lib.restore(System.getProperty("user.dir") + "/Library/"
				+ "serverLib.xml");
		new Popup("LiBRARY MADE").start();
	}
}