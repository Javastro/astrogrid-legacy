<?xml version="1.0"?>
  <xsl:stylesheet version="1.0" 
       xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  
    <xsl:template match="page">
      <div id="webstartAppsPage">
        <ag-div>
          <xsl:apply-templates/>
        </ag-div>
      </div>
    </xsl:template>

    <xsl:template match="content">
      <style type="text/css">
        span.HEADING{color:midnightblue; 
                     font-weight:bold; 
                     font-size:larger;}
      </style>  
            
      <script language="javascript">      
        var div_selected = false;
	    var previously_selected_div = null;
        function show_div(id)
        {     
          if (div_selected==false) 
          {
            div_selected=true;          
            if (document.getElementById(id))
            {          
              document.getElementById(id).style.display="";
		      previously_selected_div=id;
		    }		    
          }
          else
          {	
            if ( document.getElementById(previously_selected_div) )
            {
		      document.getElementById(previously_selected_div).style.display="none"; 
		    }
            document.getElementById(id).style.display="";
		    previously_selected_div=id;
		    div_selected = true;		  
          }
        }
      </script>    
      <agComponentTitle>Astrogrid Webstart applications</agComponentTitle> 
      <ul>
        <li><a class="strongLink" onClick="nofooter(); show_div('apps')" style="cursor: help">What are webstart applications?</a></li>
        <li><a class="strongLink" onClick="nofooter(); show_div('hap')" style="cursor: help">What will happen when I select one?</a></li>
        <li><a class="strongLink" onClick="nofooter(); show_div('req')" style="cursor: help">System requirements.</a></li>
        <li><a class="strongLink" onClick="nofooter(); show_div('faq')" style="cursor: help">Frequently asked questions.</a></li>
       </ul>
       <table cellpadding="3" cellspacing="3" border="0">
         <tr>
           <td width="5"></td>
           <td title="Launch Workbench application">
             <a href="http://software.astrogrid.org/jnlp/beta/astrogrid-desktop/astrogrid-desktop.jnlp">
               <img src="/astrogrid-portal/mount/scenarios/workbench.gif" 
                    height="80" 
                    width="80" 
                    alt="workbench"/>
             </a>
           </td>
           <td><font size="-1"><b>Astrogrid workbench</b> integrates astrogrid into the desktop.<br/> 
               Allows astrogrid services to be easily called from user applications and scripts;
               </font>
           </td>
           <td width="5"></td>                                 
         </tr>
         <tr>
           <td width="5"></td>
           <td title="Launch Aladin application">
             <a href="http://software.astrogrid.org/jnlp/beta/aladin-avo/aladin-avo.jnlp">
               <img src="/astrogrid-portal/mount/scenarios/avo.gif" 
                    height="80" 
                    width="80" 
                    alt="aladin"/>
             </a>
           </td>         
           <td><font size="-1">
             <b>Aladin</b> is an interactive software sky atlas allowing the user to visualize digitized images of any part of the sky, to superimpose entries from astronomical catalogs or personal user data files, and to interactively access related data and information from the SIMBAD, NED, VizieR, or other archives for all known objects in the field.
             Aladin is particularly useful for multi-spectral cross-identifications of astronomical sources, observation preparation and quality control of new data sets.
             The Aladin sky atlas is available in three modes: a simple previewer, a Java applet interface and a Java Standalone application.
             </font>
           </td>
           <td width="5"></td>                                 
         </tr>
         <tr>
           <td width="5"></td>
           <td title="Launch Topcat application">
             <a href="http://software.astrogrid.org/jnlp/beta/topcat/topcat.jnlp">
               <img src="/astrogrid-portal/mount/scenarios/topcat.gif" 
                    height="80" 
                    width="80" 
                    alt="topcat"/>
             </a>
           </td>
           <td><font size="-1">
             <b>Topcat</b> is an interactive graphical viewer and editor for tabular data. It has been designed for use with astronomical tables such as object catalogues, but is not restricted to astronomical applications.
             It understands a number of different astronomically important formats (including FITS and VOTable) and more formats can be added. It offers a variety of ways to view and analyse tables, 
             including a browser for the cell data themselves, viewers for information about table and column metadata, and facilities for plotting, calculating statistics and joining tables using flexible matching algorithms.
             Using a powerful and extensible Java-based expression language new columns can be defined and row subsets selected for separate analysis. Table data and metadata can be edited and the resulting modified table can be written out in a wide range of output formats.
             The program is written in pure Java and available under the GNU General Public Licence. It has been developed by the Starlink project in the UK. Its underlying table processing facilities are provided by STIL. 
             </font>
           </td>
           <td width="5"></td>                                 
         </tr>                  
       </table>
       <p /> 
       <div style="display: none; padding-left:20px;" id="apps">
       <span class="HEADING">What are webstart applications?</span><br />
       <hr />
         <table>
           <tr>
             <td>
             <font size="-1">
              Java Web Start is an application-deployment technology that gives you the power to launch full-featured applications with a single click from your Web browser.
              You can download and launch applications, such as the Astrogrid workbench or Aladin, without going through complicated installation procedures. <br/>
              Java Web Start includes the security features of the Java 2 platform, so the integrity of your data and files is never compromised. In addition, Java Web Start technology enables you to use the latest Java 2 technology with any browser.<br/>
              With Java Web Start, you launch applications simply by clicking on a Web page link. If the application is not present on your computer, Java Web Start automatically downloads all necessary files. 
              It then caches the files on your computer so the application is always ready to be relaunched anytime you want—either from an icon on your desktop or from the browser link. 
              And no matter which method you use to launch the application, the most current version of the application is always presented to you.
              <p/>
              Java Web Start is designed on the premise that most people still want applications for many activities. Most users do not want to give up their favorite traditional word processor, spreadsheet application, or email client for some HTML-based interface in a browser. The application interface has a number of benefits that are very appealing:
                 <ul>
                   <li>The user interface is very rich and responsive</li>
                   <li>Applications are easy to launch from Start Menu, Desktop, or by other means - and do not necessarily require the Web browser to be running</li>
                   <li>The speed and responsiveness of the application does not depend on the connection speed</li>
                   <li>Applications work offline, for example, in a plane when traveling </li> 
                 </ul>
               However, traditional applications also have a number of problems. In particular, applications are often complicated to install, and even worse to upgrade.
               For companies that maintain hundreds or even thousands of desktop computers for their employees, keeping software up-to-date on these computers is typically a major headache. These problems have encouraged many companies to look into using HTML-based applications because of their simple and cost-effective deployment.
               Java Web Start technology, the innovative technology for deploying applications based on the Java 2 platform, enables you to launch full-featured applications via any browser, on any platform, from anywhere on the Web, in a secure fashion. It provides the best of both worlds: The ease of deployment and use of HTML, as well as the power and flexibility of a full-fledged application.
               </font>
             </td>
           </tr>
         </table>
       </div>      
       <div style="display: none; padding-left:20px;" id="hap">
       <span class="HEADING">What will happen when I select one?</span><br />
       <hr />
         <table border="0" cellpadding="5">
           <tr>
             <td colspan="2">
               <span class="HEADING">Security Dialog:</span>
             </td>
           </tr>
           <tr>
             <td colspan="2">
             <font size="-1">
               When you choose one of the Webstart Applications a security warning dialog box will appear.
               The security warning dialog box warns the user that an application has requested unrestricted access, and asks the user to accept this request or not. 
               The dialog shows information about the origin of the code based on the certificate used to sign the code. This is so the user can make an informed decision on whether to trust the application or not.
               To run the chosen application click yes and the application will load and run.
               </font>
             </td>
           </tr>           
           <tr>
             <td width="1">
               <img src="/astrogrid-portal/mount/scenarios/jaws.flowchart.jpg" alt="webstart image"/>
             </td>
             <td>
             <font size="-1">
               The figure shows how Java Web Start technology works both from a user and technical perspective:
               The yellow arrow shows the user experience. A single click on a link launches a full-featured Java technology-based application, which may never have been present on the local computer before.<br />             
               What really happens behind the scenes is shown by the gray arrows:
               <ol>
                 <li>When the user clicks on a download link, the link instructs the browser to invoke Java Web Start technology. Java Web Start technology presents a splash screen.</li>
                 <li>Java Web Start technology queries the Web to determine if all the resources needed for the application are already downloaded.</li>
                 <ul>
                   <li>If they are, and the most recent version of the application is present, the application will be launched immediately (step 3).</li>
                   <li>If the resources are not present or an update is available, Java Web Start will download the needed resources. Thus, the initial download and subsequent updates of an application happen transparently.</li>
                 </ul>
               </ol>
               As of JAWS 1.4.2, JNLP URLs are also directly openable from the JAWS Application Manager and can be bookmarked. Moreover, they may be .html or .jnlp files.
               The steps described above are very similar to how other helper applications such as Real Audio or Acrobat Reader work. Java Web Start technology makes the Java 2 platform available to the browser, so you can launch any web-based application simply and securely.
               The only prerequisite is to download and install Java Web Start software on the client machine. This is a one-time download and, once done, installing and upgrading applications might be a thing of the past.                                          
               </font>
             </td>
           </tr>
         </table>       
       </div>
       <div style="display: none; padding-left:20px;" id="req">
       <span class="HEADING">System requirements.</span><br />
       <hr />
         <table>
           <tr>
             <td>
               <font size="-1">
               The latest version of Java Web Start is contained in the Java 2 Platform, Standard Edition 5.0.
               <p/>
               Any client system that supports the Java 2 platform can use Java Web Start. Java Web Start works with virtually all browsers.
               <p/>
               If you do not have Java Webstart installed it can be downloaded <a href="http://java.sun.com/products/javawebstart/downloads/index.html" target="new">here</a>.
               </font>
             </td>
           </tr>
         </table>       
       </div>
       <div style="display: none; padding-left:20px;" id="faq">
       <span class="HEADING">Frequently asked questions.</span><br />
       <hr />
         <table>
           <tr>
             <td>
             <font size="-1">
               A comprehensive FAQ can be found <a href="http://java.sun.com/j2se/1.5.0/docs/guide/javaws/developersguide/faq.html" target="new">here</a>.
               </font>
             </td>
           </tr>
         </table>
       </div>      
      <xsl:apply-templates/>
    </xsl:template>
  
    <xsl:template match="@*|node()">
	  <xsl:copy>
	    <xsl:apply-templates select="@*|node()"/>
	  </xsl:copy>
    </xsl:template>
	<xsl:template match="text()">
      <xsl:value-of select="."/>
    </xsl:template>
  </xsl:stylesheet>		
		
