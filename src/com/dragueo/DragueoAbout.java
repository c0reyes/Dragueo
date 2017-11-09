package com.dragueo;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.SwingConstants;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Toolkit;

public class DragueoAbout extends JDialog {
	private final Action action = new SwingActionClose();

	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public DragueoAbout() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(DragueoAbout.class.getResource("/res/icon.png")));
		setTitle("Dragueo - About");
		setResizable(false);
		setAlwaysOnTop(true);
		setBounds(100, 100, 310, 220);

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblImage = new JLabel(DragueoImg.Image("dragueo.png", 200));
			GridBagConstraints gbc_lblImage = new GridBagConstraints();
			gbc_lblImage.gridwidth = 7;
			gbc_lblImage.insets = new Insets(0, 0, 5, 0);
			gbc_lblImage.gridx = 0;
			gbc_lblImage.gridy = 0;
			contentPanel.add(lblImage, gbc_lblImage);
		}
		{
			JLabel lblHttpwwwdragueocom = new JLabel("http://www.dragueo.com");
			GridBagConstraints gbc_lblHttpwwwdragueocom = new GridBagConstraints();
			gbc_lblHttpwwwdragueocom.gridwidth = 5;
			gbc_lblHttpwwwdragueocom.insets = new Insets(0, 0, 5, 5);
			gbc_lblHttpwwwdragueocom.gridx = 0;
			gbc_lblHttpwwwdragueocom.gridy = 2;
			contentPanel.add(lblHttpwwwdragueocom, gbc_lblHttpwwwdragueocom);
		}
		{
			JLabel lblDragueo = new JLabel("\u00A9 Dragueo 2014");
			lblDragueo.setHorizontalAlignment(SwingConstants.LEFT);
			GridBagConstraints gbc_lblDragueo = new GridBagConstraints();
			gbc_lblDragueo.gridwidth = 2;
			gbc_lblDragueo.insets = new Insets(0, 0, 5, 5);
			gbc_lblDragueo.gridx = 0;
			gbc_lblDragueo.gridy = 3;
			contentPanel.add(lblDragueo, gbc_lblDragueo);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.setAction(action);
				buttonPane.add(cancelButton);
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
}
