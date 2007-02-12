;; Library module for UType-resolver

(import s2j)

(require-library 'sisc/libs/srfi/srfi-1)

(require-library 'util/lambda-contract)
(require-library 'quaestor/utils)
(require-library 'quaestor/jena)

;; (define-syntax self-test     ;in this file, discard all self-test content
;;   (syntax-rules ()
;;     ((_ form . forms)
;;      (define dummy #f))))

(module knowledge
    (first-sight-of-namespace
     ingest-utype-declaration-from-uri!
     ingest-utype-declaration-from-stream!
     query-utype-superclasses)

(import* srfi-1 remove)
(import* utils
         is-java-type?
         chatter
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

;; uri->namespace : uri -> uri
;; Given a URI, returns the corresponding namespace as a new URI.
(define (uri->namespace uri)
  ;; the following follows the procedure implied by the class identies
  ;; documented at the top of the java.net.URI javadocs
  (define-java-class <uri> |java.net.URI|)
  (define-generic-java-methods
    get-scheme get-authority get-path get-query)
  (java-new <uri>
            (get-scheme uri)
            (get-authority uri)
            (get-path uri)
            (get-query uri)
            (java-null <jstring>)))

;; first-sight-of-namespace : URI -> boolean
;; Given a URI, return #t if this is the first time this URI has been seen,
;; and #f otherwise
;; (simple-minded linear search: use a hash-table instead?)
;;
;; The namespace comparison is done on normalised URIs, because that's
;; what URI.equals() seems to do.  It shouldn't (FIXME), since
;; <http://www.w3.org/TR/rdf-concepts/#section-Graph-URIref> says that
;; URIs are comparable if and only if they are equal character by
;; character.  There are other discussions of URI-equivalence
;; mentioned there, including
;; <http://www.w3.org/2001/tag/issues.html#URIEquivalence-15> and
;; <http://www.textuality.com/tag/uri-comp-4>.  Leave it as this for
;; the moment, since there's some cleverness going on either with
;; java.net.URI.equals() or with implicit normalisation in URI
;; constructors or accessors, which I can't track down just now.
(define first-sight-of-namespace
  (let ((seen-namespaces '()))
    (define (memqx o l =?)           ;like memq, but with explicit predicate
      (cond ((null? l)
             #f)
            ((=? o (car l)))
            (else
             (memqx o (cdr l) =?))))
    (lambda/contract ((uri uri?) -> boolean?)
      (let ((ns (uri->namespace uri)))
        (define-generic-java-method equals)
        (cond ((memqx ns seen-namespaces equals)
               #f)
              (else
               (set! seen-namespaces (cons ns seen-namespaces))
               #t))))))

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
          (set! model (create-inferencing-model)))
      model)))

;; INGEST-UTYPE-DECLARATION-FROM-URI! : uri -> unspecified
;; 
;; Given a URI, ingest it as RDF.  Either succeeds or throws an error,
;; of the type expected by MAKE-FC
(define/contract (ingest-utype-declaration-from-uri! (uri uri?))
  (define-generic-java-methods add)
  (add (utype-model) (rdf:ingest-from-uri uri)))

;; INGEST-UTYPE-DECLARATION-FROM-STREAM! : stream -> unspecified
;;
;; Read N3 from a stream (this should be used much less often than the above)
(define/contract (ingest-utype-declaration-from-stream! (s java-stream?))
  (define-generic-java-methods add)
  (add (utype-model) (rdf:ingest-from-stream/language s "" "N3")))

(define (jena-model? x)
  (is-java-type? x '|com.hp.hpl.jena.rdf.model.Model|))

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
  (define-generic-java-methods
    open-connection
    set-request-property
    set-follow-redirects
    connect
    get-input-stream
    get-error-stream
    get-response-code
    get-content-type
    (to-url |toURL|)
    ;(get-url |getURL|)                  ;method on URLConnection
    to-string)
  (define-java-classes
    ;<java.io.file-input-stream>
    ;<java.lang.string>
    (<URL> |java.net.URL|))
  (define (uri->string uri)
    (if (string? uri)
        uri
        (->string (to-string uri))))
  (let ((conn (open-connection (if (string? uri)
                                   (java-new <URL> (->jstring uri))
                                   (to-url uri)))))
    (set-request-property conn ; Accept header: later, accept text/html and GRDDL
                          (->jstring "Accept")
                          (->jstring "text/rdf+n3, application/rdf+xml"))
    (set-follow-redirects conn (->jboolean #t)) ;yes, do follow 303 responses
    (connect conn)                              ;go!
    (let ((status (->number (get-response-code conn))))
;;       (let ((str (if (< status 400) (get-input-stream conn) (get-error-stream conn))))
;;         (chatter "rdf:ingest-from-uri: url=~a, status=~a, content-type=~s~%  content:~a~%"
;;                (->string (to-string (get-url conn)))
;;                status (->string (get-content-type conn))
;;                (->string (input-stream->jstring str))))
      (chatter "rdf:ingest-from-uri: uri=~a, status=~a, content-type=~s"
               (uri->string uri) status (->string (get-content-type conn)))
      (case (quotient status 100)
        ((2)
         (rdf:ingest-from-stream/language (get-input-stream conn)
                                          (to-string uri)
                                          (get-content-type conn)))
        ((1 3)                          ;these shouldn't have happened
         (report-exception 'rdf:ingest-from-uri
                           '|SC_INTERNAL_SERVER_ERROR|
                           "Unexpected (can't happen) status ~a when retrieving ~a"
                           status (uri->string uri)))
        ((4)
         (report-exception 'rdf:ingest-from-uri
                           '|SC_NOT_FOUND|
                           "Unable to retrieve resource ~a"
                           (uri->string uri)))
        ((5)
         (report-exception 'rdf:ingest-from-uri
                           '|SC_NOT_FOUND|
                           "Error retrieving remote resource ~a"
                           (uri->string uri)))
        (else
         (report-exception 'rdf:ingest-from-uri
                           '|SC_INTERNAL_SERVER_ERROR|))))))

;; QUERY-UTYPE-SUPERCLASSES : string-or-uri -> list-of-strings or false
;;
;; Given a string representing a subject, find all the classes of
;; which it is a subclass, returning them as string if there are some,
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
      (if (null? results)
          #f
          results))))

)
