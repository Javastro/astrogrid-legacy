/*
 * $Id IndexGenerator.java $
 *
 */

package org.astrogrid.fitsserver.setup;
import org.astrogrid.fitsserver.fits.*;


/**
 * A standalone tool that generates an index based on the fits files it is
 * is pointed at.
 *
 * @author M Hill
 */

import java.io.*;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.Locale;
import javax.xml.parsers.ParserConfigurationException;
import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;
import nom.tam.fits.Header;
import nom.tam.fits.HeaderCard;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.dataservice.queriers.QuerierPluginException;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


import org.astrogrid.xmldb.client.QueryService;
import org.astrogrid.xmldb.client.XMLDBFactory;

import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;



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

   /** The root element */
   public static final String ROOT_START = "<FitsIndex>";
   public static final String ROOT_END = "</FitsIndex>";
   
   /**
    * Generates a single index FitsFile 'snippet' for the FITS file at the
    * given url
    */
   public String makeIndexSnippet(URL fitsUrl) throws IOException, FitsException
   {
      assert fitsUrl != null;
      
      log.info("Examining file "+fitsUrl+"...");
      return makeIndexSnippet(new Fits(fitsUrl), fitsUrl.toString());
   }
   
   /**
    * Generates an index 'snippet' for the FITS file at the
    * given url
    * @todo tidy up so that, for example, multiline comments become one tag
    * @TODO max pixels are WRONG
    */
   public String makeIndexSnippet(Fits reader, String filename) throws IOException, FitsException
   {
      StringBuffer snippet = new StringBuffer();
      
      for (int i = 0; i < reader.getNumberOfHDUs(); i++)
      {
         log.trace(""); //seperate headers
         log.trace(""); //seperate header-reading trace code
         Header header = reader.getHDU(i).getHeader();
   
         snippet.append(makeIndexSnippet(header, filename));
      }

      return snippet.toString();
   }
   
   /**
    * Generates an index snippet for the given header.
    * Could probably do with tidying this up a bit, for example so that multiline comments become one tag
    * End of line comments are included as 'Comment' attributes within the keyword tag, so that they are searchable
    */
   public String makeIndexSnippet(Header header, String fileLocation) throws IOException, FitsException
   {
      if(header == null) {
        return "";
      }
                                
      //run trhough all keywords adding them
      StringBuffer keywordSnippet = new StringBuffer();
      
//proper way, but the object returned by header.iterator().next() is not documented      Iterator keys = header.iterator();
      for (int i = 0; i < header.getNumberOfCards(); i++)
      {
         String key = header.getKey(i).trim();
         String value = header.getStringValue(key);

         //if the key is 'comment' leave it at that, if the comment comes from and-of-line comment, include that too
         String eolComment = null;
         if (!key.toUpperCase().equals("COMMENT")) {
            eolComment =header.findCard(key).getComment().trim();
            if (eolComment.length()==0) {
               eolComment = null;
            }
         }
         String commentAtt = "";
         if (eolComment != null) {
            commentAtt = " comment='"+eolComment+"'";
         }
         
         
         if (value == null) {
            //keywordSnippet.append("      <"+key+commentAtt+"/>\n");
             keywordSnippet.append("      <"+key+"/>\n");
         } else {
            try {
               //if it's a date, we store two key entries, one with a unit attribute set to 's' and the time in ms since/before 1970
               Date dateVal = fitsDateFormat.parse(value);
               keywordSnippet.append("      <"+key+" unit='s'"+commentAtt+">"+dateVal.getTime()+"</"+key+">\n");
               //keywordSnippet.append("      <"+key+ " unit='s'>"+dateVal.getTime()+"</"+key+">\n");
               value = indexDateFormat.format(dateVal);
            } catch (ParseException e) {
               //ignore
            }
            keywordSnippet.append("      <"+key+commentAtt+">"+value+"</"+key+">\n");
            //keywordSnippet.append("      <"+key+">"+value+"</"+key+">\n");
         }
      }
      
      
      //assemble snippet
      String snippet = "<FitsFile>\n"+
               "   <Filename>"+fileLocation+"</Filename>\n"+
               makeCoverageSnippet(header)+
               keywordSnippet.toString()+
               "</FitsFile>\n";
      
      validate(snippet); //debug - test snippet
      
      return snippet;
   }

   /**
    * Makes a Coverage snippet from the keywords in the given header, depending
    * on the raAxis, decAxis settings */
   private String makeCoverageSnippet(Header header) {
      if ((header.getIntValue("NAXIS") >= 2) && (raAxis != -1) && (decAxis != -1)) {
         //return "";
      
         //work out coverage.  This is not a straightforward rectangle, as the
         //image may be rotated and possibly even skewed.  So just give it as a
         //polygon with 4 points.
         FitsWCS wcsCalculator = new FitsWCS(header);
         double maxPixelRA  = header.getDoubleValue("NAXIS"+raAxis);
         double maxPixelDec = header.getDoubleValue("NAXIS"+decAxis);
   
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
   
   private static File fileIndexDir = null;
   private File getIndexDirectory() {
       String indexPath = String.valueOf(System.currentTimeMillis());       
       String indexGenHomePath = SimpleConfig.getProperty("indexgen.path", ("." + File.separator) );
       if(!indexGenHomePath.endsWith(File.separator))
           indexGenHomePath += File.separator;
       fileIndexDir = new File(indexGenHomePath + indexPath);
       if(!fileIndexDir.exists())
           fileIndexDir.mkdir();
       return fileIndexDir;
   }

   
   /**
    * Generates an index XML file for the FITS files at the URLs listed in the
    * given file, writing them out to the target stream
    */
   public File generateIndex(InputStream urlsIn) throws IOException, FitsException
   {
      BufferedReader in = new BufferedReader(new InputStreamReader(urlsIn));
      String line = null;
      while( (line = in.readLine()) != null) {
            generateIndex(new URL(line));
      }
      return fileIndexDir;
   }
   
   /**
    * Generates an index XML file for the FITS files at the URLs listed in the
    * given file, writing them out to the target stream
    */
   public File generateIndex(URL []urls) throws IOException, FitsException
   {
      for(int i = 0; i < urls.length;i++) {
            generateIndex(urls[i]);
      }//for
      return fileIndexDir;
   }
   

   /**
    * Generates an index XML file for the FITS files at the given URLs, writing it out to the target stream
    */
   public File generateIndex(URL url) throws IOException, FitsException
   {
       Calendar rightNow = Calendar.getInstance();
       int year = rightNow.get(Calendar.YEAR);
       int month = rightNow.get(Calendar.MONTH);
       int day = rightNow.get(Calendar.DATE);
       String dirPath = String.valueOf(year) + "_" + String.valueOf(month) + "_" + String.valueOf(day);
       
       if(fileIndexDir == null)
           getIndexDirectory();
       File xmlFile = null;
       FileWriter fw = null;
       PrintWriter pw = null;
       long seqNum = System.currentTimeMillis();
       String xmlSnippet = null;
          try {
             xmlSnippet = makeIndexSnippet(url);
             String fileName = dirPath + String.valueOf((seqNum)) + ".xml";
             xmlFile = new File(fileIndexDir,fileName);
             fw = new FileWriter(xmlFile);
             pw = new PrintWriter(fw);
             try {
                 DomHelper.DocumentToWriter(DomHelper.newDocument(xmlSnippet),pw);
             }catch(Exception e) {
                 e.printStackTrace();
             }
             pw.flush();
             fw.close();
             pw.close();
          } catch (IOException ioe) {
             //log as an error but try next URL
             log.error(ioe+", processing URL "+url.toString(), ioe);
          }
          return fileIndexDir;
   }
   
   private static void updateXMLDB(String directoryName) throws Exception {

       
       File fi = new File(directoryName);
       if(!fi.exists()) {           
           String indexGenHomePath = SimpleConfig.getProperty("indexgen.path", ("." + File.separator));
           if(!indexGenHomePath.endsWith(File.separator))
               indexGenHomePath += File.separator;
           fi = new File(indexGenHomePath + directoryName);
           if(!fi.exists()) {
               System.out.println("The directory or path does not seem to exist: directory=" + directoryName +
                       "or " + indexGenHomePath + directoryName);               
               System.exit(1);
           }//if
       }
       if(!fi.isDirectory()) {
           System.out.println("There is a file that exists here, but is reported to not be a directory.  Now exiting");
           System.exit(1);
       }
       String line = null;
       InputStreamReader consoleReader = new InputStreamReader(System.in);
       BufferedReader consoleInput = new BufferedReader(consoleReader);
       System.out.println("What is the Table Name (Collection Name) you wish to put in the xml files into. Example: TraceData, EITData");
       System.out.println("You may also do sub-tables (sub-collections) with a '/' such as: Soho/CDSData, MSSL/Soho/CDSData, RAL/Soho/Tape/CDSData");
       String correct = "N";
       String uploadCollection = null;
       while(!correct.equals("Y")) {
           while( (line = consoleInput.readLine()) != null) ;
           line = line.replaceAll("[^\\w*]","_");
           uploadCollection = line;
           System.out.println("The full location of placing xml files will be: (You cannot get rid of /db/dcfitsfiles");
           System.out.println("/db/dcfitsfiles/" + uploadCollection);
           System.out.println("Is this all correct Y|N");
           while( (correct = consoleInput.readLine()) != null) ;
           correct = correct.toUpperCase();
       }
       
       correct = "N";
       String adminUser = "admin";
       String adminPass = "";
       String xmldbURI = "xmldb:exist://";
       String xmldbConfig = "../exist.xml";
       String xmldbDriver = "org.exist.xmldb.DatabaseImpl";
       int changeNum = -1;
       while(!correct.equals("Y")) {
           System.out.println("Do you need to correct|change any of the below settings, Normally No if running pal with internal eXist db. [Y|N]");
           System.out.println("Commonly number 1 may be changed, and at times 4");
           System.out.println("1.) xmldb.uri = URI to the database. Default: " + xmldbURI + " (external example: xmldb:exist://localhost:9080/exist/xmlrpc)");
           System.out.println("2.) xmldb.admin.user = Administration user for adding xml files. Default: " + adminUser);
           System.out.println("3.) xmldb.admin.password = Administration password for adding xml files. Default: " + adminPass);
           System.out.println("4.) xmldb.configuration = Location of the xml db configuration file; If your running eXist internally advise to change read pal configuration page. Default: " + xmldbConfig);
           System.out.println("5.) xmldb.driver = The XML database driver. Default: " + xmldbDriver);
           while( (line = consoleInput.readLine()) != null) ;
           correct = correct.toUpperCase();
           if(correct.equals("N")) {
               System.out.println("Which number would you like to change [1-5], or 0 to exit changing");
               while(changeNum < 0) {
                   while( (line = consoleInput.readLine()) != null) ;
                   changeNum = Integer.parseInt(line);
                   if(changeNum > 5 || changeNum < 0) {
                       System.out.println("Invalid Number");
                       changeNum = -1;
                   }
                   if(changeNum == 1)
                       xmldbURI = askQuestion("What xmldb.uri do you wish to use?");
                   else if(changeNum == 2)
                       adminUser = askQuestion("What is the admin user?");
                   else if(changeNum == 3)
                       adminPass = askQuestion("What is the admin password?");
                   else if(changeNum == 4)
                       xmldbConfig = askQuestion("What is the location of the xmldb configuration file 'exist.xml'?");
                   else if(changeNum == 5)
                       xmldbDriver = askQuestion("What is the XMLDB driver?");
               }//while
           }//if
       }//while
       

       SimpleConfig.setProperty("xmldb.uri", xmldbURI);
       SimpleConfig.setProperty("xmldb.driver", xmldbDriver);
       SimpleConfig.setProperty("xmldb.admin.user", adminUser);
       SimpleConfig.setProperty("xmldb.admin.password", adminPass);
       //commented out until latest dependency arrives
       XMLDBFactory xdb = new XMLDBFactory();
       System.out.println("Now Registering the database");
       xdb.registerDB(xmldbConfig);
       File []xmlFiles = fi.listFiles();
       Document xmlDoc = null;
       Collection coll = null;
       try {
           coll = xdb.openAdminCollection(("dcfitsfiles/" + uploadCollection));
           for(int i = 0;i < xmlFiles.length;i++) {
               xmlDoc = DomHelper.newDocument(xmlFiles[i]);
               xdb.storeXMLResource(coll,xmlFiles[i].getName(),xmlDoc);
           }//for
       }finally {
           if(coll != null) {
               xdb.closeCollection(coll);
           }//if
       }
   }
   
   private static String askQuestion(String question) throws Exception {
       InputStreamReader consoleReader = new InputStreamReader(System.in);
       BufferedReader consoleInput = new BufferedReader(consoleReader);
       String line = null;
       while( (line = consoleInput.readLine()) != null) ;
       return line;
   }
   
   
   /**
     * Test harness
     */
    public static void main(String args[]) throws IOException, MalformedURLException, FitsException, Exception
    {
      //this is a bit of a botch so that if there's no log4j.properties around, it
      //makes one
      IndexGenerator generator = new IndexGenerator();
      
      Locale.setDefault(Locale.UK);
      Document indexDoc = null;
      if(args == null || args.length < 2) {
         System.out.println("java IndexGenerator -file <filename of urls (one url per line)> or");
         System.out.println("java IndexGenerator -url <single url>");
         System.out.println("java IndexGenerator -update <directory of xml files>");
         return;
      }

          if("-file".equals(args[0])) {
             generator.generateIndex(new FileInputStream(args[1]));
          }else if("-url".equals(args[0])) {
             String urlString = args[1];
             generator.generateIndex(new URL(urlString));
          }else if("-update".equals(args[0])) {
              updateXMLDB(args[1]);
          }
    }

}

/*
$Log: IndexGenerator.java,v $
Revision 1.2  2005/03/11 14:50:59  KevinBenson
added catch for parserconfigurationexception

Revision 1.1  2005/03/10 16:42:55  mch
Split fits, sql and xdb

Revision 1.3  2005/03/10 13:59:00  KevinBenson
corrections to fits and testing to fits

Revision 1.2  2005/03/01 15:58:34  mch
Changed to use starlinks tamfits library

Revision 1.1.1.1  2005/02/17 18:37:35  mch
Initial checkin

Revision 1.1.1.1  2005/02/16 17:11:24  mch
Initial checkin

Revision 1.3  2004/11/09 17:42:22  mch
Fixes to tests after fixes for demos, incl adding closable to targetIndicators

Revision 1.2  2004/10/18 13:11:30  mch
Lumpy Merge

Revision 1.1.6.1  2004/10/15 19:59:06  mch
Lots of changes during trip to CDS to improve int test pass rate

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



