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
	
	public static void main(String[] args)
	{
		try
		{

			String endpoint  = "http://143.210.36.131:8080/axis/services/MySpaceManager";
			Service service = new Service();
			org.apache.axis.client.Call call = (Call)service.createCall();
			call.setTargetEndpointAddress( new java.net.URL(endpoint) );
			call.setOperationName( "upLoad" );
			
			call.addParameter("arg0", XMLType.XSD_STRING, ParameterMode.IN);
			
			call.setReturnType( org.apache.axis.encoding.XMLType.XSD_STRING);
			String b = (String) call.invoke( new Object[] { "MySpaceManagerClientTESTINPUT"} );

			System.out.println("inside MySpaceManagerClient test upLoad data holder, " +b );

		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.err.println("Exception caught: " + e);
		}
	}

}