<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xsp="http://apache.org/xsp"
	xmlns:util="http://apache.org/xsp/util/2.0"
	xmlns:jpath="http://apache.org/xsp/jpath/1.0" >
	
  <xsl:param name="password-change" />
  
  <xsl:template match="AstroGrid">
       <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="ProducePage">
<agComponentTitle>AstroGrid: <xsl:value-of select="userID"/>  personal data</agComponentTitle>
<!--
<form name="updateData" method="Post">
<div style="padding-left: 40px; padding-top: 20px">
<table class="compact">
<tr>
<td>Username:
</td>
<td class="agCompositeTitle"><xsl:value-of select="userID"/>
</td>
</tr>
<tr>
<td>Primary email: 
</td>
<td><input name="pemail" size="40"/>
</td>
</tr>
<tr>
<td>Secondary email: 
</td>
<td><input name="semail" size="40"/>
</td>
</tr>
<tr>
<td>Your mother's maiden name:
</td>
<td><input name="mama" size="40"/>
</td>
</tr>
<tr>
<td>Institution:
</td>
<td><input name="institute" size="40"/>
</td>
</tr>
<tr>
<td>Status:
</td>
<td><select>
<option value="ugstudent">Undergraduate Student</option>
<option value="gstudent">Graduate Student</option>
<option value="research">Researcher</option>
<option value="professor">Professor</option>
<option value="nota">N/A</option>
</select>
</td>
</tr>
<tr>
<td>Address:
</td>
<td><input name="address" size="40"/>
</td>
</tr>
<tr>
<td>Postal Code:
</td>
<td><input name="PostalCode" size="10"/>
</td>
</tr>
<tr>
<td>Line of research:
</td>
<td><input name="research" size="40"/>
</td>
</tr>
<tr>
<td>Maximum data latency (days):
</td>
<td><input name="institute" size="4" value="7"/>
</td>
</tr>
<tr>
<td>Supervisor's name (if a student):
</td>
<td><input name="institute" size="40"/>
</td>
</tr>
<tr>
<td colspan="2" align="right">
<span class="submitButton">Update information</span>
</td>
</tr>
</table>
</div>
</form>
-->

<form name="updatePass" 
      method="Post"
      action="/astrogrid-portal/main/mount/scenarios/userData.scn/df75hs3-8s7d5a"
      enctype="multipart/form-data" >
<div style="padding-left: 40px; padding-top: 2px">
<table class="compact">
<tr>
<td>Username:
</td>
<td class="agCompositeTitle"><xsl:value-of select="userID"/>
</td>
</tr>
<tr>
<td>Old password:
</td>
<td><input name="opa" type="password" size="40"/>
</td>
</tr>
<tr>
<td>New password:
</td>
<td><input name="npa" type="password" size="40"/>
</td>
</tr>
<tr>
<td>New password (verification):
</td>
<td><input name="npav" type="password" size="40"/>
</td>
</tr>
<tr>
<td colspan="2" align="right">
<span class="submitButton" onClick="submit();" >Change password</span>
</td>
</tr>
<tr>

  <xsl:if test="$password-change">
     <xsl:choose>
        <xsl:when test="$password-change = 'true'">
          <td colspan="2" align="left" style="color: black" >*** Password changed successfully ***</td>
        </xsl:when>
        <xsl:otherwise>
          <td colspan="2" align="left" style="color: red" >*** Failed! Please check your details and try again ***</td>
        </xsl:otherwise>
     </xsl:choose>
  </xsl:if>

</tr>
</table>
</div>
</form>
  </xsl:template>


<!--	leave untouched -->
  <xsl:template match="break">
       <xsl:apply-templates/>
       <br />
  </xsl:template>

  <xsl:template match="userID">
  </xsl:template>

  <xsl:template match="laconic">
  </xsl:template>

  <xsl:template match="@*|node()" priority="-2"><xsl:copy><xsl:apply-templates
  select="@*|node()"/></xsl:copy></xsl:template>
  <xsl:template match="text()" priority="-1"><xsl:value-of select="."/></xsl:template>

</xsl:stylesheet>
