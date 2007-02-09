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

    private String prompt = "#> ";

    private URL serverURL;

    public QuaestorReplClient(String host, int port, String servlet)
            throws MalformedURLException {
        serverURL = new URL("http", host, port, "/"+servlet+"/code");
        prompt = servlet+"> ";
    }

    /**
     * Send a single sexp to the Quaestor server, and return the value
     * which comes back, or an empty string if there is no response.
     */
    public String sendToServerREPL(String sexp) {
        StringBuffer value = new StringBuffer();
        HttpURLConnection urlConnection = null;
        //try {
            try {
                urlConnection = (HttpURLConnection)serverURL.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                OutputStreamWriter wr
                        = new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write(sexp);
                wr.flush();

                int code = urlConnection.getResponseCode();
                switch (code) {
                  case HttpURLConnection.HTTP_NO_CONTENT:
                    // nothing to do
                    break;

                  case HttpURLConnection.HTTP_OK:
                      {
                          // OK
                          String line;
                          BufferedReader rd
                                  = new BufferedReader
                                  (new InputStreamReader
                                   (urlConnection.getInputStream()));
                          boolean firstTime = true;
                          while ((line = rd.readLine()) != null) {
                              if (!firstTime)
                                  value.append('\n');
                              value.append(line);
                              firstTime = false;
                          }
                          
                          wr.close();
                          rd.close();
                      }
                      break;

                  case HttpURLConnection.HTTP_FORBIDDEN:
                    System.err.println
                            ("Code execution forbidden by configuration");
                    break;

                  case HttpURLConnection.HTTP_INTERNAL_ERROR:
                    System.err.println("Internal error");
                    break;

                  default:
                    System.err.println("Unexpected response code " + code);
                    break;
                }

            } catch (UnknownServiceException e) {
                System.err.println
                        ("Unknown service exception talking to server REPL: "
                         + e);
            } catch (IOException e) {
//                 if (urlConnection != null
//                     && urlConnection.getResponseCode() >= 400) {
//                     // This isn't an IOException, but a stupid Sun
//                     // implementation, which artificially throws IOExceptions
//                     // on response codes >= 400.
//                     int code = urlConnection.getResponseCode();
//                     switch (code) {
//                       case HttpURLConnection.HTTP_FORBIDDEN:
//                         System.err.println
//                                 ("Code execution forbidden by configuration");
//                         break;
//                       case HttpURLConnection.HTTP_INTERNAL_ERROR:
//                         System.err.println("Internal error");
//                         break;
//                       default:
//                         System.err.println("Unexpected response code " + code
//                                            + " (" + e + ")");
//                         break;
//                     }
//                 } else {
                System.err.println("IOException talking to server REPL: "+e);
//                 }
            }
//     } catch (IOException e) {
//                     /* This is an IOException thrown by the getResponseCode
//            That really shouldn't happen. */
//             System.err.println("Weirdo IOException: " + e);
//         }
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
     * <dt>--host=&lt;string&gt;</dt>
     * <dd>server host, default=localhost</dd>
     * <dt>--port=&lt;int&gt;</dt>
     * <dd>server port, default=8080</dd>
     * <dt>--servlet=&lt;string&gt;</dt>
     * <dd>servlet to talk to, default=quaestor
     * </dl>
     */
    public static void main(String[] args) {
        String sexp;
        String quaestorHost = "localhost";
        int quaestorPort = 8080;
        String quaestorServlet = "quaestor";

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
                } else if (option.equals("servlet")) {
                    quaestorServlet = value;
                } else {
                    Usage();
                }
            } else {
                Usage();
            }
        }

        try {
            QuaestorReplClient client
                    = new QuaestorReplClient(quaestorHost,
                                             quaestorPort,
                                             quaestorServlet);
            SexpStream in = new SexpStream(System.in);

            do {
                try {
                    System.out.print(client.prompt);
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
        System.err.println("  --host=<string>    server host, default=localhost");
        System.err.println("  --port=<integer>   server port, default=8080");
        System.err.println("  --servlet=<string> servlet name, default=quaestor");
        System.exit(1);
    }
}
