<?xml version="1.0" encoding="ISO-8859-1"?>

<!-- CVS $Id: welcome.xslt,v 1.7 2003/05/08 00:05:04 vgritsenko Exp $ -->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:param name="contextPath" select="'/cocoon'"/>

  <xsl:template match="welcome">
    <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
      <head>
        <title>Welcome to Apache Cocoon!</title>
        <!-- 
             NOTE (SM): this meta tag reflects the *output* of the pipeline and not
             the encoding of this file. I agree it's sort of an hack and it should
             be the XHTML serializer to add the meta tag to the response, but, for
             now, this fixes encoding problems in those user-agents that don't parse
             the <?xml?> processing instruction to understand the encoding of the
             stream 
         --> 
        <meta http-equiv="Content-Type" content="text/xhtml; charset=UTF-8"/>
        <link href="{$contextPath}/styles/main.css" type="text/css" rel="stylesheet"/>
        <link href="favicon.ico" rel="SHORTCUT ICON" />
      </head>
      <body>
        <h1>Welcome to Apache Cocoon!</h1>
        <xsl:apply-templates/>
        <p class="copyright">
         Copyright � 1999-2003 <a href="http://www.apache.org/">The Apache Software Foundation</a>. All rights reserved.
        </p>
        <p class="block">
          <a href="http://cocoon.apache.org/"><img src="{$contextPath}/images/powered.gif" alt="Powered by Apache Cocoon"/></a>
        </p>
      </body>
    </html>
  </xsl:template>

  <xsl:template match="message">
    <p class="block"><xsl:apply-templates/></p>
  </xsl:template>
  
  <xsl:template match="link">
    <a href="{@href}"><xsl:apply-templates/></a>
  </xsl:template>

</xsl:stylesheet>
