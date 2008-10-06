/*
C interface to the AR
Paul Harrison paul.harrison@manchester.ac.uk
produced on 2008-10-06+01:00

DO NOT EDIT - this file is produced automatically by the AR build process

 * Copyright 2007 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
*/
   
   
#include "intfclasses.h"

// factory methods

AbstractInformation_* AbstractInformation_::create(XmlRpcValue & v) {
       XmlRpcValue intf = v.mem("__interfaces");
       std::set<std::string> intfs;
       std::string s1 = intf[0];
       for (int i = 0; i < intf.size(); ++i) {
         std::string itf = intf[i];
         intfs.insert(itf);
       }
       //order these with the deepest child first - ordering that appears in __interfaces is apparently random...
         
         if(intfs.count( "org.astrogrid.acr.astrogrid.UserInformation") > 0)
         {
            return new UserInformation_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.astrogrid.NodeInformation") > 0)
         {
            return new NodeInformation_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.astrogrid.ExecutionInformation") > 0)
         {
            return new ExecutionInformation_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.astrogrid.AbstractInformation") > 0)
         {
            return new AbstractInformation_(v);
         }
   
   
      return 0;
      };
      BaseParam_* BaseParam_::create(XmlRpcValue & v) {
       XmlRpcValue intf = v.mem("__interfaces");
       std::set<std::string> intfs;
       std::string s1 = intf[0];
       for (int i = 0; i < intf.size(); ++i) {
         std::string itf = intf[i];
         intfs.insert(itf);
       }
       //order these with the deepest child first - ordering that appears in __interfaces is apparently random...
         
         if(intfs.count( "org.astrogrid.acr.astrogrid.ParameterBean") > 0)
         {
            return new ParameterBean_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.astrogrid.ColumnBean") > 0)
         {
            return new ColumnBean_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.InputParam") > 0)
         {
            return new InputParam_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.BaseParam") > 0)
         {
            return new BaseParam_(v);
         }
   
   
      return 0;
      };
      Catalog_* Catalog_::create(XmlRpcValue & v) {
       XmlRpcValue intf = v.mem("__interfaces");
       std::set<std::string> intfs;
       std::string s1 = intf[0];
       for (int i = 0; i < intf.size(); ++i) {
         std::string itf = intf[i];
         intfs.insert(itf);
       }
       //order these with the deepest child first - ordering that appears in __interfaces is apparently random...
         
         if(intfs.count( "org.astrogrid.acr.astrogrid.DatabaseBean") > 0)
         {
            return new DatabaseBean_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.Catalog") > 0)
         {
            return new Catalog_(v);
         }
   
   
      return 0;
      };
      Resource_* Resource_::create(XmlRpcValue & v) {
       XmlRpcValue intf = v.mem("__interfaces");
       std::set<std::string> intfs;
       std::string s1 = intf[0];
       for (int i = 0; i < intf.size(); ++i) {
         std::string itf = intf[i];
         intfs.insert(itf);
       }
       //order these with the deepest child first - ordering that appears in __interfaces is apparently random...
         
         if(intfs.count( "org.astrogrid.acr.astrogrid.CeaService") > 0)
         {
            return new CeaService_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.TableService") > 0)
         {
            return new TableService_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.StapService") > 0)
         {
            return new StapService_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.SsapService") > 0)
         {
            return new SsapService_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.SiapService") > 0)
         {
            return new SiapService_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.RegistryService") > 0)
         {
            return new RegistryService_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.CatalogService") > 0)
         {
            return new CatalogService_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.DataService") > 0)
         {
            return new DataService_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.ConeService") > 0)
         {
            return new ConeService_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.Service") > 0)
         {
            return new Service_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.astrogrid.CeaApplication") > 0)
         {
            return new CeaApplication_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.Application") > 0)
         {
            return new Application_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.TabularDB") > 0)
         {
            return new TabularDB_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.DataCollection") > 0)
         {
            return new DataCollection_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.StandardSTC") > 0)
         {
            return new StandardSTC_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.Organisation") > 0)
         {
            return new Organisation_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.Authority") > 0)
         {
            return new Authority_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.Resource") > 0)
         {
            return new Resource_(v);
         }
   
   
      return 0;
      };
      Service_* Service_::create(XmlRpcValue & v) {
       XmlRpcValue intf = v.mem("__interfaces");
       std::set<std::string> intfs;
       std::string s1 = intf[0];
       for (int i = 0; i < intf.size(); ++i) {
         std::string itf = intf[i];
         intfs.insert(itf);
       }
       //order these with the deepest child first - ordering that appears in __interfaces is apparently random...
         
         if(intfs.count( "org.astrogrid.acr.astrogrid.CeaService") > 0)
         {
            return new CeaService_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.TableService") > 0)
         {
            return new TableService_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.StapService") > 0)
         {
            return new StapService_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.SsapService") > 0)
         {
            return new SsapService_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.SiapService") > 0)
         {
            return new SiapService_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.RegistryService") > 0)
         {
            return new RegistryService_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.CatalogService") > 0)
         {
            return new CatalogService_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.DataService") > 0)
         {
            return new DataService_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.ConeService") > 0)
         {
            return new ConeService_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.Service") > 0)
         {
            return new Service_(v);
         }
   
   
      return 0;
      };
      DataService_* DataService_::create(XmlRpcValue & v) {
       XmlRpcValue intf = v.mem("__interfaces");
       std::set<std::string> intfs;
       std::string s1 = intf[0];
       for (int i = 0; i < intf.size(); ++i) {
         std::string itf = intf[i];
         intfs.insert(itf);
       }
       //order these with the deepest child first - ordering that appears in __interfaces is apparently random...
         
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.CatalogService") > 0)
         {
            return new CatalogService_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.DataService") > 0)
         {
            return new DataService_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.CatalogService") > 0)
         {
            return new CatalogService_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.DataService") > 0)
         {
            return new DataService_(v);
         }
   
   
      return 0;
      };
      Application_* Application_::create(XmlRpcValue & v) {
       XmlRpcValue intf = v.mem("__interfaces");
       std::set<std::string> intfs;
       std::string s1 = intf[0];
       for (int i = 0; i < intf.size(); ++i) {
         std::string itf = intf[i];
         intfs.insert(itf);
       }
       //order these with the deepest child first - ordering that appears in __interfaces is apparently random...
         
         if(intfs.count( "org.astrogrid.acr.astrogrid.CeaApplication") > 0)
         {
            return new CeaApplication_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.Application") > 0)
         {
            return new Application_(v);
         }
   
   
      return 0;
      };
      DataCollection_* DataCollection_::create(XmlRpcValue & v) {
       XmlRpcValue intf = v.mem("__interfaces");
       std::set<std::string> intfs;
       std::string s1 = intf[0];
       for (int i = 0; i < intf.size(); ++i) {
         std::string itf = intf[i];
         intfs.insert(itf);
       }
       //order these with the deepest child first - ordering that appears in __interfaces is apparently random...
         
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.TabularDB") > 0)
         {
            return new TabularDB_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.DataCollection") > 0)
         {
            return new DataCollection_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.TabularDB") > 0)
         {
            return new TabularDB_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.DataCollection") > 0)
         {
            return new DataCollection_(v);
         }
   
   
      return 0;
      };
      Capability_* Capability_::create(XmlRpcValue & v) {
       XmlRpcValue intf = v.mem("__interfaces");
       std::set<std::string> intfs;
       std::string s1 = intf[0];
       for (int i = 0; i < intf.size(); ++i) {
         std::string itf = intf[i];
         intfs.insert(itf);
       }
       //order these with the deepest child first - ordering that appears in __interfaces is apparently random...
         
         if(intfs.count( "org.astrogrid.acr.astrogrid.CeaServerCapability") > 0)
         {
            return new CeaServerCapability_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.StapCapability") > 0)
         {
            return new StapCapability_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.SsapCapability") > 0)
         {
            return new SsapCapability_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.SiapCapability") > 0)
         {
            return new SiapCapability_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.SearchCapability") > 0)
         {
            return new SearchCapability_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.HarvestCapability") > 0)
         {
            return new HarvestCapability_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.RegistryCapability") > 0)
         {
            return new RegistryCapability_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.ConeCapability") > 0)
         {
            return new ConeCapability_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.Capability") > 0)
         {
            return new Capability_(v);
         }
   
   
      return 0;
      };
      RegistryCapability_* RegistryCapability_::create(XmlRpcValue & v) {
       XmlRpcValue intf = v.mem("__interfaces");
       std::set<std::string> intfs;
       std::string s1 = intf[0];
       for (int i = 0; i < intf.size(); ++i) {
         std::string itf = intf[i];
         intfs.insert(itf);
       }
       //order these with the deepest child first - ordering that appears in __interfaces is apparently random...
         
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.SearchCapability") > 0)
         {
            return new SearchCapability_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.HarvestCapability") > 0)
         {
            return new HarvestCapability_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.RegistryCapability") > 0)
         {
            return new RegistryCapability_(v);
         }
   
   
      return 0;
      };
      Interface_* Interface_::create(XmlRpcValue & v) {
       XmlRpcValue intf = v.mem("__interfaces");
       std::set<std::string> intfs;
       std::string s1 = intf[0];
       for (int i = 0; i < intf.size(); ++i) {
         std::string itf = intf[i];
         intfs.insert(itf);
       }
       //order these with the deepest child first - ordering that appears in __interfaces is apparently random...
         
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.WebServiceInterface") > 0)
         {
            return new WebServiceInterface_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.ParamHttpInterface") > 0)
         {
            return new ParamHttpInterface_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.Interface") > 0)
         {
            return new Interface_(v);
         }
   
   
      return 0;
      };
      HasCoverage_* HasCoverage_::create(XmlRpcValue & v) {
       XmlRpcValue intf = v.mem("__interfaces");
       std::set<std::string> intfs;
       std::string s1 = intf[0];
       for (int i = 0; i < intf.size(); ++i) {
         std::string itf = intf[i];
         intfs.insert(itf);
       }
       //order these with the deepest child first - ordering that appears in __interfaces is apparently random...
         
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.TabularDB") > 0)
         {
            return new TabularDB_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.DataCollection") > 0)
         {
            return new DataCollection_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.CatalogService") > 0)
         {
            return new CatalogService_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.DataService") > 0)
         {
            return new DataService_(v);
         }
   
   
         if(intfs.count( "org.astrogrid.acr.ivoa.resource.HasCoverage") > 0)
         {
            return new HasCoverage_(v);
         }
   
   
      return 0;
      };
      