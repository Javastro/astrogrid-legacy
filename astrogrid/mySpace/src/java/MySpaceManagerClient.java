//to run myspacedemo on qin.star.le.ac.uk:
//cd /home/clq2/develop/eclipse/workspace/mySpace
//java org.astrogrid.mySpace.mySpaceDemo.MySpaceDemo /home/clq2/develop/MSRegistryTestDir/example

//to run myspacedemo on hydra:
// cd /usr/local/jakarta-tomcat-4.1.24/webapps/axis/WEB-INF/classes
// /usr/java/j2sdk1.4.1_02/bin/java -cp $CALSSPATH:/usr/local/ogsa-tp4/lib/axis.jar:/usr/local/ogsa-tp4/lib/jaxrpc.jar:/usr/local/jakarta-tomcat-4.1.24/webapps/axis/WEB-INF/lib/log4j-1.2.6.jar:. org.astrogrid.mySpace.mySpaceDemo.MySpaceDemo $CATALINA_HOME/conf/astrogrid/mySpace/example

//dataholder file:
//cd /tmp/mySpaceTest

//registry conf files:
//cd /home/clq2/develop/MSRegistryTestDir
///home/clq2/develop/MSRegistryTestDir/originalsReg


package java;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.encoding.XMLType;
import org.apache.log4j.Logger;

 
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
			}else if(arg.equals("lookupDataHolderDetails")){
				b = invokeLookUpDataHolderDetails();
			}else if(arg.equals("exportDataHolder")){
			    b = invokeExportDataHolder();
			}else if(arg.equals("createContainer")){
			    b = invokeCreateContainer();
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
		try{	
			String request ="<?xml version='1.0' encoding='UTF-8'?>" +
			"<request>" +
				"<elements>" +
					"<userID>clq</userID>" +
					"<communityID>Leicester</communityID>" +
					"<jobID>testIDIt2</jobID>" +
					"<mySpaceAction>upLoad</mySpaceAction>" +
					"<dataItemID></dataItemID>" +
					"<oldDataItemID>10</oldDataItemID>" +
					"<newDataItemName>xx</newDataItemName>" +
					"<newContainerName>x</newContainerName>" +
					"<query>x</query>" +
					"<newDataHolderName>/clq/serv2/table100</newDataHolderName>" +
					"<serverFileName>/tmp/test</serverFileName>" +
					"<fileSize>1</fileSize>" +
				"</elements>" +
			"</request>"; 
			call = createcall();
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
		try{	
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
			//String endpoint  = "http://localhost:8080/axis/services/MySpaceManager";
			String endpoint  = "http://hydra:8080/axis/services/MySpaceManager";
			
			Service service = new Service();
			call = (Call)service.createCall();
			call.setTargetEndpointAddress( new java.net.URL(endpoint) );
		}catch(Exception e){
			e.printStackTrace();
			System.err.println("Exception caught: " + e);
		}
		return call;		
	}
	
	private static String invokeCopy(){
		String b = "";
		try{	
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
		try{
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
					"<newDataItemName>/clq/serv2/table304</newDataItemName>" + //destenation of moving eg./clq/serv2/table23
					"<newContainerName>x</newContainerName>" +
					"<query>x</query>" +
					"<newDataHolderName>xx</newDataHolderName>" +
					"<serverFileName>/clq/serv2/table303</serverFileName>" + // from
					"<fileSize>1</fileSize>" +
				"</elements>" +
			"</request>"; 
			call = createcall();
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
	
	private static String invokeLookUpDataHolderDetails(){
		String b = "";
		try{
			String request ="<?xml version='1.0' encoding='UTF-8'?>" +
			"<request>" +
				"<elements>" +
					"<userID>clq</userID>" +
					"<communityID>Leicester</communityID>" +
					"<jobID>testIDIt2</jobID>" +
					"<mySpaceAction>lookupDataHolderDetails</mySpaceAction>" +
					"<dataItemID></dataItemID>" +
					"<oldDataItemID></oldDataItemID>" +
					"<newDataItemName></newDataItemName>" + 
					"<newContainerName>x</newContainerName>" +
					"<query>x</query>" +
					"<newDataHolderName>xx</newDataHolderName>" +
					"<serverFileName>/clq/serv2/</serverFileName>" + // dataholer need to look up
					"<fileSize></fileSize>" +
				"</elements>" +
			"</request>"; 
			call = createcall();
			call.setOperationName( "lookupDataHolderDetails" );
			
			call.addParameter("arg0", XMLType.XSD_STRING, ParameterMode.IN);
			
			call.setReturnType( org.apache.axis.encoding.XMLType.XSD_STRING);
			b = (String) call.invoke( new Object[] { request } );

			System.out.println("inside MySpaceManagerClient test lookupDataHolderDetails, " +b );
		}

		catch (Exception e)
		{
			e.printStackTrace();
			System.err.println("Exception caught: " + e);
		}
		return b;		
	}		
	
	private static String invokeExportDataHolder(){
		String b = "";
		try{
			String request ="<?xml version='1.0' encoding='UTF-8'?>" +
			"<request>" +
				"<elements>" +
					"<userID>clq</userID>" +
					"<communityID>Leicester</communityID>" +
					"<jobID>testIDIt2</jobID>" +
					"<mySpaceAction>exportDataHolder</mySpaceAction>" +
					"<dataItemID></dataItemID>" +
					"<oldDataItemID></oldDataItemID>" +
					"<newDataItemName></newDataItemName>" + 
					"<newContainerName>x</newContainerName>" +
					"<query>x</query>" +
					"<newDataHolderName>xx</newDataHolderName>" +
					"<serverFileName>/clq/serv2/table100</serverFileName>" + // dataholer need to export
					"<fileSize></fileSize>" +
				"</elements>" +
			"</request>"; 
			call = createcall();
			call.setOperationName( "exportDataHolder" );
			
			call.addParameter("arg0", XMLType.XSD_STRING, ParameterMode.IN);
			
			call.setReturnType( org.apache.axis.encoding.XMLType.XSD_STRING);
			b = (String) call.invoke( new Object[] { request } );

			System.out.println("inside MySpaceManagerClient test exportDataHolder, " +b );
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.err.println("Exception caught: " + e);
		}
		return b;		
	}		

	private static String invokeCreateContainer(){
		String b = "";
		try{
			String request ="<?xml version='1.0' encoding='UTF-8'?>" +
			"<request>" +
				"<elements>" +
					"<userID>clq</userID>" +
					"<communityID>Leicester</communityID>" +
					"<jobID>testIDIt2</jobID>" +
					"<mySpaceAction>createContainer</mySpaceAction>" +
					"<dataItemID></dataItemID>" +
					"<oldDataItemID></oldDataItemID>" +
					"<newDataItemName></newDataItemName>" + 
					"<newContainerName>/clq/serv1/aaa</newContainerName>" +
					"<query>x</query>" +
					"<newDataHolderName>xx</newDataHolderName>" +
					"<serverFileName></serverFileName>" + 
					"<fileSize></fileSize>" +
				"</elements>" +
			"</request>"; 
			call = createcall();
			call.setOperationName( "createContainer" );
			
			call.addParameter("arg0", XMLType.XSD_STRING, ParameterMode.IN);
			
			call.setReturnType( org.apache.axis.encoding.XMLType.XSD_STRING);
			b = (String) call.invoke( new Object[] { request } );

			System.out.println("inside MySpaceManagerClient test createContainer, " +b );
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.err.println("Exception caught: " + e);
		}
		return b;		
	}	
}