/*
 * $Id: FileServer.java,v 1.1 2009/05/13 13:20:56 gtr Exp $
 */

package org.astrogrid.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.io.Piper;
import org.astrogrid.webapp.DefaultServlet;

/**
 * Simple File Server - serves files on disk with a root given in config
 * fileserver.root and
 * relative path given by the parameter 'path'.  NB This can be a major
 * security problem if the config root is set too high and exposes subdirectories
 * you don't want - make sure all the files in the root down are suitable for
 * public access.
 *
 * The purpose of this class is to give access to FITS files etc for SIAP.
 *
 * @author mch
 */
public class FileServer extends DefaultServlet {
 
   public static final String ROOT_KEY = "fileserver.root";
   
   public void doGet(HttpServletRequest request, HttpServletResponse response)  throws IOException {
      
      String relPath = request.getParameter("path");
      if ((relPath==null) || (relPath.trim().length()==0)) {
         //do nothing, return nothing
         return;
      }

      String root = ConfigFactory.getCommonConfig().getString(ROOT_KEY);

      File source = new File(root+relPath);
      InputStream in = new FileInputStream(source);
      Piper.bufferedPipe(in, response.getOutputStream());
   }
}

