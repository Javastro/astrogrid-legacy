/*
 * $Id IndexGenerator.java $
 *
 */

package org.astrogrid.datacenter.queriers.fits;


/**
 * A standalone tool that generates an index based on the fits files it is
 * is pointed at.
 *
 * @author M Hill
 */

import org.astrogrid.datacenter.fits.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import org.astrogrid.log.Log;
import org.astrogrid.util.DomHelper;

public class IndexGenerator
{
//   private static Log log = LogFactory.getLog(IndexGenerator.class); //because it's a pain to configure
   
   /**
    * Generates an index XML file for the FITS files at the given urls
    */
   public static String generateIndex(URL[] urls) throws IOException
   {
      StringBuffer index = new StringBuffer("<FitsDataCenterIndex>\n");
      
      for (int i=0;i<urls.length;i++)
      {
         index.append(generateIndex(urls[i]));
      }

      index.append("</FitsDataCenterIndex>\n");
      
      return index.toString();
      
   }
   

   /**
    * Generates a single index FitsFile 'snippet' for the FITS file at the
    * given url
    */
   public static String generateIndex(URL fitsUrl) throws IOException
   {
      Log.trace("Examining file "+fitsUrl+"...");
      return generateIndex(new FitsStreamReader(fitsUrl), fitsUrl.toString());
   }
   
   /**
    * Generates an index 'snippet' for the FITS file at the
    * given url
    * @todo tidy up so that, for example, multiline comments become one tag
    * @TODO max pixels are WRONG
    */
   public static String generateIndex(FitsReader reader, String filename) throws IOException
   {
      StringBuffer snippet = new StringBuffer();
      
      FitsHeader header = new FitsHeader();
      reader.readHeaderKeywords(header, null);

      snippet.append(generateIndex(header, filename));

      FitsHdu primaryHdu = new FitsHdu(header);

      
      //now look for extensions
      if (primaryHdu.getHeader().getValue("EXTEND").equals("T"))
      {
         //might be extensions
         reader.readData(primaryHdu);

         
         for (int i=0;i<4;i++)
         {
            Log.trace(""); //seperate headers
            Log.trace(""); //seperate header-reading trace code
            
            header = new FitsHeader();
            reader.readHeaderKeywords(header, null);
         
            snippet.append(generateIndex(header, filename));
         
            FitsHdu extHdu = new FitsHdu(header);
         
            reader.readData(extHdu);
         }
      }
      
      return snippet.toString();
   }
   
   /**
    * Generates an index snippet for the given header
    */
   public static String generateIndex(FitsHeader header, String fileLocation) throws IOException
   {
      if (header.getNumAxis() < 2)
         return "";
      
      StringBuffer keywordSnippet = new StringBuffer();
      StringBuffer coverageSnippet = new StringBuffer();
      //work out coverage.  This is not a straightforward rectangle, as the
      //image may be rotated and possibly even skewed.  So just give it as a
      //polygon with 4 points.
      FitsWCS wcsCalculator = new FitsWCS(header);
      double maxPixelX = header.get("NAXIS1").toInt();
      double maxPixelY = header.get("NAXIS2").toInt();

      double[] point1 = wcsCalculator.toWCS( new double[] {0,0});
      double[] point2 = wcsCalculator.toWCS( new double[] {0,maxPixelY});
      double[] point3 = wcsCalculator.toWCS( new double[] {maxPixelX,maxPixelY});
      double[] point4 = wcsCalculator.toWCS( new double[] {maxPixelX,0});
      
      //NB origin pixels might not be the min RA & DEC - the picture might be 'upside down'
      coverageSnippet.append( "      <Point><RA>"+point1[0]+"</RA><Dec>"+point1[1]+"</Dec></Point>\n"+
                              "      <Point><RA>"+point2[0]+"</RA><Dec>"+point2[1]+"</Dec></Point>\n"+
                              "      <Point><RA>"+point3[0]+"</RA><Dec>"+point3[1]+"</Dec></Point>\n"+
                              "      <Point><RA>"+point4[0]+"</RA><Dec>"+point4[1]+"</Dec></Point>\n");
                                
      //run trhough all keywords adding them
      Enumeration enum = header.getKeywords();
   
      while (enum.hasMoreElements())
      {
         FitsKeyword keyword = (FitsKeyword) enum.nextElement();
         
         //could probably do with tidying this up a bit, for example so that multiline comments become one tag
         keywordSnippet.append("      <"+keyword.getKey()+">"+keyword.getValue()+"</"+keyword.getKey()+">\n");
      }
      
      
      //assemble snippet
      String snippet = "<FitsFile>\n"+
               "   <Filename>"+fileLocation+"</Filename>\n"+
               "   <Coverage shape='Polygon'>\n"+
               coverageSnippet.toString()+
               "   </Coverage>\n"+
               "   <Keywords>\n"+
               keywordSnippet.toString()+
               "   </Keywords>\n"+
               "</FitsFile>\n";
      
      validate(snippet); //debug - test snippet
      
      return snippet;
   }

   public static void validate(String snippet) throws IOException
   {
     try
     {
        DomHelper.newDocument(new ByteArrayInputStream(snippet.getBytes()));
     }
     catch (org.xml.sax.SAXException e) { throw new IOException(e.toString()); }
     catch (javax.xml.parsers.ParserConfigurationException e) { throw new IOException(e.toString()); }
      
   }

   /**
     * Test harness
     */
    public static void main(String args[]) throws IOException, MalformedURLException
    {
//      System.out.print(generateIndex(new URL[] {
//                                new URL("file://fannich/mch/fits/ngc6946/r169093.fit")
//                             }));
       Log.traceOn();
      Log.logToConsole();
      
      Log.trace(generateIndex(//new FitsStreamReader(
//                                        new File("//fannich/mch/fits/ngc6946/r169097.fit")
                              //                      ), "//fannich/mch/fits/ngc6946/r169097.fit"));
         new URL[] {
               new URL("http://www.roe.ac.uk/~mch/r169411.fit"),
               new URL("http://www.roe.ac.uk/~mch/r169097.fit"),
               new URL("http://www.roe.ac.uk/~mch/r169101.fit")
         }
      ));
    }
   
   
}

/*
$Log: IndexGenerator.java,v $
Revision 1.5  2004/03/08 00:31:28  mch
Split out webservice implementations for versioning

Revision 1.4  2004/01/13 00:33:14  nw
Merged in branch providing
* sql pass-through
* replace Certification by User
* Rename _query as Query

Revision 1.3.4.1  2004/01/08 09:43:41  nw
replaced adql front end with a generalized front end that accepts
a range of query languages (pass-thru sql at the moment)

Revision 1.3  2003/12/15 14:34:02  mch
Added trace code, extra fits file

Revision 1.2  2003/11/28 19:57:15  mch
Cone Search now works

Revision 1.1  2003/11/28 18:22:18  mch
IndexGenerator now working

*/
