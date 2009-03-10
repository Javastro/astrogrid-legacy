package org.astrogrid.contracts;



import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * A program to locate all the schema files in the schema-source tree and to
 * write an HTML page as an index for them.
 *
 * Assume that the program always runs from the base directory of the contracts
 * project.
 *
 * @author Guy Rixon
 */
public class SchemaIndexer {

  public static void main(String[] args) throws Exception {
    File prefix = new File("src/schema");
    SchemaIndexer i = new SchemaIndexer(new File("target/site/schema/index.html"));
    i.beginDocument();

    i.beginSection("Common Execution Architecture and UWS");
    i.searchTree(prefix, new File(prefix, "cea"));
    i.endSection();

    i.beginSection("VOSpace");
    i.searchTree(prefix, new File(prefix, "vo-space"));
    i.endSection();

    i.beginSection("Resource-registry entries");
    i.searchTree(prefix, new File(prefix, "vo-resource-types"));
    i.endSection();

    i.beginSection("Data formats");
    i.searchTree(prefix, new File(prefix, "vo-formats"));
    i.endSection();

    i.beginSection("Query language");
    i.searchTree(prefix, new File(prefix, "adql"));
    i.endSection();

    i.beginSection("Coordinate systems");
    i.searchTree(prefix, new File(prefix, "stc"));
    i.endSection();

    i.beginSection("DSA metadata");
    i.searchTree(prefix, new File(prefix, "dsa"));
    i.endSection();

    i.beginSection("Registry service");
    i.searchTree(prefix, new File(prefix, "registry"));
    i.endSection();

    i.beginSection("JES");
    i.searchTree(prefix, new File(prefix, "jes"));
    i.endSection();

    i.beginSection("OAI");
    i.searchTree(prefix, new File(prefix, "oai"));
    i.endSection();

    i.beginSection("VOEvent");
    i.searchTree(prefix, new File(prefix, "VOEvent"));
    i.endSection();

    i.endDocument();
  }

  private PrintWriter out;

  public SchemaIndexer(File f) throws Exception {
    f.createNewFile();
    out = new PrintWriter(f, "UTF-8");
  }

  public void beginDocument() throws Exception {
    out.println("<html>");
    out.println("<body>");
    out.println("<h1>Index of schemata</h1>");
    out.println("<p>The URIs in this list are the namespace URIs for AstroGrid");
    out.println("schemata that are stable and published. The hyperlinks in");
    out.println("the list point to the schema documents (.xsd, .wsdl);");
    out.println("the target URLs of the hyperlinks are the location URIs of");
    out.println("the schemata (i.e. you can use them in <i>schemaLocation</i>");
    out.println("attributes in your documents).</p>");
    out.println();
  }

  public void endDocument() throws Exception {
    out.println("</body>");
    out.println("</html>");
    out.flush();
  }

  public void beginSection(String title) throws Exception {
    out.print("<h2>");
    out.print(title);
    out.println("</h2>");
    out.println("<ul>");
  }

  public void endSection() throws Exception {
    out.println("</ul>");
    out.println();
  }


  /**
   * Searches a directory tree, "breadth first". First, lists all .xsd and
   * .wsdl files. Second, calls itself recursively on each sub-directory.
   *
   * @param prefix A prefix to be excluded from paths passed to XSLT.
   * @startingDirectory The directry to search.
   */
  public void searchTree(File prefix, File startingDirectory) throws Exception {
    System.out.println(startingDirectory);

    File[] schemata = startingDirectory.listFiles(
      new FilenameFilter() {
        public boolean accept(File f, String s) {
          return s.endsWith(".xsd") || s.endsWith(".wsdl");
        }
      }
    );

    for (int i = 0; i < schemata.length; i++) {
      System.out.println(schemata[i].getAbsolutePath());
      parseSchema(prefix, schemata[i]);
    }

    File[] directories = startingDirectory.listFiles(
      new FileFilter() {
        public boolean accept(File f) {
          return f.isDirectory();
        }
      }
    );

    for (int i = 0; i < directories.length; i++) {
      searchTree(prefix, directories[i]);
    }
  }

  /**
   * Parses a schema file (XSD or WSDL), extracting the target namespace.
   *
   * @param prefix A prefix to be excluded from paths passed to XSLT.
   * @param schemaFile The file to transform.
   */
  public void parseSchema(File prefix, File schemaFile) throws Exception {
    StreamSource schema = new StreamSource(schemaFile);
    StreamSource transform = new StreamSource(
      new FileInputStream("src/xsl/extract-namespace.xsl")
    );
    Transformer t = TransformerFactory.newInstance().newTransformer(transform);
    StreamResult result = new StreamResult(out);
    int prefixLength = prefix.getAbsolutePath().length() + 1; // +1 => include a slash
    String relativePath = schemaFile.getAbsolutePath().substring(prefixLength);
    t.setParameter("filename", relativePath);
    t.transform(schema, result);


  }

}
