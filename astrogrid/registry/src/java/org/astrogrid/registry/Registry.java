/*
 * Created on 20-May-2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.astrogrid.registry;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.sql.*;


/**
 * @author Elizabeth Auden
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Registry {
	String response = "";
	String queryResponse = "";
	
	public String dbQuery(String query)throws SQLException, ClassNotFoundException {

		  Class.forName("postgresql.Driver");		// load JDBC driver

		  // establish a connection with the desired database
		  Connection db = 
			DriverManager.getConnection(
			  "jdbc:postgresql://epoxy.mrs.umn.edu/sample_db",
			  "swl", 
			  "");

		  // set up a query to run
		  PreparedStatement st = db.prepareStatement("select * from sample_table");

		  // run the query to produce a ResultSet
		  ResultSet rs = st.executeQuery();
		  int numCols  = rs.getMetaData().getColumnCount();

		  // print out the contents of the ResultSet
		  while (rs.next()) {
			for (int j=1; j <= numCols; j++) 
		  System.out.print(rs.getString(j) + "  ");
			System.out.println();
		  }

		  // close the resources 
		  rs.close();
		  st.close();
		  db.close();

		return response;
	}
	
	public String xmlQuery(String query){

		Document parameterDoc = null;
		DocumentBuilderFactory parameterFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder parameterBuilder = null;
		String registryFilename = null;
		
		Document doc = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;

		try {
		  parameterBuilder = parameterFactory.newDocumentBuilder();
		  parameterDoc = parameterBuilder.parse("http://localhost:8080/org/astrogrid/registry/parameters.xml");
		}
		catch (ParserConfigurationException e) {
		  System.err.println(e);
		}
		catch (Exception e){
		  System.out.println("oops!: " + e);
		}

		Element parameterDocElement = parameterDoc.getDocumentElement();
		NodeList parameterNL = parameterDocElement.getElementsByTagName("registryFilename");
    
		registryFilename = parameterNL.item(0).getFirstChild().getNodeValue();

		if (registryFilename == null){
		  queryResponse = "Invalid registry filename.";
		}      
		else {
			try {
				builder = factory.newDocumentBuilder();
				doc = builder.parse(registryFilename);
			}
			catch (ParserConfigurationException e) {
				System.err.println(e);
			}
			catch (Exception e){
				System.out.println("oops!: " + e);
			}
			
			Element docElement = doc.getDocumentElement();
			//NodeList nl = docElement.getElementsByTagName(queryElement);
		}
		response = "xmlQuery: use XQuery!";
		return response;
	}
}
