package org.astrogrid.community.authentication.data;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.sql.Timestamp;

import org.astrogrid.community.policy.data.AccountData;


/**
 * 
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration3
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

   /**
    * Creates a new security token from the security token soap bean. This is slightly inconvenient and perhaps it might be better to use the soap generated object in all cases.
    * This has not been done in this interation however because
    * 1. not sure what the implications of the axis generation process changing
    * 2. there are issues with date conversions
    * 3. it might be a good idea to have separate objects anyway for security purposes - this (server side) object could potentially had access to secret information that the client side object does not.
    * @param soapToken
    * @return
    */
   public static SecurityToken createFromSoapToken(org.astrogrid.community.service.authentication.data.SecurityToken soapToken){
      
      SecurityToken newtoken = new SecurityToken();
      
      newtoken.account = soapToken.getAccount();
      newtoken.expirationDate = soapToken.getExpirationDate().getTime();
      newtoken.startDate = soapToken.getStartDate().getTime();
      newtoken.target = soapToken.getTarget();
      newtoken.token = soapToken.getToken();
      newtoken.used = soapToken.getUsed();
      
      
      return newtoken;
      
   }
   /**
    * Creates a new security token soap bean from this class.
    * @return
    */
   public org.astrogrid.community.service.authentication.data.SecurityToken createSoapToken(){
      org.astrogrid.community.service.authentication.data.SecurityToken soaptoken = new org.astrogrid.community.service.authentication.data.SecurityToken();
      Calendar cal = Calendar.getInstance();
      soaptoken.setAccount(account);
      cal.setTime(expirationDate);
      soaptoken.setExpirationDate(cal);
      cal.setTime(startDate);
      soaptoken.setStartDate(cal);
      soaptoken.setTarget(target);
      soaptoken.setToken(token);
      return soaptoken;
   }
}
