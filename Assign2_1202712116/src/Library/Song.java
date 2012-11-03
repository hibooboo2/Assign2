package Library;

import java.io.Serializable;

/**
 * A class defining xml serializable Song objects as java beans.
 * @author James Harris	
 * @version November 2012
 */
public class Song implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4730522003518557806L;
	private String title,author;
	private String album,file;

	public Song(){
	}

	public Song(String title, String author, String album,String file) {
		this.title = title;
		this.author = author;
		this.setAlbum(album);
		this.file=file;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
}
