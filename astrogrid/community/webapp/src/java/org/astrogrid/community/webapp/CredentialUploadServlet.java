package org.astrogrid.community.webapp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.AccessControlException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.astrogrid.community.server.sso.CredentialStore;

/**
 * A servlet to receive user-credentials uploaded to the community.
 *
 * @author Guy Rixon
 */
public class CredentialUploadServlet extends HttpServlet {
  
  private CredentialStore store;

  /**
   * Sets up the credential store according to the service configuration.
   */
  @Override
  public void init() {
    try {
      store = new CredentialStore();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /** 
   * Handles the HTTP <code>POST</code> method.
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  @Override
  protected void doPost(HttpServletRequest  request,
                        HttpServletResponse response) throws IOException,
                                                             ServletException {
    try {
      doPkcs12(request, response);
    }
    catch (AccessControlException e) {
      response.sendError(response.SC_FORBIDDEN, e.getMessage());
    }
    catch (FileUploadException e) {
      response.sendError(response.SC_BAD_REQUEST, e.getMessage());
    }

  }

  protected void doPkcs12(HttpServletRequest  request,
                          HttpServletResponse response) throws AccessControlException,
                                                               IOException,
                                                               FileUploadException,
                                                               ServletException {
    // Extract the relevant items from the request. request.getParameter() is
    // broken here because this is a multipart request.
    File keyStore = null;
    InputStream storeStream = null;
    String accountName = null;
    String accountPassword = null;
    String storeAlias = null;
    String storePassword = null;
    FileItemFactory factory = new DiskFileItemFactory();
    ServletFileUpload upload = new ServletFileUpload(factory);
    List<FileItem> items;
    items = upload.parseRequest(request);

    for (FileItem f : items) {
      if ("keystore".equals(f.getFieldName())) {
        keyStore = File.createTempFile(storeAlias, ".p12");
        keyStore.deleteOnExit();
        try {
          f.write(keyStore);
        }
        catch (Exception e) {
          throw new ServletException("Failed to read the key-store", e);
        }
      }
      else if ("accountname".equals(f.getFieldName())) {
        accountName = f.getString();
      }
      else if ("accountpassword".equals(f.getFieldName())) {
        accountPassword = f.getString();
      }
       else if ("storealias".equals(f.getFieldName())) {
        storeAlias = f.getString();
      }
      else if ("storepassword".equals(f.getFieldName())) {
        storePassword = f.getString();
      }
    }
    if (accountName == null) {
      throw new AccessControlException("Account name is missing.");
    }
    if (accountPassword == null) {
      throw new AccessControlException("Account password is missing.");
    }
    if (storeAlias == null) {
      throw new AccessControlException("Store alias is missing.");
    }
    if (storePassword == null) {
      throw new AccessControlException("Store password is missing.");
    }
    if (store == null) {
      throw new AccessControlException("Key-store is missing.");
    }

    // Check that the user is allowed to upload in this name.
    store.authenticate(accountName, accountPassword);

    // If all is well so far, capture, unpack and store the credentials.
    try {
      store.loadPkcs12(keyStore.getAbsolutePath(),
                       storeAlias,
                       storePassword, 
                       accountName,
                       accountPassword);
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
