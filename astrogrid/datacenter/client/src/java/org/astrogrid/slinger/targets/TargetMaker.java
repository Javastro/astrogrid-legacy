/*
 * $Id: TargetMaker.java,v 1.1 2004/11/09 17:42:22 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger.targets;





import java.io.OutputStream;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.Msrl;

/**
 * Factory to create targets
 *
 */

public abstract class TargetMaker  {

   /**
    * Tests the string & creates the right kind of TargetIndicator
    */
   public static TargetIndicator makeIndicator(String id) throws MalformedURLException, URISyntaxException {
      if ((id == null) || (id.trim().length() == 0) || (id.toLowerCase().equals("null")) || (id.toLowerCase().equals(NullTarget.NULL_TARGET_URI.toString().toLowerCase()))) {
         return new NullTarget();
      }
      else if (id.startsWith("mailto:")) {
         return new EmailTarget(id);
      }
      else if (Msrl.isMsrl(id)) {
         return new MySpaceTarget(new Msrl(id));
      }
      else if (isUrl(id)) {
         return new UrlTarget(new URL(id));
      }
      else if (Ivorn.isIvorn(id)) {
         return new IvornTarget(new Ivorn(id));
      }
//replaced by Uri and MySpace targets      else if (Agsl.isAgsl(id)) {
//         return new AgslTarget(new Agsl(id));
//      }
      else {
         throw new IllegalArgumentException("'"+id+" should start 'mailto:' or '"+Ivorn.SCHEME+"' or '"+Agsl.SCHEME+"'");
      }
   }

   /** Returns true if the given string is a valid URL, false if not */
   public static boolean isUrl(String urlCandidate) {
      try {
         new URL(urlCandidate);
         return true;
      }
      catch (MalformedURLException mue) {
         return false;
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
    * Makes a target that will write to the given writer.  Set closeIt to false
    * to inform the targetindicator user not to close the writer when it is
    * finished
    */
   public static TargetIndicator makeIndicator(Writer target, boolean closeIt)  {
      return new WriterTarget(target, closeIt);
   }
   
   /**
    * Makes a target that will write to the given URL
    */
   public static TargetIndicator makeIndicator(URL target)  throws URISyntaxException {
      return new UrlTarget(target);
   }
   
   /**
    * Makes a target that will write to the given msypace reference
    */
   public static TargetIndicator makeIndicator(Msrl target)  throws URISyntaxException {
      return new MySpaceTarget(target);
   }
   
   /**
    * Makes a target that will write to the given AGSL.  Returns the UrlTarget
    * or MySpaceTarget as appropriate
    * @ deprecated - use URLs or Msrls directly
    */
   public static TargetIndicator makeIndicator(Agsl target) throws MalformedURLException, URISyntaxException  {
      //remove 'astrogrid:store:' and makeIndicator from remainder
      String uri = target.toUri().toString().substring(Agsl.SCHEME.length()+1);
      return makeIndicator(uri);
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

}
/*
 $Log: TargetMaker.java,v $
 Revision 1.1  2004/11/09 17:42:22  mch
 Fixes to tests after fixes for demos, incl adding closable to targetIndicators

 Revision 1.2  2004/11/03 00:17:56  mch
 PAL_MCH Candidate 2 merge

 Revision 1.1.2.2  2004/11/02 21:51:54  mch
 Replaced AgslTarget with UrlTarget and MySpaceTarget

 Revision 1.1.2.1  2004/11/02 19:41:26  mch
 Split TargetIndicator to indicator and maker

 Revision 1.3.6.1  2004/11/01 20:47:23  mch
 Added a little bit of doc and introduced MsrlTarget/UrlTargets

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



