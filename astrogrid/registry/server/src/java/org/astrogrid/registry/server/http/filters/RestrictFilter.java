package org.astrogrid.registry.server.http.filters;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.astrogrid.config.Config;

/**
 * Class: AdminCheckFilter
 * Description: Small filter to let you check and make sure the admin password is filled out for access to the database.
 * This is typically used for access to the database via a servlet example WebDav.  
 * Initial installations of databases may have a blank password and you do not want them to have access to the database
 * with a blank password.  This filter only checks the xmldb.admin.password.
 * 
 * @author Kevin Benson
 *
 */
public final class RestrictFilter implements Filter {


    private FilterConfig filterConfig = null;
    
    private static String contextURL = null;
    
    private static String adminPassword = null;
    
    public static Config conf = null;    
    
    static {
        if(conf == null) {
           conf = org.astrogrid.config.SimpleConfig.getSingleton();
        }
    }    
    

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        
      String ipFilter = conf.getString("reg.custom.restrict.ipaddresses",filterConfig.getInitParameter("restrictIPAddresses"));
      String []ipAddresses = null;
      
      if(ipFilter != null) {
          ipAddresses = ipFilter.split(",");
          String remoteAddr = request.getRemoteAddr();
          String remoteHost = request.getRemoteHost();
          for(int j = 0;j < ipAddresses.length;j++) {
              if(ipAddresses[j].trim().equals(remoteAddr) || ipAddresses[j].trim().equals(remoteHost) || 
                 remoteAddr.matches(ipAddresses[j].trim()) || remoteHost.matches(ipAddresses[j].trim())) {
                  chain.doFilter(request, response);
                  return;              
              }
          }//for
      }

      PrintWriter out = response.getWriter();
      out.print("<html><head><title>Access Error</title></head><body><font color='red'>You tried to access a resource that has restrictions where by only certain ip addresses may acquire the resource.</font></body></html>");
      out.close();
    }


    public void destroy() {
    }


    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }    
}