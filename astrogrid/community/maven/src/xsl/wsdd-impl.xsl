<?xml version="1.0"?>
<!--+
    | <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/maven/src/xsl/wsdd-impl.xsl,v $</cvs:source>
    | <cvs:author>$Author: clq2 $</cvs:author>
    | <cvs:date>$Date: 2005/08/04 09:40:11 $</cvs:date>
    | <cvs:version>$Revision: 1.4 $</cvs:version>
    | <cvs:log>
    |   $Log: wsdd-impl.xsl,v $
    |   Revision 1.4  2005/08/04 09:40:11  clq2
    |   kevin's second batch
    |
    |   Revision 1.3.182.1  2005/07/28 13:35:53  KevinBenson
    |   No longer uses resetDatabaseTables as a web service method it is not allowed anymore.
    |
    |   Revision 1.3  2004/06/18 13:45:20  dave
    |   Merged development branch, dave-dev-200406081614, into HEAD
    |
    |   Revision 1.2.54.1  2004/06/17 13:38:59  dave
    |   Tidied up old CVS log entries
    |
    | </cvs:log>
    | 
    +-->
<xsl:stylesheet
    version="1.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:wsdd="http://xml.apache.org/axis/wsdd/">

    <!-- Implementation class from the Ant build -->
    <xsl:param name="classname"></xsl:param>
    <xsl:param name="allowedMethods"></xsl:param>
    <xsl:param name="service">target service name</xsl:param>

    <!-- Match the service classname parameter -->
    <xsl:template match="wsdd:deployment/wsdd:service[@name=$service]/wsdd:parameter[@name='className']">

            <xsl:choose>
               <xsl:when test="$classname != ''">
	    		    <xsl:copy>
		    	        <xsl:attribute name="name">
        	    		    <xsl:text>className</xsl:text>
        		    	</xsl:attribute>
			            <xsl:attribute name="value">
    	        		    <xsl:value-of select="$classname"/>
        			    </xsl:attribute>
			        </xsl:copy>
               </xsl:when>
               <xsl:otherwise>
			        <xsl:copy>
    	    		    <xsl:apply-templates select="@*|node()"/>
			        </xsl:copy>
               </xsl:otherwise>
          </xsl:choose>
    </xsl:template>

    <xsl:template match="wsdd:deployment/wsdd:service[@name=$service]/wsdd:parameter[@name='allowedMethods']">
            <xsl:choose>
               <xsl:when test="$allowedMethods != ''">
		        	<xsl:copy>
	    	        	<xsl:attribute name="name">
        	        		<xsl:text>allowedMethods</xsl:text>
            			</xsl:attribute>
			            <xsl:attribute name="value">
	    	        	    <xsl:value-of select="$allowedMethods"/>
    			        </xsl:attribute>
	    	    	</xsl:copy>
               </xsl:when>
               <xsl:otherwise>
		        <xsl:copy>
        		    <xsl:apply-templates select="@*|node()"/>
		        </xsl:copy>
               </xsl:otherwise>
          </xsl:choose>
    </xsl:template>

    <!-- Default, copy all and apply templates -->
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
