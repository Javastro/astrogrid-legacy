/*
 * $Id: SourceMaker.java,v 1.1 2005/02/14 20:47:38 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger.sources;


import java.io.InputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.astrogrid.slinger.myspace.MSRL;
import org.astrogrid.slinger.vospace.IVORN;
import org.astrogrid.slinger.vospace.IVOSRN;

/**
 * Factory to create targets
 *
 */

public abstract class SourceMaker  {

  /**
    * Tests the string & creates the right kind of TargetIndicator
    */
   public static SourceIdentifier makeSource(String id) throws MalformedURLException, URISyntaxException {
      if (MSRL.isMsrl(id)) {
         return new MSRL(id);
      }
      else if (isUrl(id)) {
         return new UrlSource(new URL(id));
      }
      else if (IVOSRN.isIvorn(id)) {
         return new IVOSRN(id);
      }
//replaced by Uri and MySpace targets      else if (Agsl.isAgsl(id)) {
//         return new AgslTarget(new Agsl(id));
//      }
      else {
         throw new IllegalArgumentException("'"+id+" should start 'mailto:' or '"+IVORN.SCHEME+"' or '"+MSRL.SCHEME+"'");
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
   public static SourceIdentifier makeSource(URI target) throws MalformedURLException, URISyntaxException {
      return makeSource(target.toString());
   }
   
   /**
    * Makes a source that will read from the given reader
    */
   public static SourceIdentifier makeSource(Reader source)  {
      return new ReaderSource(source);
   }

   /**
    * Makes a source that will read from the given URL
    */
   public static SourceIdentifier makeSource(URL target)  throws URISyntaxException {
      return new UrlSource(target);
   }
   
   /**
    * Makes a source that will read from the given msypace reference
    *
   public static SourceIdentifier makeSource(MSRL target)  throws URISyntaxException {
      return target;
   }
   
   /**
    * Makes a source that will read from the given IVORN -ie the given IVORN
    */
   public static SourceIdentifier makeSource(IVOSRN target)  {
      return target;
   }
   
   /**
    * Makes a source that will read from the given stream
    */
   public static SourceIdentifier makeSource(InputStream in)  {
      return new StreamSource(in);
   }
   

}
/*
 $Log: SourceMaker.java,v $
 Revision 1.1  2005/02/14 20:47:38  mch
 Split into API and webapp

Revision 1.3  2005/01/26 17:31:57  mch
Split slinger out to scapi, swib, etc.

Revision 1.1.2.4  2005/01/26 14:35:25  mch

 Separating slinger and scapi





 Revision 1.1.2.2  2004/12/03 11:50:19  mch

 renamed Msrl etc to separate from storeclient ones.  Prepared for SRB



 Revision 1.1.2.1  2004/11/22 00:46:28  mch

 New Slinger Package





 */







