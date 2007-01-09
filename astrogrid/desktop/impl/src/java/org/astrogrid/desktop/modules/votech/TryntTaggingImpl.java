/**
 * 
 */
package org.astrogrid.desktop.modules.votech;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.astrogrid.acr.ServiceException;
import org.votech.Tagging;

/** Implementaiton of tagging over trynt webservices.
 * (possible to implement over other webservices, if needed later).
 * @author Noel Winstanley
 * @since Dec 13, 200612:42:56 AM
 */
public class TryntTaggingImpl implements Tagging {

	public final URL suggestURL;
	public final URL groupURL;
	public final URL userURL;
	public final URL tagURL;
	/**
	 * @throws MalformedURLException 
	 * 
	 */
	public TryntTaggingImpl() throws MalformedURLException {
		suggestURL = new URL("http://www.trynt.com/term-extraction-api/v1/");
		groupURL = new URL("http://www.trynt.com/uri-group-api/v1/");
		userURL = new URL("http://www.trynt.com/uri-user-api/v1/");
		tagURL = new URL("http://www.trynt.com/uri-tag-api/v1/");
	}
//@tobedone - don't understand the api fully yet - can't implement until this is done.
	public void addTag(URI arg0, String arg1, int arg2, int arg3)
	throws ServiceException {

		XMLInputFactory fac = XMLInputFactory.newInstance();
		XMLStreamReader in = null;
		try {
			URL q = new URL(tagURL,"?action=add&uri=" 
					+ URLEncoder.encode(ObjectUtils.toString(arg0,""),"UTF-8"));
			in = fac.createXMLStreamReader(q.openStream());
			String msg = null;
			int id = -1;
			while (in.hasNext()) {
				in.next();
				if (in.isStartElement()) {
					String elName = in.getLocalName();
					if (elName.equals("msg")) {
						msg = in.getElementText();
					} else if (elName.equals("group_id")) {
						try {
							id = Integer.parseInt(in.getElementText());
						} catch (NumberFormatException e) {
							// handle it ok later.
						}
					}
				}
			}// see what we've got.
			if (id == -1) {
				throw new ServiceException("Error from Service: " + msg);
			}
//			return id;
		} catch (XMLStreamException x) {
			throw new ServiceException(x);
		} catch (IOException x) {
			throw new ServiceException(x);
		} finally {
			if (in != null) {
			try {
				in.close();
			} catch (XMLStreamException x) {
			}
			}
		}				
	}

	public void deleteTag(URI arg0, String arg1, int arg2, int arg3)
	throws ServiceException {
	}

	public String[] listLatest(URI arg0, int arg1, int arg2)
	throws ServiceException {
		return null;
	}

	public String[] listPopular(URI arg0, int arg1, int arg2)
	throws ServiceException {
		return null;
	}

	public int getGroupId(String arg0) throws ServiceException {

		XMLInputFactory fac = XMLInputFactory.newInstance();
		XMLStreamReader in = null;
		try {
			URL q = new URL(groupURL,"?group=astroruntime-" 
					+ URLEncoder.encode(ObjectUtils.toString(arg0,""),"UTF-8"));
			in = fac.createXMLStreamReader(q.openStream());
			String msg = null;
			int id = -1;
			while (in.hasNext()) {
				in.next();
				if (in.isStartElement()) {
					String elName = in.getLocalName();
					if (elName.equals("msg")) {
						msg = in.getElementText();
					} else if (elName.equals("group_id")) {
						try {
							id = Integer.parseInt(in.getElementText());
						} catch (NumberFormatException e) {
							// handle it ok later.
						}
					}
				}
			}// see what we've got.
			if (id == -1) {
				throw new ServiceException("Error from Service: " + msg);
			}
			return id;
		} catch (XMLStreamException x) {
			throw new ServiceException(x);
		} catch (IOException x) {
			throw new ServiceException(x);
		} finally {
			if (in != null) {
			try {
				in.close();
			} catch (XMLStreamException x) {
			}
			}
		}			
	}



	public int getUserId(String arg0, String arg1) throws ServiceException {
		XMLInputFactory fac = XMLInputFactory.newInstance();
		XMLStreamReader in = null;
		try {
			URL q = new URL(userURL,
					"?user=" + URLEncoder.encode(ObjectUtils.toString(arg0,""),"UTF-8")
					+ "&host=" + URLEncoder.encode(ObjectUtils.toString(arg1,""),"UTF-8"));
			in = fac.createXMLStreamReader(q.openStream());
			String msg = null;
			int id = -1;
			while (in.hasNext()) {
				in.next();
				if (in.isStartElement()) {
					String elName = in.getLocalName();
					if (elName.equals("msg")) {
						msg = in.getElementText();
					} else if (elName.equals("user_id")) {
						try {
							id = Integer.parseInt(in.getElementText());
						} catch (NumberFormatException e) {
							// handle it ok later.
						}
					}
				}
			}// see what we've got.
			if (id == -1) {
				throw new ServiceException("Error from Service: " + msg);
			}
			return id;
		} catch (XMLStreamException x) {
			throw new ServiceException(x);
		} catch (IOException x) {
			throw new ServiceException(x);
		} finally {
			if (in != null) {
			try {
				in.close();
			} catch (XMLStreamException x) {
			}
			}
		}			
	}





	public String[] suggestTags(String arg0, String arg1)
	throws ServiceException {
		XMLInputFactory fac = XMLInputFactory.newInstance();
		XMLStreamReader in = null;
		try {
			HttpURLConnection conn =(HttpURLConnection) suggestURL.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("User-Agent","AstroRuntime");
			OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
			out.write("query=" + URLEncoder.encode(ObjectUtils.toString(arg1,""),"UTF-8"));
			out.write("&context=" + URLEncoder.encode(ObjectUtils.toString(arg0,""),"UTF-8"));
			out.close();
			in = fac.createXMLStreamReader(conn.getInputStream());
			List results = new ArrayList();
			while (in.hasNext()) {
				in.next();
				if (in.isStartElement()) {
					final String localName = in.getLocalName();
					if (localName.equals("result")) {
						results.add(in.getElementText());
					} else if (localName.equals("msg")) { // error
						throw new ServiceException("Error from Service: " + in.getElementText());
					}
				}
			}
			return (String[])results.toArray(new String[results.size()]);
		} catch (XMLStreamException x) {
			throw new ServiceException(x);
		} catch (IOException x) {
			throw new ServiceException(x);
		} finally {
			if (in != null) {
			try {
				in.close();
			} catch (XMLStreamException x) {
			}
			}
		}
	}

}
