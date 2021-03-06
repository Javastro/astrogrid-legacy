;; Library module for UType-resolver

(require-library 'sisc/libs/srfi/srfi-1)  ;lists
(require-library 'sisc/libs/srfi/srfi-8)  ;receive
(require-library 'sisc/libs/srfi/srfi-13) ;strings

(require-library 'util/lambda-contract)
(require-library 'quaestor/utils)
(require-library 'quaestor/jena)
(require-library 'quaestor/knowledgebase)


(module knowledge
    (query-utype-superclasses
     get-namespace-description
     get-namespace-list
     forget-namespace!
     ;; The following should be used only sparingly; they're
     ;; really only to be used for debugging and testing this module.
     show-utypes-as-n3
     *ingest-utype-declaration/stream!)

(import s2j)
(import threading)
(import java-io)                        ;for ->jreader

(import* srfi-1 remove)
(import srfi-8)                         ;receive
(import* srfi-13
         string-suffix?
         string-index)
(import* quaestor-support
         report-exception
         chatter)
(import* utils
         is-java-type?
         input-stream->jstring)
(import jena)
(import knowledgebase)

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
(define (jstring? x)
  (is-java-type? x <jstring>))

;; Simple assert macro
;;   (assert [id] (test ...))
;;   (assert [id] (test ...) var ...)
;; See eg <http://okmij.org/ftp/Scheme/util.html#assert> for 
;;    a more sophisticated version
(define-syntax assert
  (syntax-rules ()
    ((_ (test ...))
     (assert assertion-failure (test ...)))
    ((_ id (test ...))
     (if (not (test ...))
         (error (quote id) "Assertion ~s failed"
                (quote (test ...)))))
    ((_ id (test ...) (reports ...))
     (if (not (test ...))
          (let ((reps (map (lambda (p)
                             (format #f "  ~a=~s~%" (car p) (cdr p)))
                           `(reports ...))))
            (error (quote id)
                   (apply string-append
                          (cons (format #f "Assertion ~s failed.~%Bindings:~%"
                                        (quote (test ...)))
                                reps))))))
    ((_ id (test ...) (reports ...) report . more-reports)
     (assert id (test ...) ((report . ,report) . (reports ...)) . more-reports))
    ;; interface
    ((_  (test ...) report . more-reports)
     (assert assertion-failure (test ...) ((report . ,report)) . more-reports))
    ((_ id (test ...) report . more-reports)
     (assert id (test ...) ((report . ,report)) . more-reports))))

;; uri->namespace : uri/string/jstring -> string
;; (ripe for memoization???)
(define (uri->namespace uri)
  (define-generic-java-method to-string)
  (let ((s (cond ((string? uri) uri)
                 ((jstring? uri)
                  (->string uri))
                 (else
                  (->string (to-string uri))))))
    (cond ((string-index s #\#)
           => (lambda (index)
                (substring s 0 index)))
          (else
           s))))

;; KNOWLEDGEBASE : -> quaestor-knowledgebase
;; Return the Quaestor knowledgebase for this application, creating
;; it if necessary.
(define knowledgebase
  (let ((kb #f))
    (define-java-class <java.io.byte-array-input-stream>)
    (define-generic-java-method get-bytes)
    (define knowledgebase-metadata "
@prefix dc: <http://purl.org/dc/elements/1.1/>.
@prefix quaestor: <http://ns.nxg.me.uk/quaestor#>.

<> dc:description \"Namespace knowledgebase\";
    dc:creator \"Norman\";
    quaestor:requiredReasoner [
        quaestor:level \"simpleRDFS\"
    ].")
    (lambda ()
      (if (not kb)
          (let ((knowledgebase-metadata-stream
                 (java-new <java.io.byte-array-input-stream>
                           (get-bytes (->jstring knowledgebase-metadata)))))
            (set! kb (kb:new "namespaces"))
            (kb 'set-metadata
                knowledgebase-metadata-stream
                "urn:dummy"             ;dummy non-null base URI
                "text/rdf+n3")))
      kb)))

;; namespace-seen? : uri/string/jstring -> object
;; Return true (non-#f) if we've seen this namespace already
(define (namespace-seen? uri)
  (let ((ns (uri->namespace uri)))
    ((knowledgebase) 'has-model ns)))

(define/contract (forget-namespace! (uri string?) -> boolean?)
  (let ((ns (uri->namespace uri)))
    ((knowledgebase) 'drop-submodel ns)))

;; INGEST-UTYPE-DECLARATION/URI! : uri/string/jstring -> unspecified
;; 
;; Given a URI, ingest it as RDF.  Either succeeds or throws an error,
;; of the type expected by MAKE-FC
(define (ingest-utype-declaration/uri! uri)
  (let ((ns (uri->namespace uri)))
    (chatter "ingest-utype-declaration-from-uri! ~s" uri)
    ((knowledgebase) 'add-tbox ns (rdf:ingest-from-uri ns))))

;; *INGEST-UTYPE-DECLARATION/STREAM! : string stream -> unspecified
;;
;; Ingest N3 from the given stream, in the context of the given namespace.
;; For debugging/testing only -- include the * at the beginning to mark this.
(define/contract (*ingest-utype-declaration/stream! (ns string?)
                                                    (s java-stream?))
  ((knowledgebase) 'add-tbox ns (rdf:ingest-from-stream/language s ns "N3")))

(define (jena-model? x)
  (is-java-type? x '|com.hp.hpl.jena.rdf.model.Model|))

;; GET-NAMESPACE-DESCRIPTION : string -> model-or-#f
;; GET-NAMESPACE-DESCRIPTION : -> model
(define (get-namespace-description . arg)
  (case (length arg)
    ((0)
     (*get-all-description))
    ((1)
     (*get-namespace-description (car arg)))
    (else
     (error "Bad call to GET-NAMESPACE-DESCRIPTION with args ~s" arg))))
(define/contract (*get-namespace-description (ns string?)
                                            -> (lambda (o)
                                                 (or (not o)
                                                     (jena-model? o))))
  ((knowledgebase) 'get-model (uri->namespace ns)))
(define/contract (*get-all-description -> jena-model?)
  ((knowledgebase) 'get-model))

;; GET-NAMESPACE-LIST : -> list of strings
;;
;; Return a list of strings representing the namespaces
;; the knowledgebase knows about.
(define/contract (get-namespace-list -> list?)
  (let ((submodel-pair (assq 'submodels ((knowledgebase) 'info))))
    (with/fc
        (lambda (m e)
          (error 'get-namespace-list
                 "Ooops: can't get submodel list from knowledgebase (this really shouldn't happen!) [~s]" m))
      (lambda ()
        (map (lambda (alist)
               (cdr (assq 'name alist)))
             (cdr submodel-pair))))))

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
                                                  content-type
                                                  ;; report failures as
                                                  ;; `not my fault'
                                                  '|SC_BAD_GATEWAY|)
                 ;; XXX This is a point at which we could usefully add
                 ;; annotations such as the time, for expiry purposes.
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

;; QUERY-UTYPE-SUPERCLASSES : string/jstring/uri -> list-of-strings or false
;;
;; Given a string representing a subject, find all the classes of
;; which it is a subclass, returning them as a list of strings if there are some,
;; or #f if there are none.
;;
;; If we haven't seen the namespace before, then load it and re-call ourselves.
;; If one or more of the superclasses is in a namespace we haven't seen before,
;; then load them and re-call ourselves.
;;
;; XXX This function is probably full of race conditions, connected with
;; namespace-seen and ingest-utype-declaration/uri!  Does this simply require
;; wrapping the whole body in a synchronisation block?  Synchronising on what,
;; though?  And would the procedure's re-calling itself mess that up?
(define/contract (query-utype-superclasses (utype-a (or (jstring? utype-a)
                                                        (string? utype-a)
                                                        (uri? utype-a)))
                                           -> (lambda (res)
                                                (or (not res)
                                                    (and (list? res)
                                                         (not (null? res))
                                                         (string? (car res))))))
  (let ((rdfs-ns (->jstring "http://www.w3.org/2000/01/rdf-schema#")))
    (define-generic-java-methods to-string)
    (define wrapped-ingest-utype-declaration/uri!
      (let ((fc (lambda (m k)
               (let ((msg (error-message m)))
                 ;; this is probably a (symbol . string)
                 ;; pair thrown by report-exception, because
                 ;; the namespace document couldn't be
                 ;; retrieved...
                 (report-exception
                  'query-utype-superclasses
                  '|SC_BAD_GATEWAY|     ;not our fault
                  "Unable to determine superclasses (unretrievable namespace?):~%~s~%~%~%(I don't believe this is my fault, by the way...)"
                  (if (and (pair? msg)
                           (symbol? (car msg))) ;yup!
                      (format #f "~s: ~a" (car msg) (cdr msg))
                      msg))))))
        (lambda (uri)
          (with/fc
              fc
            (lambda ()
              (ingest-utype-declaration/uri! uri))))))
    (define (get-known-superclasses model utype) ;utype is jstring
      (define-generic-java-methods resource? equals starts-with)
      (remove (lambda (js)
                ;; remove this URI, and any RDFS classes
                ;; (typically rdfs:Resource)
                (or (->boolean (equals js utype))
                    (->boolean (starts-with js rdfs-ns))))
              (map (lambda (node)
                     (assert query-utype-superclasses
                             (resource? node)
                             node utype)
                     (to-string node))
                   (rdf:select-statements model
                                          utype
                                          "http://www.w3.org/2000/01/rdf-schema#subClassOf"
                                          #f))))
    (let ((utype (cond ((jstring? utype-a)
                        utype-a)
                       ((string? utype-a)
                        (->jstring utype-a))
                       (else
                        (to-string utype-a))))
          (model ((knowledgebase) 'get-inferencing-model)))
      ;; the model will be #f if the reasoner knows nothing at all (first time)
      (if (and model
               (namespace-seen? utype))
          (let ((results (map ->string (get-known-superclasses model utype))))
            (if (null? results)
                #f                      ;definitely no superclasses
                (let ((unknown-namespaces (remove namespace-seen? results)))
                  (if (null? unknown-namespaces)
                      results           ;all's well
                      (begin (for-each wrapped-ingest-utype-declaration/uri!
                                       unknown-namespaces)
                             ;; ...and re-call
                             (query-utype-superclasses utype))))))
          (begin (wrapped-ingest-utype-declaration/uri! utype)
                 (assert query-utype-superclasses
                         ((knowledgebase) 'get-inferencing-model))
                 ;; ...and re-call
                 (query-utype-superclasses utype))))))

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
            ;; if we set the following properties on the TagSoup parser
            ;; ("http://xml.org/sax/features/namespaces" . #t)
            ;; ("http://xml.org/sax/features/namespace-prefixes" . #t)
            ;; ("http://www.ccil.org/~cowan/tagsoup/features/bogons-empty" . #f)
            ;; then it appears it can also read XML, and so be used for both
            ;; this and the GROK-UTYPES/XML, but that feels a bit fragile.
            (transform transformer
                       (java-new <saxsource>
                                 parser
                                 (java-new <saxinput> input-stream))
                       (java-new <result> sw))
            (let ((sws (to-string sw)))
              (chatter "grok-utypes/string/tagsoup: string=~a" (->string sws))
              (java-new <java.io.byte-array-input-stream>
                        (get-bytes sws)))))))))

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

;; TEMP DEBUGGING
(define (show-utypes-as-n3)
  (define-java-classes
    <java.io.string-writer>)
  (define-generic-java-methods
    write to-string)
  (let ((sw (java-new <java.io.string-writer>)))
    (write ((knowledgebase) 'get-model) sw (->jstring "N3"))
    (write ((knowledgebase) 'get-metadata) sw (->jstring "N3"))
    (->string (to-string sw))))

)
