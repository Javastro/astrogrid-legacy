<?xml version="1.0"?>
<!--+
    | <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/tomcat/src/xsl/tomcat-users.xsl,v $</cvs:source>
    | <cvs:author>$Author: dave $</cvs:author>
    | <cvs:date>$Date: 2004/02/20 21:11:05 $</cvs:date>
    | <cvs:version>$Revision: 1.2 $</cvs:version>
    | <cvs:log>
    |   $Log: tomcat-users.xsl,v $
    |   Revision 1.2  2004/02/20 21:11:05  dave
    |   Merged development branch, dave-dev-200402120832, into HEAD
    |
    |   Revision 1.1.2.2  2004/02/16 15:20:54  dave
    |   Changed tabs to spaces
    |
    |   Revision 1.1.2.1  2004/02/14 22:24:09  dave
    |   Test toolkit for the install and tomcat sub-projects
    |
    | </cvs:log>
    |
    +-->
<xsl:stylesheet
    version="1.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    >
    <!--+
        | Name, password and roles from our Ant script.
        +-->
    <xsl:param name="account.name"/>
    <xsl:param name="account.pass"/>
    <xsl:param name="account.role"/>

    <!--+
        | Process a tomcat-users element that already contains our account.
        +-->
    <xsl:template match="/tomcat-users[user/@name = $account.name]">
        <xsl:copy>
            <!--+
                | Process all the elements, including ours.
                +-->
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

    <!--+
        | Process a tomcat-users element that does NOT contain our account.
        +-->
    <xsl:template match="/tomcat-users[not(user/@name = $account.name)]">
        <xsl:copy>
            <!--+
                | Process the rest of the elements.
                +-->
            <xsl:apply-templates/>
            <!--+
                | Call our template at the end.
                +-->
            <xsl:comment> New account added </xsl:comment>
            <xsl:call-template name="config-account"/>
        </xsl:copy>
    </xsl:template>

    <!--+
        | Process a user element that matches our account.
        +-->
    <xsl:template name="config-account" match="user[@name = $account.name]">
        <xsl:element name="user">
            <xsl:attribute name="name">
                <xsl:value-of select="$account.name"/>
            </xsl:attribute>
            <xsl:attribute name="password">
                <xsl:value-of select="$account.pass"/>
            </xsl:attribute>
            <xsl:attribute name="roles">
                <xsl:value-of select="$account.role"/>
            </xsl:attribute>
        </xsl:element>
    </xsl:template>

    <!--+
        | Default, copy all and apply templates.
        +-->
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
