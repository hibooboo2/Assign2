package Library;

import java.util.Vector;
import java.io.*;
import java.beans.*;

/**
 * A class defining xml serializable Library objects as java beans.
 * 
 * @author James Harris
 * @version November 2012
 */

public class Library implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8857783159351436853L;
	private String libTitle;
	private Vector<Album> albums;

	public String getLibTitle() {
		return libTitle;
	}

	public void setLibTitle(String libTitle) {
		this.libTitle = libTitle;
	}

	public Vector<Album> getAlbums() {
		return albums;
	}

	public void setAlbums(Vector<Album> albums) {
		this.albums = albums;
	}

	public Library() {
		this.albums = new Vector<Album>();
		albums.trimToSize();
	}

	public Library(String libTitle) {
		this.libTitle = libTitle;
		this.albums = new Vector<Album>();
		albums.trimToSize();

	}

	private void addAlbum(String album) {
		boolean added = false;
		for (Album alb : this.getAlbums()) {
			if (alb.getAlbum().equals(album)) {
				added = true;
			}
			if (added) {
				break;
			}
		}
		if (!added) {
			this.getAlbums().add(new Album(album));
			this.getAlbums().trimToSize();
		}
	}

	private void removeAlbum(String album) {
		for (Album alb : this.getAlbums()) {
			if (alb.getAlbum().equals(album)) {
				this.getAlbums().remove(alb);
				this.getAlbums().trimToSize();
				break;
			}
		}
	}

	public void addSong(String title, String author, String album, String file) {
		boolean added = false;
		if (added) {
			return;
		}
		for (Album alb : this.getAlbums()) {
			if (alb.getAlbum().equals(album)) {
				alb.addSong(title, author, file);
				added = true;
				break;
			}
		}
		if (!added) {
			this.addAlbum(album);
			this.addSong(title, author, album, file);
		}
	}

	public void addSong(String title, String author, String album) {
		addSong(title, author, album, "NONE");
	}

	public void removeSong(String title, String album) {
		boolean removed = false;
		for (Album alb : this.getAlbums()) {
			if (alb.getAlbum().equals(album)) {
				removed = alb.removeSong(title);
				if (alb.getSongs().isEmpty()) {
					this.getAlbums().remove(alb);
					this.getAlbums().trimToSize();
				}
				;
				break;
			}
		}
		if (!removed) {
			this.removeAlbum(album);
		}
	}

	public void save(String filename) {
		try {
			FileOutputStream xmlos = new FileOutputStream(filename);
			XMLEncoder encoder = new XMLEncoder(xmlos);
			encoder.writeObject(this);
			encoder.close();
			xmlos.close();
			System.out.println("Done exporting a user as xml to " + filename);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Library restore(String lib) {
		Library newLib = null;
		try {
			FileInputStream inFileStream = new FileInputStream(lib);
			XMLDecoder decoder = new XMLDecoder(inFileStream);
			newLib = (Library) decoder.readObject();
			decoder.close();
			inFileStream.close();
			System.out.println("Libloaded " + newLib.getLibTitle());
			return newLib;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return newLib;

	}

	public Song findSong(String label) {
		// checks for song with title first
		for (Album alb : this.albums) {
			for (Song son : alb.getSongs()) {
				if (son.getTitle().equals(label)) {
					System.out.println("From Find Song "+son.toString());
					return son;
				}
			}
		}
		// checks to see if its an Album
		for (Album alb : this.albums) {
			if (alb.getAlbum().equals(label)) {
				return new Song("", "", label, "");
			}
		}
		return null;

	}

	public Vector<String> getAllSongs() {
		Vector<String> songs = new Vector<String>();
		for (Album alb : albums) {
			for (Song song : alb.getSongs()) {
				songs.add(song.toString());
			}
		}
		return songs;
	}

	public void addSong(String description) {
		String[] song = description.split("\\Q$");
		addSong(song[0],song[1],song[2]);
	}

}