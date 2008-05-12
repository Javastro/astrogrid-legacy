package org.eurovotech.bibxml2rdf;

import java.io.File;
import java.io.Reader;
import org.xml.sax.SAXException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class GrokXML {
    public static void main(String[] args) {
        Pattern opt = Pattern.compile("^--([a-zA-Z]+)?(:?=(.*))?");

        int exitStatus;

        try {

            File sourceFile = null;
            BibContentHandler bch = null;

            for (String arg : args) {
                Matcher m = opt.matcher(arg);
                if (m.matches()) {
                    String optname = (m.groupCount() > 0 ? m.group(1) : null);
                    String optvalue = (m.groupCount() > 1 ? m.group(2) : null);
                    if (optname == null) // option was just "--"
                        Usage();
                    else if (optname.equals("ADS"))
                        bch = new ADSContentHandler();
                    else if (optname.equals("arXiv"))
                        bch = new ArXivContentHandler();
                    else
                        Usage();
                } else {
                    if (sourceFile != null)
                        Usage();
                    sourceFile = new File(arg);
                }
            }
            if (bch == null)
                Usage();

            // We need to be sure to parse the input XML file as UTF-8
            java.io.InputStreamReader fr
                    = new java.io.InputStreamReader(new java.io.FileInputStream(sourceFile), "UTF8");

            // do the work
            boolean res = processXML(fr, bch, System.out);

            exitStatus = (res ? 0 : 1);
        } catch (java.io.FileNotFoundException e) {
            System.err.println("Can't find file: " + e);
            exitStatus = 1;
        } catch (java.io.UnsupportedEncodingException e) {
            System.err.println("Unsupported encoding requested when parsing XML file: " + e);
            exitStatus = 1;
        }
        
        System.exit(exitStatus);
    }

    private static void Usage() {
        System.err.println("Usage: GrokXML [--ADS|--arXiv] filename");
        System.exit(1);
    }

    private static boolean processXML(Reader input,
                                      BibContentHandler bch,
                                      java.io.PrintStream output) {
        try {
            javax.xml.parsers.SAXParserFactory spf = javax.xml.parsers.SAXParserFactory.newInstance();
            spf.setNamespaceAware(true);
            spf.setFeature("http://xml.org/sax/features/string-interning", true);
            javax.xml.parsers.SAXParser sp = spf.newSAXParser();

            BibRecordHandler rh = new SimpleRecordHandler(output);
            bch.setBibRecordHandler(rh);

            sp.parse(new org.xml.sax.InputSource(input), bch);

            rh.close();

            return true;
            
        } catch (java.io.FileNotFoundException e) {
            System.err.println("File not found: " + e);
        } catch (javax.xml.parsers.ParserConfigurationException e) {
            System.err.println("Error creating parser: " + e);
        } catch (SAXException e) {
            System.err.println("Other SAX exception: " + e);
        } catch (java.io.IOException e) {
            System.err.println("IO Exception: " + e);
        }

        return false;
    }
}
