//To run
//cd /home/clq2/develop/eclipse/workspace/mySpaceClient
//./runCallDelegate.sh listDataHoldingsGen

//To build a new jar 
//cd /home/clq2/develop/eclipse/jars
//cp /home/clq2/develop/eclipse/workspace/mySpace2/org/astrogrid/mySpace/delegate/mySpaceManager/MySpaceManagerDelegate.class ./org/astrogrid/mySpace/delegate/mySpaceManager
//cp /home/clq2/develop/eclipse/workspace/mySpace2/org/astrogrid/mySpace/delegate/helper/MySpaceHelper.class ./org/astrogrid/mySpace/delegate/helper/
//jar -cvf mySpaceDelegate.jar ./org/

package mySpace.test.java;
import java.util.Vector;

import org.astrogrid.mySpace.delegate.helper.*;
import org.astrogrid.mySpace.delegate.mySpaceManager.*;

public class MySpaceCallDelegate
{
	private static MySpaceManagerDelegate delegate = new MySpaceManagerDelegate("http://localhost:8080/axis/services/MySpaceManager");
	public static void main(String[] args)
		{		
			String b = "";
			String arg = args[0];
			Vector v = new Vector();
			System.out.println("arg = " +arg);
			try{
				if(arg.equalsIgnoreCase("listdataholdings")){
					v = invokeListDataholdings();	
				}else if(arg.equalsIgnoreCase("listDataHoldingsGen")){
					v = invokeListDataholdingsGen();
				}else if(arg.equalsIgnoreCase("copyDataHolder")){
					//b = MySpaceManagerClient.invokeCopy();
				}else if(arg.equalsIgnoreCase("deleteDataHolder")){
					//b = MySpaceManagerClient.invokeDelete();
				}else if(arg.equalsIgnoreCase("moveDataHolder")){
					//b = MySpaceManagerClient.invokeMove();
				}else if(arg.equalsIgnoreCase("lookupDataHolderDetails")){
					//b = invokeLookUpDataHolderDetails();
				}else if(arg.equalsIgnoreCase("exportDataHolder")){
					//b = invokeExportDataHolder();
				}else if(arg.equalsIgnoreCase("createContainer")){
					//b = invokeCreateContainer();
				}else if(arg.equalsIgnoreCase("extendLease")){
					//b = invokeExtendLease();
				}
				System.out.println("main args invokes: " +b );			
			}

			catch (Exception e)
			{
				e.printStackTrace();
				System.err.println("Exception caught in main: " + e);
			}
		}
		
	private static Vector invokeListDataholdings(){
		Vector vec = new Vector();
		try{
			System.out.println("invokeListDataholdings... entre()");
			vec = delegate.listDataHoldings(" ", " ", "/ktn@leicester/serv1/workflow/*");
			if (!(vec.isEmpty())){
				for (int i =0;i<vec.size();i++){
					System.out.println("vec("+i+") = "+vec.elementAt(i));
				}
			}
		}catch(Exception e){
			System.out.println("Exception while MySpaceCallDelegate.invokeListDataholdings: "+e.toString());
		}
		return vec;
	}
	
	private static Vector invokeListDataholdingsGen(){
		Vector vec = new Vector();
		try{
		    vec = delegate.listDataHoldingsGen(" ", " ", "*");
			if (!(vec.isEmpty())){
				for (int i =0;i<vec.size();i++){
					System.out.println("vec("+i+") = "+vec.elementAt(i));
				}
			}
		}catch(Exception e){
			System.out.println("Exception while MySpaceCallDelegate.invokeListDataholdingsGen: "+e.toString());
		}
		return vec;
	}
		
}