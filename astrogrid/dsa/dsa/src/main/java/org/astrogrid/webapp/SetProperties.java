/*
 * $Id: SetProperties.java,v 1.1 2009/05/13 13:20:56 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.webapp;

import java.io.IOException;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.cfg.ConfigFactory;

/**
 * A servlet that can be called from JSPs etc to set properties.  BEWARE that
 * this obviously can create a nasty huge security hole.  Make sure that all
 * your setup stuff - but particularly this servlet, which is used to make the
 * actual changes - are suitably locked away behind a login
 * <p>
 * Also provides a convenience routine for generating the input fields with
 * the right names and values that will make suitable parameters for the servlet
 *
 * @author M Hill
 */

public class SetProperties extends DefaultServlet
{
   /**
    * Takes the multi-value parameter 'set' which has values corresponding to
    * the property keys to set.  Each parameter key should then reference a value.
    * Not sure how to get descriptions in here yet...
    * <p>
    * set the 'forwardTo' parameter to set which page should be automatically
    * loaded when this servlet has finished
    */
   public void doGet(HttpServletRequest request,
                     HttpServletResponse response) throws ServletException, IOException {

      String[] sets = request.getParameterValues("set");
      
      for (int i = 0; i < sets.length; i++) {
         String key = sets[i];
         ConfigFactory.getCommonConfig().setProperty(key, request.getParameter(key));
      }
      
      //forward
      String targetUrl = request.getParameter("returnTo");
      request.getRequestDispatcher(targetUrl).forward(request, response);
//      response.getWriter().write("<META HTTP-EQUIV='Refresh' CONTENT='0;URL="+targetUrl+"'>");
   }
   
   public static String makePropertyInput(String key, String description, String defaultValue) {

      if ((description == null) || (description.trim().length()==0)) {
         //look in JNDI for description?
      }
      
      String value = ConfigFactory.getCommonConfig().getString(key, defaultValue);
      
      return
         "<input type='checkbox' name='set' value='"+key+"' />"+
         "<input type='text' name='key' value='"+key+"' readonly='readonly' />"+
         "<input type='text' name='description' value='"+description+"' readonly='readonly' />"+
         "<input type='text' name='"+key+"' value='"+value+"'/>";
  
   }
   
}

