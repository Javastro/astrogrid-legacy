package org.eurovotech.quaestor.repl;

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
 * <p>Usage:
 * <pre>
 * java -cp ... org.eurovotech.quaestor.repl.QuaestorReplClient [options]
 * </pre>
 * <p>Sexps typed in at the prompt are evaluated in the Quaestor REPL
 * (this is done by sending them via a PUT request to the Quaestor servlet).
 * <p>See {@link #main} for details.
 */
public class QuaestorReplClient {

    private static final String prompt = "quaestor> ";

    private URL serverURL;

    public QuaestorReplClient(String host, int port)
            throws MalformedURLException {
        serverURL = new URL("http", host, port, "/quaestor/code");
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
                value.append(line); // .append('\n');
            wr.close();
            rd.close();

        } catch (UnknownServiceException e) {
            System.err.println
                    ("Unknown service exception talking to server REPL: " + e);
        } catch (IOException e) {
            System.err.println("IOException talking to server REPL: " + e);
        }
        return value.toString();
    }

    /**
     * Main command-line function.
     * <p>Usage:
     * <pre>
     * java -cp ... org.eurovotech.quaestor.repl.QuaestorReplClient [options]
     * </pre>
     * <p>Options:
     * <dl>
     * <dt>--host=&lt;hostname&gt;</dt>
     * <dd>server host, default=localhost</dd>
     * <dt>--port=&lt;port&gt;</dt>
     * <dd>server port, default=8080</dd>
     * </dl>
     */
    public static void main(String[] args) {
        String sexp;
        String quaestorHost = "localhost";
        int quaestorPort = 8080;

        java.util.regex.Pattern opt
                = java.util.regex.Pattern.compile("^--([^=]*)(?:=(.*))?");
        for (int i=0; i<args.length; i++) {
            java.util.regex.Matcher m = opt.matcher(args[i]);
            if (m.matches()) {
                String option = m.group(1);
                String value  = m.group(2);
                if (option.equals("host")) {
                    quaestorHost = value;
                } else if (option.equals("port")) {
                    try {
                        quaestorPort = Integer.parseInt(value);
                    } catch (NumberFormatException e) {
                        System.err.println("Badly formatted number: " + value);
                        Usage();
                    }
                } else {
                    Usage();
                }
            } else {
                Usage();
            }
        }

        try {
            QuaestorReplClient client
                    = new QuaestorReplClient(quaestorHost, quaestorPort);
            SexpStream in = new SexpStream(System.in);

            do {
                try {
                    System.out.print(prompt);
                    sexp = in.readSexp();
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

    private static void Usage() {
        System.err.println("Usage: QuaestorReplClient [options]");
        System.err.println("Options:");
        System.err.println("  --host=<hostname>  server host, default=localhost");
        System.err.println("  --port=<port>      server port, default=8080");
        System.exit(1);
    }
}
