package org.astrogrid.mySpace.mySpaceManager;

import java.io.*;

import org.astrogrid.community.common.util.CommunityMessage;
import org.astrogrid.community.delegate.policy.PolicyServiceDelegate;

import org.astrogrid.mySpace.mySpaceStatus.MySpaceStatus;
import org.astrogrid.mySpace.mySpaceStatus.MySpaceStatusCode;

/**
 * The <code>UserAccount</code> class represents details of an AstroGrid
 * User within the MySpace system.  It encapsulates details such as his
 * identifier (ID), community and any privileges that he might have.
 * <p>
 * Many of the details of how Users are to be represented are still
 * unclear, so in Iteration 3 some of the details of the class are
 * provisional.  Most of the MySpace system merely passes
 * <code>UserAccount</code> objects about, but does not inquire they
 * contents, precisely because they may change in future iterations.
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

   private String userId;
   private String communityId;
   private String credentials;
   private String userName;

//
// Constructors.

/**
 * The simplest constructor to create a new <code>UserAccount</code>.
 * Note that in Iteration 3 all the arguments are simple Strings.
 *
 * @param userId User identifier; the user's unique identifier within
 *   his AstroGrid community.
 * @param communityId Community identifier; the unique identifier for
 *   the user's AstroGrid community.
 * @param credentials The user's credentials; a String specifying the
 *   operations that he is allowed to perform.
 */

   public UserAccount (String userId, String communityId, String credentials)
   {  this.userId = userId;
      this.communityId = communityId;
      this.credentials = credentials;
      this.userName = null;
   }

/**
 * A constructor to create a new UserAccount in the case where in
 * addition to the mandatory parameters a (human-readable) name for
 * the user is also known.
 *
 * @param userId User identifier; the user's unique identifier within
 *   his AstroGrid community.
 * @param communityId Community identifier; the unique identifier for
 *   the user's AstroGrid community.
 * @param credentials The user's credentials; a String specifying the
 *   operations that he is allowed to perform.
 * @param userName The user's name (in a human-readable form)
 */

   public UserAccount (String userId, String communityId, String credentials,
     String userName)
   {  this.userId = userId;
      this.communityId = communityId;
      this.credentials = credentials;
      this.userName = userName;
   }

//
// Methods.

/**
 * Return an identifier for the User which is globally unique within the
 * AstroGrid system.  The User's identifier is assumed to be unique within
 * his community and the community identifiers are assumed to be unique.
 * Therefore a globally unique identifier is created by concatenating the
 * User identifier and the community identifier.  An `@'is inserted as a
 * separator between the user and community identifiers.
 */

   public String getUserAGrId()
   {  return userId + "@" + communityId;
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

   public String getUserId()
   {  return userId;
   }

/**
 * Return the User's community identifier.
 */

   public String getCommunityId()
   {  return communityId;
   }

/**
 * Return the User's credentials.
 */

   public String getCredentials()
   {  return credentials;
   }

/**
 * Return the User's base container.
 *
 * The base container starts with a leading `/'.  It then comprises
 * the user's userId concatenated with the his communityId.  The
 * combination of userId and communityId should be unique throughout
 * the AstroGrid system.
 */

   public String getBaseContainer()
   {  return "/" +  userId + "@" + communityId;
   }

//
// Authentication and Authorisation methods.
// 
// The UserAccount class, and indeed, the MySpace system does not
// perform any user authentication.  That is, no checks are made to
// ensure that the user is who he says that he is.  Such checks are
// assumed to have been made elsewhere in the AstroGrid system.
//
// There are, however, two methods for checking the user's authorisation.
// That is, whether he is permitted to perform the operation that he
// is attempting:
//
// checkAuthorisation: provides fine-grained checks on operations
//   attempted on individual dataHolders.  The current implementation
//   is a dummy,
//
// checkSystemAuthorisation: provides a coarse-grained check on whether
//   the user is permitted to use the MySpace system for a given class
//   of operations.  Currently a simple implementation is available.
//
// The difference in usage is (typically) that checkAuthorisation would
// be invoked every time an operation is attempted on a dataHolder,
// whereas checkSystemAuthorisation is be invoked once by every `action'
// method in the MySpaceActions class.

/**
 * Check whether the <code>UserAccount</code> has the necessary
 * privileges to perform a given operation on a given
 * <code>DataHolder</code>.  The current implementation is a dummy which
 * always returns true.
 * 
 * @param opCode The operation to be performed, coded as follows:
 * <code>UserAccount.READ</code> - read; <code>UserAccount.WRITE</code> -
 * write; <code>UserAccount.DELETE</code> - delete.
 * @param ownerID The identifer of the owner of the <code>DataHolder</code>
 * on which the operation is to be performed.
 * @param permissions The permissions mask of the <code>DataHolder</code>
 * on which the operation is to be performed.
 * @returns Returns true if the user has the necessary privileges to
 * perform the requested given operation, otherwise returns false.
 */

  public boolean checkAuthorisation(int opCode, String ownerID,
    String permissions)
  {  return true;
  }

/**
 * Check whether the <code>UserAccount</code> has the necessary
 * privileges to perform a given class of operation on the current
 * MySpace system.
 *
 * @param opCode <code>UserAccount.READ</code> code for the class of
 *   operation being attempted.
 */

  public boolean checkSystemAuthorisation(int opCode)
  {  boolean authorised = true;

     String oper = null;

//
//  Translate the operation codes into the form required by the
//  permissions manager.

     if (opCode == UserAccount.READ)
     {  oper = "read";
     }
     else if (opCode == UserAccount.WRITE)
     {  oper = "write";
     }
     else
     {  authorised = false;

        MySpaceStatus status  = new MySpaceStatus(
           MySpaceStatusCode.AGMMCE00050, MySpaceStatusCode.ERROR,
           MySpaceStatusCode.NOLOG, this.getClassName() );
     }

//
//  If ok then create a permissions manager delegate and check whether
//  the user is authorised for this type of operation.

     if (authorised)
     {  PolicyServiceDelegate psd = new PolicyServiceDelegate();

        try
        {  String agUserId = this.getUserAGrId();

           authorised = psd.checkPermissions(agUserId, credentials,
             "myspace", oper);
        }
        catch (Exception e)
        {  authorised = false;

           MySpaceStatus status  = new MySpaceStatus(
              MySpaceStatusCode.AGMMCE00050, MySpaceStatusCode.ERROR,
              MySpaceStatusCode.NOLOG, this.getClassName() );
        }
     }

     return authorised;
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
   {  String userRepn = null;

      if (userName != null)
      {  userRepn = userId + "@" + communityId + " (" + userName + ")";
      }
      else
      {  userRepn = userId + "@" + communityId;
      }

      return userRepn;
   }

/**
 * Obtain the name of the current Java class.
 */

   protected String getClassName()
   { Class currentClass = this.getClass();
     String name =  currentClass.getName();
     int dotPos = name.lastIndexOf(".");
     if (dotPos > -1)
     {  name = name.substring(dotPos+1, name.length() );
     }

     return name;
   }
}
