package org.astrogrid.registry.services;

import javax.xml.parsers.*;
import org.w3c.dom.*;

public class QueryServices
{

  public static void main(String[] args)
  {

    if (args.length != 1)
    {
      System.err.println("Usage: java QueryServices <queryElement>");
    }
    String queryElement = args[0];

    Document doc = null;
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = null;
    try
    {
      builder = factory.newDocumentBuilder();
      doc = builder.parse("astrogrid/registry/services/services.xml");
      QueryServices dom = new QueryServices();

      String response = dom.generateResponse(doc, queryElement);
      System.out.println("\nServices matching query on " + queryElement + ": \n");
      System.out.println(response);
    }
    catch (ParserConfigurationException e)
    {
      System.err.println(e);
    }
    catch (Exception e)
    {
      System.out.println("oops!: " + e);
    }

  }

  public String generateResponse(Document doc, String queryElement)
  {

    String response = "";

    Element docElement = doc.getDocumentElement();

    NodeList nl = docElement.getElementsByTagName(queryElement);

    for (int i = 0; i < nl.getLength(); i++)
    {

      Element parent = (Element) nl.item(i).getParentNode();

      String matchElementValue = parent.getElementsByTagName(queryElement).item(0).getFirstChild().getNodeValue();

      String id = parent.getElementsByTagName("id").item(0).getFirstChild().getNodeValue();
      String title = parent.getElementsByTagName("title").item(0).getFirstChild().getNodeValue();

      response = response + "<id>" + id + "</id> \n";
      response = response + "<title>" + title + "</title> \n\n";
    }

    return response;

  }

}