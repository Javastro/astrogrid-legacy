import java.io.*;
import java.util.Calendar;
import java.util.Date;

import org.astrogrid.mySpace.mySpaceManager.*;

/**
 * Create example mySpace registry file.
 *
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 2.
 */

class CreateRegistry
{  public static void main (String argv[])
   {  Date creation = new Date();
        creation = Calendar.getInstance().getTime();

      DataItemRecord itemRec0 = new DataItemRecord("/ktn",
        1, "f1", "ktn", creation, creation, 99999, DataItemRecord.CON,
        "permissions");

      DataItemRecord itemRec1 = new DataItemRecord("/ktn/con1",
        2, "f2", "ktn", creation, creation, 99999, DataItemRecord.CON,
        "permissions");

      DataItemRecord itemRec2 = new DataItemRecord("/ktn/con1/table1",
        3, "f3", "ktn", creation, creation, 99999, DataItemRecord.VOT,
        "permissions");

      DataItemRecord itemRec3 = new DataItemRecord("/ktn/con1/table2",
        4, "f4", "ktn", creation, creation, 99999, DataItemRecord.VOT,
        "permissions");

      DataItemRecord itemRec4 = new DataItemRecord("/ktn/con2",
        5, "f5", "ktn", creation, creation, 99999, DataItemRecord.CON,
        "permissions");

      DataItemRecord itemRec5 = new DataItemRecord("/ktn/con2/table3",
        6, "f6", "ktn", creation, creation, 99999, DataItemRecord.VOT,
        "permissions");


      DataItemRecord itemRec6 = new DataItemRecord("/clq",
        7, "f7", "clq", creation, creation, 99999, DataItemRecord.CON,
        "permissions");

      DataItemRecord itemRec7 = new DataItemRecord("/clq/con1",
        8, "f8", "clq", creation, creation, 99999, DataItemRecord.CON,
        "permissions");

      DataItemRecord itemRec8 = new DataItemRecord("/clq/con1/table1",
        9, "f9", "ktn", creation, creation, 99999, DataItemRecord.VOT,
        "permissions");


      DataItemRecord itemRec9 = new DataItemRecord("/acd",
        10, "f10", "acd", creation, creation, 99999, DataItemRecord.CON,
        "permissions");

      DataItemRecord itemRec10 = new DataItemRecord("/acd/table1",
        11, "f11", "acd", creation, creation, 99999, DataItemRecord.VOT,
        "permissions");




      RegistryManager reg = new RegistryManager("example", "new");

      reg.addDataItemRecord(itemRec8);
      reg.addDataItemRecord(itemRec7);
      reg.addDataItemRecord(itemRec6);
      reg.addDataItemRecord(itemRec5);
      reg.addDataItemRecord(itemRec4);
      reg.addDataItemRecord(itemRec3);
      reg.addDataItemRecord(itemRec2);
      reg.addDataItemRecord(itemRec1);
      reg.addDataItemRecord(itemRec0);

//      reg.addDataItemRecord(itemRec9);
//      reg.addDataItemRecord(itemRec10);

      reg.finalize();
   }
}
