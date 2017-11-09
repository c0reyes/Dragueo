package com.dragueo;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import java.awt.Checkbox;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Properties;

import javax.swing.Action;
import java.awt.Toolkit;
import javax.swing.JCheckBox;

public class DragueoConfiguracion extends JDialog {
	private final JPanel contentPanel = new JPanel();
	private JTextField source;
	private JComboBox comboBox;
	private Checkbox checkDebug;
	private Checkbox checkDump;
	private Checkbox checkTest;
	private Checkbox checkStartup; 
	private final Action action = new SwingActionClose();
	private final Action actionSave = new SwingActionSave();
	
	private Properties prop = new Properties();
	private JTextField lines;

	/**
	 * Create the dialog.
	 */
	public DragueoConfiguracion() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(DragueoConfiguracion.class.getResource("/res/icon.png")));
		setTitle("Dragueo - Configuration");
		setResizable(false);
		setAlwaysOnTop(true);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		/* Port */
		JLabel lblPuerto = new JLabel("Port");
		contentPanel.add(lblPuerto, "2, 2, right, default");
		
		List<String> ports = com.dragueo.port.Ports.listPorts();
		comboBox = new JComboBox(ports.toArray());
		contentPanel.add(comboBox, "4, 2, fill, default");
		
		JLabel lblSource = new JLabel("Code");
		contentPanel.add(lblSource, "2, 4, right, default");
		
		source = new JTextField();
		contentPanel.add(source, "4, 4, fill, default");
		source.setColumns(10);
	
		JLabel lblLiniasFinales = new JLabel("Eject Lines");
		contentPanel.add(lblLiniasFinales, "2, 6, right, default");
		
		lines = new JTextField();
		contentPanel.add(lines, "4, 6, fill, default");
		lines.setColumns(10);
		JSeparator separator = new JSeparator();
		contentPanel.add(separator, "2, 8, 3, 1");
		
		checkDump = new Checkbox("Dump");
		contentPanel.add(checkDump, "4, 12");

		checkDebug = new Checkbox("Debug");
		contentPanel.add(checkDebug, "4, 14");
		
		checkTest = new Checkbox("Test");
		contentPanel.add(checkTest, "4, 16");
		
		checkStartup = new Checkbox("Start on startup");
		contentPanel.add(checkStartup, "4, 18");
			
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		JButton okButton = new JButton("Save");
		okButton.setAction(actionSave);
		okButton.setActionCommand("Save");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		
		JButton cancelButton = new JButton("Close");
		cancelButton.setAction(action);
		cancelButton.setActionCommand("Close");
		buttonPane.add(cancelButton);

		prop = Prop.loadProp();
		
		if(prop.getProperty("code") != null) {
			source.setText(prop.getProperty("code"));
		}
		if(prop.getProperty("com") != null) {
			int c = 0;
			for(String t : ports) {
				if(t.equals(prop.getProperty("com"))) {
					comboBox.setSelectedIndex(c);
				}
				c++;
			}
		}
		if(prop.getProperty("lines") != null) {
			lines.setText(prop.getProperty("lines"));
		}
		if(prop.getProperty("checkDump") != null) {
			if("true".equals(prop.getProperty("checkDump"))) {
				checkDump.setState(true);
			}
		}
		if(prop.getProperty("checkDebug") != null) {
			if("true".equals(prop.getProperty("checkDebug"))) {
				checkDebug.setState(true);
			}
		}
		if(prop.getProperty("checkTest") != null) {
			if("true".equals(prop.getProperty("checkTest"))) {
				checkTest.setState(true);
			}
		}
		if(prop.getProperty("checkStartup") != null) {
			if("true".equals(prop.getProperty("checkStartup"))) {
				checkStartup.setState(true);
			}
		}
	}

	private class SwingActionClose extends AbstractAction {
		public SwingActionClose() {
			putValue(NAME, "Close");
		}
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}
	
	private class SwingActionSave extends AbstractAction {
		public SwingActionSave() {
			putValue(NAME, "Save");
		}
		public void actionPerformed(ActionEvent e) {
			String msg = "";
			
			if( source.getText().isEmpty() ) {
				msg += "- Source is empty\n";
			}else{
				prop.setProperty("code", source.getText());
			}
			
			if( lines.getText().isEmpty() ) {
				msg += "- Lines is empty\n";
			}else{
				prop.setProperty("lines", lines.getText());
			}
			
			if(comboBox.getSelectedItem().toString() == null) {
				msg += "- Port is empty\n";
			}else{
				prop.setProperty("com", comboBox.getSelectedItem().toString());
			}
			
			prop.setProperty("checkDump","" + checkDump.getState());
			prop.setProperty("checkDebug","" + checkDebug.getState());
			prop.setProperty("checkTest","" + checkTest.getState());
			prop.setProperty("checkStartup","" + checkStartup.getState());
			
			if(!"".equals(msg)) {
				JOptionPane.showMessageDialog(null, msg);
			}else{
				Prop.saveProp(prop);
				Prop.reloadProp();
			}
		}
	}
}
