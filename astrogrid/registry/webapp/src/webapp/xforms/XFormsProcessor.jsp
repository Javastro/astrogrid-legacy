<%@ page import="org.astrogrid.config.SimpleConfig,
                 org.w3c.dom.NodeList,
                 org.w3c.dom.Node,
                 org.w3c.dom.Element,
                 org.w3c.dom.Document,   
                 org.astrogrid.util.DomHelper,
                 java.net.*,
                 java.io.*,
                 java.util.*,
                 org.astrogrid.registry.server.RegistryServerHelper,
                 javax.xml.transform.*,
                 javax.xml.transform.dom.*,
                 javax.xml.transform.stream.*,
                 org.astrogrid.registry.server.query.*"
   isThreadSafe="false"
   session="false"
%>
<html>
<head>
<title>XForms Processing</title>
<style type="text/css" media="all">
          @import url("../style/astrogrid.css");
</style>
</head>

<body>
<%@ include file="../header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>


<%
Hashtable xformsMapping = new Hashtable();

xformsMapping.put("Organisation_0.10",new String[]{"organisation"} );
xformsMapping.put("Authority_0.10",new String[]{"authority"} );
xformsMapping.put("DataCollection_0.10",new String[]{"datacollection"} );
xformsMapping.put("TabularDB_0.10",new String[]{"datacollection","datacollection_tabulardb"} );
xformsMapping.put("Service_0.10",new String[]{"service"} );
xformsMapping.put("Registry_0.10",new String[]{"service","service_registry"} );
xformsMapping.put("SkyService_0.10",new String[]{"service","service_skyservice"} );
xformsMapping.put("TabularSkyService_0.10",new String[]{"service","service_skyservice","service_skyservice_tabularskyservice",} );
xformsMapping.put("ConeSearch_0.10",new String[]{"service","service_skyservice","service_skyservice_tabularskyservice","service_skyservice_tabularskyservice_conesearch"} );
xformsMapping.put("SimpleImageAccess_0.10",new String[]{"service","service_skyservice","service_skyservice_tabularskyservice","service_skyservice_tabularskyservice_simpleimageaccess"} );


ClassLoader loader = this.getClass().getClassLoader();
InputStream  is = loader.getResourceAsStream("xforms/model/resource_xforms_model.xml");
if(is == null) {
  throw new Exception("Could not find xforms model");
}
Document xformsModelDoc = DomHelper.newDocument(is);

String mapType = request.getParameter("mapType");
String version = request.getParameter("version");
boolean xformsContinue = true;
if(version == null || version.trim().length() == 0) {
  out.write("<font color='red'>No version was in the request, do not know what xforms version document to get.</font>");  
  xformsContinue = false;
}
String ivorn = request.getParameter("IVORN");
String instanceType = null;
if(xformsContinue) {   
      NodeList divNodeList = xformsModelDoc.getElementsByTagNameNS("*","div");
      if(divNodeList.getLength() == 0) {
         throw new Exception("Could not find a 'div' element/node in the main resource xforms model document");
      }
      Node divNode = divNodeList.item(0);    

   if(mapType == null || mapType.trim().length() == 0) {
      out.write("<font color='red'>You have entered this area incorrectly with nothing to fill in or recognize what kind of data to fill out, please go back to your main registry page. </font>");
      xformsContinue = false;
   }else if(mapType != null && !mapType.equals("none") && xformsMapping.containsKey(mapType + "_" + version)) {
      String []xforms_models = (String [])xformsMapping.get(mapType  + "_" + version);    
      for(int i = 0;i < xforms_models.length;i++) {
         is = loader.getResourceAsStream("xforms/model/" + xforms_models[i] + "_xforms_model.xml");
         Document xformsModelDocAdd = DomHelper.newDocument(is);        
         divNode.getParentNode().appendChild(xformsModelDoc.importNode(xformsModelDocAdd.getDocumentElement(),true));      
         instanceType = xforms_models[i];
      }//for
   }else if(mapType != null && mapType.equals("default")) {
     //do nothing it is the default resource.   
   }else {
      out.write("<font color='red'>You have entered this area incorrectly with nothing to fill in or recognize what kind of data to fill out, please go back to your main registry page. </font>");
      xformsContinue = false;
   }
   Document submitXFormControl = DomHelper.newDocument("<xforms:submit xmlns:xforms=\"http://www.w3.org/2002/xforms\" submission=\"s00\">" + 
                                 "<xforms:label>Register/Submit</xforms:label>" + 
                                 "</xforms:submit>");
   divNode.getParentNode().appendChild(xformsModelDoc.importNode(submitXFormControl.getDocumentElement(),true));
}//if

if(xformsContinue) {
      NodeList instanceNodeList = xformsModelDoc.getElementsByTagNameNS("*","instance");
      if(instanceNodeList.getLength() == 0) {
         throw new Exception("Could not find a 'instance' element/node in the main resource xforms model document");
      }
      Node instanceNode = instanceNodeList.item(0);
      Document instanceDoc = null;
      if(instanceType == null) 
        instanceType = "resource";
      
      //Okay get both documents and do a merge with Stax.
      if(ivorn != null && ivorn.trim().length() > 0) {
         RegistryQueryService rqs = new RegistryQueryService();
			instanceDoc = rqs.getResourceByIdentifier(ivorn,version);
			NodeList nlResources = instanceDoc.getElementsByTagNameNS("*","Resource");
			if(nlResources.getLength() == 0) {
				throw new Exception("Could not find the document to be an instance of ivorn = " + ivorn);
			}
		   instanceNode.appendChild(xformsModelDoc.importNode(nlResources.item(0),true));			
      }else {
         is = loader.getResourceAsStream("xforms/instance/" + instanceType + "_xforms_instance.xml");
         instanceDoc = DomHelper.newDocument(is);
         instanceNode.appendChild(xformsModelDoc.importNode(instanceDoc.getDocumentElement(),true));
      }
 
}

if(xformsContinue) {
   //File dir = (File) getServletContext().getAttribute("javax.servlet.context.tempdir");
   File dir = new File(getServletContext().getRealPath(".") + "/xforms_tmp");
   dir.mkdir();
   dir.deleteOnExit();
   
   //out.write("<!-- the dir.toString() = " + dir.toString() + " --> ");
   File f = File.createTempFile("xforms_", ".html", dir);
   FileOutputStream fout = new FileOutputStream(f);
   fout.write(DomHelper.DocumentToString(xformsModelDoc).getBytes());
   fout.flush();
   fout.close();
  // out.write("finished with writing file to file:" + f.toString());
   f.deleteOnExit();
   
   out.write(
   "<object classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\" " +
      "codebase=\"http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0\" " +
      "width=\"100%\" height=\"100%\" id=\"deng\" align=\"\"> " + 
      "<param name=\"movie\" value=\"deng_test.swf\"><param name=\"loop\" value=\"false\"> " +
      "<param name=\"menu\" value=\"true\"><param name=\"quality\" value=\"high\"> " +
      "<param name=\"scale\" value=\"noScale\"> " +
      "<param name=\"salign\" value=\"LT\"><param name=\"bgcolor\" value=\"#ffffff\"> " +
      "<param name=\"FlashVars\" value=\"filename=../xforms_tmp/" + f.getName() + "&debug=0\"> " +
   "<EMBED src=\"deng_test.swf\" loop=\"false\" menu=\"true\" quality=\"high\"  scale=\"noScale\" " +
      "salign=\"LT\" bgcolor=\"#ffffff\" width=\"100%\" height=\"100%\" name=\"deng\" align=\"\" " + 
      "FlashVars=\"filename=../xforms_tmp/" + f.getName() + "&debug=0\" " + 
      "type=\"application/x-shockwave-flash\" pluginspace=\"http://www.macromedia.com/go/getflashplayer\"> " +
   "</EMBED></object>");   
}
%>
</div>
<body>
</html>