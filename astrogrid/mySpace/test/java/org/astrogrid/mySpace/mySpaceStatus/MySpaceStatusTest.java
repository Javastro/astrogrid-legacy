package org.astrogrid.mySpace.mySpaceStatus;

import junit.framework.*;

public class MySpaceStatusTest extends TestCase
{

   public MySpaceStatusTest (String name)
   {  super(name);
   }

   public void testAddInfoMessage()
   {  MySpaceStatus msStatus= new
        MySpaceStatus("Test informational message", MySpaceMessage.INFO);

      Assert.assertTrue(msStatus.getSuccessStatus() );
      Assert.assertTrue(!msStatus.getWarningStatus() );
   }

   public void testAddWarnMessage()
   {  MySpaceStatus msStatus= new MySpaceStatus();
      msStatus.reset();
      msStatus.addMessage("Test warning message", MySpaceMessage.WARN);

      Assert.assertTrue(msStatus.getSuccessStatus() );
      Assert.assertTrue(msStatus.getWarningStatus() );
   }

   public void testAddErrorMessage()
   {  MySpaceStatus msStatus= new MySpaceStatus();
      msStatus.reset();
      msStatus.addMessage("Test error message", MySpaceMessage.ERROR);

      Assert.assertTrue(!msStatus.getSuccessStatus() );
      Assert.assertTrue(!msStatus.getWarningStatus() );
   }

   public static void main (String[] args)
   {  
//    junit.textui.TestRunner.run (MySpaceStatusTest.class);
      junit.swingui.TestRunner.run (MySpaceStatusTest.class);
   }


}
