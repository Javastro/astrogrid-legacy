package org.astrogrid.desktop.modules.ag.vfs.myspace;

import junit.framework.TestCase;
import org.apache.commons.vfs.FileType;
import org.astrogrid.store.Ivorn;

/**
 * JUnit-3 tests for {@link org.astrogrid.desktop.modules.ag.vfs.myspace.MyspaceFileName}.
 *
 * @author Guy Rixon
 */
public class MySpaceFileNameTest extends TestCase {
    
  public void testGetIvorn() throws Exception {
      MyspaceFileName sut = new MyspaceFileName("what.ever",
                                                "vospace",
                                                "user-000",
                                                FileType.FOLDER);
      Ivorn i = sut.getIvorn();
      assertEquals("ivo://what.ever/vospace#user-000", i.toString());
  }
    
}
