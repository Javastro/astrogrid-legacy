;; SISC library module for Quaestor
;; Interactions with Jena

(import s2j)

(require-library 'sisc/libs/srfi/srfi-1)
(import* srfi-1
         reduce)

(require-library 'quaestor/utils)
(import* utils report-exception format-error-record)

(require-library 'util/lambda-contract)

(module jena
( rdf:new-empty-model
  rdf:ingest-from-stream
  ;rdf:ingest-from-uri
  rdf:merge-models
  ;rdf:language-ok?
  rdf:mime-type->language
  rdf:language->mime-type
  rdf:mime-type-list
  rdf:get-reasoner)

;; heavily used classes
(define-java-classes
  <java.lang.string>
  <java.io.input-stream>
  <java.io.reader>)

;; contract assertions
;;
;; Verifies that a JOBJECT is an instance of a Java class CLASS (which
;; can be a class, a string, or a symbol).  Returns true if so, #f it
;; it's not, or if it's not a Java object at all.
(define (is-java-type? jobject class)
  (define-generic-java-method instance?)
  (and (java-object? jobject)
       (->boolean (instance?
                   (if (java-class? class)
                       class
                       (java-class (if (symbol? class)
                                       class
                                       (string->symbol class))))
                   jobject))))
;; is something a java stream or a java reader
(define (java-input? x)
  (or (is-java-type? x <java.io.input-stream>)
      (is-java-type? x <java.io.reader>)))
(define (jstring? x) ; null objects are also deemed true
  (or (java-null? x)
      (is-java-type? x <java.lang.string>)))
(define (string-or-false? x)
  (or (not x) (string? x)))
;; is something a Jena model?
(define (jena-model? x)
  (is-java-type? x '|com.hp.hpl.jena.rdf.model.Model|))
(define (jena-model-or-false? x)
  (or (not x) (jena-model? x)))

;; Return a new empty model
(define/contract (rdf:new-empty-model -> jena-model?)
  (define-java-class <com.hp.hpl.jena.rdf.model.model-factory>)
  ((generic-java-method '|createDefaultModel|)
   (java-null <com.hp.hpl.jena.rdf.model.model-factory>)))

;; Given a URI, this reads in the RDF within it, and returns the
;; resulting model.  The format of the input file is determined using
;; GET-RDF-LANGUAGE-FROM-EXTENSION.
(define/contract (rdf:ingest-from-uri (uri string?) -> jena-model?)
  (define-generic-java-methods
    read
    open-stream)
  (define-java-classes
    <java.io.file-input-stream>
    <java.lang.string>
    (<URL> |java.net.URL|))
  (let ((model (rdf:new-empty-model))
        (full-uri (->jstring (normalise-uri uri))))
    (ingest-from-stream/language (open-stream (java-new <URL> full-url))
                                 (normalise-uri uri)
                                 (or (get-rdf-language-from-extension uri)
                                     (java-null <java.lang.string>)))))

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
    ("*/*"                 . "RDF/XML")
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
;; one of the strings accepted by RDF:LANGUAGE-OK?.  If the MIME type
;; isn't recognised, return #f.
(define/contract (rdf:mime-type->language (s string?) -> string-or-false?)
  (let ((p (assoc s mime-lang-mappings)))
    (and p (cdr p))))

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
;; default language for the Model read function.  If it is given, it
;; must be one of the mime-types which is handled by RDF:MIME-TYPE->LANGUAGE.
;;
;; If there is a problem reading the stream, or if the mime-type is present
;; and illegal, then throw an error.
(define/contract (rdf:ingest-from-stream (stream java-input?)
                                         (mime-type string-or-false?)
                                         -> jena-model?)
  (let ((language (if mime-type
                      (->jstring
                       (or (rdf:mime-type->language mime-type)
                           (error (format #f "not an RDF MIME type: ~s"
                                          mime-type))))
                      (java-null <java.lang.string>))))
    (ingest-from-stream/language stream
                                 "<stream>"
                                 language)))

;; Implementation of the RDFErrorHandler interface
(define-java-classes
  (<RDFErrorHandler> |com.hp.hpl.jena.rdf.model.RDFErrorHandler|))
(define-java-proxy (rdf-error-handler uri logger)
  (<RDFErrorHandler>)
  (define (error p ex)
    (define-generic-java-method get-message)
    (logger "Error: can't read ~a: ~s" uri (get-message ex)))
  (define (fatal-error p ex)
    (define-generic-java-method get-message)
    (error (format #f "RDF fatal parse error reading ~a: ~a"
                   uri (->string (get-message ex)))))
  (define (warning p ex)
    ;; do nothing -- ought we to report these somewhere?
    #f))

;; This is the function which ingests RDF from a STREAM, which is
;; expected to be in the named LANGUAGE (which may be jnull to
;; indicate the default Jena behaviour).  Any errors reported include
;; the name of the given REFERENCE-URI.
(define/contract (ingest-from-stream/language (stream java-input?)
                                              (reference-uri string?)
                                              (language jstring?)
                                              -> jena-model?)
  (define-generic-java-methods
    read
    get-reader
    set-error-handler)
  (let* ((model (rdf:new-empty-model))
         (reader (and model (get-reader model language))))
    (define logger
      (let ((errlist '()))
        (lambda args
          (if (null? args)
              (reverse errlist)
              (set! errlist
                    (cons (apply format `(#f ,(string-append (car args) "~%")
                                             . ,(cdr args)))
                          errlist))))))
    (or reader
      (error "Failed to get reader!"))

    (set-error-handler reader (rdf-error-handler reference-uri logger))
    ;; should the following be MAKE-FC (and have the extra logic folded 
    ;; in there)
    (with/fc
        ;; (lambda (m e)
;;                (define-java-classes
;;                  (<throwable> |java.lang.reflect.UndeclaredThrowableException|)
;;                  (<exception> |java.lang.Exception|))
;;                (define-generic-java-methods
;;                  get-message
;;                  get-undeclared-throwable
;;                  to-string)
;;                (let ((msg (error-message m)))
;;                  (report-exception 
;;                   'ingest-from-stream
;;                   '|SC_BAD_REQUEST|
;;                   "Error reading ~a (~a)~%Other details:~%~a~%"
;;                   reference-uri
;;                   (cond ((is-java-type? msg <throwable>)
;;                          (get-message
;;                           (get-undeclared-throwable
;;                            msg)))
;;                         ((is-java-type? msg <exception>)
;;                          (to-string msg))
;;                         (else
;;                          msg))
;;                   (apply string-append (logger)))))
        (lambda (m e)
          (report-exception 'ingest-from-stream
                            '|SC_BAD_REQUEST|
                            "Error reading ~a (~a)~%Other details:~%~a~%"
                            reference-uri
                            (format-error-record m)
                            (apply string-append (logger))))
      (lambda ()
        (chatter "rdf:ingest-from-stream ~s (URI=~a, language=~a)"
                 stream reference-uri language)
        (read reader model stream (->jstring ""))))
    model))

;; (define/contract (java-input? string? -> jena-model?)
;;     (rdf:ingest-from-stream stream mime-type)
;;   (define-generic-java-method
;;     read)
;;   (define-java-class <java.lang.string>)
;;   (let ((language             ;set to valid language (jstring or null)
;;          (if mime-type                  ;...or #f
;;              (let ((l (rdf:mime-type->language mime-type)))
;;                (and l (->jstring l)))
;;              (java-null <java.lang.string>))))
;;     (and language
;;          (let ((model (rdf:new-empty-model)))
;;            (read model
;;                  stream
;;                  (->jstring "")  ;base -- let the serialisation handle this
;;                  language)))))

;; Return the object S, which should be either a Java or Scheme string,
;; as a Scheme string.
(define (as-scheme-string s)
  (cond ((string? s)
         s)
        ((java-object? s)               ;should be a jstring
         (->string s))
        (else
         #f)))

;; Return a new Reasoner object, or #f on error
(define (rdf:get-reasoner)

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
        (get-rdfs-reasoner |getRDFSReasoner|)
        get-transitive-reasoner)
      (let ((reasoner-list (list (cons "owl" get-owl-reasoner)
                                 (cons "rdfs" get-rdfs-reasoner)
                                 (cons "transitive" get-transitive-reasoner))))
        (lambda (name)
          (let ((getter (assoc name reasoner-list)))
            (cond ((not getter)
                   #f)                       ;error
                  ((procedure? (cdr getter)) ;not cached yet
                   (chatter "Creating ~a reasoner" name)
                   ;; get a reasoner and cache it
                   (set-cdr! getter ((cdr getter) (java-null <registry>)))
                   (cdr getter))
                  (else                 ;already cached
                   (chatter "Retrieving ~a reasoner" name)
                   (cdr getter))))))))

;;   (define (get-owl-reasoner)
;;     (define-java-classes
;;       (<registry> |com.hp.hpl.jena.reasoner.ReasonerRegistry|))
;;     (define-generic-java-methods
;;       (get-owl-reasoner |getOWLReasoner|))
;;     ;(chatter "Creating OWL reasoner")
;;     (get-owl-reasoner (java-null <registry>)))

;;   (define (get-rdfs-reasoner)
;;     (define-java-class
;;       <com.hp.hpl.jena.reasoner.reasoner-registry>)
;;     (define-generic-java-methods
;;       (get-rdfs-reasoner |getRDFSReasoner|))
;;     ;(chatter "Creating RDFS reasoner")
;;     (get-rdfs-reasoner (java-null <com.hp.hpl.jena.reasoner.reasoner-registry>)))

  (get-named-reasoner "rdfs")
  )

)
