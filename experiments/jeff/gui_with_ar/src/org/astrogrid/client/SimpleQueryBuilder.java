package org.astrogrid.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.Applications;
import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.acr.astrogrid.ExecutionMessage;
import org.astrogrid.acr.astrogrid.RemoteProcessListener;
import org.astrogrid.acr.astrogrid.RemoteProcessManager;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.builtin.Shutdown;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.workflow.beans.v1.Tool;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.Document;

public class SimpleQueryBuilder extends JFrame {

	private static final long serialVersionUID = 7516420866606716212L;
	
	private ACR ar ; 
	private Applications apps ;
	private RemoteProcessManager rpm ;
	private Shutdown sd ;
	private ACRException acrException ;
	
	private JTextArea queryText ;
	private JComboBox collectionCB ;
	private JButton submitButton, resultsButton, resetButton ;
	
	String appIvorn = "ivo://wfau.roe.ac.uk/sdssdr5-dsa/dsa/ceaApplication" ;
	String serverIvorn = "ivo://wfau.roe.ac.uk/sdssdr5-dsa/dsa" ;
	String adqlQuery = 
		    "Select Top 100 a.ra, a.dec, a.psfMag_g, a.psfMag_r " +
		    "From PhotoTag as a " +
			"Where a.ra>110 And a.ra<230 And a.dec>20 And a.dec<=22 " +
			" And  (a.psfMag_g-a.psfMag_r <0.4 )  " +
			" And a.psfMag_r>20.0 And a.psfMag_g>0 And a.mode=1 And a.probPSF=1" ;
	
	public synchronized void setACR( ACR ar, Applications apps, RemoteProcessManager rpm, Shutdown sd ) {
		this.ar = ar ;
		this.apps = apps ;
		this.rpm = rpm ;
		this.sd = sd ;
		SwingUtilities.invokeLater( new Runnable() {
			public void run(){
				queryText.setText( adqlQuery  ) ;
			}
		} ) ;
	}
	
	public synchronized void setException( final ACRException acrException ) {
		this.acrException = acrException ;
		SwingUtilities.invokeLater( new Runnable() {
			public void run(){
				queryText.setText( acrException.getLocalizedMessage() ) ;
			}
		} ) ;		
	} 
	
	public synchronized ACRException getException() {
		return this.acrException ;
	}
	
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
		
		//
		// This could probably be made more subtle...
		addWindowListener( new WindowAdapter() {
			public void windowClosing( WindowEvent event ) {
				sd.reallyHalt() ;
			}
		} ) ;
	}
	
	public static void main( String argv[] ) {
		new SimpleQueryBuilder() ;
	}
	
	class SubmitActionListener implements ActionListener {
		
		public void actionPerformed( ActionEvent e ) {
			String adqlString = queryText.getText() ;
			queryText.setText( "Submitting without using background thread..." ) ;	
			try {	
				
				URI appURI = new URI( appIvorn ) ;
				Document doc = apps.createTemplateDocument( appURI, "ADQL" ) ;
				Tool tool = (Tool) Unmarshaller.unmarshal( Tool.class, doc ) ;
				ParameterValue[] pvs = tool.getInput().getParameter() ;
				pvs[0].setValue( adqlString ) ;
				doc = doc.getImplementation().createDocument( null, null, null ) ;
				Marshaller.marshal(tool,doc) ;
//				XMLUtils.PrettyDocumentToStream(doc,System.out);
				
				URI jobURI = rpm.submitTo( doc, new URI(serverIvorn) ) ;
				//
				// Need to set up a listener here...
				rpm.addRemoteProcessListener( jobURI, new JobListener() ) ;
				queryText.setText( "Submitted successfully!" ) ;							
			}
			catch( final Exception  usx ) {
//				SwingUtilities.invokeLater( new Runnable() {
//					Exception usx1 = usx ;
//					public void run() {					
//						queryText.setText( usx.getLocalizedMessage() ) ;			
//					}
//				}) ;	
				queryText.setText( usx.getLocalizedMessage() ) ;	
			}	
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

	public synchronized ACR getAr() {
		return ar;
	}

	public synchronized Applications getApps() {
		return apps;
	}

	public synchronized RemoteProcessManager getRpm() {
		return rpm;
	}

	public synchronized Shutdown getSd() {
		return sd;
	}
	
	class JobListener implements RemoteProcessListener {
		
		//
		// Question. If this is made more sophisticated,
		// will thread safety need to be taken into account,
		// or is this guaranteed by the calling environment?
		
		public JobListener() {}

		public void messageReceived( URI arg0, final ExecutionMessage message ) {
//			queryText.setText( queryText.getText() + "\n" + message.getContent() ) ;
			SwingUtilities.invokeLater( new Runnable() {
				public void run() {
					queryText.setText( queryText.getText() + "\n" + message.getContent() ) ;
				}
			}) ;
		}

		public void resultsReceived( URI arg0, final Map resultsMap ) {	
//			Map resultsMap1 = resultsMap ;
//			Iterator it = resultsMap.entrySet().iterator() ;
//			if( it.hasNext() ) {
//				queryText.setText( it.next().toString() ) ;
//			}				
			SwingUtilities.invokeLater( new Runnable() {
				public void run() {
					Map resultsMap1 = resultsMap ;
					Iterator it = resultsMap.entrySet().iterator() ;
					if( it.hasNext() ) {
						queryText.setText( it.next().toString() ) ;
					}				
				}
			}) ;		
		}
		
		public void statusChanged( final URI jobURI, final String status ) {

			try {
				if( status.equalsIgnoreCase( ExecutionInformation.ERROR ) 
					||
					status.equalsIgnoreCase( ExecutionInformation.UNKNOWN )				
				) {
					SwingUtilities.invokeLater( new Runnable() {
						public void run() {
							String status1 = status ;
							queryText.setText( status ) ;
							try {
								rpm.delete( jobURI ) ;
							}
							catch( Exception ex ) {
								;
							}						
						}
					}) ;
//					queryText.setText( status ) ;
					
				}
				else if ( status.equalsIgnoreCase( ExecutionInformation.COMPLETED ) ) {
					SwingUtilities.invokeLater( new Runnable() {
						public void run() {
							try {
								queryText.setText(  rpm.getResults( jobURI).toString() ) ;
							}
							catch( Exception ex ) {
								queryText.setText(  ex.getLocalizedMessage() ) ;
							}
						}
					}) ;
//					try {
//						queryText.setText(  rpm.getResults( jobURI).toString() ) ;
//					}
//					catch( Exception ex ) {
//						queryText.setText(  ex.getLocalizedMessage() ) ;
//					}
				}
			}
			catch( final Exception ex ) {
				SwingUtilities.invokeLater( new Runnable() {				
					public void run() {
						Exception ex1 = ex ;
						queryText.setText( ex1.getLocalizedMessage() ) ;						
					}
				} ) ;
//				queryText.setText( ex.getLocalizedMessage() ) ;	
			} 
			
		}
		
	}

}