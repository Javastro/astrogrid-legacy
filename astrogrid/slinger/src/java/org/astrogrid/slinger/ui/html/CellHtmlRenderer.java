/**
 * $Id: CellHtmlRenderer.java,v 1.2 2004/12/07 01:33:36 jdt Exp $
 */
package org.astrogrid.slinger.ui.html;
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
