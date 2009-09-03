package org.astrogrid.client;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.Finder;
import org.astrogrid.acr.astrogrid.Applications;
import org.astrogrid.acr.astrogrid.RemoteProcessManager;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.builtin.Shutdown;

//
// A simple test bed for trying out a Swing GUI with
// the AstroGrid ACR running headless.
//
// To be run with the vodesktop-1.3-app.jar or similar
// running on the classpath.
//
// Tested under jdk 1.5.0_15
//
// Problems:
// (1) Do not have the vodesktop running before this is started.
// (2) No way of serializing large results set at the moment,
//     so all queries should limit output (Select top 100 or so).
// (3) At present only supports ADQL v1 syntax.
public class ClientWithACR {
	
	Finder f = new Finder();
	ACR ar ; 
	Applications apps ;
	RemoteProcessManager rpm ;
	Shutdown sd ;
	
	public static void main( String argv[] ) {	
		
		final ClientWithACR client = new ClientWithACR() ;	
		//
		// This brings up the GUI, which is a simple panel 
		// consisting of a combo box of hard-coded DSA services, 
		// a text area for queries/messages/results,
		// a submit button and a reset button.
		// The submit button is initially not enabled.
		final SimpleQueryBuilder sqb = new SimpleQueryBuilder() ;
		try {
			//
			// This retrieves the ACR within the same JVM provided
			// the vodesktop jar is on the classpath.
			client.retrieveACR() ;
			//
			// Sets up requisite state for the GUI to use the ACR...
			sqb.setACR( client.ar, client.apps, client.rpm, client.sd ) ;
			//
			// Enables the submit button
			sqb.setReady() ;
		}
		catch( final ACRException ex ) {
			//
			// If anything goes wrong, a message should appear in the GUI
			sqb.setException( ex ) ;
		}

	}
	
	public ClientWithACR() {}
	
	public void retrieveACR() throws ACRException {	
		try {
			//
			// Retrieve the ACR...
			// I think this needs to be done on the main thread.
			// (But it might be worth experimentation).
			System.setProperty("java.awt.headless","true");
			ar = f.find() ;

			//
			// I suspect these other artifacts could be retrieved on
			// a background thread...

			//
			// This is the service that will do the query...
			apps = (Applications) ar.getService( Applications.class ) ;
			//
			// This is a convenience object for managing the remote process...
			rpm = (RemoteProcessManager) ar.getService( RemoteProcessManager.class ) ;
			//
			// This is to manage shutdown in a controlled fashion...
			sd = (Shutdown) ar.getService( Shutdown.class ) ;			
		}
		finally {
			System.setProperty("java.awt.headless","false");
		}
		
	}

}
