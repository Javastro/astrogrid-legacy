<?xml version="1.0"?>
<!-- $id$
   a stylesheet to create C from the jel description...note this is using xslt 2.0 features. -->
   
 <!-- issues
 for derived structs that can be returned
   1. declare as return the pointer to the base struct.
   2 listOf
   
   ListOf - may be a list of base type
   
  -->
<xsl:stylesheet version="2.0"
   xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
xmlns:xs="http://www.w3.org/2001/XMLSchema"
   xmlns:lfn="urn:acrclocalfunctions">
   <xsl:preserve-space elements="*" />
   <xsl:output method="text" />
   <!-- this is just to get a default root context node for the document in for-each loops.... -->
   <xsl:variable name="jel" select="/jel"></xsl:variable>
   <xsl:variable name="internalPackages"
      select="('org.astrogrid.acr', 'org.astrogrid.acr.opt', 'org.astrogrid.acr.builtin', 'org.votech.plastic')" />
   <xsl:variable name="javaSimpleTypes"
      select="('String', 'float' ,'int', 'double' ,'void' , 'boolean', 'byte', 'long')" />
      <!-- types that are dealt with by static declarations because they are so common -->
   <xsl:variable name="javaStdTypes" select="('Object', 'Map','Calendar','URI','Date', 'URL', 'Document', $javaSimpleTypes)" />
   <xsl:variable name="typeMap">
      <map>
       <t from="Map" to="ACRKeyValueMap" /><!-- TODO - check if this has things other than strings in it -->
       <t from="List" to="ACRList" /><!-- TODO - check if this has things other than strings in it -->
       <t from="String" to="JString" /> <!-- make all floats double -->
       <t from="URL" to="URLString" />
       <t from="Document" to="XMLString" />
       <t from="URI" to="IvornOrURI" />
       <t from="boolean" to="BOOL" />
       <t from="float" to="double" />
       <t from="Calendar" to="time_t" /><!-- use the time type from <time.h> -->
       <t from="Date" to="time_t" />
      </map>
   </xsl:variable>
   <xsl:variable name="stdInterfaces" select="('Serializable', 'Comparator', 'InvocationHandler')" />
   <!-- compute a list of package names - will reuse this to iterate through later.. -->
   <xsl:variable name="packages"
      select="/jel/jelclass[ not(@package=following::jelclass/@package) ]/@package[not (.= $internalPackages)]" />
   <!-- list of service interfaces -->
   <xsl:variable name="services"
      select="/jel/jelclass[@interface='true' and comment/attribute[@name='@service'] and not (@package = $internalPackages) ]" />
   <!-- list of bean classes - various heuristics should capture most
      - any class ending in 'Information' or 'Bean'
      - any class in package 'org.astrogrid.acr.ivoa.resource', 
      - or which extends or implements a class in that package
      , bean classes that don't match these rules need to be tagged with '@bean'-->
   <xsl:variable name="beans"
      select="/jel/jelclass[contains(@type,'Information') or contains(@type,'Bean') or comment/attribute[@name='@bean'] or contains(@fulltype,'org.astrogrid.acr.ivoa.resource') or contains(implements/interface/@fulltype,'org.astrogrid.acr.ivoa.resource') or contains(@superclassfulltype,'org.astrogrid.acr.ivoa.resource') and not (@package = $internalPackages) ]" />
   <xsl:variable name="hier">
     <xsl:call-template name="classHierarchy"></xsl:call-template>
   </xsl:variable>
   <xsl:template match="jel">
   <xsl:message>packages <xsl:value-of select="$packages"/></xsl:message>
      <xsl:text>
#ifndef INTF_H_
#define INTF_H_
</xsl:text>
<xsl:call-template name="header"></xsl:call-template>
<xsl:text>
      
#include "core/acrtypes.h"
#ifdef __cplusplus
 extern "C" {
#endif

</xsl:text>
<xsl:text>
/* enumeration of the types available */
 enum AcrType {
</xsl:text>
      <xsl:variable name="beanNameList">
         <xsl:value-of select="$beans/@type" separator=","></xsl:value-of>
      </xsl:variable>
      <xsl:value-of select="translate($beanNameList,'.','_')"></xsl:value-of>
      <xsl:text> };

/* type tree */
</xsl:text>


      <!-- List Types as strongly typed strucures. -->
      <xsl:text>
/* list types */
   </xsl:text>
      <xsl:for-each-group
         select="/jel//jelclass[not (@package= $internalPackages)]//(field|method|param)[contains(@fulltype,'[]') and not(@type = $javaStdTypes)]"
         group-by="@fulltype">
         <xsl:sort select="@type" />
         <xsl:call-template name="listof">
            <xsl:with-param name="t" select="@type"></xsl:with-param>
         </xsl:call-template>
         <xsl:text>
</xsl:text>
      </xsl:for-each-group>
      <!-- have a go at deciding what are derived types. -->
      <xsl:text>
/*
structures - map onto the java beans
*/ 
</xsl:text>
      <!-- declare the beans in the correct order. -->
      

      <xsl:variable name="deptree">
         <here>
            <xsl:for-each select="$beans">
               <xsl:sort select="@type"></xsl:sort>
               <xsl:call-template name="depend">
                  <xsl:with-param name="t" select="."></xsl:with-param>
               </xsl:call-template>
            </xsl:for-each>
         </here>
      </xsl:variable>
      <!--   <xsl:copy-of select="$deptree"></xsl:copy-of> 
      -->
      <xsl:apply-templates select="$deptree/here/*" mode="order" />
 

      <xsl:text>
/*
Unions for derived types - functions can return derived types

*/
</xsl:text>
    <xsl:for-each select="$hier/hierarchy/*[count(child::*)>0]">
      <xsl:call-template  name="derivedUnion">
          <xsl:with-param name="t" select="name(.)"></xsl:with-param>
      </xsl:call-template>
    </xsl:for-each>
     
      <!-- generate package descriptions -->
      <xsl:text>

    
/*

functions

*/
</xsl:text>
      <xsl:for-each select="$packages">
         <xsl:sort />
         <xsl:variable name="curr" select="." />
         <xsl:variable name="packageName">
            <xsl:call-template name="substring-after-last">
               <xsl:with-param name="input" select="." />
               <xsl:with-param name="marker" select="'.'" />
            </xsl:call-template>
         </xsl:variable>
         <xsl:text>/* begin package </xsl:text>
         <xsl:value-of select="$packageName" />
         <xsl:text> */
           
</xsl:text>
         <xsl:for-each select="$services[@package=$curr]">
            <xsl:sort select="@type" />
            <xsl:call-template name="funcwriter"><xsl:with-param name="t" select="."></xsl:with-param></xsl:call-template>
         </xsl:for-each>
         <xsl:text>
/* end package </xsl:text>
         <xsl:value-of select="$packageName" />
         <xsl:text> */

</xsl:text>
      </xsl:for-each>
<xsl:text>
#ifdef __cplusplus
 }
#endif
#endif
</xsl:text>      
<!-- end of the header file definition --> 
         <xsl:result-document href="intf.cpp">
         <xsl:call-template name="header"/>
         <xsl:text>
#include "AR.h"
#include "intf.h"
#include "intfclasses.h"

         
         </xsl:text>
          <xsl:for-each select="$services">
            <xsl:sort select="@type" />
            <xsl:call-template name="funcwriter">
            <xsl:with-param name="t" select="."/>
            <xsl:with-param name="body" select="'t'"></xsl:with-param>
            </xsl:call-template>
         </xsl:for-each>
        
         </xsl:result-document>
        <xsl:result-document href="intfclasses.h">
           <xsl:text>
#ifndef INTFCLASSES_H_
#define INTFCLASSES_H_

           </xsl:text>
           <xsl:call-template name="header"></xsl:call-template>
           <xsl:message>
            <xsl:copy-of select="$hier" />
           </xsl:message>
           
      <xsl:variable name="deptreeclass">
         <here>
            <xsl:for-each select="$beans">
               <xsl:sort select="@type"></xsl:sort>
               <xsl:call-template name="dependClass">
                  <xsl:with-param name="t" select="."></xsl:with-param>
               </xsl:call-template>
            </xsl:for-each>
         </here>
      </xsl:variable>
           
       <xsl:apply-templates select="$deptreeclass/here/*" mode="beandef" />
       <xsl:text>
#endif
        </xsl:text>
        </xsl:result-document> 
      
   </xsl:template>
   
 <!-- end of main template -->
 
 
   <xsl:template name="dobean">
      <xsl:param name="b"></xsl:param>
      <xsl:text>
struct  </xsl:text>
      <xsl:value-of select="translate($b/@type,'.','_') " />
      <xsl:text> {
       /*
        from 
</xsl:text>
      <xsl:value-of select="$b/@fulltype" />
      <xsl:text>
</xsl:text>
      <xsl:if test="$b/comment/attribute[@name='@deprecated']">
         <xsl:text>!!Deprecated: </xsl:text>
         <xsl:value-of
            select="$b/comment/attribute[@name='@deprecated']" />
      </xsl:if>
      <xsl:value-of select="$b/comment/description"
         disable-output-escaping="yes" />
      <xsl:text>
   */
    enum AcrType _type;
</xsl:text>

      <xsl:call-template name="recursiveDataMembers">
         <xsl:with-param name="t" select="$b" />
      </xsl:call-template>
      <xsl:text>
};
</xsl:text>
   </xsl:template>
   <xsl:template name="funcwriter">
      <xsl:param name="t" />
      <xsl:param name="body"/>
      <xsl:variable name="service-name"
         select="$t/comment/attribute[@name='@service']/description" />
      <xsl:text>/* begin class </xsl:text>
      <xsl:value-of select="$service-name" />
      <xsl:text>
    </xsl:text>
      <xsl:if test="$t/comment/attribute[@name='@deprecated']">
         <xsl:text>Deprecated: </xsl:text>
         <xsl:value-of select="$t/comment/attribute[@name='@deprecated']" />
      </xsl:if>
      <xsl:value-of select="$t/comment/description"
         disable-output-escaping="yes" />
      <xsl:if test="$t/comment/attribute[@name='@see']">
         <xsl:text>See: </xsl:text>
         <xsl:for-each select="$t/comment/attribute[@name='@see']">
            <xsl:value-of select="." disable-output-escaping="yes" />
            <xsl:text> </xsl:text>
         </xsl:for-each>
      </xsl:if>
      <xsl:text> */
	 
	</xsl:text>
      <xsl:variable name="inheritanceTypes"
         select="$t/implements/interface/@fulltype" />
      <xsl:variable name="inheritanceStack"
         select="$t | /jel/jelclass[@fulltype =$inheritanceTypes]" />
         <!-- note that callFunction is being explicitly excluded -->
      <xsl:for-each
         select="$inheritanceStack/methods/method[@visibility='public' and not ( comment/attribute[@name='@deprecated'] or contains(@name, 'Listener') or @name='callFunction') ]">
         <xsl:text>
			
			
/* function </xsl:text>
         <xsl:variable name="thefunction">
            <xsl:value-of select="translate($service-name,'.','_')" />
            <xsl:text>_</xsl:text>
            <xsl:value-of select="@name" />
         </xsl:variable>
         <xsl:value-of select="$thefunction" />
         <xsl:choose>
            <xsl:when test="params/param">
               <xsl:text>(</xsl:text>
               <xsl:for-each select="params/param">
                  <xsl:value-of select="translate(@name,'.','_')" />
                  <xsl:if test="position() != last()">
                     <xsl:text>, </xsl:text>
                  </xsl:if>
               </xsl:for-each>
               <xsl:text>)</xsl:text>
            </xsl:when>
            <xsl:otherwise>
               <xsl:text>()</xsl:text>
            </xsl:otherwise>
         </xsl:choose>
         <xsl:if test="comment/attribute[@name='@deprecated']">
            Deprecated:
            <xsl:value-of
               select="comment/attribute[@name='@deprecated']" />
         </xsl:if>
         <xsl:value-of select="comment/description"
            disable-output-escaping="yes" />
         <xsl:text>
		
		</xsl:text>
         <xsl:for-each select="params/param">
            <xsl:value-of select="translate(@name,'.','_')" />
            <xsl:text> - </xsl:text>
            <xsl:value-of select="@comment"
               disable-output-escaping="yes" />
            <xsl:text>(</xsl:text>
            <xsl:call-template name="convert-type">
               <xsl:with-param name="p" select="." />
               <xsl:with-param name="f" select="'t'" />              
            </xsl:call-template>
            <xsl:text>)</xsl:text>
            <xsl:text>
		</xsl:text>
         </xsl:for-each>
         <xsl:text>
	Returns </xsl:text>
         <xsl:call-template name="convert-type">
            <xsl:with-param name="p" select="." />
                       
         </xsl:call-template>
         <xsl:text> - </xsl:text>
         <xsl:value-of select="@returncomment"
            disable-output-escaping="yes" />
         <xsl:text>
       */
</xsl:text>
         <!-- now do the actual declaration -->
         <xsl:call-template name="convert-type">
            <xsl:with-param name="p" select="." />
                          
         </xsl:call-template>
         <xsl:text> </xsl:text>
         <xsl:value-of select="$thefunction" />
         <xsl:text> ( </xsl:text>
         <xsl:for-each select="params/param">
            <xsl:call-template name="convert-type">
               <xsl:with-param name="p" select="." />
                <xsl:with-param name="f" select="'t'" />              
            </xsl:call-template>
            <xsl:if test="$body">
               <xsl:text> </xsl:text>
               <xsl:value-of select="translate(@name,'.','_')"></xsl:value-of>
            </xsl:if>
            <xsl:if test="position() != last()">
               <xsl:text>, </xsl:text>
            </xsl:if>
         </xsl:for-each>
         <xsl:text>)</xsl:text>
         
         <xsl:choose>
           <xsl:when test="$body">
              <xsl:call-template name="funcbody"> <xsl:with-param name="f" select="."></xsl:with-param>
              <xsl:with-param name="service" select="$service-name"></xsl:with-param></xsl:call-template>
           </xsl:when>
           <xsl:otherwise>
                   <xsl:text>;</xsl:text>
 
           </xsl:otherwise>
         </xsl:choose>
         
       </xsl:for-each>
      /* end class
      <xsl:value-of select="$service-name" />
      */
   </xsl:template>
   <xsl:template name="funcbody">
   <xsl:param name="service"></xsl:param>
   <xsl:param name="f"></xsl:param>
   <xsl:text>
   {
     XmlRpcValue _args, _result;
   </xsl:text>
   <xsl:if test="$f/@type != 'void'">
   
   <xsl:call-template name="convert-type"><xsl:with-param name="p" select="$f"></xsl:with-param></xsl:call-template><xsl:text> retval;
   </xsl:text>
   </xsl:if>
   <xsl:for-each select="$f/params/param">
   <xsl:text>_args[</xsl:text><xsl:value-of select="position()-1"/><xsl:text>] = </xsl:text><xsl:value-of select="@name"/><xsl:text>;
   </xsl:text>
   </xsl:for-each>
   <xsl:text>
     if (myAR->execute("</xsl:text><xsl:value-of select="concat($service,'.',$f/@name)"/>
     <xsl:text>", _args, _result))
     {
     </xsl:text>
     <xsl:choose>
     <xsl:when test="$f/@type = $javaStdTypes">
       <xsl:if test="$f/@type != 'void'">
         <xsl:choose>
            <xsl:when test="contains($f/@fulltype, '[')">
            <xsl:text>//FIXME  simple array types to be filled here 
            </xsl:text>
            
            </xsl:when>
            <xsl:otherwise>
            <xsl:text>    retval = _result;</xsl:text>
            </xsl:otherwise>
        </xsl:choose>
       </xsl:if>
     </xsl:when>
    
     <xsl:otherwise> <!-- object is returned  
         <xsl:call-template name="retbody"><xsl:with-param name="t" select="$jel/jelclass[@type = $f/@type]"></xsl:with-param></xsl:call-template>
         -->
         <xsl:text>//FIXME object type to be filled here</xsl:text>
     </xsl:otherwise>
     </xsl:choose>
     <xsl:text>
     }
    </xsl:text>
    <xsl:if test="$f/@type != 'void'">
    <xsl:text>
     return retval;
    </xsl:text>
    </xsl:if>
    <xsl:text>
   };
   </xsl:text>
   </xsl:template>
   <xsl:template name="retbody">
     <xsl:param name="t"></xsl:param>
       <xsl:for-each
         select="$t/methods/method[@visibility='public' and contains(@name,'get')]">
         <xsl:variable name="tmp" select="substring-after(@name,'get')" />
         <!-- convert method to javabean style field name. -->
         <xsl:variable name="fieldname"
            select="concat( lower-case(substring($tmp,1,1) ) ,substring($tmp,2) )" />
         <xsl:text>retval.</xsl:text><xsl:value-of select="$fieldname"/> <xsl:text> = _result["</xsl:text><xsl:value-of select="$fieldname"/> <xsl:text>"];
         </xsl:text>
      </xsl:for-each>    
   </xsl:template>
   <!-- helper methods -->
   <xsl:template name="recursiveDataMembers">
      <!-- todo - need to do something different for inner classes  -->
      <xsl:param name="t" />
      <xsl:message>
         in
         <xsl:value-of select="$t/@type" />
      </xsl:message>
      <xsl:if test="$t/@superclass != 'Object' and $t/@superclass">
<!-- note that interfaces do not declare that they have a superclass at all, so he @superclass is there to test for existance  -->
         <xsl:message>
            recurse into <xsl:value-of select="$t/@superclass" /> actually
            <xsl:value-of
               select="name(/jel/jelclass[@type = $t/@superclass])" />
         </xsl:message>
         <xsl:call-template name="recursiveDataMembers">
            <xsl:with-param name="t"
               select="/jel/jelclass[@type = $t/@superclass]" />
         </xsl:call-template>
      </xsl:if>
      <xsl:for-each
         select="$t/implements/interface[not(@type = $stdInterfaces)]">
         <xsl:variable name="inheritedType" select="@type" />
         <xsl:message>
            inherited type
            <xsl:value-of select="@type" />
            actualy
            <xsl:value-of
               select="name(/jel/jelclass[@type = $inheritedType])" />
         </xsl:message>
         <xsl:call-template name="recursiveDataMembers">
            <!-- should prehaps reformulate with an axis specifier -->
            <xsl:with-param name="t"
               select="/jel/jelclass[@type = $inheritedType]" />
         </xsl:call-template>
      </xsl:for-each>
      <!-- finally just do these data members -->
      <xsl:call-template name="dataMembers">
         <xsl:with-param name="t" select="$t" />
      </xsl:call-template>
   </xsl:template>
   <xsl:template name="dataMembers">
      <xsl:param name="t" />
      <xsl:message>
         data members for element
         <xsl:value-of select="node-name($t)" />
         , type
         <xsl:value-of select="$t/@type" />
      </xsl:message>
      <xsl:text>/* data members from </xsl:text>
      <xsl:value-of select="$t/@type" />
      <xsl:text> */
</xsl:text>
      <xsl:for-each
         select="$t/methods/method[@visibility='public' and contains(@name,'get')]">
         <xsl:variable name="tmp" select="substring-after(@name,'get')" />
         <!-- convert method to javabean style field name. -->
         <xsl:variable name="fieldname"
            select="concat( lower-case(substring($tmp,1,1) ) ,substring($tmp,2) )" />
         <!-- TODO put the description back in later 		
            /*
            <xsl:value-of select="comment/description" disable-output-escaping="yes" />
            <xsl:if test="$t/comment/attribute[@name='@deprecated']">
            <b>Deprecated: </b>
            <xsl:value-of select="comment/attribute[@name='@deprecated']" />
            <br />
            </xsl:if>
            */ -->
         <xsl:call-template name="convert-type">
            <xsl:with-param name="p" select="." />
         </xsl:call-template>
         <xsl:value-of select="' '" />
         <xsl:choose>
            <!-- IMPL specific hack to change reserved fieldname token long into something nicer - perhaps the acr could chose a different  -->
            <xsl:when test="$fieldname = 'long'">
               <xsl:text>lon</xsl:text>
            </xsl:when>
            <xsl:when test="$fieldname = 'struct'">
            <xsl:text>structure</xsl:text></xsl:when>
            <xsl:otherwise>
               <xsl:value-of select="$fieldname" />
            </xsl:otherwise>
         </xsl:choose>
         <xsl:text>;
</xsl:text>
      </xsl:for-each>
   </xsl:template>
   
   <xsl:template name="datamemberseq">
   <!-- todo - integrate with the DataMembers call -->
       <xsl:param name="t" />
      <xsl:message>
 data members for element <xsl:value-of select="node-name($t)" />, type<xsl:value-of select="$t/@type" />
    </xsl:message>
    <members>
      <xsl:for-each
         select="$t/methods/method[@visibility='public' and contains(@name,'get')]">
         <xsl:element name="member">
         <xsl:attribute name="name">
         <xsl:variable name="tmp" select="substring-after(@name,'get')" />
         <!-- convert method to javabean style field name. -->
         <xsl:variable name="fieldname"
            select="concat( lower-case(substring($tmp,1,1) ) ,substring($tmp,2) )" />
         <xsl:choose>
            <!-- IMPL specific hack to change reserved fieldname token long into something nicer - perhaps the acr could chose a different  -->
            <xsl:when test="$fieldname = 'long'">
               <xsl:text>lon</xsl:text>
            </xsl:when>
            <xsl:when test="$fieldname = 'struct'">
            <xsl:text>structure</xsl:text></xsl:when>
            <xsl:otherwise>
               <xsl:value-of select="$fieldname" />
            </xsl:otherwise>
         </xsl:choose>
         </xsl:attribute>
         <xsl:attribute name="type">
          <xsl:call-template name="convert-type">
            <xsl:with-param name="p" select="." />
         </xsl:call-template>
         </xsl:attribute>
         </xsl:element>
    </xsl:for-each>
    </members>     
   </xsl:template>
   
   <xsl:template name="beanclassdef">
      <xsl:param name="b"></xsl:param>
      <xsl:text>
class </xsl:text>
      <xsl:value-of select="translate(concat($b/@type,'_'),'.','_')" />
      <xsl:if
         test="count($b/implements/interface[not (@type = $stdInterfaces)])>0">
         <xsl:text> : </xsl:text>
         <xsl:value-of
            select="for $i in $b/implements/interface[not (@type = $stdInterfaces)]/@type return concat('public ',translate(concat($i, '_'),'.','_'))"
            separator="," />
      </xsl:if>
      <xsl:text>{
public:
</xsl:text>
      <xsl:variable name="members">
         <xsl:call-template name="datamemberseq">
            <xsl:with-param name="t" select="$b"></xsl:with-param>
         </xsl:call-template>
      </xsl:variable>
      <xsl:for-each select="$members/members/member">
         <xsl:value-of select="concat(@type,' ',@name,'_;')"></xsl:value-of>
         <xsl:text>
</xsl:text>
      </xsl:for-each>
      <xsl:value-of select="translate(concat($b/@type,'_'),'.','_')" />
      <xsl:text>( XmlRpcValue&amp; v)</xsl:text>
      <xsl:if
         test="count($b/implements/interface[not (@type = $stdInterfaces)])>0 or count($members/members/*) > 0">
         <xsl:text> : </xsl:text>
         <xsl:value-of
            select="for $i in $b/implements/interface[not (@type = $stdInterfaces)]/@type  return concat(translate(concat($i, '_'),'.','_'),'(v)')"
            separator=", " />
         <xsl:if
            test="count($b/implements/interface[not (@type = $stdInterfaces)])>0">
            <xsl:text>, </xsl:text>
         </xsl:if>
         <xsl:value-of
            select="for $j in $members/members/member/@name return concat($j,'_(v[&quot;',$j,'&quot;])')"
            separator=", " />
      </xsl:if>
         <xsl:text> {
}
virtual ~</xsl:text>
         <xsl:value-of select="translate(concat($b/@type,'_'),'.','_')" />
         <xsl:text>() {
}
</xsl:text>
      <xsl:text>


};
</xsl:text>
   </xsl:template>
   <xsl:template name="convert-type"><!-- this is a little ugly... -->
      <xsl:param name="p" /> <!-- the actual type -->
      <xsl:param name="f"/> <!-- in a function definition -->
      <xsl:param name="c" /> <!-- in a class definition -->
      <xsl:if test="contains($p/@fulltype,'[]')">
         <xsl:text>ListOf</xsl:text>
      </xsl:if>
      <xsl:choose>
         <xsl:when test="$typeMap/map/t[@from = $p/@type]">            
            <xsl:value-of select="$typeMap/map/t[@from = $p/@type]/@to"/>
         </xsl:when>
         <xsl:when
            test="contains($p/@fulltype,'java')  or $p/@fulltype = $javaSimpleTypes">
<!--  standard lib or prim type. -->
            <xsl:value-of select="$p/@type" />
         </xsl:when>
         <xsl:otherwise><!-- complex type - use a struct -->
            <xsl:if test="not (contains($p/@fulltype,'[]'))">
               <xsl:text>struct </xsl:text>
            </xsl:if>
            
            <xsl:value-of select="translate($p/@type,'.','_')" />
           
            <xsl:if test="$hier/hierarchy//*[name() = $p/@type and count(child::*)>0]">
              <xsl:text>_Base</xsl:text>
            </xsl:if>
            <!-- if in a function parameter definition then add pointer -->
            <xsl:if test="$f">
              <xsl:text>* </xsl:text>
            </xsl:if> 
         </xsl:otherwise>
      </xsl:choose>
   </xsl:template>
   <xsl:template name="listof">
      <xsl:param name="t" />
      <xsl:variable name="tt">
         <xsl:value-of select="$t" />
         
         <xsl:if
            test="$hier/hierarchy//*[name() = $t and count(child::*)>0]">
            <xsl:text>_Base</xsl:text>
         </xsl:if>
          
      </xsl:variable>
      <xsl:text>
typedef struct {
    int n;
    struct </xsl:text>
      <xsl:value-of select="$tt"></xsl:value-of>
      <!-- IMPL prehaps it would be nicer to have more meaningful member name for the list -->
      <xsl:text> *list[];
   } ListOf</xsl:text>
      <xsl:value-of select="$tt" />
      <xsl:text>;</xsl:text>
   </xsl:template>
   <xsl:template name="typeTreeInv">
      <xsl:param name="t"></xsl:param>
      <xsl:message> treetype=<xsl:value-of select="$t/@type" /> supertype=<xsl:value-of select="$t/@superclass" />
      </xsl:message>
      <xsl:element name="{$t/@type}">
         <xsl:choose>
            <xsl:when test="$t/@superclass != 'Object'">
               <xsl:message>recursing direct supertype</xsl:message>
               <xsl:call-template name="typeTreeInv">
                  <xsl:with-param name="t"
                     select="$jel/jelclass[@type=$t/@superclass]">
                  </xsl:with-param>
               </xsl:call-template>
            </xsl:when>
            <!-- TODO multiple interfaces -->
            <xsl:when test="$t/implements/interface">
               <xsl:for-each
                  select="$t/implements/interface[not(contains(@fulltype, 'java.'))]">
                  <xsl:message>
                     recursing interface
                     <xsl:value-of select="@type" />
                  </xsl:message>
                  <xsl:call-template name="typeTreeInv">
                     <xsl:with-param name="t"
                        select="$jel/jelclass[@type=current()/@type]">
                     </xsl:with-param>
                  </xsl:call-template>
               </xsl:for-each>
            </xsl:when>
         </xsl:choose>
      </xsl:element>
   </xsl:template>
   <xsl:template name="substring-after-last">
      <xsl:param name="input" />
      <xsl:param name="marker" />
      <xsl:choose>
         <xsl:when test="contains($input,$marker)">
            <xsl:call-template name="substring-after-last">
               <xsl:with-param name="input"
                  select="substring-after($input,$marker)" />
               <xsl:with-param name="marker" select="$marker" />
            </xsl:call-template>
         </xsl:when>
         <xsl:otherwise>
            <xsl:value-of select="$input" />
         </xsl:otherwise>
      </xsl:choose>
   </xsl:template>
   <xsl:template name="reverse">
      <xsl:param name="t"></xsl:param>
      <xsl:element name="{$t/name()}">
         <xsl:choose>
            <xsl:when test="$t/parent::node()/name() != 'here'">
               <xsl:message>
                  reversing into
                  <xsl:value-of select="$t/parent::node()/name()" />
               </xsl:message>
               <xsl:call-template name="reverse">
                  <xsl:with-param name="t" select="$t/parent::node()"></xsl:with-param>
               </xsl:call-template>
            </xsl:when>
         </xsl:choose>
      </xsl:element>
   </xsl:template>
   <xsl:template name="grouper">
      <xsl:param name="t"></xsl:param>
      <xsl:message>grouping on <xsl:value-of select="$t/name()"></xsl:value-of>
      </xsl:message>
      <xsl:for-each-group select="$t/*" group-by="name()">
         <xsl:element name="{current-grouping-key()}">
            <!--  
               <xsl:element name="{name()}" />
            -->
            <xsl:call-template name="grouper">
               <xsl:with-param name="t" select="current-group()"></xsl:with-param>
            </xsl:call-template>
         </xsl:element>
      </xsl:for-each-group>
   </xsl:template>
   <xsl:template name="derivedUnion">
    <xsl:param name="t"></xsl:param>
   <xsl:text>
   struct </xsl:text>
   <xsl:value-of select="$t"/><xsl:text>_Base {
   enum AcrType _type;
   union {
       struct </xsl:text><xsl:value-of select="$t"/> <xsl:text> </xsl:text><xsl:value-of select="lower-case($t)"/><xsl:text>;
</xsl:text>
   <xsl:for-each select="$hier//*[ancestor::*/name() = $t]">
   <xsl:text>
   struct </xsl:text><xsl:value-of select="concat (name(.), ' ')"/><xsl:value-of select="concat(lower-case(name(.)),';')"/>
   </xsl:for-each>
   <xsl:text>
   } d;
};
</xsl:text>
   </xsl:template>
   <xsl:template name="classHierarchy">
   <!-- TODO this is still not really doing completely the correct thing? - see hasCoverage multiple inheritance....-->
      <xsl:variable name="obtree">
         <here>
            <xsl:for-each
               select="$beans[@superclass != 'Object'] | $beans[implements/interface]">
               <xsl:call-template name="typeTreeInv">
                  <xsl:with-param name="t" select="."></xsl:with-param>
               </xsl:call-template>
            </xsl:for-each>
         </here>
      </xsl:variable>
      <xsl:call-template name="reversegrouper"><xsl:with-param name="t" select="$obtree"></xsl:with-param></xsl:call-template>
   </xsl:template>
   <xsl:template name="reversegrouper">
   <xsl:param name="t"></xsl:param>
         <xsl:variable name="reversed">
         <reversed>
            <xsl:for-each
               select="$t//*[count(descendant::*)=0 and count(ancestor::*) > 0]">
               <xsl:text>
</xsl:text>
               <xsl:call-template name="reverse">
                  <xsl:with-param name="t" select="."></xsl:with-param>
               </xsl:call-template>
            </xsl:for-each>
         </reversed>
      </xsl:variable>
      <hierarchy>
         <xsl:call-template name="grouper">
            <xsl:with-param name="t" select="$reversed/reversed"></xsl:with-param>
         </xsl:call-template>
      </hierarchy>
   
   </xsl:template>
    <xsl:template name="depend">
      <xsl:param name="t"></xsl:param>
      <xsl:message><xsl:value-of select="$t/@type"/></xsl:message>
       <xsl:element name="{$t/@type}">
         <xsl:variable name="alltypes" as="xs:string *">
             <xsl:for-each select="$hier//*[name() = $t/@type]/ancestor-or-self::*"><!-- for each of the ancestors --> 
               <xsl:message>looking at type hierarchy for <xsl:value-of select="current()/name()"/> type =<xsl:value-of select="$jel/jelclass[@type = current()/name()]/@type"/></xsl:message>
               <xsl:sequence select="$jel/jelclass[@type = current()/name()]//field[ not(@type = $javaStdTypes )]/@type|$jel/jelclass[@type = current()/name()]//method[@visibility='public' and contains(@name,'get') and not(@type = $javaStdTypes )]/@type"/>           
             </xsl:for-each>
         </xsl:variable>
         <xsl:message>var=<xsl:value-of select="$alltypes" separator=","/> empty=<xsl:value-of select="empty($alltypes)"/>
         </xsl:message>
         <xsl:if test="not (empty($alltypes))" >
         <xsl:for-each-group
            select="$alltypes"
            group-by=".">
            <xsl:message> group = <xsl:value-of select="."></xsl:value-of></xsl:message>
           <!-- <xsl:element name="{current-grouping-key()}"></xsl:element>-->
             <xsl:message>recursing into <xsl:value-of select="current-grouping-key()" /></xsl:message>
             <xsl:call-template name="depend">
               
              <xsl:with-param name="t" select="$jel/jelclass[@type=current-grouping-key()]"></xsl:with-param>
            </xsl:call-template>
             
         </xsl:for-each-group>
         </xsl:if>
      </xsl:element>
   </xsl:template>
     <xsl:template name="dependClass">
      <xsl:param name="t"></xsl:param>
      <xsl:message><xsl:value-of select="$t/@type"/></xsl:message>
       <xsl:element name="{$t/@type}">
         <xsl:variable name="alltypes" as="xs:string *">
              <xsl:message>looking at Class type hierarchy for <xsl:value-of select="$t/@type"/> </xsl:message>
               <xsl:sequence select="$t/@superclass[.!='Object']|$t/implements/interface[not(@type = $stdInterfaces)]/@type|$t//method[@visibility='public' and contains(@name,'get') and not(@type = $javaStdTypes )]/@type"/>           
         </xsl:variable>
         <xsl:message>var=<xsl:value-of select="$alltypes" separator=","/> empty=<xsl:value-of select="empty($alltypes)"/>
         </xsl:message>
         <xsl:if test="not (empty($alltypes))" >
         <xsl:for-each-group
            select="$alltypes"
            group-by=".">
            <xsl:message> group = <xsl:value-of select="."></xsl:value-of></xsl:message>
           <!-- <xsl:element name="{current-grouping-key()}"></xsl:element>-->
             <xsl:message>recursing into <xsl:value-of select="current-grouping-key()" /></xsl:message>
             <xsl:call-template name="dependClass">
               
              <xsl:with-param name="t" select="$jel/jelclass[@type=current-grouping-key()]"></xsl:with-param>
            </xsl:call-template>
             
         </xsl:for-each-group>
         </xsl:if>
      </xsl:element>
   </xsl:template>
   
   <xsl:template name="header">
   <xsl:text>
/*
C interface to the AR
Paul Harrison paul.harrison@manchester.ac.uk

DO NOT EDIT - this file is produced automatically by the AR build process

 * Copyright 2007 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
*/
   
   </xsl:text></xsl:template>
   
   <xsl:template match="node()" mode="beandef">
      <!-- this is not the correct ordering because of multiple inheritance - need to do breadth first traversal -->
       <xsl:apply-templates select="*" mode="beandef" />
      <xsl:if test="not(name() = preceding::*/name())">
         <xsl:call-template name="beanclassdef">
            <xsl:with-param name="b"
               select="$jel/jelclass[@type = current()/name()]">
            </xsl:with-param>
         </xsl:call-template>
      </xsl:if>    
      
   </xsl:template>
   <xsl:template match="node()" mode="order">
      <xsl:if test="count(*)>0">
         <xsl:apply-templates select="*" mode="order" />
      </xsl:if>
      <xsl:if test="not(name() = preceding::*/name())">
         <xsl:message>
            odered decl of bean
            <xsl:value-of select="name()" />
         </xsl:message>
         <xsl:call-template name="dobean">
            <xsl:with-param name="b"
               select="$jel/jelclass[@type = current()/name()]">
            </xsl:with-param>
         </xsl:call-template>
      </xsl:if>
   </xsl:template>
   <!-- default template -->
   <xsl:template match="node()">
      <xsl:copy-of select="." />
   </xsl:template>
</xsl:stylesheet>