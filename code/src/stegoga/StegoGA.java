package stegoga;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javafx.stage.FileChooser;
import javax.swing.JFileChooser;
import java.awt.BorderLayout;

public class StegoGA {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StegoGA window = new StegoGA();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public StegoGA() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 606, 472);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setBounds(23, 17, 550, 286);
		frame.getContentPane().add(fileChooser);
	}
}
