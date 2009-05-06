<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html">
    <title>Upload user credentials to community</title>
    <style type="text/css" media="all">
      @import url("style/astrogrid.css");
    </style>
  </head>
  <body>
    <%@ include file="header.xml" %>
    <%@ include file="navigation.xml" %>
    <div id="bodyColumn">
    <h1>Uploading your credentials to the community</h1>
    <p>
      If you have better credentials than the community provides, you may
      upload them here. "Better" means more-widely accepted; typically,
      credentials that are valid on a large, computational grid such as EGEE
      or TeraGrid. By uploading your credentials, you can use them in
      virtual-observatory services that connect to the Grid.
    </p>
    <p>
      Uploading is a one-time operation. The community will keep a copy of your
      credentials for as long as you need them. Uploading does <em>not</em>
      mean that your credentials are shared with or exposed to other members of
      the community.
    </p>
    <p>
      To upload your credentials, you must present them packed in a <i>PKCS#12
      key-store</i>, commonly known as a <i>.p12</i> file (if you export your
      credentials from Firefox they are stored in PKCS#12 format).
    </p>
    <form action="user-credential-upload" method="post" enctype="multipart/form-data">
      <table>
        <tr>
          <td>Community user-name:</td><td><input name="accountname" type="text" size="64"/></td>
        </tr>
        <tr>
          <td>Community password:</td><td><input name="accountpassword" type="password" size="64"/></td>
        </tr>
        <tr>
          <td>Store alias:</td><td><input name="storealias" type="text" size="64"/></td>
        </tr>
        <tr>
          <td>Store password:</td><td><input name="storepassword" type="password" size="64"/></td>
        </tr>
        <tr>
          <td>Key-store:</td><td><input name="keystore" type="file" size="64"/></td>
        </tr>
        <tr>
          <td><input type="submit" value="Upload credentials"/></td><td/>
        </tr>
      </table>
    </form>
    </div>
  </body>
</html>
