/*
 * $Id: TargetMaker.java,v 1.2 2005/03/15 12:07:28 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger.targets;


import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.astrogrid.slinger.myspace.MSRL;
import org.astrogrid.slinger.vospace.HomespaceName;
import org.astrogrid.slinger.vospace.IVORN;
import org.astrogrid.slinger.vospace.IVOSRN;

/**
 * Factory to create targets
 *
 */

public abstract class TargetMaker  {

   /**
    * Tests the string & creates the right kind of TargetIndicator
    */
   public static TargetIdentifier makeTarget(String id) throws URISyntaxException, IOException {
      if ((id == null) || (id.trim().length() == 0) || (id.toLowerCase().equals("null")) || (id.toLowerCase().equals(NullTarget.NULL_TARGET_URI.toString().toLowerCase()))) {
         return new NullTarget();
      }
      else if (id.startsWith("mailto:")) {
         return new EmailTarget(id);
      }
      else if (MSRL.isMsrl(id)) {
         return new MSRL(id);
      }
      else if (isUrl(id)) {
         return new UrlTarget(new URL(id));
      }
      else if (IVOSRN.isIvorn(id)) {
         return new IVOSRN(id);
      }
      else if (HomespaceName.isHomespaceName(id)) {
         return new HomespaceName(id);
      }
      else {
         //could do with adding some sort of configurable plugin mechanism similar to the URL plugins
         throw new IllegalArgumentException("'"+id+" should be a URL, or start 'mailto:' or '"+IVORN.SCHEME+"' or '"+MSRL.SCHEME+"' or '"+HomespaceName.SCHEME+"'");
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
   public static TargetIdentifier makeTarget(URI target) throws IOException, URISyntaxException {
      return makeTarget(target.toString());
   }
   
   /**
    * Makes a target that will write to the given writer
    */
   public static TargetIdentifier makeTarget(Writer target)  {
      return new WriterTarget(target);
   }

   /**
    * Makes a target that will write to the given writer.  Set closeIt to false
    * to inform the targetindicator user not to close the writer when it is
    * finished
    */
   public static TargetIdentifier makeTarget(Writer target, boolean closeIt)  {
      return new WriterTarget(target, closeIt);
   }
   
   /**
    * Makes a target that will write to the given URL
    */
   public static TargetIdentifier makeTarget(URL target) throws IOException  {
      return new UrlTarget(target);
   }
   
   /**
    * Makes a target that will write to the given msypace reference. Just returns
    * the MSRL
    */
   public static TargetIdentifier makeTarget(MSRL target)   {
      return target;
   }
   
   /**
    * Makes a target that will write to the given AGSL.  Returns the UrlTarget
    * or MySpaceTarget as appropriate
    * @ deprecated - use URLs or Msrls directly
    *
   public static TargetIdentifier makeIndicator(Agsl target) throws MalformedURLException, URISyntaxException  {
      //remove 'astrogrid:store:' and makeIndicator from remainder
      String uri = target.toUri().toString().substring(Agsl.SCHEME.length()+1);
      return makeIndicator(uri);
   }
   
   /**
    * Makes a target that will write to the given IVORN - ie the given Ivorn
    */
   public static TargetIdentifier makeTarget(IVOSRN target)  {
      return target;
   }
   

   /**
    * Makes a target that will write to the given stream
    */
   public static TargetIdentifier makeTarget(OutputStream target)  {
      return new StreamTarget(target);
   }

}
/*
 $Log: TargetMaker.java,v $
 Revision 1.2  2005/03/15 12:07:28  mch
 Added FileManager support

 Revision 1.1  2005/02/14 20:47:38  mch
 Split into API and webapp

 Revision 1.4  2005/02/14 17:53:38  mch
 Split between webnode (webapp) and library, prepare to split between API and special implementations

 Revision 1.3  2005/01/26 17:31:57  mch
 Split slinger out to scapi, swib, etc.

 Revision 1.1.2.7  2005/01/26 14:35:28  mch
 Separating slinger and scapi


 Revision 1.1.2.4  2004/12/03 11:50:19  mch
 renamed Msrl etc to separate from storeclient ones.  Prepared for SRB

 Revision 1.1.2.3  2004/11/23 12:33:42  mch
 renamed to makeTarget

 Revision 1.1.2.1  2004/11/22 00:46:28  mch
 New Slinger Package


 Revision 1.1  2004/03/14 04:13:04  mch
 Wrapped output target in TargetIndicator

 */




