package org.astrogrid.xdbserver.xql;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.TreeMap;
import java.util.Hashtable;
import java.io.IOException;

import org.astrogrid.dataservice.queriers.status.QuerierProcessingResults;
import org.astrogrid.dataservice.queriers.status.QuerierStatus;
import org.astrogrid.tableserver.out.TableWriter;
import org.astrogrid.tableserver.metadata.ColumnInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;



public class XMLDBHandler extends DefaultHandler {        
    private String currentString = "";
    String topLevel = null;
    QuerierStatus statusToUpdate;
    TreeMap tableData = null;
    TableWriter tableWriter;
    private static final Log log = LogFactory.getLog(XMLDBHandler.class);
    Hashtable prefixTable;
    TableMetaDocInterpreter interpreter;
    
    /**
     * ContractHandler to send xml schemas and wsdls to the client.
     * @param out
     * @param urlString
     * @param currentURLSchemaBase
     */
    public XMLDBHandler(TableWriter tableWriter, QuerierStatus statusToUpdate, String xql) {
        tableData = new TreeMap();
        this.tableWriter = tableWriter;
        this.statusToUpdate = statusToUpdate;
        prefixTable = new Hashtable();
        parseNS(xql);
        
        
        try {
            interpreter = new TableMetaDocInterpreter();}
        catch(IOException ioe) {
            //currently expect to fail here.
            interpreter = null;
        }
    }
    
    private void parseNS(String xql) {
         String []nSplit = xql.split("declare namespace ");
         String temp;
         String prefix;
         String uri;
         //loop through all the namespaces no need to do the final one that is the actual query.
         for(int j = 0;j < (nSplit.length - 1);j++) {
             temp = nSplit[j];
             temp = temp.trim();
             if(temp.indexOf('=') != -1 && temp.indexOf('"') != -1) {
                 prefix = temp.substring(0,temp.indexOf('='));
                 uri = temp.substring(temp.indexOf('"')+1,temp.lastIndexOf('"'));
                 prefixTable.put(uri,prefix.trim());
             }//if
         }//for
    }
    
    
    
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
      String temp = null;      
      if(topLevel == null) {
          topLevel = qName;          
      }
      if(localName != null) {
          temp = localName;
      }
      if(temp == null) {
          temp = qName;
          int index = -1;
          if( (index = temp.indexOf(":")) != -1) {
              temp = temp.substring(index);
          }
      }
      if(prefixTable.containsKey(uri)) {
          temp = (String)prefixTable.get(uri) + ":" + temp;
      } 
      for(int i = 0;i < attributes.getLength();i++) {
          tableData.put(temp + "@" + attributes.getQName(i),attributes.getValue(i).trim());
      }//for
      currentString += temp + "/";
    }
    
    public void endElement(String uri, String localName, String qName) throws SAXException {
        String temp = null;
        if(localName != null) {
            temp = localName;
        }
        if(temp == null) {
            temp = qName;
            int index = -1;
            if( (index = temp.indexOf(":")) != -1) {
                temp = temp.substring(index);
            }
        }
        if(prefixTable.containsKey(uri)) {
            temp = (String)prefixTable.get(uri) + ":" + temp;
        }         
        currentString = currentString.substring(0,(currentString.length() - ("/" + temp).length()));
        temp = null;
        if(qName.equals(topLevel)) {
            //we have reached the end of this Resource lets write out the table.
            try {
            ColumnInfo[] cols = new ColumnInfo[tableData.size()];
            Object []keys = tableData.keySet().toArray();
            for(int i = 0;i < keys.length;i++) {
                temp = (String)keys[i];
                if(temp.charAt((temp.length() - 1)) == '/') {
                    temp = temp.substring(0,(temp.length() - 1));
                }
                if(temp.startsWith("result/")) {
                    temp = temp.substring(7);
                }
                if(interpreter != null) {
                    try {
                        String collectionName = ConfigFactory.getCommonConfig().getString("datacenter.xmldb.collection","db");
                        cols[i] = interpreter.getColumn(null, collectionName, temp);
                    }catch(Exception ex) {
                        log.info("Tried to get column and failed; defaulting to string type",ex);
                        //ex.printStackTrace();
                        cols[i] = new ColumnInfo();                        
                        cols[i].setName(temp);
                        cols[i].setJavaType(String.class);
                        cols[i].setPublicType("string");                    
                    }
                }else {
                    cols[i] = new ColumnInfo();                        
                    cols[i].setName(temp);
                    cols[i].setJavaType(String.class);
                    cols[i].setPublicType("string");
                }
            }
            tableWriter.startTable(cols);
            /*
            for(int i = 0;i < keys.length;i++) {
                cols[i].setName((String)keys[i]);
            }
            */
            String[] colValues = new String[keys.length];
            for(int i = 0;i < keys.length;i++) {
                colValues[i] = (String)tableData.get(keys[i]);
            }
            tableWriter.writeRow(colValues);
            tableWriter.endTable();
            
            statusToUpdate.addDetail(keys+" rows sent");
            statusToUpdate.clearProgress();
            }catch(IOException ioe) {
                log.error(ioe);
                ioe.printStackTrace();
            }
        }
    }
    
    public void characters(char[] ch, int start, int length) throws SAXException {
        String result = null;
        if(tableData.containsKey(currentString)) {
            result = (String)tableData.get(currentString) + new String(ch);
        }else {
            result = new String(ch);
        }
        if(currentString != null && !currentString.trim().equals("result/")) {
            tableData.put(currentString,result.trim());
        }
    }
        
    public void startPrefixMapping(String prefix, String uri) {
      if(prefix == null || prefix.trim().length() == 0) {
          //its a default one ignore there elemetns should have a prefix with it
      }else {
          prefixTable.put(uri,prefix);
      }          
    }
}
