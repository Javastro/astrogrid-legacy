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
	private final String JOBCONTROLLER_URL = "http://hydra.star.le.ac.uk:8080/axis/services/JobControllerService"; 

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

		Element userElem,commElem,jobStepElem,serviceElem,queryElem,fromElem,returnElem,criteriaElem,fieldElem,catalogElem,operationElem;

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

			jobStepElem = doc.createElement(JOBSTEP_ELEMENT);
			jobStepElem.setAttribute(NAME_ATTR,dsInfo.getName());
			jobStepElem.appendChild( (queryElem = doc.createElement(QUERY_ELEMENT)) );

			queryElem.appendChild( (fromElem = doc.createElement(FROM_ELEMENT)) );
				fromElem.appendChild( (catalogElem = doc.createElement(CATALOG_ELEMENT)) );
					catalogElem.setAttribute(NAME_ATTR,dsInfo.getName());
					catalogElem.appendChild( (serviceElem = doc.createElement(SERVICE_ELEMENT)) );
						serviceElem.setAttribute(NAME_ATTR,dsInfo.getName());
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
				for(int k = 0;k < dsInfo.getCriteriaInformation().size();k++) {
					criteriaElem.appendChild( (operationElem = doc.createElement(OPERATION_ELEMENT)) );
					CriteriaInformation ci = (CriteriaInformation)dsInfo.getCriteriaInformation().get(k);
					doCriteria(doc,operationElem,ci);
				}//for
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

	/**
	 * This method recursively calls itself doing the <operation> part of the schema.  Which is held in all the different
	 * CriteriaInformation objects.
	 * @param doc
	 * @param operationElem
	 * @param ci
	 */
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