package org.astrogrid.common.creator;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class InstanceCreatorTestDummy {
  private String foo;
  private int bar;
  
  public static InstanceCreatorTestDummy getInstance() {
    return new InstanceCreatorTestDummy();
  }
  
  public static InstanceCreatorTestDummy getInstance(String foo, int bar) {
    return new InstanceCreatorTestDummy(foo, bar);
  }
  
  public String getFoo() {
    return foo;
  }
  
  public int getBar() {
    return bar;
  }
  
  private InstanceCreatorTestDummy() {
  }
  
  private InstanceCreatorTestDummy(String foo, int bar) {
    this.foo = foo;
    this.bar = bar;
  }
}
