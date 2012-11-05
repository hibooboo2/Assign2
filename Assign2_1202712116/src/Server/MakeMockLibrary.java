package Server;

import java.io.File;

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
		lib.addSong("Song 1", "Sweet", "Album 1");
		lib.addSong("Song 2", "Coolio", "Album 1");
		lib.addSong("Song 3", "Cool", "Album 2");
		lib.addSong("Song 4", "Cool", "Album 2");
		lib.addSong("Song 5", "Cool", "Album 3");
		lib.save(System.getProperty("user.dir") + "/Library/" + "serverLib.xml");
		lib = lib.restore(System.getProperty("user.dir") + "/Library/"
				+ "serverLib.xml");
	}
}