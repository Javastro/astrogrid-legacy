package org.astrogrid.mySpace.mySpaceManager;

import java.io.*;

/**
 * The <code>UserAccount</code> class represents details of an AstroGrid
 * User within the MySpace system.  It encapsulates details such as his
 * identifier (ID), community and (in principle) any privileges that he
 * might have.
 * <p>
 * Many of the details of how Users are to be represented are still
 * unclear, so in Iteration 2 the class is largely a dummy.  It contains
 * some details for representing the user, but no information about, for
 * example, privileges.  Further, most of the MySpace system merely passes
 * <code>UserAccount</code> objects about, but does not inquire they
 * contents, precisely because they are uncertain.
 * </p>
 * <p>
 * A <code>UserAccount</code> object is created immediately the MySpace
 * system in invoked, and is assembled using details passed about the
 * user.  In future iterations information might be passed allowing the
 * object to be created directly.
 *
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 3.
 *
 */

public class UserAccount
{

//Public constants defining the permitted codes for attempted read,
//write and delete operations.

   public static final int READ   = 1;      // Read.
   public static final int WRITE  = 2;      // Write.
   public static final int DELETE = 3;      // Delete.

   private String userID;
   private String communityID;
   private String userName;

//
// Constructors.

/**
 * The simplest constructor to create a new <code>UserAccount</code>.
 * Identifiers for the User and the community to which he belongs must
 * be supplied.  For Iteration 2 these identifiers are assumed to be
 * simple strings.
 */

   public UserAccount (String userID, String communityID)
   {  this.userID = userID;
      this.communityID = communityID;
      this.userName = "unknown";
   }

/**
 * A constructor to create a new UserAccount in the case where the
 * User's identifier, community and name are all known.
 */

   public UserAccount (String userID, String communityID, String userName)
   {  this.userID = userID;
      this.communityID = communityID;
      this.userName = userName;
   }

//
// Methods.

/**
 * Return an identifier for the User which is globally unique within the
 * AstroGrid system.  The User's identifier is assumed to be unique within
 * his community and the community identifiers are assumed to be unique.
 * Therefore a globally unique identifier is created by concatenating the
 * User identifier and the community identifier.  A colon (`:') is
 * inserted as a separator between the user and community identifiers.
 */

   public String getURI()
   {  return userID + ":" + communityID;
   }

/**
 * Return the User's real name.
 */

   public String getUserName()
   {  return userName;
   }
/**
 * Return the User's identifier.
 */

   public String getUserID()
   {  return userID;
   }

/**
 * Return the User's community identifier.
 */

   public String getCommunityID()
   {  return communityID;
   }

/**
 * Return the User's base container.
 *
 * The base container starts with a leading `/'.  It then comprises
 * the user's userID concatenated with the his communityID.  The
 * combination of userID and communityID should be unique throughout
 * the AstroGrid system.
 */

   public String getBaseContainer()
   {  return "/" +  userID + communityID;
   }

//
// Authentication and Authorisation methods.
// 
// The two following methods, checkAuthentication and checkAuthorisation,
// should not be confused.  checkAuthentication is used to check that
// a User is an accredited user of both AstroGrid and the current MySpace
// System, whereas checkAuthorisation is used to determine whether a user
// has the necessary privileges to perform a given operation on a given
// DataHolder.  For example, I might be an accredited user of a given
// MySpace System, but nonetheless I'm unlikely to have sufficient
// privileges to delete another user's DataHolders.

/**
 * Check that the <code>UserAccount</code> is an accredited user of
 * both AstroGrid and the current MySpace System.  The current
 * implementation is a dummy which always returns true.
 *
 * @returns Returns true if the user authenticates ok, otherwise returns
 *    false.
 */

  public boolean checkAuthentication()
  {  return true;
  }

/**
 * Check whether the <code>UserAccount</code> has the necessary
 * privileges to perform a given operation on a given
 * <code>DataHolder</code>.  The current implementation is a dummy which
 * always returns true.
 * 
 * @param oper The operation to be performed, coded as follows:
 * <code>UserAccount.READ</code> - read; <code>UserAccount.WRITE</code> -
 * write; <code>UserAccount.DELETE</code> - delete.
 * @param ownerID The identifer of the owner of the <code>DataHolder</code>
 * on which the operation is to be performed.
 * @param permissions The permissions mask of the <code>DataHolder</code>
 * on which the operation is to be performed.
 * @returns Returns true if the user has the necessary privileges to
 * perform the requested given operation, otherwise returns false.
 */

  public boolean checkAuthorisation(int oper, String ownerID,
    String permissions)
  {  return true;
  }


/**
  * Check that the user can modify (create, change, delete) new users
  * of the MySpace system.
  */

  public boolean checkCanModifyUsers()
  {  return true;
  }


/**
 * Produce a reasonable string representation of a
 * <code>UserAccount</code>.
 */

   public String toString()
   {  return userID + ":" + communityID + " (" + userName + ")";
   }
}
