package org.astrogrid.mySpace.delegate.helper;

import java.io.StringReader ;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource ;

import org.astrogrid.community.User;

import java.util.Vector;

/**
 * @author C L QIN
 * @version Iteration 2.
 */

public class MySpaceHelper{
    
    private static boolean DEBUG = true;
	
	//Constructor
	public MySpaceHelper (){}
		
    public String buildSave(String userid, String communityid, String credential, String fileName, String fileContent, String category, String action){
        String fileFullName = "/"+userid.trim()+"@"+communityid.trim()+"/serv1/"+category.toLowerCase().trim()+"/"+fileName.trim();
		StringBuffer request = new StringBuffer() ;
		try {		
			request.append("<request>") ;
			request.append("<userID>") ;
			request.append(userid);
			request.append("</userID>");

			request.append("<communityID>") ;
			request.append(communityid);
			request.append("</communityID>");
			
			request.append("<credential>");
			request.append(credential);
			request.append("</credential>");

			request.append("<fileContent>") ;
			request.append(fileContent) ;
			request.append("</fileContent>") ;
			
			request.append("<newDataHolderName>") ;
			request.append(fileFullName) ;
			request.append("</newDataHolderName>") ;
			
			request.append("<action>");
			request.append(action);
			request.append("</action>");
			request.append("</request>") ;					
		}
		catch ( Exception ex ) {
			System.out.println("MySpaceHelper.buildSave exception: "+ex.toString());
			ex.printStackTrace();
		}
		return request.toString();
	}
	
	public String buildSaveURL(String userid, String communityid, String credential, String fileName, String importURL, String category, String action){
		String fileFullName = "/"+userid.trim()+"/"+communityid.trim()+"/serv1/"+category.toLowerCase().trim()+"/"+fileName.trim();
		StringBuffer request = new StringBuffer() ;
		try {		
			request.append("<request>") ;
			request.append("<userID>") ;
			request.append(userid);
			request.append("</userID>");

			request.append("<communityID>") ;
			request.append(communityid);
			request.append("</communityID>");
			
			request.append("<credential>");
			request.append(credential);
			request.append("</credential>");

			request.append("<importURI>") ;
			request.append(importURL) ;
			request.append("</importURI>") ;
			
			request.append("<newDataHolderName>") ;
			request.append(fileFullName) ;
			request.append("</newDataHolderName>") ;
			
			request.append("<action>");
			request.append(action);
			request.append("</action>");
			request.append("</request>") ;					
		}
		catch ( Exception ex ) {
			System.out.println("MySpaceHelper.buildSave exception: "+ex.toString());
			ex.printStackTrace();
		}
		return request.toString();
	}
	
	public String buildDownload(String userid, String communityid, String credential, String fullFileName){
		StringBuffer request = new StringBuffer() ;
		try {		
			request.append("<request>") ;
			request.append("<userID>") ;
			request.append(userid);
			request.append("</userID>");

			request.append("<communityID>") ;
			request.append(communityid);
			request.append("</communityID>");
			
			request.append("<credential>");
			request.append(credential);
			request.append("</credential>");

			request.append("<serverFileName>") ;
			request.append(fullFileName) ;
			request.append("</serverFileName>") ;
			
			request.append("</request>") ;
		}
		catch ( Exception ex ) {
			System.out.println("MySpaceHelper.buildSave exception: "+ex.toString());
			ex.printStackTrace();
		}
		return request.toString();
	}
	
	public String buildListDataHoldings(String userid, String communityid, String credential, String criteria){
		StringBuffer request = new StringBuffer() ;
		Vector vector = new Vector();
		try {		
			request.append("<request>") ;
			request.append("<userID>") ;
			request.append(userid);
			request.append("</userID>");
	
			request.append("<communityID>") ;
			request.append(communityid);
			request.append("</communityID>");
				
			request.append("<credential>");
			request.append(credential);
			request.append("</credential>");
	
			request.append("<query>") ;
			request.append(criteria) ;
			request.append("</query>") ;
	
			request.append("</request>") ;
			
		}
		catch ( Exception ex ) {
			System.out.println("MySpaceHelper.buildListDataHoldings exception: "+ex.toString());
			ex.printStackTrace();
		}
		return request.toString();
	}
	
	public String buildListDataHolding(String userid, String communityid, String credential, String serverFileName){
		StringBuffer request = new StringBuffer() ;
		Vector vector = new Vector();
		try {		
			request.append("<request>") ;
			request.append("<userID>") ;
			request.append(userid);
			request.append("</userID>");
	
			request.append("<communityID>") ;
			request.append(communityid);
			request.append("</communityID>");
				
			request.append("<credential>");
			request.append(credential);
			request.append("</credential>");
	
			request.append("<serverFileName>") ;
			request.append(serverFileName) ;
			request.append("</serverFileName>") ;
	
			request.append("</request>") ;
			
		}
		catch ( Exception ex ) {
			System.out.println("MySpaceHelper.buildListDataHolding exception: "+ex.toString());
			ex.printStackTrace();
		}
		return request.toString();
	}	
	
	public String buildCopy(String userid, String communityid, String credential, String serverFileName, String newDataHolderName){
		StringBuffer request = new StringBuffer() ;
		Vector vector = new Vector();
		try {		
			request.append("<request>") ;
			request.append("<userID>") ;
			request.append(userid);
			request.append("</userID>");
	
			request.append("<communityID>") ;
			request.append(communityid);
			request.append("</communityID>");
				
			request.append("<credential>");
			request.append(credential);
			request.append("</credential>");
	
			request.append("<serverFileName>") ;
			request.append(serverFileName) ;
			request.append("</serverFileName>") ;
			
			request.append("<newDataItemName>") ;
			request.append(newDataHolderName) ;
			request.append("</newDataItemName>") ;			
	
			request.append("</request>") ;
			
		}
		catch ( Exception ex ) {
			System.out.println("MySpaceHelper.buildListDataHolding exception: "+ex.toString());
			ex.printStackTrace();
		}
		return request.toString();
	}	
	
	public String buildDelete(String userid, String communityid, String credential, String serverFileName){
		StringBuffer request = new StringBuffer() ;
		Vector vector = new Vector();
		try {		
			request.append("<request>") ;
			request.append("<userID>") ;
			request.append(userid);
			request.append("</userID>");
	
			request.append("<communityID>") ;
			request.append(communityid);
			request.append("</communityID>");
				
			request.append("<credential>");
			request.append(credential);
			request.append("</credential>");
	
			request.append("<serverFileName>") ;
			request.append(serverFileName) ;
			request.append("</serverFileName>") ;		
	
			request.append("</request>") ;
			
		}
		catch ( Exception ex ) {
			System.out.println("MySpaceHelper.buildDelete exception: "+ex.toString());
			ex.printStackTrace();
		}
		return request.toString();
	}	
	
	public String buildRename(String userid, String communityid, String credential, String serverFileName, String newDataHolderName){
		StringBuffer request = new StringBuffer() ;
		Vector vector = new Vector();
		try {		
			request.append("<request>") ;
			request.append("<userID>") ;
			request.append(userid);
			request.append("</userID>");
	
			request.append("<communityID>") ;
			request.append(communityid);
			request.append("</communityID>");
				
			request.append("<credential>");
			request.append(credential);
			request.append("</credential>");
	
			request.append("<serverFileName>") ;
			request.append(serverFileName) ;
			request.append("</serverFileName>") ;
			
			request.append("<newDataItemName>") ;
			request.append(newDataHolderName) ;
			request.append("</newDataItemName>") ;			
	
			request.append("</request>") ;
			
		}
		catch ( Exception ex ) {
			System.out.println("MySpaceHelper.buildListDataHolding exception: "+ex.toString());
			ex.printStackTrace();
		}
		return request.toString();
	}	
	
	public String buildExtendlease(String userid, String communityid, String credential, String serverFileName, int extentionPeriod){
		StringBuffer request = new StringBuffer() ;
		Vector vector = new Vector();
		try {		
			request.append("<request>") ;
			request.append("<userID>") ;
			request.append(userid);
			request.append("</userID>");
	
			request.append("<communityID>") ;
			request.append(communityid);
			request.append("</communityID>");
				
			request.append("<credential>");
			request.append(credential);
			request.append("</credential>");
	
			request.append("<serverFileName>") ;
			request.append(serverFileName) ;
			request.append("</serverFileName>") ;
			
			request.append("<extentionPeriod>") ;
			request.append(extentionPeriod) ;
			request.append("</extentionPeriod>") ;				
	
			request.append("</request>") ;
			
		}
		catch ( Exception ex ) {
			System.out.println("MySpaceHelper.buildDelete exception: "+ex.toString());
			ex.printStackTrace();
		}
		return request.toString();
	}	
	
	public String buildContainer(String userid, String communityid, String credential, String newContainerName){
		StringBuffer request = new StringBuffer() ;
		Vector vector = new Vector();
		try {		
			request.append("<request>") ;
			request.append("<userID>") ;
			request.append(userid);
			request.append("</userID>");
	
			request.append("<communityID>") ;
			request.append(communityid);
			request.append("</communityID>");
			request.append("<credential>");
			request.append(credential);
			request.append("</credential>");
	
			request.append("<newContainerName>") ;
			request.append(newContainerName) ;
			request.append("</newContainerName>") ;			
	
			request.append("</request>") ;
			
		}
		catch ( Exception ex ) {
			System.out.println("MySpaceHelper.buildCreateContainer exception: "+ex.toString());
			ex.printStackTrace();
		}
		return request.toString();
	}		

	public Vector getList( String xmlRequest , String key){
		Vector request = new Vector();
		try{		
			Document doc = parseRequest( xmlRequest );
			Node checker;
			checker = doc.getDocumentElement();
			boolean ascending = false;
			int level = 1;
			while (true) {
				
					if (checker!=null && (checker.hasChildNodes()) && (!ascending)) {
						checker = checker.getFirstChild();
						if( DEBUG )  System.out.println("GOING DOWN"+checker.getNodeName() +"NODETYPE="+checker.getNodeType());
						if(checker.getNodeType()==1){
							String text = "";
							if(checker.getFirstChild()!=null){
								if(checker.getFirstChild().getNodeType()==Node.TEXT_NODE) {
									text = checker.getFirstChild().getNodeValue();
									if( DEBUG )  System.out.println("BEFORE.PUT.DOWN" +checker.getNodeName()+" TEXT " +text);
									if (checker.getNodeName().equalsIgnoreCase(key)){
										if(key.equals("dataItemName")){
										    text = text.substring(text.lastIndexOf('/')+1,text.trim().length());
										}
										request.add(text);
									}								
									if( DEBUG )  System.out.println("NODENAME: "+checker.getNodeName() +",  TEXTVALUE: "+text);
								}
							}
						}
						ascending = false;
						level++;
						}
						else if (checker!=null && checker.getNextSibling() != null) {
							checker= checker.getNextSibling();
							String text = "";
							
							if(checker.getFirstChild()!=null){
								if(checker.getFirstChild().getNodeType()==Node.TEXT_NODE) {
									text = checker.getFirstChild().getNodeValue();
									if (checker.getNodeName().equalsIgnoreCase(key)){
										if(key.equals("dataItemName")){
											text = text.substring(text.lastIndexOf('/')+1,text.trim().length());
										}
										request.add(text);
									}
																
									System.out.println("NODENAME: "+checker.getNodeName() +",  TEXTVALUE: "+text);
								}
							}
							ascending = false;					
							}
						else if (checker !=null && checker.getParentNode() != null) {
							checker= checker.getParentNode();
							String text = "";
							if(checker.getFirstChild()!=null){
								if(checker.getFirstChild().getNodeType()==Node.TEXT_NODE) {
									text = checker.getFirstChild().getNodeValue();
									if (checker.getNodeName().equalsIgnoreCase(key)){
										if(key.equals("dataItemName")){
											text = text.substring(text.lastIndexOf('/')+1,text.trim().length());
										}
										request.add(text);
									}								
									
									System.out.println("NODENAME: "+checker.getNodeName() +",  TEXTVALUE: "+text);
								}
							}
							ascending = true;
							level--;
							}
							else {
								break;
								}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return request;
	}
	
	private Document parseRequest ( String xmlRequest){		
		Document doc = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
	       
		try {
		   builder = factory.newDocumentBuilder();
		   InputSource requestSource = new InputSource( new StringReader( xmlRequest ) );
		   doc = builder.parse( requestSource );
		}
		catch ( Exception ex ) {
			System.out.println("MySpaceHelper.parseRequest" +ex.toString());
			ex.printStackTrace();		
		}		
		return doc;	
	}	
   
   public static String formatMyspaceReference(User user, String server, String container, String file)
   {
      return "/"+user.getAccount()+"@"+user.getGroup()+"/"+server+"/"+container+"/"+file;
   }
	
}	