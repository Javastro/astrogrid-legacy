package org.astrogrid.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
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

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
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

//
// Simple GUI client for submitting ADQL queries.
//
// The critical methods are within the SubmitActionListener and 
// the JobListener, which illustrate interaction with the ACR. 
//
// I suspect a more sophisticated approach to multi-threading
// would be achievable by a Swing programmer. But this works.
public class SimpleQueryBuilder extends JFrame {

	private static final long serialVersionUID = 7516420866606716212L;
	
	//
	// The ACR and services required for managing queries and shutdown...
	private ACR ar ; 
	private Applications apps ;
	private RemoteProcessManager rpm ;
	private Shutdown sd ;
	
	//
	// GUI constructs ...
	private JTextArea queryArea, outputArea ;
	private JComboBox collectionCB ;
	private JButton submitButton, resetButton ;
	
	//
	// Output required
	// 0 = VOTABLE
	// 1 = COMMA-SEPARATED
	// 2 = HTML
	// NOTE: VOTABLE-BINARY not utilized in this example
	private int outputType = 0 ;

	//
	// Services used to populate the combo box
	private static final String[] SERVER_IVORNS = { 
		"ivo://uk.ac.cam.ast/2dFGRS/object-catalogue/Object_catalogue_2dF_Galaxy_Redshift_Survey",
		"ivo://uk.ac.cam.ast/newhipparcos-dsa-catalog/HIPPARCOS_NEWLY_REDUCED",
		"ivo://wfau.roe.ac.uk/sdssdr3-dsa/dsa",
		"ivo://wfau.roe.ac.uk/sdssdr5-dsa/dsa"			
	} ;
	
	//
	// Examples of ADQL queries.
	// One of these is used to pre-populate the work area.
	// Now co-ordinated with the combo box:
	// if a choice is selected from the combo box, then a suitable
	// example is displayed in the query area.
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
	
	//
	// Sets the ACR and associated variables once they have been retrieved.
	// Puts out an example query.
	public synchronized void setACR( ACR ar, Applications apps, RemoteProcessManager rpm, Shutdown sd ) {
		this.ar = ar ;
		this.apps = apps ;
		this.rpm = rpm ;
		this.sd = sd ;
		SwingUtilities.invokeLater( new Runnable() {
			public void run(){
				queryArea.setText( EXAMPLE_2DF_QUERY  ) ;
			}
		} ) ;
	}

	//
	// If anything went wrong in retrieving the ACR, 
	// the message from the exception gets displayed.
	public synchronized void setException( final ACRException acrException ) {
		SwingUtilities.invokeLater( new Runnable() {
			public void run(){
				outputArea.setText( acrException.getLocalizedMessage() ) ;
			}
		} ) ;		
	} 
	
	// 
	// GUI constructor. Mostly inconsequential GUI stuff here.
	// But, see:
	// (1) The SubmitActionListener which manages the action from
	//     the submit button.
	// (2) The window listener that gets added on window closing.
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
		//
		// The combo box where the access ivorns are hard coded ...
		collectionCB = new JComboBox() ;
		for( int i=0; i< SERVER_IVORNS.length; i++ ) {
			collectionCB.addItem( SERVER_IVORNS[i] ) ;
		}
		
		//
		// This is very, very crude but convenient for the examples...
		collectionCB.addActionListener( new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String ivorn = (String)collectionCB.getSelectedItem() ;
				if( ivorn.indexOf( "2dF" ) > 0 ) {
					queryArea.setText( EXAMPLE_2DF_QUERY ) ;
				}
				else if( ivorn.indexOf( "hipparcos" ) > 0 ) {
					queryArea.setText( EXAMPLE_HIPPARCOS_QUERY ) ;
				}
				else if( ivorn.indexOf( "sdssdr3" ) > 0 ) {
					queryArea.setText( EXAMPLE_SDSS_QUERY ) ;
				}
				else if( ivorn.indexOf( "sdssdr5" ) > 0 ) {
					queryArea.setText( EXAMPLE_SDSS_QUERY ) ;
				} 
			}
			
		}) ;
				
		collectionCB.setPreferredSize( new Dimension( 600, 20 ) ) ;
		c.gridx = 0;
		c.weightx = 1.0 ;
		c.gridwidth = 6 ;
		c.fill = GridBagConstraints.HORIZONTAL ;
		panel.add( collectionCB, c ) ;
		//
		// The query area.
		queryArea = new JTextArea() ;
		queryArea.setText( "" ) ;
		queryArea.setEditable( false ) ;
		JScrollPane waScroll = new JScrollPane( queryArea ) ;
		waScroll.setPreferredSize( new Dimension( 600, 150) ) ;
		
		//
		// The feedback area.
		// Message feedback and results are
		// all displayed here.
		outputArea = new JTextArea() ;
		outputArea.setText( "Initializing, please wait." ) ;
		outputArea.setEditable( false ) ;
		JScrollPane fbScroll = new JScrollPane( outputArea ) ;
		fbScroll.setPreferredSize( new Dimension( 600, 350) ) ;
		
		JSplitPane sp = new JSplitPane( JSplitPane.VERTICAL_SPLIT, waScroll, fbScroll ) ;
		sp.setDividerSize( 8 ) ;
		sp.setDividerLocation( 100 ) ;
		sp.setResizeWeight( 0.5 ) ;
		sp.setOneTouchExpandable( true ) ;
		c.gridx = 0 ;
		c.gridy = 1 ;
		c.weightx = 1.0 ;
		c.weighty = 1.0 ;
		c.gridwidth = 6 ;
		c.gridheight = 7 ;
		c.fill = GridBagConstraints.BOTH ;
		panel.add( sp, c ) ;
						
		c.anchor = GridBagConstraints.SOUTH ;
		
		//
		// Type of output...
		JPanel radioPanel = new JPanel() ;
		radioPanel.setLayout( new FlowLayout( FlowLayout.CENTER, 5, 0 ) ) ;
		ButtonGroup bGroup = new ButtonGroup() ;
		JRadioButton votButton = new JRadioButton( "VOTable" ) ;
		votButton.setSelected( true ) ;
		bGroup.add( votButton ) ;
		JRadioButton csvButton = new JRadioButton( "Comma-Separated" ) ;
		bGroup.add( csvButton ) ;
		JRadioButton htmlButton = new JRadioButton( "HTML" ) ;
		bGroup.add( htmlButton ) ;
		radioPanel.add( votButton ) ;
		radioPanel.add( csvButton ) ;
		radioPanel.add( htmlButton ) ;
		c.gridx = 4 ;
		c.gridy = 10 ;
		c.gridwidth = 1 ;
		c.gridheight = 1 ;
		c.weighty = 0.0 ;
		panel.add( radioPanel, c ) ;
		
		votButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outputType = 0 ;
			}			
		}) ;
		csvButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outputType = 1 ;
			}			
		}) ;
		htmlButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outputType = 2 ;
			}			
		}) ;
		
		//
		// The submit button has an action listener
		// which does most of the hard work ...
		submitButton = new JButton( "Submit" ) ;
		submitButton.addActionListener( new  SubmitActionListener() ) ;
		submitButton.setEnabled( false ) ;
		c.gridx = 0 ;
		c.gridy = 11 ;
		c.gridwidth = 1 ;
		c.gridheight = 1 ;
		c.weighty = 0.0 ;
		c.fill = GridBagConstraints.HORIZONTAL ;
		panel.add( submitButton, c ) ;
		//
		// The reset button does very little,
		// but it's listener re-enables the submit button
		// (You may find this annoying)
		resetButton = new JButton( "Reset" ) ;
		resetButton.addActionListener( new ResetActionListener() ) ;
		c.gridx = 5 ;
		panel.add( resetButton, c ) ;
		
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE ) ;	
		
		//
		// Ensures that the ACR's shutdown service gets the opportunity
		// of a controlled shutdown.
		//
		// (There's probably a small timing exposure here too)
		//
		// A bit pedantic, but we shutdown on a separate thread.
		addWindowListener( new WindowAdapter() {
			public void windowClosing( WindowEvent event ) {
				Thread worker = new Thread() {
					public void run() {
						Shutdown sd1 = getSd() ;
						if( sd1 != null ) {
							sd1.reallyHalt() ;
						}						
					}
				};
				worker.start();				
			}
		} ) ;
		
		//
		// Then we make it visible, but the submit button is not enabled
		// and will only be enabled once the ACR has been successfully
		// retrieved.
		setVisible( true ) ;
	}
	
	//
	// Used by the client to ready the GUI for work...
	public void setReady() {
		outputArea.setText( "Initiliazed! You can now submit a query." ) ; 
		submitButton.setEnabled( true ) ;
	}
	
	//
	// Most of the hard work is done here
	// (and in the remote process listener), 
	// triggered from the submit button.
	class SubmitActionListener implements ActionListener {
		
		public void actionPerformed( ActionEvent e ) {
			//
			// This is a simple client. Allow only one job at a time ...
			submitButton.setEnabled( false ) ;
			collectionCB.setEnabled( false ) ;
			queryArea.setEditable( false ) ;
			//
			// Retrieve the ADQL query string from the work area ...
			final String adqlString = queryArea.getText() ;
			//
			// Then give the user some feed back ...
			outputArea.setText( "Submitting..." ) ;	
			//
			// OK, this we presume is the data collection we wish to query ...
			final String serverIvorn = (String)collectionCB.getSelectedItem() ;
			//
			// The following relationship between server ivorn and application ivorn is informal,
			// depending upon how the system admin of a DSA server registers its details,
			// so it cannot be relied upon in all circumstances.
			// But both can be retrieved from the Registry.
			// (I used the VODesktop to find suitable candidates.)
			final String appIvorn = serverIvorn + "/ceaApplication" ;
			
			Thread worker = new Thread() {
				public void run() {
					try {
						//
						// Set up a suitable template document for this application
						// and then unmarshall it into a tool object suitable for simple
						// manipulation ...
						URI appURI = new URI( appIvorn ) ;
						Document doc = apps.createTemplateDocument( appURI, "ADQL" ) ;
						Tool tool = (Tool) Unmarshaller.unmarshal( Tool.class, doc ) ;
						//
						// Set the input parameter to the ADQL query 
						// and marshall it back into the document ...
						ParameterValue[] pvs = tool.getInput().getParameter() ;
						pvs[0].setValue( adqlString ) ;
						//
						// Try different output formats ...
						// "VOTABLE","VOTABLE-BINARY","COMMA-SEPARATED","HTML"
						if( outputType == 0 ) {
							pvs[1].setValue( "VOTABLE" ) ;
						}
						else if( outputType == 1 ) {
							pvs[1].setValue( "COMMA-SEPARATED" ) ;
						}
						else if( outputType == 2 ) {
							pvs[1].setValue( "HTML" ) ;
						}
												
						doc = doc.getImplementation().createDocument( null, null, null ) ;
						Marshaller.marshal(tool,doc) ;
						// XMLUtils.PrettyDocumentToStream(doc,System.out);
						
						//
						// Submit the query using the remote process manager ...
						URI jobURI = rpm.submitTo( doc, new URI(serverIvorn) ) ;
						SwingUtilities.invokeLater( new Runnable() {
							public void run() {
								outputArea.setText( "Submitted successfully!" ) ;
							}
						}) ;
						//
						// The remote listener deals with problems, output etc...
						rpm.addRemoteProcessListener( jobURI, new JobListener() ) ;
					}
					catch( final Exception ex ) {
						//
						// A problem occurred. Display message in the work area ...
						SwingUtilities.invokeLater( new Runnable() {
							public void run() {
								outputArea.setText( "Problem occurred: " + ex.getLocalizedMessage() ) ;
							}
						}) ;				
					}
				}
			};
			worker.start();
		}
		
	}
	
	//
	// Action listener for the reset button.
	class ResetActionListener implements ActionListener {

		public void actionPerformed( ActionEvent e ) {
			queryArea.setText( "Type query here..." ) ;	
			submitButton.setEnabled( true ) ;
			collectionCB.setEnabled( true ) ;
			queryArea.setEditable( true ) ;
			outputArea.setText( "" ) ;
		}
		
	}

	//
	// Bunch of synchronized getters for ACR stuff.
	// Probably not needed, just Jeff being neurotic.
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
	
	//
	// Remote process listener for the query once it has been submitted.
	class JobListener implements RemoteProcessListener {
		
		public JobListener() {}

		//
		// Concatinate any messages received to the contents of the work area ...
		public void messageReceived( URI arg0, final ExecutionMessage message ) {
			SwingUtilities.invokeLater( new Runnable() {
				public void run() {
					outputArea.setText( outputArea.getText() + "\n" + message.getContent() ) ;
				}
			}) ;
		}
		//
		// Display the results.
		// NOTE. I don't believe this is how the results are
		//       currently displayed in the work area ...
		public void resultsReceived( URI arg0, final Map resultsMap ) {					
			SwingUtilities.invokeLater( new Runnable() {
				public void run() {
					Iterator it = resultsMap.values().iterator() ;
					if( it.hasNext() ) {
						outputArea.setText( it.next().toString() ) ;
					}				
				}
			}) ;		
		}
		//
		// The remote process has sent a status changed message.
		// We are interested in errors or completion ...
		public void statusChanged( final URI jobURI, final String status ) {

			try {
				//
				// If an error (or unknown?), display the status
				// and delete the job ...
				if( status.equalsIgnoreCase( ExecutionInformation.ERROR ) 
					||
					status.equalsIgnoreCase( ExecutionInformation.UNKNOWN )				
				) {
					SwingUtilities.invokeLater( new Runnable() {
						public void run() {
							outputArea.setText( status ) ;
							deleteJob( jobURI ) ;			
						}
					}) ;
				}
				//
				// If query completed, display the results 
				// and delete the job ...
				else if ( status.equalsIgnoreCase( ExecutionInformation.COMPLETED ) ) {
					String results = "" ;
					Iterator it = rpm.getResults( jobURI).values().iterator() ;
					if( it.hasNext() ) {
						results = it.next().toString() ;
					}	
					final String fResults = results;
					SwingUtilities.invokeLater( new Runnable() {
						public void run() {
							outputArea.setText( fResults ) ;
							deleteJob( jobURI ) ;
						}
					}) ;
				}
			}
			catch( final Exception ex ) {
				SwingUtilities.invokeLater( new Runnable() {				
					public void run() {
						outputArea.setText( ex.getLocalizedMessage() ) ;
					}
				} ) ;
			} 
			
		}
		
		//
		// Convenience method for signalling to the remote
		// process that the job is no longer needed ...
		private void deleteJob( final URI jobURI ) { 	
			//
			// Probably need to remove the listener at some point!!!
			// or reposition this within the listener and remove
			// the listener from within the listener.
			Thread worker = new Thread() {

				public void run() {
					try {
						RemoteProcessManager rpm = getRpm() ;
						rpm.delete( jobURI ) ;
						
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