<?xml version="1.0" encoding="UTF-8"?>
<jsp:root
	xmlns:jsp="http://java.sun.com/JSP/Page"
	version="1.2"
	>
	<jsp:directive.page contentType="text/plain"/>
	<!--+
	    |
	    | <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/registry.jsp,v $</cvs:source>
	    | <cvs:author>$Author: dave $</cvs:author>
	    | <cvs:date>$Date: 2004/07/27 13:41:17 $</cvs:date>
	    | <cvs:version>$Revision: 1.2 $</cvs:version>
	    | <cvs:log>
	    |   $Log: registry.jsp,v $
	    |   Revision 1.2  2004/07/27 13:41:17  dave
	    |   Merged development branch, dave-dev-200407261230, into HEAD
	    |
	    |   Revision 1.1.2.1  2004/07/27 13:27:07  dave
	    |   Added registry entry and resolver to integtation tests
	    |
	    |   Revision 1.4  2004/06/18 13:45:20  dave
	    |   Merged development branch, dave-dev-200406081614, into HEAD
	    |
	    |   Revision 1.3.32.1  2004/06/17 13:38:59  dave
	    |   Tidied up old CVS log entries
	    |
	    | </cvs:log>
	    |
	    +-->
	<VODescription
	  xmlns:c="http://java.sun.com/jsp/jstl/core"
	  xmlns="http://www.ivoa.net/xml/VOResource/v0.9"
	  xmlns:vr="http://www.ivoa.net/xml/VOResource/v0.9"
	  xmlns:vc="http://www.ivoa.net/xml/VOCommunity/v0.2"
	  xmlns:vg="http://www.ivoa.net/xml/VORegistry/v0.2"
	  xmlns:vs="http://www.ivoa.net/xml/VODataService/v0.4"
	  xmlns:vt="http://www.ivoa.net/xml/VOTable/v0.1"
	  xmlns:cs="http://www.ivoa.net/xml/ConeSearch/v0.2"
	  xmlns:sia="http://www.ivoa.net/xml/SIA/v0.6"
	  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	  xsi:schemaLocation="http://www.ivoa.net/xml/VOResource/v0.9
	                      http://www.ivoa.net/xml/VOResource/VOResource-v0.9.xsd
	                      http://www.ivoa.net/xml/VOCommunity/v0.2
	                      http://www.ivoa.net/xml/VOCommunity/VOCommunity-v0.2.xsd
	                      http://www.ivoa.net/xml/VORegistry/v0.2
	                      http://www.ivoa.net/xml/VORegistry/VORegistry-v0.2.xsd
	                      http://www.ivoa.net/xml/ConeSearch/v0.2
	                      http://www.ivoa.net/xml/ConeSearch/ConeSearch-v0.2.xsd
	                      http://www.ivoa.net/xml/SIA/v0.6
	                      http://www.ivoa.net/xml/SIA/SIA-v0.6.xsd">
	 <vr:Resource xsi:type="ServiceType">
	    <Identifier>
	      <AuthorityID>community ident</AuthorityID>
	      <ResourceKey>service class</ResourceKey>
	    </Identifier>
	    <Title>Test service</Title>
	    <ShortName>Test service</ShortName>
	    <Summary>
	      <Description/>
	      <ReferenceURL>http://www.astrogrid.org</ReferenceURL>
	    </Summary>
	    <Type>Archive</Type>
	    <Curation>
	      <Publisher>
	         <Title>Test service</Title>
	      </Publisher>
	      <Contact>
	        <Name>Test contact</Name>
	          <Email>test@astrogrid.org</Email>
	      </Contact>
	      <Date>2004-02-02</Date>
	    </Curation>
	    <Subject>Test service</Subject>
	    <Interface>
	      <Invocation>WebService</Invocation>
			<AccessURL use="base">

				<jsp:expression>request.getScheme()</jsp:expression>://<jsp:expression>request.getServerName()</jsp:expression>:<jsp:expression>request.getServerPort()</jsp:expression><jsp:expression>request.getContextPath()</jsp:expression>/services/FileStore

			</AccessURL>
	    </Interface>
	  </vr:Resource>
	</VODescription>
</jsp:root>
