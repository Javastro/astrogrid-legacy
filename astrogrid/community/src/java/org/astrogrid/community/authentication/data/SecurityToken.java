package org.astrogrid.community.authentication.data;

import java.util.Date;
import java.util.Calendar;
import java.sql.Timestamp;

import org.astrogrid.community.policy.data.AccountData;

/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/authentication/data/Attic/SecurityToken.java,v $</cvs:source>
 * <cvs:author>$Author: pah $</cvs:author>
 * <cvs:date>$Date: 2003/09/15 21:51:45 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityToken.java,v $
 *   Revision 1.5  2003/09/15 21:51:45  pah
 *   authentication database backend tested
 *
 *   Revision 1.4  2003/09/10 20:48:17  pah
 *   Authentication Service without database backend
 *
 *   Revision 1.3  2003/09/08 11:01:35  KevinBenson
 *   A check in of the Authentication authenticateToken roughdraft and some changes to the groudata and community data
 *   along with an AdministrationDelegate
 *
 *   Revision 1.2  2003/09/05 09:13:35  KevinBenson
 *   Just changed the boolean to the object Boolean
 *
 *   Revision 1.1  2003/09/05 09:00:45  KevinBenson
 *   Got rid of a package that made things look a little confusing.
 *   Added a quick dataobject called SecurityToken under authentication that will be usedf for castor.
 *   And added a small CommunityMessage object that will create a small message for sending to other systems
 *
 *   Revision 1.2  2003/09/04 23:58:10  dave
 *   Experimenting with using our own DataObjects rather than the Axis generated ones ... seems to work so far
 *
 *   Revision 1.1  2003/09/03 06:39:13  dave
 *   Rationalised things into one set of SOAP stubs and one set of data objects for both client and server.
 *
 * </cvs:log>
 *
 */

public class SecurityToken {

   private static SecurityToken invalidToken = new SecurityToken();
   /**
    * Our Account ident.
    *
    */
   private String token;

   /**
    * Our Account ident.
    *
    */
   private String target;

   /**
    * Our Account ident.
    *
    */
   private Date expirationDate;

   /**
    * Our Account ident.
    *
    */
   private Boolean used = new Boolean(false);

   /**
    * Our Account ident.
    *
    */
   private String account;

   /**
    * Our Account ident.
    *
    */
   private Date startDate;

   private static final int INCREMENT_HOUR = 8;

   /**
    * Default Public constructor. This will return an invalid security token as a proper security token can only be created with one of the other constructors.
    *
    */
   public SecurityToken() {
      this("BAD", "BAD");
      this.used = Boolean.TRUE;
      //mark the token as already used to indicate that it is invalid
   }

   /**
    * Public constructor.
    *
    */
   public SecurityToken(SecurityToken securityToken) {
      this(securityToken.account, securityToken.target);
   }

   /**
    * Public constructor.
    * This will most likely be the one used for the creation of new tokens.
    *
    */
   public SecurityToken(String account, String target) {
      this.account = account;
      this.target = target;
      this.token = account + String.valueOf(System.currentTimeMillis());
      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.HOUR_OF_DAY, INCREMENT_HOUR);
      this.expirationDate = new Timestamp(cal.getTimeInMillis());
      this.startDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
      this.used = Boolean.FALSE;
   }

   /**
    * Access to our Token.
    *
   */
   public String getToken() {
      return this.token;
   }

   /**
    * Access to our Token.
    *
   */
   public void setToken(String token) {
      this.token = token;
   }

   /**
    * Access to our Token.
    *
   */
   public String getTarget() {
      return this.target;
   }

   /**
    * Access to our Token.
    *
   */
   public void setTarget(String target) {
      this.target = target;
   }

   /**
    * Access to our Token.
    *
   */
   public String getAccount() {
      return this.account;
   }

   /**
    * Access to our Token.
    *
   */
   public void setAccount(String account) {
      this.account = account;
   }

   /**
    * Access to our Token.
    *
   */
   public Date getExpirationDate() {
      return this.expirationDate;
   }

   /**
    * Access to our Token.
    *
   */
   public void setExpirationDate(Date expirationDate) {
      this.expirationDate = expirationDate;
   }

   /**
    * Access to our Token.
    *
   */
   public Boolean getUsed() {
      return this.used;
   }

   /**
    * Access to our Token.
    *
   */
   public void setUsed(Boolean used) {
      this.used = used;
   }

   public String toString() {
      String val = "Token=" + this.token + ", Target=" + target;
      return val;
   }
   /**
    * @return
    */
   public Date getStartDate() {
      return startDate;
   }

   /**
    * @param date
    */
   public void setStartDate(Date date) {
      startDate = date;
   }

   /**
    * Creates an invalid security token. This is a token that has already been used by setting the appropriate field.
    * @return a static instance of an invalid security token...
    */
   public static SecurityToken createInvalidToken() {
      return invalidToken;
   }
   public boolean equals(java.lang.Object obj) {
      if (!(obj instanceof SecurityToken))
         return false;
      SecurityToken other = (SecurityToken)obj;
      if (obj == null)
      {
      
         return false;
      }
      if (this == obj)
      {
      
         return true;
      }
      boolean _equals;
      _equals =
         true
            && ((this.account == null && other.getAccount() == null)
               || (this.account != null
                  && this.account.equals(other.getAccount())))
            && ((this.expirationDate == null && other.getExpirationDate() == null)
               || (this.expirationDate != null
                  && this.expirationDate.equals(other.getExpirationDate())))
            && ((this.target == null && other.getTarget() == null)
               || (this.target != null && this.target.equals(other.getTarget())))
            && ((this.startDate == null && other.getStartDate() == null)
               || (this.startDate != null
                  && this.startDate.equals(other.getStartDate())))
            && ((this.token == null && other.getToken() == null)
               || (this.token != null && this.token.equals(other.getToken())))
            && ((this.used == null && other.getUsed() == null)
               || (this.used != null && this.used.equals(other.getUsed())));
      return _equals;
   }

}
