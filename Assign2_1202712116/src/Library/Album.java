package Library;

import java.io.Serializable;
import java.util.Vector;

/**
 * A class defining xml serializable Albums objects as java beans.
 * 
 * @author James Harris
 * @version November 2012
 */
public class Album implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9031558621886050124L;
	private String album;
	private Vector<Song> songs;

	public Vector<Song> getSongs() {
		return songs;
	}

	public void setSongs(Vector<Song> songs) {
		this.songs = songs;
	}

	public Album() {
	}

	public Album(String album) {
		this.album = album;
		this.songs = new Vector<Song>();
		songs.trimToSize();

	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public void addSong(String song, String author, String file) {
		boolean added = false;
		for (Song son : this.getSongs()) {
			if (son.getTitle().equals(song)) {
				added = true;
			}
			if (added) {
				break;
			}
		}
		if (!added) {
			this.getSongs().add(new Song(song, author, this.album, file));
			this.getSongs().trimToSize();
		}
	}

	public boolean removeSong(String song) {
		for (Song son : this.getSongs()) {
			if (son.getTitle().equals(song)) {
				this.getSongs().remove(son);
				this.getSongs().trimToSize();
				return true;
			}
		}
		return false;

	}

}
