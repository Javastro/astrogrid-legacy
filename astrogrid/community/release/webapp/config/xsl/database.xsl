<?xml version="1.0"?>
<!--+
    | <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/release/webapp/config/xsl/Attic/database.xsl,v $</cvs:source>
    | <cvs:author>$Author: dave $</cvs:author>
    | <cvs:date>$Date: 2003/12/31 18:18:27 $</cvs:date>
    | <cvs:version>$Revision: 1.2 $</cvs:version>
    | <cvs:log>
    |   $Log: database.xsl,v $
    |   Revision 1.2  2003/12/31 18:18:27  dave
    |   Updated install script and added INSTALL.txt to release webapp
    |
    |   Revision 1.2  2003/12/17 15:16:01  dave
    |   Fixed missing properties in config files
    |
    |   Revision 1.1  2003/12/16 16:44:30  dave
    |   Added deployment test data
    |
    | </cvs:log>
    |
    | XSL transform to update the database config.
    |
    +-->
<xsl:stylesheet
    version="1.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    >
    <!-- Database url from the Ant build -->
    <xsl:param name="url">url</xsl:param>
    <xsl:param name="user">sa</xsl:param>
    <xsl:param name="pass"></xsl:param>

    <!-- Match the named property -->
    <xsl:template match="/database[@name='policy']/driver">
        <xsl:copy>
        <xsl:attribute name="class-name">
            <xsl:text>org.hsqldb.jdbcDriver</xsl:text>
        </xsl:attribute>
        <xsl:attribute name="url">
            <xsl:value-of select="$url"/>
        </xsl:attribute>
        <xsl:element name="param">
            <xsl:attribute name="name">
                <xsl:text>user</xsl:text>
            </xsl:attribute>
            <xsl:attribute name="value">
                <xsl:value-of select="$user"/>
            </xsl:attribute>
        </xsl:element>
        <xsl:element name="param">
            <xsl:attribute name="name">
                <xsl:text>password</xsl:text>
            </xsl:attribute>
            <xsl:attribute name="value">
                <xsl:value-of select="$pass"/>
            </xsl:attribute>
        </xsl:element>
        <!--+
            | TODO - This needs to be updated by the build config
        <mapping href="astrogrid-community-mapping.xml"/>
            +-->
        </xsl:copy>
    </xsl:template>

    <!-- Default, copy all and apply templates -->
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>

