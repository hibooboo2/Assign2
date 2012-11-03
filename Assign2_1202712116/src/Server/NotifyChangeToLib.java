package Server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

/**
 * Purpose of this class is to notify each client
 *  that the need to refresh when a change is made
 *  to the server library.
 * 
 * @author James Harris
 * @version November 2 2012
 */

public class NotifyChangeToLib extends Thread {

	private boolean refreshFlag;
	private int port;

	public boolean isRefreshFlag() {
		return refreshFlag;
	}

	public void setRefreshFlag(boolean refreshFlag) {
		this.refreshFlag = refreshFlag;
	}

	public NotifyChangeToLib() {
		this.refreshFlag = false;
	}

	public void run(){
		ServerSocket notifySocket;
		try {
			System.out.println("Try to make socket to notify");
			notifySocket = new ServerSocket(port);
			System.out.println("notify socket made");
			Socket notify =notifySocket.accept();
			notifySocket.close();
			DataOutputStream out = new DataOutputStream(notify.getOutputStream());
			while(!notify.isClosed()){
				if(isRefreshFlag()){
				out.writeUTF("refresh");
				System.out.println("notified of lib change");
				refreshFlag = false;
				}
			}
			System.out.println("Notify Thread Killed");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
