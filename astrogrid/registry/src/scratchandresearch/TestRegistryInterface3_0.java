package org.astrogrid.registry;

/**
 * @author Elizabeth Auden
 */
public class TestRegistryInterface3_0 {


	public static void main(String args[]){
		
		//String query = "<query><selectionSequence><selection item='searchElements' itemOp='EQ' value='identity'/><selectionOp op='AND'/><selection item='publisherID' itemOp='EQ' value='MSSL'/></selectionSequence></query>";
		String query = "<query> <selectionSequence><selection item='searchElements' itemOp='EQ' value='identity'/><selectionOp op='$and$'/><selection item='shortName' itemOp='EQ' value='SURF'/></selectionSequence> </query>";
		RegistryInterface3_0 ri = new RegistryInterface3_0();
		String response = ri.submitQuery(query);
		System.out.println("TRI Response = " + response);
		System.out.println("\n\n");		
		String response2 = ri.fullNodeQuery(query);
		System.out.println("TRI Response2 = " + response2);

	}
}
