package org.astrogrid.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class SimpleQueryBuilder extends JFrame {

	private static final long serialVersionUID = 7516420866606716212L;
	
	private JTextArea queryText ;
	private JComboBox collectionCB ;
	private JButton submitButton, resultsButton, resetButton ;

	public SimpleQueryBuilder() {
		super( "Simple Query Builder" ) ;
		setSize( 600, 600 ) ;
		
		JPanel panel = new JPanel() ;
		panel.setLayout( new GridBagLayout() ) ;
		
		panel.setBorder( new EmptyBorder( new Insets( 5,5,5,5 ) ) );
		getContentPane().add( BorderLayout.CENTER, panel ) ;
		
		GridBagConstraints c = new GridBagConstraints() ;
		c.insets = new Insets( 2, 2, 2, 2 ) ;
		c.anchor = GridBagConstraints.WEST ;
		
		collectionCB = new JComboBox() ;
		collectionCB.addItem( "ivo://wfau.roe.ac.uk/sdssdr5-dsa/dsa" ) ;
		collectionCB.setPreferredSize( new Dimension( 500, 20 ) ) ;
		c.gridx = 0;
		c.weightx = 1.0 ;
		c.gridwidth = 6 ;
		c.fill = GridBagConstraints.HORIZONTAL ;
		panel.add( collectionCB, c ) ;
		
		queryText = new JTextArea() ;
		JScrollPane scroll = new JScrollPane( queryText ) ;
		scroll.setPreferredSize( new Dimension( 500, 500) ) ;
		c.gridx = 0 ;
		c.gridy = 1 ;
		c.weightx = 1.0 ;
		c.weighty = 1.0 ;
		c.gridwidth = 6 ;
		c.gridheight = 6 ;
		c.fill = GridBagConstraints.BOTH ;
		panel.add( scroll, c ) ;
		
		c.anchor = GridBagConstraints.SOUTH ;
		
		submitButton = new JButton( "Submit" ) ;
		submitButton.addActionListener( new  SubmitActionListener() ) ;
		c.gridx = 0 ;
		c.gridy = 7 ;
		c.gridwidth = 1 ;
		c.gridheight = 1 ;
		c.weighty = 0.0 ;
		c.fill = GridBagConstraints.HORIZONTAL ;
		panel.add( submitButton, c ) ;
		
		resultsButton = new JButton( "Results" ) ;
		resultsButton.addActionListener( new ResultsActionListener() ) ;
		resultsButton.setEnabled( false ) ;
		c.gridx = 2 ;
		panel.add( resultsButton, c ) ;
		
		resetButton = new JButton( "Reset" ) ;
		resetButton.addActionListener( new ResetActionListener() ) ;
		c.gridx = 5 ;
		panel.add( resetButton, c ) ;
		
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE ) ;
		setVisible( true ) ;
		
	}
	
	public static void main( String argv[] ) {
		new SimpleQueryBuilder() ;
	}
	
	class SubmitActionListener implements ActionListener {

		public void actionPerformed( ActionEvent e ) {
			queryText.setText( "Just submitted" ) ;	
			submitButton.setEnabled( false ) ;
			resultsButton.setEnabled( true ) ;
		}
		
	}
	
	class ResultsActionListener implements ActionListener {

		public void actionPerformed( ActionEvent e ) {
			queryText.setText( "These are the results" ) ;	
		}
		
	}
	
	class ResetActionListener implements ActionListener {

		public void actionPerformed( ActionEvent e ) {
			queryText.setText( "Type query here..." ) ;	
			submitButton.setEnabled( true ) ;
			resultsButton.setEnabled( false ) ;
		}
		
	}

}