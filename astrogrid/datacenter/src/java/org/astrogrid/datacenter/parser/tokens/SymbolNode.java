package parser.tokens;

import java.io.*;
import java.util.*;

public class SymbolNode {
  protected char myChar;
  protected Vector children = new Vector(); // of Node
  protected boolean valid = false;
  protected SymbolNode parent;

  /**
   *
   * @param parent SymbolNode
   * @param myChar char
   */
  public SymbolNode(SymbolNode parent, char myChar) {
    this.parent = parent;
    this.myChar = myChar;
  }

  /**
   *
   * @param s String
   */
  protected void addDescendantLine(String s) {
    if (s.length() > 0) {
      char c = s.charAt(0);
      SymbolNode n = ensureChildWithChar(c);
      n.addDescendantLine(s.substring(1));
    }
  }

  /**
   *
   * @return String
   */
  public String ancestry() {
    return parent.ancestry() + myChar;
  }

  /**
   *
   * @param r PushbackReader
   * @throws IOException
   * @return SymbolNode
   */
  protected SymbolNode deepestRead(PushbackReader r) throws IOException {

    char c = (char) r.read();
    SymbolNode n = findChildWithChar(c);
    if (n == null) {
      r.unread(c);
      return this;
    }
    return n.deepestRead(r);
  }

  /**
   *
   * @param c char
   * @return SymbolNode
   */
  protected SymbolNode ensureChildWithChar(char c) {
    SymbolNode n = findChildWithChar(c);
    if (n == null) {
      n = new SymbolNode(this, c);
      children.addElement(n);
    }
    return n;
  }

  /**
   *
   * @param c char
   * @return SymbolNode
   */
  protected SymbolNode findChildWithChar(char c) {
    Enumeration e = children.elements();
    while (e.hasMoreElements()) {
      SymbolNode n = (SymbolNode) e.nextElement();
      if (n.myChar == c) {
        return n;
      }
    }
    return null;
  }

  /**
   *
   * @param s String
   * @return SymbolNode
   */
  protected SymbolNode findDescendant(String s) {
    char c = s.charAt(0);
    SymbolNode n = findChildWithChar(c);
    if (s.length() == 1) {
      return n;
    }
    return n.findDescendant(s.substring(1));
  }

  /**
   *
   * @param b boolean
   */
  protected void setValid(boolean b) {
    valid = b;
  }

  /**
   *
   * @return String
   */
  public String toString() {
    return "" + myChar + '(' + valid + ')';
  }

  /**
   *
   * @param r PushbackReader
   * @throws IOException
   * @return SymbolNode
   */
  protected SymbolNode unreadToValid(PushbackReader r) throws IOException {

    if (valid) {
      return this;
    }
    r.unread(myChar);
    return parent.unreadToValid(r);
  }
}
