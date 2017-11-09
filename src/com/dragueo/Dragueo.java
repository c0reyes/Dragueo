package com.dragueo;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.GroupLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultCaret;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.dragueo.http.Tickets;
import com.dragueo.port.ComPortRead;
import java.awt.Toolkit;

public class Dragueo {
	private JFrame frame;
	private JTable table;
	private JProgressBar progressBar;
	private JMenuItem mntmStart;
	private JMenuItem mntmStartHttp;
	private JMenuItem mntmStartCom;
	
	JProgressBar connectionBar;
	
	JTextArea textArea;
	DefaultTableModel model;
	
	static Dragueo window = null;
	
	private final Action actionStart = new SwingActionStart();
	private final Action actionStop = new SwingActionStop();
	private final Action action = new SwingActionConf();
	private final Action about = new SwingAbout();
	private final Action actionExit = new SwingActionExit();
	private final Action simulator = new SwingSimulator();
	
	DragueoAbout aboutDialog = null;
	DragueoConfiguracion confDialog = null;
	DragueoSimulator simulatorDialog = null;
	
	private static Logger log = Logger.getLogger(Dragueo.class);
	
	private ComPortRead comportread = null;
	private Tickets tickets = null;
	private final Action action_1 = new SwingActionStartCom();
	private final Action action_2 = new SwingActionStartHttp();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			PropertyConfigurator.configure(Dragueo.class.getClass().getResource("/log4jwin.properties"));		
		}catch(Exception e) {}
		
		if(!new File("log").exists()) {
			new File("log").mkdir();
		}
		try {
		        for (javax.swing.UIManager.LookAndFeelInfo info :  javax.swing.UIManager.getInstalledLookAndFeels()) {
		            if ("Windows".equals(info.getName())) {
		               javax.swing.UIManager.setLookAndFeel(info.getClassName());
		                break;
		             }
		         }
		}catch(Exception e) {
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new Dragueo();
					window.frame.setVisible(true);
					log.info("Starting Dragueo...");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the application.
	 */
	public Dragueo() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Dragueo");
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Dragueo.class.getResource("/res/icon.png")));
		frame.setBounds(100, 100, 500, 430);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JLabel lblLog = new JLabel("Log");

		JSeparator separator = new JSeparator();
		
		progressBar = new JProgressBar();
		progressBar.setToolTipText("");
		progressBar.setValue(100);
		progressBar.setForeground(Color.RED);
		progressBar.setBackground(Color.RED);
		
		JLabel lblStatus = new JLabel("Status:");
		
		JScrollPane scrollPane = new JScrollPane();
		
		textArea = new JTextArea();
		JScrollPane scrollPane_1 = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		textArea.setEditable(false);

		DefaultCaret caret = (DefaultCaret) textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		JLabel lblConection = new JLabel("Conection:");
		
		connectionBar = new JProgressBar();
		connectionBar.setValue(0);
		connectionBar.setToolTipText("");
		connectionBar.setForeground(Color.RED);
		
		JLabel lblNewLabel = new JLabel(DragueoImg.Image("dragueo.png", 200));
		
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblStatus)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 91, Short.MAX_VALUE)
							.addComponent(lblConection)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(connectionBar, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE))
						.addComponent(separator, GroupLayout.DEFAULT_SIZE, 472, Short.MAX_VALUE)
						.addComponent(lblNewLabel, Alignment.TRAILING)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 472, Short.MAX_VALUE)
						.addComponent(lblLog)
						.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 472, Short.MAX_VALUE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblLog)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblStatus)
						.addComponent(connectionBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblConection))
					.addContainerGap())
		);


		Object[] columnNames = {"Date","Tickets","Code","Status"};
		table = new JTable(new DefaultTableModel(null, columnNames)) {
            public Class getColumnClass(int column)
            {
                return getValueAt(0, column).getClass(); 
            }
        };
		scrollPane.setViewportView(table);
		table.setEnabled(false);	
		model = (DefaultTableModel)table.getModel();

		frame.getContentPane().setLayout(groupLayout);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnInicio = new JMenu("File");
		menuBar.add(mnInicio);
		
		mntmStart = new JMenuItem("Start");
		mntmStart.setAction(actionStart);
		mnInicio.add(mntmStart);
		
		JMenuItem mntmStop = new JMenuItem("Stop");
		mntmStop.setAction(actionStop);
		mnInicio.add(mntmStop);
		
		JSeparator separator_3 = new JSeparator();
		mnInicio.add(separator_3);
		
		mntmStartCom = new JMenuItem("Start com");
		mntmStartCom.setAction(action_1);
		mnInicio.add(mntmStartCom);
		
		mntmStartHttp = new JMenuItem("Start http");
		mntmStartHttp.setAction(action_2);
		mnInicio.add(mntmStartHttp);
		
		JSeparator separator_1 = new JSeparator();
		mnInicio.add(separator_1);
		
		JMenuItem mntmSimulator = new JMenuItem("Simulator");
		mntmSimulator.setAction(simulator);
		mnInicio.add(mntmSimulator);
		
		JSeparator separator_2 = new JSeparator();
		mnInicio.add(separator_2);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.setAction(actionExit);
		mnInicio.add(mntmExit);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmConfiguration = new JMenuItem("Configuration");
		mntmConfiguration.setAction(action);
		mnHelp.add(mntmConfiguration);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.setAction(about);
		mnHelp.add(mntmAbout);
		
		if("true".equals(Prop.loadProp().getProperty("checkStartup","false"))) {
			actionStart.actionPerformed(null);
		}
		
		simulator.setEnabled(false);
	}
	
	private class SwingActionStart extends AbstractAction {
		public SwingActionStart() {
			putValue(NAME, "Start All");	
		}
		public void actionPerformed(ActionEvent e) {
			progressBar.setValue(100);
			progressBar.setForeground(Color.GREEN);
			progressBar.setBackground(Color.GREEN);
			
			mntmStart.setEnabled(false);
			mntmStartHttp.setEnabled(false);
			mntmStartCom.setEnabled(false);
			
			log.info("Starting ComPortRead...");
			textArea.append("Starting ComPortRead...\n");
			
			comportread = new ComPortRead(textArea);
			comportread.start();
			
			log.info("Starting Tickets...");
			textArea.append("Starting Tickets...\n");
			tickets = new Tickets(textArea, model, connectionBar);
			tickets.start();
		}
	}
	
	private class SwingActionStop extends AbstractAction {
		public SwingActionStop() {
			putValue(NAME, "Stop All");
		}
		public void actionPerformed(ActionEvent e) {
			progressBar.setValue(100);
			progressBar.setForeground(Color.RED);
			progressBar.setBackground(Color.RED);
			
			mntmStart.setEnabled(true);
			mntmStartHttp.setEnabled(true);
			mntmStartCom.setEnabled(true);

			comportread.terminate();
			tickets.terminate();	
		}
	}
	
	private class SwingActionConf extends AbstractAction {
		public SwingActionConf() {
			putValue(NAME, "Configuration");
		}
		public void actionPerformed(ActionEvent e) {
			confDialog = new DragueoConfiguracion();
			confDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			confDialog.setVisible(true); 
		}
	}
	
	private class SwingAbout extends AbstractAction {
		public SwingAbout() {
			putValue(NAME, "About");
		}
		public void actionPerformed(ActionEvent e) {
			aboutDialog = new DragueoAbout();
			aboutDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			aboutDialog.setVisible(true); 
		}
	}
	
	private class SwingSimulator extends AbstractAction {
		public SwingSimulator() {
			putValue(NAME, "Simulator");
		}
		public void actionPerformed(ActionEvent e) {
			simulatorDialog = new DragueoSimulator();
			simulatorDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			simulatorDialog.setVisible(true); 
		}
	}
	
	private class SwingActionExit extends AbstractAction {
		public SwingActionExit() {
			putValue(NAME, "Exit");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			if(aboutDialog != null) {
				aboutDialog.dispose();
			}
			if(confDialog != null) {
				confDialog.dispose();
			}
			if(simulatorDialog != null) {
				simulatorDialog.dispose();
			}
			if(comportread != null) {		
				comportread.terminate();		
			}
			if(tickets != null) {
				tickets.terminate();		
			}
			
			log.info("Stopping Dragueo...");
			
			window.frame.dispose();
		}
	}
	
	private class SwingActionStartCom extends AbstractAction {
		public SwingActionStartCom() {
			putValue(NAME, "Start Com");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			progressBar.setValue(100);
			progressBar.setForeground(Color.GREEN);
			progressBar.setBackground(Color.GREEN);
			
			mntmStart.setEnabled(false);
			mntmStartCom.setEnabled(false);
			
			log.info("Starting ComPortRead...");
			textArea.append("Starting ComPortRead...\n");
			
			comportread = new ComPortRead(textArea);
			comportread.start();
		}
	}
	
	private class SwingActionStartHttp extends AbstractAction {
		public SwingActionStartHttp() {
			putValue(NAME, "Start Http");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			progressBar.setValue(100);
			progressBar.setForeground(Color.GREEN);
			progressBar.setBackground(Color.GREEN);
			
			mntmStart.setEnabled(false);
			mntmStartHttp.setEnabled(false);
			
			log.info("Starting Tickets...");
			textArea.append("Starting Tickets...\n");
			tickets = new Tickets(textArea, model, connectionBar);
			tickets.start();
		}
	}
}
