/*
 * $Id: SkyNode.java,v 1.1 2004/10/05 14:56:45 mch Exp $
 */

package org.astrogrid.datacenter.service.v06.skynode;

import java.io.StringWriter;
import java.rmi.Remote;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.query.AdqlQuery;
import org.astrogrid.datacenter.returns.ReturnTable;
import org.astrogrid.datacenter.returns.TargetIndicator;
import org.astrogrid.datacenter.service.AxisDataServer;

/**
 * SkyNode - a partial implementation of the SkyNode interface.
 *
 * This file was auto-generated from WSDL by the Apache Axis WSDL2Java emitter
 * from the WSDL at http://openskyquery.net/nodes/sdss/nodeb.asmx?wsdl, and
 * then changed by hand to try and make the whole thing a bit simpler...
 */

public class SkyNode implements Remote {
   
   AxisDataServer server = new AxisDataServer();
   
   /**
    * [BASIC] Run a query, get back VOTable
    */
   public String performQuery(String query, java.lang.String requestedFormat) throws java.rmi.RemoteException {
      try {
         StringWriter sw = new StringWriter();
         server.askQuery(Account.ANONYMOUS, new AdqlQuery(query), new ReturnTable(TargetIndicator.makeIndicator(sw), requestedFormat));
         return sw.toString();
      }
      catch (Throwable e) {
         throw server.makeFault(server.SERVERFAULT, "Error asking Query("+query+", "+requestedFormat+")", e);
      }
                                                                                
   }
   
   /** [BASIC] Returns description of the table with the given name
   public MetaTable table(String tableName) throws java.rmi.RemoteException {
      MetaTable mt = new MetaTable();
      return mt;
   }
   
   /**
   // [BASIC] Returns an array of MetaTable
   public net.ivoa.SkyNode.ArrayOfMetaTable tables() throws java.rmi.RemoteException;
   
   // [BASIC] Returns MetaColumn[] info for a given table.
   public net.ivoa.SkyNode.MetaColumn column(java.lang.String table, java.lang.String column) throws java.rmi.RemoteException;
   
   // [BASIC] Returns MetaColumn[] info for a given table.
   public net.ivoa.SkyNode.ArrayOfMetaColumn columns(java.lang.String table) throws java.rmi.RemoteException;
    */
   
   /*
   // [BASIC] Returns a string[] of available query result formats.
   public net.ivoa.SkyNode.ArrayOfString formats() throws java.rmi.RemoteException;
    */
   
   /*
   // [BASIC] Returns MetaFunction[] with info for each function supported
   // by the service. These are functions other than the standard ones defined
   // in ADQL.
   public net.ivoa.SkyNode.ArrayOfMetaFunction functions() throws java.rmi.RemoteException;
   
   // [BASIC] Returns uptime infomration about the is service
   public net.ivoa.SkyNode.Availability getAvailability() throws java.rmi.RemoteException;
   
   // [FULL] Returns object count for a given criteria.
   public float queryCost(long planid, net.ivoa.www.xml.ADQL.v0_7_4.SelectType select) throws java.rmi.RemoteException;
   
   // [FULL] Run an ExecPlan, get back a VOData
   public net.ivoa.SkyNode.VOData executePlan(net.ivoa.SkyNode.ExecPlan plan) throws java.rmi.RemoteException;
   
   // [FULL] **NOT IMPLEMENTED** Returns a region which is the intersection
   // of the survey and the given region.
   public nvo_region.RegionType footprint(nvo_region.RegionType region) throws java.rmi.RemoteException;
    */
}
