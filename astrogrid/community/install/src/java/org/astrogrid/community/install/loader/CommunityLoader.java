/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/install/src/java/org/astrogrid/community/install/loader/CommunityLoader.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/23 16:34:08 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityLoader.java,v $
 *   Revision 1.2  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.1.2.1  2004/03/20 06:54:11  dave
 *   Added addAccount(AccountData) to PolicyManager et al.
 *   Added XML loader for AccountData.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.install.loader ;

import java.net.URL ;
import java.net.URISyntaxException ;
import java.net.MalformedURLException ;

import java.io.IOException ;

import java.util.Iterator ;
import java.util.Collection ;

import org.xml.sax.InputSource;

import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.CastorException ;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.registry.RegistryException;

import org.astrogrid.community.common.ivorn.CommunityServiceIvornFactory ;

import org.astrogrid.community.common.policy.data.AccountData ;
import org.astrogrid.community.common.exception.CommunityPolicyException ;
import org.astrogrid.community.common.exception.CommunityServiceException ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

import org.astrogrid.community.client.policy.manager.PolicyManagerDelegate ;
import org.astrogrid.community.resolver.policy.manager.PolicyManagerResolver ;
import org.astrogrid.community.resolver.exception.CommunityResolverException ;

 /**
  * A utility to load Community data from an XML file.
  *
  */
public class CommunityLoader
	{
    /**
     * Switch for our debug statements.
     * @todo Refactor to use the common logging.
     *
     */
    private static boolean DEBUG_FLAG = true ;

	/**
	 * The default name for our XML mapping file.
	 *
	 */
	private static final String XML_MAPPING_NAME = "/org/astrogrid/community/install/loader/mapping.xml" ;

	/**
	 * Public constructor.
	 *
	 */
	public CommunityLoader()
		{
		}

	/**
	 * Our Community data.
	 *
	 */
	private CommunityLoaderData data ;

	/**
	 * Load our Community data from our source.
	 *
	 */
	public void load(URL source)
		throws IOException, CommunityServiceException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("CommunityLoader.load()") ;
		//
		// Check for null param.
		if (null == source)
			{
			throw new CommunityServiceException(
				"Null Community data URL"
				) ;
			}
		//
		// Try parsing the data source.
		try {
			//
			// Locate our XML mapping resource.
			URL map = this.getClass().getResource(
				XML_MAPPING_NAME
				) ;
			if (null == map)
				{
				throw new CommunityServiceException(
					"Unable to locate XML mapping"
					) ;
				}
			//
			// Create our XML mapping.
			Mapping mapping = new Mapping();
			//
			// Load our XML mapping resource.
			mapping.loadMapping(map) ;
			if (DEBUG_FLAG) System.out.println("PASS : Got mapping") ;
			//
			// Create our marshaller.
			Unmarshaller marshaller = new Unmarshaller(mapping);
			if (DEBUG_FLAG) System.out.println("PASS : Got marshaller") ;
			//
			// Load the Community data.
			this.data = (CommunityLoaderData) marshaller.unmarshal(
				new InputSource(
					source.openStream()
					)
				) ;
			if (DEBUG_FLAG) System.out.println("PASS : Got data") ;
			}
		catch (CastorException ouch)
			{
			if (DEBUG_FLAG) System.out.println("----\"----") ;
			if (DEBUG_FLAG) System.out.println("Castor Exception") ;
			if (DEBUG_FLAG) System.out.println(ouch) ;
			throw new CommunityServiceException(
				"Error occurred loading Community data",
				ouch
				) ;
			}
		catch (MappingException ouch)
			{
			if (DEBUG_FLAG) System.out.println("----\"----") ;
			if (DEBUG_FLAG) System.out.println("Mapping Exception") ;
			if (DEBUG_FLAG) System.out.println(ouch) ;
			throw new CommunityServiceException(
				"Error occurred loading Community data",
				ouch
				) ;
			}
		}

	/**
	 * Our PolicyManager resolver.
	 *
	 */
	private PolicyManagerResolver resolver = new PolicyManagerResolver() ;

	/**
	 * Resolve an ident.
	 *
	 */
	private Ivorn ivorn(String ident)
		throws CommunityIdentifierException
		{
		try {
			return new Ivorn(
				ident
				) ;
			}
		catch (URISyntaxException ouch)
			{
			throw new CommunityIdentifierException(
				ouch
				) ;
			}
		}

	/**
	 * Upload our Community data.
	 *
	 */
	public void upload()
		throws RegistryException, CommunityServiceException, CommunityIdentifierException, CommunityResolverException, CommunityPolicyException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("CommunityLoader.upload()") ;
		//
		// Check for null data.
		if (null == data)
			{
			throw new CommunityServiceException(
				"No community data loaded"
				) ;
			}
		//
		// Create our Community Ivorn.
		Ivorn ivorn = this.ivorn(
			this.data.getIdent()
			) ;
		if (DEBUG_FLAG) System.out.println("  Ivorn : " + ivorn) ;
		//
		// Try resolving our Community service.
		PolicyManagerDelegate manager = resolver.resolve(
			ivorn
			) ;
		//
		// Process the Community Accounts.
		Iterator iter = data.getAccounts().iterator() ;
		while (iter.hasNext())
			{
			AccountData account = (AccountData) iter.next() ;
			if (DEBUG_FLAG) System.out.println("----") ;
			if (DEBUG_FLAG) System.out.println("Account : " + account.getIdent()) ;
			manager.addAccount(account) ;
			}
		}


	/**
	 * Try the Castor tools.
	 *
	public void castor()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("CommunityLoader.castor()") ;
		//
		// Load the XML mapping information.
		Mapping mapping = new Mapping();
		mapping.loadMapping("mapping.xml");
		//
		// Load the Community data.
		Unmarshaller marshaller = new Unmarshaller(mapping);
		CommunityData data = (CommunityData) marshaller.unmarshal(
			new InputSource(
				new FileReader(
					"data.xml"
					)
				)
			) ;
		//
		// Display our list of Accounts.
		Iterator iter = data.getAccounts().iterator() ;
		while (iter.hasNext())
			{
			AccountData account = (AccountData) iter.next() ;
			if (DEBUG_FLAG) System.out.println("  ----\"----") ;
			if (DEBUG_FLAG) System.out.println("  Ident       : " + account.getIdent()) ;
			if (DEBUG_FLAG) System.out.println("  Display     : " + account.getDisplayName()) ;
			if (DEBUG_FLAG) System.out.println("  Description : " + account.getDescription()) ;
			if (DEBUG_FLAG) System.out.println("  Home space  : " + account.getHomeSpace()) ;
			if (DEBUG_FLAG) System.out.println("  Email       : " + account.getEmailAddress()) ;
			}
		}
	 */

	}

