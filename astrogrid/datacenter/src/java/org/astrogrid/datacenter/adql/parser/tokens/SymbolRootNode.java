package  org.astrogrid.datacenter.parser.tokens;

import java.io.*;

/**
 * This class is a special case of a <code>SymbolNode</code>. A
 * <code>SymbolRootNode</code> object has no symbol of its
 * own, but has children that represent all possible symbols.
 *
 * @author Steven J. Metsker
 *
 * @version 1.0
 */
public class SymbolRootNode
    extends SymbolNode {
  protected SymbolNode[] children = new SymbolNode[256];

  //created a initial node
  public SymbolRootNode() {
    super(null, (char) 0);
    init();
  }

  /**
   *
   * @param s String sequence to add
   */
  public void add(String s) {
    char c = s.charAt(0);
    SymbolNode n = ensureChildWithChar(c);
    n.addDescendantLine(s.substring(1));
    findDescendant(s).setValid(true);
  }

  /**
   *
   * @return String
   */
  public String ancestry() {
    return "";
  }

  /**
   *
   * @param c char
   * @return SymbolNode
   */
  protected SymbolNode findChildWithChar(char c) {
    return children[c];
  }

  /**
   *
   */
  protected void init() {
    int len = children.length;
    for (char i = 0; i < len; i++) {
      children[i] = new SymbolNode(this, i);
      children[i].setValid(true);
    }
  }

  /**
   *
   * @param r PushbackReader
   * @param first int
   * @throws IOException
   * @return String
   */
  public String nextSymbol(PushbackReader r, int first) throws IOException {

    SymbolNode n1 = findChildWithChar( (char) first);
    SymbolNode n2 = n1.deepestRead(r);
    SymbolNode n3 = n2.unreadToValid(r);
    return n3.ancestry();
  }
}
