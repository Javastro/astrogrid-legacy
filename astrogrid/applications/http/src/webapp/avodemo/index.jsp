<?xml version="1.0" encoding="iso-8859-1"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.sql.*,org.astrogrid.applications.avodemo.*" errorPage="" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>AVO Demo - Photometric redshift determination</title>
<link href="http://vm06.astrogrid.org:8080/astrogrid-portal/test/portal.css" rel="stylesheet" type="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
</head>
<body>
<h2>AVO Demo - Photometric redshift determination
</h2>
<form action="runjob.jsp" enctype="application/x-www-form-urlencoded" method="get" name="jobsubmit">

<table width="90%" border="0" cellspacing="5" cellpadding="2">
  <tr>
    <td>
      <div align="right">choose sector</div>
    </td>
    <td>
      <select name="sector" id="sector">
        <option>sect11</option>
        <option>sect12</option>
        <option>sect13</option>
        <option>sect14</option>
        <option>sect21</option>
        <option>sect22</option>
        <option selected="selected">sect23</option>
        <option>sect24</option>
        <option>sect25</option>
        <option>sect31</option>
        <option>sect32</option>
        <option>sect33</option>
        <option>sect34</option>
        <option>sect35</option>
        <option>sect41</option>
        <option>sect42</option>
        <option>sect43</option>
        <option>sect44</option>
        <option>sect45</option>
        <option>sect52</option>
        <option>sect53</option>
      </select>
    </td>
  </tr>
  <tr>
    <td>
      <div align="right">north or south?</div>
    </td>
    <td>
      <select name="hemi" id="hemi">
      <option value="n">North</option>
      <option value="s" selected="selected">South</option>
      </select>
    </td>
  </tr>
  <tr>
    <td>
      <div align="right">email address to notify</div>
    </td>
    <td>
      <input name="email" type="text" id="email" size="40" />
    </td>
  </tr>
<tr><td>
<input name="Submit" type="submit" value="submit and run job" />
<input name="workflow" type="submit" value="create workflow" />
</td></tr>
</table>
</form>
<h3>CDF-S Section Layout</h3>    
<img src="ftp://archive.stsci.edu/pub/hlsp/goods/v1/h_cdfs_v1.0sects_plt.jpg" alt="CDF-S Section Layout"/>


<h3>HDF-N Section Layout</h3>    
<img src="ftp://archive.stsci.edu/pub/hlsp/goods/v1/h_hdfn_v1.0sects_plt.jpg" alt="HDF-N Section Layout"/>


</body>
</html>
