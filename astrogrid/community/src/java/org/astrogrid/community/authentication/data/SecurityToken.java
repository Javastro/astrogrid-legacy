package org.astrogrid.community.authentication.data;

import java.util.Date;
import java.util.Calendar;
import java.sql.Timestamp;

/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/authentication/data/Attic/SecurityToken.java,v $</cvs:source>
 * <cvs:author>$Author: pah $</cvs:author>
 * <cvs:date>$Date: 2003/09/10 20:48:17 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityToken.java,v $
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
      /**
       * Our Account ident.
       *
       */
      private String ident;

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
      private Date todaysDate;
      
      private static final int INCREMENT_HOUR = 8;
      
      
   /**
    * Public constructor.
    *
    */
   public SecurityToken() {
      this(null);
   }

   /**
    * Public constructor.
    *
    */
   public SecurityToken(String ident) {
      this.ident = ident;
   }
   
   /**
    * Public constructor.
    * This will most likely be the one used for the creation of new tokens.
    *
    */
   public SecurityToken(String ident,String account,String target) {
      this.ident = ident;
      this.account = account;
      this.target = target;
      this.token = account + String.valueOf(System.currentTimeMillis());
      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.HOUR_OF_DAY,INCREMENT_HOUR);
      this.expirationDate = new Timestamp(cal.getTimeInMillis());
      this.todaysDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
   }   


   /**
    * Access to our Token's ident.
    *
    */
   public String getIdent() {
      return this.ident;
   }

   /**
    * Access to our Token's ident.
    *
    */
   public void setIdent(String ident) {
      this.ident = ident;
   }
      
   /**
    * Access to our Token.
    *
   */
   public String getToken(){
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
   public String getTarget(){
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
   public String getAccount(){
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
   public Date getExpirationDate(){
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
   public Date getTodaysDate(){
      return this.todaysDate;
   }

   /**
    * Access to our Token.
    *
   */
   public void setTodaysDate(Date todaysDate) {
      this.todaysDate = todaysDate;
   }
   
   /**
    * Access to our Token.
    *
   */
   public Boolean getUsed(){
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
}
