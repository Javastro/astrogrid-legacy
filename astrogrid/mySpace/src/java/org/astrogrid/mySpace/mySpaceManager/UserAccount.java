// package org.astrogrid.mySpace.mySpaceRegistry;

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
 * @version Iteration 2.
 *
 */

public class UserAccount
{  private String userID;
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
 * Produce a reasonable string representation of a
 * <code>UserAccount</code>.
 */

   public String toString()
   {  return userID + ":" + communityID + " (" + userName + ")";
   }
}
