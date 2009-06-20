<%@ page import="org.astrogrid.config.SimpleConfig, java.util.*, java.io.*"
   isThreadSafe="false" 
   contentType="text/xml" 
   session="false"
%>

<%

  String regMetaFile = SimpleConfig.getSingleton().getString("regproperties.file.location","regmetadata.properties");
  ClassLoader loader = this.getClass().getClassLoader();
  InputStream inputFile = loader.getResourceAsStream(regMetaFile);
  if(inputFile == null) {
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
%>
<vor:VOResources xmlns:vor="http://www.ivoa.net/xml/RegistryInterface/v0.1"
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
   xmlns:xs="http://www.w3.org/2001/XMLSchema" 
   xmlns:vr="http://www.ivoa.net/xml/VOResource/v0.10" 
   xmlns:vs="http://www.ivoa.net/xml/VODataService/v0.5" 
   xmlns:vg="http://www.ivoa.net/xml/VORegistry/v0.3" xmlns="http://www.ivoa.net/xml/VOResource/v0.10">
<vor:Resource xmlns:sta="urn:astrogrid:schema:vo-resource-types:SimpleTimeAccess:v0.1" xsi:type="sta:SimpleTimeAccess"  updated="2007-06-22" status="active" xsi:schemaLocation="urn:astrogrid:schema:vo-resource-types:SimpleTimeAccess:v0.1 http://msslxt.mssl.ucl.ac.uk:8080/misc_stuff/STAP.xsd">
 <title><%=title%></title>
 <shortName><%=shortName%></shortName>
 <identifier><%=ident%></identifier>
 <curation>
  <publisher><%=publisher%></publisher>
  <%if(creatorName.size() > 0) { %>
  <creator>
    <name><%=creatorName.elementAt(0)%></name>
      <%if(creatorLogo.size() > 0) { %>
	    <logo><%=creatorLogo.elementAt(0)%></logo>
	    <%}%>
  </creator>
  <%}
  for(int j = 0;j < contributor.size();j++) {
  %>
  <contributor><%=contributor.elementAt(j)%></contributor>
  <%} 
  for(int j = 0;j < dates.size();j++) {
  %>
  <date<% if(dateRole.size() > j){%> role="<%=dateRole.elementAt(j)%><%}%>"><%=dates.elementAt(j)%></date>
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
    <interface xsi:type="vs:ParamHTTP" qtype="GET">
		<accessURL use="full"><%=accessURL%></accessURL>
		<vs:resultType>text/xml</vs:resultType>
    </interface> 
</vor:Resource>
</vor:VOResources>