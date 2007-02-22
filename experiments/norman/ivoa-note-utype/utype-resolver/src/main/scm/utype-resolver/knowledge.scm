;; Library module for UType-resolver

(require-library 'sisc/libs/srfi/srfi-1)  ;lists
(require-library 'sisc/libs/srfi/srfi-8)  ;receive
(require-library 'sisc/libs/srfi/srfi-13) ;strings

(require-library 'util/lambda-contract)
(require-library 'quaestor/utils)
(require-library 'quaestor/jena)

;; (define-syntax self-test     ;in this file, discard all self-test content
;;   (syntax-rules ()
;;     ((_ form . forms)
;;      (define dummy #f))))

(module knowledge
    (namespace-seen?
     ingest-utype-declaration-from-uri!
     query-utype-superclasses
     show-utypes-as-n3
     ;; the following two should be used only sparingly; indeed they're
     ;; really only to be used for debugging this namespace
     namespace-seen!
     ingest-utype-declaration-from-stream!)

(import s2j)
(import threading)
(import java-io)                        ;for ->jreader

(import* srfi-1 remove)
(import srfi-8)                         ;receive
(import* srfi-13 string-suffix?)
(import* quaestor-support
         report-exception
         chatter)
(import* utils
         is-java-type?
         input-stream->jstring)
(import jena)

;; Useful classes
(define-java-classes
  (<jstring> |java.lang.String|)
  (<uri> |java.net.URI|)
  <java.io.input-stream>)

;; predicates used in contracts
(define (uri? x)
  (is-java-type? x <uri>))
(define (java-stream? x)
  (is-java-type? x <java.io.input-stream>))

;; (self-test ; dummy test
;;  (expect #t (and #t #t)))

;; uri->namespace : uri-or-string -> string
(define (uri->namespace uri)
  (define-generic-java-methods to-string)
  (let ((uri-string (if (string? uri) uri (->string (to-string uri)))))
    (let loop ((i 0))
      (cond ((= i (string-length uri-string))
             uri-string)
            ((char=? (string-ref uri-string i) #\#)
             (substring uri-string 0 i))
            (else
             (loop (+ i 1)))))))

(define *namespaces-seen* '())          ;list of strings

;; namespace-seen? : uri -> boolean
(define/contract (namespace-seen? (uri (or (uri? uri) (string? uri)))
                                  -> boolean?)
  (let ((ns (uri->namespace uri)))
    (and (member ns *namespaces-seen*)
         #t)))

;; namespace-seen! : uri -> unspecified
(define/contract (namespace-seen! (uri uri?))
  (let ((ns (uri->namespace uri)))
    (or (namespace-seen? ns)
        (set! *namespaces-seen*
              (cons ns *namespaces-seen*)))))

;; uri->namespace : uri -> uri
;; Given a URI, returns the corresponding namespace (ie, the URI
;; without the fragment) as a new URI.
;; (define (uri->namespace uri)
;;   ;; the following follows the procedure implied by the class identies
;;   ;; documented at the top of the java.net.URI javadocs
;;   (define-java-class <uri> |java.net.URI|)
;;   (define-generic-java-methods
;;     get-scheme get-authority get-path get-query)
;;   (java-new <uri>
;;             (get-scheme uri)
;;             (get-authority uri)
;;             (get-path uri)
;;             (get-query uri)
;;             (java-null <jstring>)))

;; ;; first-sight-of-namespace : URI -> boolean
;; ;; Given a URI, return #t if this is the first time this URI has been seen,
;; ;; and #f otherwise
;; ;; (simple-minded linear search: use a hash-table instead?)
;; ;;
;; ;; XXX this reports a namespace as seen if we tried to retrieve it, and failed.
;; ;; Instead, we should query the model to discover if we've seen a namespace,
;; ;; or else set this true only once we've successfully loaded the RDF.
;; ;;
;; ;; The namespace comparison is done on normalised URIs, because that's
;; ;; what URI.equals() seems to do.  It shouldn't (FIXME), since
;; ;; <http://www.w3.org/TR/rdf-concepts/#section-Graph-URIref> says that
;; ;; URIs are comparable if and only if they are equal character by
;; ;; character.  There are other discussions of URI-equivalence
;; ;; mentioned there, including
;; ;; <http://www.w3.org/2001/tag/issues.html#URIEquivalence-15> and
;; ;; <http://www.textuality.com/tag/uri-comp-4>.  Leave it as this for
;; ;; the moment, since there's some cleverness going on either with
;; ;; java.net.URI.equals() or with implicit normalisation in URI
;; ;; constructors or accessors, which I can't track down just now.
;; (define *seen-namespaces* '())   ;list of jstrings (other type better?)
;; (define/contract (namespace-seen?       ;exported function
;;                   (uri uri?) ;(uri (or (uri? uri) (string? uri)))
;;                   -> boolean?)
;;   (define (memx (obj l =?))
;;     (cond ((null? l)
;;            #f)
;;           ((=? obj (car l)))
;;           (else
;;            (memx obj (cdr l) =?))))
;;   (define-generic-java-methods to-string equals)
;;   (and (memx (to-string (uri->namespace uri))
;;              *seen-namespaces*
;;              (lambda (a b) (equals a b)))
;;        #t))
;; (define/contract (namespace-seen! (uri uri?))
;;   (define-generic-java-methods to-string)
;;   (or (namespace-seen? uri)
;;       (set! *seen-namespaces*
;;             (cons (to-string (uri->namespace uri))
;;                   *seen-namespaces*))))

;; (define first-sight-of-namespace
;;   (let ((seen-namespaces '()))
;;     (define (memqx o l =?)           ;like memq, but with explicit predicate
;;       (cond ((null? l)
;;              #f)
;;             ((=? o (car l)))
;;             (else
;;              (memqx o (cdr l) =?))))
;;     (define-generic-java-methods to-string)
;;     (lambda/contract ((uri uri?) -> boolean?)
;;       (let ((ns (->string (to-string (uri->namespace uri)))))
;;         ;(define-generic-java-method equals)
;;         (cond ((memqx ns seen-namespaces
;;                       string=?
;;                       ;; (lambda (ns1 ns2) (->boolean (equals ns1 ns2)))
;;                       )
;;                (chatter "first-sight-of-namespace ~a: no" ns)
;;                #f)
;;               (else
;;                (set! seen-namespaces (cons ns seen-namespaces))
;;                (chatter "first-sight-of-namespace ~a: yes" ns)
;;                #t))))))

;; (self-test
;;  (define (mk-uri s)
;;    (java-new <uri> (->jstring s)))
;;  (define u1 (mk-uri "http://example.org/a/b#f1"))
;;  (expect simple1 #t (first-sight-of-namespace u1))
;;  (expect simple2 #f (first-sight-of-namespace u1))
;;  ;; try with a URI differing only in fragment -- same namespace
;;  (expect newfrag
;;          #f
;;          (first-sight-of-namespace (mk-uri "http://example.org/a/b#f2"))))

;; utype-model : -> model
;; The model which holds information about UTypes
(define utype-model
  (let ((model #f))
    (define (create-inferencing-model)
      (define-java-classes
        (<factory> |com.hp.hpl.jena.rdf.model.ModelFactory|))
      (define-generic-java-methods create-inf-model)
      (create-inf-model (java-null <factory>)
                        (rdf:get-reasoner "transitive")
                        (rdf:new-empty-model)))
    (lambda ()
      (if (not model)
          (let ((m (create-inferencing-model)))
            (define-generic-java-methods add)
            (define-java-classes )
            (add m (call-with-input-file (find-resource "utypes.n3")
                     (lambda (port)
                       ;;note we're using the Reader i/f: coding issues
                       ;;are handled by SISC
                       (rdf:ingest-from-stream/language
                        (->jreader port) "" "N3"))))
            (set! model m)))
      model)))

;; INGEST-UTYPE-DECLARATION-FROM-URI! : uri -> unspecified
;; 
;; Given a URI, ingest it as RDF.  Either succeeds or throws an error,
;; of the type expected by MAKE-FC
(define/contract (ingest-utype-declaration-from-uri! (uri uri?))
  (define-generic-java-methods add)
  (chatter "ingest-utype-declaration-from-uri! ~s" uri)
  (add (utype-model) (rdf:ingest-from-uri (uri->namespace uri)))
  (namespace-seen! uri))

;; INGEST-UTYPE-DECLARATION-FROM-STREAM! : stream -> unspecified
;;
;; Read N3 from a stream (this should be used much less often than the above)
(define/contract (ingest-utype-declaration-from-stream! (s java-stream?))
  (define-generic-java-methods add)
  (add (utype-model) (rdf:ingest-from-stream/language s "" "N3")))

(define (jena-model? x)
  (is-java-type? x '|com.hp.hpl.jena.rdf.model.Model|))

;; RETRIEVE-URI : string string-or-false -> integer string stream
;;
;; Retrieve a given URI, and return the status, content-type and content
;; stream as multiple values.
(define (retrieve-uri uri accept)
  (define-generic-java-methods
    open-connection
    set-request-property
    set-follow-redirects
    connect
    get-input-stream
    get-error-stream
    get-response-code
    get-content-type
    (to-url |toURL|))
  (define-java-classes
    (<url> |java.net.URL|))
  (chatter "retrieve-uri: uri=~a  accept=~a" uri accept)
  (let ((conn (open-connection (java-new <url> (->jstring uri)))))
    (if accept
        (set-request-property conn
                              (->jstring "Accept")
                              (->jstring accept)))
    (set-follow-redirects conn (->jboolean #t)) ;yes, do follow 303 responses
    (connect conn)                              ;go!
    (let ((status (->number (get-response-code conn)))
          (content-type (->string (get-content-type conn))))
      (chatter "retrieve-uri: uri=~a, status=~a, content-type=~a"
               uri status content-type)
      (values status
              content-type
              ((if (< status 400) get-input-stream get-error-stream)
               conn)))))

;; RDF:INGEST-FROM-URI : string -> model
;;
;; Given a URI, this reads in the RDF within it, and returns the
;; resulting model.
;;
;; Either succeeds, or throws an exception using REPORT-EXCEPTION (of the 
;; type expected by MAKE-FC)
;; 
;; This might more naturally live within quaestor/jena.scm.  However the 
;; original procedure, from which this is derived, was neither used nor tested
;; by Quaestor, and adding it to the Quaestor tests would require moving the
;; redirector.scm support from here to there.  That wouldn't necessarily be bad,
;; but it would be a fair amount of work.
(define/contract (rdf:ingest-from-uri
                  (uri (or (string? uri)
                           (is-java-type? uri '|java.net.URI|)))
                  -> jena-model?)
  (define-generic-java-methods to-string)
  (let ((uri-string (if (string? uri)
                        uri
                        (->string (to-string uri)))))
    (let loop ((attempts
                (list (lambda ()
                        (retrieve-uri uri-string
                                      "text/rdf+n3, application/rdf+xml"))
                      (lambda ()
                        (retrieve-uri (string-append uri-string ".n3")
                                      "text/rdf+n3"))
                      (lambda ()
                        (retrieve-uri (string-append uri-string ".rdf")
                                      "application/rdf+xml"))
                      (lambda ()
                        ;; Try getting and transforming (X)HTML.
                        ;; See <http://www.w3.org/TR/xhtml-media-types/>
                        ;; for discussion of XHTML media types.
                        (receive (status content-type http-stream)
                            (retrieve-uri (string-append uri-string ".html")
                                          "application/xhtml+xml, text/html, application/xml, text/xml")
                          (cond ((= status 200)
                                 (values 200
                                         "text/rdf+n3"
                                         (if (string-suffix? "xml" content-type)
                                             (grok-utypes/xml http-stream)
                                             (grok-utypes/tagsoup http-stream))))
                                ((= (quotient status 100) 2)
                                 ;; ooops -- that doesn't make sense
                                 (report-exception
                                  'rdf:ingest-from-uri
                                  '|SC_INTERNAL_SERVER_ERROR|
                                  "Unexpected status (~a) from request on ~a"
                                  status uri-string))
                                (else
                                 (values status content-type http-stream)))))
                      )))
      (chatter "ingest-from-uri: Trying to ingest ~a [~a]..."
               uri-string (length attempts))
      (if (null? attempts)
          (report-exception 'rdf:ingest-from-uri
                            '|SC_NOT_FOUND|
                            "Unable to retrieve resource ~a" uri-string)
          (call-with-values
              (car attempts)
            (lambda (status content-type stream)
              (chatter "  ...ingest-from-uri got status ~a" status)
              (case (quotient status 100)
                ((2)                    ;bingo!
                 (rdf:ingest-from-stream/language stream
                                                  uri-string
                                                  content-type)
                 ;;XXX here or elsewhere: add assertions that the namespace
                 ;; URI is a 'namespace', and perhaps the time, such that
                 ;; first-sight-of-namespace can query for this.
                 ;; Use new rdf:ingest-from-string/n3
                 )
                ((4)
                 ;; not necessarily a problem -- loop
                 (loop (cdr attempts)))

                ;; the following are errors
                ((1 3) ;these should have been handled within retrieve-uri
                 (report-exception 'rdf:ingest-from-uri
                                   '|SC_INTERNAL_SERVER_ERROR|
                                   "Unexpected status ~a when retrieving ~a"
                                   status uri-string))
                ((5)                    ;better give up now
                 (report-exception 'rdf:ingest-from-uri
                                   '|SC_NOT_FOUND|
                                   "Error retrieving remote resource ~a"
                                   uri-string))
                (else                   ;eh!?
                 (report-exception 'rdf:ingest-from-uri
                                   '|SC_INTERNAL_SERVER_ERROR|
                                   "Impossible HTTP status in rdf:ingest-from-uri: ~s"
                                   status)))))))))

;; (define/contract (rdf:ingest-from-uri
;;                   (uri (or (string? uri)
;;                            (is-java-type? uri '|java.net.URI|)))
;;                   -> jena-model?)
;;   (define (uri->string uri)
;;     (define-generic-java-methods to-string)
;;     (if (string? uri)
;;         uri
;;         (->string (to-string uri))))
;;   (receive (status content-type stream)
;;       (retrieve-uri (uri->string uri)
;;                     "text/rdf+n3, application/rdf+xml")
;;     (case (quotient status 100)
;;         ((2)
;;          (rdf:ingest-from-stream/language stream
;;                                           (uri->string uri)
;;                                           content-type))
;;         ((1 3)                          ;these shouldn't have happened
;;          (report-exception 'rdf:ingest-from-uri
;;                            '|SC_INTERNAL_SERVER_ERROR|
;;                            "Unexpected status ~a when retrieving ~a"
;;                            status (uri->string uri)))
;;         ((4)
;;          (report-exception 'rdf:ingest-from-uri
;;                            '|SC_NOT_FOUND|
;;                            "Unable to retrieve resource ~a"
;;                            (uri->string uri)))
;;         ((5)
;;          (report-exception 'rdf:ingest-from-uri
;;                            '|SC_NOT_FOUND|
;;                            "Error retrieving remote resource ~a"
;;                            (uri->string uri)))
;;         (else                           ;eh!?
;;          (report-exception 'rdf:ingest-from-uri
;;                            '|SC_INTERNAL_SERVER_ERROR|
;;                            "Impossible HTTP status in rdf:ingest-from-uri: ~s"
;;                            status)))))

;; (define/contract (rdf:ingest-from-uri
;;                   (uri (or (string? uri)
;;                            (is-java-type? uri '|java.net.URI|)))
;;                   -> jena-model?)
;;   (define-generic-java-methods
;;     open-connection
;;     set-request-property
;;     set-follow-redirects
;;     connect
;;     get-input-stream
;;     get-error-stream
;;     get-response-code
;;     get-content-type
;;     (to-url |toURL|)
;;     ;(get-url |getURL|)                  ;method on URLConnection
;;     to-string)
;;   (define-java-classes
;;     ;<java.io.file-input-stream>
;;     ;<java.lang.string>
;;     (<URL> |java.net.URL|))
;;   (define (uri->string uri)
;;     (if (string? uri)
;;         uri
;;         (->string (to-string uri))))
;;   (let ((conn (open-connection (if (string? uri)
;;                                    (java-new <URL> (->jstring uri))
;;                                    (to-url uri)))))
;;     (set-request-property conn ; Accept header: later, accept text/html and GRDDL
;;                           (->jstring "Accept")
;;                           (->jstring "text/rdf+n3, application/rdf+xml"))
;;     (set-follow-redirects conn (->jboolean #t)) ;yes, do follow 303 responses
;;     (connect conn)                              ;go!
;;     (let ((status (->number (get-response-code conn))))
;; ;;       (let ((str (if (< status 400) (get-input-stream conn) (get-error-stream conn))))
;; ;;         (chatter "rdf:ingest-from-uri: url=~a, status=~a, content-type=~s~%  content:~a~%"
;; ;;                (->string (to-string (get-url conn)))
;; ;;                status (->string (get-content-type conn))
;; ;;                (->string (input-stream->jstring str))))
;;       (chatter "rdf:ingest-from-uri: uri=~a, status=~a, content-type=~s"
;;                (uri->string uri) status (->string (get-content-type conn)))
;;       (case (quotient status 100)
;;         ((2)
;;          (rdf:ingest-from-stream/language (get-input-stream conn)
;;                                           (to-string uri)
;;                                           (get-content-type conn)))
;;         ((1 3)                          ;these shouldn't have happened
;;          (report-exception 'rdf:ingest-from-uri
;;                            '|SC_INTERNAL_SERVER_ERROR|
;;                            "Unexpected (can't happen) status ~a when retrieving ~a"
;;                            status (uri->string uri)))
;;         ((4)
;;          (report-exception 'rdf:ingest-from-uri
;;                            '|SC_NOT_FOUND|
;;                            "Unable to retrieve resource ~a"
;;                            (uri->string uri)))
;;         ((5)
;;          (report-exception 'rdf:ingest-from-uri
;;                            '|SC_NOT_FOUND|
;;                            "Error retrieving remote resource ~a"
;;                            (uri->string uri)))
;;         (else
;;          (report-exception 'rdf:ingest-from-uri
;;                            '|SC_INTERNAL_SERVER_ERROR|))))))

;; QUERY-UTYPE-SUPERCLASSES : string-or-uri -> list-of-strings or false
;;
;; Given a string representing a subject, find all the classes of
;; which it is a subclass, returning them as a list of strings if there are some,
;; or #f if there are none.
(define/contract (query-utype-superclasses (utype (or (string? utype)
                                                      (uri? utype)))
                                           -> (lambda (res)
                                                (or (not res)
                                                    (and (list? res)
                                                         (not (null? res))
                                                         (string? (car res))))))
  (define-generic-java-methods resource? to-string equals)
;;   (let ((r (rdf:select-statements
;;             (utype-model)
;;             (if (string? utype)
;;                 utype
;;                 (to-string utype))
;;             "http://www.w3.org/2000/01/rdf-schema#subClassOf"
;;             #f)))
;;     (chatter "query-utype-superclasses: utype=~s, r=~s"
;;              utype r)
;;     (let ((results
;;            (map (lambda (node)
;;                   (or (resource? node)
;;                       (report-exception 'query-utype-superclasses
;;                                         |SC_INTERNAL_SERVER_ERROR|
;;                                         "Class ~a has 'superclass' ~a, which is not a resource!"
;;                                         utype (to-string node)))
;;                   (->string (to-string node)))
;;                 r)))
;;       (chatter " ... results=~s" results)
;;       (if (null? results)
;;           #f
;;           results))
  (let ((utype-jstring (if (string? utype)
                           (->jstring utype)
                           (to-string utype))))
    (chatter "query-utype-superclasses: utype ~s..." utype-jstring)
    (let ((results
           (map ->string
                (remove (lambda (js) (->boolean (equals utype-jstring js)))
                        (map (lambda (node)
                               (if (resource? node)
                                   (to-string node)
                                   (report-exception 'query-utype-superclasses
                                                     |SC_INTERNAL_SERVER_ERROR|
                                                     "Class ~a has 'superclass' ~s, which is not a resource!"
                                                     utype (to-string node))))
                             (rdf:select-statements (utype-model)
                                                    utype-jstring
                                                    "http://www.w3.org/2000/01/rdf-schema#subClassOf"
                                                    #f))))))
      (chatter "query-utype-superclasses: ~s -> ~s" utype-jstring results)
      (if (null? results)
          #f
          results))))

;; Given a scheme string naming a resource (an .xslt file to be found on
;; the classpath), return a transformer which implements that transformation
(define (create-transformer-from-resource res)
  (define-java-classes
    <javax.xml.transform.transformer-factory>
    <javax.xml.transform.stream.stream-source>)
  (define-generic-java-methods
    new-instance
    new-transformer)
  (new-transformer (new-instance
                    (java-null <javax.xml.transform.transformer-factory>))
                   (java-new <javax.xml.transform.stream.stream-source>
                             (->jstring (find-resource res)))))

;; GROK-UTYPES/STREAM : input-stream -> input-stream
;;
;; The following is attractive in principle, but doesn't work, because 
;; handling threads is more complicated than this makes it appear.
;; The problem is that the thread has to have its state reaped, and
;; only then will it either return its value (not relevant below) or
;; throw any exception.  But in the design below, there's nothing
;; hanging around to reap this status, so the thread hangs around long
;; term if it's successful (as far as I can tell), and simply blocks
;; if it throws an error which is never passed on.  A working solution
;; would require some thread handler, which curated running threads,
;; and reaped them when necessary, but that's not something I'm going
;; to knock up right on the spot.
;;
;; (define grok-utypes/stream
;;   (let ((transformer #f))
;;     (define-java-classes
;;       (<source> |javax.xml.transform.stream.StreamSource|)
;;       (<result> |javax.xml.transform.stream.StreamResult|)
;;       <java.io.piped-input-stream>
;;       <java.io.piped-output-stream>
;;       )
;;     (define-generic-java-methods
;;       transform
;;       connect
;;       flush
;;       close)
;;     (lambda (input-stream)
;;       (if (not transformer)
;;           (set! transformer
;;                 (create-transformer-from-resource "grok-utypes.xslt")))
;;       (let ((pipe-out (java-new <java.io.piped-output-stream>))
;;             (pipe-in  (java-new <java.io.piped-input-stream>)))
;;         (connect pipe-out pipe-in)
;;         (call/fc
;;          (lambda (fk)                   ;transformer errors invoke this FC
;;            (thread/spawn
;;             (lambda ()
;;               (with/fc
;;                   (lambda (e k)
;;                     (chatter "Transformer error: e=~s~%" e)
;;                     ;; tidy up
;;                                         ;(flush pipe-out)
;;                                         ;(close pipe-out)
;;                                         ;get out of here!
;;                     (fk (make-nested-error (make-error
;;                                             'grok-utypes/stream
;;                                             (cons 502
;;                                                   "Error transforming HTML from source"))
;;                                            e k)
;;                         k))
;;                 (lambda ()
;;                   (transform transformer
;;                              (java-new <source> input-stream)
;;                              (java-new <result> pipe-out))
;;                   (flush pipe-out)
;;                   (close pipe-out)))))))
;;         pipe-in))))

;; GROK-UTYPES/TAGSOUP : stream -> stream
;; Given a stream containing HTML, return a stream which has this transformed
;; to N3.  Use the TagSoup parser to grok the HTML:
;; <http://home.ccil.org/~cowan/XML/tagsoup/>
(define grok-utypes/tagsoup
  (let ((transformer #f))
    (define-java-classes
      (<saxsource> |javax.xml.transform.sax.SAXSource|)
      (<saxinput> |org.xml.sax.InputSource|)
      (<result> |javax.xml.transform.stream.StreamResult|)
      <org.ccil.cowan.tagsoup.parser>
      <java.io.string-writer>
      <java.io.byte-array-input-stream>)
    (define-generic-java-methods
      transform to-string get-bytes)
    (lambda (input-stream)
      (if (not transformer)
          (set! transformer
                (create-transformer-from-resource "grok-utypes.xslt")))
      (with/fc
          (lambda (e k)
            ;; The error message E wraps a Java exception
            (define-generic-java-methods get-message)
            (report-exception 'grok-utypes/string
                              '|SC_BAD_GATEWAY| ; 502
                              "Error transforming HTML from source (~s)"
                              (->string (get-message (error-message e)))))
        (lambda ()
          (let ((sw (java-new <java.io.string-writer>))
                (parser (java-new <org.ccil.cowan.tagsoup.parser>)))
;;             (define-generic-java-methods set-feature!)
;;             (for-each (lambda (p)
;;                         (set-feature! parser
;;                                       (->jstring (car p))
;;                                       (->jboolean (cdr p))))
;;                       '(("http://xml.org/sax/features/namespaces" . #t)
;;                         ("http://xml.org/sax/features/namespace-prefixes" . #t)
;;                         ("http://www.ccil.org/~cowan/tagsoup/features/bogons-empty" . #f)))
            (transform transformer
                       (java-new <saxsource>
                                 parser
                                 (java-new <saxinput> input-stream))
                       (java-new <result> sw))
            (let ((sws (to-string sw)))
              (chatter "grok-utypes/string/tagsoup: string=~a" (->string sws))
              (java-new <java.io.byte-array-input-stream>
                        (get-bytes sws)))
;;             (java-new <java.io.byte-array-input-stream>
;;                       (get-bytes (to-string sw)))
            ))))))
;; GROK-UTYPES/XML : stream -> stream
;; Given a stream containing XHTML (though potentially any XML which had the
;; same attributes), return a stream which has this transformed
;; to N3.  Use the default XML parser.
(define grok-utypes/xml
  (let ((transformer #f))
    (define-java-classes
      (<source> |javax.xml.transform.stream.StreamSource|)
      (<result> |javax.xml.transform.stream.StreamResult|)
      <java.io.string-writer>
      <java.io.byte-array-input-stream>)
    (define-generic-java-methods
      transform to-string get-bytes)
    (lambda (input-stream)
      (if (not transformer)
          (set! transformer
                (create-transformer-from-resource "grok-utypes.xslt")))
      (with/fc
          (lambda (e k)
            ;; The error message E wraps a Java exception
            (define-generic-java-methods get-message)
            (report-exception 'grok-utypes/string
                              '|SC_BAD_GATEWAY| ; 502
                              "Error transforming HTML from source (~s)"
                              (->string (get-message (error-message e)))))
        (lambda ()
          (let ((sw (java-new <java.io.string-writer>)))
            (transform transformer
                       (java-new <source> input-stream)
                       (java-new <result> sw))
            (java-new <java.io.byte-array-input-stream>
                      (get-bytes (to-string sw)))))))))

;; TEMP DEBUGGING
(define (show-utypes-as-n3)
  (define-java-classes
    <java.io.string-writer>)
  (define-generic-java-methods
    write to-string)
  (let ((sw (java-new <java.io.string-writer>)))
    (write (utype-model) sw (->jstring "N3"))
    (->string (to-string sw))))

)
