package org.astrogrid.registry.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.message.SOAPBodyElement;
import org.apache.axis.utils.XMLUtils;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.log4j.Category;
import org.w3c.dom.Document;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class SoapMessagingClient {
  private Category logger = Category.getInstance(getClass()); 

  public static void main(String[] args) {
    Options options = new Options();
    
    Option urlOption = new Option("u", "url", true, "SOAP end point URL");
    urlOption.setRequired(true);
    options.addOption(urlOption);

    Option operationOption = new Option("o", "op", true, "SOAP operation name");
    operationOption .setRequired(true);
    options.addOption(operationOption);
    
    Option msgOption = new Option("m", "msg", true, "SOAP message body file");
    msgOption.setRequired(true);
    options.addOption(msgOption);

    Option numOption = new Option("n", "num", true, "Number of times message should be sent");
    options.addOption(numOption);

    Parser cmdLineParser = new BasicParser();
    CommandLine cmdLine = null;
    try {
      cmdLine = cmdLineParser.parse(options, args);
    }
    catch (ParseException e) {
      usage(options);
      e.printStackTrace();
      System.err.println("[main] " + e.getMessage());
    }
    
    String url = cmdLine.getOptionValue("u");
    String operation = cmdLine.getOptionValue("o");
    String messageFile = cmdLine.getOptionValue("m");
    
    String numMessages = cmdLine.getOptionValue("n", "1");
    final int messageCount = Integer.parseInt(numMessages);

    for(int messageIndex = 0; messageIndex < messageCount; messageIndex++) {
      InputStream messageInputStream;
      sendMessage(url, operation, messageFile);
    }
  }

  private static Object sendMessage(String url, String operation, String messageFile) {
    Object result = null;
    
    InputStream messageInputStream = null;
    try {
      messageInputStream = new FileInputStream(messageFile);
      SoapMessagingClient client = new SoapMessagingClient();
      result = client.call(url, operation, messageInputStream);
    
      System.out.println("[sendMessage] result: " + result);
    }
    catch(Exception e) {
      e.printStackTrace();
      System.err.println("[sendMessage] " + e.getMessage());
    }
    finally {
      try {
        if(messageInputStream != null) {
          messageInputStream.close();
        }
      }
      catch(IOException e) {
        // assume closure.
      }
    }

    return result;     
  }
  
  private static void usage(Options options) {
    new HelpFormatter().printHelp("java org.astrogrid.registry.util.SoapMessagingClient", options);
  }

  private Object call(String endPoint, String operation, InputStream messageSource) throws Exception {
    Service service = new Service();
    Call call = (Call) service.createCall();
    call.setTargetEndpointAddress(new URL(endPoint));
      
    SOAPBodyElement[] bodyElement = new SOAPBodyElement[1];
    Document doc = XMLUtils.newDocument(messageSource);
    bodyElement[0] = new SOAPBodyElement(doc.getDocumentElement());
    
    logger.debug("[call] url: " + endPoint);
    logger.debug("[call] op:  " + operation);
    logger.debug("[call] msg: " + doc);
      
    Object result = call.invoke(bodyElement);

    logger.debug("[call] res: " + result);
    
    return result;
  }
}
