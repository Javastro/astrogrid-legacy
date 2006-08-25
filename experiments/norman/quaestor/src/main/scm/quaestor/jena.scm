;; SISC library module for Quaestor
;; Interactions with Jena

(import s2j)

(require-library 'sisc/libs/srfi/srfi-1)
(import* srfi-1
         reduce)
(require-library 'sisc/libs/srfi/srfi-26)
(import* srfi-26
         cut cute)

(require-library 'quaestor/utils)
(import* utils
         report-exception
         format-error-record
         is-java-type?
         iterator->list)

(require-library 'util/lambda-contract)

(module jena
( rdf:new-empty-model
  rdf:ingest-from-stream
  rdf:ingest-from-stream/language
  ;rdf:ingest-from-uri
  rdf:merge-models
  ;rdf:language-ok?
  rdf:mime-type->language
  rdf:language->mime-type
  rdf:mime-type-list
  rdf:get-reasoner
  rdf:property-on-resource
  rdf:properties-on-resource
  rdf:select-statements)

;; heavily used classes
(define-java-classes
  <java.lang.string>
  <java.io.input-stream>
  <java.io.reader>
  <com.hp.hpl.jena.rdf.model.resource>
  <com.hp.hpl.jena.rdf.model.property>
  (<rdfnode> |com.hp.hpl.jena.rdf.model.RDFNode|))

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

;; Return a new empty model
(define/contract (rdf:new-empty-model -> jena-model?)
  (define-java-class <com.hp.hpl.jena.rdf.model.model-factory>)
  ((generic-java-method '|createDefaultModel|)
   (java-null <com.hp.hpl.jena.rdf.model.model-factory>)))

;; Given a URI, this reads in the RDF within it, and returns the
;; resulting model.
;;
;; UNTESTED
(define/contract (rdf:ingest-from-uri (uri string?) -> jena-model?)
  (define-generic-java-methods
    read
    open-connection
    get-input-stream
    get-content-type)
  (define-java-classes
    <java.io.file-input-stream>
    <java.lang.string>
    (<URL> |java.net.URL|))
  (let* ((full-uri (normalise-uri uri))
         (conn (open-connection (java-new <URL> (->jstring full-uri)))))
    (rdf:ingest-from-stream/language (get-input-stream conn)
                                     full-uri
                                     (get-content-type conn))))

;; Given a list of RDF models, merge them into a single one, and return it
(define/contract (rdf:merge-models (models list?) -> jena-model?)
  (define-generic-java-method add)
  (reduce (lambda (new result)
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
    ("*/*"                 . "N3")      ;Notation3 is the default type
    ("text/rdf+n3"         . "N3")
    ;; ...http://www.w3.org/DesignIssues/Notation3
    ;; (and there's apparently an IANA registration pending)
    ("application/n3"      . "N3")
    ;; ...http://infomesh.net/2002/notation3/#mimetype
    ;; (but deprecated in the Notation3 page above)
    ("application/rdf+xml" . "RDF/XML")
    ;; ...http://www.w3.org/TR/rdf-syntax-grammar/#section-MIME-Type
    ;; Generic RDF MIME type: http://www.ietf.org/rfc/rfc3870.txt
    ("text/plain"          . "N-TRIPLE")
    ;; ...http://www.dajobe.org/2001/06/ntriples/
    ))

;; Given a MIME type (a non-null string), return an RDF language, as
;; one of the strings accepted by RDF:LANGUAGE-OK?.
;; As a convenience, the `string' may be passed as #f,
;; to retrieve the default language.
;; If the MIME type isn't recognised, return #f.
(define/contract (rdf:mime-type->language (s string-or-false?)
                                          -> string-or-false?)
  (if s
      (let ((p (assoc s mime-lang-mappings)))
        (and p (cdr p)))
      (rdf:mime-type->language "*/*")))

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

;; rdf:ingest-from-stream java-stream string -> java-model
;; rdf:ingest-from-stream java-reader string -> java-model
;;
;; Given a Java STREAM or Java Reader, this reads in the RDF within it,
;; and returns the resulting model.
;; MIME-TYPE may be #f, in which case we use the
;; default language (as for */* accept headers).  If it is given, it
;; must be one of the mime-types which is handled by RDF:MIME-TYPE->LANGUAGE.
;;
;; If there is a problem reading the stream, or if the mime-type is present
;; and illegal, then throw an error.
(define/contract (rdf:ingest-from-stream (stream java-input?)
                                         (mime-type string-or-false?)
                                         -> jena-model?)
  (let ((language (if mime-type
                      (or (rdf:mime-type->language mime-type)
                          (error (format #f "rdf:ingest-from-stream not an RDF MIME type: ~s"
                                         mime-type)))
                      (rdf:mime-type->language #f)))) ;use default
    (rdf:ingest-from-stream/language stream "" language)))

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

;; RDF:INGEST-FROM-STREAM/LANGUAGE jinput-stream string (j)string -> model
;; RDF:INGEST-FROM-STREAM/LANGUAGE jreader       string (j)string -> model
;;
;; This is the function which ingests RDF from an InputStream or Reader,
;; which is expected to be in the named LANGUAGE.  The RDF is read using the
;; given BASE-URI, and any errors reported include this name.
;;
;; The LANGUAGE may be a java or scheme string representing a content-type
;; or (if it is not a valid content-type) a Jena language.  The LANGUAGE
;; may not be null (ie, we don't fall back on the default Jena behaviour).
(define/contract (rdf:ingest-from-stream/language
                  (stream java-input?)
                  (base-uri string?)
                  (language (or (jstring? language) (string? language)))
                  -> jena-model?)
  (define-generic-java-methods
    read
    get-reader
    set-error-handler)
  (define logger                        ;collects warnings from rdf-error-handler
    (let ((errlist '()))
      (lambda args
        (if (null? args)
            (reverse errlist)
            (set! errlist
                  (cons (apply format `(#f ,(string-append (car args) "~%")
                                           . ,(cdr args)))
                        errlist))))))
  (let* ((ser-lang (cond ((and (string? language)
                               (rdf:mime-type->language language))
                          => ->jstring)
                         ((string? language)
                          (->jstring language))
                         ((rdf:mime-type->language (->string language))
                          => ->jstring)
                         (else
                          language)))
         (model (rdf:new-empty-model))
         (reader (and model (get-reader model ser-lang))))

    (or reader
        (error "Failed to get reader!"))

    (set-error-handler reader (rdf-error-handler base-uri logger))

    ;; should the following be MAKE-FC (and have the extra logic folded 
    ;; in there)
    (with/fc
        (lambda (m e)
          (report-exception 'ingest-from-stream
                            '|SC_BAD_REQUEST|
                            "Error reading ~a (~a)~%~a"
                            base-uri
                            (format-error-record m)
                            (if (null? (logger))
                                ""
                                (format #f "Other warnings:~%~a~%"
                                        (apply string-append (logger))))))
      (lambda ()
        (read reader model stream (->jstring base-uri))))
    ;; we might as well add any logger warnings to the (chatter)
    (chatter (apply string-append (cons "Logger warnings: " (logger))))
    model))

;; Return the object S, which should be either a Java or Scheme string,
;; as a Scheme string.
(define (as-scheme-string s)
  (cond ((string? s)
         s)
        ((java-object? s)               ;should be a jstring
         (->string s))
        (else
         #f)))

;; RDF:PROPERTY-ON-RESOURCE resource property-or-string -> rdfnode-or-false
;;
;; Return a single object (RDFNode), or #f if there is no such property
;; Equivalent to (car (RDF:PROPERTIES-ON-RESOURCE resource property)),
;; except that it returns #f if the list is null
(define (rdf:property-on-resource resource property)
    (let ((l (rdf:properties-on-resource resource property)))
      (if (null? l)
          #f
          (car l))))

;; RDF:PROPERTIES-ON-RESOURCE resource property-or-string -> list-of-objects
;;
;; Return list of objects corresponding to the given property, on the given
;; resource.  The property may be a Java Property or a (scheme) string.
(define/contract (rdf:properties-on-resource (resource jena-resource?)
                                             (property (or (string? property)
                                                           (jena-property? property)))
                                             -> list?)
  (define-generic-java-methods
    get-model list-properties create-property get-object)
  (let ((model (get-model resource)))
    (map get-object
         (iterator->list
          (list-properties
           resource
           (if (jena-property? property)
               property
               (create-property model (->jstring property))))))))

;; RDF:SELECT-STATEMENTS model subject predicate object -> list-of-rdfnode
;;
;; Query a model, matching the specified patterns.
;;
;; SUBJECT and PREDICATE may be scheme strings, or Java objects of type
;; Resource and Property respectively.  In the latter cases, they are
;; transformed into the appropriate Java objects.
;;
;; OBJECT may be either an RDFNode or a _Java_ object.  In the latter case,
;; it is transformed to an RDF literal of the appropriate Datatype, based on
;; its Java type.
;;
;; Precisely one of SUBJECT, PREDICATE and OBJECT must be #f: we return a list
;; of RDFNode objects, one for each of the statements which matches the pattern.
(define/contract (rdf:select-statements (model     jena-model?)
                                        (subject   (or (not subject)
                                                       (jena-resource? subject)
                                                       (string? subject)))
                                        (predicate (or (not predicate)
                                                       (jena-property? predicate)
                                                       (string? predicate)))
                                        (object    (or (not object)
                                                       (jena-rdfnode? object)
                                                       (java-object? object)))
                                        -> list?)
    (define-generic-java-methods
      list-statements
      create-resource
      create-property
      create-typed-literal
      get-subject
      get-predicate
      get-object)
    (define (jobj-or-null x class constructor)
      (cond ((is-java-type? x class)
             x)
            (x
             (constructor x))
            (else                       ;#f
             (java-null class))))
    (define-java-classes
      <com.hp.hpl.jena.rdf.model.resource>
      <com.hp.hpl.jena.rdf.model.property>
      (<rdf-node> |com.hp.hpl.jena.rdf.model.RDFNode|))
    (let ((accessor (cond ((not subject)   get-subject)
                          ((not predicate) get-predicate)
                          ((not object)    get-object)
                          (else
                           (error "Bad call to rdf:select-statements")))))
      (map accessor
           (iterator->list
            (list-statements model
                             (jobj-or-null subject
                                           <com.hp.hpl.jena.rdf.model.resource>
                                           (lambda (subj)
                                             (create-resource model
                                                              (->jstring subj))))
                             (jobj-or-null predicate
                                           <com.hp.hpl.jena.rdf.model.property>
                                           (lambda (pred)
                                             (create-property model
                                                              (->jstring pred)
                                                              (->jstring ""))))
                             (jobj-or-null object ;presume it's a literal
                                           <rdf-node>
                                           (cut create-typed-literal
                                                model <>)))))))

;; Return a new Reasoner object, or #f on error
;; The optional KEY parameter is one of the strings transitive,
;; simpleRDFS, defaultRDFS, 
;; fullRDFS, defaultOWL=fullOWL (not yet miniOWL or microOWL).
(define (rdf:get-reasoner . key)

  (define (get-dig-reasoner)
    (define-java-classes
      ;;(<registry> |com.hp.hpl.jena.reasoner.ReasonerRegistry|)
      <com.hp.hpl.jena.reasoner.reasoner-registry>
      <com.hp.hpl.jena.rdf.model.model-factory>
      ;;(<factory> |com.hp.hpl.jena.reasoner.dig.DIGReasonerFactory|)
      ;;<com.hp.hpl.jena.rdf.model.resource>
      )
    (define-generic-java-methods
      the-registry
      (create-with-owl-axioms |createWithOWLAxioms|)
      get-factory
      create-resource
      create-default-model
      add-property)

    (let* ((config-model (create-default-model
                          (java-null <com.hp.hpl.jena.rdf.model.model-factory>)))
           (conf (create-resource config-model)))
      (add-property conf
                    (java-retrieve-static-object
                     '|com.hp.hpl.jena.vocabulary.ReasonerVocabulary.EXT_REASONER_URL|)
                    (create-resource config-model
                                     (->jstring (dig-uri))))
      ;(chatter "Connecting to DIG reasoner at ~a" (dig-uri))
      (create-with-owl-axioms
       (get-factory
        (the-registry (java-null <com.hp.hpl.jena.reasoner.reasoner-registry>))
        (java-retrieve-static-object
         '|com.hp.hpl.jena.reasoner.dig.DIGReasonerFactory.URI|))
       conf)))

  (define get-named-reasoner
    (let ()
      (define-java-classes
        (<registry> |com.hp.hpl.jena.reasoner.ReasonerRegistry|))
      (define-generic-java-methods
        (get-owl-reasoner |getOWLReasoner|)
        ;;(get-rdfs-reasoner |getRDFSReasoner|)
        get-transitive-reasoner)
      (let ((reasoner-list (list (cons "defaultOWL"
                                       (lambda ()
                                         (get-owl-reasoner
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
                                          (java-null <registry>)))))))
        (lambda (name)
          (let ((getter (assoc name reasoner-list)))
            (cond ((not getter)
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

;;   (define (get-rdfs-reasoner)
;;     (define-java-class
;;       <com.hp.hpl.jena.reasoner.reasoner-registry>)
;;     (define-generic-java-methods
;;       (get-rdfs-reasoner |getRDFSReasoner|))
;;     ;(chatter "Creating RDFS reasoner")
;;     (get-rdfs-reasoner (java-null <com.hp.hpl.jena.reasoner.reasoner-registry>)))

  (get-named-reasoner (if (or (null? key)
                              (not (car key)))
                          "defaultOWL"
                          (car key))))

)
