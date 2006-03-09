package org.eurovotech.quaestor.repl;

import java.io.PushbackReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.UnknownServiceException;
import java.net.URL;

/**
 * A helper class which provides command-line access to the REPL in Quaestor.
 * <p>Sexps typed in at the prompt are PUT to the Quaestor REPL.
 */
public class QuaestorReplClient {

    private static final String prompt = "quaestor> ";

    private PushbackReader stdin;
    private URL serverURL;

    public QuaestorReplClient(String host, int port)
            throws MalformedURLException {
        serverURL = new URL("http", host, port, "/quaestor/code");
    }

    /**
     * Read a sexp from the stdin.  Will return a parenthesised expression or
     * a single other atom delimited by whitespace.
     * @return string sexp, or null on EOF
     * @throws IOException if the input is malformed (ie, too many right
     * parentheses)
     */
    public String readSexp() 
            throws IOException {
        if (stdin == null)
            stdin = new PushbackReader
                    (new BufferedReader
                     (new InputStreamReader(System.in)));
        System.out.print(prompt);

        int chint;
        char ch;
        do {
            chint = stdin.read();
            if (chint < 0)
                return null;        // EOF
            ch = (char)chint;
        } while (Character.isWhitespace(ch));

        stdin.unread(chint);
        if (ch == '(') 
            return readSexpList();
        else
            return readSexpOther();
    }

    /**
     * Return a single list from the input stream.
     * @return string sexp, or null on EOF
     * @throws IOException if the input is malformed (ie, too many right
     * parentheses)
     */
    private String readSexpList()
            throws IOException {

        int chint;
        char ch;

        do {
            if ((chint = stdin.read()) < 0)
                return null;    // EOF
            ch = (char)chint;
        } while (Character.isWhitespace(ch));
        stdin.unread(chint);
        
        StringBuffer sexp = new StringBuffer();
        int nesting = 0;
        do {
            if ((chint = stdin.read()) < 0)
                return null; // EOF
            ch = (char)chint;
            if (ch == '(')
                nesting++;
            else if (ch == ')')
                nesting--;
            if (nesting < 0)
                throw new IOException
                        ("Malformed input -- too many right parentheses");
            sexp.append(ch);
        } while (nesting > 0);
        return sexp.toString();
    }

    /**
     * Read a non-list from the input.  Read until whitespace, leaving the 
     * stream positioned at the first whitespace character.
     * @return trimmed string containing the next object in the stream, 
     * or null on EOF
     */
    private String readSexpOther() 
            throws IOException {
        int chint;
        char ch;
        StringBuffer str = new StringBuffer();
        do {
            if ((chint = stdin.read()) < 0)
                return null;
            ch = (char)chint;
        } while (Character.isWhitespace(ch));
        do {
            str.append(ch);
            if ((chint = stdin.read()) < 0)
                return str.toString();
            ch = (char)chint;
        } while (!Character.isWhitespace(ch));
        stdin.unread(chint);
        return str.toString();
    }

    /**
     * Send a single sexp to the Quaestor server, and return the value
     * which comes back.
     */
    public String sendToServerREPL(String sexp) {
        StringBuffer value = new StringBuffer();
        try {
            HttpURLConnection urlConnection
                    = (HttpURLConnection)serverURL.openConnection();
            urlConnection.setRequestMethod("PUT");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            OutputStreamWriter wr
                    = new OutputStreamWriter(urlConnection.getOutputStream());
            wr.write(sexp);
            wr.flush();

            String line;
            BufferedReader rd
                    = new BufferedReader
                    (new InputStreamReader(urlConnection.getInputStream()));
            while ((line = rd.readLine()) != null)
                value.append(line).append('\n');
            wr.close();
            rd.close();

        } catch (UnknownServiceException e) {
            System.err.println("Unknown service exception talking to server REPL: " + e);
        } catch (IOException e) {
            System.err.println("IOException talking to server REPL: " + e);
        }
        return value.toString();
    }

    /**
     * Main command-line function.  No args at present, so it always
     * connects to the same server (localhost:8080)
     */
    public static void main(String[] args) {
        String sexp;
        try {
            QuaestorReplClient client
                    = new QuaestorReplClient("localhost", 8080);

            do {
                try {
                    sexp = client.readSexp();
                    if (sexp == null)
                        break;      // JUMP OUT
                    System.out.println(client.sendToServerREPL(sexp));
                } catch (IOException e) {
                    System.err.println("Bad input: " + e);
                }
            } while (true);
        } catch (MalformedURLException e) {
            System.err.println("Can't create Quaestor client!: " + e);
            System.exit(1);
        }
    }

}
