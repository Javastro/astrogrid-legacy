;; SISC library module for Quaestor
;; Interactions with Jena

(import s2j)

(require-library 'sisc/libs/srfi/srfi-1)
(require-library 'sisc/libs/srfi/srfi-13)
(require-library 'sisc/libs/srfi/srfi-26)

(require-library 'quaestor/utils)

(require-library 'util/lambda-contract)

(module jena
( rdf:new-empty-model
  ;rdf:ingest-from-stream
  rdf:ingest-from-stream/language
  rdf:ingest-from-string/n3
  rdf:merge-models
  rdf:mime-type->language
  rdf:language->mime-type
  rdf:mime-type-list
  rdf:get-reasoner
  rdf:get-property-on-resource
  rdf:get-properties-on-resource
  rdf:select-statements
  rdf:select-object/string)

(import* srfi-1
         fold)
(import* srfi-13
         string-prefix?
         string-index)
(import* srfi-26
         cut)
(import* utils
         is-java-type?
         jobject->list)

;; heavily used classes
(define-java-classes
  <java.lang.string>
  <java.io.input-stream>
  <java.io.reader>
  <com.hp.hpl.jena.rdf.model.resource>
  <com.hp.hpl.jena.rdf.model.property>
  (<rdfnode> |com.hp.hpl.jena.rdf.model.RDFNode|)
  (<uri> |java.net.URI|))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; contract assertions

;; is something a java stream or a java reader
(define (java-input? x)
  (or (is-java-type? x <java.io.input-stream>)
      (is-java-type? x <java.io.reader>)))
(define (jstring? x)                    ;null objects are not Strings
  (is-java-type? x <java.lang.string>))
;; (define (jstring? x) ; null objects are also deemed true
;;   (or (java-null? x)
;;       (is-java-type? x <java.lang.string>)))
(define (string-or-false? x)
  (or (not x) (string? x)))
;; is something a Jena model?
(define (jena-model? x)
  (is-java-type? x '|com.hp.hpl.jena.rdf.model.Model|))
(define (jena-model-or-false? x)
  (or (not x) (jena-model? x)))
(define (jena-resource? x)
  (is-java-type? x <com.hp.hpl.jena.rdf.model.resource>))
(define (jena-property? x)
  (is-java-type? x <com.hp.hpl.jena.rdf.model.property>))
(define (jena-rdfnode? x)
  (is-java-type? x <rdfnode>))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Start the implementation

;; Return a new empty model
(define/contract (rdf:new-empty-model -> jena-model?)
  (define-java-class <com.hp.hpl.jena.rdf.model.model-factory>)
  ((generic-java-method '|createDefaultModel|)
   (java-null <com.hp.hpl.jena.rdf.model.model-factory>)))

;; rdf:ingest-from-uri : string -> model
;; Given a URI, this reads in the RDF within it, and returns the
;; resulting model.
;;
;; Either succeeds, or throws an exception using REPORT-EXCEPTION (of the 
;; type expected by MAKE-FC)
;; 
;; NB: not currently covered by test cases
;; (define/contract (rdf:ingest-from-uri
;;                   (uri (or (string? uri)
;;                            (is-java-type? uri '|java.net.URI|)))
;;                   -> jena-model?)
;;   (define-generic-java-methods
;;     ;read
;;     open-connection
;;     get-input-stream
;;     get-content-type
;;     (to-url |toURL|))
;;   (define-java-classes
;;     ;<java.io.file-input-stream>
;;     ;<java.lang.string>
;;     (<URL> |java.net.URL|))
;;   (let ((conn (open-connection (if (string? uri)
;;                                    (java-new <URL> (->jstring uri))
;;                                    (to-url uri)))))
;; ;  (let* ((full-uri (normalise-uri uri))
;; ;         (conn (open-connection (java-new <URL> (->jstring full-uri)))))
;;     (rdf:ingest-from-stream/language (get-input-stream conn)
;;                                      uri
;;                                      (get-content-type conn))))

;; RDF:MERGE-MODELS : list -> model
;;
;; Given a list of RDF models, merge them into a single one, and return it.
;; If there are no models in the list, return a new empty model.
;; Note that if there is only one model in the input list, this currently
;; has the effect of duplicating it -- this might change in future.
(define/contract (rdf:merge-models (models list?) -> jena-model?)
  (define-generic-java-method add)
  (fold (lambda (new result)
          (add result new))
        (rdf:new-empty-model)
        models))

;; Given a language LANG, which may be a Jstring, scheme string or #f,
;; return true if it is one of the allowed RDF languages, "RDF/XML[-ABBREV]",
;; "N-TRIPLE" and "N3".  Returns #f if it is not a legal language.
;;  (define (rdf:language-ok? lang)
;;    (or (not lang)
;;        (member (as-scheme-string lang)
;;                '("RDF/XML" "RDF/XML-ABBREV" "N-TRIPLE" "N3"))))

;; The set of mappings from MIME types to RDF languages.
;; Used in both directions.
(define mime-lang-mappings
  '( ;; default type -- leave this first, so rdf:mime-type-list can strip it
    ("*/*"                 . "N3")    ;Notation3 is the default type

    ;; ...http://www.w3.org/DesignIssues/Notation3
    ;; (and there's apparently an IANA registration pending)
    ("text/rdf+n3"         . "N3")

    ;; ...http://infomesh.net/2002/notation3/#mimetype
    ;; (but deprecated in the Notation3 page above)
    ("application/n3"      . "N3")

    ;;MIME type for Turtle http://www.dajobe.org/2004/01/turtle/
    ("application/x-turtle" . "N3")

    ;;Now preferred to application/x-turtle
    ;; see http://www.w3.org/TeamSubmission/turtle/#sec-mediaReg
    ("text/turtle"         . "N3")

    ;; ...http://www.w3.org/TR/rdf-syntax-grammar/#section-MIME-Type
    ;; Generic RDF MIME type: http://www.ietf.org/rfc/rfc3870.txt
    ("application/rdf+xml" . "RDF/XML")

    ;; ...http://www.dajobe.org/2001/06/ntriples/
    ("text/plain"          . "N-TRIPLE")
    ))

;; RDF:MIME-TYPE->LANGUAGE : string-or-false -> string-or-false
;;
;; Given a MIME type (a non-null string), return an RDF language, as
;; one of the strings accepted by RDF:LANGUAGE-OK?.
;; As a convenience, the `string' may be passed as #f,
;; to retrieve the default language.
;; Any parameters on the MIME type are ignored.
;; If the MIME type isn't recognised, return #f.
(define/contract (rdf:mime-type->language (s string-or-false?)
                                          -> string-or-false?)
  (let ((p (assoc (cond ((not s)
                         "*/*")
                        ((string-index s #\;)
                         => (cut substring s 0 <>))
                        (else
                         s))
                  mime-lang-mappings)))
    (and p (cdr p))))

;; RDF:LANGUAGE->MIME-TYPE : string -> string-or-false
;;
;; Map RDF language to MIME type.  This is the inverse of
;; RDF:MIME-TYPE->LANGUAGE.  Return #f if LANG is not a legal language.
(define/contract (rdf:language->mime-type (lang string?) -> string-or-false?)
  (let loop ((l (cdr mime-lang-mappings))) ;strip initial "*/*"
    (cond ((null? l)
           #f)
          ((string=? lang (cdar l))
           (caar l))
          (else
           (loop (cdr l))))))

;; Return a list of allowed mime-types.  This does not include "*/*".
(define (rdf:mime-type-list)
  (map car (cdr mime-lang-mappings)))

;; RDF:INGEST-FROM-STREAM : java-stream string -> java-model
;; RDF:INGEST-FROM-STREAM : java-reader string -> java-model
;;
;; Given a Java STREAM or Java Reader, this reads in the RDF within it,
;; and returns the resulting model.
;; MIME-TYPE may be #f, in which case we use the
;; default language (as for */* accept headers).  If it is given, it
;; must be one of the mime-types which is handled by RDF:MIME-TYPE->LANGUAGE.
;;
;; If there is a problem reading the stream, or if the mime-type is present
;; and illegal, then throw an error.
;; (define/contract (rdf:ingest-from-stream (stream java-input?)
;;                                          (mime-type string-or-false?)
;;                                          -> jena-model?)
;;   (let ((language (if mime-type
;;                       (or (rdf:mime-type->language mime-type)
;;                           (error (format #f "rdf:ingest-from-stream not an RDF MIME type: ~s"
;;                                          mime-type)))
;;                       (rdf:mime-type->language #f)))) ;use default
;;     (rdf:ingest-from-stream/language stream "" language)))

;; Implementation of the RDFErrorHandler interface
(define-java-classes
  (<RDFErrorHandler> |com.hp.hpl.jena.rdf.model.RDFErrorHandler|))
(define-java-proxy (rdf-error-handler uri logger)
  (<RDFErrorHandler>)
  (define (error p ex)
    (define-generic-java-method get-message)
    (error (format #f "Error: can't read ~a: ~s"
                   uri (->string (get-message ex)))))
  (define (fatal-error p ex)
    (define-generic-java-method get-message)
    (error (format #f "RDF fatal parse error reading ~a: ~a"
                   uri (->string (get-message ex)))))
  (define (warning p ex)
    (define-generic-java-method get-message)
    ;; just add it to the logger
    (logger "Warning parsing RDF at ~a: ~a" uri (->string (get-message ex)))))

;; RDF:INGEST-FROM-STREAM/LANGUAGE : jinput-stream string (j)string/uri [symbol] -> model
;; RDF:INGEST-FROM-STREAM/LANGUAGE : jreader       string (j)string/uri [symbol] -> model
;;
;; This is the function which ingests RDF from an InputStream or Reader,
;; which is expected to be in the named LANGUAGE.  The RDF is read using the
;; given BASE-URI, and any errors reported include this name.
;;
;; The LANGUAGE may be a java or scheme string representing a content-type
;; or (if it is not a valid content-type) a Jena language.  The LANGUAGE
;; may not be null (ie, we don't fall back on the default Jena behaviour).
;;
;; Either succeeds, or throws an exception using REPORT-EXCEPTION (of the 
;; type expected by MAKE-FC).  The optional EXCEPTION argument must contain
;; a symbol which is one of the symbols acceptable to SET-RESPONSE-STATUS!,
;; namely one of the SC_* fields in javax.servlet.http.HttpServletResponse.
;; In this case, this indicates the HTTP status which should be used when
;; reporting any error, instead of the default SC_BAD_REQUEST.
;;
;; Is there an issue about encodings, here?  Interface
;; com.hp.hpl.jena.rdf.model.RDFReader suggests that it handles encodings,
;; but it's not transparently clear to me quite how.  RDF/XML should be OK,
;; since XML should have the encoding declared in the XML declaration,
;; but can the same be said for Notation3 -- is that always UTF-8,
;; for example?
(define (rdf:ingest-from-stream/language stream base-uri language . exception)
  (cond ((null? exception)
         (*rdf:ingest-from-stream/language stream base-uri language #f))
        ((and (= (length exception) 1)
              (symbol? (car exception)))
         (*rdf:ingest-from-stream/language stream base-uri language
                                           (car exception)))
        (else
         (error 'rdf:ingest-from-stream/language
                "Bad call: wrong number or type of arguments ~s" exception))))

;; ...and the actual procedure
(define/contract (*rdf:ingest-from-stream/language
                  (stream java-input?)
                  (base-uri (or (jstring? base-uri)
                                (string? base-uri)
                                (is-java-type? base-uri <uri>)))
                  (language (or (jstring? language) (string? language)))
                  (exception (or (not exception) (symbol? exception)))
                  -> jena-model?)
  (define-generic-java-methods
    read
    close
    get-reader
    set-error-handler)
  (define logger             ;collects warnings from rdf-error-handler
    (let ((errlist '()))
      (lambda args
        (if (null? args)
            (and (not (null? errlist)) ;return non-null or #f
                 (reverse errlist))
            (set! errlist
                  (cons (apply format `(#f ,(string-append (car args) "~%")
                                           . ,(cdr args)))
                        errlist))))))

  (let ((ser-lang (cond ((and (string? language)
                              (rdf:mime-type->language language))
                         => ->jstring)
                        ((string? language)
                         (->jstring language))
                        ((rdf:mime-type->language (->string language))
                         => ->jstring)
                        (else
                         language)))
        (model (rdf:new-empty-model)))

    (with/fc
        (lambda (m e)
          (let ((logger-msgs (cond ((logger)
                                    => (lambda (l)
                                         (format #f "Other warnings:~%~a~%"
                                                 (apply string-append l))))
                                   (else ""))))
            (chatter "rdf:ingest-from-stream/language: error reading ~a (~a):~a"
                     (as-scheme-string base-uri)
                     (format-error-record m)
                     logger-msgs)
            (close stream)              ;might help...
            (report-exception 'ingest-from-stream
                              (or exception
                                  '|SC_BAD_REQUEST|)
                              "Error reading ~a (~a)~%~a"
                              (as-scheme-string base-uri)
                              (format-error-record m)
                              logger-msgs)))
      (lambda ()
        (chatter "rdf:ingest-from-stream/language: base=~a language=~s -> ~s"
                 (as-scheme-string base-uri)
                 language ser-lang)
        (let ((reader (and model (get-reader model ser-lang))))
          (or reader
              (error "Failed to get reader!"))
          (set-error-handler reader
                             (rdf-error-handler (as-scheme-string base-uri)
                                                logger))
          (read reader model stream (as-java-string base-uri))
          (close stream))
        (chatter "rdf:ingest-from-stream/language: read stream OK")))
    ;; we might as well add any logger warnings to the (chatter)
    (cond ((logger)
           => (lambda (l)
                (chatter (apply string-append (cons "Logger warnings: " l))))))
    model))

;; RDF:INGEST-FROM-STRING/N3 : string uri? -> model
;;
;; Convenience method, which takes a string containing Notation3,
;; and ingests it.  The language is fixed as N3, and the base URI
;; is either the given URI, or "".
;;
;; Either succeeds, or throws an exception.
(define (rdf:ingest-from-string/n3 string . opt-base-uri)
  (rdf:ingest-from-string/n3* string
                              (if (null? opt-base-uri)
                                  (->jstring "")
                                  (as-java-string (car opt-base-uri)))))
(define/contract (rdf:ingest-from-string/n3* (string string?) (base jstring?)
                  -> jena-model?)
  (define-java-class <java.io.string-reader>)
  (rdf:ingest-from-stream/language (java-new <java.io.string-reader>
                                             (->jstring string))
                                   base
                                   (->jstring "N3")))

;; Return the object S, which should be either a Java or Scheme string,
;; as a Scheme string.
(define (as-scheme-string s)
  (define-generic-java-method to-string)
  (cond ((string? s)
         s)
        ((java-object? s)               ;should be a jstring
         (->string (to-string s)))
        (else
         #f)))
(define (as-java-string s)
  (define-generic-java-method to-string)
  (cond ((java-object? s)
         (to-string s))
        ((string? s)
         (->jstring s))
        (else
         #f)))

;; RDF:GET-PROPERTY-ON-RESOURCE : resource property-or-string -> rdfnode-or-false
;;
;; Return a single object (RDFNode), or #f if there is no such property
;; Equivalent to (car (RDF:GET-PROPERTIES-ON-RESOURCE resource property)),
;; except that it returns #f if the list is null
(define (rdf:get-property-on-resource resource property)
    (let ((l (rdf:get-properties-on-resource resource property)))
      (if (null? l)
          #f
          (car l))))

;; RDF:GET-PROPERTIES-ON-RESOURCE : resource property-or-string -> list-of-objects
;;
;; Return list of objects corresponding to the given property, on the given
;; resource.  The property may be a Java Property or a (scheme) string.
(define/contract (rdf:get-properties-on-resource (resource jena-resource?)
                                                 (property (or (string? property)
                                                               (jena-property? property)))
                                                 -> list?)
  (define-generic-java-methods
    get-model list-properties create-property get-object)
  (let ((model (get-model resource)))
    (map get-object
         (jobject->list
          (list-properties
           resource
           (if (jena-property? property)
               property
               (create-property model (->jstring property))))))))

;; RDF:SELECT-OBJECT/STRING : model resource-or-string property-or-string -> string-or-false
;; Equivalent to (->string (to-string (car (rdf:select-statements model resource property #f)))),
;; but returning #f if there are no matches
(define/contract (rdf:select-object/string (model jena-model?)
                                           (subject   (or (jena-resource? subject)
                                                          (string? subject)
                                                          (jstring? subject)))
                                           (predicate (or (jena-property? predicate)
                                                          (string? predicate)
                                                          (jstring? predicate)))
                                           -> (lambda (x)
                                                (or (not x)
                                                    (string? x))))
  (define-generic-java-method to-string)
  (let ((result-nodes (rdf:select-statements model subject predicate #f)))
    (and (not (null? result-nodes))
         (->string (to-string (car result-nodes))))))

;; RDF:SELECT-STATEMENTS : model subject predicate object -> list-of-rdfnode
;;
;; Query a model, matching the specified patterns.
;;
;; SUBJECT and PREDICATE may be scheme/java strings, or URIs, or Java objects of type
;; Resource and Property respectively.  In the former cases, they are
;; transformed into the appropriate Java objects.
;;
;; As a special case, PREDICATE may be the scheme string "a", in which
;; case it is transformed into the Property
;; <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>.
;;
;; OBJECT may be either an RDFNode or a Java object or a scheme
;; string.  If it's an RDFNode, then that is what is used.  If it's a
;; Java object, it is transformed to an RDF literal of the appropriate
;; Datatype, based on its Java type.  If it's a scheme string which
;; starts "http://", then it is transformed into a Resource with that
;; URL; otherwise (it's a scheme string), it's transformed into an RDF
;; literal of string type.
;;
;; If precisely one of SUBJECT, PREDICATE and OBJECT is #f, we return a list
;; of RDFNode objects, one for each of the statements which matches the pattern.
;; If more than one of SUBJECT, PREDICATE and OBJECT is #f, we return a list of Statements.
(define/contract (rdf:select-statements (model     jena-model?)
                                        (subject   (or (not subject)
                                                       (jena-resource? subject)
                                                       (string? subject)
                                                       (is-java-type? subject <uri>)
                                                       (jstring? subject)))
                                        (predicate (or (not predicate)
                                                       (jena-property? predicate)
                                                       (string? predicate)
                                                       (is-java-type? subject <uri>)
                                                       (jstring? predicate)))
                                        (object    (or (not object)
                                                       (jena-rdfnode? object)
                                                       (java-object? object)
                                                       (string? object)))
                                        -> list?)
  (define-generic-java-methods
    list-statements
    create-resource
    create-property
    create-typed-literal
    get-subject
    get-predicate
    get-object
    to-string)
  (define-java-classes
    (<resource> |com.hp.hpl.jena.rdf.model.Resource|)
    (<property> |com.hp.hpl.jena.rdf.model.Property|)
    (<rdf-node> |com.hp.hpl.jena.rdf.model.RDFNode|))
  (let ((accessor ;get accessor for result, checking precisely one #f.
                                        ;Throw error if none, accessor => #f if more than one.
         (cond ((not subject)   (and predicate object
                                     get-subject))
               ((not predicate) (and subject object
                                     get-predicate))
               ((not object)    (and subject predicate
                                     get-object))
               (else
                (error 'rdf:select-statements "Bad call: no #f slot"))))
        (qsubject                      ;subject, as a resource or null
         (cond ((not subject)
                (java-null <resource>))
               ((is-java-type? subject <resource>)
                subject)
               ((is-java-type? subject <uri>)
                (create-resource model (to-string subject)))
               (else                    ;string
                (create-resource model (as-java-string subject)))))
        (qpredicate                  ;predicate, as a property or null
         (cond ((not predicate)
                (java-null <property>))
               ((is-java-type? predicate <property>)
                predicate)
               ((and (string? predicate)
                     (string=? predicate "a"))
                (create-property
                 model
                 (->jstring "http://www.w3.org/1999/02/22-rdf-syntax-ns#type")
                 (->jstring "")))
               ((is-java-type? predicate <uri>)
                (create-property model (to-string predicate)))
               (else                    ; string
                (create-property model (as-java-string predicate)
                                 (->jstring "")))))
        (qobject              ;object, as a resource, literal, or null
         (cond ((not object)            ;#f
                (java-null <rdf-node>))
               ((is-java-type? object <rdf-node>)
                                        ;already a RDFNode
                object)
               ((java-object? object)
                                        ;some other Java obj
                (create-typed-literal model
                                      object))
               ((string-prefix? "http://" object)
                                        ;string naming resource
                (create-resource model
                                 (->jstring object)))
               (else                    ;literal string
                (create-typed-literal model
                                      (->jstring object))))))
    (if accessor
        (map accessor
             (jobject->list (list-statements model qsubject qpredicate qobject)))
        (jobject->list (list-statements model qsubject qpredicate qobject)))))

;; RDF:GET-REASONER : string -> reasoner
;;
;; Return a new Reasoner object,
;; or 'NONE if we are not to use a reasoner,
;; or #f on error.
;;
;; The REASONER-LEVEL parameter is one of the strings 
;; defaultOWL, miniOWL, microOWL, defaultRDFS, simpleRDFS, fullRDFS or transitive
(define (rdf:get-reasoner reasoner-level)

;;   (define (get-dig-reasoner)
;;     (define-java-classes
;;       ;;(<registry> |com.hp.hpl.jena.reasoner.ReasonerRegistry|)
;;       <com.hp.hpl.jena.reasoner.reasoner-registry>
;;       <com.hp.hpl.jena.rdf.model.model-factory>
;;       ;;(<factory> |com.hp.hpl.jena.reasoner.dig.DIGReasonerFactory|)
;;       ;;<com.hp.hpl.jena.rdf.model.resource>
;;       )
;;     (define-generic-java-methods
;;       the-registry
;;       (create-with-owl-axioms |createWithOWLAxioms|)
;;       get-factory
;;       create-resource
;;       create-default-model
;;       add-property)

;;     (let* ((config-model (create-default-model
;;                           (java-null <com.hp.hpl.jena.rdf.model.model-factory>)))
;;            (conf (create-resource config-model)))
;;       (add-property conf
;;                     (java-retrieve-static-object
;;                      '|com.hp.hpl.jena.vocabulary.ReasonerVocabulary.EXT_REASONER_URL|)
;;                     (create-resource config-model
;;                                      (->jstring (dig-uri))))
;;       ;(chatter "Connecting to DIG reasoner at ~a" (dig-uri))
;;       (create-with-owl-axioms
;;        (get-factory
;;         (the-registry (java-null <com.hp.hpl.jena.reasoner.reasoner-registry>))
;;         (java-retrieve-static-object
;;          '|com.hp.hpl.jena.reasoner.dig.DIGReasonerFactory.URI|))
;;        conf)))

  (define get-named-reasoner
    (let ()
      (define-java-classes
        (<registry> |com.hp.hpl.jena.reasoner.ReasonerRegistry|))
      (define-generic-java-methods
        (get-owl-reasoner |getOWLReasoner|)
        (get-owl-mini-reasoner |getOWLMiniReasoner|)
        (get-owl-micro-reasoner |getOWLMicroReasoner|)
        get-transitive-reasoner)
      (let ((reasoner-list (list (cons "defaultOWL"
                                       (lambda ()
                                         (get-owl-reasoner
                                          (java-null <registry>))))
                                 (cons "miniOWL"
                                       (lambda ()
                                         (get-owl-mini-reasoner
                                          (java-null <registry>))))
                                 (cons "microOWL"
                                       (lambda ()
                                         (get-owl-micro-reasoner
                                          (java-null <registry>))))
                                 (cons "defaultRDFS"
                                       (lambda ()
                                         (config-rdfs-reasoner "default")))
                                 (cons "simpleRDFS"
                                       (lambda ()
                                         (config-rdfs-reasoner "simple")))
                                 (cons "fullRDFS"
                                       (lambda ()
                                         (config-rdfs-reasoner "full")))
                                 (cons "transitive"
                                       (lambda ()
                                         (get-transitive-reasoner
                                          (java-null <registry>))))
                                 ;; the reasoner-level "none" is a valid 'reasoner',
                                 ;; but is special-cased below
                                 )))
        (lambda (name)
          (let ((getter (assoc name reasoner-list)))
            (cond ((string=? name "none")
                   'none)
                  ((not getter)
                   #f)                       ;error
                  ((procedure? (cdr getter)) ;not cached yet
                   (chatter "Creating ~a reasoner" name)
                   ;; get a reasoner and cache it
                   (set-cdr! getter ((cdr getter)))
                   (cdr getter))
                  (else                 ;already cached
                   (chatter "Retrieving ~a reasoner" name)
                   (cdr getter))))))))

  (define (config-rdfs-reasoner level)
    (define-java-classes
      (<rdfs-factory> |com.hp.hpl.jena.reasoner.rulesys.RDFSRuleReasonerFactory|)
      (<model-factory> |com.hp.hpl.jena.rdf.model.ModelFactory|)
      (<vocabulary> |com.hp.hpl.jena.vocabulary.ReasonerVocabulary|))
    (define-generic-java-methods
      create-default-model create-resource add-property the-instance create)
    (define-generic-java-field-accessors
      (:prop-set-rdfs-level |PROPsetRDFSLevel|))
    (chatter "Creating an RDFSReasoner with level ~s" level)
    (create (the-instance (java-null <rdfs-factory>))
            (add-property (create-resource
                           (create-default-model (java-null <model-factory>)))
                          (:prop-set-rdfs-level (java-null <vocabulary>))
                          (->jstring level))))

  (get-named-reasoner reasoner-level))

)
