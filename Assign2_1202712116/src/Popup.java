import javax.swing.JOptionPane;

public class Popup extends Thread {

	private String toSay;
	public Popup(String toSay){
		this.toSay = toSay;
	}
	
	public void run(){
		JOptionPane.showMessageDialog(null,toSay);
	}
	
}
