package Server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.swing.JOptionPane;

import Library.Library;

public class MakeMockLibrary {

	public int foldernumber = 0;
	public int filenumber = 0;
	public String baseStart;
	private Writer outTxt;

	public MakeMockLibrary(String base, Writer output) {
		baseStart = base;
		outTxt = output;
	}

	public void listAllFiles(String path, String ext, int i) throws IOException {
		// String files;
		if (i == 2)
			return;
		String fullext = "." + ext;
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		if (listOfFiles != null) {
			for (File file : listOfFiles) {
				if (!file.isFile()) {
					foldernumber++;
					listAllFiles(file.getAbsolutePath(), ext, i);

				}
				if (file.isFile() && file.getName().contains(fullext) && i == 0
						&& file.getAbsolutePath().contains(ext.toUpperCase())) {
					filenumber++;
					String baseSort = baseStart + "/" + ext.toUpperCase();
					String newLoc = baseSort
							+ file.getParent().substring(baseStart.length(),
									file.getParent().length()) + "\\";
					makeNewDir(newLoc);
					moveFile(file.getAbsolutePath(), baseStart);
				}
				if (file.isFile() && i == 1 && file.getName().contains(fullext)) {
					outTxt.write("\n" + file.getAbsolutePath());
				}
			}
		}
	}

	private void makeNewDir(String newLoc) throws IOException {
		File theDir = new File(newLoc);
		if (!theDir.exists()) {
			if (theDir.mkdirs()) {
				outTxt.write("\n" + "Created directory: " + newLoc);
			} else {
				outTxt.write("\n" + "Failed to create directory: " + newLoc);
			}
		}

	}

	public void moveFile(String file, String newLoc) {
		try {

			File afile = new File(file);
			if (afile.renameTo(new File(newLoc + afile.getName()))) {
				outTxt.write("\n" + "File is moved successful!");
			} else {
				outTxt.write("\n" + "File is failed to move!");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// public static void makeMockLib() {
	// String liblocation = "D:/MEDIA/MUSIC/WAV/";
	// File theDir = new File(liblocation);
	// if (!theDir.exists()) {
	// System.out.println("creating directory: " + liblocation);
	// theDir.mkdir();
	// }
	// Library lib = new Library("serverLib");
	// lib.addSong("Song 1", "Sweet", "Album 1");
	// lib.addSong("Song 2", "Coolio", "Album 1");
	// lib.addSong("Song 3", "Cool", "Album 2");
	// lib.addSong("Song 4", "Cool", "Album 2");
	// lib.addSong("Song 5", "Cool", "Album 3");
	// lib.save(liblocation + "serverLib.xml");
	// lib = lib.restore(liblocation + "serverLib.xml");
	// }

	public static void main(String noargs[]) throws IOException {
		Writer output = null;
		File file = new File("myFiles.txt");
		JOptionPane
				.showMessageDialog(
						null,
						"Choose a folder of path in windows. If "
								+ "you hit no then it will list the files of "
								+ "the extension given. But if no ext given lists all files.");
		output = new BufferedWriter(new FileWriter(file));
		String chosen = JOptionPane.showInputDialog(null,
				"What folder do you want to sort?", null);
		String ext = JOptionPane.showInputDialog(null,
				"What folder file extension", null);
		int choice = JOptionPane.showConfirmDialog(null, "Move Files?");
		output.write("Chose path " + chosen + "to sort.\n Files were ");
		if (choice == 1) {
			output.write("not moved and listed to file.");
		} else {
			output.write("moved to " + chosen + "/" + ext.toUpperCase() + "/");
		}
		MakeMockLibrary test = new MakeMockLibrary(chosen, output);
		test.listAllFiles(test.baseStart, ext, choice);
		output.write("\n Folders " + test.foldernumber + "Files "
				+ test.filenumber);
		output.close();
		JOptionPane.showInputDialog(null, "DONE", null);
	}
}
