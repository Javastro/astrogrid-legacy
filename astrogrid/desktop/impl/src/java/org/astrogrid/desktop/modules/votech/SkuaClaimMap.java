package org.astrogrid.desktop.modules.votech;

import java.net.URI;
import java.net.URL;
import java.net.HttpURLConnection;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.SAXParserFactory;

import org.astrogrid.acr.ivoa.resource.Resource;

/**
 * A Map of URIs to annotations of them, which are persisted in a
 * local SKUA node.  The contents of the map are retrieved initially
 * from the node, and puts to this map are persisted there.
 *
 * @fixme Once annotations are retrieved from the node, we don't
 * currently check there again, so won't pick up any further
 * annotations or other remarks which have been placed there by other
 * processes, or which have appeared in nodes we delegate to.  This
 * could be addressed by occasionally restoring the whole lot from the
 * node, or perhaps by doing a per-URI query whenever get() is called.
 */
public class SkuaClaimMap
        extends java.util.HashMap<URI,UserAnnotation> {
	// should this extend Hashtable<Resource,UserAnnotation> instead? (synchronised)

	private static final org.apache.commons.logging.Log logger
			= org.apache.commons.logging.LogFactory.getLog(SkuaClaimMap.class);

	/**
	 * The RDF namespace for (at present only one) properties specific
	 * to VODesktop.
	 * The property in question is vodesktop:highlightColour, whose
	 * range is xsd:hexBinary -- ie, a hex number unprefixed by '#' or
	 * '0x' or anything like that.
	 */
	private static final String vodesktopNamespace = "http://www.astrogrid.org/software/vodesktop#";

	private final AnnotationSource skuaSource;
	private final String skuaSourceBase;
	private final SkuaPersister skuaPersister;

	private final Map<URI,String> annotationClaims;
	private final Map<URI,Integer> annotationHashes;

	private final Map<String,String> knownPrefixes;

	private final String allClaimsQuery = 
			"prefix skua: <http://myskua.org/claimtypes/1.0#>\n" +
			"prefix dcterms: <http://purl.org/dc/terms/>\n" +
			"prefix foaf: <http://xmlns.com/foaf/0.1/>\n" +
			"prefix vodesktop: <" + vodesktopNamespace + ">\n" +
			"SELECT ?claim ?uri ?title ?description ?tag ?keyword ?colour \n" +
			"WHERE {\n" +
			"  ?claim skua:ref [ skua:uri ?uri ].\n" +
			"  OPTIONAL { ?claim skua:ref [ dcterms:description ?description ] }.\n" +
			"  OPTIONAL { ?claim skua:ref [ dcterms:title ?title ] }.\n" +
			"  OPTIONAL { ?claim skua:tag ?tag }.\n" +
			"  OPTIONAL { ?claim skua:keyword ?keyword }.\n" +
			"  OPTIONAL { ?claim vodesktop:highlightColour ?colour }.\n" +
			"}\n";

	/**
	 * A SPARQL query which will retrieve all the SKUA claims which refer to a given URI.
	 */
	private static final String claimsPerUriQuery1 = 
			"prefix skua: <http://myskua.org/claimtypes/1.0#>\n" +
			"prefix dcterms: <http://purl.org/dc/terms/>\n" +
			"prefix foaf: <http://xmlns.com/foaf/0.1/>\n" +
			"prefix vodesktop: <" + vodesktopNamespace + ">\n" +
			"SELECT ?type ?title ?description ?tag ?keyword ?colour\n" +
			"WHERE {\n" +
			"  ?claim skua:ref [ skua:uri <";
	private static final String claimsPerUriQuery2 = "> ].\n" +
			"  ?claim a ?type .\n" +
			"  OPTIONAL { ?claim skua:ref [ dcterms:title ?title ] }.\n" +
			"  OPTIONAL { ?claim skua:ref [ dcterms:description ?description ] }.\n" +
			"  OPTIONAL { ?claim skua:tag ?tag }.\n" +
			"  OPTIONAL { ?claim skua:keyword ?keyword }.\n" +
			"  OPTIONAL { ?claim vodesktop:highlightColour ?colour }.\n" +
			"}\n";


	public SkuaClaimMap(final AnnotationSource skuaSource) {
		super();
		this.skuaSource = skuaSource;

		skuaPersister = new SkuaPersister();
		skuaSourceBase = skuaSource.getSource().toString();
		annotationClaims = new java.util.HashMap<URI,String>();
		annotationHashes = new java.util.HashMap<URI,Integer>();

		// only one 'known prefix' so far -- more may appear
		knownPrefixes = new java.util.HashMap<String,String>(2);
		knownPrefixes.put("aakeys", "http://www.ivoa.net/rdf/Vocabularies/AAkeys#");

		// start off by putting into the map all the annotations we can find
		new Thread(new Runnable() {
				public void run() {
					final Map<URI,UserAnnotation> newmap = new ClaimRetriever(skuaSource, allClaimsQuery).getAnnotations();
					putAllIntoSuper(newmap);
					logger.info("Found annotations from skuaSource");
				}}).start();
	}

	// Special convenience class for the Runner in the constructor,
	// so we can call super.putAll() from there
	private void putAllIntoSuper(final Map<URI,UserAnnotation> m) 
	{
		super.putAll(m);
	}

	/**
	 * Add the given URI/Annotation pair to the map.
	 * This may be called even when the URI-Annotation mapping hasn't changed,
	 * so detect that by checking the hash of the annotation,
	 * and avoid persisting the mapping in that case.
	 * @param uri the URI being annotation
	 * @param ua the annotation
	 */
	public UserAnnotation put(final URI uri, final UserAnnotation ua) {
		// first add the mapping to this current map
		final UserAnnotation previous = super.put(uri, ua);

		// now work out if we have to persist this
		final Integer savedHash = annotationHashes.get(uri);
		
		if (savedHash == null || savedHash.intValue() != ua.hashCode()) {
			skuaPersister.persistAnnotation(uri, ua);
			annotationHashes.put(uri, Integer.valueOf(ua.hashCode()));
		}

		return previous;
	}

	// Make the unsupported operations throw the correct Exception
	public UserAnnotation remove(final Object key) {
		// we will support this later
		throw new java.lang.UnsupportedOperationException("SkuaClaimSet does not support that operation");
	}
	public void putAll(final Collection<?> c) {
		throw new java.lang.UnsupportedOperationException("SkuaClaimSet does not support that operation");
	}
	public void clear() {
		throw new java.lang.UnsupportedOperationException("SkuaClaimSet does not support that operation");
	}

	/**
	 * Retrieve information about a SKUA Claim from a SKUA service.
	 * This is potentially slow, so should be done on a separate thread.
	 */
	private class ClaimRetriever
			extends org.xml.sax.helpers.DefaultHandler {
		private final AnnotationSource skuaSource;
		private final String skuaSourceBase;
		private final String queryString;

		public ClaimRetriever(final AnnotationSource skuaSource, final String query) {
			this.skuaSource = skuaSource;
			this.queryString = query;
			skuaSourceBase = skuaSource.getSource().toString();
		}

		/**
		 * Retrieve the list of annotations from the service.
		 */
		public Map<URI,UserAnnotation> getAnnotations() {
			SparqlQuerier sq = null;
			try {
				sq = new SparqlQuerier(skuaSource.getSource().toURL());
			} catch (final java.net.MalformedURLException e) {
				logger.warn("Can't convert source " + skuaSource + " to a URL");
				return null;
			}
			
			final Map<URI,UserAnnotation> annotations = new java.util.HashMap<URI,UserAnnotation>();
			for (final Map<String,String> m : sq.makeQuery(queryString)) {
				String v;
				// Work through the items in the claim.
				// There might be multiple Annotations for a single resourceId, both because there
				// might be multiple tags, or because the annotations are coming from multiple sources.
				// If there are multiple tags, they should be collected into a Set.
				// If there are multiple titles, ignore successive ones.
				// If there are multiple descriptions, append them.
				UserAnnotation ua;

				try {
					if (m.get("uri") == null) {
						logger.warn("Given a query which didn't select ?URI");
						return null;
					}

					final URI uri = new URI(m.get("uri"));
					ua = annotations.get(uri);
					if (ua == null) {
						ua = new UserAnnotation();
						ua.setSource(skuaSource);
						ua.setResourceId(uri);
						annotations.put(uri, ua);
					}

					final String claim = m.get("claim");
					if (claim == null) {
						logger.warn("Given a query which didn't select ?CLAIM");
						return null;
					}
					if (claim.startsWith(skuaSourceBase)) {
						// it's one of ours, rather than one we've retrieved from someone else
						synchronized (annotationClaims) {
							annotationClaims.put(uri, claim);
						}
					}

					v = m.get("title");
					if (v != null && ua.getAlternativeTitle() == null) {
                        ua.setAlternativeTitle(v);
                    }

					v = m.get("description");
					if (v != null) {
						final String currentNote = ua.getNote();
						// this won't do the right thing if we have multiple descriptions coming
						// from multiple sources
						if (currentNote == null || currentNote.equals(v)) {
                            ua.setNote(v);
                        } else {
                            ua.setNote(currentNote + '\n' + v);
                        }
					}

					v = m.get("type");
					if (v != null && v.equals("http://myskua.org/claimtypes/1.0#FlaggedBookmark")) {
                        ua.setFlagged(true);
                    }

					v = m.get("tag");
					if (v != null) {
						java.util.Set tags = ua.getTags();
						if (tags == null) {
							tags = new java.util.HashSet<String>();
							ua.setTags(tags);
						}
						tags.add(v);
					}

					v = m.get("keyword");
					if (v != null) {
						java.util.Set tags = ua.getTags();
						if (tags == null) {
							tags = new java.util.HashSet<String>();
							ua.setTags(tags);
						}
						boolean foundMatch = false;
						for (final Map.Entry<String,String> me : knownPrefixes.entrySet()) {
							if (v.startsWith(me.getValue())) {
								tags.add(me.getKey() + ":" + v.substring(me.getValue().length()));
								foundMatch = true;
							}
						}
						if (!foundMatch) {
							// this namespace isn't one of the known
							// ones, so just add the whole URL as a 'tag'
							tags.add(v);
						}
					}

					v = m.get("colour");
					if (v != null) {
						try {
							final Long l = Long.valueOf(v, 16);
							ua.setHighlight(new java.awt.Color(l.intValue(), true));
						} catch (final NumberFormatException e) {
							// invalid colour -- ignore it silently
						}
					}

					annotationHashes.put(uri, Integer.valueOf(ua.hashCode()));
				} catch (final java.net.URISyntaxException e) {
					logger.warn("Can't convert string " + m.get("uri") + " to a URI");
					return null;
				}
						
			}
			return annotations;
		}
	}

	/**
	 * Make SELECT requests to a SPARQL endpoint.
	 * This is a generic method, which could usefully move to the SKUA
	 * client code.
	 */
	public static class SparqlQuerier {
		private SAXParserFactory spf = null;
		private final URL endpointURL;
		
		public SparqlQuerier(final URL endpointURL) {
			this.endpointURL = endpointURL;
		}
			
		/**
		 * Make a SPARQL SELECT query.
		 * The response is a Collection of Maps.  The collection
		 * contains one Map for each of the results of the SPARQL
		 * SELECT query.  In each map, the keys are the binding
		 * variables mentioned in the SELECT statement, and the values
		 * are the strings bound to those variables.  At present,
		 * these results are all strings; it may be useful in a future
		 * version of this method to convert the results to
		 * appropriate Java types.
		 *
		 * <p>If the input query is not a SELECT query, or if there is
		 * some other HTTP problem, then return null
		 *
		 * @param sparqlQuery a SPARQL SELECT query
		 * @return the results from the query, or null on error
		 */
		public java.util.Collection<Map<String,String>> makeQuery(final String sparqlQuery) {
			try {
				final java.net.URLConnection tempConnection = endpointURL.openConnection();
				if (! (tempConnection instanceof HttpURLConnection)) {
					logger.warn("Can't open HTTP connection to URL " + endpointURL);
					return null;
				}
			
				final HttpURLConnection conn = (HttpURLConnection)tempConnection;

				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/sparql-query");
				conn.setRequestProperty("Accept", "application/sparql-results+xml");
				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.connect();

				conn.getOutputStream().write(sparqlQuery.getBytes());

				if (spf == null) {
					spf = SAXParserFactory.newInstance();
					spf.setNamespaceAware(true);
				}

				if (! (conn.getResponseCode() == 200
					   && conn.getContentType().equals("application/sparql-results+xml"))) {
					logger.warn("Unexpected response from SPARQL endpoint: response status=" + conn.getResponseCode()
								+ ", content-type=" + conn.getContentType());
					return null;
				}

				final SparqlResponseHandler srh = new SparqlResponseHandler();
				spf.newSAXParser().parse(conn.getInputStream(), srh);

				if (srh.getResultList() == null) {
					// this shouldn't happen, but might if there was no startDocument
					// in the query response
					return null;
				} else {
					return srh.getResultList();
				}

			} catch (final javax.xml.parsers.ParserConfigurationException e) {
				logger.warn("Couldn't create a parser for SPARQL output: " + e);
				return null;//return Collections.EMPTY_LIST;
			} catch (final org.xml.sax.SAXException e) {
				logger.warn("Error parsing SPARQL response: " + e);
				return null; //return Collections.EMPTY_LIST;
			} catch (final java.io.IOException e) {
				logger.warn("IO Exception reading from URL " + endpointURL);
				return null; //return Collections.EMPTY_LIST;
			}
		}

		private class SparqlResponseHandler 
				extends org.xml.sax.helpers.DefaultHandler {
			private List<Map<String,String>> resultList = null;
			private Map<String,String> currentResult;
			private String currentBinding;
			private String currentContent;
			private boolean snarfContent;

			public List<Map<String,String>> getResultList() {
				return resultList;
			}

			// org.xml.sax implementation
			// The handler isn't terribly paranoid, and could probably be made more robust
			public void startDocument() {
				resultList = new java.util.ArrayList<Map<String,String>>();
				snarfContent = false;
			}

			public void startElement(final String uri, final String localName, final String qName,
									 final org.xml.sax.Attributes atts) {
				if (uri.equals("http://www.w3.org/2005/sparql-results#")) {
					if (localName.equals("result")) {
						currentResult = new java.util.HashMap(8); // smaller initial capacity
					} else if (localName.equals("binding")) {
						currentBinding = atts.getValue("name");
					} else if (localName.equals("uri") || localName.equals("literal")) {
						currentContent = null;
						snarfContent = true;
					}
				}
			}

			public void endElement(final String uri, final String localName, final String qName) {
				if (uri.equals("http://www.w3.org/2005/sparql-results#")) {
					if (localName.equals("result")) {
						// SAX being SAX, we can't have got here without first seeing a startElement
						// for "result", so currentResult being null can't happen
						assert currentResult != null;
						resultList.add(currentResult);
					} else if (localName.equals("uri") || localName.equals("literal")) {
						// if any of currentResult, currentBinding or currentContent is null,
						// then the XML is seriously b0rked, but it's possible (ie, not an assertion error)
						// so just skip this entry
						if (currentResult != null && currentBinding != null && currentContent != null) {
							currentResult.put(currentBinding, currentContent);
						}
						currentContent = null;
						snarfContent = false;
					}
				}
			}

			public void characters(final char[] ch, final int start, final int length) {
				if (snarfContent) {
					if (currentContent == null) {
                        currentContent = new String(ch, start, length);
                    } else {
                        currentContent = currentContent + new String(ch, start, length);
                    }
				}
			}
		}
	}

	/**
	 * Handles persisting annotations to a SKUA service.
	 * If the annotation being persisted originally came from our
	 * service (which we can tell, based on the claim URL), then we
	 * replace it; if not (because it is a new annotation, or because
	 * it originally came from another SKUA node), then create a new
	 * annotation.
	 *
	 * @fixme This logic isn't quite sufficient, because in 'updating' an
	 * annotation, we overwrite any preexisting assertions in that
	 * SKUA Claim, which won't be appropriate if there was other stuff
	 * in there that was put there by someone else.  Fixing this
	 * requires having a more sophisticated RDF interface.  If we
	 * assume for the moment that the only thing reading and writing
	 * the relevant claims is VODesktop, then we're OK.
	 *
	 * @fixme It'd be good if there were some way to get the current
	 * user's email address or name, to put in the claim.
	 */
	private class SkuaPersister
			implements Runnable {
		private Map<URI,UserAnnotation> persistenceBuffer;
		private long persistenceTimestamp;
		private final long delayInterval = 10000; // milliseconds
		private Thread persisterThread = null;
		
		/**
		 * Persist a URI-Annotation pair to the SKUA sustem
		 */
		public void persistAnnotation(final URI uri, final UserAnnotation annotation) {
			persistenceTimestamp = System.currentTimeMillis();
			logger.info("AddAnnotation: uri=" + uri + ", ann=" + annotation);
			synchronized (this) {
				if (persisterThread == null) {
					if (persistenceBuffer == null) {
                        persistenceBuffer = new java.util.HashMap<URI,UserAnnotation>();
                    }
					persistenceBuffer.put(uri, annotation);

					persisterThread = new Thread(this);
					persisterThread.start();
					logger.info("Started Persister thread");
				} else {
					// add to the list of tasks for the persister thread
					persistenceBuffer.put(uri, annotation);
				}
			}
		}

		/**
		 * Run the timed task.
		 * When we run, we check that it's at least delayInterval
		 * milliseconds since the persistAnnotation method was last
		 * called: if so, then persist the elements in the
		 * persistenceBuffer; if that buffer was modified more
		 * recently than that, then hold off.  This is because
		 * VODesktop will persist annotations while they're still
		 * being typed, so we tend to get a flurry of writes here,
		 * only the last of which is actually the real thing.
		 */
		public void run() {
			boolean keepGoing = true;
			while (keepGoing) {
				if (System.currentTimeMillis() > persistenceTimestamp + delayInterval) {
					// do it
					Map<URI,UserAnnotation> copyBuffer;
					synchronized (this) {
						logger.warn("SkuaPersister: started");
						copyBuffer = persistenceBuffer;
						persisterThread = null; // forget the reference to this thread
						persistenceBuffer = null; // ...and discard the reference to this buffer, now we've copied it
					}
					// Finally, go ahead and persist the elements of the copied buffer
					// Do the work on this thread.
					for (final URI uri : copyBuffer.keySet()) {
						persistAnnotationToSKUA(uri, copyBuffer.get(uri));
					}
					// ...and end the thread
					logger.info("SkuaPersister: thread due to end...");
					keepGoing = false;
				} else {
					logger.info("SkuaPersister: postponed for another " + delayInterval + "ms");
					try {
						synchronized (this) {
							wait(delayInterval);
						}
					} catch (final InterruptedException e) {
						// nothing to do
					}
				}
			}
		}

		private void persistAnnotationToSKUA(final URI uri, final UserAnnotation ann) {
			// Create a Turtle claim.  There should be a better way of doing this, using the SKUA API
			final StringBuffer sb = new StringBuffer();
			sb.append("@prefix skua: <http://myskua.org/claimtypes/1.0#> .\n");
			sb.append("@prefix dcterms: <http://purl.org/dc/terms/> .\n");
			sb.append("@prefix foaf: <http://xmlns.com/foaf/0.1/> .\n");
			sb.append("@prefix vodesktop: <").append(vodesktopNamespace).append(">.\n"); // FIXME fake URI
			sb.append("<>\n");
			if (ann.isFlagged()) {
                sb.append("a skua:FlaggedBookmark;\n");
            } else {
                sb.append("a skua:Bookmark;\n");
            }
			// how do we get the identity of the current user?
			sb.append("dcterms:creator [ a foaf:Person; foaf:name \"Me!\"; ];\n");
			sb.append("skua:ref [ a skua:IVORN;\n" ); // we're always annotating IVORNs in VODesktop
			sb.append("  skua:uri <").append(uri.toString()).append(">;\n");
			if (ann.getAlternativeTitle() != null) {
                sb.append("  dcterms:title \"\"\"").append(ann.getAlternativeTitle()).append("\"\"\";\n");
            }
			if (ann.getNote() != null) {
                sb.append("  dcterms:description \"\"\"").append(ann.getNote()).append("\"\"\";\n");
            }
			sb.append("];\n");
			final java.util.Set tags = ann.getTags();
			if (tags != null) {
				for (final Iterator i=tags.iterator(); i.hasNext(); ) {
					final String s = (String)i.next();
					final int colon = s.indexOf(':');
					if (colon > 0) {
						final String prefix = s.substring(0, colon);
						final String namespace = knownPrefixes.get(prefix);
						if (namespace != null) {
                            sb.append("skua:keyword <").append(namespace).append(s.substring(colon+1)).append(">;\n");
                        } else {
                            sb.append("skua:tag \"").append(s).append("\";\n");
                        }
					} else {
						sb.append("skua:tag \"").append(s).append("\";\n");
					}
				}
			}
			if (ann.getHighlight() != null) {
                sb.append("vodesktop:highlightColour \"")
						.append(Integer.toHexString(ann.getHighlight().getRGB()))
						.append("\";\n");
            }
			sb.append("skua:time \"");
			sb.append(String.format("%1$tFT%1$tT%1$tz", java.util.Calendar.getInstance()));
			sb.append("\".\n");

			final String turtleClaim = sb.toString();
			logger.info("Adding claim to "  + skuaSourceBase + ": " + turtleClaim);

			final org.myskua.sac.client.QsacSacClientImpl sac = new org.myskua.sac.client.QsacSacClientImpl();
			try {
				synchronized (annotationClaims) {
					final String claimURL = annotationClaims.get(uri);
					if (claimURL == null) {
						final String newClaim = sac.addClaim(skuaSourceBase, turtleClaim, sac.TURTLE_CONTENT);
						annotationClaims.put(uri, newClaim);
					} else {
						sac.replaceClaim(claimURL, turtleClaim, sac.TURTLE_CONTENT);
					}
				}
			} catch (final org.myskua.sac.client.SacClientException e) {
				logger.warn("Failed to add claim to SAC " + skuaSourceBase + ": " + e);
			}
		}
	}

}


// Temporary: Norman's buffer settings
// Local Variables:
// c-basic-offset: 4
// tab-width: 4
// indent-tabs-mode: t
// End:
