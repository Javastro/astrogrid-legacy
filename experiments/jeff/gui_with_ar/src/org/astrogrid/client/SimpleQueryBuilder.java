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
import org.astrogrid.acr.astrogrid.Applications;
import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.acr.astrogrid.ExecutionMessage;
import org.astrogrid.acr.astrogrid.RemoteProcessListener;
import org.astrogrid.acr.astrogrid.RemoteProcessManager;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.builtin.Shutdown;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.workflow.beans.v1.Tool;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.w3c.dom.Document;

public class SimpleQueryBuilder extends JFrame {

	private static final long serialVersionUID = 7516420866606716212L;
	
	private ACR ar ; 
	private Applications apps ;
	private RemoteProcessManager rpm ;
	private Shutdown sd ;
	private ACRException acrException ;
	
	private JTextArea workArea ;
	private JComboBox collectionCB ;
	private JButton submitButton, resetButton ;

	private static final String[] SERVER_IVORNS = { 
		"ivo://uk.ac.cam.ast/2dFGRS/object-catalogue/Object_catalogue_2dF_Galaxy_Redshift_Survey",
		"ivo://uk.ac.cam.ast/newhipparcos-dsa-catalog/HIPPARCOS_NEWLY_REDUCED",
		"ivo://wfau.roe.ac.uk/sdssdr3-dsa/dsa",
		"ivo://wfau.roe.ac.uk/sdssdr5-dsa/dsa"			
	} ;
	
	private static final String EXAMPLE_SDSS_QUERY = 
		"Select Top 100 a.ra, a.dec, a.psfMag_g, a.psfMag_r \n" +
		"From PhotoTag as a \n" +
		"Where a.ra > 110 And a.ra < 230 And a.dec > 20 And a.dec <= 22 \n" +
		"And (a.psfMag_g - a.psfMag_r < 0.4) \n" +
		"And a.psfMag_r > 20.0 And a.psfMag_g > 0 And a.mode = 1 And a.probPSF = 1" ;
	
	private static final String EXAMPLE_2DF_QUERY =
		"Select Top 100 * \n" +
		"From best_observations as a \n" +
		"Where a.serial >= 100000 And a.serial <= 100100 And a.quality >= 3" ;
	
	private static final String EXAMPLE_HIPPARCOS_QUERY =
		"SELECT Top 100 m.HIP, h.HD, m.Plx \n" +
		"From maincat as m, hdtohip as h \n" +
		"Where m.HIP = h.HIP" ;
	
	public synchronized void setACR( ACR ar, Applications apps, RemoteProcessManager rpm, Shutdown sd ) {
		this.ar = ar ;
		this.apps = apps ;
		this.rpm = rpm ;
		this.sd = sd ;
		SwingUtilities.invokeLater( new Runnable() {
			public void run(){
				workArea.setText( EXAMPLE_2DF_QUERY  ) ;
			}
		} ) ;
	}
	
	public synchronized void setException( final ACRException acrException ) {
		this.acrException = acrException ;
		SwingUtilities.invokeLater( new Runnable() {
			public void run(){
				workArea.setText( acrException.getLocalizedMessage() ) ;
			}
		} ) ;		
	} 
	
	public synchronized ACRException getException() {
		return this.acrException ;
	}
	
	public SimpleQueryBuilder() {
		super( "Simple Query Builder" ) ;
		setSize( 700, 600 ) ;
		
		JPanel panel = new JPanel() ;
		panel.setLayout( new GridBagLayout() ) ;
		
		panel.setBorder( new EmptyBorder( new Insets( 5,5,5,5 ) ) );
		getContentPane().add( BorderLayout.CENTER, panel ) ;
		
		GridBagConstraints c = new GridBagConstraints() ;
		c.insets = new Insets( 2, 2, 2, 2 ) ;
		c.anchor = GridBagConstraints.WEST ;
		
		collectionCB = new JComboBox() ;
		for( int i=0; i< SERVER_IVORNS.length; i++ ) {
			collectionCB.addItem( SERVER_IVORNS[i] ) ;
		}
		collectionCB.setPreferredSize( new Dimension( 600, 20 ) ) ;
		c.gridx = 0;
		c.weightx = 1.0 ;
		c.gridwidth = 6 ;
		c.fill = GridBagConstraints.HORIZONTAL ;
		panel.add( collectionCB, c ) ;
		
		workArea = new JTextArea() ;
		workArea.setText( "Initializing, please wait." ) ;
		JScrollPane scroll = new JScrollPane( workArea ) ;
		scroll.setPreferredSize( new Dimension( 600, 500) ) ;
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
		submitButton.setEnabled( false ) ;
		c.gridx = 0 ;
		c.gridy = 7 ;
		c.gridwidth = 1 ;
		c.gridheight = 1 ;
		c.weighty = 0.0 ;
		c.fill = GridBagConstraints.HORIZONTAL ;
		panel.add( submitButton, c ) ;
		
		resetButton = new JButton( "Reset" ) ;
		resetButton.addActionListener( new ResetActionListener() ) ;
		c.gridx = 5 ;
		panel.add( resetButton, c ) ;
		
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE ) ;	
		
		//
		// A bit pedantic, but we close down the ACR on a separate thread.
		addWindowListener( new WindowAdapter() {
			public void windowClosing( WindowEvent event ) {
				Thread worker = new Thread() {
					public void run() {
						sd.reallyHalt() ;
					}
				};
				worker.start();				
			}
		} ) ;
		
		setVisible( true ) ;
	}
	
	public void setReady() {
		submitButton.setEnabled( true ) ;
	}
	
	class SubmitActionListener implements ActionListener {
		
		public void actionPerformed( ActionEvent e ) {
			final String adqlString = workArea.getText() ;
			workArea.setText( "Submitting..." ) ;	
			final String serverIvorn = (String)collectionCB.getSelectedItem() ;
			//
			// This relationship between server ivorn and application ivorn is informal,
			// so it cannot be relied upon in all circumstances.
			// Both can be retrieved from the Registry.
			// (I use the VODesktop to find suitable candidates.)
			final String appIvorn = serverIvorn + "/ceaApplication" ;
			Thread worker = new Thread() {
				public void run() {
					try {
										
						URI appURI = new URI( appIvorn ) ;
						Document doc = apps.createTemplateDocument( appURI, "ADQL" ) ;
						Tool tool = (Tool) Unmarshaller.unmarshal( Tool.class, doc ) ;
						ParameterValue[] pvs = tool.getInput().getParameter() ;
						pvs[0].setValue( adqlString ) ;
						doc = doc.getImplementation().createDocument( null, null, null ) ;
						Marshaller.marshal(tool,doc) ;
						// XMLUtils.PrettyDocumentToStream(doc,System.out);

						URI jobURI = rpm.submitTo( doc, new URI(serverIvorn) ) ;
						SwingUtilities.invokeLater( new Runnable() {
							public void run() {
								workArea.setText( "Submitted successfully!" ) ;	
							}
						}) ;
						//
						// The remote listener deals with problems, output etc...
						rpm.addRemoteProcessListener( jobURI, new JobListener() ) ;						
					}
					catch( final Exception ex ) {
						SwingUtilities.invokeLater( new Runnable() {
							public void run() {
								workArea.setText( "Problem occurred: " + ex.getLocalizedMessage() ) ;	
							}
						}) ;				
					}
				}
			};
			worker.start();
			submitButton.setEnabled( false ) ;
		}
		
	}
	
	class ResetActionListener implements ActionListener {

		public void actionPerformed( ActionEvent e ) {
			workArea.setText( "Type query here..." ) ;	
			submitButton.setEnabled( true ) ;
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
		
		public JobListener() {}

		public void messageReceived( URI arg0, final ExecutionMessage message ) {
			SwingUtilities.invokeLater( new Runnable() {
				public void run() {
					workArea.setText( workArea.getText() + "\n" + message.getContent() ) ;
				}
			}) ;
		}

		public void resultsReceived( URI arg0, final Map resultsMap ) {					
			SwingUtilities.invokeLater( new Runnable() {
				public void run() {
					Iterator it = resultsMap.values().iterator() ;
					if( it.hasNext() ) {
						workArea.setText( it.next().toString() ) ;
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
							workArea.setText( status ) ;
							deleteJob( jobURI ) ;			
						}
					}) ;
				}
				else if ( status.equalsIgnoreCase( ExecutionInformation.COMPLETED ) ) {
					String results = "" ;
					Iterator it = rpm.getResults( jobURI).values().iterator() ;
					if( it.hasNext() ) {
						results = it.next().toString() ;
					}	
					final String fResults = results;
					SwingUtilities.invokeLater( new Runnable() {
						public void run() {
							try {
								workArea.setText( fResults ) ;
							}
							catch( Exception ex ) {
								workArea.setText(  ex.getLocalizedMessage() ) ;
							}
							finally {
								deleteJob( jobURI ) ;
							}
						}
					}) ;
				}
			}
			catch( final Exception ex ) {
				SwingUtilities.invokeLater( new Runnable() {				
					public void run() {
						Exception ex1 = ex ;
						workArea.setText( ex1.getLocalizedMessage() ) ;						
					}
				} ) ;
			} 
			
		}
		
		private void deleteJob( final URI jobURI ) { 			
			Thread worker = new Thread() {
				public void run() {
					try {
						getRpm().delete( jobURI ) ;
					}
					catch( Exception ex ) {
						;
					}
				}
			};
			worker.start();
		}
		
	}

}