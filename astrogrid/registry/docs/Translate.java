import java.io.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.*;



public class Translate {

   public static void main(String []argv) {

      try {
         File xmlFile = new File(argv[0]);
         File xslFile = new File(argv[1]);
         File fi = new File("Test.xml");
         DocumentBuilder registryBuilder = null;
         DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
         dbf.setNamespaceAware(true);
         registryBuilder = dbf.newDocumentBuilder();
         Document doc = registryBuilder.parse(xmlFile);
         Source xmlSource = new DOMSource(doc);
         Document resultDoc = null;
         Source xslSource = new StreamSource(xslFile);

         //DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
         //builderFactory.setNamespaceAware(true);
         //DocumentBuilder builder = builderFactory.newDocumentBuilder();
         //resultDoc = builder.newDocument();
         //DOMResult result = new DOMResult(resultDoc);

         //StreamResult result = new StreamResult(System.out);
         StreamResult result = new StreamResult(fi);
         System.out.println("BEGIN TRANSFORMATION");
         TransformerFactory transformerFactory = TransformerFactory.newInstance();
         Transformer transformer = transformerFactory.newTransformer(xslSource);
         transformer.transform(xmlSource,result);
         System.out.println("Finished");
      }catch(ParserConfigurationException pce) {
         pce.printStackTrace();
      }catch(TransformerConfigurationException tce) {
         tce.printStackTrace();
      }catch(TransformerException te) {
         te.printStackTrace();
      }catch(Exception e) {
         e.printStackTrace();
      }



   }



}