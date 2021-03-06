<?xml version="1.0"?>
<!--+
    | <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portal/login/src/ant/xsl/webapp.xsl,v $</cvs:source>
    | <cvs:author>$Author: dave $</cvs:author>
    | <cvs:date>$Date: 2004/01/09 03:16:18 $</cvs:date>
    | <cvs:version>$Revision: 1.1 $</cvs:version>
    | <cvs:log>
    |   $Log: webapp.xsl,v $
    |   Revision 1.1  2004/01/09 03:16:18  dave
    |   Initial commit for login action
    |
    |   Revision 1.2  2003/12/31 18:18:27  dave
    |   Updated install script and added INSTALL.txt to release webapp
    |
    |   Revision 1.4  2003/12/16 12:46:00  dave
    |   Fixed the web.xml dtd
    |
    |   Revision 1.3  2003/11/06 15:35:26  dave
    |   Replaced tabs with spaces
    |
    |   Revision 1.2  2003/09/17 19:47:21  dave
    |   1) Fixed classnotfound problems in the build.
    |   2) Added the JUnit task to add the initial accounts and groups.
    |   3) Got the build to work together with the portal.
    |   4) Fixed some bugs in the Account handling.
    |
    |   Revision 1.1  2003/09/13 02:18:52  dave
    |   Extended the jConfig configuration code.
    |
    | </cvs:log>
    |
    +-->
<xsl:stylesheet
    version="1.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    >
    <!-- Set the output document type -->
    <!-- It looks like Sun have changed the location of the DTD (this used to resolve ok, but started to fail).
    <xsl:output
        doctype-system="http://java.sun.com/j2ee/dtds/web-app_2.2.dtd"
        doctype-public="-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN"
        />
    -->
    <xsl:output
        doctype-system="http://java.sun.com/j2ee/dtds/web-app_2_2.dtd"
        doctype-public="-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN"
        />

    <!-- Config path from the Ant build -->
    <xsl:param name="property.name">property name</xsl:param>
    <xsl:param name="property.value">property.value</xsl:param>

    <!-- Match a web-app element that already contains our JNDI property -->
    <xsl:template match="/web-app[env-entry/env-entry-name/text() = $property.name]">
        <xsl:copy>
            <!-- Process all of the elements, including our config-property -->
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

    <!-- Match a web-app element that does not contain our JNDI property -->
    <xsl:template match="/web-app[not(env-entry/env-entry-name/text() = $property.name)]">
        <xsl:copy>
            <!-- Process the rest of the elements -->
            <xsl:apply-templates/>
            <!-- Call our config-property template -->
            <xsl:call-template name="config-property"/>
        </xsl:copy>
    </xsl:template>

    <!-- Match our JNDI property -->
    <xsl:template name="config-property" match="/web-app/env-entry[env-entry-name/text() = $property.name]">
        <env-entry>
            <xsl:comment>JNDI property for community config</xsl:comment>
            <env-entry-name>
                <xsl:value-of select="$property.name"/>
            </env-entry-name>
            <env-entry-value>
                <xsl:value-of select="$property.value"/>
            </env-entry-value>
            <env-entry-type>
                <xsl:text>java.lang.String</xsl:text>
            </env-entry-type>
        </env-entry>
    </xsl:template>

    <!-- Default, copy all and apply templates -->
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
