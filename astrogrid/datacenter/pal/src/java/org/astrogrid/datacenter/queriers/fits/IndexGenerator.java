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

import java.io.*;
import org.astrogrid.datacenter.fits.*;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.datacenter.queriers.QuerierPluginException;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


public class IndexGenerator
{
   /** this is meant to run as a command-line program, so output should go to the
    * console, but it uses routines that use commons-logging anyway...
    */
   public static Log log = LogFactory.getLog(IndexGenerator.class);
   
   /** Should we test for extensions.  There is no standard way of checking if
    * a FITS file has extensions (the EXTEND keyword merely says that it *may*
    * have extensions), and for large FITS files where you *know* there are no
    * extensions, this can take some time.  So set this to false */
   public boolean checkForExtensions = true;

   /** Configures which axis is the RA (if any) */
   public int raAxis = -1;

   /** Configures which axis is the DEC (if any) */
   public int decAxis = -1;

   /** Configures the format of dates in the fits file headers so they can be parsed*/
   public DateFormat fitsDateFormat = DateFormat.getDateTimeInstance();

   /** Defines the format of the dates in the index - although they will also be output in seconds */
   public SimpleDateFormat indexDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

   /**
    * Generates a single index FitsFile 'snippet' for the FITS file at the
    * given url
    */
   public String makeIndexSnippet(URL fitsUrl) throws IOException
   {
      assert fitsUrl != null;
      
      log.info("Examining file "+fitsUrl+"...");
      return makeIndexSnippet(new FitsStreamReader(fitsUrl), fitsUrl.toString());
   }
   
   /**
    * Generates an index 'snippet' for the FITS file at the
    * given url
    * @todo tidy up so that, for example, multiline comments become one tag
    * @TODO max pixels are WRONG
    */
   public String makeIndexSnippet(FitsReader reader, String filename) throws IOException
   {
      StringBuffer snippet = new StringBuffer();
      
      FitsHeader header = new FitsHeader();
      reader.readHeaderKeywords(header, null);

      snippet.append(makeIndexSnippet(header, filename));

      FitsHdu primaryHdu = new FitsHdu(header);

      //now look for extensions
      if ((checkForExtensions) && primaryHdu.getHeader().getValue("EXTEND").equals("T"))
      {
         //read data into HDU
         reader.readData(primaryHdu);

         try {
            //*might* be extensions...
            for (int i=0;i<4;i++)
            {
               log.trace(""); //seperate headers
               log.trace(""); //seperate header-reading trace code
            
               header = new FitsHeader();
               reader.readHeaderKeywords(header, null);
         
               snippet.append(makeIndexSnippet(header, filename));
         
               FitsHdu extHdu = new FitsHdu(header);
         
               reader.readData(extHdu);
            }
         }
         catch (FitsFormatException ffe) {
            log.warn("Fits format exception: "+ffe+" assuming no extension headers");
         }
      }
      return snippet.toString();
   }
   
   /**
    * Generates an index snippet for the given header
    */
   public String makeIndexSnippet(FitsHeader header, String fileLocation) throws IOException
   {
      if(header == null) {
        return "";
      }
                                
      //run trhough all keywords adding them
      StringBuffer keywordSnippet = new StringBuffer();
      String key = null;
      String val = null;
      
      Enumeration enum = header.getKeywords();
      while (enum.hasMoreElements())
      {
         FitsKeyword keyword = (FitsKeyword) enum.nextElement();
         val = keyword.getValue();
         
         //could probably do with tidying this up a bit, for example so that multiline comments become one tag
         if (val == null) {
            keywordSnippet.append("      <"+keyword.getKey()+"/>\n");
         } else {
            try {
               Date dateVal = fitsDateFormat.parse(val);
               keywordSnippet.append("      <"+keyword.getKey()+" units='s'>"+dateVal.getTime()+"</"+keyword.getKey()+">\n");
               val = indexDateFormat.format(dateVal);
            } catch (ParseException e) {
               //ignore
            }
            keywordSnippet.append("      <"+keyword.getKey()+">"+val+"</"+keyword.getKey()+">\n");
         }
      }
      
      
      //assemble snippet
      String snippet = "<FitsFile>\n"+
               "   <Filename>"+fileLocation+"</Filename>\n"+
               makeCoverageSnippet(header)+
               "   <Keywords>\n"+
               keywordSnippet.toString()+
               "   </Keywords>\n"+
               "</FitsFile>\n";
      
      validate(snippet); //debug - test snippet
      
      return snippet;
   }

   /**
    * Makes a Coverage snippet from the keywords in the given header, depending
    * on the raAxis, decAxis settings */
   private String makeCoverageSnippet(FitsHeader header) {
      if ((header.getNumAxis() >= 2) && (raAxis != -1) && (decAxis != -1)) {
         //return "";
      
         //work out coverage.  This is not a straightforward rectangle, as the
         //image may be rotated and possibly even skewed.  So just give it as a
         //polygon with 4 points.
         FitsWCS wcsCalculator = new FitsWCS(header);
         double maxPixelRA  = header.get("NAXIS"+raAxis).toInt();
         double maxPixelDec = header.get("NAXIS"+decAxis).toInt();
   
         double[] point1 = wcsCalculator.toWCS( new double[] {0,0});
         double[] point2 = wcsCalculator.toWCS( new double[] {0,maxPixelDec});
         double[] point3 = wcsCalculator.toWCS( new double[] {maxPixelRA,maxPixelDec});
         double[] point4 = wcsCalculator.toWCS( new double[] {maxPixelRA,0});
         
         //NB origin pixels might not be the min RA & DEC - the picture might be 'upside down'
         return
            "   <Coverage shape='Polygon'>\n"+
            "      <Point><RA>"+point1[0]+"</RA><Dec>"+point1[1]+"</Dec></Point>\n"+
            "      <Point><RA>"+point2[0]+"</RA><Dec>"+point2[1]+"</Dec></Point>\n"+
            "      <Point><RA>"+point3[0]+"</RA><Dec>"+point3[1]+"</Dec></Point>\n"+
            "      <Point><RA>"+point4[0]+"</RA><Dec>"+point4[1]+"</Dec></Point>\n"+
            "   </Coverage>\n";
      }
      return "";
   }
   /** Checks that the given snippet is valid XML */
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
    * Generates an index XML file for the FITS files at the given urls
    */
   public String generateIndex(Object[] urls) throws IOException
   {
      StringBuffer index = new StringBuffer("<FitsDataCenterIndex>\n");
      
      for (int i=0;i<urls.length;i++)
      {
         assert urls[i] != null;  //or could report it and continue?
         index.append(makeIndexSnippet((URL)urls[i]));
      }

      index.append("</FitsDataCenterIndex>\n");
      
      return index.toString();
      
   }
   
   /**
    * Generates an index XML file for the FITS files at the URLs listed in the
    * given file, writing them out to the target stream
    */
   public void generateIndex(InputStream urlsIn, OutputStream out) throws IOException
   {
         BufferedReader in
            = new BufferedReader(new InputStreamReader(urlsIn));
         String line = null;
         while( (line = in.readLine()) != null) {
            try {
               out.write(makeIndexSnippet(new URL(line)).getBytes());
               out.flush();
            } catch (IOException ioe) {
               log.error("Could not read file at "+line, ioe);
            }
         }
         in.close();
   }

   
   /**
     * Test harness
     */
    public static void main(String args[]) throws IOException, MalformedURLException
    {
//      System.out.print(generateIndex(new URL[] {
//                                new URL("file://fannich/mch/fits/ngc6946/r169093.fit")
//                             }));
      //this is a bit of a botch so that if there's no log4j.properties around, it
      //makes one
      IndexGenerator generator = new IndexGenerator();
      
      Locale.setDefault(Locale.UK);
      Document indexDoc = null;
      if(args == null || args.length < 2) {
         System.out.println("java IndexGenerator -f <filename of urls (one url per line)> or");
         System.out.println("java IndexGenerator -u <URLs seperated by spaces>");
         return;
      }
      String indexFile = null;
      if("-f".equals(args[0])) {
         OutputStream out = new ByteArrayOutputStream();
         generator.generateIndex(new FileInputStream(args[1]), out);
         indexFile = out.toString();
      } else if("-u".equals(args[0])) {
         Object []fitsURLS = new Object[(args.length-1)];
         for(int i = 1;i < args.length;i++) {
            fitsURLS[(i-1)] = new URL(args[i]);
         }
         indexFile = generator.generateIndex(fitsURLS);
         log.trace(indexFile);
      }
      
      if(indexFile != null) {
         try {
            indexDoc = DomHelper.newDocument(indexFile);
         }catch(ParserConfigurationException pce) {
            throw new RuntimeException("Server configuration error",pce);
         }catch(SAXException se) {
            throw new QuerierPluginException("FitsQuerierPlugin index not valid xml",se);
         }
      }
      
      long fileTime = System.currentTimeMillis();
      if(indexDoc != null) {
         
         FileWriter fw = new FileWriter(String.valueOf(fileTime) + ".xml");
         PrintWriter pw = new PrintWriter(fw);
         DomHelper.DocumentToWriter(indexDoc,pw);
         pw.flush();
         fw.close();
         pw.close();
         System.out.println("Successfull creation of fits file index, it was created as = " + String.valueOf(fileTime) + ".xml");
         System.out.println("Now the file must be uploaded to the eXist xml database to be used for querying the file");
         System.out.println("You may do this manually or this program can do it automatically by calling \"java -s IndexGenerator <filename>\" by using the file name just created");
         
      }

      if("-s".equals(args[0])) {
         try {
            indexDoc = DomHelper.newDocument(new File(args[1]));
         }catch(ParserConfigurationException pce) {
            throw new RuntimeException("Server configuration error",pce);
         }catch(SAXException se) {
            throw new QuerierPluginException("FitsQuerierPlugin index not valid xml",se);
         }
         if(indexDoc != null) {
   
            InputStreamReader consoleReader = new InputStreamReader(System.in);
            BufferedReader consoleInput = new BufferedReader(consoleReader);
   
            
            
            System.out.println("This program will now ask three questions to do this automatically and it will update or insert the new xml file for queyring");
            System.out.println("What is the name of the file you wish for it to live in the db?");
            System.out.println("Please keep it unique, but remember it so you can update it later.  Now the Name?");
            String line = null;
            String fileName = null;
            URL eXistLocation = null;
            while( (line = consoleInput.readLine()) != null) ;
            line = line.replaceAll("[^\\w*]","_");
            if(!line.endsWith(".xml")) line += ".xml";
            fileName = line;
            System.out.println("What is the url to the exist database? ex: http://localhost:8080/exist  is a typical url");
            System.out.println("We only need it up to the \"exist\" part");
            while( (line = consoleInput.readLine()) != null) ;
            
            if(!line.endsWith("/")) {
               line += "servlet/db/dcfitsfiles/" + fileName;
            }
            else {
               line += "/servlet/db/dcfitsfiles/" + fileName;
            }
            eXistLocation = new URL(line);
            System.out.println("The Full URL to Exist is\n: " + line + "\n");
            System.out.println("Remember this url you may need it on the final configuration of your datacenter");
            updateFile(eXistLocation,indexFile);
         }
      }
    }
    
    private static void updateFile(URL urlLocation, String contents) throws IOException {
       HttpURLConnection huc = (HttpURLConnection)
                                urlLocation.openConnection();
       huc.setRequestProperty("Content-Type", "text/xml");
       huc.setDoOutput(true);
       huc.setRequestMethod("PUT");
       huc.connect();
       try {
          DataOutputStream dos = new DataOutputStream(huc.getOutputStream());
          DomHelper.DocumentToStream(DomHelper.newDocument(contents),dos);
          dos.flush();
          dos.close();
          huc.disconnect();
       } catch (javax.xml.parsers.ParserConfigurationException e) {
          throw new IOException(e.toString());
       } catch (org.xml.sax.SAXException e) {
          throw new IOException(e.toString());
       }
    }
    
}

/*
$Log: IndexGenerator.java,v $
Revision 1.1  2004/09/28 15:02:13  mch
Merged PAL and server packages

Revision 1.21  2004/09/08 15:45:33  mch
Fix for unconfigured ra/dec axis

Revision 1.20  2004/09/07 14:52:10  mch
Fixes etc for SEC

Revision 1.19  2004/09/07 13:50:43  mch
Added fix so generator continues even if there's a problem reading a file

Revision 1.18  2004/09/07 09:48:34  mch
Logging updates

Revision 1.17  2004/09/07 01:39:27  mch
Moved email keys from TargetIndicator to Slinger

Revision 1.16  2004/09/07 00:54:20  mch
Tidied up Querier/Plugin/Results, and removed deprecated SPI-visitor-SQL-translator

Revision 1.15  2004/09/06 21:38:34  mch
Changed to take InputStream

Revision 1.14  2004/09/06 21:20:01  mch
Factored out generateIndex for a filename for tests

Revision 1.13  2004/08/05 15:14:22  KevinBenson
small bug fix in the FitsREsults.  And now uses dates was teh result of the mber of kevin-dev-03-08-04

Revision 1.11.2.1  2004/08/05 15:10:35  KevinBenson
Changes to look for dates and make dates into UTC dates.

Revision 1.11  2004/07/29 11:17:44  KevinBenson
small change to check that the header is null return an empty string

Revision 1.10  2004/07/29 11:15:22  KevinBenson
*** empty log message ***

Revision 1.9  2004/07/29 11:05:45  KevinBenson
small change to make sure the header is not null

Revision 1.8  2004/07/26 13:53:44  KevinBenson
Changes to Fits to do an xquery on an xml file dealing with fits data.
Small xsl style sheet to make the xql which will get the filename element

Revision 1.7.4.2  2004/07/26 08:53:40  KevinBenson
Still need to make a few more corrections, but wanted to check this in now.
It is the fits querier that now uses exist for doing adql->xquery

Revision 1.7.4.1  2004/07/16 09:02:25  KevinBenson
small change for IndexGenerator to use arguments for it's files and urls.  And
Fitskeyword to handle dates.

Revision 1.7  2004/07/12 23:26:14  mch
Botch fix for no extensions

Revision 1.6  2004/03/12 04:45:26  mch
It05 MCH Refactor

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


