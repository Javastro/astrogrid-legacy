/**
 * MsComServicesSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.microsoft.www;

public interface MsComServicesSoap extends java.rmi.Remote {

    // <DIV style='background-color:darkseagreen;'><B>Summary :</B>Retrieves
    // the current version of Microsoft.Com Platform WebServices<BR><B>Parameters:</B><Table
    // border=1 style='boderStyle:solid'><TR><TD><B>Name</B></TD><TD><B>Type</B></TD><TD><B>Description</B></TD></TR></Table><B>Example
    // : </B>GetVersion()<BR></DIV>
    public java.lang.String getVersion() throws java.rmi.RemoteException;

    // <DIV style='background-color:darkseagreen;'><B>Summary :</B>Retrieves
    // the Top 10 Downloads @Microsoft.com<BR><B>Parameters:</B><Table border=1
    // style='boderStyle:solid'><TR><TD><B>Name</B></TD><TD><B>Type</B></TD><TD><B>Description</B></TD></TR><TR><TD>topType</TD><TD>enum
    // TopType</TD><TD>The type{Popular|Recent}of Top Downloads you want
    // to retrieve. <BR><B>Note : </B> Currently for topType only {Popular|Recent}
    // are supported </TD></TR><TR><TD>topN</TD><TD>int</TD><TD>The Top n
    // rows for Downloads. <BR><B>Note:</B>topN <= 25 .</TD></TR><TR><TD>cultureID</TD><TD>string</TD><TD>Supported
    // CultureID can be otained by calling GetCultures Example: {de-DE|en-US|es-ES|fr-FR|it-IT|it-IT|ja-JP|ko-KR|zh-CHS|zh-CN}.
    // </TD></TR></Table><B>Example : </B> To retrieve Top 10 Popular Downloads
    // use GetTopDownloads(&quot;Popular&quot;&#44;10&#44;&quot;en-US&quot;)<BR>To
    // retrieve Top 10 Recent Downloads use GetTopDownloads(&quot;Recent&quot;&#44;10&#44;&quot;en-US&quot;)<BR></DIV>
    public com.microsoft.www.Downloads getTopDownloads(com.microsoft.www.TopType topType, int topN, java.lang.String cultureID) throws java.rmi.RemoteException;

    // <DIV style='background-color:darkseagreen;'><B>Summary :</B>Retrieves
    // Downloads Details <BR><B>Parameters:</B><Table border=1 style='boderStyle:solid'><TR><TD><B>Name</B></TD><TD><B>Type</B></TD><TD><B>Description</B></TD></TR><TR><TD>downloadID</TD><TD>GUID</TD><TD>DownloadID
    // retrieved from GetTopDownloads method</TD></TR><TR><TD>cultureID</TD><TD>string</TD><TD>Supported
    // CultureID can be otained by calling GetCultures Example: {de-DE|en-US|es-ES|fr-FR|it-IT|it-IT|ja-JP|ko-KR|zh-CHS|zh-CN}.
    // </TD></TR></Table><B>Example : </B>GetDownloadDetail(&quot;a19bed22-0b25-4e5d-a584-6389d8a3dad0&quot;&#44;&quot;en-US&quot;)<BR></DIV>
    public com.microsoft.www.Download getDownloadDetail(com.microsoft.wsdl.types.Guid downloadID, java.lang.String cultureID) throws java.rmi.RemoteException;

    // <DIV style='background-color:darkseagreen;'><B>Summary :</B>Retrieves
    // the list of supported Cultures Taxonomy<BR><B>Parameters:</B><Table
    // border=1 style='boderStyle:solid'><TR><TD><B>Name</B></TD><TD><B>Type</B></TD><TD><B>Description</B></TD></TR></Table><B>Example
    // : </B>GetCultures()<BR></DIV>
    public com.microsoft.www.Cultures getCultures() throws java.rmi.RemoteException;
}
