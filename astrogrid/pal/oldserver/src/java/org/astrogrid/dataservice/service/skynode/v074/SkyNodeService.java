/*
 * $Id: SkyNodeService.java,v 1.2 2007/06/08 13:16:12 clq2 Exp $
 */

package org.astrogrid.dataservice.service.skynode.v074;

import java.io.IOException;
import java.io.StringWriter;
import java.rmi.RemoteException;
import javax.xml.soap.SOAPException;
import org.apache.axis.MessageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.dataservice.queriers.QuerierPluginException;
import org.astrogrid.dataservice.service.AxisDataServer;
import org.astrogrid.dataservice.service.DataServer;
import org.astrogrid.io.account.LoginAccount;
import org.astrogrid.io.mime.MimeTypes;
import org.astrogrid.query.Query;
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.slinger.targets.WriterTarget;
import org.astrogrid.xml.DomHelper;
import org.w3c.dom.Element;

/**
 * SkyNode - an implementation of the SkyNode interface.
 * This should be described in the WSDD as a message-style service, as the
 * methods take the list
 * of elements that are childs of the soap:Body element
 *
 */

public class SkyNodeService //implements SkyNodeSoap
{

   private static Log log = LogFactory.getLog(SkyNodeService.class);
   
   AxisDataServer server = new AxisDataServer();
   
   
   /** Example/test method, returns the elements sent */
   public Element[] echo(Element[] elements) {
      return elements;
   }
   
   /** Returns string array of which formats the results can be produced in */
   public Element[] formats(Element[] elements) throws RemoteException {
      try {
         String[] formats = DataServer.getFormats();
         Element[] returns = new Element[formats.length];
         for (int i = 0; i < formats.length; i++) {
            returns[i] = elements[0].getOwnerDocument().createElementNS(elements[0].getNamespaceURI(), "Format");
         }
         return returns;
      }
      catch (QuerierPluginException pe) {
         throw server.makeFault(false, "Getting formats", pe);
      }
   }

   /** Returns information about the given table.  Case insensitive. *
   public MetaTable table(String table) throws RemoteException {
      try {
         return makeMetaTable(RdbmsResourceReader.getTable(table));
      }
      catch (IOException e) {
         throw server.makeFault(server.SERVERFAULT, e+" getting/parsing "+RdbmsResourceGenerator.XSI_TYPE,e);
      }
   }
   
   /** Returns details about the given column *
   public MetaColumn column(String table, String column) throws RemoteException {
      try {
         return makeMetaColumn(RdbmsResourceReader.getColumn(table, column));
      }
      catch (IOException e) {
         throw server.makeFault(server.SERVERFAULT, e+" getting/parsing "+RdbmsResourceGenerator.XSI_TYPE,e);
      }
  }
   
  /** Given a resource table element, returns a metatable object *
  private MetaTable makeMetaTable(Element tableRes) {
      MetaTable returns = new MetaTable();
      returns.setName(DomHelper.getValue(DsaDomHelper.getSingleChildByTagName(tableRes, "Name")));
      returns.setDescription(DomHelper.getValue(DsaDomHelper.getSingleChildByTagName(tableRes, "Description")));
      return returns;
  }

  /** Given a resource column element, returns a metacolumn object *
  private MetaColumn makeMetaColumn(Element colRes) {
      MetaColumn returns = new MetaColumn();
      returns.setName(DomHelper.getValue(DsaDomHelper.getSingleChildByTagName(colRes, "Name")));
      returns.setDescription(DomHelper.getValue(DsaDomHelper.getSingleChildByTagName(colRes, "Description")));
      returns.setUCD(DomHelper.getValue(DsaDomHelper.getSingleChildByTagName(colRes, "UCD")));
      returns.setUnit(DomHelper.getValue(DsaDomHelper.getSingleChildByTagName(colRes, "Unit")));
      return returns;
  }
  
   public ArrayOfMetaTable tables() throws RemoteException {
      try {
        Element[] tableRes = RdbmsResourceReader.getTables();
        MetaTable[] meta = new MetaTable[tableRes.length];
        for (int i = 0; i < tableRes.length; i++) {
           meta[i] = makeMetaTable(tableRes[i]);
        }
        ArrayOfMetaTable returns = new ArrayOfMetaTable();
        returns.setMetaTable(meta);
        return returns;
      }
      catch (IOException e) {
         throw server.makeFault(server.SERVERFAULT, e+" getting/parsing "+RdbmsResourceGenerator.XSI_TYPE,e);
      }
   }

   public ArrayOfMetaColumn columns(String table) throws RemoteException {
      try {
        Element[] colRes = RdbmsResourceReader.getColumns(table);
        MetaColumn[] meta = new MetaColumn[colRes.length];
        for (int i = 0; i < colRes.length; i++) {
           meta[i] = makeMetaColumn(colRes[i]);
        }
        ArrayOfMetaColumn returns = new ArrayOfMetaColumn();
        returns.setMetaColumn(meta);
        return returns;
      }
      catch (IOException e) {
         throw server.makeFault(server.SERVERFAULT, e+" getting/parsing "+RdbmsResourceGenerator.XSI_TYPE,e);
      }
   }
    */
   
   
   public Element[] performQuery(Element[] bodyElements) throws RemoteException {
       try {
         MessageContext.getCurrentContext().getMessage().writeTo(System.out);
         
         log.debug("SkyNode SOAP Message, body element 0: "+DomHelper.ElementToString(bodyElements[0]));
       }
       catch (SOAPException e) {} //ignore
       catch (IOException e) {} //ignore
      /*
       if (!select.getLocalName().equals("Select")) {
       try {
       MessageContext.getCurrentContext().getMessage().writeTo(System.out);
       }
       catch (SOAPException e) {} //ignore
       catch (IOException e) {} //ignore
       throw server.makeFault(server.SERVERFAULT, "First parameter (ADQL element) is '"+select.getLocalName()+"', should be 'Select'", null);
       }
       */
       /*
      //manky thing to get the adql document without having to re/unmarshall the SelectType object model
      SOAPMessage message = MessageContext.getCurrentContext().getMessage();
      Element adqlElement = null;
      try
      {
         Iterator children = message.getSOAPPart().getEnvelope().getBody().getChildElements();
         while (children.hasNext() && (adqlElement == null)) {
            RPCElement child = (RPCElement) children.next();
            log.debug("body child: "+child+" ("+child.getClass()+")");
            adqlElement = DsaDomHelper.getSingleChildByTagName(child.getAsDOM(), "Select");
         }
      }
      catch (Throwable e) {
         throw server.makeFault(server.SERVERFAULT, e.toString()+" in performQuery()", e);
      }
        */
      //normal bit - carry out the query
      try
      {
         Element performQueryElement = bodyElements[0];
         Element[] parameters = DomHelper.getChildren(performQueryElement);
         Element adqlElement = DomHelper.getSingleChildByTagName(parameters[0], "Select");
         
         String requestedFormat = MimeTypes.VOTABLE;
         
         if (parameters.length>1) {
            requestedFormat = DomHelper.getValueOf(parameters[1]);
         }
      
         StringWriter sw = new StringWriter();
         //server.askQuery(LoginAccount.ANONYMOUS, AdqlQueryMaker.makeQuery(adqlElement, new WriterTarget(sw), requestedFormat), server.getSource()+" via SkyNode");
         server.askQuery(LoginAccount.ANONYMOUS, 
             new Query(adqlElement, 
                new ReturnTable(new WriterTarget(sw), requestedFormat)),
             server.getSource()+" via SkyNode");
         Element votable = DomHelper.newDocument(sw.toString()).getDocumentElement();
         return new Element[] { votable };
      }
      catch (Throwable e) {
         throw server.makeFault(server.SERVERFAULT, e.toString()+" in performQuery()", e);
      }
   }
   /*
    *
   public Availability getAvailability() throws RemoteException {
      throw server.makeFault("Method not implemented");
   }
   
   public float queryCost(long planid, SelectType select) throws RemoteException {
      throw server.makeFault("Method not implemented");
   }
   
   public ArrayOfMetaFunction functions() throws RemoteException {
      throw server.makeFault("Method not implemented");
   }

   public VOData executePlan(ExecPlan plan) throws RemoteException {
      throw server.makeFault("Method not implemented");
   }
   
   public RegionType footprint(RegionType region) throws RemoteException {
      throw server.makeFault("Method not implemented");
   }
    */
   
}
