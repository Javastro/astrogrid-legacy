/**
 * $Id: DefaultCellHtmlRenderer.java,v 1.1.1.1 2005/02/16 15:02:46 mch Exp $
 */
package org.astrogrid.storebrowser.html;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.Icon;
import javax.swing.table.TableModel;

/**
 * Renders a StoreNode in HTML for a table cell
 */

public class DefaultCellHtmlRenderer implements CellHtmlRenderer {
   
   private static DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
   

   /** renders the given TreeNode in html */
   public String htmlCell(TableModel table, int row, int column) throws IOException {

      Object o = table.getValueAt(row, column);

      if (o==null) {
         return "";
      }
      else if (o instanceof Date) {
         return date( (Date) o);
      }
      else {
         return o.toString();
      }
   }
   
   
   public String date(Date d) {
      if (d==null) {
         return "";
      }
      else {
         return dateFormat.format(d);
      }
   }

}
