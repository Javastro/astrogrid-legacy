/*
 * $Id: SkyNode.java,v 1.4 2004/11/09 17:42:22 mch Exp $
 */

package org.astrogrid.datacenter.service.skynode.v074;

import net.ivoa.SkyNode.*;

import java.io.StringWriter;
import java.rmi.RemoteException;
import net.ivoa.www.xml.ADQL.v0_7_4.SelectType;
import nvo_region.RegionType;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.query.AdqlQueryMaker;
import org.astrogrid.datacenter.returns.ReturnTable;
import org.astrogrid.slinger.targets.TargetMaker;
import org.astrogrid.datacenter.service.AxisDataServer;
import org.astrogrid.util.DomHelper;

/**
 * SkyNode - an implementation of the SkyNode interface.
 *
 */

public class SkyNode implements SkyNodeSoap {

   AxisDataServer server = new AxisDataServer();
   
   public ArrayOfString formats() throws RemoteException {
      ArrayOfString strings = new ArrayOfString();
      strings.setString(new String[] { "VOTABLE", "CSV", "HTML" });
      return strings;
   }
   
   /** Returns details about the given column */
   public MetaColumn column(String table, String column) throws RemoteException {
      throw server.makeFault("Method not implemented");
   }
   
   public MetaTable table(String table) throws RemoteException {
      throw server.makeFault("Method not implemented");
   }
   
   public VOData executePlan(ExecPlan plan) throws RemoteException {
      throw server.makeFault("Method not implemented");
   }
   
   public RegionType footprint(RegionType region) throws RemoteException {
      throw server.makeFault("Method not implemented");
   }
   
   public ArrayOfMetaColumn columns(String table) throws RemoteException {
      throw server.makeFault("Method not implemented");
   }
   
   public Availability getAvailability() throws RemoteException {
      throw server.makeFault("Method not implemented");
   }
   
   public float queryCost(long planid, SelectType select) throws RemoteException {
      throw server.makeFault("Method not implemented");
   }
   
   public ArrayOfMetaFunction functions() throws RemoteException {
      throw server.makeFault("Method not implemented");
   }
   
   public ArrayOfMetaTable tables() throws RemoteException {
      throw server.makeFault("Method not implemented");
   }
   
   public VOData performQuery(SelectType select, String requestedFormat) throws RemoteException {
      throw server.makeFault("Method not implemented");
      /*
      try {
         StringWriter sw = new StringWriter();
         server.askQuery(Account.ANONYMOUS, new AdqlQuery(select.DomHelper.DocumentToString(query), new ReturnTable(TargetIndicator.makeIndicator(sw), requestedFormat));
         return sw.toString();
      }
      catch (Throwable e) {
         throw server.makeFault(server.SERVERFAULT, "Error asking Query("+query+", "+requestedFormat+")", e);
      }
       */
   }
   
   
}
