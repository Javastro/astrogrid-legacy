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

import java.io.File;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;

import org.astrogrid.datacenter.queriers.QuerierPluginException;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.util.Enumeration;
import org.astrogrid.log.Log;
import org.astrogrid.util.DomHelper;
import java.util.Date;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import org.w3c.dom.Document;

public class IndexGenerator
{
//   private static Log log = LogFactory.getLog(IndexGenerator.class); //because it's a pain to configure
   
   /**
    * Generates an index XML file for the FITS files at the given urls
    */
   public static String generateIndex(Object[] urls) throws IOException
   {
      StringBuffer index = new StringBuffer("<FitsDataCenterIndex>\n");
      
      for (int i=0;i<urls.length;i++)
      {
         assert urls[i] != null;  //or could report it and continue?
         index.append(generateIndex((URL)urls[i]));
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
      assert fitsUrl != null;
      
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
         //read data into HDU
         reader.readData(primaryHdu);

         try {
            //*might* be extensions...
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
         catch (FitsFormatException ffe) {
            Log.logWarning("Fits format exception: "+ffe+" assuming no extension headers");
         }
      }
      return snippet.toString();
   }
   
   /**
    * Generates an index snippet for the given header
    */
   public static String generateIndex(FitsHeader header, String fileLocation) throws IOException
   {
      StringBuffer keywordSnippet = new StringBuffer();
      StringBuffer coverageSnippet = new StringBuffer();
      String key = null;
      String val = null;

      if (header.getNumAxis() != null && header.getNumAxis() >= 2) {      
         //return "";
      
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
      }//if
                                
      //run trhough all keywords adding them
      Enumeration enum = header.getKeywords();
      System.out.println("the header length = " + header.getList().length);
      while (enum.hasMoreElements())
      {
         FitsKeyword keyword = (FitsKeyword) enum.nextElement();
         val = keyword.getValue();
//         if(keyword.isDate()) {
//            val = String.valueOf(keyword.toDate());
//         }
         
         //could probably do with tidying this up a bit, for example so that multiline comments become one tag
         keywordSnippet.append("      <"+keyword.getKey()+">"+val+"</"+keyword.getKey()+">\n");
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
      Document indexDoc = null;
      if(args == null || args.length < 2) {
         System.out.println("java IndexGenerator -f <filename of urls (one url per line)> or");   
         System.out.println("java IndexGenerator -u <URLs seperated by spaces>");
         return;
      }
      String indexFile = null;
      if("-f".equals(args[0])) {         
         BufferedReader in
            = new BufferedReader(new FileReader(args[1]));
         ArrayList al = new ArrayList();
         String line = null;
         while( (line = in.readLine()) != null) {
            System.out.println("the url added to be processed = " + line);
            al.add(new URL(line));
         }
         in.close();
         indexFile = generateIndex(al.toArray()); 
         Log.trace(indexFile);
      }else if("-u".equals(args[0])) {
         Object []fitsURLS = new Object[(args.length-1)];
         for(int i = 1;i < args.length;i++) {
            fitsURLS[(i-1)] = new URL(args[i]);
         }
         indexFile = generateIndex(fitsURLS);
         Log.trace(indexFile);
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
