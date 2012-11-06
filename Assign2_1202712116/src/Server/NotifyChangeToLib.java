package Server;

import java.io.DataOutputStream;
import java.io.IOException;

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
	private boolean connected;
	private DataOutputStream out;

	public boolean isRefreshFlag() {
		return refreshFlag;
	}

	public void setRefreshFlag(boolean refreshFlag) {
		this.refreshFlag = refreshFlag;
		
	}

	public NotifyChangeToLib(DataOutputStream out) {
		this.refreshFlag = false;
		this.connected = true;
		this.out = out;
		
	}

	public void run(){
		try {
			while(connected){
				if(isRefreshFlag()){
				System.out.println("Enter");
				out.write("notify".getBytes());
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

	public void setConnected(boolean connected) {
		this.connected = connected;
		
	}
}
