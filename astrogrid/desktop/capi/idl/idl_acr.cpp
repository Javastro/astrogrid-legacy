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
   
   
#include <stdio.h>
#include "AR.h"
#include "IDLStruct.h"
#include "idlhelper.h"

using namespace std;

static void IDL_ar_init(int argc, IDL_VPTR *argv, char* argk){   init_ar();};

 static IDL_VPTR IDL_astrogrid_stap_constructQuery(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //service
      _args[1] = argv[1]->value.str.s; //start
      _args[2] = argv[2]->value.str.s; //end

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.stap.constructQuery", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_stap_constructQueryF(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //service
      _args[1] = argv[1]->value.str.s; //start
      _args[2] = argv[2]->value.str.s; //end
      _args[3] = argv[3]->value.str.s; //format

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.stap.constructQueryF", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_stap_constructQueryP(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //service
      _args[1] = argv[1]->value.str.s; //start
      _args[2] = argv[2]->value.str.s; //end
      _args[3] = argv[3]->value.d; //ra
      _args[4] = argv[4]->value.d; //dec
      _args[5] = argv[5]->value.d; //size

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.stap.constructQueryP", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_stap_constructQueryPF(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //service
      _args[1] = argv[1]->value.str.s; //start
      _args[2] = argv[2]->value.str.s; //end
      _args[3] = argv[3]->value.d; //ra
      _args[4] = argv[4]->value.d; //dec
      _args[5] = argv[5]->value.d; //size
      _args[6] = argv[6]->value.str.s; //format

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.stap.constructQueryPF", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_stap_constructQueryS(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //service
      _args[1] = argv[1]->value.str.s; //start
      _args[2] = argv[2]->value.str.s; //end
      _args[3] = argv[3]->value.d; //ra
      _args[4] = argv[4]->value.d; //dec
      _args[5] = argv[5]->value.d; //ra_size
      _args[6] = argv[6]->value.d; //dec_size

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.stap.constructQueryS", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_stap_constructQuerySF(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //service
      _args[1] = argv[1]->value.str.s; //start
      _args[2] = argv[2]->value.str.s; //end
      _args[3] = argv[3]->value.d; //ra
      _args[4] = argv[4]->value.d; //dec
      _args[5] = argv[5]->value.d; //ra_size
      _args[6] = argv[6]->value.d; //dec_size
      _args[7] = argv[7]->value.str.s; //format

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.stap.constructQuerySF", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_stap_addOption(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //query
      _args[1] = argv[1]->value.str.s; //optionName
      _args[2] = argv[2]->value.str.s; //optionValue

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.stap.addOption", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_stap_execute(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //query

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.stap.execute", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_stap_executeVotable(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //query

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.stap.executeVotable", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static void IDL_astrogrid_stap_executeAndSave(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //query
      _args[1] = argv[1]->value.str.s; //saveLocation
if (ARexecute("astrogrid.stap.executeAndSave", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static IDL_VPTR IDL_astrogrid_stap_saveDatasets(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //query
      _args[1] = argv[1]->value.str.s; //saveLocation

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.stap.saveDatasets", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_stap_saveDatasetsSubset(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //query
      _args[1] = argv[1]->value.str.s; //saveLocation
   //FIXME type too complex for IDL? - rows List

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.stap.saveDatasetsSubset", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_stap_getRegistryXQuery(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.stap.getRegistryXQuery", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_processManager_list(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.processManager.list", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_processManager_submit(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //document

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.processManager.submit", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_processManager_submitTo(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //document
      _args[1] = argv[1]->value.str.s; //server

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.processManager.submitTo", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_processManager_submitStored(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //documentLocation

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.processManager.submitStored", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_processManager_submitStoredTo(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //documentLocation
      _args[1] = argv[1]->value.str.s; //server

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.processManager.submitStoredTo", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static void IDL_astrogrid_processManager_halt(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //executionId
if (ARexecute("astrogrid.processManager.halt", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static void IDL_astrogrid_processManager_delete(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //executionId
if (ARexecute("astrogrid.processManager.delete", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static IDL_VPTR IDL_astrogrid_processManager_getExecutionInformation(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //executionId

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.processManager.getExecutionInformation", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_processManager_getMessages(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //executionId

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.processManager.getMessages", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_processManager_getResults(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //executionid

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.processManager.getResults", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_processManager_getSingleResult(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //executionId
      _args[1] = argv[1]->value.str.s; //resultName

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.processManager.getSingleResult", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_myspace_getHome(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.myspace.getHome", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_myspace_exists(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //filename

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.myspace.exists", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_myspace_getNodeInformation(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //filename

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.myspace.getNodeInformation", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static void IDL_astrogrid_myspace_createFile(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //filename
if (ARexecute("astrogrid.myspace.createFile", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static void IDL_astrogrid_myspace_createFolder(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //foldername
if (ARexecute("astrogrid.myspace.createFolder", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static IDL_VPTR IDL_astrogrid_myspace_createChildFolder(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //parentFolder
      _args[1] = argv[1]->value.str.s; //name

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.myspace.createChildFolder", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_myspace_createChildFile(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //parentFolder
      _args[1] = argv[1]->value.str.s; //name

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.myspace.createChildFile", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_myspace_getParent(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //filename

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.myspace.getParent", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_myspace_list(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //ivorn

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.myspace.list", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_myspace_listIvorns(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //ivorn

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.myspace.listIvorns", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_myspace_listNodeInformation(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //ivorn

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.myspace.listNodeInformation", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static void IDL_astrogrid_myspace_refresh(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //ivorn
if (ARexecute("astrogrid.myspace.refresh", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static void IDL_astrogrid_myspace_delete(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //ivorn
if (ARexecute("astrogrid.myspace.delete", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static IDL_VPTR IDL_astrogrid_myspace_rename(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //srcIvorn
      _args[1] = argv[1]->value.str.s; //newName

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.myspace.rename", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_myspace_move(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //srcIvorn
      _args[1] = argv[1]->value.str.s; //newParentIvorn
      _args[2] = argv[2]->value.str.s; //newName

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.myspace.move", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static void IDL_astrogrid_myspace_changeStore(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //srcIvorn
      _args[1] = argv[1]->value.str.s; //storeIvorn
if (ARexecute("astrogrid.myspace.changeStore", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static IDL_VPTR IDL_astrogrid_myspace_copy(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //srcIvorn
      _args[1] = argv[1]->value.str.s; //newParentIvorn
      _args[2] = argv[2]->value.str.s; //newName

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.myspace.copy", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_myspace_read(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //ivorn

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.myspace.read", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static void IDL_astrogrid_myspace_write(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //ivorn
      _args[1] = argv[1]->value.str.s; //content
if (ARexecute("astrogrid.myspace.write", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static IDL_VPTR IDL_astrogrid_myspace_readBinary(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //ivorn

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.myspace.readBinary", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static void IDL_astrogrid_myspace_writeBinary(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //ivorn
   //FIXME type too complex for IDL? - content byte
if (ARexecute("astrogrid.myspace.writeBinary", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static IDL_VPTR IDL_astrogrid_myspace_getReadContentURL(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //ivorn

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.myspace.getReadContentURL", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_myspace_getWriteContentURL(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //ivorn

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.myspace.getWriteContentURL", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static void IDL_astrogrid_myspace_transferCompleted(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //ivorn
if (ARexecute("astrogrid.myspace.transferCompleted", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static void IDL_astrogrid_myspace_copyContentToURL(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //ivorn
      _args[1] = argv[1]->value.str.s; //destination
if (ARexecute("astrogrid.myspace.copyContentToURL", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static void IDL_astrogrid_myspace_copyURLToContent(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //src
      _args[1] = argv[1]->value.str.s; //ivorn
if (ARexecute("astrogrid.myspace.copyURLToContent", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static IDL_VPTR IDL_astrogrid_myspace_listStores(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.myspace.listStores", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_jobs_list(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.jobs.list", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_jobs_listFully(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.jobs.listFully", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_jobs_createJob(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.jobs.createJob", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_jobs_wrapTask(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //taskDocument

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.jobs.wrapTask", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_jobs_getJobTranscript(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //jobURN

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.jobs.getJobTranscript", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_jobs_getJobInformation(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //jobURN

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.jobs.getJobInformation", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static void IDL_astrogrid_jobs_cancelJob(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //jobURN
if (ARexecute("astrogrid.jobs.cancelJob", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static void IDL_astrogrid_jobs_deleteJob(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //jobURN
if (ARexecute("astrogrid.jobs.deleteJob", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static IDL_VPTR IDL_astrogrid_jobs_submitJob(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //workflow

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.jobs.submitJob", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_jobs_submitStoredJob(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //workflowReference

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.jobs.submitStoredJob", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static void IDL_astrogrid_community_login(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //username
      _args[1] = argv[1]->value.str.s; //password
      _args[2] = argv[2]->value.str.s; //community
if (ARexecute("astrogrid.community.login", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static IDL_VPTR IDL_astrogrid_community_getUserInformation(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.community.getUserInformation", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static void IDL_astrogrid_community_logout(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
if (ARexecute("astrogrid.community.logout", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static IDL_VPTR IDL_astrogrid_community_isLoggedIn(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.community.isLoggedIn", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static void IDL_astrogrid_community_guiLogin(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
if (ARexecute("astrogrid.community.guiLogin", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static IDL_VPTR IDL_astrogrid_applications_list(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.applications.list", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_applications_getQueryToListApplications(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.applications.getQueryToListApplications", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_applications_getRegistryQuery(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.applications.getRegistryQuery", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_applications_getRegistryAdqlQuery(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.applications.getRegistryAdqlQuery", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_applications_getRegistryXQuery(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.applications.getRegistryXQuery", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_applications_getCeaApplication(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //applicationName

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.applications.getCeaApplication", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_applications_getDocumentation(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //applicationName

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.applications.getDocumentation", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_applications_createTemplateDocument(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //applicationName
      _args[1] = argv[1]->value.str.s; //interfaceName

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.applications.createTemplateDocument", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_applications_createTemplateStruct(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //applicationName
      _args[1] = argv[1]->value.str.s; //interfaceName

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.applications.createTemplateStruct", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_applications_convertDocumentToStruct(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //document

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.applications.convertDocumentToStruct", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_applications_convertStructToDocument(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
   //FIXME type too complex for IDL? - structure Map

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.applications.convertStructToDocument", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static void IDL_astrogrid_applications_validate(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //document
if (ARexecute("astrogrid.applications.validate", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static void IDL_astrogrid_applications_validateStored(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //documentLocation
if (ARexecute("astrogrid.applications.validateStored", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static IDL_VPTR IDL_astrogrid_applications_listServersProviding(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //applicationId

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.applications.listServersProviding", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_applications_submit(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //document

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.applications.submit", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_applications_submitTo(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //document
      _args[1] = argv[1]->value.str.s; //server

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.applications.submitTo", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_applications_submitStored(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //documentLocation

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.applications.submitStored", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_applications_submitStoredTo(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //documentLocation
      _args[1] = argv[1]->value.str.s; //server

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.applications.submitStoredTo", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static void IDL_astrogrid_applications_cancel(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //executionId
if (ARexecute("astrogrid.applications.cancel", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static IDL_VPTR IDL_astrogrid_applications_getExecutionInformation(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //executionId

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.applications.getExecutionInformation", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_astrogrid_applications_getResults(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //executionid

       IDL_VPTR idl_retval;
if (ARexecute("astrogrid.applications.getResults", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_cds_vizier_cataloguesMetaData(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //target
      _args[1] = argv[1]->value.d; //radius
      _args[2] = argv[2]->value.str.s; //unit
      _args[3] = argv[3]->value.str.s; //text

       IDL_VPTR idl_retval;
if (ARexecute("cds.vizier.cataloguesMetaData", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_cds_vizier_cataloguesMetaDataWavelength(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //target
      _args[1] = argv[1]->value.d; //radius
      _args[2] = argv[2]->value.str.s; //unit
      _args[3] = argv[3]->value.str.s; //text
      _args[4] = argv[4]->value.str.s; //wavelength

       IDL_VPTR idl_retval;
if (ARexecute("cds.vizier.cataloguesMetaDataWavelength", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_cds_vizier_cataloguesData(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //target
      _args[1] = argv[1]->value.d; //radius
      _args[2] = argv[2]->value.str.s; //unit
      _args[3] = argv[3]->value.str.s; //text

       IDL_VPTR idl_retval;
if (ARexecute("cds.vizier.cataloguesData", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_cds_vizier_cataloguesDataWavelength(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //target
      _args[1] = argv[1]->value.d; //radius
      _args[2] = argv[2]->value.str.s; //unit
      _args[3] = argv[3]->value.str.s; //text
      _args[4] = argv[4]->value.str.s; //wavelength

       IDL_VPTR idl_retval;
if (ARexecute("cds.vizier.cataloguesDataWavelength", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_cds_vizier_metaAll(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   

       IDL_VPTR idl_retval;
if (ARexecute("cds.vizier.metaAll", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_cds_ucd_UCDList(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   

       IDL_VPTR idl_retval;
if (ARexecute("cds.ucd.UCDList", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_cds_ucd_resolveUCD(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //ucd

       IDL_VPTR idl_retval;
if (ARexecute("cds.ucd.resolveUCD", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_cds_ucd_UCDofCatalog(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //catalog_designation

       IDL_VPTR idl_retval;
if (ARexecute("cds.ucd.UCDofCatalog", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_cds_ucd_translate(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //ucd

       IDL_VPTR idl_retval;
if (ARexecute("cds.ucd.translate", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_cds_ucd_upgrade(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //ucd

       IDL_VPTR idl_retval;
if (ARexecute("cds.ucd.upgrade", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_cds_ucd_validate(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //ucd

       IDL_VPTR idl_retval;
if (ARexecute("cds.ucd.validate", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_cds_ucd_explain(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //ucd

       IDL_VPTR idl_retval;
if (ARexecute("cds.ucd.explain", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_cds_ucd_assign(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //descr

       IDL_VPTR idl_retval;
if (ARexecute("cds.ucd.assign", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_cds_sesame_resolve(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //name

       IDL_VPTR idl_retval;
if (ARexecute("cds.sesame.resolve", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_cds_sesame_sesame(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //name
      _args[1] = argv[1]->value.str.s; //resultType

       IDL_VPTR idl_retval;
if (ARexecute("cds.sesame.sesame", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_cds_sesame_sesameChooseService(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //name
      _args[1] = argv[1]->value.str.s; //resultType
      _args[2] = argv[2]->value.i; //all
      _args[3] = argv[3]->value.str.s; //service

       IDL_VPTR idl_retval;
if (ARexecute("cds.sesame.sesameChooseService", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_cds_glu_getURLfromTag(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //id

       IDL_VPTR idl_retval;
if (ARexecute("cds.glu.getURLfromTag", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_cds_coordinate_convert(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.d; //x
      _args[1] = argv[1]->value.d; //y
      _args[2] = argv[2]->value.d; //z
      _args[3] = argv[3]->value.i; //precision

       IDL_VPTR idl_retval;
if (ARexecute("cds.coordinate.convert", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_cds_coordinate_convertL(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.d; //lon
      _args[1] = argv[1]->value.d; //lat
      _args[2] = argv[2]->value.i; //precision

       IDL_VPTR idl_retval;
if (ARexecute("cds.coordinate.convertL", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_cds_coordinate_convertE(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.i; //frame1
      _args[1] = argv[1]->value.i; //frame2
      _args[2] = argv[2]->value.d; //x
      _args[3] = argv[3]->value.d; //y
      _args[4] = argv[4]->value.d; //z
      _args[5] = argv[5]->value.i; //precision
      _args[6] = argv[6]->value.d; //equinox1
      _args[7] = argv[7]->value.d; //equinox2

       IDL_VPTR idl_retval;
if (ARexecute("cds.coordinate.convertE", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_cds_coordinate_convertLE(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.i; //frame1
      _args[1] = argv[1]->value.i; //frame2
      _args[2] = argv[2]->value.d; //lon
      _args[3] = argv[3]->value.d; //lat
      _args[4] = argv[4]->value.i; //precision
      _args[5] = argv[5]->value.d; //equinox1
      _args[6] = argv[6]->value.d; //equinox2

       IDL_VPTR idl_retval;
if (ARexecute("cds.coordinate.convertLE", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_dialogs_toolEditor_edit(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //t

       IDL_VPTR idl_retval;
if (ARexecute("dialogs.toolEditor.edit", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_dialogs_toolEditor_editStored(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //documentLocation

       IDL_VPTR idl_retval;
if (ARexecute("dialogs.toolEditor.editStored", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_dialogs_toolEditor_selectAndBuild(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   

       IDL_VPTR idl_retval;
if (ARexecute("dialogs.toolEditor.selectAndBuild", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_dialogs_resourceChooser_chooseResource(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //title
      _args[1] = argv[1]->value.i; //enableRemote

       IDL_VPTR idl_retval;
if (ARexecute("dialogs.resourceChooser.chooseResource", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_dialogs_resourceChooser_chooseFolder(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //title
      _args[1] = argv[1]->value.i; //enableRemote

       IDL_VPTR idl_retval;
if (ARexecute("dialogs.resourceChooser.chooseFolder", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_dialogs_resourceChooser_fullChooseResource(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //title
      _args[1] = argv[1]->value.i; //enableVospace
      _args[2] = argv[2]->value.i; //enableLocal
      _args[3] = argv[3]->value.i; //enableURL

       IDL_VPTR idl_retval;
if (ARexecute("dialogs.resourceChooser.fullChooseResource", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_dialogs_resourceChooser_fullChooseFolder(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //title
      _args[1] = argv[1]->value.i; //enableVospace
      _args[2] = argv[2]->value.i; //enableLocal
      _args[3] = argv[3]->value.i; //enableURL

       IDL_VPTR idl_retval;
if (ARexecute("dialogs.resourceChooser.fullChooseFolder", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_dialogs_registryGoogle_selectResourcesXQueryFilter(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //prompt
      _args[1] = argv[1]->value.i; //multiple
      _args[2] = argv[2]->value.str.s; //xqueryFilter

       IDL_VPTR idl_retval;
if (ARexecute("dialogs.registryGoogle.selectResourcesXQueryFilter", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_dialogs_registryGoogle_selectResourcesFromList(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //prompt
      _args[1] = argv[1]->value.i; //multiple
      _args[2] = argv[2]->value.str.s; //identifiers

       IDL_VPTR idl_retval;
if (ARexecute("dialogs.registryGoogle.selectResourcesFromList", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_vospace_getHome(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.vospace.getHome", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_vospace_getNodeInformation(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //vosuri

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.vospace.getNodeInformation", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_vospace_listVOSpaces(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.vospace.listVOSpaces", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_ssap_constructQuery(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //service
      _args[1] = argv[1]->value.d; //ra
      _args[2] = argv[2]->value.d; //dec
      _args[3] = argv[3]->value.d; //size

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.ssap.constructQuery", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_ssap_constructQueryS(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //service
      _args[1] = argv[1]->value.d; //ra
      _args[2] = argv[2]->value.d; //dec
      _args[3] = argv[3]->value.d; //ra_size
      _args[4] = argv[4]->value.d; //dec_size

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.ssap.constructQueryS", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_ssap_addOption(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //query
      _args[1] = argv[1]->value.str.s; //optionName
      _args[2] = argv[2]->value.str.s; //optionValue

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.ssap.addOption", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_ssap_execute(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //query

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.ssap.execute", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_ssap_executeVotable(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //query

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.ssap.executeVotable", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static void IDL_ivoa_ssap_executeAndSave(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //query
      _args[1] = argv[1]->value.str.s; //saveLocation
if (ARexecute("ivoa.ssap.executeAndSave", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static IDL_VPTR IDL_ivoa_ssap_saveDatasets(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //query
      _args[1] = argv[1]->value.str.s; //saveLocation

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.ssap.saveDatasets", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_ssap_saveDatasetsSubset(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //query
      _args[1] = argv[1]->value.str.s; //saveLocation
   //FIXME type too complex for IDL? - rows List

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.ssap.saveDatasetsSubset", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_ssap_getRegistryXQuery(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.ssap.getRegistryXQuery", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_siap_constructQuery(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //service
      _args[1] = argv[1]->value.d; //ra
      _args[2] = argv[2]->value.d; //dec
      _args[3] = argv[3]->value.d; //size

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.siap.constructQuery", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_siap_constructQueryF(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //service
      _args[1] = argv[1]->value.d; //ra
      _args[2] = argv[2]->value.d; //dec
      _args[3] = argv[3]->value.d; //size
      _args[4] = argv[4]->value.str.s; //format

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.siap.constructQueryF", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_siap_constructQueryS(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //service
      _args[1] = argv[1]->value.d; //ra
      _args[2] = argv[2]->value.d; //dec
      _args[3] = argv[3]->value.d; //ra_size
      _args[4] = argv[4]->value.d; //dec_size

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.siap.constructQueryS", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_siap_constructQuerySF(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //service
      _args[1] = argv[1]->value.d; //ra
      _args[2] = argv[2]->value.d; //dec
      _args[3] = argv[3]->value.d; //ra_size
      _args[4] = argv[4]->value.d; //dec_size
      _args[5] = argv[5]->value.str.s; //format

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.siap.constructQuerySF", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_siap_addOption(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //query
      _args[1] = argv[1]->value.str.s; //optionName
      _args[2] = argv[2]->value.str.s; //optionValue

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.siap.addOption", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_siap_execute(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //query

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.siap.execute", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_siap_executeVotable(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //query

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.siap.executeVotable", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static void IDL_ivoa_siap_executeAndSave(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //query
      _args[1] = argv[1]->value.str.s; //saveLocation
if (ARexecute("ivoa.siap.executeAndSave", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static IDL_VPTR IDL_ivoa_siap_saveDatasets(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //query
      _args[1] = argv[1]->value.str.s; //saveLocation

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.siap.saveDatasets", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_siap_saveDatasetsSubset(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //query
      _args[1] = argv[1]->value.str.s; //saveLocation
   //FIXME type too complex for IDL? - rows List

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.siap.saveDatasetsSubset", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_siap_getRegistryXQuery(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.siap.getRegistryXQuery", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_registry_adqlxSearch(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //adqlx

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.registry.adqlxSearch", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_registry_adqlsSearch(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //adqls

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.registry.adqlsSearch", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_registry_keywordSearch(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //keywords
      _args[1] = argv[1]->value.i; //orValues

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.registry.keywordSearch", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_registry_getResource(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //id

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.registry.getResource", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_registry_xquerySearch(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //xquery

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.registry.xquerySearch", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_registry_xquerySearchXML(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //xquery

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.registry.xquerySearchXML", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_registry_getIdentity(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.registry.getIdentity", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_registry_getSystemRegistryEndpoint(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.registry.getSystemRegistryEndpoint", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_registry_getFallbackSystemRegistryEndpoint(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.registry.getFallbackSystemRegistryEndpoint", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_externalRegistry_adqlxSearchXML(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //registry
      _args[1] = argv[1]->value.str.s; //adqlx
      _args[2] = argv[2]->value.i; //identifiersOnly

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.externalRegistry.adqlxSearchXML", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_externalRegistry_adqlxSearch(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //registry
      _args[1] = argv[1]->value.str.s; //adqlx

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.externalRegistry.adqlxSearch", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_externalRegistry_adqlsSearchXML(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //registry
      _args[1] = argv[1]->value.str.s; //adqls
      _args[2] = argv[2]->value.i; //identifiersOnly

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.externalRegistry.adqlsSearchXML", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_externalRegistry_adqlsSearch(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //registry
      _args[1] = argv[1]->value.str.s; //adqls

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.externalRegistry.adqlsSearch", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_externalRegistry_keywordSearchXML(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //registry
      _args[1] = argv[1]->value.str.s; //keywords
      _args[2] = argv[2]->value.i; //orValues

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.externalRegistry.keywordSearchXML", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_externalRegistry_keywordSearch(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //registry
      _args[1] = argv[1]->value.str.s; //keywords
      _args[2] = argv[2]->value.i; //orValues

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.externalRegistry.keywordSearch", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_externalRegistry_getResourceXML(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //registry
      _args[1] = argv[1]->value.str.s; //id

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.externalRegistry.getResourceXML", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_externalRegistry_getResource(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //registry
      _args[1] = argv[1]->value.str.s; //id

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.externalRegistry.getResource", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_externalRegistry_xquerySearchXML(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //registry
      _args[1] = argv[1]->value.str.s; //xquery

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.externalRegistry.xquerySearchXML", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_externalRegistry_xquerySearch(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //registry
      _args[1] = argv[1]->value.str.s; //xquery

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.externalRegistry.xquerySearch", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_externalRegistry_getIdentityXML(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //registry

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.externalRegistry.getIdentityXML", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_externalRegistry_getIdentity(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //registry

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.externalRegistry.getIdentity", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_externalRegistry_buildResources(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //doc

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.externalRegistry.buildResources", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_cone_addOption(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //query
      _args[1] = argv[1]->value.str.s; //optionName
      _args[2] = argv[2]->value.str.s; //optionValue

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.cone.addOption", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_cone_execute(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //query

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.cone.execute", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_cone_executeVotable(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //query

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.cone.executeVotable", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static void IDL_ivoa_cone_executeAndSave(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //query
      _args[1] = argv[1]->value.str.s; //saveLocation
if (ARexecute("ivoa.cone.executeAndSave", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static IDL_VPTR IDL_ivoa_cone_saveDatasets(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //query
      _args[1] = argv[1]->value.str.s; //saveLocation

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.cone.saveDatasets", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_cone_saveDatasetsSubset(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //query
      _args[1] = argv[1]->value.str.s; //saveLocation
   //FIXME type too complex for IDL? - rows List

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.cone.saveDatasetsSubset", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_cone_getRegistryXQuery(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.cone.getRegistryXQuery", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_ivoa_cone_constructQuery(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //service
      _args[1] = argv[1]->value.d; //ra
      _args[2] = argv[2]->value.d; //dec
      _args[3] = argv[3]->value.d; //sr

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.cone.constructQuery", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static void IDL_ivoa_cache_flush(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
if (ARexecute("ivoa.cache.flush", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static IDL_VPTR IDL_ivoa_adql_s2x(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //s

       IDL_VPTR idl_retval;
if (ARexecute("ivoa.adql.s2x", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_nvo_cone_constructQuery(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //service
      _args[1] = argv[1]->value.d; //ra
      _args[2] = argv[2]->value.d; //dec
      _args[3] = argv[3]->value.d; //sr

       IDL_VPTR idl_retval;
if (ARexecute("nvo.cone.constructQuery", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_nvo_cone_addOption(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //coneQuery
      _args[1] = argv[1]->value.str.s; //optionName
      _args[2] = argv[2]->value.str.s; //optionValue

       IDL_VPTR idl_retval;
if (ARexecute("nvo.cone.addOption", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_nvo_cone_getResults(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //coneQuery

       IDL_VPTR idl_retval;
if (ARexecute("nvo.cone.getResults", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static void IDL_nvo_cone_saveResults(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //coneQuery
      _args[1] = argv[1]->value.str.s; //saveLocation
if (ARexecute("nvo.cone.saveResults", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static IDL_VPTR IDL_nvo_cone_getRegistryQuery(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   

       IDL_VPTR idl_retval;
if (ARexecute("nvo.cone.getRegistryQuery", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_nvo_cone_getRegistryAdqlQuery(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   

       IDL_VPTR idl_retval;
if (ARexecute("nvo.cone.getRegistryAdqlQuery", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_nvo_cone_getRegistryXQuery(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   

       IDL_VPTR idl_retval;
if (ARexecute("nvo.cone.getRegistryXQuery", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static void IDL_userInterface_registryBrowser_show(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
if (ARexecute("userInterface.registryBrowser.show", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static void IDL_userInterface_registryBrowser_hide(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
if (ARexecute("userInterface.registryBrowser.hide", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static void IDL_userInterface_registryBrowser_search(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //xquery
if (ARexecute("userInterface.registryBrowser.search", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static void IDL_userInterface_registryBrowser_open(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //uri
if (ARexecute("userInterface.registryBrowser.open", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static void IDL_userInterface_queryBuilder_show(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
if (ARexecute("userInterface.queryBuilder.show", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static void IDL_userInterface_myspaceBrowser_show(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
if (ARexecute("userInterface.myspaceBrowser.show", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static void IDL_userInterface_myspaceBrowser_hide(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
if (ARexecute("userInterface.myspaceBrowser.hide", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static void IDL_userInterface_lookout_show(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
if (ARexecute("userInterface.lookout.show", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static void IDL_userInterface_lookout_hide(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
if (ARexecute("userInterface.lookout.hide", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static void IDL_userInterface_lookout_refresh(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
if (ARexecute("userInterface.lookout.refresh", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static void IDL_userInterface_jobMonitor_show(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
if (ARexecute("userInterface.jobMonitor.show", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static void IDL_userInterface_jobMonitor_hide(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
if (ARexecute("userInterface.jobMonitor.hide", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static void IDL_userInterface_jobMonitor_refresh(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
if (ARexecute("userInterface.jobMonitor.refresh", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static void IDL_userInterface_jobMonitor_addApplication(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //name
      _args[1] = argv[1]->value.str.s; //executionId
if (ARexecute("userInterface.jobMonitor.addApplication", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static void IDL_userInterface_jobMonitor_displayApplicationTab(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
if (ARexecute("userInterface.jobMonitor.displayApplicationTab", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static void IDL_userInterface_jobMonitor_displayJobTab(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
if (ARexecute("userInterface.jobMonitor.displayJobTab", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static void IDL_userInterface_fileManager_show(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
if (ARexecute("userInterface.fileManager.show", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static void IDL_userInterface_astroscope_show(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
if (ARexecute("userInterface.astroscope.show", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static void IDL_userInterface_applicationLauncher_show(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
if (ARexecute("userInterface.applicationLauncher.show", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static void IDL_util_tables_convertFiles(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //inLocation
      _args[1] = argv[1]->value.str.s; //inFormat
      _args[2] = argv[2]->value.str.s; //outLocation
      _args[3] = argv[3]->value.str.s; //outFormat
if (ARexecute("util.tables.convertFiles", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static void IDL_util_tables_convertToFile(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //input
      _args[1] = argv[1]->value.str.s; //inFormat
      _args[2] = argv[2]->value.str.s; //outLocation
      _args[3] = argv[3]->value.str.s; //outFormat
if (ARexecute("util.tables.convertToFile", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static IDL_VPTR IDL_util_tables_convertFromFile(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //inLocation
      _args[1] = argv[1]->value.str.s; //inFormat
      _args[2] = argv[2]->value.str.s; //outFormat

       IDL_VPTR idl_retval;
if (ARexecute("util.tables.convertFromFile", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_util_tables_convert(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //input
      _args[1] = argv[1]->value.str.s; //inFormat
      _args[2] = argv[2]->value.str.s; //outFormat

       IDL_VPTR idl_retval;
if (ARexecute("util.tables.convert", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_util_tables_listOutputFormats(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   

       IDL_VPTR idl_retval;
if (ARexecute("util.tables.listOutputFormats", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_util_tables_listInputFormats(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   

       IDL_VPTR idl_retval;
if (ARexecute("util.tables.listInputFormats", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static void IDL_votech_vomon_reload(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
if (ARexecute("votech.vomon.reload", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
}
   };
static IDL_VPTR IDL_votech_vomon_checkAvailability(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //id

       IDL_VPTR idl_retval;
if (ARexecute("votech.vomon.checkAvailability", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_VPTR IDL_votech_vomon_checkCeaAvailability(int argc, IDL_VPTR *argv, char* argk)
{
   XmlRpcValue _args, _result;
   
      _args[0] = argv[0]->value.str.s; //id

       IDL_VPTR idl_retval;
if (ARexecute("votech.vomon.checkCeaAvailability", _args, _result))
       {
         IDLBase * idls = IDLBase::factory(_result);
idl_retval = idls->makeIDLVar("");
        }
     return idl_retval;
    
   };
static IDL_SYSFUN_DEF2 procedure_addr[] = {
 { (IDL_SYSRTN_GENERIC) IDL_astrogrid_stap_executeAndSave,"ASTROGRID_STAP_EXECUTEANDSAVE", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_processManager_halt,"ASTROGRID_PROCESSMANAGER_HALT", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_processManager_delete,"ASTROGRID_PROCESSMANAGER_DELETE", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_createFile,"ASTROGRID_MYSPACE_CREATEFILE", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_createFolder,"ASTROGRID_MYSPACE_CREATEFOLDER", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_refresh,"ASTROGRID_MYSPACE_REFRESH", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_delete,"ASTROGRID_MYSPACE_DELETE", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_changeStore,"ASTROGRID_MYSPACE_CHANGESTORE", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_write,"ASTROGRID_MYSPACE_WRITE", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_writeBinary,"ASTROGRID_MYSPACE_WRITEBINARY", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_transferCompleted,"ASTROGRID_MYSPACE_TRANSFERCOMPLETED", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_copyContentToURL,"ASTROGRID_MYSPACE_COPYCONTENTTOURL", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_copyURLToContent,"ASTROGRID_MYSPACE_COPYURLTOCONTENT", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_jobs_cancelJob,"ASTROGRID_JOBS_CANCELJOB", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_jobs_deleteJob,"ASTROGRID_JOBS_DELETEJOB", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_community_login,"ASTROGRID_COMMUNITY_LOGIN", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_community_logout,"ASTROGRID_COMMUNITY_LOGOUT", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_community_guiLogin,"ASTROGRID_COMMUNITY_GUILOGIN", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_applications_validate,"ASTROGRID_APPLICATIONS_VALIDATE", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_applications_validateStored,"ASTROGRID_APPLICATIONS_VALIDATESTORED", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_applications_cancel,"ASTROGRID_APPLICATIONS_CANCEL", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_ssap_executeAndSave,"IVOA_SSAP_EXECUTEANDSAVE", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_siap_executeAndSave,"IVOA_SIAP_EXECUTEANDSAVE", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_cone_executeAndSave,"IVOA_CONE_EXECUTEANDSAVE", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_cache_flush,"IVOA_CACHE_FLUSH", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_nvo_cone_saveResults,"NVO_CONE_SAVERESULTS", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_userInterface_registryBrowser_show,"USERINTERFACE_REGISTRYBROWSER_SHOW", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_userInterface_registryBrowser_hide,"USERINTERFACE_REGISTRYBROWSER_HIDE", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_userInterface_registryBrowser_search,"USERINTERFACE_REGISTRYBROWSER_SEARCH", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_userInterface_registryBrowser_open,"USERINTERFACE_REGISTRYBROWSER_OPEN", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_userInterface_queryBuilder_show,"USERINTERFACE_QUERYBUILDER_SHOW", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_userInterface_myspaceBrowser_show,"USERINTERFACE_MYSPACEBROWSER_SHOW", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_userInterface_myspaceBrowser_hide,"USERINTERFACE_MYSPACEBROWSER_HIDE", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_userInterface_lookout_show,"USERINTERFACE_LOOKOUT_SHOW", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_userInterface_lookout_hide,"USERINTERFACE_LOOKOUT_HIDE", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_userInterface_lookout_refresh,"USERINTERFACE_LOOKOUT_REFRESH", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_userInterface_jobMonitor_show,"USERINTERFACE_JOBMONITOR_SHOW", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_userInterface_jobMonitor_hide,"USERINTERFACE_JOBMONITOR_HIDE", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_userInterface_jobMonitor_refresh,"USERINTERFACE_JOBMONITOR_REFRESH", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_userInterface_jobMonitor_addApplication,"USERINTERFACE_JOBMONITOR_ADDAPPLICATION", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_userInterface_jobMonitor_displayApplicationTab,"USERINTERFACE_JOBMONITOR_DISPLAYAPPLICATIONTAB", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_userInterface_jobMonitor_displayJobTab,"USERINTERFACE_JOBMONITOR_DISPLAYJOBTAB", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_userInterface_fileManager_show,"USERINTERFACE_FILEMANAGER_SHOW", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_userInterface_astroscope_show,"USERINTERFACE_ASTROSCOPE_SHOW", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_userInterface_applicationLauncher_show,"USERINTERFACE_APPLICATIONLAUNCHER_SHOW", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_util_tables_convertFiles,"UTIL_TABLES_CONVERTFILES", 4, 4, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_util_tables_convertToFile,"UTIL_TABLES_CONVERTTOFILE", 4, 4, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_votech_vomon_reload,"VOTECH_VOMON_RELOAD", 0, 0, 0, 0 },

      { (IDL_SYSRTN_GENERIC) IDL_ar_init, "AR_INIT", 0, 0, 0, 0},
      { (IDL_SYSRTN_GENERIC) NULL, "", 0, 0, 0, 0}
  
};

static IDL_SYSFUN_DEF2 function_addr[] = {
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_stap_constructQuery,"ASTROGRID_STAP_CONSTRUCTQUERY", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_stap_constructQueryF,"ASTROGRID_STAP_CONSTRUCTQUERYF", 4, 4, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_stap_constructQueryP,"ASTROGRID_STAP_CONSTRUCTQUERYP", 6, 6, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_stap_constructQueryPF,"ASTROGRID_STAP_CONSTRUCTQUERYPF", 7, 7, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_stap_constructQueryS,"ASTROGRID_STAP_CONSTRUCTQUERYS", 7, 7, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_stap_constructQuerySF,"ASTROGRID_STAP_CONSTRUCTQUERYSF", 8, 8, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_stap_addOption,"ASTROGRID_STAP_ADDOPTION", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_stap_execute,"ASTROGRID_STAP_EXECUTE", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_stap_executeVotable,"ASTROGRID_STAP_EXECUTEVOTABLE", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_stap_saveDatasets,"ASTROGRID_STAP_SAVEDATASETS", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_stap_saveDatasetsSubset,"ASTROGRID_STAP_SAVEDATASETSSUBSET", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_stap_getRegistryXQuery,"ASTROGRID_STAP_GETREGISTRYXQUERY", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_processManager_list,"ASTROGRID_PROCESSMANAGER_LIST", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_processManager_submit,"ASTROGRID_PROCESSMANAGER_SUBMIT", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_processManager_submitTo,"ASTROGRID_PROCESSMANAGER_SUBMITTO", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_processManager_submitStored,"ASTROGRID_PROCESSMANAGER_SUBMITSTORED", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_processManager_submitStoredTo,"ASTROGRID_PROCESSMANAGER_SUBMITSTOREDTO", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_processManager_getExecutionInformation,"ASTROGRID_PROCESSMANAGER_GETEXECUTIONINFORMATION", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_processManager_getMessages,"ASTROGRID_PROCESSMANAGER_GETMESSAGES", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_processManager_getResults,"ASTROGRID_PROCESSMANAGER_GETRESULTS", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_processManager_getSingleResult,"ASTROGRID_PROCESSMANAGER_GETSINGLERESULT", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_getHome,"ASTROGRID_MYSPACE_GETHOME", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_exists,"ASTROGRID_MYSPACE_EXISTS", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_getNodeInformation,"ASTROGRID_MYSPACE_GETNODEINFORMATION", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_createChildFolder,"ASTROGRID_MYSPACE_CREATECHILDFOLDER", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_createChildFile,"ASTROGRID_MYSPACE_CREATECHILDFILE", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_getParent,"ASTROGRID_MYSPACE_GETPARENT", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_list,"ASTROGRID_MYSPACE_LIST", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_listIvorns,"ASTROGRID_MYSPACE_LISTIVORNS", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_listNodeInformation,"ASTROGRID_MYSPACE_LISTNODEINFORMATION", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_rename,"ASTROGRID_MYSPACE_RENAME", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_move,"ASTROGRID_MYSPACE_MOVE", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_copy,"ASTROGRID_MYSPACE_COPY", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_read,"ASTROGRID_MYSPACE_READ", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_readBinary,"ASTROGRID_MYSPACE_READBINARY", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_getReadContentURL,"ASTROGRID_MYSPACE_GETREADCONTENTURL", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_getWriteContentURL,"ASTROGRID_MYSPACE_GETWRITECONTENTURL", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_myspace_listStores,"ASTROGRID_MYSPACE_LISTSTORES", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_jobs_list,"ASTROGRID_JOBS_LIST", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_jobs_listFully,"ASTROGRID_JOBS_LISTFULLY", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_jobs_createJob,"ASTROGRID_JOBS_CREATEJOB", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_jobs_wrapTask,"ASTROGRID_JOBS_WRAPTASK", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_jobs_getJobTranscript,"ASTROGRID_JOBS_GETJOBTRANSCRIPT", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_jobs_getJobInformation,"ASTROGRID_JOBS_GETJOBINFORMATION", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_jobs_submitJob,"ASTROGRID_JOBS_SUBMITJOB", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_jobs_submitStoredJob,"ASTROGRID_JOBS_SUBMITSTOREDJOB", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_community_getUserInformation,"ASTROGRID_COMMUNITY_GETUSERINFORMATION", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_community_isLoggedIn,"ASTROGRID_COMMUNITY_ISLOGGEDIN", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_applications_list,"ASTROGRID_APPLICATIONS_LIST", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_applications_getQueryToListApplications,"ASTROGRID_APPLICATIONS_GETQUERYTOLISTAPPLICATIONS", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_applications_getRegistryQuery,"ASTROGRID_APPLICATIONS_GETREGISTRYQUERY", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_applications_getRegistryAdqlQuery,"ASTROGRID_APPLICATIONS_GETREGISTRYADQLQUERY", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_applications_getRegistryXQuery,"ASTROGRID_APPLICATIONS_GETREGISTRYXQUERY", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_applications_getCeaApplication,"ASTROGRID_APPLICATIONS_GETCEAAPPLICATION", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_applications_getDocumentation,"ASTROGRID_APPLICATIONS_GETDOCUMENTATION", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_applications_createTemplateDocument,"ASTROGRID_APPLICATIONS_CREATETEMPLATEDOCUMENT", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_applications_createTemplateStruct,"ASTROGRID_APPLICATIONS_CREATETEMPLATESTRUCT", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_applications_convertDocumentToStruct,"ASTROGRID_APPLICATIONS_CONVERTDOCUMENTTOSTRUCT", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_applications_convertStructToDocument,"ASTROGRID_APPLICATIONS_CONVERTSTRUCTTODOCUMENT", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_applications_listServersProviding,"ASTROGRID_APPLICATIONS_LISTSERVERSPROVIDING", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_applications_submit,"ASTROGRID_APPLICATIONS_SUBMIT", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_applications_submitTo,"ASTROGRID_APPLICATIONS_SUBMITTO", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_applications_submitStored,"ASTROGRID_APPLICATIONS_SUBMITSTORED", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_applications_submitStoredTo,"ASTROGRID_APPLICATIONS_SUBMITSTOREDTO", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_applications_getExecutionInformation,"ASTROGRID_APPLICATIONS_GETEXECUTIONINFORMATION", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_astrogrid_applications_getResults,"ASTROGRID_APPLICATIONS_GETRESULTS", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_cds_vizier_cataloguesMetaData,"CDS_VIZIER_CATALOGUESMETADATA", 4, 4, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_cds_vizier_cataloguesMetaDataWavelength,"CDS_VIZIER_CATALOGUESMETADATAWAVELENGTH", 5, 5, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_cds_vizier_cataloguesData,"CDS_VIZIER_CATALOGUESDATA", 4, 4, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_cds_vizier_cataloguesDataWavelength,"CDS_VIZIER_CATALOGUESDATAWAVELENGTH", 5, 5, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_cds_vizier_metaAll,"CDS_VIZIER_METAALL", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_cds_ucd_UCDList,"CDS_UCD_UCDLIST", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_cds_ucd_resolveUCD,"CDS_UCD_RESOLVEUCD", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_cds_ucd_UCDofCatalog,"CDS_UCD_UCDOFCATALOG", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_cds_ucd_translate,"CDS_UCD_TRANSLATE", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_cds_ucd_upgrade,"CDS_UCD_UPGRADE", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_cds_ucd_validate,"CDS_UCD_VALIDATE", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_cds_ucd_explain,"CDS_UCD_EXPLAIN", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_cds_ucd_assign,"CDS_UCD_ASSIGN", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_cds_sesame_resolve,"CDS_SESAME_RESOLVE", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_cds_sesame_sesame,"CDS_SESAME_SESAME", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_cds_sesame_sesameChooseService,"CDS_SESAME_SESAMECHOOSESERVICE", 4, 4, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_cds_glu_getURLfromTag,"CDS_GLU_GETURLFROMTAG", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_cds_coordinate_convert,"CDS_COORDINATE_CONVERT", 4, 4, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_cds_coordinate_convertL,"CDS_COORDINATE_CONVERTL", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_cds_coordinate_convertE,"CDS_COORDINATE_CONVERTE", 8, 8, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_cds_coordinate_convertLE,"CDS_COORDINATE_CONVERTLE", 7, 7, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_dialogs_toolEditor_edit,"DIALOGS_TOOLEDITOR_EDIT", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_dialogs_toolEditor_editStored,"DIALOGS_TOOLEDITOR_EDITSTORED", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_dialogs_toolEditor_selectAndBuild,"DIALOGS_TOOLEDITOR_SELECTANDBUILD", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_dialogs_resourceChooser_chooseResource,"DIALOGS_RESOURCECHOOSER_CHOOSERESOURCE", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_dialogs_resourceChooser_chooseFolder,"DIALOGS_RESOURCECHOOSER_CHOOSEFOLDER", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_dialogs_resourceChooser_fullChooseResource,"DIALOGS_RESOURCECHOOSER_FULLCHOOSERESOURCE", 4, 4, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_dialogs_resourceChooser_fullChooseFolder,"DIALOGS_RESOURCECHOOSER_FULLCHOOSEFOLDER", 4, 4, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_dialogs_registryGoogle_selectResourcesXQueryFilter,"DIALOGS_REGISTRYGOOGLE_SELECTRESOURCESXQUERYFILTER", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_dialogs_registryGoogle_selectResourcesFromList,"DIALOGS_REGISTRYGOOGLE_SELECTRESOURCESFROMLIST", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_vospace_getHome,"IVOA_VOSPACE_GETHOME", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_vospace_getNodeInformation,"IVOA_VOSPACE_GETNODEINFORMATION", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_vospace_listVOSpaces,"IVOA_VOSPACE_LISTVOSPACES", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_ssap_constructQuery,"IVOA_SSAP_CONSTRUCTQUERY", 4, 4, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_ssap_constructQueryS,"IVOA_SSAP_CONSTRUCTQUERYS", 5, 5, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_ssap_addOption,"IVOA_SSAP_ADDOPTION", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_ssap_execute,"IVOA_SSAP_EXECUTE", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_ssap_executeVotable,"IVOA_SSAP_EXECUTEVOTABLE", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_ssap_saveDatasets,"IVOA_SSAP_SAVEDATASETS", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_ssap_saveDatasetsSubset,"IVOA_SSAP_SAVEDATASETSSUBSET", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_ssap_getRegistryXQuery,"IVOA_SSAP_GETREGISTRYXQUERY", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_siap_constructQuery,"IVOA_SIAP_CONSTRUCTQUERY", 4, 4, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_siap_constructQueryF,"IVOA_SIAP_CONSTRUCTQUERYF", 5, 5, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_siap_constructQueryS,"IVOA_SIAP_CONSTRUCTQUERYS", 5, 5, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_siap_constructQuerySF,"IVOA_SIAP_CONSTRUCTQUERYSF", 6, 6, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_siap_addOption,"IVOA_SIAP_ADDOPTION", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_siap_execute,"IVOA_SIAP_EXECUTE", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_siap_executeVotable,"IVOA_SIAP_EXECUTEVOTABLE", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_siap_saveDatasets,"IVOA_SIAP_SAVEDATASETS", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_siap_saveDatasetsSubset,"IVOA_SIAP_SAVEDATASETSSUBSET", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_siap_getRegistryXQuery,"IVOA_SIAP_GETREGISTRYXQUERY", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_registry_adqlxSearch,"IVOA_REGISTRY_ADQLXSEARCH", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_registry_adqlsSearch,"IVOA_REGISTRY_ADQLSSEARCH", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_registry_keywordSearch,"IVOA_REGISTRY_KEYWORDSEARCH", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_registry_getResource,"IVOA_REGISTRY_GETRESOURCE", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_registry_xquerySearch,"IVOA_REGISTRY_XQUERYSEARCH", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_registry_xquerySearchXML,"IVOA_REGISTRY_XQUERYSEARCHXML", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_registry_getIdentity,"IVOA_REGISTRY_GETIDENTITY", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_registry_getSystemRegistryEndpoint,"IVOA_REGISTRY_GETSYSTEMREGISTRYENDPOINT", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_registry_getFallbackSystemRegistryEndpoint,"IVOA_REGISTRY_GETFALLBACKSYSTEMREGISTRYENDPOINT", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_externalRegistry_adqlxSearchXML,"IVOA_EXTERNALREGISTRY_ADQLXSEARCHXML", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_externalRegistry_adqlxSearch,"IVOA_EXTERNALREGISTRY_ADQLXSEARCH", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_externalRegistry_adqlsSearchXML,"IVOA_EXTERNALREGISTRY_ADQLSSEARCHXML", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_externalRegistry_adqlsSearch,"IVOA_EXTERNALREGISTRY_ADQLSSEARCH", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_externalRegistry_keywordSearchXML,"IVOA_EXTERNALREGISTRY_KEYWORDSEARCHXML", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_externalRegistry_keywordSearch,"IVOA_EXTERNALREGISTRY_KEYWORDSEARCH", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_externalRegistry_getResourceXML,"IVOA_EXTERNALREGISTRY_GETRESOURCEXML", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_externalRegistry_getResource,"IVOA_EXTERNALREGISTRY_GETRESOURCE", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_externalRegistry_xquerySearchXML,"IVOA_EXTERNALREGISTRY_XQUERYSEARCHXML", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_externalRegistry_xquerySearch,"IVOA_EXTERNALREGISTRY_XQUERYSEARCH", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_externalRegistry_getIdentityXML,"IVOA_EXTERNALREGISTRY_GETIDENTITYXML", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_externalRegistry_getIdentity,"IVOA_EXTERNALREGISTRY_GETIDENTITY", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_externalRegistry_buildResources,"IVOA_EXTERNALREGISTRY_BUILDRESOURCES", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_cone_addOption,"IVOA_CONE_ADDOPTION", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_cone_execute,"IVOA_CONE_EXECUTE", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_cone_executeVotable,"IVOA_CONE_EXECUTEVOTABLE", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_cone_saveDatasets,"IVOA_CONE_SAVEDATASETS", 2, 2, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_cone_saveDatasetsSubset,"IVOA_CONE_SAVEDATASETSSUBSET", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_cone_getRegistryXQuery,"IVOA_CONE_GETREGISTRYXQUERY", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_cone_constructQuery,"IVOA_CONE_CONSTRUCTQUERY", 4, 4, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_ivoa_adql_s2x,"IVOA_ADQL_S2X", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_nvo_cone_constructQuery,"NVO_CONE_CONSTRUCTQUERY", 4, 4, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_nvo_cone_addOption,"NVO_CONE_ADDOPTION", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_nvo_cone_getResults,"NVO_CONE_GETRESULTS", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_nvo_cone_getRegistryQuery,"NVO_CONE_GETREGISTRYQUERY", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_nvo_cone_getRegistryAdqlQuery,"NVO_CONE_GETREGISTRYADQLQUERY", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_nvo_cone_getRegistryXQuery,"NVO_CONE_GETREGISTRYXQUERY", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_util_tables_convertFromFile,"UTIL_TABLES_CONVERTFROMFILE", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_util_tables_convert,"UTIL_TABLES_CONVERT", 3, 3, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_util_tables_listOutputFormats,"UTIL_TABLES_LISTOUTPUTFORMATS", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_util_tables_listInputFormats,"UTIL_TABLES_LISTINPUTFORMATS", 0, 0, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_votech_vomon_checkAvailability,"VOTECH_VOMON_CHECKAVAILABILITY", 1, 1, 0, 0 },
{ (IDL_SYSRTN_GENERIC) IDL_votech_vomon_checkCeaAvailability,"VOTECH_VOMON_CHECKCEAAVAILABILITY", 1, 1, 0, 0 },

     { (IDL_SYSRTN_GENERIC) NULL, "", 0, 0, 0, 0}
 
};

/*****************************************************************************/


extern "C" {
//    __declspec(dllexport) int IDL_Load(void)
     int IDL_Load(void)
    {
    
    
    
        return IDL_SysRtnAdd(function_addr, TRUE, IDL_CARRAY_ELTS(function_addr)-1)
            && IDL_SysRtnAdd(procedure_addr, FALSE, IDL_CARRAY_ELTS(procedure_addr)-1);
    }
   
}

 