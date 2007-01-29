/*$Id: SpringLayoutHelper.java,v 1.2 2007/01/29 11:11:37 nw Exp $
 * Created on 08-Sep-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ui.comp;

import java.awt.Component;
import java.awt.Container;

import javax.swing.Spring;
import javax.swing.SpringLayout;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** Class of helper code for phil's spring layout stuff
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 08-Sep-2005
 *
 */
public class SpringLayoutHelper {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(SpringLayoutHelper.class);

    /** Construct a new SpringLayoutHelper
     * 
     */
    private SpringLayoutHelper() {
        super();
    }

    /**
       * Aligns the first <code>rows</code> * <code>cols</code>
       * components of <code>parent</code> in
       * a grid. Each component in a column is as wide as the maximum
       * preferred width of the components in that column;
       * height is similarly determined for each row.
       * The parent is made just big enough to fit them all.
       *
       * @param rows number of rows
       * @param cols number of columns
       * @param initialX x location to start the grid at
       * @param initialY y location to start the grid at
       * @param xPad x padding between cells
       * @param yPad y padding between cells
       */
      public static void makeCompactGrid(Container parent,
                                  int rows, int cols,
                                  int initialX, int initialY,
                                  int xPad, int yPad) {
          SpringLayout layout;
          try {
              layout = (SpringLayout)parent.getLayout();
          } catch (ClassCastException exc) {
              logger.error("The first argument to makeCompactGrid must use SpringLayout.");
              return;
          }
    
          //Align all cells in each column and make them the same width.
          Spring x = Spring.constant(initialX);
          for (int c = 0; c < cols; c++) {
              Spring width = Spring.constant(0);
              for (int r = 0; r < rows; r++) {
                  width = Spring.max(width,
                                     getConstraintsForCell(r, c, parent, cols).
                                         getWidth());
              }
              for (int r = 0; r < rows; r++) {
                  SpringLayout.Constraints constraints =
                          getConstraintsForCell(r, c, parent, cols);
                  constraints.setX(x);
                  constraints.setWidth(width);
              }
              x = Spring.sum(x, Spring.sum(width, Spring.constant(xPad)));
          }
    
          //Align all cells in each row and make them the same height.
          Spring y = Spring.constant(initialY);
          for (int r = 0; r < rows; r++) {
              Spring height = Spring.constant(0);
              for (int c = 0; c < cols; c++) {
                  height = Spring.max(height,
                                      getConstraintsForCell(r, c, parent, cols).
                                          getHeight());
              }
              for (int c = 0; c < cols; c++) {
                  SpringLayout.Constraints constraints =
                          getConstraintsForCell(r, c, parent, cols);
                  constraints.setY(y);
                  constraints.setHeight(height);
              }
              y = Spring.sum(y, Spring.sum(height, Spring.constant(yPad)));
          }
    
          //Set the parent's size. 
          SpringLayout.Constraints pCons = layout.getConstraints(parent);
          pCons.setConstraint(SpringLayout.SOUTH, y);
          pCons.setConstraint(SpringLayout.EAST, x);
      }
      
      /* Used by makeCompactGrid. */
      private static SpringLayout.Constraints getConstraintsForCell(
                                                  int row, int col,
                                                  Container parent,
                                                  int cols) {
          SpringLayout layout = (SpringLayout) parent.getLayout();
          Component c = parent.getComponent(row * cols + col);
          return layout.getConstraints(c);
      }

}


/* 
$Log: SpringLayoutHelper.java,v $
Revision 1.2  2007/01/29 11:11:37  nw
updated contact details.

Revision 1.1  2006/05/17 23:59:06  nw
documentaiton, and tweaks.

Revision 1.2  2006/04/18 23:25:43  nw
merged asr development.

Revision 1.1.56.1  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.1  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder
 
*/