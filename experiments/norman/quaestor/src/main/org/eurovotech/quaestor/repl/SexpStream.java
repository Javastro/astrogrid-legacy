package org.eurovotech.quaestor.repl;

import java.io.PushbackReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * This stream translates an input stream or reader into a sequence of
 * sexps, which are balanced lists, strings, or schemely identifiers.
 * It isn't a full Scheme tokenizer, since that is best left to the
 * Scheme reader which will receive the expressions which are read, and this
 * is only intended to ensure that we read expressions from the stream
 * one at a time.  The goal is not to parse scheme expressions fully, not to
 * detect malformed expressions, but simply to avoid creating new errors.
 *
 * <p>Despite that limited goal, it would probably be best to use a proper
 * tokenizer here, as that'll get round edge-cases more reliably than the 
 * burgeoning set of special-cases below.
 */
public class SexpStream {
    private PushbackReader in = null;

    public SexpStream(java.io.InputStream inputStream) {
        in = new PushbackReader
                (new BufferedReader
                 (new InputStreamReader(inputStream)));
        assert in != null;
    }

    public SexpStream(java.io.Reader inputReader) {
        in = new PushbackReader(new BufferedReader(inputReader));
        assert in != null;
    }
    
    /**
     * Read a sexp from the input stream.  Will return a parenthesised
     * expression, a string or a single identifier delimited by whitespace.
     * @return string sexp, or null on EOF
     * @throws IOException if the input is malformed (ie, too many left or
     * right parentheses)
     */
    public String readSexp() 
            throws IOException {
        assert in != null;

        int chint;
        char ch;
        do {
            if ((chint = in.read()) < 0)
                return null;        // EOF
            ch = (char)chint;
        } while (Character.isWhitespace(ch));
        if (ch == ')')
            throw new IOException
                    ("malformed input -- too many right parentheses");

        if (ch == '\\') {
            // escape character            
            if ((chint = in.read()) < 0)
                return null;    // (unexpected) EOF, but don't raise an error
            return "\\"+(char)chint;
        } else if (ch == ';') {
            skipComment();
            return readSexp();
        } else if (ch=='\'' || ch=='`' || ch=='@' || ch==',') {
            return String.valueOf(ch) + readSexp(); // not very efficient
        } else if (ch == '(') {
            in.unread(chint);       // push it back into the stream
            return readSexpList();
        } else if (ch == '"') {
            in.unread(chint);
            return readSexpString();
        } else {
            in.unread(chint);
            return readSexpOther();
        }
        
    }

    /**
     * Return a single list from the input stream.
     * The stream is positioned at the parenthesis which opens the list.
     * Leave the stream positioned after the parenthesis which closes the list.
     * @return string sexp, not null
     * @throws IOException if the input is malformed (ie, too many right
     * parentheses) or if the input ends unexpectedly (ie, too many left
     * parentheses)
     */
    private String readSexpList()
            throws IOException {
        assert in != null;

        int chint;
        char ch;

        StringBuffer sexp = new StringBuffer();
        int nesting = 0;
        do {
            if ((chint = in.read()) < 0)
                throw new IOException("Unexpected end of input -- too many left parentheses");
            ch = (char)chint;
            if (ch == '(') {
                nesting++;
                sexp.append(ch);
            } else if (ch == ')') {
                nesting--;
                sexp.append(ch);
            } else if (ch == '"') {
                in.unread(chint);
                sexp.append(readSexpString());
            } else if (ch == '\\') {
                // escape character            
                if ((chint = in.read()) < 0)
                    throw new IOException("Unexpected end of input -- last character was \\");
                sexp.append('\\').append((char)chint);
            } else if (ch == ';') {
                skipComment();
                sexp.append("\n");
            } else {
                sexp.append(ch);
            }
            if (nesting < 0)
                throw new IOException
                        ("Malformed input -- too many right parentheses");
        } while (nesting > 0);
        return sexp.toString();
    }

    /**
     * Read a string from the input.  The stream is positioned at the
     * opening quotation mark.  Leave the stream positioned at the first
     * character after the end-quote.  Allow a sequence <code>\"</code> to
     * be present in the string without terminating it.
     * @return the string read from the input, including its quotation marks,
     * not null
     * @throws IOException if the stream is malformed, so that there is
     * no end-quote before EOF
     */
    private String readSexpString()
            throws IOException {
        assert in != null;

        int chint;
        char ch;
        StringBuffer sb = new StringBuffer("\"");

        chint = in.read();      // should be '"'
        assert chint >= 0 && (char)chint == '"';

        do {
            if ((chint = in.read()) < 0)
                throw new IOException
                        ("Unexpected end of input before end-quote");
            ch = (char)chint;
            sb.append(ch);
            if (ch == '\\') {
                // append escaped character (this only makes a difference
                // if the character in question is a quote, which mustn't
                // end this string)
                if ((chint = in.read()) < 0)
                    throw new IOException
                            ("Unexpected end of string after backslash");
                sb.append((char)chint);

                // ...and again, to get the character after a possible quote
                if ((chint = in.read()) < 0)
                    throw new IOException
                            ("Unexpected end of string after backslash");
                ch = (char)chint;
                sb.append(ch);
            }
        } while (ch != '"');
        return sb.toString();
    }

    /**
     * Read a non-list from the input.  The stream is positioned at the 
     * non-whitespace character which starts the expression.
     * Read until whitespace, leaving the 
     * stream positioned at the first whitespace character.
     * @return trimmed string containing the next object in the stream, 
     * or null on EOF
     * @throws IOException if there is an error eading the stream
     */
    private String readSexpOther()
            throws IOException {
        assert in != null;

        int chint;
        char ch;
        StringBuffer str = new StringBuffer();

        chint = in.read();
        assert chint >= 0 && !Character.isWhitespace((char)chint);

        ch = (char)chint;
        do {
            str.append(ch);
            if ((chint = in.read()) < 0)
                return str.toString();
            ch = (char)chint;
        } while (!(Character.isWhitespace(ch)
                   || ch=='(' || ch==')'
                   || ch=='"' || ch==',' || ch=='\'' || ch=='`'));
        in.unread(chint);
        return str.toString();
    }

    /**
     * Skip comments, starting from semicolon to end of line.  If the stream
     * is ended by EOF, exit without error
     */
    private void skipComment()
            throws IOException {
        int chint;
        do {
            chint = in.read();
            if (chint < 0)
                return;         // JUMP OUT
        } while (chint != '\n' && chint != '\r');
        return;
    }
}
