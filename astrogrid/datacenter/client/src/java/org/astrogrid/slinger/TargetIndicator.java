/*
 * $Id: TargetIndicator.java,v 1.3 2004/10/18 13:11:30 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger;



import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import org.astrogrid.community.Account;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.Ivorn;

/**
 * Used to indicate the target where the results are to be sent.  May be an AGSL, or an email address, or
 * some IVO based thingamy that is still to be resolved
 *
 */

public abstract class TargetIndicator  {

   /**
    * Tests the string & creates the right kind of TargetIndicator
    */
   public static TargetIndicator makeIndicator(String id) throws MalformedURLException, URISyntaxException {
      if ((id == null) || (id.length() == 0) || (id.toLowerCase().equals("null")) || (id.toLowerCase().equals(NullTarget.NULL_TARGET_URI.toString().toLowerCase()))) {
         return new NullTarget();
      }
      else if (id.startsWith("mailto:")) {
         return new EmailTarget(id);
      }
      else if (Agsl.isAgsl(id)) {
         return new AgslTarget(new Agsl(id));
      }
      else if (Ivorn.isIvorn(id)) {
         return new IvornTarget(new Ivorn(id));
      }
      else {
         throw new IllegalArgumentException("'"+id+" should start 'mailto:' or '"+Ivorn.SCHEME+"' or '"+Agsl.SCHEME+"'");
      }
   }

   /**
    * Creates the right kind of TargetIndicator for the given URI
    */
   public static TargetIndicator makeIndicator(URI target) throws MalformedURLException, URISyntaxException {
      return makeIndicator(target.toString());
   }
   
   /**
    * Makes a target that will write to the given writer
    */
   public static TargetIndicator makeIndicator(Writer target)  {
      return new WriterTarget(target);
   }
   
   /**
    * Makes a target that will write to the given AGSL
    */
   public static TargetIndicator makeIndicator(Agsl target)  {
      return new AgslTarget(target);
   }
   
   /**
    * Makes a target that will write to the given IVORN
    */
   public static TargetIndicator makeIndicator(Ivorn target)  {
      return new IvornTarget(target);
   }
   

   /**
    * Makes a target that will write to the given stream
    */
   public static TargetIndicator makeIndicator(OutputStream target)  {
      return new StreamTarget(target);
   }

   /** All targets must be able to resolve to a writer. The user is required
    * for permissions */
   public abstract Writer resolveWriter(Account user) throws IOException;
   
   /** All targets must be able to resolve to a stream.  The user is required
    * for permissioning. */
   public abstract OutputStream resolveStream(Account user) throws IOException;
   
   /** Returns true if the target indicator is forwardable.  That is to say, if
    * it is a reference to a target that can be passed on to a remote service.
    * WriterTargets for example are not forwardable, as they hold a reference to
    * a java object, which does not survive remote requests */
   public abstract boolean isForwardable();
   
}
/*
 $Log: TargetIndicator.java,v $
 Revision 1.3  2004/10/18 13:11:30  mch
 Lumpy Merge

 Revision 1.2.2.2  2004/10/16 14:29:07  mch
 Forwardable null targets

 Revision 1.2.2.1  2004/10/15 19:59:05  mch
 Lots of changes during trip to CDS to improve int test pass rate

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



