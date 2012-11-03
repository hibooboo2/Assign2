package Client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import Library.Album;
import Library.Library;
import Library.Song;
import cst420.media.MusicLibraryGui;

/**
 * Purpose is to serve as the main gui and flow of control for the app.
 * Test
 * @author James Harris
 * @version November 2 2012
 */
@SuppressWarnings("serial")
public class MusicApp extends MusicLibraryGui implements
		TreeWillExpandListener, ActionListener, TreeSelectionListener {

	private Library mainLibrary;
	private PlayWavThread player = null;
	private boolean stopPlaying;
	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	private FileSendThreadClient fileUploader;
	private String host;
	private LibraryRefreshNotifier treeRefresher;
	private int port;

	public MusicApp(String base) {
		super(base);
		try {
			host = JOptionPane.showInputDialog(this, "What is the server ip?",
					"localhost");
			port = Integer.parseInt(JOptionPane.showInputDialog(this,
					"What is the server port?", "8888"));
			socket = new Socket(host, port);
			Thread.sleep(200);
			treeRefresher = new LibraryRefreshNotifier(this);
			treeRefresher.start();
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			setStopPlaying(false);
			this.mainLibrary = new Library(System.getProperty("user.name"));
			for (int i = 0; i < userMenuItems.length; i++) {
				for (int j = 0; j < userMenuItems[i].length; j++) {
					userMenuItems[i][j].addActionListener(this);
				}
			}
			tree.addTreeSelectionListener(this);
			tree.addTreeWillExpandListener(this);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			treeRefresh();
			setVisible(true);

		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void getLibrary() throws UnknownHostException, IOException,
			InterruptedException {
		out.writeUTF("getLibrary");
		Thread.sleep(200);
		Socket libSocket = new Socket(host, (port + 4));
		DataInputStream inPut = new DataInputStream(libSocket.getInputStream());
		File theDir = new File(System.getProperty("user.dir") + "/Temp/");
		if (!theDir.exists()) {
			System.out.println("creating directory: "
					+ System.getProperty("user.dir") + "/Temp/");
			theDir.mkdir();
		}
		String fileName = System.getProperty("user.dir") + "/Temp/" + "lib.xml";
		FileOutputStream outStream = new FileOutputStream(fileName);
		byte[] buffer = new byte[1024];
		int size = inPut.read(buffer);
		while (size > 0) {
			outStream.write(buffer, 0, size);
			size = inPut.read(buffer);
			System.out.println("Reading");
		}
		outStream.close();
		libSocket.close();
		System.out.println("Download Successfully!");
		this.mainLibrary = new Library();
		this.mainLibrary = mainLibrary.restore(fileName);

	}

	public void treeRefresh() throws UnknownHostException, IOException,
			InterruptedException {
		getLibrary();
		tree.removeTreeSelectionListener(this);
		tree.removeTreeWillExpandListener(this);
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		clearTree(root, model);
		int pos = 0;
		for (Album alb : this.mainLibrary.getAlbums()) {
			model.insertNodeInto(new DefaultMutableTreeNode(alb.getAlbum()),
					root, model.getChildCount(root));
			for (Song son : alb.getSongs()) {
				model.insertNodeInto(
						new DefaultMutableTreeNode(son.getTitle()),
						(MutableTreeNode) root.getChildAt(pos),
						model.getChildCount(root.getChildAt(pos)));
			}
			pos++;
		}
		for (int r = 0; r < tree.getRowCount(); r++) {
			tree.expandRow(r);
		}
		tree.addTreeSelectionListener(this);
		tree.addTreeWillExpandListener(this);

	}

	public boolean isStopPlaying() {
		return stopPlaying;
	}

	public void setStopPlaying(boolean stopPlaying) {
		this.stopPlaying = stopPlaying;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public LibraryRefreshNotifier getTreeRefresher() {
		return treeRefresher;
	}

	public void setTreeRefresher(LibraryRefreshNotifier treeRefresher) {
		this.treeRefresher = treeRefresher;
	}

	public DataInputStream getIn() {
		return in;
	}

	public void setIn(DataInputStream in) {
		this.in = in;
	}

	public JTextField getTitleJTF() {
		return this.titleJTF;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public DataOutputStream getOut() {
		return out;
	}

	public PlayWavThread getPlayer() {
		return player;
	}

	public void setPlayer(PlayWavThread player) {
		this.player = player;
	}

	public void setOut(DataOutputStream out) {
		this.out = out;
	}

	public FileSendThreadClient getFileHandler() {
		return fileUploader;
	}

	public void setFileHandler(FileSendThreadClient fileHandler) {
		this.fileUploader = fileHandler;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public void treeWillCollapse(TreeExpansionEvent tee) {
		tree.setSelectionPath(tee.getPath());
	}

	public void treeWillExpand(TreeExpansionEvent tee) {
		DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) tee.getPath()
				.getLastPathComponent();
		System.out.println("will expand node: " + dmtn.getUserObject()
				+ " whose path is: " + tee.getPath());
	}

	// This method makes fields update when a node is clicked on
	public void valueChanged(TreeSelectionEvent e) {
		try {
			tree.removeTreeSelectionListener(this);
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
					.getLastSelectedPathComponent();
			String nodeLabel = (String) node.getUserObject();
			System.out.println("Selected node labelled: " + nodeLabel);
			setFields(nodeLabel);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		tree.addTreeSelectionListener(this);
	}

	// This function is to set fields according to a song object
	private void setFields(String label) {
		Song song = this.mainLibrary.findSong(label);
		if (!(song == null)) {
			this.titleJTF.setText(song.getTitle());
			this.albumJTF.setText(song.getAlbum());
			this.authorJTF.setText(song.getAuthor());
		} else {
			// This is so if a song had be selected and the main lirary nod is
			// selected the fields are cleared.
			this.titleJTF.setText("");
			this.albumJTF.setText("");
			this.authorJTF.setText("");
		}

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Exit")) {
			try {
				out.writeUTF("exit");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.exit(0);
			// The following is to Serialize a library when pressing save.
		} else if (e.getActionCommand().equals("Save")) {
			try {
				System.out.println("Save Selected");
				out.writeUTF("save");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		// The following is to Deserialize a library when pressing restore.
		else if (e.getActionCommand().equals("Restore")) {
			try {
				System.out.println("Restore selected, initializing tree");
				out.writeUTF("restore");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		// The following is what adds a song to the library it then rebuilds the
		// tree
		else if (e.getActionCommand().equals("Add")) {
			try {
				System.out.println("Add Selected");
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File(System
						.getProperty("user.dir")));
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"Wav files", "wav");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					System.out.println("You selected the file: "
							+ chooser.getSelectedFile().getAbsolutePath());
					String file = chooser.getSelectedFile().getAbsolutePath();
					out.writeUTF("add");
					out.writeUTF(this.titleJTF.getText());
					out.writeUTF(this.authorJTF.getText());
					out.writeUTF(this.albumJTF.getText());
					this.fileUploader = new FileSendThreadClient(file, this, new Socket(host, (port+1)));
					this.fileUploader.start();
				}

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		// The following is what removes a song or album from the library
		else if (e.getActionCommand().equals("Remove")) {
			System.out.println("Remove Selected");
			try {
				out.writeUTF("remove");
				out.writeUTF(this.titleJTF.getText());
				out.writeUTF(this.albumJTF.getText());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		// Plays the Selected Song
		else if (e.getActionCommand().equals("Play")) {
			try {
				System.out.println("Play Selected");
				// get the currently selected node in the tree.
				// if the user hasn't already selected a node for which
				// there must be a wav file then exit ungracefully!
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
						.getLastSelectedPathComponent();
				String nodeLabel = (String) node.getUserObject();
				if (getPlayer() != null && getPlayer().isAlive()) {
					System.out
							.println("Already playing: Interrupting the thread");
					stopPlaying = true;
					Thread.sleep(500); // give the thread time to complete
					stopPlaying = false;
				}
				if (!(this.mainLibrary.findSong(nodeLabel).getFile().equals(""))) {
					out.writeUTF("play");
					out.writeUTF(nodeLabel);
					FileRecieveThreadClient fileRecieveSong = new FileRecieveThreadClient(
							new Socket(host, port + 2), nodeLabel, this);
					fileRecieveSong.start();
				}
			} catch (InterruptedException | IOException ex) { // sleep may throw
																// this
				// exception
				System.out.println("MusicThread sleep was interrupted.");
				ex.printStackTrace();
			}
		}
		// Refresh the tree
		else if (e.getActionCommand().equals("Tree Refresh")) {
			System.out.println("Tree Refresh Selected");
			try {
				treeRefresh();
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

	}

	private void clearTree(DefaultMutableTreeNode root, DefaultTreeModel model) {
		tree.removeTreeSelectionListener(this);
		tree.removeTreeWillExpandListener(this);
		try {
			DefaultMutableTreeNode next = null;
			int subs = model.getChildCount(root);
			for (int k = subs - 1; k >= 0; k--) {
				next = (DefaultMutableTreeNode) model.getChild(root, k);
				model.removeNodeFromParent(next);
			}
		} catch (Exception ex) {
			System.out.println("Exception while trying to clear tree:");
			ex.printStackTrace();
		}
		tree.addTreeSelectionListener(this);
		tree.addTreeWillExpandListener(this);
	}

	@SuppressWarnings("unused")
	public static void main(String args[]) {
		try {
			String name = "Music Library";
			if (args.length >= 1) {
				name = args[0];
			}
			MusicApp ltree = new MusicApp(name);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
