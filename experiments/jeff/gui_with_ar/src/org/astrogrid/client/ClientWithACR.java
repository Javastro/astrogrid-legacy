package org.astrogrid.client;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.Finder;
import org.astrogrid.acr.astrogrid.Applications;
import org.astrogrid.acr.astrogrid.RemoteProcessManager;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.builtin.Shutdown;

public class ClientWithACR {
	
	Finder f = new Finder();
	ACR ar ; 
	Applications apps ;
	RemoteProcessManager rpm ;
	Shutdown sd ;
	
	public static void main( String argv[] ) {	
		
		final ClientWithACR client = new ClientWithACR() ;	
		final SimpleQueryBuilder sqb = new SimpleQueryBuilder() ;
		try {
			client.retrieveACR() ;
			sqb.setACR( client.ar, client.apps, client.rpm, client.sd ) ;
			sqb.setReady() ;
		}
		catch( final ACRException ex ) {
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
