<?xml version="1.0"?>
<!--+
    | <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/install/src/xsl/Attic/context-insert.xsl,v $</cvs:source>
    | <cvs:author>$Author: dave $</cvs:author>
    | <cvs:date>$Date: 2004/02/20 21:11:05 $</cvs:date>
    | <cvs:version>$Revision: 1.2 $</cvs:version>
    | <cvs:log>
    |   $Log: context-insert.xsl,v $
    |   Revision 1.2  2004/02/20 21:11:05  dave
    |   Merged development branch, dave-dev-200402120832, into HEAD
    |
    |   Revision 1.1.2.2  2004/02/16 15:20:54  dave
    |   Changed tabs to spaces
    |
    |   Revision 1.1.2.1  2004/02/15 00:28:22  dave
    |   Added tools for adding separate webapp context file to Tomcat
    |
    | </cvs:log>
    |
    | XSL transform to add a WebApp context to a Tomcat server.xml file.
    | Params :
    |   context.path - the URL base for the context.
    |   context.base - the document base for the context.
    |
    | Notes :
    | You can set the context.base to either your webapp name, or your war file name.
    | In both cases, this will stop Tomcat from automatically unpacking the war file, but for different reasons.
    |
    | 1) If you set the context.base to the war file name, astrogrid-community.war, the Tomcat will just serve the WebApp content
    | directly from the war file, and does not need to unpack it.
    |
    | 2) If you set the context.base to the webapp name, astrogrid-community, then you are declaring a new context with that name.
    | In which case, Tomcat will not unpack the war file because a contect with that name already exists.
    | Setting the context.base to the webapp name does work, but only if the context is added after Tomcat has unpacked the war file.
    |
    +-->
<xsl:stylesheet
    version="1.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    >
    <!--+
        | Params from the Ant build.
        +-->
    <xsl:param name="context.base"/>
    <xsl:param name="context.path"/>

    <!-- Process a 'Host' element that does NOT contain our context -->
    <xsl:template match="/Server/Service/Engine/Host[not(Context/@path = $context.path)]">
        <xsl:copy>
            <!--+
                | Process all of the attributes.
                +-->
            <xsl:apply-templates select="@*"/>
            <!--+
                | Process the rest of the elements.
                +-->
            <xsl:apply-templates/>
            <!--+
                 | Call our template to add a new context.
                 +-->
            <xsl:call-template name="context-insert"/>
        </xsl:copy>
    </xsl:template>

    <!--+
        | Insert our context element.
        +-->
    <xsl:template name="context-insert">
        <xsl:element name="Context">
            <!--+
                | Add the context attributes.
                +-->
            <xsl:attribute name="reloadable">
                <xsl:text>true</xsl:text>
            </xsl:attribute>
            <xsl:attribute name="crossContext">
                <xsl:text>true</xsl:text>
            </xsl:attribute>
            <xsl:attribute name="path">
                <xsl:value-of select="$context.path"/>
            </xsl:attribute>
            <xsl:attribute name="docBase">
                <xsl:value-of select="$context.base"/>
            </xsl:attribute>
        </xsl:element>
    </xsl:template>

    <!--+
        | Default template, copy all and apply templates.
        +-->
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
