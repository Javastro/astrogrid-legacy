/**
 * $Id: TableHtmlRenderer.java,v 1.1 2005/02/16 15:02:46 mch Exp $
 */
package org.astrogrid.storebrowser.html;
import java.io.IOException;
import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.table.TableModel;

/**
 * Represents a TableModel in HTML
 */

public class TableHtmlRenderer  {
   TableModel model = null;
   
   /** person viewing these files */
   private Principal user = null;
   
   /** Node renderer */
   CellHtmlRenderer cellRenderer = new DefaultCellHtmlRenderer();
   
   public static final String hdrClr = "#BBBBBB";
  // public static final String hilitedClr = "#0000BB";

   /** Create a view onto files with the given root storefile */
   public TableHtmlRenderer(TableModel givenTable, Principal aUser) {
      this.model = givenTable;
      this.user = aUser;
   }

   public void setModel(TableModel newModel) {
      this.model = newModel;
   }
   
   public void setCellRenderer(CellHtmlRenderer newRenderer)
   {
      cellRenderer = newRenderer;
   }
   

   /** Writes the panel that contains the list of files */
   public void writeView(HttpServletRequest request, HttpServletResponse response) throws IOException {
 
      response.getWriter().print("\n<div id='files'>\n"+
                                    "<table width='100%'>\n");
      
      //write header
      response.getWriter().print("<tr bgcolor='#AAAAAA'>");
      for (int col = 0; col < model.getColumnCount(); col++) {
         response.getWriter().write("<th>"+model.getColumnName(col)+"</th>");
      }
      response.getWriter().write("</tr>\n");
      response.getWriter().flush();

      if ((model == null) || (model.getRowCount() == 0)) {
         response.getWriter().println("</table><p align='center'>No entries to list</p></div>");
         return;
      }

      //write rows
      for (int r=0;r<model.getRowCount();r++) {
         response.getWriter().write("<tr>");
         for (int c=0;c<model.getColumnCount();c++) {
            response.getWriter().write("<td>");
            response.getWriter().write(
               cellRenderer.htmlCell(model, r, c)
            );
            response.getWriter().write("</td>");
         }
         response.getWriter().write("</tr>");
      }
      
      response.getWriter().print("</table></div>\n");
      
   }
   
   
   
}
