package org.astrogrid.portal.query;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;
import java.io.*;

/**
 * ClassName: CreateRequest
 * Purpose:  Might need to change the name to something else now.  The main purpose is to instantiate an XML document
 * that conforms to the jobcontroller scheme.  It takes a QueryBuilder object and using the buildXML method creates a 
 * Document object of the xml that conforms to the scheme.  Also can be used as a standalone class that I needed it for testing
 * purposes.
 * @author Kevin Benson
 *
 */
public class CreateRequest {

	private final String JOB_ELEMENT = "job";
	private final String JOBURN_ATTR = "jobURN";
	private final String JOBMONITOR_ATTR = "jobMonitorURL";
	private final String NAME_ATTR = "name";
	private final String URL_ATTR = "url";
	private final String TYPE_ATTR = "type";
	private final String USERID_ELEMENT = "userid";
	private final String COMM_ELEMENT = "community";
	private final String JOBSTEP_ELEMENT = "jobstep";
	private final String QUERY_ELEMENT = "query";
	private final String CATALOG_ELEMENT = "catalog";
	private final String FROM_ELEMENT = "from";
	private final String SERVICE_ELEMENT = "service";
	private final String RETURN_ELEMENT = "return";
	private final String FIELD_ELEMENT = "field";
	private final String OPERATION_ELEMENT = "operation";
	private final String CRITERIA_ELEMENT = "criteria";
	private final String JOBCONTROLLER_URL = "http://hydra.star.le.ac.uk:8080/axis/services/DatasetAgent"; 

	public CreateRequest() {

	}

	public CreateRequest(QueryBuilder qb) {
		buildXMLRequest(qb);
	}

	/**
	 * BuildXMl request is the main method in this object.  It takes a QueryBuilder object that has all their DataSetInformation
	 * objects and thier DataSetColumn and CriteriaInformation and form to the jobController schema.
	 * Right now looks a little messy because it has all these different Element objects to it.  But that is about it nothing
	 * exciting.  It does call doCriteria for doing the criteria Information by itself because it can be done recursively
	 * and it is a little more complex.
	 * @param qb
	 * @return
	 */
	public Document buildXMLRequest(QueryBuilder qb) {
		try {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		//dbf.setValidating(true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.getDOMImplementation().createDocument(null,JOB_ELEMENT,null);
		String dsNameTemp = null;
		String opTemp = null;
		int iTemp = 0;

		Element userElem,commElem,jobStepElem,serviceElem,queryElem;
		Element fromElem,returnElem,criteriaElem,fieldElem,catalogElem;
		Element opElement = null,opElem = null;
		Element operationElem;

		doc.getDocumentElement().setAttribute(NAME_ATTR,qb.getName());

		userElem = doc.createElement(USERID_ELEMENT);
		userElem.appendChild(doc.createTextNode(qb.getUserName()));
		doc.getDocumentElement().appendChild(userElem);

		commElem = doc.createElement(COMM_ELEMENT);
		commElem.appendChild(doc.createTextNode(qb.getCommunity()));
		doc.getDocumentElement().appendChild(commElem);
//		System.out.println("the username = " + qb.getUserName());
//		System.out.println("the community = " + qb.getCommunity());

		for(int i=0;i < qb.getDataSetInformation().size();i++) {
			DataSetInformation dsInfo = (DataSetInformation)qb.getDataSetInformation().get(i);
			dsNameTemp = dsInfo.getName().toLowerCase();
			if( (iTemp = dsNameTemp.indexOf("-")) != -1) {
				String temp = new String(dsNameTemp);
				dsNameTemp = dsNameTemp.substring(0,iTemp);
				dsNameTemp = dsNameTemp.concat(temp.substring((iTemp+1)));
			}
			jobStepElem = doc.createElement(JOBSTEP_ELEMENT);
			jobStepElem.setAttribute(NAME_ATTR,dsNameTemp);
			jobStepElem.appendChild( (queryElem = doc.createElement(QUERY_ELEMENT)) );

			queryElem.appendChild( (fromElem = doc.createElement(FROM_ELEMENT)) );
				fromElem.appendChild( (catalogElem = doc.createElement(CATALOG_ELEMENT)) );
					catalogElem.setAttribute(NAME_ATTR,dsNameTemp);
					catalogElem.appendChild( (serviceElem = doc.createElement(SERVICE_ELEMENT)) );
						serviceElem.setAttribute(NAME_ATTR,dsNameTemp);
						serviceElem.setAttribute(URL_ATTR,JOBCONTROLLER_URL);
			queryElem.appendChild ( (returnElem = doc.createElement(RETURN_ELEMENT)) );
				for(int j=0;j < dsInfo.getDataSetColumns().size();j++) {
					DataSetColumn dsColumn = (DataSetColumn)dsInfo.getDataSetColumns().get(j);
					returnElem.appendChild( (fieldElem = doc.createElement(FIELD_ELEMENT)) );
					fieldElem.setAttribute(NAME_ATTR,dsColumn.getName());
					if(dsColumn.getType() != null && dsColumn.getType().length() > 0) {
						fieldElem.setAttribute(TYPE_ATTR,dsColumn.getType());
					}//if
				}//for

			queryElem.appendChild( (criteriaElem = doc.createElement(CRITERIA_ELEMENT)) );
			criteriaElem.appendChild( (operationElem = doc.createElement(OPERATION_ELEMENT)) );
			for(int k = 0;k < dsInfo.getCriteriaInformation().size();k++) {
				CriteriaInformation ci = (CriteriaInformation)dsInfo.getCriteriaInformation().get(k);
				if(k == 0 && dsInfo.getCriteriaInformation().size() == 1)  { 
					doCriteria(doc,operationElem,ci);	
				}else {
					if(k == 0 && dsInfo.getCriteriaInformation().size() > 1)  {
						CriteriaInformation ciTemp = (CriteriaInformation)dsInfo.getCriteriaInformation().get(1);
						operationElem.setAttribute(NAME_ATTR,ciTemp.getJoinType());
						opTemp = ciTemp.getJoinType();
						operationElem.appendChild( (opElement = doc.createElement(OPERATION_ELEMENT)) );
						doCriteria(doc,opElement,ci);					
					}else {
						operationElem.appendChild( (opElement = doc.createElement(OPERATION_ELEMENT)) );
						if(k > 1 && ci.getJoinType() != null && ci.getJoinType().length() > 0) {
							if(!ci.getJoinType().equals(opTemp)) {
								opElement.setAttribute(NAME_ATTR,ci.getJoinType());
								operationElem.appendChild( (opElem = doc.createElement(OPERATION_ELEMENT)) );
								doCriteria(doc,opElem,ci);
								opTemp = ci.getJoinType();							
							}else {
								doCriteria(doc,opElement,ci);							
							}//else						
						}else {
							if(k == 1) {
								doCriteria(doc,opElement,ci);	
							}	
						}
																	
					}//else
				}//else
			}
			/*
			for(int k = (dsInfo.getCriteriaInformation().size() -1) ;k >= 0;k--) {
				CriteriaInformation ci = (CriteriaInformation)dsInfo.getCriteriaInformation().get(k);
				if(ci.getJoinType() != null && ci.getJoinType().length() > 0) {
					if(k == (dsInfo.getCriteriaInformation().size() -1) ) {
						operationElem.setAttribute(NAME_ATTR,ci.getJoinType());
						operationElem.appendChild( (opElement = doc.createElement(OPERATION_ELEMENT)) );
						doCriteria(doc,opElement,ci);						
					}else {
						operationElem.appendChild( (opElement = doc.createElement(OPERATION_ELEMENT)) );
						opElement.setAttribute(NAME_ATTR,ci.getJoinType());
						opTemp = ci.getJoinType();
						operationElem.appendChild( (opElement = doc.createElement(OPERATION_ELEMENT)) );					
					}
				}else {
					if(k == (dsInfo.getCriteriaInformation().size() -1) ) {
						doCriteria(doc,operationElem,ci);		
					}//if
				}//else
				//else {opElement = operationElem;}
				
				//
			}//for
			*/
			/*
				for(int k = 0;k < dsInfo.getCriteriaInformation().size();k++) {
					criteriaElem.appendChild( (operationElem = doc.createElement(OPERATION_ELEMENT)) );
					CriteriaInformation ci = (CriteriaInformation)dsInfo.getCriteriaInformation().get(k);
					doCriteria(doc,operationElem,ci);
				}//for
				*/
			doc.getDocumentElement().appendChild(jobStepElem);
		}//for

		//printDocument(doc);
		return doc;
		}catch(Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Takes an xml document and prints it out on the System.out (the command line) for viewing.
	 * @param doc
	 * @throws Exception
	 */
	private void printDocument(Document doc) throws Exception {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer output = tf.newTransformer();
		output.transform(new DOMSource(doc), new StreamResult(System.out));
	}

	/**
	 * Just takes an XML document and puts it into a nice readable string and returns it.
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	public String writeDocument(Document doc) throws Exception {
		//StringWriter sw = new StringWriter();
		ByteArrayOutputStream sw = new ByteArrayOutputStream();
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer output = tf.newTransformer();
		output.transform(new DOMSource(doc), new StreamResult(sw));
		return sw.toString();
	}
	
	private void doCriteria(Document doc,Element opElement,CriteriaInformation ci) {

		Element fieldElem;
		Element op,opTemp;
		String []fValues;
		if(ci.getDataSetColumn().getType() == null || !ci.getDataSetColumn().getType().equals("FUNCTION")) {
			opElement.setAttribute(NAME_ATTR,ci.getFilterType());
			opElement.appendChild( (fieldElem = doc.createElement(FIELD_ELEMENT)) );
			fieldElem.setAttribute(NAME_ATTR,ci.getDataSetColumn().getName());
			if(ci.getDataSetColumn().getType() != null) {
				fieldElem.setAttribute(TYPE_ATTR,ci.getDataSetColumn().getType());
			}//if
		}else {
			opElement.setAttribute(NAME_ATTR,ci.getDataSetColumn().getName());
			fValues = ci.getFunctionValues().split(",");
			for(int i = 0; i < fValues.length;i++) {
				opElement.appendChild( (fieldElem = doc.createElement(FIELD_ELEMENT)) );
				fieldElem.appendChild(doc.createTextNode(fValues[i]));
			}//for
			if(ci.getFilterType() != null && !ci.getFilterType().equals("NONE")) {
			  opElement.appendChild( (opTemp = doc.createElement(OPERATION_ELEMENT)) );
			  opTemp.setAttribute(NAME_ATTR,ci.getFilterType());
			}
		}//else
		if(ci.getFilterType() != null && !ci.getFilterType().equals("NONE")) {
			opElement.appendChild ( (fieldElem = doc.createElement(FIELD_ELEMENT)) );
			fieldElem.appendChild(doc.createTextNode(ci.getValue()));
		}
		/*if(ci.getLinkedCriteria() != null) {
			opElement.appendChild( (op = doc.createElement(OPERATION_ELEMENT)) );
			doCriteria(doc,op,ci.getLinkedCriteria());
		}//if
		*/
	}//doCriteria
	

	/**
	 * This method recursively calls itself doing the <operation> part of the schema.  Which is held in all the different
	 * CriteriaInformation objects.
	 * @param doc
	 * @param operationElem
	 * @param ci
	 */
	/*
	 OLD VERSION
	private void doCriteria(Document doc,Element operationElem,CriteriaInformation ci) {

		Element opElement,fieldElem;
		Element op,opTemp;
		String []fValues;
		if(ci.getJoinType() != null && ci.getJoinType().length() > 0) {
			operationElem.setAttribute(NAME_ATTR,ci.getJoinType());
			operationElem.appendChild( (opElement = doc.createElement(OPERATION_ELEMENT)) );
		}else {
			opElement = operationElem;
		}//else
		if(ci.getDataSetColumn().getType() == null || !ci.getDataSetColumn().getType().equals("FUNCTION")) {
			opElement.setAttribute(NAME_ATTR,ci.getFilterType());
			opElement.appendChild( (fieldElem = doc.createElement(FIELD_ELEMENT)) );
			fieldElem.setAttribute(NAME_ATTR,ci.getDataSetColumn().getName());
			if(ci.getDataSetColumn().getType() != null) {
				fieldElem.setAttribute(TYPE_ATTR,ci.getDataSetColumn().getType());
			}//if
		}else {
			opElement.setAttribute(NAME_ATTR,ci.getDataSetColumn().getName());
			fValues = ci.getFunctionValues().split(",");
			for(int i = 0; i < fValues.length;i++) {
				opElement.appendChild( (fieldElem = doc.createElement(FIELD_ELEMENT)) );
				fieldElem.appendChild(doc.createTextNode(fValues[i]));
			}//for
			if(ci.getFilterType() != null && !ci.getFilterType().equals("NONE")) {
			  opElement.appendChild( (opTemp = doc.createElement(OPERATION_ELEMENT)) );
			  opTemp.setAttribute(NAME_ATTR,ci.getFilterType());
			}
		}//else
		if(ci.getFilterType() != null && !ci.getFilterType().equals("NONE")) {
			opElement.appendChild ( (fieldElem = doc.createElement(FIELD_ELEMENT)) );
			fieldElem.appendChild(doc.createTextNode(ci.getValue()));
		}
		if(ci.getLinkedCriteria() != null) {
			opElement.appendChild( (op = doc.createElement(OPERATION_ELEMENT)) );
			doCriteria(doc,op,ci.getLinkedCriteria());
		}//if
	}//doCriteria
	*/

	public static void main(String []args) {
		try {
		QueryBuilder qb = new QueryBuilder("JobTest");
		DataSetInformation ds = qb.addDataSetInformation("USNOB");
		ds.addDataSetColumn("ID","COLUMN");
		ds.addCriteriaInformation("ID","COLUMN","EQUALS","5",null,null);

		CreateRequest ci = new CreateRequest();
		Document doc = ci.buildXMLRequest(qb);
		String res = ci.writeDocument(doc);
		System.out.println("the result = " + res);
	}catch(Exception e1){e1.printStackTrace();}


	}

}