package  org.astrogrid.datacenter.parser.tokens;

/**
 *
 * <p>Title: SQL to ADQL Parser</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Queen's University Belfast</p>
 * @author Pedro Contreras
 * @version 1.0
 */
import java.io.*;

public class NumberState
    extends TokenizerState {
  protected int c;
  protected double value;
  protected boolean absorbedLeadingMinus;
  protected boolean absorbedDot;
  protected boolean gotAdigit;

  /**
   * Convert a stream of digits into a number, making this
   * number a fraction if the boolean parameter is true.
   * @param r PushbackReader
   * @param fraction boolean
   * @throws IOException
   * @return double
   */
  protected double absorbDigits(PushbackReader r, boolean fraction) throws
      IOException {
    int divideBy = 1;
    double v = 0;
    while ('0' <= c && c <= '9') {
      gotAdigit = true;
      v = v * 10 + (c - '0');
      c = r.read();
      if (fraction) {
        divideBy *= 10;
      }
    }
    if (fraction) {
      v = v / divideBy;
    }
    return v;
  }

  /**
   * Return a number token from a reader
   * @param r PushbackReader
   * @param cin int
   * @param t Tokenizer
   * @throws IOException
   * @return Token
   */
  public Token nextToken(PushbackReader r, int cin, Tokenizer t) throws
      IOException {
    reset(cin);
    parseLeft(r);
    parseRight(r);
    r.unread(c);
    return value(r, t);
  }

  /**
   * Parse up to a decimal point
   * @param r PushbackReader
   * @throws IOException
   */
  protected void parseLeft(PushbackReader r) throws IOException {
    if (c == '-') {
      c = r.read();
      absorbedLeadingMinus = true;
    }
    value = absorbDigits(r, false);
  }

  /**
   * Parse from a decimal point to the end of the number
   * @param r PushbackReader
   * @throws IOException
   */
  protected void parseRight(PushbackReader r) throws IOException {
    if (c == '.') {
      c = r.read();
      absorbedDot = true;
      value += absorbDigits(r, true);
    }
  }

  /**
   * Prepare to assemble a new number
   * @param cin int
   */
  protected void reset(int cin) {
    c = cin;
    value = 0;
    absorbedLeadingMinus = false;
    absorbedDot = false;
    gotAdigit = false;
  }

  /**
   * Put together the pieces of a number.
   * @param r PushbackReader
   * @param t Tokenizer
   * @throws IOException
   * @return Token
   */
  protected Token value(PushbackReader r, Tokenizer t) throws IOException {
    if (!gotAdigit) {
      if (absorbedLeadingMinus && absorbedDot) {
        r.unread('.');
        return t.symbolState().nextToken(r, '-', t);
      }
      if (absorbedLeadingMinus) {
        return t.symbolState().nextToken(r, '-', t);
      }
      if (absorbedDot) {
        return t.symbolState().nextToken(r, '.', t);
      }
    }
    if (absorbedLeadingMinus) {
      value = -value;
    }
    return new Token(Token.TT_NUMBER, "", value);
  }
}
