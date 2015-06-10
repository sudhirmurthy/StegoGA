package stegoga;

import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import java.awt.GridLayout;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.TextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

import javax.swing.JSeparator;

import java.awt.TextField;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPasswordField;

public class StegoMainFrame {

	private static JFrame frmImageSteganographyUsing;
	private JTextField txtImagePath;
	private JTextField txtMsg;
	private JTextField textPitFile;
	private JTextField txtStegoImg;
	private JTextField txtpsnrOrig;
	private JTextField txtpsnrStego;
	private JFileChooser fileChooserImg;
	private JFileChooser fileChooserFil;
	private StegoEncode stegoEnc;
	private StegoDecode stegoDec;
	private JPasswordField txtPass;
	private JPasswordField txtPassword;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StegoMainFrame window = new StegoMainFrame();
					window.frmImageSteganographyUsing.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public StegoMainFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmImageSteganographyUsing = new JFrame();
		frmImageSteganographyUsing.setTitle("Image Steganography Using GA");
		frmImageSteganographyUsing.setBounds(100, 100, 718, 656);
		frmImageSteganographyUsing.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmImageSteganographyUsing.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		
		JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		frmImageSteganographyUsing.getContentPane().add(tabbedPane_1);
		
		JPanel panel_1 = new JPanel();
		tabbedPane_1.addTab("Encode", null, panel_1, null);
		panel_1.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Choose Image         :");
		lblNewLabel.setBounds(56, 11, 129, 16);
		panel_1.add(lblNewLabel);
		
		txtImagePath = new JTextField();
		txtImagePath.setEditable(false);
		txtImagePath.setBounds(211, 5, 348, 28);
		panel_1.add(txtImagePath);
		txtImagePath.setColumns(10);
		
		JLabel lblEnterPassword = new JLabel("Enter Password        :");
		lblEnterPassword.setBounds(56, 42, 131, 16);
		panel_1.add(lblEnterPassword);
		
		txtMsg = new JTextField();
		txtMsg.setEditable(false);
		txtMsg.setBounds(212, 68, 347, 28);
		panel_1.add(txtMsg);
		txtMsg.setColumns(10);
		
		JLabel lblEnterTextTo = new JLabel("Choose File to hide  :");
		lblEnterTextTo.setBounds(56, 74, 133, 16);
		panel_1.add(lblEnterTextTo);
		
		JLabel lblOriginal = new JLabel("");
		lblOriginal.setHorizontalAlignment(SwingConstants.CENTER);
		lblOriginal.setBounds(58, 155, 172, 154);
		panel_1.add(lblOriginal);
		
		JLabel lblStego = new JLabel("");
		lblStego.setHorizontalAlignment(SwingConstants.CENTER);
		lblStego.setBounds(381, 155, 178, 154);
		panel_1.add(lblStego);
		
		JLabel lblOriginalImage = new JLabel("Original Image");
		lblOriginalImage.setBounds(58, 321, 110, 16);
		panel_1.add(lblOriginalImage);
		
		JLabel lblStegoImage = new JLabel("Stego Image");
		lblStegoImage.setBounds(381, 319, 128, 16);
		panel_1.add(lblStegoImage);
		
		TextArea logEncode = new TextArea();
		logEncode.setForeground(Color.BLACK);
		logEncode.setBackground(Color.WHITE);
		logEncode.setBounds(16, 349, 671, 227);
		panel_1.add(logEncode);
				
		JButton btnBrowseFile = new JButton("Browse File..");
		btnBrowseFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileChooserFil = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Text and Bitmaps", "txt","bmp");
				fileChooserFil.setFileFilter(filter);
				int returnVal = fileChooserFil.showOpenDialog(btnBrowseFile);
				//returnVal - 0 is success
				//returnVal - 1 is cancelled by user
				if(returnVal==0){
					File txtFile = fileChooserFil.getSelectedFile();
					txtMsg.setText(txtFile.getAbsolutePath());
				}
				
			}
		});
		btnBrowseFile.setBounds(558, 68, 129, 29);
		panel_1.add(btnBrowseFile);
				
		JButton btnBrowseImage = new JButton("Browse Image..");
		btnBrowseImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileChooserImg = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("bmp Files", "bmp");
				fileChooserImg.setFileFilter(filter);
				int returnVal = fileChooserImg.showOpenDialog(btnBrowseImage);
				//returnVal - 0 is success
				//returnVal - 1 is cancelled by user
				if(returnVal==0){
					File imgFile = fileChooserImg.getSelectedFile();
					txtImagePath.setText(imgFile.getAbsolutePath());
				}
			}
		});
		
		btnBrowseImage.setBounds(558, 6, 129, 29);
		panel_1.add(btnBrowseImage);
		
		JLabel lblPasswordLengthShould = new JLabel("Password length should be atleast 16 characters.");
		lblPasswordLengthShould.setBounds(371, 45, 316, 16);
		panel_1.add(lblPasswordLengthShould);
		
		txtPass = new JPasswordField();
		txtPass.setBounds(212, 36, 159, 28);
		panel_1.add(txtPass);
		
		
		
		JButton btnEncode = new JButton("1. Encode and Read Image");
		btnEncode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String password = new String(txtPass.getPassword());
				stegoEnc = new StegoEncode(txtImagePath.getText(),password, txtMsg.getText(),lblOriginal,lblStego,logEncode);
				stegoEnc.Encrypt();
			}
		});
		
		btnEncode.setBounds(211, 106, 208, 29);
		panel_1.add(btnEncode);
				
		JButton btnStartGa = new JButton("2. Start GA");
		btnStartGa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					stegoEnc.DoGA();
				} catch (IOException e1) {
					logEncode.append(e1.getStackTrace().toString());					
				}
			}
		});
		btnStartGa.setBounds(414, 106, 149, 29);
		panel_1.add(btnStartGa);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(2, 335, 685, 16);
		panel_1.add(separator_2);
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setBounds(6, 133, 685, 16);
		panel_1.add(separator_3);
		
		JPanel panel_2 = new JPanel();
		tabbedPane_1.addTab("Decode", null, panel_2, null);
		panel_2.setLayout(null);
		
		JLabel lblEnterThePath = new JLabel("Enter the path to the PIT file");
		lblEnterThePath.setBounds(17, 19, 215, 16);
		panel_2.add(lblEnterThePath);
		
		JLabel lblEnterThePath_1 = new JLabel("Enter the path to the Stego Image");
		lblEnterThePath_1.setBounds(17, 46, 227, 16);
		panel_2.add(lblEnterThePath_1);
		
		JLabel lblEnterPassword_1 = new JLabel("Enter Password");
		lblEnterPassword_1.setBounds(17, 74, 120, 16);
		panel_2.add(lblEnterPassword_1);
		
		textPitFile = new JTextField();
		textPitFile.setEditable(false);
		textPitFile.setBounds(244, 13, 358, 28);
		panel_2.add(textPitFile);
		textPitFile.setColumns(10);
		
		txtStegoImg = new JTextField();
		txtStegoImg.setEditable(false);
		txtStegoImg.setColumns(10);
		txtStegoImg.setBounds(244, 40, 358, 28);
		panel_2.add(txtStegoImg);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(6, 120, 691, 12);
		panel_2.add(separator);
		
		TextArea txtDecodeLog = new TextArea();
		txtDecodeLog.setBounds(16, 135, 669, 443);
		panel_2.add(txtDecodeLog);
		
		JButton btnBrowsePit = new JButton("Browse..");
		btnBrowsePit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//txtPitFile.txt
				fileChooserImg = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("pit Files", "csv");
				fileChooserImg.setFileFilter(filter);
				int returnVal = fileChooserImg.showOpenDialog(btnBrowsePit);
				//returnVal - 0 is success
				//returnVal - 1 is cancelled by user
				if(returnVal==0){
					File imgFile = fileChooserImg.getSelectedFile();
					textPitFile.setText(imgFile.getAbsolutePath());
				}
			}
		});
		btnBrowsePit.setBounds(604, 13, 87, 29);
		panel_2.add(btnBrowsePit);
		
		JButton btnBrowseStego = new JButton("Browse..");
		btnBrowseStego.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//txtStegoImg.txt
				fileChooserImg = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("bmp Files", "bmp");
				fileChooserImg.setFileFilter(filter);
				int returnVal = fileChooserImg.showOpenDialog(btnBrowsePit);
				//returnVal - 0 is success
				//returnVal - 1 is cancelled by user
				if(returnVal==0){
					File imgFile = fileChooserImg.getSelectedFile();
					txtStegoImg.setText(imgFile.getAbsolutePath());
				}
			}
		});
		btnBrowseStego.setBounds(604, 41, 87, 29);
		panel_2.add(btnBrowseStego);
	
		JButton btnFetchMsgFrom = new JButton("Decode");
		btnFetchMsgFrom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String pass = new String(txtPassword.getPassword());
				stegoDec = new StegoDecode(textPitFile.getText(), txtStegoImg.getText(), pass,txtDecodeLog);
				try {
					stegoDec.Decode();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnFetchMsgFrom.setBounds(240, 93, 367, 29);
		panel_2.add(btnFetchMsgFrom);
		
		txtPassword = new JPasswordField();
		txtPassword.setBounds(244, 68, 358, 28);
		panel_2.add(txtPassword);
		
		
		JPanel panel_3 = new JPanel();
		tabbedPane_1.addTab("Test", null, panel_3, null);
		panel_3.setLayout(null);
		
		JLabel lblPathToSource = new JLabel("Path to Original Image");
		lblPathToSource.setBounds(54, 32, 152, 16);
		panel_3.add(lblPathToSource);
		
		JLabel label_1 = new JLabel("Path to Stego Image");
		label_1.setBounds(54, 60, 152, 16);
		panel_3.add(label_1);
		
		txtpsnrOrig = new JTextField();
		txtpsnrOrig.setEditable(false);
		txtpsnrOrig.setBounds(218, 26, 355, 28);
		panel_3.add(txtpsnrOrig);
		txtpsnrOrig.setColumns(10);
		
		txtpsnrStego = new JTextField();
		txtpsnrStego.setEditable(false);
		txtpsnrStego.setColumns(10);
		txtpsnrStego.setBounds(218, 54, 355, 28);
		panel_3.add(txtpsnrStego);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(6, 115, 685, 16);
		panel_3.add(separator_1);
		
		TextArea txtpsnrlog = new TextArea();
		txtpsnrlog.setBackground(Color.WHITE);
		txtpsnrlog.setBounds(16, 131, 671, 447);
		panel_3.add(txtpsnrlog);
		
		JButton btnBrowseOrigImg = new JButton("Browse..");
		btnBrowseOrigImg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileChooserImg = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("bmp Files", "bmp");
				fileChooserImg.setFileFilter(filter);
				int returnVal = fileChooserImg.showOpenDialog(btnBrowseImage);
				//returnVal - 0 is success
				//returnVal - 1 is cancelled by user
				if(returnVal==0){
					File imgFile = fileChooserImg.getSelectedFile();
					txtpsnrOrig.setText(imgFile.getAbsolutePath());
				}
			}
		});
		btnBrowseOrigImg.setBounds(576, 26, 117, 29);
		panel_3.add(btnBrowseOrigImg);
		
		JButton btnBrowseStegoImg = new JButton("Browse..");
		btnBrowseStegoImg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileChooserImg = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("bmp Files", "bmp");
				fileChooserImg.setFileFilter(filter);
				int returnVal = fileChooserImg.showOpenDialog(btnBrowseImage);
				//returnVal - 0 is success
				//returnVal - 1 is cancelled by user
				if(returnVal==0){
					File imgFile = fileChooserImg.getSelectedFile();
					txtpsnrStego.setText(imgFile.getAbsolutePath());
				}
			}
		});
		btnBrowseStegoImg.setBounds(576, 53, 117, 29);
		panel_3.add(btnBrowseStegoImg);
		
		JButton btnGenerateTests = new JButton("Generate Tests");
		btnGenerateTests.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PSNR p = new PSNR(txtpsnrOrig.getText(),txtpsnrStego.getText());
				p.calculatePsnr();
				txtpsnrlog.append(p.getLog());
			}
		});
		btnGenerateTests.setBounds(216, 83, 361, 29);
		panel_3.add(btnGenerateTests);
		
		frmImageSteganographyUsing.getContentPane().setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{tabbedPane_1}));
				
	}
}
