package org.astrogrid.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.Finder;
import org.astrogrid.acr.builtin.Shutdown ;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.Applications;
import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.acr.astrogrid.ExecutionMessage;
import org.astrogrid.acr.astrogrid.RemoteProcessListener;
import org.astrogrid.acr.astrogrid.RemoteProcessManager;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.workflow.beans.v1.Tool;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.Document;

public class ClientWithACR {
	
	private static Log log = LogFactory.getLog( ClientWithACR.class ) ;
	
	private SimpleQueryBuilder sqb ;
	
	Finder f = new Finder();
	ACR ar ; 
	Applications apps ;
	RemoteProcessManager rpm ;
	Shutdown sd ;
	String appIvorn = "ivo://wfau.roe.ac.uk/sdssdr5-dsa/dsa/ceaApplication" ;
	String serverIvorn = "ivo://wfau.roe.ac.uk/sdssdr5-dsa/dsa" ;
	String adqlQuery = 
		    "Select Top 100 a.ra, a.dec, a.psfMag_g, a.psfMag_r " +
		    "From PhotoTag as a " +
			"Where a.ra>110 And a.ra<230 And a.dec>20 And a.dec<=22 " +
			" And  (a.psfMag_g-a.psfMag_r <0.4 )  " +
			" And a.psfMag_r>20.0 And a.psfMag_g>0 And a.mode=1 And a.probPSF=1" ;
	
	public static void main( String argv[] ) {		
		ClientWithACR client = new ClientWithACR() ;		
		client.execute() ;
	}
	
	public ClientWithACR() {
		sqb = new SimpleQueryBuilder() ;
		
		
		
	}
	
	public void execute() {
		if( log.isTraceEnabled() ) log.trace( "enter: execute()" ) ;
		try {
						
			System.setProperty("java.awt.headless","true");
			ar = f.find() ;
			System.setProperty("java.awt.headless","false");
			
			apps = (Applications) ar.getService(Applications.class) ;
			rpm = (RemoteProcessManager) ar.getService( RemoteProcessManager.class ) ;
			sd = (Shutdown) ar.getService( Shutdown.class ) ;
			URI appURI = new URI( appIvorn ) ;
			Document doc = apps.createTemplateDocument( appURI, "ADQL" ) ;
			Tool tool = (Tool) Unmarshaller.unmarshal( Tool.class, doc ) ;
			ParameterValue[] pvs = tool.getInput().getParameter() ;
			pvs[0].setValue( adqlQuery ) ;
			doc = doc.getImplementation().createDocument( null, null, null ) ;
			Marshaller.marshal(tool,doc) ;
//			XMLUtils.PrettyDocumentToStream(doc,System.out);
			
			URI jobURI = rpm.submitTo( doc, new URI(serverIvorn) ) ;
			//
			// Need to set up a listener here...
			rpm.addRemoteProcessListener( jobURI, new JobListener() ) ;
						
			
			
		}
		catch( URISyntaxException  usx ) {
			log.error( "", usx ) ;
		}
		catch( InvalidArgumentException iax ) {
			log.error( "", iax ) ;
		}
		catch( NotFoundException nfx ) {
			log.error( "", nfx ) ;
		}
		catch( ServiceException sx ) {
			log.error( "", sx ) ;
		}
		catch( ACRException acrx ) {
			log.error( "", acrx ) ;
		}
		catch( ValidationException vx ) {
			log.error( "", vx ) ;
		}
		catch( MarshalException mx ) {
			log.error( "", mx ) ;
		}
		
		if( log.isTraceEnabled() ) log.trace( "exit : execute()" ) ;	
	}
	
	class JobListener implements RemoteProcessListener {
		
		//
		// Question. If this is made more sophisticated,
		// will thread safety need to be taken into account,
		// or is this guaranteed by the calling environment?
		
		public JobListener() {}

		public void messageReceived( URI arg0, ExecutionMessage message ) {
			log.info( "Message:\n" + message.getContent() ) ;
		}

		public void resultsReceived( URI arg0, Map resultsMap ) {
			log.info( "Results:\n" + resultsMap.toString() ) ;			
		}

		public void statusChanged( URI jobURI, String status ) {

			try {
				if( status.equalsIgnoreCase( ExecutionInformation.ERROR ) 
					||
					status.equalsIgnoreCase( ExecutionInformation.UNKNOWN )				
				) {
					log.error( "Error status on ClientWithACR." + status ) ;
					rpm.delete( jobURI ) ;
					sd.reallyHalt() ;
				}
				else if ( status.equalsIgnoreCase( ExecutionInformation.COMPLETED ) ) {
					log.info( "COMPLETED status on ClientWithACR." ) ;
					log.info( "Results: " + rpm.getResults( jobURI) ) ;
					rpm.delete( jobURI ) ;
					sd.reallyHalt() ;
				}
			}
			catch( Exception ex ) {
				log.error( "", ex ) ;
			} 
			
		}
		
	}

}
