package org.eurovotech.quaestor.repl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.MalformedURLException;
import java.net.UnknownServiceException;
import java.net.URI;
import java.net.URL;

/**
 * A helper class which provides command-line access to the REPL in Quaestor.
 * <p>Usage:
 * <pre>
 * java -cp ... org.eurovotech.quaestor.repl.QuaestorReplClient [options]
 * </pre>
 * <p>Sexps typed in at the prompt are evaluated in the Quaestor REPL
 * (this is done by sending them via a POST request to the Quaestor servlet's
 * code handler).
 * <p>See {@link #main} for details.
 */
public class QuaestorReplClient {

    private String prompt = "#> ";

    private URI serverURI;
    private URL codeURL;

    public QuaestorReplClient(String host, int port, String servlet)
            throws URISyntaxException, MalformedURLException {
        serverURI = new URI("http", null, host, port, "/"+servlet, null, null);
        codeURL = serverURI.resolve("./code").toURL();
        prompt = host+":quaestor> ";
    }

    public QuaestorReplClient(URI uri) 
            throws MalformedURLException {
        serverURI = uri;
        if (serverURI.getRawPath() == null)
            codeURL = serverURI.resolve("/code").toURL();
        else
            codeURL = serverURI.resolve("./code").toURL();
        prompt = uri.getHost() + ":quaestor> ";
    }

    /**
     * Send a single sexp to the Quaestor server, and return the value
     * which comes back
     * @param sexp the scheme sexp to be evaluated
     * @return the value of the sexp, as a non-null (but possibly empty) string
     * @throws IOException if the sexp could not be evaluated for any reason, including
     *   scheme syntax errors
     */
    public String sendToServerREPL(String sexp) 
            throws IOException {
        String value = null;
        HttpURLConnection urlConnection = null;

        try {
            urlConnection = (HttpURLConnection)codeURL.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "text/plain");

            OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
            wr.write(sexp);
            wr.flush();

            int code = urlConnection.getResponseCode();
            switch (code) {
              case HttpURLConnection.HTTP_NO_CONTENT:
                // nothing to do
                value = "";
                break;

              case HttpURLConnection.HTTP_OK:
                value = readFromStream(urlConnection.getInputStream());
                break;

              case HttpURLConnection.HTTP_BAD_REQUEST:
                // presumably a syntax error in the input
                throw new IOException(readFromStream(urlConnection.getInputStream()));
                
              case HttpURLConnection.HTTP_FORBIDDEN:
                throw new IOException("Code execution forbidden by configuration");

              case HttpURLConnection.HTTP_INTERNAL_ERROR:
                throw new IOException("Internal error");

              default:
                throw new IOException("Unexpected response code " + code);
            }
            wr.close();
        } catch (UnknownServiceException e) {
            System.err.println("Unknown service exception talking to server REPL: " + e);
        }
        return value;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public URI getServerURI() {
        return serverURI;
    }

    /**
     * Get an identification string for the Quaestor service.
     * This also checks that the server is present at the expected URL
     * @return an identification string, or null if no service can be found
     */
    public String getIdent() {
        try {
            return sendToServerREPL("(ident)");
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Read the contents of a stream, and return it as a string
     */
    private String readFromStream(java.io.InputStream is) 
            throws IOException {
        String line;
        StringBuffer value = new StringBuffer();

        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        boolean firstTime = true;
        while ((line = rd.readLine()) != null) {
            if (!firstTime)
                value.append('\n');
            value.append(line);
            firstTime = false;
        }
                          
        rd.close();
        return value.toString();
    }

    /**
     * Main command-line function.
     * <p>Usage:
     * <pre>
     * java -jar quaestor-client.jar [options]
     * </pre>
     * <p>Options:
     * <dl>
     * <dt>--host=&lt;string&gt;</dt>
     * <dd>server host, default=localhost</dd>
     * <dt>--port=&lt;int&gt;</dt>
     * <dd>server port, default=8080</dd>
     * <dt>--servlet=&lt;string&gt;</dt>
     * <dd>servlet to talk to, default=quaestor
     * <dt>--url=&lt;url&gt;</dt>
     * <dd>set URL explicitly, overriding above
     * <dt>--prompt=&lt;string&gt;</dt>
     * <dd>force prompt string</dd>
     * </dl>
     */
    public static void main(String[] args) {
        // defaults for service location
        String quaestorHost = "localhost";
        int quaestorPort = 8080;
        String quaestorServlet = "quaestor";

        // options
        String forcedPrompt = null; // non-null overrides
        URI serverURI = null;       // non-null overrides
        boolean chatter = true;

        try {
            java.util.regex.Pattern opt = java.util.regex.Pattern.compile("^--([^=]*)(?:=(.*))?");
            for (String arg : args) {
                java.util.regex.Matcher m = opt.matcher(arg);
                if (m.matches()) {
                    String option = m.group(1);
                    String value  = m.group(2);
                    if (option.equals("host")) {
                        quaestorHost = value;
                    } else if (option.equals("port")) {
                        try {
                            quaestorPort = Integer.parseInt(value);
                        } catch (NumberFormatException e) {
                            System.err.println("Badly formatted port number: " + value);
                            Usage();
                        }
                    } else if (option.equals("servlet")) {
                        quaestorServlet = value;
                    } else if (option.equals("prompt")) {
                        forcedPrompt = value;
                    } else if (option.equals("url")) {
                        serverURI = new URI(value);
                    } else if (option.equals("quiet")) {
                        chatter = false;
                    } else {
                        Usage();
                    }
                } else {
                    Usage();
                }
            }

            QuaestorReplClient client;
            if (serverURI == null)
                client = new QuaestorReplClient(quaestorHost,
                                                quaestorPort,
                                                quaestorServlet);
            else
                client = new QuaestorReplClient(serverURI);

            // get service ident, thereby checking that it's there
            String ident = client.getIdent();

            if (ident == null) {
                System.err.println("Can't contact Quaestor at <" + client.getServerURI() + ">");
                System.exit(1);
            }

            if (chatter) {
                System.out.println("Quaestor client at <" + client.getServerURI() + ">");
                System.out.println("Ident: " + ident);
            }

            if (forcedPrompt != null)
                client.setPrompt(forcedPrompt);

            SexpStream in = new SexpStream(System.in);

            do {
                try {
                    System.out.print(client.prompt);
                    String sexp = in.readSexp();
                    if (sexp == null)
                        break;      // JUMP OUT
                    System.out.println(client.sendToServerREPL(sexp));
                } catch (IOException e) {
                    System.err.println("Error: " + e);
                }
            } while (true);
        } catch (URISyntaxException e) {
            System.err.println("That's not a good URI: " + e);
            System.exit(1);
        } catch (MalformedURLException e) {
            System.err.println("That's not a good URL: " + e);
            System.exit(1);
        }
    }

    private static void Usage() {
        System.err.println("Usage: QuaestorReplClient [options]");
        System.err.println("Options:");
        System.err.println("  --host=<string>    server host, default=localhost");
        System.err.println("  --port=<integer>   server port, default=8080");
        System.err.println("  --servlet=<string> servlet name, default=quaestor");
        System.err.println("  --url=<url>        set URL explicitly, overriding above");
        System.err.println("  --prompt=<string>  force prompt string");
        System.err.println("  --quiet            no chatter");
        System.exit(1);
    }
}
