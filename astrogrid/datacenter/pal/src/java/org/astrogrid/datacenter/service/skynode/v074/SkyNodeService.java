/*
 * $Id: SkyNodeService.java,v 1.1 2004/11/11 20:42:50 mch Exp $
 */

package org.astrogrid.datacenter.service.skynode.v074;

import net.ivoa.SkyNode.*;

import java.io.IOException;
import java.io.StringWriter;
import java.rmi.RemoteException;
import net.ivoa.www.xml.ADQL.v0_7_4.SelectType;
import nvo_region.RegionType;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.DsaDomHelper;
import org.astrogrid.datacenter.queriers.sql.RdbmsResourceGenerator;
import org.astrogrid.datacenter.queriers.sql.RdbmsResourceReader;
import org.astrogrid.datacenter.query.AdqlQueryMaker;
import org.astrogrid.datacenter.service.AxisDataServer;
import org.astrogrid.slinger.targets.TargetMaker;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Element;

/**
 * SkyNode - an implementation of the SkyNode interface.
 *
 */

public class SkyNodeService //implements SkyNodeSoap
{

   AxisDataServer server = new AxisDataServer();
   
   /** Returns string array of which formats the results can be produced in */
   public ArrayOfString formats() throws RemoteException {
      ArrayOfString strings = new ArrayOfString();
      strings.setString(new String[] { "VOTABLE", "CSV", "HTML" });
      return strings;
   }

   /** Returns information about the given table.  Case insensitive. */
   public MetaTable table(String table) throws RemoteException {
      try {
         return makeMetaTable(RdbmsResourceReader.getTable(table));
      }
      catch (IOException e) {
         throw new RemoteException(e+", getting "+RdbmsResourceGenerator.XSI_TYPE,e);
      }
   }
   
   /** Returns details about the given column */
   public MetaColumn column(String table, String column) throws RemoteException {
      try {
         return makeMetaColumn(RdbmsResourceReader.getColumn(table, column));
      }
      catch (IOException e) {
         throw new RemoteException(e+", getting "+RdbmsResourceGenerator.XSI_TYPE,e);
      }
  }
   
  /** Given a resource table element, returns a metatable object */
  private MetaTable makeMetaTable(Element tableRes) {
      MetaTable returns = new MetaTable();
      returns.setName(DomHelper.getValue(DsaDomHelper.getSingleChildByTagName(tableRes, "Name")));
      returns.setDescription(DomHelper.getValue(DsaDomHelper.getSingleChildByTagName(tableRes, "Description")));
      return returns;
  }

  /** Given a resource column element, returns a metacolumn object */
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
         throw new RemoteException(e+", getting "+RdbmsResourceGenerator.XSI_TYPE,e);
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
         throw new RemoteException(e+", getting "+RdbmsResourceGenerator.XSI_TYPE,e);
      }
   }
   
   public Element performQuery(Element select, String requestedFormat) throws RemoteException {
      try {
         StringWriter sw = new StringWriter();
         server.askQuery(Account.ANONYMOUS, AdqlQueryMaker.makeQuery(select, TargetMaker.makeIndicator(sw), requestedFormat), this);
         VOTableData returns = new VOTableData();
         return DomHelper.newDocument(sw.toString()).getDocumentElement();
      }
      catch (Throwable e) {
         throw server.makeFault(server.SERVERFAULT, e.toString()+" in performQuery()", e);
      }
   }
   /*
   public VOData performQuery(SelectType select, String requestedFormat) throws RemoteException {
      throw server.makeFault("Typed Method PerformQuery not implemented; used performQuery(Element select)");
   }
    */
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
   
   
}
