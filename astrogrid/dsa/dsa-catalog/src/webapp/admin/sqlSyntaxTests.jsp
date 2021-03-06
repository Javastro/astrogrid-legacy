<%@ page 
   import="org.astrogrid.dataservice.service.InstallationSyntaxCheck, java.io.PrintWriter"
   isThreadSafe="false"
   session="false"
%>
<% String pathPrefix = ".."; // For the navigation include %>

<html>
<head>
<title>SQL-syntax tests</title>
<style type="text/css" media="all">
          @import url("../style/astrogrid.css");
</style>
<style type='text/css'>
          .pass {background: #9f9;}
          .fail {background: #f99;}
</style>
</head>

<body>
<%@ include file="../header.xml" %>
<%@ include file="../navigation.xml" %>

<div id='bodyColumn'>

<h1>SQL-syntax tests</h1>

<p> These tests check whether your Datacenter installation is producing
SQL suited to your RDBMS backend.  
</p>

<p>The actual queries being produced are mostly nonsensical;  these tests 
are purely to check that the translation process produces syntactially 
valid SQL.
</p>

<p><strong>Note: Please scroll down the page to 
<a href="#NotesSection">here</a> to view a detailed commentary about
these tests, and about customising your SQL translation to fix 
any broken tests.
</strong></p>

<a name="ResultsSection"><h1>Test Results</h1></a>
<%= new InstallationSyntaxCheck().runAllTests() %>

<hr/>

<a name="NotesSection"><h1>Reference Notes</h1></a>
<p>
This page tests your configuration to ensure that input ADQL/S queries
can be successfully translated into SQL and run against your RDBMS.
</p>


<p>
Note that the example queries are constructed from templates, using
the following properties from your configuration to set the name of the
RDBMS table to run the query on, and the name of the RA and DEC columns 
in that table:
</p>
<ul>
<li><tt> datacenter.self-test.table </tt></li>
<li><tt> datacenter.self-test.column1 </tt></li>
<li><tt> datacenter.self-test.column2 </tt></li>
</ul>
<p>
If these properties are not correctly set, the SQL queries produced 
from the ADQL/XML inputs will not run successfully against your RDBMS
(although the SQL may still be syntactically valid).
</p>

<h3>Complete failure</h3>
<p>
If all of these queries are failing to run, check your RDBMS 
connection settings (the Self Test page may help you to debug this, and 
you can also check your tomcat logs).  Also check that the properties mentioned
above are set correctly.
</p>

<h3>Partial failure</h3>
<p>
If only some of these queries are failing to run, you may be using a RDBMS 
that is not 100% compatible with your SQL translation functionality.
</p>

<p>
If you are using a customised (e.g. locally-built) plugin for RDBMS access, 
you will need to investigate the SQL generated by this plugin and fix it
where required.
</p>

<p>
More commonly, if you are using the standard JDBC plugin for RDBMS access 
(i.e. your configuration sets the property <tt>datacenter.querier.plugin</tt>
 to the value <tt>org.astrogrid.tableserver.jdbc.JdbcPlugin</tt>, then the 
XSLT stylesheet you are using to translate the query from ADQL/XML to SQL is 
not fully compatible with your chosen RDBMS.   
</p>

<p>
The stylesheet to be used is 
specified by the <tt>datacenter.sqlmaker.xslt</tt> property.  If you are 
familiar with XSL transformations, you may wish to take a copy of the 
stylesheet you are currently using, and tweak it to suit your RDBMS's flavour 
of SQL.  Otherwise, please contact 
<a href="mailto:astrogrid_dsa@star.le.ac.uk">astrogrid_dsa@star.le.ac.uk</a>
to report the problems you are encountering, including full details of the
RDBMS and stylesheet you are using and the errors you are seeing.
</p>

<p>
If you do produce customised stylesheets, the AstroGrid team would be 
glad to incorporate them into future distributions of this component;
please send them to 
<a href="mailto:astrogrid_dsa@star.le.ac.uk">astrogrid_dsa@star.le.ac.uk</a>.

</div>

<%@ include file="../footer.xml" %>
</body>
</html>


