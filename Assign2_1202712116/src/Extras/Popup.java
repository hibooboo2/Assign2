package Extras;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Popup extends Thread {

	private String toSay;

	public Popup() {
		toSay = "BLANK";
	}

	public Popup(String toSay) {
		this.toSay = toSay;
	}

	public void run() {
		JOptionPane.showMessageDialog(new JFrame(), toSay);
	}

	public static void main(String noargs[]) throws InterruptedException {
		Popup test = new Popup();
		for (int i = 0; i < Integer.parseInt(noargs[0]); i++) {
			test.start();
			noargs[0] = (Integer.toString(Integer.parseInt(noargs[0]) - 1));
			// test.main(noargs);
			System.out.println("Thread died!");
			// Thread.sleep(5000);
		}
		JTextField xField = new JTextField(5);
		JTextField yField = new JTextField(5);

		JPanel myPanel = new JPanel();
		myPanel.add(new JLabel("x:"));
		myPanel.add(xField);
		myPanel.add(Box.createHorizontalStrut(15)); // a spacer
		myPanel.add(new JLabel("y:"));
		myPanel.add(yField);

		int result = JOptionPane.showConfirmDialog(null, myPanel,
				"Please Enter X and Y Values", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			System.out.println("x value: " + xField.getText());
			System.out.println("y value: " + yField.getText());
		}

	}

}
