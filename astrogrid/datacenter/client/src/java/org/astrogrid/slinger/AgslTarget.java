/*
 * $Id: AgslTarget.java,v 1.2 2004/10/12 17:41:41 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import org.astrogrid.community.Account;
import org.astrogrid.store.Agsl;

/**
 * Used to indicate the target where the results are to be sent.  May be an AGSL, or an email address, or
 * some IVO based thingamy that is still to be resolved
 *
 */

public class AgslTarget extends UriTarget {

   public AgslTarget(Agsl targetAgsl)  {
      super(targetAgsl.toUri());
   }

   public Agsl getAgsl() {
      try {
         return new Agsl(uri.toString());
      }
      catch (MalformedURLException e) {
         //since this class only allows AGSLs to be set, this shouldn't happen...
         throw new RuntimeException("Application error: "+toString()+" is not an AGSL");
      }
   }
   
   public OutputStream resolveStream(Account user) throws IOException {
     return getAgsl().openOutputStream(user.toUser());
   }

   
   public String toString() {
      return "Agsl TargetIndicator "+uri;
   }
   
   /** Can be forwarded to remote services */
   public boolean isForwardable() { return true; }
   
}
/*
 $Log: AgslTarget.java,v $
 Revision 1.2  2004/10/12 17:41:41  mch
 added isForwardable

 Revision 1.1  2004/10/06 21:12:17  mch
 Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

 Revision 1.5  2004/10/05 14:55:00  mch
 Added factory methods

 Revision 1.4  2004/09/07 01:39:27  mch
 Moved email keys from TargetIndicator to Slinger

 Revision 1.3  2004/09/07 01:01:29  mch
 Moved testConnection to server slinger

 Revision 1.2  2004/09/07 00:54:20  mch
 Tidied up Querier/Plugin/Results, and removed deprecated SPI-visitor-SQL-translator

 Revision 1.1  2004/08/25 23:38:33  mch
 (Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

 Revision 1.3  2004/08/19 08:35:54  mch
 Fix to email constructor

 Revision 1.2  2004/08/18 22:27:57  mch
 Better error checking

 Revision 1.1  2004/08/17 20:19:36  mch
 Moved TargetIndicator to client

 Revision 1.5  2004/07/15 17:07:23  mch
 Added factory method to make from a string

 Revision 1.4  2004/03/15 19:16:12  mch
 Lots of fixes to status updates

 Revision 1.3  2004/03/15 17:08:11  mch
 Added compression adn format placeholders

 Revision 1.2  2004/03/14 16:55:48  mch
 Added XSLT ADQL->SQL support

 Revision 1.1  2004/03/14 04:13:04  mch
 Wrapped output target in TargetIndicator

 */



