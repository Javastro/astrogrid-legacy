package org.astrogrid.dataservice.service.vosi;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;

/**
 * A base for building VOSI servlets.
 * There is one servlet for each VOSI resource, and
 * each such servlet overrides {@link #output(String[], String, Writer)
 * to produce its particular data.
 * 
 * @author Guy Rixon
 */
public abstract class VosiServlet extends HttpServlet {

  /**
   * The instant at which the servlet was initialized.
   */
  private Date startTime;
  
  /**
   * Initializes the servlet. Records the start time so that service
   * up-time may later be reported.
   */
  @Override
  public void init() throws ServletException {
    this.startTime = new Date();
  }
  

  /**
   * Handles the HTTP <code>GET</code> method.
   * @param request servlet request
   * @param response servlet response
   */
  @Override
  protected void doGet(HttpServletRequest  request,
                       HttpServletResponse response) throws ServletException,
                                                            IOException {
    
    Writer writer = response.getWriter();

     response.setContentType("text/xml");
     response.setCharacterEncoding("UTF-8");
     writer.write("<?xml version='1.0' encoding='UTF-8'?>\n");

     String chosenCatalog = request.getParameter("COLLECTION");
     String[] catalogNames = TableMetaDocInterpreter.getCatalogNames();

     output(catalogNames, chosenCatalog, writer);
  }
  
  /**
   * Writes the VOSI document to the given stream.
   * 
   * @param catalogNames All the catalogues configured on this service.
   * @param chosenCatalog The name of the catalogue of choice (null means use all catalogues).
   * @param writer The output stream.
   * @throws ServletException On any failure.
   */
  protected abstract void output(String[] catalogNames,
                                 String   chosenCatalog,
                                 Writer   writer) throws IOException,
                                                         ServletException;

  /**
   * Reveals the time at which the web application was started.
   *
   * @return The instant of starting this servlet.
   */
  protected Date getStartTime() {
    return startTime;
  }

}
