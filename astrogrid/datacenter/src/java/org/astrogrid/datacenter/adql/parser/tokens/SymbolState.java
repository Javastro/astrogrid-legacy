package  org.astrogrid.datacenter.parser.tokens;

import java.io.*;

public class SymbolState
    extends TokenizerState {
  /**
   * This class must be modify if other symbol is consider into Xadql parser
   * here 3 symbols are consider '!=', '<=', and '>='
   */
  SymbolRootNode symbols = new SymbolRootNode();
  public SymbolState() {
    add("!=");
    add("<=");
    add(">=");
  }

  /**
   *
   * @param s String Add multi char symbol
   */
  public void add(String s) {
    symbols.add(s);
  }

  /**
   * Return a symbol token from a reader
   * @param r PushbackReader
   * @param first int
   * @param t Tokenizer
   * @throws IOException
   * @return Token
   */
  public Token nextToken(PushbackReader r, int first, Tokenizer t) throws
      IOException {

    String s = symbols.nextSymbol(r, first);
    return new Token(Token.TT_SYMBOL, s, 0);
  }
}
