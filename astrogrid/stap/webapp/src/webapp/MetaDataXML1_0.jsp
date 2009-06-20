<%@ page import="org.astrogrid.config.SimpleConfig, java.util.*, java.io.*"
   isThreadSafe="false" 
   contentType="text/xml" 
   session="false"
%>


<%
  String regMetaFile = SimpleConfig.getSingleton().getString("regproperties.file.location");
  System.out.println("here is the refMetaFile2 = " + regMetaFile);
  ClassLoader loader = this.getClass().getClassLoader();
  InputStream inputFile = loader.getResourceAsStream(regMetaFile);
  
  if(inputFile == null) {
     System.out.println("original2 input file is null");
     inputFile = new FileInputStream(regMetaFile);
  }
  Properties regProps = new Properties();
  regProps.load(inputFile);

  String ident = regProps.getProperty("reg.metadata.identifier");
  String accessURL = regProps.getProperty("reg.metadata.accessURL");
  String title = regProps.getProperty("reg.metadata.title");
  String shortName = regProps.getProperty("reg.metadata.shortName");  
  String publisher = regProps.getProperty("reg.metadata.publisher");
  String version = regProps.getProperty("reg.metadata.version");  
  String contactName = regProps.getProperty("reg.metadata.contactName.1");
  String contactAddress = regProps.getProperty("reg.metadata.contactAddress.1");  
  String contactEmail = regProps.getProperty("reg.metadata.contactEmail.1");
  String contactTelephone = regProps.getProperty("reg.metadata.contactTelephone.1");  
  String description = regProps.getProperty("reg.metadata.description");  
  String source = regProps.getProperty("reg.metadata.source");
  String referenceURL = regProps.getProperty("reg.metadata.refURL");    
  java.util.Vector dates = new java.util.Vector();
  int i = 1;
    while(regProps.getProperty("reg.metadata.date." + String.valueOf(i)) != null) {
    dates.add((String)(regProps.getProperty("reg.metadata.date." + String.valueOf(i))));
    i++;
  }
  
  java.util.Vector dateRole = new java.util.Vector();
  i = 1;
  while(regProps.getProperty("reg.metadata.date.role." + String.valueOf(i)) != null) {
    dateRole.add((String)(regProps.getProperty("reg.metadata.date.role." + String.valueOf(i))));
    i++;
  }
  
  java.util.Vector contributor = new java.util.Vector();
  i = 1;
  while(regProps.getProperty("reg.metadata.contributor." + String.valueOf(i)) != null) {
    contributor.add((String)(regProps.getProperty("reg.metadata.contributor." + String.valueOf(i))));
    i++;
  }  
  
  java.util.Vector creatorName = new java.util.Vector();
  i = 1;
  while(regProps.getProperty("reg.metadata.creatorName." + String.valueOf(i)) != null) {
    creatorName.add((String)(regProps.getProperty("reg.metadata.creatorName." + String.valueOf(i))));
    i++;
  }

  java.util.Vector creatorLogo = new java.util.Vector();
  i = 1;
  while(regProps.getProperty("reg.metadata.creatorLogo." + String.valueOf(i)) != null) {
    creatorLogo.add((String)(regProps.getProperty("reg.metadata.creatorLogo." + String.valueOf(i))));
    i++;
  }

  
  java.util.Vector subjects = new java.util.Vector();  
  i = 1;
  while(regProps.getProperty("reg.metadata.subject." + String.valueOf(i)) != null) {
    subjects.add((String)(regProps.getProperty("reg.metadata.subject." + String.valueOf(i))));
    i++;
  }
  
  java.util.Vector types = new java.util.Vector();
  i = 1;
  while(regProps.getProperty("reg.metadata.type." + String.valueOf(i)) != null) {
    types.add((String)(regProps.getProperty("reg.metadata.type." + String.valueOf(i))));
    i++;
  }  
  
  java.util.Vector contentLevel = new java.util.Vector();
  i = 1;
  while(regProps.getProperty("reg.metadata.contentLevel." + String.valueOf(i)) != null) {
    contentLevel.add((String)(regProps.getProperty("reg.metadata.contentLevel." + String.valueOf(i))));
    i++;
  }   
  
  java.util.Vector rights = new java.util.Vector();
  i = 1;
  while(regProps.getProperty("reg.metadata.rights." + String.valueOf(i)) != null) {
    rights.add((String)(regProps.getProperty("reg.metadata.rights." + String.valueOf(i))));
    i++;
  }    

  String regBas = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
 
%>
<ri:Resource  xmlns:stap="urn:astrogrid:schema:vo-resource-types:STAP:v1.0" xmlns:vr="http://www.ivoa.net/xml/VOResource/v1.0" xmlns:vs="http://www.ivoa.net/xml/VODataService/v1.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
   xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:ri="http://www.ivoa.net/xml/RegistryInterface/v1.0" xsi:type="vr:Service" status="active"  created="2000-01-01T09:00:00" updated="2000-01-01T09:00:00">          
 <title><%=title%></title>
 <shortName><%=shortName%></shortName>
 <identifier><%=ident%></identifier>
 <curation>
  <publisher><%=publisher%></publisher>
<%for(int j=0;j < creatorName.size();j++)  { %>
  <creator>
    <name><%=creatorName.elementAt(j)%></name>
      <%if(creatorLogo.size() > j) { %>
	    <logo><%=creatorLogo.elementAt(j)%></logo>
	    <%}%>
  </creator>
  <%} System.out.println("just finished creator");
  for(int j = 0;j < contributor.size();j++) {
  %>
  <contributor><%=contributor.elementAt(j)%></contributor>
  <%} System.out.println("just finished contributor");
  for(int j = 0;j < dates.size();j++) {
  %>
  <date<% if(dateRole.size() > j){%> role="<%=dateRole.elementAt(j)%>"<%}%>><%=dates.elementAt(j)%></date>
  <% } %>
  <%if(version != null && version.trim().length() > 0) { %>
    <version><%=version%></version>
  <%}%>
  <contact>
   <name><%= contactName %></name>
   <%if(contactAddress != null  && contactAddress.trim().length() > 0) { %>
   <address><%=contactAddress%></address>
   <%}
   if(contactEmail != null && contactEmail.trim().length() > 0) {
   %>
   <email><%= contactEmail%></email>
   <%}
   if(contactTelephone != null && contactTelephone.trim().length() > 0) {
   %>
   <telephone></telephone>
   <%}%>
  </contact>
 </curation>
 <content>
  <%for(int j = 0;j < subjects.size();j++) {%>
	  <subject><%=subjects.elementAt(j)%></subject>
  <%}%>
  <description><%=description%></description>
  <%if(source != null && source.trim().length() > 0) {%>
	  <source><%=source%></source>
  <%}%>
  <%if(referenceURL != null && referenceURL.trim().length() > 0) {%>
  <referenceURL><%=referenceURL%></referenceURL>
  <%} 
  for(int j = 0;j < types.size();j++) {
  %>
  <type><%=types.elementAt(j)%></type>
  <%}
  	for(int j = 0;j < contentLevel.size();j++) {
  %>
  	<contentLevel><%=contentLevel.elementAt(j)%></contentLevel>
  <%}%>  
   </content>
 <%for(int j = 0;j < rights.size();j++) {
 %>
   <rights><%=rights.elementAt(j)%></rights>
 <%}%>
 <capability standardID="ivo://org.astrogrid/stap/v1.0" xsi:type="stap:SimpleTimeAccess">
    <description>Stap capability</description>
    <interface xsi:type="vs:ParamHTTP" version="1.0">
		<accessURL use="full"><%=accessURL%></accessURL>
		<resultType>text/xml</resultType>
		<!--
		  Security Method area there can be many of these.
		  Are they needed?
		  Docs1: the mechanism the client must employ to gain secure
                  access to the service.  
          Docs2: when more than one method is listed, each one must
                  be employed to gain access.  
		  <securityMethod standardID="ivo://standardid" />
		-->
    </interface> 
   <supportPositioning>false</supportPositioning>
  </capability>
</ri:Resource>