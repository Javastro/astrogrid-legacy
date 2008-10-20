<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!-- TODO replace this with a servlet that actually reads the contents of the xdoc subfolder so that the individual JSPs do not have to be created-->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>${param.title}</title>
<%@ include file="../inc/header.jsp" %>
<!-- body -->
<div id="bodyColumn">

<div class="contentBox">
<!-- is there a more efficient way to do this? -->
  <c:import var="xml" url="./xdoc/${param['doc']}"/>
  <c:import var="xslt" url="./xdocTohtml.xsl"/>
   <x:transform xml="${xml}" xslt="${xslt}"/>
</div>
</div>
<%@ include file="../inc/footer.jsp"%>