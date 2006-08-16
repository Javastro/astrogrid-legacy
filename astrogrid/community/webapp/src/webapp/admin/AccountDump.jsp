<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.io.File,
                java.io.FileOutputStream,
                java.io.OutputStream,
                java.io.PrintWriter,
                java.util.ArrayList,
                java.util.Iterator,
                java.util.List,
                org.exolab.castor.jdo.Database,
                org.exolab.castor.jdo.Query,
                org.exolab.castor.jdo.QueryResults,
                org.astrogrid.community.common.policy.data.AccountData,
                org.astrogrid.community.server.security.data.PasswordData,
                org.astrogrid.community.server.service.CommunityServiceImpl"%>

<%
 //CommunityServiceImpl service = new CommunityServiceImpl();
 Database db = new CommunityServiceImpl().getDatabase();
 
 // Query to get a cursor on a list of AccountData.
 // Copy these objects into a List to preserve them
 // after the transaction finishes.
 db.begin();
 Query oql = db.getOQLQuery("CALL SQL SELECT ident,display,description,home,email FROM accounts AS org.astrogrid.community.common.policy.data.AccountData");
 QueryResults results = oql.execute();
 List accounts = new ArrayList();
 while(results.hasMore()) {
   accounts.add((AccountData)(results.next()));
 }
 db.rollback();

 // Work out where to put the output.
 String tmpDirName = System.getProperty("java.io.tmpdir");
 File tmpDir = new File(tmpDirName);
 File accountFile = new File(tmpDirName, "myproxy-account-transfer.txt");
 accountFile.createNewFile();
 OutputStream fos = new FileOutputStream(accountFile);
 PrintWriter pw = new PrintWriter(fos);
 
 // Process the raised AccountData.
 // For each one, raise the matching PasswordData and
 // print out the joined information.
 Iterator i = accounts.iterator();
 while (i.hasNext()) {
   AccountData account = (AccountData)(i.next());
   pw.println(account.getIdent()); 
   pw.println(account.getDisplayName());
   db.begin();
   PasswordData pwd = (PasswordData)(db.load(PasswordData.class, account.getIdent()));
   pw.println(pwd.getPassword());
   db.rollback();
 }
 
 // Finished with the file; slush it to disc.
 pw.flush();
 pw.close();
 
 // Finished with the database.
 db.close();
%>

<html>
<head>
<title>Accounts dumped to file</title>
</head>
<body>
<h1>Accounts dumped to file</h1>
<p>
The account data have been written to <%=accountFile.getAbsolutePath()%>.
</p>
</body>