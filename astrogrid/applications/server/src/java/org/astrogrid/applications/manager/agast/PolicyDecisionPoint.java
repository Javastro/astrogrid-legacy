/**
 * 
 */
package org.astrogrid.applications.manager.agast;

import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.util.Random;

import javax.security.auth.x500.X500Principal;

import org.agastpdp.qadi.client.QadiClient;
import org.agastpdp.qadi.client.QadiClientException;
import org.agastpdp.qadi.client.QadiPDPClientImpl;
import org.apache.log4j.Logger;
import org.astrogrid.applications.authorization.AuthorizationPolicy;
import org.astrogrid.applications.authorization.CEAOperation;
import org.astrogrid.applications.manager.persist.ExecutionHistory;
import org.astrogrid.security.SecurityGuard;

/**
 * An Authorization policy that uses the AGAST service to decide.
 * 
 * @author jl99
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 15 May 2009 - changed to use different more CEA specific {@link AuthorizationPolicy}
 *
 */
public class PolicyDecisionPoint implements AuthorizationPolicy {
	
	private static final Logger log = Logger.getLogger( PolicyDecisionPoint.class ) ;
		
	private static final String QADI_URL_KEY = "qadi.url" ;
	private static final String PDPNAME = "cea-pdp" ;
	private static final String METADATA_FILE = "applications-metadata.ttl" ;
	private static final String POLICY_FILE = "applications-policy.owl" ;
	private static final String KNOWN_INSTANCES_FILE = "known-instances.ttl" ;
	
	private static final String JOHN_DOE = "JohnDoe" ;
	private static final String PUBLIC = "public" ;
	
	// Partial Prefix ( It lacks a closing > )
	private static final String PPREFIX = "<http://www.astrogrid.org/agast/ontologies/applications-policy.owl#" ;
 
	private static final String ASK_IS_KNOWN_COMMUNITY_TEMPLATE =
		"prefix : <http://www.astrogrid.org/agast/ontologies/applications-policy.owl#> \n" +
		"ask { \n" + 
		"{ " + PPREFIX + "${community}>  a :LocalCommunity } \n" +
		"UNION \n" +
		"{ " + PPREFIX + "${community}> a :DelegateCommunity } \n" +
		"}" ;
	
	private static final String RUNTIME_INSTANCE_TEMPLATE =
		"@prefix : <http://www.astrogrid.org/agast/ontologies/applications-policy.owl#> . \n" +
		PPREFIX + "${user}> :inCommunity " + PPREFIX + "${community}> . \n" +
		":${job} :hasApplication :${application} . \n" +
		":${job} :hasUser " + PPREFIX + "${user}> . " ;
	
	private static final String ADD_REMOTE_COMMUNITY_TEMPLATE =
		"@prefix : <http://www.astrogrid.org/agast/ontologies/applications-policy.owl#> . \n" +
		PPREFIX + "${community}> a :RemoteCommunity . " ;
	
	private static final String ASK_JOB_OK_TEMPLATE =
		"prefix : <http://www.astrogrid.org/agast/ontologies/applications-policy.owl#> \n" +
		"ask { \n" + 
		"{ :${job} a :HighUsageJob } \n" +
		"UNION \n" +
		"{ :${job} a :MediumUsageJob } \n" +
		"UNION \n" +
		"{ :${job} a :LowUsageJob } \n" +
		"}" ;	
	
	private static final String TMP_PATH =
		"/tmp" ;
	
	private QadiClient qadi ;
	private SecurityGuard securityGuard ;
	private String qadiUrl ;
	private String pdpEndpoint ;
	private String decider ;
	private String community ;
	private String user ;
	private String application ;
	private String job ;
	private final ExecutionHistory eh;
	
	public PolicyDecisionPoint( String qadiUrl, ExecutionHistory executionHistory ) {
		
	       eh = executionHistory;
		try {
			this.qadi = new QadiPDPClientImpl() ;
			//
			// Use the service to create the Policy Decision Point 
			// and add the applications policy to it...
			pdpEndpoint = qadi.createPdp( qadiUrl, getUniquePDPName(), readMetadata(), QadiClient.TURTLE_CONTENT ) ;
			qadi.addPolicy( pdpEndpoint, readPolicy(), QadiClient.RDF_XML_CONTENT ) ;
			//
			// Add all the information we have regarding the local community, any delegate communities
			// and the names of all applications that are controlled by this cea...
			addKnownCommunitiesAndApplications() ;
			
		}
		catch( QadiClientException qcex ) {
			this.qadi = null ;
			log.error( "QADI error: failed to load QADI service, policy and known CEA information.", qcex ) ;
		}
		
	}
	
	/* (non-Javadoc)
	 * @see org.astrogrid.security.authorization.AccessPolicy#decide(org.astrogrid.security.SecurityGuard, java.util.Map)
	 */
	   public void decide(CEAOperation op, String executionID,
	            String applicationID, SecurityGuard caller)
	            throws GeneralSecurityException {
		//
		// Retrieve the user's runtime data (ie: name, community, the application to run, and it's job id).
		// Incidentally, we check here for anonymous access (ie: no user, no community)...
	        job = executionID;
	        application = applicationID;
		retrieveRuntimeData( securityGuard ) ;
		//
		try {
			//
			// Retrieve the qadi service endpoint.
			// This is not a secure way to do it, but OK for the moment...
			// qadiUrl = System.getProperty( QADI_URL_KEY ) ;
			//
			// Use the service to create the Policy Decision Point 
			// and add the applications policy to it...
			// pdpEndpoint = qadi.createPdp( qadiUrl, getUniquePDPName(), readMetadata(), QadiClient.TURTLE_CONTENT ) ;
			// qadi.addPolicy( pdpEndpoint, readPolicy(), QadiClient.RDF_XML_CONTENT ) ;
			//
			// Add all the information we have regarding the local community, any delegate communities
			// and the names of all applications that are controlled by this cea...
			// addKnownCommunitiesAndApplications() ;
			//
			// If this is not anonymous access, 
			// and the community is not amongst our known ones,
			// then we add it as a remote community...
			checkUsersCommunityIsKnown() ;
			//
			// Now we add the user's runtime data (name, community, application, job id) 
			// to the ontology/policy...
			addRunTimeData() ;
			//
			// Will the job be permitted by our policy? ...
			if( !isJobPermitted() ) {
				throw new GeneralSecurityException( 
						application + " exceeds your authorised level of resource usage." ) ;
			}
			return ;
		} catch (QadiClientException e) {
            throw new GeneralSecurityException("problem communicating with policy server", e);
        }
		finally {
//			cleanup() ;
		}
		
	}
	
	private void retrieveRuntimeData( SecurityGuard securityGuard) {	
		this.securityGuard = securityGuard ;
		//
		// If the application name is its ivorn, just use the name.
		if( application.contains( "://" ) ) {
			application = application.substring( application.lastIndexOf( "/" )+1 ) ;
		}

		//
		// Check for anonymous access...
		if( isAnonymousAccess() ) {
			user = JOHN_DOE ;
			community = PUBLIC ;
		}
		else {
			//
			// We have a signed-on user...
			X509Certificate certificate = securityGuard.getIdentityCertificate() ;
									
			X500Principal p = securityGuard.getX500Principal() ;
			user = p.getName().replaceAll( " ", "_") ;
						
			p = certificate.getIssuerX500Principal() ;			
			community = p.getName().replaceAll( " ", "_" ) ;
		}
		
		if( log.isDebugEnabled() ) {
			log.debug( "\nuser: " + user 
					 + "\ncommunity: " + community 
					 + "\napplication: " + application
					 + "\njob: " + job ) ;
		}
	}
	
	private boolean isAnonymousAccess() {
		return this.securityGuard.getX500Principal() == null ;
	}
	
	private String readMetadata() {
		return read( this.getClass().getClassLoader().getResourceAsStream( METADATA_FILE ) ) ;
	}
	
	private String readPolicy() {
		return read( this.getClass().getClassLoader().getResourceAsStream( POLICY_FILE ) ) ;
	}
	
	private void addKnownCommunitiesAndApplications() throws QadiClientException {
		String knownInstances = read( this.getClass().getClassLoader().getResourceAsStream( KNOWN_INSTANCES_FILE ) ) ;
		setDecider( qadi.addInstanceData( pdpEndpoint, knownInstances, QadiClient.TURTLE_CONTENT ) ) ;
	}
	
	private void checkUsersCommunityIsKnown() throws QadiClientException {
		if( isAnonymousAccess() == false ) {
			String askIsCommunityKnown = ASK_IS_KNOWN_COMMUNITY_TEMPLATE.replaceAll( "\\$\\{community\\}", community ) ;
			String result = qadi.runQuery( decider, askIsCommunityKnown , QadiClient.TEXT_CONTENT ) ;
			if( log.isDebugEnabled() ) {
				log.debug( "askIsCommunityKnown returned: " + result ) ;
			}
			if( !isTrue( result ) ) {
				String addCommunityAsRemote = ADD_REMOTE_COMMUNITY_TEMPLATE.replaceAll( "\\$\\{community\\}", community ) ;
				setDecider( qadi.addInstanceData( decider, addCommunityAsRemote, QadiClient.TURTLE_CONTENT ) ) ;
			}
		}
	}
		
	private void addRunTimeData() throws QadiClientException {
		String runtimeData = RUNTIME_INSTANCE_TEMPLATE
								.replaceAll( "\\$\\{community\\}", community )
								.replaceAll( "\\$\\{application\\}", application )
								.replaceAll( "\\$\\{user\\}", user ) 
								.replaceAll( "\\$\\{job\\}", job ) ;
		if( log.isDebugEnabled() ) {
			log.debug( "addRunTimeData: \n" + runtimeData ) ;
		}
		setDecider( qadi.addInstanceData( decider, runtimeData, QadiClient.TURTLE_CONTENT ) ) ;
	}
	
	private boolean isJobPermitted() throws QadiClientException {
		String askJobOK = ASK_JOB_OK_TEMPLATE.replaceAll( "\\$\\{job\\}", job ) ;
		String result = qadi.runQuery( decider, askJobOK , QadiClient.TEXT_CONTENT ) ;
		if( log.isDebugEnabled() ) {
			log.debug( "askJobOK returned: " + result ) ;
		}
		return isTrue( result ) ;
	}
	
	private void cleanup() {
		//
		// Needs revision to remove some instance data
		try {
			if( pdpEndpoint != null ) {
				qadi.deletePdp( pdpEndpoint ) ;
				pdpEndpoint = null ;
				decider = null ;
			}				
		}
		catch( QadiClientException ex ) {
			;
		}
	}
	
	private String read( InputStream s ) {
        StringBuffer buffer = new StringBuffer( 1024 ) ;
        try {
            int ch = s.read() ;
            while( ch != -1 ) {
                buffer.append( (char)ch ) ;
                ch = s.read() ;
            }
        }
        catch( Exception iox ) {
            ;
        }
        finally {
            try { s.close() ; } catch( Exception ex ) { ; }
        }
        return buffer.toString() ;
    }
	
	private void setDecider( String url ) {
		if( url != null ) {
			this.decider = url ;
		}
	}
	
	private String getUniquePDPName() {
		return PDPNAME + "-" + new Random().nextInt() ;
	}
	
	private boolean isTrue( String b ) {
		if( b.equalsIgnoreCase( "yes" ) )
			return true ;
		return Boolean.valueOf( b ).booleanValue() ;
	}

 
}
