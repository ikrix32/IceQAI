package com.iceq.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.iceq.dictionary.analyser.GramaticalAnalyser;

public class MainWindow {
	private ClassPathXmlApplicationContext appContext;
	private GramaticalAnalyser analyser;
	
	private JFrame frame;
	private TextArea textArea;
	private JTextField editBox;
	
	public void boot(){
		frame.setVisible(true);
		
		PrintStream printStream = new PrintStream(new ConsoleRedirectStream(textArea));
		
		System.setOut(printStream);
		//System.setErr(printStream);
		
		analyser = new GramaticalAnalyser();
		
		appContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		//appService = appContext.getBean(TestService.class);
		
		//add shutdown hook
		Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
            	appContext.close();
            }
        });
	}
	
	void buttonPressed(){
		String text = editBox.getText();
		editBox.setText("");
		analyser.processSentence(text);
		//textArea.setText( textArea.getText() + "\n" + text);
		//TestService service = appContext.getBean(TestService.class);
		//service.runTestRelations();
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
		
		editBox = new JTextField();
		editBox.setText("Copilul este om.");
		editBox.setColumns(10);
		
		JButton btnButton = new JButton("analyse");
		btnButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonPressed();
			}
		});
		
		JScrollPane scrollPane = new JScrollPane();
		
		textArea = new TextArea();
		textArea.setForeground(Color.BLACK);
		textArea.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		scrollPane.setViewportView(textArea);
		textArea.setWrapStyleWord(true);
		textArea.setTabSize(4);
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(editBox, GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnButton)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(editBox, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnButton))
					.addContainerGap())
		);
		frame.getContentPane().setLayout(groupLayout);
	}
}
