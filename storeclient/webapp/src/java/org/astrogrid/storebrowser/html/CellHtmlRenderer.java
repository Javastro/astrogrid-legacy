/**
 * $Id: CellHtmlRenderer.java,v 1.1 2005/02/16 15:02:46 mch Exp $
 */
package org.astrogrid.storebrowser.html;
import java.io.IOException;
import java.io.Writer;
import javax.swing.table.TableModel;

/**
 * Renders a StoreNode in HTML
 */

public interface CellHtmlRenderer  {

   /** renders the given TreeNode in html */
   public String htmlCell(TableModel table, int row, int cell) throws IOException;
   
}
