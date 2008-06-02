package org.astrogrid.security.delegation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import org.astrogrid.security.SecurityGuard;

/**
 *
 * @author Guy Rixon
 */
public class WebResource {
  
  String redirectLocation;
  HttpURLConnection connection;
  
  /**
   * The URI for the resource.
   */
  private URI uri;
  
  /**
   * The holoder of the caller's credentials.
   */
  private SecurityGuard guard;
  
  /**
   * Constructs a WebResource.
   */
  public WebResource(URI uri) {
    this.uri = uri;
    this.guard = null;
  }
  
  /**
   * Constructs a WebResource with caller credentials.
   * This object can then autenticate the caller to an HTTPS service using
   * a certificate chain. This variant of web-resource does not authenticate
   * the server. Thus, it can call HTTPS endpoints where the server is
   * using a self-signed host-certificate.
   *
   * @param uri The URI for the resource.
   * @param guard The holder of the caller's credentials.
   */
  public WebResource(URI uri, SecurityGuard guard) {
    this.uri = uri;
    this.guard = guard;
  }
  
  public URI getUri() {
    return this.uri;
  }
  
  public void get() throws IOException {
    URL url = this.uri.toURL();
    this.connection = (HttpURLConnection)(url.openConnection());
    this.connection.setInstanceFollowRedirects(false);
    this.connection.setDoInput(true);
    this.connection.setDoOutput(true);
    this.connection.setRequestMethod("GET");
    if (this.guard != null) {
      this.guard.configureHttps(this.connection);
    }
    this.connection.connect();
  }
  
  public void post(Map parameters) throws IOException {
    URL url = this.uri.toURL();
    this.connection = (HttpURLConnection)(url.openConnection());
    this.connection.setInstanceFollowRedirects(false);
    this.connection.setDoInput(true);
    this.connection.setDoOutput(true);
    this.connection.setRequestMethod("POST");
    if (this.guard != null) {
      this.guard.configureHttps(this.connection);
    }
    try {
      this.connection.connect();
    }
    catch (Exception e) {
      e.printStackTrace();
      return;
    }
    StringBuffer b = new StringBuffer();
    Iterator i = parameters.keySet().iterator();
    while (i.hasNext()) {
      Object key = i.next();
      Object value = parameters.get(key);
      b.append(key.toString());
      b.append('=');
      b.append(value.toString());
      b.append('+');
    }
    OutputStream out = this.connection.getOutputStream();
    out.write(b.toString().getBytes());
    out.close();
  }
  
  public int getResponseCode() throws IOException {
    return (this.connection == null)? 0 : this.connection.getResponseCode();
  }
  
  public URI getResponseLocation() throws URISyntaxException {
    if (this.connection == null) {
      return null;
    }
    else {
      String location = this.connection.getHeaderField("Location");
      if (location == null) {
        return null;
      }
      else {
        return new URI(location);
      }
    }
  }
  
  /**
   * Provides the content of the response in an input stream.
   */
  public InputStream getInputStream() throws IOException {
    return (this.connection == null)? null : this.connection.getInputStream();
  }
  
  /**
   * Returns an object for the resource identified in the Location header
   * of the last HTTP response. This will only work if the response was
   * a redirection (3xx) or 201 "Created"; other responses won't have a
   * location.
   */
  public WebResource getRedirectionWebResource() throws URISyntaxException {
    URI locationUri = this.getResponseLocation();
    if (locationUri == null) {
      return null;
    }
    else {
      URI resourceUri = this.uri.resolve(locationUri);
      return new WebResource(resourceUri, this.guard);
    }
  }
  
  public WebResource getSubordinateWebResource(String name) throws URISyntaxException {
    URI slashedUri;
    if (this.uri.toString().endsWith("/")) {
      slashedUri = this.uri;
    }
    else {
      slashedUri = new URI(this.uri.toString() + "/");
    }
    URI subordinateUri = slashedUri.resolve(name);
    return new WebResource(subordinateUri, this.guard);
  }

  public OutputStream put() throws MalformedURLException, IOException {
    URL url = this.uri.toURL();
    this.connection = (HttpURLConnection)(url.openConnection());
    this.connection.setRequestMethod("PUT");
    this.connection.setInstanceFollowRedirects(false);
    this.connection.setDoInput(true);
    this.connection.setDoOutput(true);
    if (this.guard != null) {
      this.guard.configureHttps(this.connection);
    }
    this.connection.connect();
    return this.connection.getOutputStream();
  }
 
    
}
