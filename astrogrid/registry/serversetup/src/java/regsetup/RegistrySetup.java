package regsetup;

import java.io.*;
import java.net.*;
import org.w3c.dom.Document;
import org.xml.sax.*;
import javax.xml.parsers.*;
import org.astrogrid.registry.client.harvest.RegistryHarvestService;
import org.astrogrid.registry.client.query.RegistryService;
import org.astrogrid.registry.client.admin.RegistryAdminService;
import org.astrogrid.util.DomHelper;


public class RegistrySetup {
   
   public static void main(String []args) {
      try {
         
       String user = System.getProperty("user","");
       InputStreamReader isr = new InputStreamReader(System.in);
       BufferedReader d = new BufferedReader(isr);
       System.out.println("===========================================================");
       System.out.println("Hello " + user + "What would you like to do today?");
       System.out.println("1.) Install a new Registry Server (this will ask 2-4 as well)");       
       System.out.println("2.) Harvest a Astrogrid Registry");
       System.out.println("3.) Create the Registry entry for this Registry.");
       System.out.println("4.) Add/Update Resource entries into this Registry.");
       System.out.println("===========================================================");
       
       int menuNumber = chooseMenuOption();   
       
       if(menuNumber == 1) {
          menuOptionOne();
       }else if(menuNumber == 2) {
          chooseMenuOptionTwo(null,false);
       }else if(menuNumber == 3) {
          chooseMenuOptionThree(null,null,false);
       }else if(menuNumber == 4) {
          chooseMenuOptionFour(null, false);
       }
      }catch(Exception e) {
         System.out.println("error message toString = " + e.toString());
         e.printStackTrace();
      }       
   }
   
   private static int chooseMenuOption() throws Exception {
      InputStreamReader isr = new InputStreamReader(System.in);
      BufferedReader d = new BufferedReader(isr);
      String menuOption = d.readLine();
      System.out.println("Please choose a menu option number?");
      int menuChosen = -1;
      try {
        menuChosen = Integer.parseInt(menuOption);
      }catch(Exception e1) {
         System.out.println("Please choose a menu option numbers.");
         chooseMenuOption();
      }
      if(menuChosen == 1 || menuChosen == 2 || menuChosen == 3 || menuChosen == 4)
         return menuChosen;
      return chooseMenuOption();
   }
   
   private static void menuOptionOne() throws Exception {

         System.out.println("Deals with installing a new registry. It may also be used for updating configuration of an old registry");
         System.out.println("This option must be ran on the same machine as the registry server.");
               
         InputStreamReader isr = new InputStreamReader(System.in);
         BufferedReader d = new BufferedReader(isr);
         System.out.println("Please Copy the astrogrid-registry-webapp.war and exist.war to your tomcat and restart tomcat");
      
         String answer = null;
         String tomcatLocation = System.getProperty("CATALINA_HOME",null);
         String defaultConfigPath = null;
         boolean dirCheck = false;
         
      
         if(tomcatLocation != null) {
            dirCheck = isDirectory(new File(tomcatLocation));
            if(!dirCheck) {
               System.out.println("Found a server Location, but does not seem to exist or be a directory:" + tomcatLocation);
               tomcatLocation = null;
            }
         }

         while(tomcatLocation == null || tomcatLocation.trim().length() <= 0) {
            System.out.println("Could not find your server location (tomcat) location, Please give location (normally your CATALINA_HOME directory, not url)");
            tomcatLocation = d.readLine();
            //d.close();
            if(tomcatLocation != null) {
               dirCheck = isDirectory(new File(tomcatLocation));
               if(!dirCheck) {
                  System.out.println("Found a server Location, but does not seem to exist or be a directory:" + tomcatLocation);
                  tomcatLocation = null;
               }
            }         
         }
         defaultConfigPath = tomcatLocation + "/registry/server/config/server_config.properties";

         System.out.println("Where would you like all your Config file placed?  Hit <Return> to use default");
         System.out.println("(Please use full location with filename), Default: \r\n" + defaultConfigPath + "\r\n");
      
         //d = new BufferedReader(isr);          
         String configPath = d.readLine();
         if(configPath == null || configPath.trim().length() <= 0) {
            configPath = defaultConfigPath;   
         }
         configPath = configPath.trim();
         //d.close();
         File fi = new File(configPath);
         if(fi.exists()) {
            System.out.println("There seems to be a file already there by this name, do you wish to overwrite?");
            //d = new BufferedReader(new InputStreamReader(System.in));
            while(!"yes".equals(answer)  && !"no".equals(answer)) {
               System.out.println("(no - Will automatically exit) Answer: (yes|no) \r\n");
               answer = d.readLine();
            }//while
            //d.close();
            if("no".equals(answer))
               System.exit(0);
         }else {
            //System.out.println("should have created" + fi.getParent());
            if(fi.getParentFile() != null)
               fi.getParentFile().mkdirs();
         
         }
         
         if(!fi.createNewFile()) {
            if(answer == null) {         
               System.out.println("Cannot create file it seems to exist already?");
               while(!"yes".equals(answer)  && !"no".equals(answer)) {
                  System.out.println("(no - Will automatically exit) Answer: (yes|no) \r\n");
                  answer = d.readLine();
               }//while
               if("no".equals(answer))
                  System.exit(0);
            }//if
         }
      
         if(!fi.canWrite()) {
            System.out.println("Cannot write to this file");
            System.exit(0);
         }
      
         String registryAuthorityID = null; 

         //d = new BufferedReader(new InputStreamReader(System.in));
         while(registryAuthorityID == null || registryAuthorityID.trim().length() <= 0) {
            System.out.println("What is your Authority ID for this Registry? ex: leicester.astrogrid.registry\r\n");
            registryAuthorityID = d.readLine();
         }
         //d.close();
      
         registryAuthorityID = registryAuthorityID.trim();
      
         String existLocation = "localhost:8080";
         //d = new BufferedReader(new InputStreamReader(System.in));
         System.out.println("What is the location of your exist db (usually the same as the server and normally localhost if the db is on the same machine as the registry server otherwise change it)?");
         System.out.println("You may hit <Return> to use Default: " + existLocation + "\r\n");
         existLocation = d.readLine();      
         //d.close();
         if(existLocation == null || existLocation.trim().length() <= 0) {
            existLocation = "localhost:8080";
         }
         existLocation = existLocation.trim();
      
         String fullExistURI = "xmldb:exist://" + existLocation + "/exist/xmlrpc";
      
         System.out.println("Here is your full eXist URI connection.  You may change it now or hit <Return> to use it. Notice the /exist after " + existLocation + " this should correspond to the webappname under webapps for the exist database");
         System.out.println("Full eXist URI: " + fullExistURI);
         //d = new BufferedReader(new InputStreamReader(System.in));
         fullExistURI = d.readLine();
         if(fullExistURI == null || fullExistURI.trim().length() <= 0) {
            fullExistURI = "xmldb:exist://" + existLocation + "/exist/xmlrpc";
         }
         //d.close();
      
         FileWriter fw = new FileWriter(fi);
         String fileContents = "org.astrogrid.registry.authorityid=" + registryAuthorityID + "\r\n";
         fileContents += "registry.exist.db.uri=" + fullExistURI + "\r\n";
         fileContents += "org.astrogrid.registry.version=0_9\r\n";
         fw.write(fileContents);
         fw.close();
      
         String webFileLocation = tomcatLocation + "/webapps/astrogrid-registry-webapp/WEB-INF/web.xml";
      
         File webFile = new File(webFileLocation);
         if(!webFile.exists() || !webFile.isFile()) {
            String tempLocation = new String(webFileLocation);
            webFileLocation = null;
            while(webFileLocation == null) {
               System.out.println("Cannot seem to find web.xml at location or is not a file = " + tempLocation);
               System.out.println("This might be from a different name or directory than the default plese give the full location including file of 'web.xml'\r\n");
               //d = new BufferedReader(new InputStreamReader(System.in));
               webFileLocation = d.readLine();
               if(webFileLocation != null && webFileLocation.trim().length() > 0) {
                  webFile = new File(webFileLocation);
                  if(!webFile.exists() || !webFile.isFile()) {
                     tempLocation = new String(webFileLocation);
                     webFileLocation = null;   
                  }//if                  
               }//if            
            }//while
         }//if
      
         String jndiEntry = "<env-entry> \r\n<env-entry-name>org.astrogrid.config.url</env-entry-name>\r\n " +
         "<env-entry-value>file:///" + configPath + "</env-entry-value>\r\n" +
         "<env-entry-type>java.lang.String</env-entry-type>\r\n" + "</env-entry>\r\n";
         //System.out.println("the webfile information path = " + webFile.getAbsolutePath() + " read = " + webFile.canRead());
         //System.out.println("the webfile information conocalpath = " + webFile.getCanonicalPath() + " name = " + webFile.getName());
      
         if(!webFile.canRead()) {
             System.out.println("Cannot read from file");
             System.out.println("Because we cannot read from the file, please manually add entry to your web.xml then restart the registry webapp");
             System.out.println("Make sure there is not an entry already in there if so replace it or check it");          
             System.out.println("entry to add:\r\n");
             System.out.println(jndiEntry);
             System.exit(0);            
         }
      
         if(!webFile.canWrite()) {
            System.out.println("Cannot write to file");
            System.out.println("Because we cannot write to the file, please manually add entry to your web.xml then restart the registry webapp");
            System.out.println("Make sure there is not an entry already in there if so replace it or check it");
            System.out.println("entry to add:\r\n");
            System.out.println(jndiEntry);
            System.exit(0);            
         }
      
         BufferedReader buffReader = new BufferedReader(new FileReader(webFile));
         StringBuffer sb = new StringBuffer();
         String tempLine = null;
         int lineTemp = 0;
         boolean replacingConfig = false;
         while( (tempLine = buffReader.readLine()) != null) {
            if(tempLine.indexOf("org.astrogrid.config.url") != -1) {
               replacingConfig = true;
               while(tempLine != null && tempLine.indexOf("env-entry-value") == -1) {
                  sb.append(tempLine + "\r\n");
                  tempLine = buffReader.readLine();
               }
               sb.append("<env-entry-value>file:///" + configPath + "</env-entry-value>\r\n");
               while(tempLine != null && tempLine.indexOf("</env-entry-value") == -1) {
               //                  sb.append(tempLine + "\r\n");
                  tempLine = buffReader.readLine();
               }
               if(tempLine.indexOf("</env-entry-value>")!= -1) {
                  lineTemp = tempLine.indexOf("</env-entry-value>") + 18;
                  if(lineTemp < tempLine.length()) {                     
                     tempLine = tempLine.substring(lineTemp);
                     sb.append(tempLine + "\r\n");
                  }//if   
               }//if   
            }else {
               if(!replacingConfig && tempLine.indexOf("</web-app>") != -1) {
                  sb.append(jndiEntry);
                  replacingConfig = true;               
               }
               sb.append(tempLine + "\r\n");
            }//else
         }//while
         buffReader.close();
      
         PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(webFile)));
         out.write(sb.toString());
         out.close();
      
         System.out.println("the configuration of the Registry server is complete.YEAH\r\n\r\n\r\n");
         System.out.println("Please restart your registry server webapp and then finish the last steps below");
         System.out.println("=================================================================================");      
         int webappTemp = webFileLocation.indexOf("webapps");
         int endofWebappTemp = webFileLocation.indexOf("/",webappTemp+9);
         if(endofWebappTemp == -1)
            endofWebappTemp = webFileLocation.indexOf("\\",webappTemp+9);
         //System.out.println("the webfileLocation = " + webFileLocation + " webapptemp = " + webappTemp + " endofwebabb" + endofWebappTemp);
         String webappname = webFileLocation.substring(webappTemp+8,endofWebappTemp);
         //System.out.println("the webappname = " + webappname);
         chooseMenuOptionTwo(webappname,true);
         chooseMenuOptionThree(registryAuthorityID,webappname,true);
         chooseMenuOptionFour(webappname, true);
   }
   
   private static boolean isDirectory(File loc) {
      if(loc.exists() && loc.isDirectory()) return true;
      return false;      
   }

   public static void chooseMenuOptionTwo(String webappname, boolean enteredfrommenuone) throws Exception {
      System.out.println("We are beginning menu option two. Which deals with registering other registries and grabbing their information.");
      System.out.println("This option also can work from a seperate machine than the registry, just remember to change any reference to localhost to the correct location");
      
         
      InputStreamReader isr = new InputStreamReader(System.in);
      BufferedReader d = new BufferedReader(isr);
         
      String skipOption = "cont";
         
      if(webappname == null) {
         while(webappname == null || webappname.trim().length() <= 0) {
            System.out.println("What is the webapp name on the registry server.  This is usually after the webapps in your directory such as astrogrid-registry-webapp?\r\n");
            webappname = d.readLine();
         }
         webappname = webappname.trim();
      }

      if(enteredfrommenuone) {
         while(skipOption != null && skipOption.equals("cont")) {
            System.out.println("You may exit now and go onto option number 3.  Hit <Return> to continue or type 'skip' to go to number 3?\r\n");
            skipOption = d.readLine();
            if(skipOption == null || skipOption.trim().length() <= 0) 
               skipOption = null;
            else {
               if("skip".equals(skipOption))
                  return;
               else
                  skipOption = "cont";   
            }
         }//while
      }//if         
         
      String queryEndPoint = "http://msslxy.mssl.ucl.ac.uk:8080/astrogrid-registry-webapp/services/Registry";
      System.out.println("What is the query endpoint of another Registry?");
      System.out.println("A default example is given you may hit <Return> to use the default: " + queryEndPoint);

         System.out.println("Query EndPoint?\r\n");
         String tempEndPoint = d.readLine();
            
      if(tempEndPoint != null && tempEndPoint.trim().length() > 0) {
         queryEndPoint = tempEndPoint;
      }

         
      URL url = new URL(queryEndPoint);
      RegistryService rs = new RegistryService(url);
      Document doc = rs.loadRegistryDOM();
      System.out.println("other regs loadregistry = " + DomHelper.DocumentToString(doc));
         

      String endPoint = "http://localhost:8080/" + webappname + "/services/RegistryHarvest";
      String endPointCheck = null;
      System.out.println("Okay almost done for menu option number two, just need to start the harvest, by using the harvest endpoint for this registry.");
      System.out.println("From your previous input, it is set to:" + endPoint);
      System.out.println("You can enter a new url now or hit <Return> to use the above endpoint");
      //d = new BufferedReader(new InputStreamReader(System.in));
      endPointCheck = d.readLine();
      if(endPointCheck != null && endPointCheck.trim().length() > 0) 
         endPoint = endPointCheck;
            
      URL url2 = new URL(endPoint);
      RegistryHarvestService rhs = new RegistryHarvestService(url2);
      rhs.harvestResource(doc);
         
      String cont = null;      
      while(cont == null || cont.trim().length() <= 0) {
         System.out.println("Would you like to do this option again. Normally your harvest should pick up other entries, but you can add other registries if needed? (y|n)");
         cont = d.readLine();
         if("y".equals(cont)) {
            chooseMenuOptionTwo(webappname,enteredfrommenuone);
         } else if("n".equals(cont)) {
            return;   
         }else {
            cont = null;   
         }
      }
   }
   
   
   public static void chooseMenuOptionThree(String registryAuthorityID,String webappname,boolean enteredfrommenuone) throws Exception {
      System.out.println("Entered menu option three.  Dealing with adding/updating a new registry resource entry to a registry");
      System.out.println("This option also can work from a seperate machine than the registry, just remember to change any reference to localhost to the correct location");
      
      InputStreamReader isr = new InputStreamReader(System.in);
      BufferedReader d = new BufferedReader(isr);
 
      String skipOption = "cont";
         
      if(enteredfrommenuone) {
         while(skipOption != null && skipOption.equals("cont")) {
            System.out.println("You may exit now and go onto option number 3.  Hit <Return> to continue or type 'skip' to go to number 3?\r\n");
            skipOption = d.readLine();
            if(skipOption == null || skipOption.trim().length() <= 0) 
               skipOption = null;
            else {
               if("skip".equals(skipOption))
                  return;
               else
                  skipOption = "cont";   
            }
         }//while
      }//if         
         

      if(registryAuthorityID == null) {            
         while(registryAuthorityID == null || registryAuthorityID.trim().length() <= 0) {
            System.out.println("What is your Authority ID for this Registry? ex: leicester.astrogrid.registry \r\n");
            registryAuthorityID = d.readLine();
         }
         registryAuthorityID = registryAuthorityID.trim();
      }
      
 
      if(webappname == null) {
         while(webappname == null || webappname.trim().length() <= 0) {
            System.out.println("What is the webapp name on the registry server.  This is usually after the webapps in your directory such as astrogrid-registry-webapp?\r\n");
            webappname = d.readLine();
         }
         webappname = webappname.trim();
      }
         
         
               
      System.out.println("This installer only asks the key requirements for a registry entry (except ResourceKey)");

      String resKey = null;
         

      System.out.println("What is the ResourceKey of the registry?");
      System.out.println("This is the only entry where your allowed to hit <Return> and have no ResourceKey");
      resKey = d.readLine();
      //d.close();
      if(resKey != null && resKey.trim().length() > 0)
         resKey = resKey.trim();
      else
         resKey = null;

      
      String title = null;
      //d = new BufferedReader(new InputStreamReader(System.in));
      while(title == null || title.trim().length() <= 0) {
         System.out.println("What is the title of the registry? ex: Leicester astrogrid registry");
         title = d.readLine();
      }
      //d.close();
      title = title.trim();
      
      String desc = null;
      //d = new BufferedReader(new InputStreamReader(System.in));
      while(desc == null || desc.trim().length() <= 0) {
         System.out.println("What is the description of the registry? ex: A full registry handling metadata in the Leicester area. ");         
         desc = d.readLine();
      }
      //d.close();
      desc = desc.trim();

      String refURL = null;
      //d = new BufferedReader(new InputStreamReader(System.in));
      while(refURL == null || refURL.trim().length() <= 0) {
         System.out.println("What is the reference URL of the registry? ex: http://www.astrogrid.portal.org");         
         refURL = d.readLine();
      }
      //d.close();
      refURL = refURL.trim();

      String contactName = null;
      //d = new BufferedReader(new InputStreamReader(System.in));
      while(contactName == null || contactName.trim().length() <= 0) {
         System.out.println("Who is the contact name of the registry? ex: Tony Linde");         
         contactName = d.readLine();
      }
      //d.close();
      contactName = contactName.trim();

      String pubTitle = null;
      //d = new BufferedReader(new InputStreamReader(System.in));
      while(pubTitle == null || pubTitle.trim().length() <= 0) {
         System.out.println("What is the Publisher's title (The currator for this registry)? ex: Keith Noddle");         
         pubTitle = d.readLine();
      }
      //d.close();
      pubTitle = pubTitle.trim();
      
      String interfaceType = null;
      //d = new BufferedReader(new InputStreamReader(System.in));
      while(interfaceType == null || interfaceType.trim().length() <= 0) {
         System.out.println("What is the interface Type must be WebService OR WebBrowser normally WEBSERVICE for astrogrid?");
         interfaceType = d.readLine();
         if("WebService".equals(interfaceType) || "WebBrowser".equals(interfaceType) ) {
            //do nothing
         }else {
            interfaceType = null;
         }//else
      }
      //d.close();
      interfaceType = interfaceType.trim();

      String accessURL = null;
      //d = new BufferedReader(new InputStreamReader(System.in));
      while(accessURL == null || accessURL.trim().length() <= 0) {
         System.out.println("What is the AccessURL for the registry. Needs to be a WSDL for a WebService type (currently this installer does not check though that it is a wsdl)?");
         System.out.println("Example is this, but DO NOT use loacalhost ex: http://localhost:8080/astrogrid-registry-webapp/services/RegistryHarvest?wsdl")         
         accessURL = d.readLine();
      }
      //d.close();
      accessURL = accessURL.trim();
      
      Document doc = getRegistryEntry(registryAuthorityID,resKey, title,desc,refURL,contactName,pubTitle,interfaceType,accessURL);
      
      String endPoint = "http://localhost:8080/" + webappname + "/services/RegistryHarvest";
      String endPointCheck = null;
      System.out.println("Okay almost done to put your new registry entry we need the endpoint of your harvest service.");
      System.out.println("From your previous input, it is set to:" + endPoint);
      System.out.println("You can enter a new url now or hit <Return> to use the above endpoint");
      //d = new BufferedReader(new InputStreamReader(System.in));
      endPointCheck = d.readLine();
      if(endPointCheck != null && endPointCheck.trim().length() > 0) 
         endPoint = endPointCheck;
      
      System.out.println("Now submitting entry");
      URL url = new URL(endPoint);
      RegistryHarvestService rhs = new RegistryHarvestService(url);
      rhs.harvestResource(doc);
   }
   
   public static Document getRegistryEntry(String authID,String resKey, String title, String desc, String refURL, String contactName, String pubTitle, String interfaceType, String accessURL) throws Exception {
      StringBuffer sb = new StringBuffer();
      sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
      sb.append("<VODescription xmlns=\"http://www.ivoa.net/xml/VOResource/v0.9\" ");
      sb.append(" xmlns:vr=\"http://www.ivoa.net/xml/VOResource/v0.9\" ");
      sb.append(" xmlns:vc=\"http://www.ivoa.net/xml/VOCommunity/v0.2\" ");
      sb.append(" xmlns:vg=\"http://www.ivoa.net/xml/VORegistry/v0.2\" ");
      sb.append(" xmlns:vs=\"http://www.ivoa.net/xml/VODataService/v0.4\" ");
      sb.append(" xmlns:vt=\"http://www.ivoa.net/xml/VOTable/v0.1\" ");
      sb.append(" xmlns:cs=\"http://www.ivoa.net/xml/ConeSearch/v0.2\" ");
      sb.append(" xmlns:sia=\"http://www.ivoa.net/xml/SIA/v0.6\" ");
      sb.append(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" >");
      
      sb.append("<vr:Resource xsi:type=\"RegistryType\">");
      sb.append("<Identifier>");
      sb.append("<AuthorityID>" + authID + "</AuthorityID>");
      if(resKey == null)
         sb.append("<ResourceKey xsi:nil=\"true\" />");
      else
         sb.append("<ResourceKey>" + resKey + "</ResourceKey>");
      sb.append("</Identifier>");
      sb.append("<Title>" + title + "</Title>");
      sb.append("<Summary><Description>" + desc + "</Description>");
      sb.append("<ReferenceURL>" + refURL + "</ReferenceURL></Summary>");
      sb.append("<Curation><Publisher><Title>" + pubTitle + "</Title></Publisher>");
      sb.append("<Contact><Name>" + contactName + "</Name></Contact></Curation>");
      sb.append("<vg:ManagedAuthority>" + authID + "</vg:ManagedAuthority>");
      sb.append("<Interface><Invocation>" + interfaceType + "</Invocation>");
      sb.append("<AccessURL use=\"full\">" + accessURL + "</AccessURL></Interface>");
      sb.append("</vr:Resource></VODescription>");

      Reader reader = new StringReader(sb.toString());
      InputSource inputSource = new InputSource(reader);
      DocumentBuilder registryBuilder = null;
      registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = registryBuilder.parse(inputSource);
      System.out.println("the look of the entry being updated in the db is = " + DomHelper.DocumentToString(doc));
      return doc;
   }

   public static void chooseMenuOptionFour(String webappname, boolean enteredfrommenuone) throws Exception {
   System.out.println("We are beginning menu option four. Which deals with letting you add/update information in a registry from a file or url.");
   System.out.println("This option also can work from a seperate machine than the registry, just remember to change any reference to localhost to the correct location");
            
   InputStreamReader isr = new InputStreamReader(System.in);
   BufferedReader d = new BufferedReader(isr);
         
   String skipOption = "cont";
         
   if(webappname == null) {
      while(webappname == null || webappname.trim().length() <= 0) {
         System.out.println("What is the webapp name.  This is usually after the webapps in your directory such as astrogrid-registry-webapp?\r\n");
         webappname = d.readLine();
      }
      webappname = webappname.trim();
   }

   if(enteredfrommenuone) {
      while(skipOption != null && skipOption.equals("cont")) {
         System.out.println("You may exit now and go onto option number 4.  Hit <Return> to continue or type 'skip' to exit?\r\n");
         skipOption = d.readLine();
         if(skipOption == null || skipOption.trim().length() <= 0) 
            skipOption = null;
         else {
            if("skip".equals(skipOption))
               return;
            else
               skipOption = "cont";   
         }
      }//while
   }//if         
         
   String endPoint = "http://localhost:8080/" + webappname + "/services/RegistryAdmin";
   String endPointCheck = null;
   System.out.println("Okay this option just needs your Admin endpoint.");
   System.out.println("From your previous input, it is set to:" + endPoint);
   System.out.println("You can enter a new url now or hit <Return> to use the above endpoint");
   //d = new BufferedReader(new InputStreamReader(System.in));
   endPointCheck = d.readLine();
   if(endPointCheck != null && endPointCheck.trim().length() > 0) 
      endPoint = endPointCheck;

   URL url = new URL(endPoint);
   RegistryAdminService ras = new RegistryAdminService(url);

         
   System.out.println("What would you like to upload from a file or url?");
   System.out.println("1.) File on this machine");
   System.out.println("2.) URL (any public location)");
      
   int menuOption = chooseMenuOption();
   String loc = null;
   if(menuOption == 2) {
                  
      while(loc == null || loc.trim().length() <= 0) {
         System.out.println("Please give the url");
         loc = d.readLine();  
      }//while
      URL urlPlace = new URL(loc);
      ras.updateFromURL(urlPlace);
   }
   if(menuOption == 1) {
         
      while(loc == null || loc.trim().length() <= 0) {
         System.out.println("Please give the url");
         loc = d.readLine();  
      }//while
      File fi = new File(loc);
      ras.updateFromFile(fi);
   }
   String cont = null;      
   while(cont == null || cont.trim().length() <= 0) {
      System.out.println("Would you like to do this option again? (y|n)");
      cont = d.readLine();
      if("y".equals(cont)) {
         chooseMenuOptionFour(webappname,enteredfrommenuone);
      } else if("n".equals(cont)) {
         return;   
      }else {
         cont = null;   
      }
   }
}
   
   
}
   
  /* 
   
   private static boolean isDirectory(File loc) {
      if(loc.exists() && loc.isDirectory()) return true;
      return false;      
   }
   */
     /* 
   private static void menuOptionOne() throws Exception {
      
         InputStreamReader isr = new InputStreamReader(System.in);
         BufferedReader d = new BufferedReader(isr);
         System.out.println("Please Copy the astrogrid-registry-webapp.war and exist.war to your tomcat and restart tomcat");
      
         String answer = null;
         String tomcatLocation = System.getProperty("CATALINA_HOME",null);
         String defaultConfigPath = null;
         boolean dirCheck = false;
         
      
         if(tomcatLocation != null) {
            dirCheck = isDirectory(new File(tomcatLocation));
            if(!dirCheck) {
               System.out.println("Found a server Location, but does not seem to exist or be a directory:" + tomcatLocation);
               tomcatLocation = null;
            }
         }

         while(tomcatLocation == null || tomcatLocation.trim().length() <= 0) {
            System.out.println("Could not find your server location (tomcat) location, Please give location (normally your CATALINA_HOME directory, not url)");
            tomcatLocation = d.readLine();
            //d.close();
            if(tomcatLocation != null) {
               dirCheck = isDirectory(new File(tomcatLocation));
               if(!dirCheck) {
                  System.out.println("Found a server Location, but does not seem to exist or be a directory:" + tomcatLocation);
                  tomcatLocation = null;
               }
            }         
         }
         defaultConfigPath = tomcatLocation + "/registry/server/config/server_config.properties";

         System.out.println("Where would you like all your Config file placed?  Hit <Return> to use default");
         System.out.println("(Please use full location with filename), Default: \r\n" + defaultConfigPath + "\r\n");
      
         //d = new BufferedReader(isr);          
         String configPath = d.readLine();
         if(configPath == null || configPath.trim().length() <= 0) {
            configPath = defaultConfigPath;   
         }
         configPath = configPath.trim();
         //d.close();
         File fi = new File(configPath);
         if(fi.exists()) {
            System.out.println("There seems to be a file already there by this name, do you wish to overwrite?");
            //d = new BufferedReader(new InputStreamReader(System.in));
            while(!"yes".equals(answer)  && !"no".equals(answer)) {
               System.out.println("(no - Will automatically exit) Answer: (yes|no) \r\n");
               answer = d.readLine();
            }//while
            //d.close();
            if("no".equals(answer))
               System.exit(0);
         }else {
            //System.out.println("should have created" + fi.getParent());
            if(fi.getParentFile() != null)
               fi.getParentFile().mkdirs();
         
         }
         
         if(!fi.createNewFile()) {
            if(answer == null) {         
               System.out.println("Cannot create file it seems to exist already?");
               while(!"yes".equals(answer)  && !"no".equals(answer)) {
                  System.out.println("(no - Will automatically exit) Answer: (yes|no) \r\n");
                  answer = d.readLine();
               }//while
               if("no".equals(answer))
                  System.exit(0);
            }//if
         }
      
         if(!fi.canWrite()) {
            System.out.println("Cannot write to this file");
            System.exit(0);
         }
      
         String registryAuthorityID = null; 

         //d = new BufferedReader(new InputStreamReader(System.in));
         while(registryAuthorityID == null || registryAuthorityID.trim().length() <= 0) {
            System.out.println("What is your Authority ID for this Registry?\r\n");
            registryAuthorityID = d.readLine();
         }
         //d.close();
      
         registryAuthorityID = registryAuthorityID.trim();
      
         String existLocation = "localhost:8080";
         //d = new BufferedReader(new InputStreamReader(System.in));
         System.out.println("What is the location of your exist db (usually the same as the server and normally localhost if the db is on the same machine as the registry server otherwise change it)?");
         System.out.println("You may hit <Return> to use Default: " + existLocation + "\r\n");
         existLocation = d.readLine();      
         //d.close();
         if(existLocation == null || existLocation.trim().length() <= 0) {
            existLocation = "localhost:8080";
         }
         existLocation = existLocation.trim();
      
         String fullExistURI = "xmldb:exist://" + existLocation + "/exist/xmlrpc";
      
         System.out.println("Here is your full eXist URI connection.  You may change it now or hit <Return> to use it");
         System.out.println("Full eXist URI: " + fullExistURI);
         //d = new BufferedReader(new InputStreamReader(System.in));
         fullExistURI = d.readLine();
         if(fullExistURI == null || fullExistURI.trim().length() <= 0) {
            fullExistURI = "xmldb:exist://" + existLocation + "/exist/xmlrpc";
         }
         //d.close();
      
         FileWriter fw = new FileWriter(fi);
         String fileContents = "org.astrogrid.registry.authorityid=" + registryAuthorityID + "\r\n";
         fileContents += "registry.exist.db.uri=" + fullExistURI + "\r\n";
         fw.write(fileContents);
         fw.close();
      
         String webFileLocation = tomcatLocation + "/webapps/astrogrid-registry-webapp/WEB-INF/web.xml";
      
         File webFile = new File(webFileLocation);
         if(!webFile.exists() || !webFile.isFile()) {
            String tempLocation = new String(webFileLocation);
            webFileLocation = null;
            while(webFileLocation == null) {
               System.out.println("Cannot seem to find web.xml at location or is not a file = " + tempLocation);
               System.out.println("This might be from a different name or directory than the default plese give the full location including file of 'web.xml'\r\n");
               //d = new BufferedReader(new InputStreamReader(System.in));
               webFileLocation = d.readLine();
               if(webFileLocation != null && webFileLocation.trim().length() > 0) {
                  webFile = new File(webFileLocation);
                  if(!webFile.exists() || !webFile.isFile()) {
                     tempLocation = new String(webFileLocation);
                     webFileLocation = null;   
                  }//if                  
               }//if            
            }//while
         }//if
      
         String jndiEntry = "<env-entry> \r\n<env-entry-name>org.astrogrid.config.url</env-entry-name>\r\n " +
         "<env-entry-value>file:///" + configPath + "</env-entry-value>\r\n" +
         "<env-entry-type>java.lang.String</env-entry-type>\r\n" + "</env-entry>\r\n";
         //System.out.println("the webfile information path = " + webFile.getAbsolutePath() + " read = " + webFile.canRead());
         //System.out.println("the webfile information conocalpath = " + webFile.getCanonicalPath() + " name = " + webFile.getName());
      
         if(!webFile.canRead()) {
             System.out.println("Cannot read from file");
             System.out.println("Because we cannot read from the file, please manually add entry to your web.xml then restart the registry webapp");
             System.out.println("Make sure there is not an entry already in there if so replace it or check it");          
             System.out.println("entry to add:\r\n");
             System.out.println(jndiEntry);
             System.exit(0);            
         }
      
         if(!webFile.canWrite()) {
            System.out.println("Cannot write to file");
            System.out.println("Because we cannot write to the file, please manually add entry to your web.xml then restart the registry webapp");
            System.out.println("Make sure there is not an entry already in there if so replace it or check it");
            System.out.println("entry to add:\r\n");
            System.out.println(jndiEntry);
            System.exit(0);            
         }
      
         BufferedReader buffReader = new BufferedReader(new FileReader(webFile));
         StringBuffer sb = new StringBuffer();
         String tempLine = null;
         int lineTemp = 0;
         boolean replacingConfig = false;
         while( (tempLine = buffReader.readLine()) != null) {
            if(tempLine.indexOf("org.astrogrid.config.url") != -1) {
               replacingConfig = true;
               if(tempLine.indexOf("env-entry-name") != -1) {
                  tempLine.substring(0,tempLine.indexOf("<env-entry-name"));
                  sb.append(tempLine);
                  lineTemp = tempLine.indexOf("</env-entry-name>") + 17;
                  if(lineTemp < tempLine.length()) {                     
                     tempLine = tempLine.substring(lineTemp);
                     sb.append(tempLine + "\r\n");
                  }//if  
               }else {
                  while(tempLine != null && tempLine.indexOf("env-entry-name") == -1) {
                     sb.append(tempLine + "\r\n");
                     tempLine = buffReader.readLine();
                  }
                  if(tempLine == null) {
                     System.out.println("Something went wrong, found a org.astrogrid.config.url entry in the web.xml file, but no value for it so could not overwrite it.  Now exiting");
                     System.out.println("add jndi entry to your web.xml:\r\n" + jndiEntry);   
                     System.exit(1);
                  }
                  if(tempLine.indexOf("env-entry-name") != -1) {
                     sb.append("<env-entry-value>file:///" + configPath + "</env-entry-value>\r\n");
                     if(tempLine.indexOf("</env-entry-name>")!= -1) {
                        lineTemp = tempLine.indexOf("</env-entry-name>") + 17;
                        if(lineTemp < tempLine.length()) {                     
                           tempLine = tempLine.substring(lineTemp);
                           sb.append(tempLine + "\r\n");
                        }//if   
                     }//if   
                  }//if                  
               }//else
            }else {
               if(!replacingConfig && tempLine.indexOf("</web-app>") != -1) {
                  sb.append(jndiEntry);
                  replacingConfig = true;               
               }
               sb.append(tempLine + "\r\n");
            }//else
         }//while
         buffReader.close();
      
         PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(webFile)));
         out.write(sb.toString());
         out.close();
      
         System.out.println("the configuration of the Registry server is complete.YEAH\r\n\r\n\r\n");
         System.out.println("Please restart your registry server webapp and then finish the last steps below");
         System.out.println("=================================================================================");      
         int webappTemp = webFileLocation.indexOf("webapps");
         int endofWebappTemp = webFileLocation.indexOf("/",webappTemp+9);
         if(endofWebappTemp == -1)
            endofWebappTemp = webFileLocation.indexOf("\\",webappTemp+9);
         //System.out.println("the webfileLocation = " + webFileLocation + " webapptemp = " + webappTemp + " endofwebabb" + endofWebappTemp);
         String webappname = webFileLocation.substring(webappTemp+8,endofWebappTemp);
         //System.out.println("the webappname = " + webappname);
         //chooseMenuOptionTwo(webappname,true);
         //chooseMenuOptionThree(registryAuthorityID,webappname,true);
         //chooseMenuOptionFour(webappname, true);
   }
   */
   /*
      public static void chooseMenuOptionTwo(String webappname, boolean enteredfrommenuone) throws Exception {
         System.out.println("We are beginning menu option two. Which deals with registering other registries and grabbing their information.");
         
         InputStreamReader isr = new InputStreamReader(System.in);
         BufferedReader d = new BufferedReader(isr);
         
         String skipOption = "cont";
         
         if(webappname == null) {
            while(webappname == null || webappname.trim().length() <= 0) {
               System.out.println("What is the webapp name.  This is usually after the webapps in your directory such as astrogrid-registry-webapp?\r\n");
               webappname = d.readLine();
            }
            webappname = webappname.trim();
         }

         if(enteredfrommenuone) {
            while(skipOption != null && skipOption.equals("cont")) {
               System.out.println("You may exit now and go onto option number 3.  Hit <Return> to continue or type 'skip' to go to number 3?\r\n");
               skipOption = d.readLine();
               if(skipOption == null || skipOption.trim().length() <= 0) 
                  skipOption = null;
               else {
                  if("skip".equals(skipOption))
                     return;
                  else
                     skipOption = "cont";   
               }
            }//while
         }//if         
         
         String queryEndPoint = "http://msslxy.mssl.ucl.ac.uk/astrogrid-registry-webapp/services/Registry";
         System.out.println("What is the query endpoint of another Registry?");
         System.out.println("A default example is you may hit <Return> to use the default: " + queryEndPoint);

            System.out.println("Query EndPoint?\r\n");
            String tempEndPoint = d.readLine();
            
         if(tempEndPoint != null || tempEndPoint.trim().length() > 0) {
            queryEndPoint = tempEndPoint;
         }

         
         URL url = new URL(queryEndPoint);
         RegistryService rs = new RegistryService();
         Document doc = rs.loadRegistryDOM();
         System.out.println("other regs loadregistry = " + DomHelper.DocumentToString(doc));
         

         String endPoint = "http://localhost:8080/" + webappname + "/services/RegistryHarvest";
         String endPointCheck = null;
         System.out.println("Okay almost done for menu option number two, just need to start the harvest, by using the getting the harvest endpoint for this registry.");
         System.out.println("From your previous input, it is set to:" + endPoint);
         System.out.println("You can enter a new url now or hit <Return> to use the above endpoint");
         //d = new BufferedReader(new InputStreamReader(System.in));
         endPointCheck = d.readLine();
         if(endPointCheck != null && endPointCheck.trim().length() > 0) 
            endPoint = endPointCheck;
            
         URL url2 = new URL(endPoint);
         RegistryHarvestService rhs = new RegistryHarvestService(url2);
         rhs.harvestResource(doc);
         
         String cont = null;      
         while(cont == null || cont.trim().length() <= 0) {
            System.out.println("Would you like to do this option again. Normally your harvest should pick up other entries, but you can add other registries if needed? (y|n)");
            cont = d.readLine();
            if("y".equals(cont)) {
               chooseMenuOptionFour(webappname,enteredfrommenuone);
            } else if("n".equals(cont)) {
               return;   
            }else {
               cont = null;   
            }
         }
      }
   
   
      public static void chooseMenuOptionThree(String registryAuthorityID,String webappname,boolean enteredfrommenuone) throws Exception {
         System.out.println("Entered menu option three.  Dealing with adding a new registry resource entry to this registry");

         InputStreamReader isr = new InputStreamReader(System.in);
         BufferedReader d = new BufferedReader(isr);
 
         String skipOption = "cont";
         
         if(enteredfrommenuone) {
            while(skipOption != null && skipOption.equals("cont")) {
               System.out.println("You may exit now and go onto option number 3.  Hit <Return> to continue or type 'skip' to go to number 3?\r\n");
               skipOption = d.readLine();
               if(skipOption == null || skipOption.trim().length() <= 0) 
                  skipOption = null;
               else {
                  if("skip".equals(skipOption))
                     return;
                  else
                     skipOption = "cont";   
               }
            }//while
         }//if         
         

         if(registryAuthorityID == null) {            
            while(registryAuthorityID == null || registryAuthorityID.trim().length() <= 0) {
               System.out.println("What is your Authority ID for this Registry?\r\n");
               registryAuthorityID = d.readLine();
            }
            registryAuthorityID = registryAuthorityID.trim();
         }
      
 
         if(webappname == null) {
            while(webappname == null || webappname.trim().length() <= 0) {
               System.out.println("What is the webapp name.  This is usually after the webapps in your directory such as astrogrid-registry-webapp?\r\n");
               webappname = d.readLine();
            }
            webappname = webappname.trim();
         }
         
         
               
         System.out.println("This installer only asks the key requirements for a registry entry");

         String resKey = null;
         

         System.out.println("What is the ResourceKey of the registry?");
         System.out.println("This is the only entry where your allowed to hit <Return> and have no ResourceKey");
         resKey = d.readLine();
         //d.close();
         if(resKey != null && resKey.trim().length() > 0)
            resKey = resKey.trim();
         else
            resKey = null;

      
         String title = null;
         //d = new BufferedReader(new InputStreamReader(System.in));
         while(title == null || title.trim().length() <= 0) {
            System.out.println("What is the title of the registry?");
            title = d.readLine();
         }
         //d.close();
         title = title.trim();
      
         String desc = null;
         //d = new BufferedReader(new InputStreamReader(System.in));
         while(desc == null || desc.trim().length() <= 0) {
            System.out.println("What is the description of the registry?");         
            desc = d.readLine();
         }
         //d.close();
         desc = desc.trim();

         String refURL = null;
         //d = new BufferedReader(new InputStreamReader(System.in));
         while(refURL == null || refURL.trim().length() <= 0) {
            System.out.println("What is the reference URL of the registry?");         
            refURL = d.readLine();
         }
         //d.close();
         refURL = refURL.trim();

         String contactName = null;
         //d = new BufferedReader(new InputStreamReader(System.in));
         while(contactName == null || contactName.trim().length() <= 0) {
            System.out.println("Who is the contact name of the registry?");         
            contactName = d.readLine();
         }
         //d.close();
         contactName = contactName.trim();

         String pubTitle = null;
         //d = new BufferedReader(new InputStreamReader(System.in));
         while(pubTitle == null || pubTitle.trim().length() <= 0) {
            System.out.println("What is the Publisher's title?");         
            pubTitle = d.readLine();
         }
         //d.close();
         pubTitle = pubTitle.trim();
      
         String interfaceType = null;
         //d = new BufferedReader(new InputStreamReader(System.in));
         while(interfaceType == null || interfaceType.trim().length() <= 0) {
            System.out.println("What is the interface Type must be WEBSERVICE OR WEBBROWSER normally WEBSERVICE for astrogrid?");
            interfaceType = d.readLine();
            if("WEBSERVICE".equals(interfaceType) || "WEBBROWSER".equals(interfaceType) ) {
               //do nothing
            }else {
               interfaceType = null;
            }//else
         }
         //d.close();
         interfaceType = interfaceType.trim();

         String accessURL = null;
         //d = new BufferedReader(new InputStreamReader(System.in));
         while(accessURL == null || accessURL.trim().length() <= 0) {
            System.out.println("What is the AccessURL for the registry. Needs to be a WSDL for a WEBSERVICE type (currently this installer does not check though that it is a wsdl)?");         
            accessURL = d.readLine();
         }
         //d.close();
         accessURL = accessURL.trim();
      
         Document doc = getRegistryEntry(registryAuthorityID,resKey, title,desc,refURL,contactName,pubTitle,interfaceType,accessURL);
      
         String endPoint = "http://localhost:8080/" + webappname + "/services/RegistryHarvest";
         String endPointCheck = null;
         System.out.println("Okay almost done to put your new registry entry we need the endpoint of your harvest service.");
         System.out.println("From your previous input, it is set to:" + endPoint);
         System.out.println("You can enter a new url now or hit <Return> to use the above endpoint");
         //d = new BufferedReader(new InputStreamReader(System.in));
         endPointCheck = d.readLine();
         if(endPointCheck != null && endPointCheck.trim().length() > 0) 
            endPoint = endPointCheck;
      
         System.out.println("Now submitting entry");
         URL url = new URL(endPoint);
         RegistryHarvestService rhs = new RegistryHarvestService(url);
         rhs.harvestResource(doc);
      }
   
      public static Document getRegistryEntry(String authID,String resKey, String title, String desc, String refURL, String contactName, String pubTitle, String interfaceType, String accessURL) throws Exception {
         StringBuffer sb = new StringBuffer();
         sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
         sb.append("<VODescription xmlns=\"http://www.ivoa.net/xml/VOResource/v0.9\" ");
         sb.append(" xmlns:vr=\"http://www.ivoa.net/xml/VOResource/v0.9\" ");
         sb.append(" xmlns:vc=\"http://www.ivoa.net/xml/VOCommunity/v0.2\" ");
         sb.append(" xmlns:vg=\"http://www.ivoa.net/xml/VORegistry/v0.2\" ");
         sb.append(" xmlns:vs=\"http://www.ivoa.net/xml/VODataService/v0.4\" ");
         sb.append(" xmlns:vt=\"http://www.ivoa.net/xml/VOTable/v0.1\" ");
         sb.append(" xmlns:cs=\"http://www.ivoa.net/xml/ConeSearch/v0.2\" ");
         sb.append(" xmlns:sia=\"http://www.ivoa.net/xml/SIA/v0.6\" ");
         sb.append(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" >");
      
         sb.append("<vr:Resource xsi:type=\"RegistryType\">");
         sb.append("<Identifier>");
         sb.append("<AuthorityID>" + authID + "</AuthorityID>");
         if(resKey == null)
            sb.append("<ResourceKey xsi:nil=\"true\" />");
         else
            sb.append("<ResourceKey>" + resKey + "</ResourceKey>");
         sb.append("</Identifier>");
         sb.append("<Title>" + title + "</Title>");
         sb.append("<Summary><Description>" + desc + "</Description>");
         sb.append("<ReferenceURL>" + refURL + "</ReferenceURL></Summary>");
         sb.append("<Curation><Publisher><Title>" + pubTitle + "</Title></Publisher>");
         sb.append("<Contact><Name>" + contactName + "</Name></Contact></Curation>");
         sb.append("<vg:ManagedAuthority>" + authID + "</vg:ManagedAuthority>");
         sb.append("<Interface><Invocation>" + interfaceType + "</Invocation>");
         sb.append("<AccessURL use=\"full\">" + accessURL + "</AccessURL></Interface>");
         sb.append("</vr:Resource></VODescription>");

         Reader reader = new StringReader(sb.toString());
         InputSource inputSource = new InputSource(reader);
         DocumentBuilder registryBuilder = null;
         registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         Document doc = registryBuilder.parse(inputSource);
         System.out.println("the look of the entry being updated in the db is = " + DomHelper.DocumentToString(doc));
         return doc;
      }

   public static void chooseMenuOptionFour(String webappname, boolean enteredfrommenuone) throws Exception {
      System.out.println("We are beginning menu option four. Which deals with letting you add/update informatin in this registry from a file or url.");
         
      InputStreamReader isr = new InputStreamReader(System.in);
      BufferedReader d = new BufferedReader(isr);
         
      String skipOption = "cont";
         
      if(webappname == null) {
         while(webappname == null || webappname.trim().length() <= 0) {
            System.out.println("What is the webapp name.  This is usually after the webapps in your directory such as astrogrid-registry-webapp?\r\n");
            webappname = d.readLine();
         }
         webappname = webappname.trim();
      }

      if(enteredfrommenuone) {
         while(skipOption != null && skipOption.equals("cont")) {
            System.out.println("You may exit now and go onto option number 3.  Hit <Return> to continue or type 'skip' to go to number 3?\r\n");
            skipOption = d.readLine();
            if(skipOption == null || skipOption.trim().length() <= 0) 
               skipOption = null;
            else {
               if("skip".equals(skipOption))
                  return;
               else
                  skipOption = "cont";   
            }
         }//while
      }//if         
         
      String endPoint = "http://localhost:8080/" + webappname + "/services/RegistryAdmin";
      String endPointCheck = null;
      System.out.println("Okay this option just needs your Admin endpoint.");
      System.out.println("From your previous input, it is set to:" + endPoint);
      System.out.println("You can enter a new url now or hit <Return> to use the above endpoint");
      //d = new BufferedReader(new InputStreamReader(System.in));
      endPointCheck = d.readLine();
      if(endPointCheck != null && endPointCheck.trim().length() > 0) 
         endPoint = endPointCheck;

      URL url = new URL(endPoint);
      RegistryAdminService ras = new RegistryAdminService(url);

         
      System.out.println("What would you like to upload from a file or url?");
      System.out.println("1.) File on this machine");
      System.out.println("2.) URL (any public location)");
      
      int menuOption = chooseMenuOption();
      String loc = null;
      if(menuOption == 2) {
                  
         while(loc == null || loc.trim().length() <= 0) {
            System.out.println("Please give the url");
            loc = d.readLine();  
         }//while
         URL urlPlace = new URL(loc);
         ras.updateFromURL(urlPlace);
      }
      if(menuOption == 1) {
         
         while(loc == null || loc.trim().length() <= 0) {
            System.out.println("Please give the url");
            loc = d.readLine();  
         }//while
         File fi = new File(loc);
         ras.updateFromFile(fi);
      }
      String cont = null;      
      while(cont == null || cont.trim().length() <= 0) {
         System.out.println("Would you like to do this option again? (y|n)");
         cont = d.readLine();
         if("y".equals(cont)) {
            chooseMenuOptionFour(webappname,enteredfrommenuone);
         } else if("n".equals(cont)) {
            return;   
         }else {
            cont = null;   
         }
      }
   }
 }
   */
   
   
   
