package org.astrogrid.common.creator;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class CreatorTestDummy {
  private String foo;
  private int bar;
  
  public CreatorTestDummy() {
  }

  public CreatorTestDummy(String foo, int bar) {
    this.foo = foo;
    this.bar = bar;
  }

  public String getFoo() {
    return foo;
  }
  
  public int getBar() {
    return bar;
  }
}
