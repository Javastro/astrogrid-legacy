/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/server/Attic/PolicyManagerImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/06 20:10:07 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyManagerImpl.java,v $
 *   Revision 1.4  2003/09/06 20:10:07  dave
 *   Split PolicyManager into separate components.
 *
 *   Revision 1.3  2003/09/04 23:33:05  dave
 *   Implemented the core account manager methods - needs data object to return results
 *
 *   Revision 1.2  2003/09/03 15:23:33  dave
 *   Split API into two services, PolicyService and PolicyManager
 *
 *   Revision 1.1  2003/09/03 06:39:13  dave
 *   Rationalised things into one set of SOAP stubs and one set of data objects for both client and server.
 *
 *   Revision 1.1  2003/08/28 17:33:56  dave
 *   Initial policy prototype
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.server ;

import java.rmi.RemoteException ;

import java.util.Vector ;
import java.util.Collection ;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.PersistenceException ;
import org.exolab.castor.jdo.ObjectNotFoundException ;
import org.exolab.castor.jdo.DatabaseNotFoundException ;
import org.exolab.castor.jdo.DuplicateIdentityException ;
import org.exolab.castor.jdo.TransactionNotInProgressException ;
import org.exolab.castor.jdo.ClassNotPersistenceCapableException ;

import org.astrogrid.community.policy.data.GroupData ;
import org.astrogrid.community.policy.data.ServiceData ;
import org.astrogrid.community.policy.data.AccountData ;
import org.astrogrid.community.policy.data.CommunityData ;

public class PolicyManagerImpl
	implements PolicyManager
	{
	/**
	 * Switch for our debug statements.
	 *
	 */
	protected static final boolean DEBUG_FLAG = true ;

	/**
	 * Public constructor.
	 *
	 */
	public PolicyManagerImpl()
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("PolicyManagerImpl()") ;
		//
		// Initialise our service.
		this.init() ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		}

	/**
	 * Our DatabaseManager.
	 *
	 */
	private DatabaseManager databaseManager ;

	/**
	 * Our AccountManager.
	 *
	 */
	private AccountManager accountManager ;

	/**
	 * Our GroupManager.
	 *
	 */
	private GroupManager groupManager ;

	/**
	 * Our CommunityManager.
	 *
	 */
	private CommunityManager communityManager ;

	/**
	 * Initialise our service.
	 *
	 */
	public void init()
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("PolicyManagerImpl.init()") ;

		//
		// Initialise our DatabaseManager.
		databaseManager = new DatabaseManagerImpl() ;
		//
		// Initialise our AccountManager.
		accountManager = new AccountManagerImpl(databaseManager.getDatabase()) ;
		//
		// Initialise our GroupManager.
		groupManager = new GroupManagerImpl(databaseManager.getDatabase()) ;
		//
		// Initialise our CommunityManager.
		communityManager = new CommunityManagerImpl(databaseManager.getDatabase()) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Service health check.
	 *
	 */
	public ServiceData getServiceStatus()
		throws RemoteException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("PolicyManagerImpl.getServiceStatus()") ;

		ServiceData result =  new ServiceData() ;
		result.setIdent("localhost") ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return result ;
		}

	/**
	 * Create a new Account.
	 * TODO Change this to only accept the account name.
	 *
	 */
	public AccountData addAccount(AccountData account)
		throws RemoteException
		{
		return accountManager.addAccount(account) ;
		}

	/**
	 * Request an Account details.
	 *
	 */
	public AccountData getAccount(String ident)
		throws RemoteException
		{
		return accountManager.getAccount(ident) ;
		}

	/**
	 * Update an Account details.
	 *
	 */
	public AccountData setAccount(AccountData account)
		throws RemoteException
		{
		return accountManager.setAccount(account) ;
		}

	/**
	 * Delete an Account.
	 *
	 */
	public boolean delAccount(String ident)
		throws RemoteException
		{
		return accountManager.delAccount(ident) ;
		}

	/**
	 * Request a list of Accounts.
	 *
	 */
	public Object[] getAccountList()
		throws RemoteException
		{
		return accountManager.getAccountList() ;
		}

	/**
	 * Create a new Group.
	 * TODO Change this to only accept the group name.
	 *
	 */
	public GroupData addGroup(GroupData group)
		throws RemoteException
		{
		return groupManager.addGroup(group) ;
		}

	/**
	 * Request an Group details.
	 *
	 */
	public GroupData getGroup(String ident)
		throws RemoteException
		{
		return groupManager.getGroup(ident) ;
		}

	/**
	 * Update an Group details.
	 *
	 */
	public GroupData setGroup(GroupData group)
		throws RemoteException
		{
		return groupManager.setGroup(group) ;
		}

	/**
	 * Delete an Group.
	 *
	 */
	public boolean delGroup(String ident)
		throws RemoteException
		{
		return groupManager.delGroup(ident) ;
		}

	/**
	 * Request a list of Groups.
	 *
	 */
	public Object[] getGroupList()
		throws RemoteException
		{
		return groupManager.getGroupList() ;
		}

	/**
	 * Create a new Community.
	 * TODO Change this to only accept the community name.
	 *
	 */
	public CommunityData addCommunity(CommunityData community)
		throws RemoteException
		{
		return communityManager.addCommunity(community) ;
		}

	/**
	 * Request an Community details.
	 *
	 */
	public CommunityData getCommunity(String ident)
		throws RemoteException
		{
		return communityManager.getCommunity(ident) ;
		}

	/**
	 * Update an Community details.
	 *
	 */
	public CommunityData setCommunity(CommunityData community)
		throws RemoteException
		{
		return communityManager.setCommunity(community) ;
		}

	/**
	 * Delete an Community.
	 *
	 */
	public boolean delCommunity(String ident)
		throws RemoteException
		{
		return communityManager.delCommunity(ident) ;
		}

	/**
	 * Request a list of Communitys.
	 *
	 */
	public Object[] getCommunityList()
		throws RemoteException
		{
		return communityManager.getCommunityList() ;
		}



	}



