//to run myspacedemo:
//cd /home/clq2/develop/eclipse/workspace/mySpace
//java org.astrogrid.mySpace.mySpaceDemo.MySpaceDemo /home/clq2/develop/MSRegistryTestDir/example

//dataholder file:
//cd /tmp/mySpaceTest

//registry conf files:
///home/clq2/develop/MSRegistryTestDir
///home/clq2/develop/MSRegistryTestDir/originalsReg


import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.encoding.XMLType;
import org.apache.log4j.Logger;

import java.util.Iterator;

//java
//import java.io.PrintWriter;
//import java.io.File;
//import java.io.FileOutputStream;
 
public class MySpaceManagerClient
{
	private static Logger logger = Logger.getLogger(MySpaceServerManagerClient.class);
	private static boolean DEBUG = true;
	private static Call call = null;

	public static void main(String[] args)
	{
		String b = "";
		String arg = args[0];
		System.out.println("arg = " +arg);
		try{
			if(arg.equals("lookupDataHoldersDetails")){
				b = invokeLookUpDataHoldersDetails();		
			}else if(arg.equals("upLoad")){
				b = MySpaceManagerClient.invokeUpload();
			}else if(arg.equals("copyDataHolder")){
				b = MySpaceManagerClient.invokeCopy();
			}else if(arg.equals("deleteDataHolder")){
				b = MySpaceManagerClient.invokeDelete();
			}else if(arg.equals("moveDataHolder")){
				b = MySpaceManagerClient.invokeMove();
		}
			System.out.println("main args invokes: " +b );			

		}

		catch (Exception e)
		{
			e.printStackTrace();
			System.err.println("Exception caught: " + e);
		}
	}
	private static String invokeUpload(){
		String b = "";
		try
		{	
			//upload data holder
			String request =//<?xml version='1.0' encoding='UTF-8'?> \
			"<request>" +
				"<elements>" +
					"<userID>clq</userID>" +
					"<communityID>Leicester</communityID>" +
					"<jobID>testIDIt2</jobID>" +
					"<mySpaceAction>upLoad</mySpaceAction>" +
					"<dataItemID>10</dataItemID>" +
					"<oldDataItemID>10</oldDataItemID>" +
					"<newDataItemName>xx</newDataItemName>" +
					"<newContainerName>x</newContainerName>" +
					"<query>x</query>" +
					"<newDataHolderName>/clq/serv2/table106</newDataHolderName>" +
					"<serverFileName>/tmp/test1</serverFileName>" +
					"<fileSize>1</fileSize>" +
				"</elements>" +
			"</request>"; 
			call = createcall();
			//String endpoint  = "http://localhost:8080/axis/services/MySpaceManager";
			//Service service = new Service();
			//org.apache.axis.client.Call call = (Call)service.createCall();
			//call.setTargetEndpointAddress( new java.net.URL(endpoint) );
			call.setOperationName( "upLoad" );
			
			call.addParameter("arg0", XMLType.XSD_STRING, ParameterMode.IN);
			
			call.setReturnType( org.apache.axis.encoding.XMLType.XSD_STRING);
			b = (String) call.invoke( new Object[] { request } );

			System.out.println("inside MySpaceManagerClient test upLoad data holder, " +b );
			
			System.out.println("response message: "+call.getResponseMessage().toString());

		}

		catch (Exception e)
		{
			e.printStackTrace();
			System.err.println("Exception caught: " + e);
		}
		return b;
	}
	
	private static String invokeLookUpDataHoldersDetails(){
		String b = "";
		try
		{	
			//upload data holder
			String request ="<?xml version='1.0' encoding='UTF-8'?>" +
			"<request>" +
				"<elements>" +
					"<userID>clq</userID>" +
					"<communityID>Leicester</communityID>" +
					"<jobID>testIDIt2</jobID>" +
					"<mySpaceAction>upLoad</mySpaceAction>" +
					"<dataItemID>10</dataItemID>" +
					"<oldDataItemID>10</oldDataItemID>" +
					"<newDataItemName>xx</newDataItemName>" +
					"<newContainerName>x</newContainerName>" +
					"<query>/clq*</query>" +
					"<newDataHolderName>x</newDataHolderName>" +
					"<serverFileName>x</serverFileName>" +
					"<fileSize>x</fileSize>" +
				"</elements>" +
			"</request>"; 
			call = createcall();
			//String endpoint  = "http://localhost:8080/axis/services/MySpaceManager";
			//Service service = new Service();
			//org.apache.axis.client.Call call = (Call)service.createCall();
			//call.setTargetEndpointAddress( new java.net.URL(endpoint) );
			call.setOperationName( "lookupDataHoldersDetails" );
			
			call.addParameter("arg0", XMLType.XSD_STRING, ParameterMode.IN);
			
			call.setReturnType( org.apache.axis.encoding.XMLType.XSD_STRING);
			b = (String) call.invoke( new Object[] { request } );

			System.out.println("inside MySpaceManagerClient test lookupDataHoldersDetails data holder, " +b );			
		}

		catch (Exception e)
		{
			e.printStackTrace();
			System.err.println("Exception caught: " + e);
		}
		return b;
	}
		
	
	private static Call createcall(){
		org.apache.axis.client.Call call = null;
		try{
			String endpoint  = "http://localhost:8080/axis/services/MySpaceManager";
			Service service = new Service();
			call = (Call)service.createCall();
			call.setTargetEndpointAddress( new java.net.URL(endpoint) );
		}catch(Exception e){
		//..
		}
		return call;		
	}
	
	private static String invokeCopy(){
		String b = "";
		try
		{	
			//upload data holder
			String request ="<?xml version='1.0' encoding='UTF-8'?>" +
			"<request>" +
				"<elements>" +
					"<userID>clq</userID>" +
					"<communityID>Leicester</communityID>" +
					"<jobID>testIDIt2</jobID>" +
					"<mySpaceAction>copyDataHolder</mySpaceAction>" +
					"<dataItemID> </dataItemID>" +
					"<oldDataItemID>0</oldDataItemID>" +
					"<newDataItemName>/clq/serv2/table37</newDataItemName>" + //destenation of copying eg./clq/serv2/table23
					"<newContainerName>x</newContainerName>" +
					"<query>x</query>" +
					"<newDataHolderName>xx</newDataHolderName>" +
					"<serverFileName>/clq/serv1/table5</serverFileName>" +
					"<fileSize>1</fileSize>" +
				"</elements>" +
			"</request>"; 
			call = createcall();
			call.setOperationName( "copyDataHolder" );		
			call.addParameter("arg0", XMLType.XSD_STRING, ParameterMode.IN);
			
			call.setReturnType( org.apache.axis.encoding.XMLType.XSD_STRING);
			b = (String) call.invoke( new Object[] { request } );

			System.out.println("inside MySpaceManagerClient test copy data holder, " +b );

		}

		catch (Exception e)
		{
			e.printStackTrace();
			System.err.println("Exception caught: " + e);
		}
		return b;
	}	
	
	private static String invokeDelete(){
		String b = "";
		try
		{	
			//upload data holder
			String request ="<?xml version='1.0' encoding='UTF-8'?>" +
			"<request>" +
				"<elements>" +
					"<userID>clq</userID>" +
					"<communityID>Leicester</communityID>" +
					"<jobID>testIDIt10</jobID>" +
					"<mySpaceAction>deleteDataHolder</mySpaceAction>" +
					"<dataItemID></dataItemID>" +
					"<oldDataItemID></oldDataItemID>" +
					"<newDataItemName></newDataItemName>" + 
					"<newContainerName>x</newContainerName>" +
					"<query>x</query>" +
					"<newDataHolderName>xx</newDataHolderName>" +
					"<serverFileName>/clq/serv2/table26</serverFileName>" + //file that will be deleted
					"<fileSize>1</fileSize>" +
				"</elements>" +
			"</request>"; 
			call = createcall();
			//String endpoint  = "http://localhost:8080/axis/services/MySpaceManager";
			//Service service = new Service();
			//org.apache.axis.client.Call call = (Call)service.createCall();
			//call.setTargetEndpointAddress( new java.net.URL(endpoint) );
			call.setOperationName( "deleteDataHolder" );
			
			call.addParameter("arg0", XMLType.XSD_STRING, ParameterMode.IN);
			
			call.setReturnType( org.apache.axis.encoding.XMLType.XSD_STRING);
			b = (String) call.invoke( new Object[] { request } );

			System.out.println("inside MySpaceManagerClient test delete data holder, " +b );

		}

		catch (Exception e)
		{
			e.printStackTrace();
			System.err.println("Exception caught: " + e);
		}
		return b;		
	}
	
	private static String invokeMove(){
		String b = "";
		try
		{	
			//upload data holder
			String request ="<?xml version='1.0' encoding='UTF-8'?>" +
			"<request>" +
				"<elements>" +
					"<userID>clq</userID>" +
					"<communityID>Leicester</communityID>" +
					"<jobID>testIDIt2</jobID>" +
					"<mySpaceAction>copyDataHolder</mySpaceAction>" +
					"<dataItemID> </dataItemID>" +
					"<oldDataItemID>0</oldDataItemID>" +
					"<newDataItemName>/clq/serv2/table303</newDataItemName>" + //destenation of moving eg./clq/serv2/table23
					"<newContainerName>x</newContainerName>" +
					"<query>x</query>" +
					"<newDataHolderName>xx</newDataHolderName>" +
					"<serverFileName>/clq/serv2/table106</serverFileName>" + // from
					"<fileSize>1</fileSize>" +
				"</elements>" +
			"</request>"; 
			call = createcall();
			//String endpoint  = "http://localhost:8080/axis/services/MySpaceManager";
			//Service service = new Service();
			//org.apache.axis.client.Call call = (Call)service.createCall();
			//call.setTargetEndpointAddress( new java.net.URL(endpoint) );
			call.setOperationName( "moveDataHolder" );
			
			call.addParameter("arg0", XMLType.XSD_STRING, ParameterMode.IN);
			
			call.setReturnType( org.apache.axis.encoding.XMLType.XSD_STRING);
			b = (String) call.invoke( new Object[] { request } );

			System.out.println("inside MySpaceManagerClient test move data holder, " +b );

		}

		catch (Exception e)
		{
			e.printStackTrace();
			System.err.println("Exception caught: " + e);
		}
		return b;		
	}	
}