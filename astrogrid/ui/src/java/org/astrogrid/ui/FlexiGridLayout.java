/**
 * FlexiGridLayout.java
 *
 * It seems I often want a table-like layout, where the components are all
 * arranged in a grid, but where the columns can be of different widths.
 *

 * @author M Hill
 */

package org.astrogrid.ui;

import java.awt.*;

public class FlexiGridLayout extends GridBagLayout
{
   int numCols = 1;

   int addCol = 0;   //next column 'add' will use

// public final static String NEXT_LINE = "NEXT LINE";
   int vGap = 0;
   int hGap = 0;

   public FlexiGridLayout(int cols)
   {
      numCols = cols;
      addCol = 0;
   }

   /**
    * Override layoutManager2 method to add component in grid-like way
    */
   public void addLayoutComponent(Component aComponent, Object unusedConstraint)
   {
      GridBagConstraints c = new GridBagConstraints();
      c.fill = c.BOTH;
      c.weightx = 1;
      c.weighty = 1;
      c.insets = new Insets(vGap,hGap,vGap,hGap);
      c.gridx = addCol;
      if (addCol == numCols-1)
      {
         c.gridwidth = c.REMAINDER;
         addCol = 0;
      }
      else
      {
         addCol ++;
      }
      super.addLayoutComponent(aComponent, c);
   }

   public void setColWidth(int col, int newWidth)
   {
      if (columnWidths == null)
         columnWidths = new int[numCols];

      columnWidths[col] = newWidth;
   }

   /**
    * Sets the horizontal gap between components.  NB, must be set before
    * adding the component(s). Applies to both left and right gap.
    */
   public void setHGap(int gap)
   {
      hGap = gap;
   }

   /**
    * Sets the vertical gap between components.  NB, must be set before
    * adding the component(s).Applies to both top & bottom gap
    */
   public void setVGap(int gap)
   {
      vGap = gap;
   }

   public static void main(String[] args)
   {
      Frame f = new Frame();
      Panel p = new Panel();
      FlexiGridLayout l = new FlexiGridLayout(4);
      p.setLayout(l);
      p.add(new Label("Wibble"));
      p.add(new Label("Wibble"));
      p.add(new Label("Wibble"));
      p.add(new Label("Wibble"));
      p.add(new Label("Wibble"));
      p.add(new Label("Wibble"));
      p.add(new Label("Wibble"));
      p.add(new Label("Wibble"));
      p.add(new Label("Wibble"));
      p.add(new Label("Wibble"));
      f.add(p);
      f.setSize(300,300);
      l.setColWidth(2,100);
//    f.validate();
      f.show();
   }
}

