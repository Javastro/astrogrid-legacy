/*
 * $Id GridBagHelper.java $
 *
 */

package org.astrogrid.ui;

import java.awt.GridBagConstraints;

/**
 * A set of static routines for helping fill out gridbagconstraints
 * for standard fields
 *
 * @author M Hill
 */


public class GridBagHelper
{
   /**
    * A convenience routine setting constraints suitable for a label
    * (full fill, low weighting, etc)
    */
   public static void setLabelConstraints(GridBagConstraints constraints)
   {
      constraints.fill = constraints.BOTH;
      constraints.gridheight = 1;
      constraints.gridwidth = 1;
      constraints.gridx = 0;
      constraints.weightx = 0;
      constraints.weighty = 0;
   }

   /**
    * A convenience routine setting constraints for a user entry component -
    * centers in its cell, fills horizontally, high weight
    */
   public static void setEntryConstraints(GridBagConstraints constraints)
   {
      constraints.anchor = constraints.CENTER;
      constraints.fill = constraints.HORIZONTAL;
      constraints.gridheight = 1;
      constraints.gridwidth = 1;
      constraints.gridx = 1;
      constraints.weightx = 1;
      constraints.weighty = 0;
   }

   /**
    * A convenience routine setting constraints for a supplementary control
    * (eg button for selecting a file).  centers in its cell, does not fill,
    * low weight
    */
   public static void setControlConstraints(GridBagConstraints constraints)
   {
      constraints.anchor = constraints.CENTER;
      constraints.fill = constraints.NONE;
      constraints.gridheight = 1;
      constraints.gridwidth = 1;
      constraints.gridx = 3;
      constraints.weightx = 0;
      constraints.weighty = 0;
   }

}

/*
$Log: GridBagHelper.java,v $
Revision 1.1  2003/08/25 18:36:32  mch
*** empty log message ***

Revision 1.1  2003/07/02 19:14:58  mch
Constraint-setting methods

*/
