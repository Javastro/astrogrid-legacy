package  org.astrogrid.datacenter.parser.tokens;

import java.io.*;

public class Tokenizer {
  /**
   * A Token divided into string, this is the main class
   * which is refer to chunck a String with the direfent token states
   *
   */
  protected PushbackReader reader;
  protected static final int DEFAULT_SYMBOL_MAX = 4;
  protected TokenizerState[] characterState = new TokenizerState[256];
  protected NumberState numberState = new NumberState();
  protected QuoteState quoteState = new QuoteState();
  protected SlashState slashState = new SlashState();
  protected SymbolState symbolState = new SymbolState();
  protected WhitespaceState whitespaceState = new WhitespaceState();
  protected WordState wordState = new WordState();
  public Tokenizer() {
    setCharacterState(0, 255, symbolState()); // the default
    setCharacterState(0, ' ', whitespaceState());
    setCharacterState('a', 'z', wordState());
    setCharacterState('A', 'Z', wordState());
    setCharacterState(0xc0, 0xff, wordState());
    setCharacterState('0', '9', numberState());
    setCharacterState('-', '-', numberState());
    setCharacterState('.', '.', numberState());
    setCharacterState('"', '"', quoteState());
    setCharacterState('\'', '\'', quoteState());
    setCharacterState('/', '/', slashState());
  }

  /**
   * Constructor
   * @param s String
   */
  public Tokenizer(String s) {
    this();
    setString(s);
  }

  /**
   *
   * @return PushbackReader
   */
  public PushbackReader getReader() {
    return reader;
  }

  /**
   *
   * @throws IOException
   * @return Token next token
   */
  public Token nextToken() throws IOException {
    int c = reader.read();
    if (c >= 0 && c < characterState.length) {
      return characterState[c].nextToken(reader, c, this);
    }
    return Token.EOF;
  }

  /**
   *
   * @return NumberState
   */
  public NumberState numberState() {
    return numberState;
  }

  /**
   *
   * @return QuoteState
   */
  public QuoteState quoteState() {
    return quoteState;
  }

  /**
   *
   * @param from int
   * @param to int
   * @param state TokenizerState
   */
  public void setCharacterState(
      int from, int to, TokenizerState state) {

    for (int i = from; i <= to; i++) {
      if (i >= 0 && i < characterState.length) {
        characterState[i] = state;
      }
    }
  }

  /**
   *
   * @param r PushbackReader
   */
  public void setReader(PushbackReader r) {
    this.reader = r;
  }

  /**
   *
   * @param s String
   */
  public void setString(String s) {
    setString(s, DEFAULT_SYMBOL_MAX);
  }

  /**
   *
   * @param s String
   * @param symbolMax int
   */
  public void setString(String s, int symbolMax) {
    setReader(
        new PushbackReader(new StringReader(s), symbolMax));
  }

  /**
   *
   * @return SlashState
   */
  public SlashState slashState() {
    return slashState;
  }

  /**
   *
   * @return SymbolState
   */
  public SymbolState symbolState() {
    return symbolState;
  }

  /**
   *
   * @return WhitespaceState
   */
  public WhitespaceState whitespaceState() {
    return whitespaceState;
  }

  /**
   *
   * @return WordState
   */
  public WordState wordState() {
    return wordState;
  }
}
