package org.astrogrid.mySpace.mySpaceManager;

import java.util.*;

/**
 * Comparator for the DataItemRecord class.
 *
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 2.
 */

public class DataItemRecordComparator implements Comparator
{  

   public int compare(Object item1, Object item2)
   {  DataItemRecord dataItem1 = (DataItemRecord)item1;
      DataItemRecord dataItem2 = (DataItemRecord)item2;

      String name1 = dataItem1.getDataItemName();
      String name2 = dataItem2.getDataItemName();

      return name1.compareTo(name2);
   }
}
