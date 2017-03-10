package com.iceq.gui;

import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.iceq.springdata.controller.TestService;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainWindow {
	private ClassPathXmlApplicationContext appContext;
	
	private JFrame frame;
	private JTextArea textArea;
	private JTextField editBox;
	
	public void boot(){
		frame.setVisible(true);
		
		appContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		//appService = appContext.getBean(TestService.class);
		
		//add shutdown hook
		Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
            	appContext.close();;
            }
        });
	}
	
	void buttonPressed(){
		String text = editBox.getText();
		editBox.setText("");
		textArea.setText( textArea.getText() + "\n" + text);
		TestService service = appContext.getBean(TestService.class);
		service.runTest();
	}
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.boot();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		textArea = new JTextArea();
		textArea.setWrapStyleWord(true);
		textArea.setTabSize(4);
		textArea.setLineWrap(true);
		textArea.setEnabled(false);
		textArea.setEditable(false);
		textArea.setBounds(5, 6, 364, 218);
		frame.getContentPane().add(textArea);
		
		editBox = new JTextField();
		editBox.setBounds(5, 230, 364, 28);
		editBox.setText("asdas");
		frame.getContentPane().add(editBox);
		editBox.setColumns(10);
		
		JButton btnButton = new JButton("button");
		btnButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonPressed();
			}
		});
		btnButton.setBounds(378, 228, 66, 28);
		frame.getContentPane().add(btnButton);
	}

}
