package  org.astrogrid.datacenter.parser.parse;

import java.util.*;

public abstract class Assembly
    implements Enumeration, PCloneable {

  /*
   * a place to keep track of consumption progress
   */
  protected Stack stack = new Stack();
  protected PCloneable target;
  protected int index = 0;

  /**
   *
   * @return Object copy of a object
   */
  public Object clone() {
    try {
      Assembly ass = (Assembly)super.clone();
      ass.stack = (Stack) stack.clone();
      if (target != null) {
        ass.target = (PCloneable) target.clone();
      }
      return ass;
    }
    catch (CloneNotSupportedException e) {
      throw new InternalError();
    }
  }

  /**
   *
   * @param delimiter String
   * @return String
   */
  public abstract String consumed(String delimiter);

  /**
   *
   * @return String a delimeter
   */
  public abstract String defaultDelimiter();

  /**
   *
   * @return int index
   */
  public int elementsConsumed() {
    return index;
  }

  /**
   *
   * @return int element that have not been consumed
   */
  public int elementsRemaining() {
    return length() - elementsConsumed();
  }

  /**
   *
   * @return Stack
   */
  public Stack getStack() {
    return stack;
  }

  /**
   *
   * @return Object
   */
  public Object getTarget() {
    return target;
  }

  /**
   *
   * @return boolean
   */
  public boolean hasMoreElements() {
    return elementsConsumed() < length();
  }

  /**
   *
   * @return int
   */
  public abstract int length();

  /**
   *
   * @return Object
   */
  public abstract Object peek();

  /**
   *
   * @return Object
   */
  public Object pop() {
    return stack.pop();
  }

  /**
   *
   * @param o Object
   */
  public void push(Object o) {
    stack.push(o);
  }

  /**
   *
   * @param delimiter String
   * @return String
   */
  public abstract String remainder(String delimiter);

  /**
   *
   * @param target PCloneable
   */
  public void setTarget(PCloneable target) {
    this.target = target;
  }

  /**
   *
   * @return boolean
   */
  public boolean stackIsEmpty() {
    return stack.isEmpty();
  }

  /**
   *
   * @return String
   */
  public String toString() {
    String delimiter = defaultDelimiter();
    return stack + consumed(delimiter) + "^" + remainder(delimiter);
  }

  /**
   *
   * @param n int
   */
  public void unget(int i) {
    index -= i;
    if (index < 0) {
      index = 0;
    }
  }
}
