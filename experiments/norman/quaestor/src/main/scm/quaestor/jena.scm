;; SISC library module for Quaestor
;; Interactions with Jena

(import s2j)

(require-library 'sisc/libs/srfi/srfi-1)
(require-library 'sisc/libs/srfi/srfi-13)
(require-library 'sisc/libs/srfi/srfi-26)

(require-library 'quaestor/utils)
(require-library 'org/eurovotech/quaestor/scheme-wrapper-support)

(require-library 'util/lambda-contract)

(module jena
( rdf:new-empty-model
  rdf:create-persistent-model-factory
  rdf:ingest-from-stream/language
  rdf:ingest-from-string/turtle
  rdf:merge-models
  rdf:mime-type->language
  rdf:language->mime-type
  rdf:mime-type-list
  rdf:get-reasoner
  rdf:get-property-on-resource
  rdf:get-properties-on-resource
  rdf:select-statements
  rdf:select-object/string
  rdf:make-quaestor-resource
  rdf:mutate/changeset!
  rdf:->uri)

(import* srfi-1
         fold)
(import* srfi-13
         string-prefix?
         string-index)
(import* srfi-26
         cut)
(import* utils
         is-java-type?
         jobject->list
         java-class-present)
(import* quaestor-support
         chatter
         format-error-record
         report-exception)

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

;; ANDF : list -> boolean
;; Implements 'and' as a function -- returns true if all elements of the list are non-#f
(define (andf l)
  (cond ((null? l)
         #t)
        ((car l)
         (andf (cdr l)))
        (else
         #f)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Start the implementation

;; Return a new empty model, which is not backed by anything
(define/contract (rdf:new-empty-model -> jena-model?)
  (define-java-class <com.hp.hpl.jena.rdf.model.model-factory>)
  ((generic-java-method '|createDefaultModel|)
   (java-null <com.hp.hpl.jena.rdf.model.model-factory>)))

(define (as-jstring s) ; convert a string to a jstring, as long as it's not #f
  (and s (->jstring s)))

;; RDF:CREATE-PERSISTENT-MODEL-FACTORY : model -> procedure-or-false
;; RDF:CREATE-PERSISTENT-MODEL-FACTORY : string -> <java.io.file>
;;
;; Given a Jena model, return a procedure which will create persistent models.
;; The CONFIG model must include a resource which has a string-valued
;; property quaestor:persistenceDirectory.  This names a directory
;; which will be used to hold persistent models, and which will be created 
;; if it does not already exist.  As well as this directory, this procedure
;; will create a file with the same name as the directory, and an ".rdf" suffix,
;; which is the TDB assembly file required for the persistent model.
;;
;; The resulting procedure has the contract
;;   jena-model? -> jena-model?
;; The input model must declare that some resource is a quaestor:PersistentModel,
;; and this is used to name the model.
;; If no resource is so declared, that is an error; if more than one is declared,
;; it is unspecified which one is used.
;;
;; On success, the procedure returns a persistent model which includes any
;; triples previously in the model, and the metadata passed as input.
;;
;; If persistent models cannot be created, because the TDBFactory support is
;; not available, return #f; if they cannot be created because of a configuration error,
;; throw an error (FIXME: is this consistent?)
;;
;; Given a string, return a <java.io.file> which represents a file in the persistence directory,
;; which has this string as its root and the extension ".rdf"
;;
(define/contract (rdf:create-persistent-model-factory (config jena-model?)
                                                      -> (lambda (x) (or (not x) (procedure? x))))
  (define-java-classes <java.io.file>)
  (define-generic-java-methods exists mkdirs to-string)
  (define (java-file? x)
    (is-java-type? x <java.io.file>))

  (define/contract (create-persistent-model (persistence-directory java-file?)
                                            (metadata-model jena-model?) -> jena-model?)
    ;; given a model, create a persistent model which is named after the
    ;; quaetor:PersistentModel resource within it
    (define-java-classes (<tdb-factory> |com.hp.hpl.jena.tdb.TDBFactory|) <java.io.file>)
    (define-generic-java-methods assemble-model replace-all hash-code to-string)

    (define (make-persistence-file directory key)
      ;; <java.io.File> jstring? -> <java.io.File>
      (define-java-classes <java.io.file-output-stream>)
      (define-generic-java-methods write concat close
        create-resource create-property add add-literal)
      (let ((m (rdf:new-empty-model))
            (tfile (java-new <java.io.file> directory (concat key (->jstring ".rdf"))))
            (ja-ns  (lambda (x) (->jstring (string-append "http://jena.hpl.hp.com/2005/11/Assembler#" x))))
            (tdb-ns (lambda (x) (->jstring (string-append "http://jena.hpl.hp.com/2008/tdb#" x))))
            (rdf-ns (lambda (x) (->jstring (string-append "http://www.w3.org/1999/02/22-rdf-syntax-ns#" x))))
            (null-string (->jstring "")))
        (chatter "make-persistence-file: directory=~a  key=~a  tfile=~a"
                 directory key (to-string tfile))
        (let ((r (create-resource m))
              (graphspec (create-resource m))
              (type-property (create-property m (rdf-ns "type") null-string)))
          (add-literal m r
                       (create-property m (ja-ns "loadClass") null-string)
                       (->jstring "com.hp.hpl.jena.tdb.TDB"))
          (add m r
               type-property
               (create-resource m (ja-ns "RDFDataset")))
          (add m r (create-property m (ja-ns "defaultGraph") null-string) graphspec)
          (add m graphspec type-property (create-resource m (tdb-ns "GraphTDB")))
          (add-literal m graphspec
                       (create-property m (tdb-ns "location") null-string)
                       (to-string (java-new <java.io.file> directory key)))
          (add m metadata-model) ;add all the metadata to the saved model
          (let ((fos (java-new <java.io.file-output-stream> tfile)))
            (write m fos)
            (close fos))
          tfile)))

    (let ((persistent-model-names
           (rdf:select-statements metadata-model #f "a" (rdf:make-quaestor-resource "PersistentModel"))))
      (cond ((null? persistent-model-names)
             (error "Can't make persistent model: there is no quaestor:PersistentModel resource in the metadata"))
            (else
             (assemble-model (java-null <tdb-factory>)
                             (to-string (make-persistence-file persistence-directory
                                                               (replace-all (to-string (car persistent-model-names))
                                                                            (->jstring "[^A-Za-z0-9]")
                                                                            (->jstring "-")))))))))
                
  (define/contract (create-persistent-rdf-file (persistence-directory java-file?)
                                               (fileroot string?)
                                               -> java-file?)
    ;; given a string, return a <java.io.file> in the persistence directory, which has extension .rdf
    (java-new <java.io.file> persistence-directory (->jstring (string-append fileroot ".rdf"))))

  (and (java-class-present '|com.hp.hpl.jena.tdb.TDBFactory|)
       (let ((persistence-directory
              (cond ((as-jstring
                      (rdf:select-object/string config #f (rdf:make-quaestor-resource "persistenceDirectory")))
                     => (lambda (dirname) (java-new <java.io.file> dirname)))
                    (else #f))))
         ;; (attempt to) make the persistence-directory if it doesn't already exist
         (if (and persistence-directory
                  (not (->boolean (exists persistence-directory))))
             (mkdirs persistence-directory))

         (cond ((and persistence-directory
                     (->boolean (exists persistence-directory)))
                (lambda (arg)
                  (cond ((string? arg)
                         (create-persistent-rdf-file persistence-directory arg))
                        ((jena-model? arg)
                         (create-persistent-model persistence-directory arg))
                        (else
                         (error "Bad call to persistence factory with argument ~s" arg)))))

               (persistence-directory
                (error "Can't make persistent model handler: the persistence-directory ~s does not exist and cannot be created" (->string (to-string persistence-directory))))
               (else
                (error "Can't make persistent model handler: the persistence info does not indicate a quaestor:persistenceDirectory"))))))

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
;; "N-TRIPLE" and "TURTLE".  Returns #f if it is not a legal language.
;;  (define (rdf:language-ok? lang)
;;    (or (not lang)
;;        (member (as-scheme-string lang)
;;                '("RDF/XML" "RDF/XML-ABBREV" "N-TRIPLE" "TURTLE"))))
;;
;; Note: as of at least Jena 2.5.6, possibly earlier, the N3 parser
;; has been deprecated in favour of the TURTLE one, and will
;; eventually be removed.  When we are asked for Notation3, below, we
;; will actually return Turtle (which is equivalent, as far as RDF
;; serialisation is concerned).

;; The set of mappings from MIME types to RDF languages.
;; Used in both directions.
(define mime-lang-mappings
  '(;; The 'default' type is the first one in this list

    ;; See http://www.w3.org/TR/rdf-syntax-grammar/#section-MIME-Type
    ;; Generic RDF MIME type: http://www.ietf.org/rfc/rfc3870.txt
    ("application/rdf+xml" . "RDF/XML")

    ;; ...http://www.w3.org/DesignIssues/Notation3
    ;; (and there's apparently an IANA registration pending)
    ("text/rdf+n3"         . "TURTLE")

    ;; ...http://infomesh.net/2002/notation3/#mimetype
    ;; (but deprecated in the Notation3 page above)
    ("application/n3"      . "TURTLE")

    ;;MIME type for Turtle http://www.dajobe.org/2004/01/turtle/
    ("application/x-turtle" . "TURTLE")

    ;;Now preferred to application/x-turtle
    ;; see http://www.w3.org/TeamSubmission/turtle/#sec-mediaReg
    ("text/turtle"         . "TURTLE")

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
  (cond ((not s)
         (cdar mime-lang-mappings))
        ((assoc (cond ((string-index s #\;)
                       => (cut substring s 0 <>))
                      (else
                       s))
                mime-lang-mappings)
         => cdr)
        (else #f)))

;; RDF:LANGUAGE->MIME-TYPE : string -> string-or-false
;;
;; Map RDF language to MIME type.  This is the inverse of
;; RDF:MIME-TYPE->LANGUAGE.  Return #f if LANG is not a legal language.
;; If LANG is #f, return the MIME type of the default language.
(define/contract (rdf:language->mime-type (lang string-or-false?) -> string-or-false?)
  (if (not lang)
      (caar mime-lang-mappings)
      (let loop ((l mime-lang-mappings))
        (cond ((null? l)
               #f)
              ((string=? lang (cdar l))
               (caar l))
              (else
               (loop (cdr l)))))))

;; Return a list of allowed mime-types.  This does not include "*/*",
;; however the first element of this list will be the same as the default type.
(define (rdf:mime-type-list)
  (map car mime-lang-mappings))

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
    (error 'rdf-error-handler
           (format #f "Error: can't read ~a: ~a"
                   uri (->string (get-message ex)))))
  (define (fatal-error p ex)
    (define-generic-java-method get-message)
    (error 'rdf-error-handler
           (format #f "RDF fatal parse error reading ~a: ~a"
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
;; This procedure either succeeds, or throws an exception using REPORT-EXCEPTION (of the 
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
            (chatter "rdf:ingest-from-stream/language: error reading ~a~%~a~%~a"
                     (as-scheme-string base-uri)
                     (format-error-record m)
                     logger-msgs)
            (close stream)              ;might help...
            (report-exception 'rdf:ingest-from-stream/language
                              (or exception
                                  '|SC_BAD_REQUEST|)
                              "Error reading ~a~%Error: ~a~%Logger:~a"
                              (as-scheme-string base-uri)
                              (format-error-record m)
                              logger-msgs)))
      (lambda ()
        (chatter "rdf:ingest-from-stream/language: base=~a language=~s -> ~s"
                 (as-scheme-string base-uri)
                 language ser-lang)
        (let ((reader (and model (get-reader model ser-lang))))
          (or reader
              (error 'rdf:ingest-from-stream/language "Failed to get reader!"))
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

;; RDF:INGEST-FROM-STRING/TURTLE : string uri? -> model
;;
;; Convenience method, which takes a string containing Notation3,
;; and ingests it.  The language is fixed as TURTLE, and the base URI
;; is either the given URI, or "".
;;
;; Either succeeds, or throws an exception.
(define (rdf:ingest-from-string/turtle string . opt-base-uri)
  (rdf:ingest-from-string/turtle* string
                              (if (null? opt-base-uri)
                                  (->jstring "")
                                  (as-java-string (car opt-base-uri)))))
(define/contract (rdf:ingest-from-string/turtle* (string string?) (base jstring?)
                  -> jena-model?)
  (define-java-class <java.io.string-reader>)
  (rdf:ingest-from-stream/language (java-new <java.io.string-reader>
                                             (->jstring string))
                                   base
                                   (->jstring "TURTLE")))

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
;; but returning #f if there are no matches.
(define/contract (rdf:select-object/string (model jena-model?)
                                           (subject   (or (not subject)
                                                          (jena-resource? subject)
                                                          (string? subject)
                                                          (jstring? subject)))
                                           (predicate (or (jena-property? predicate)
                                                          (string? predicate)
                                                          (jstring? predicate)))
                                           -> (lambda (x)
                                                (or (not x)
                                                    (string? x))))
  (define-generic-java-methods to-string get-object)
  (let ((result-nodes (rdf:select-statements model subject predicate #f)))
    (if subject
        (and (not (null? result-nodes))
             (->string (to-string (car result-nodes))))
        (and (not (null? result-nodes)) ;result-nodes is a list of <Statement>
             (->string (to-string (get-object (car result-nodes))))))))

;; RDF:SELECT-STATEMENTS : model subject predicate object -> list
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
;; If none of SUBJECT, PREDICATE or OBJECT is #f, we return a list which is non-null if the
;; corresponding statement appears in the model.
;; If precisely one of SUBJECT, PREDICATE and OBJECT is #f, we return a list
;; of RDFNode objects corresponding to the slot indicated with #f,
;; one for each of the statements which matches the pattern.
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
                                                       (is-java-type? predicate <uri>)
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
  (let ((accessor ;get accessor for result:
         ; no #f: accessor <= 'test-only
         ; precisely one #f: accessor <= procedure
         ; more than one #f: accessor <= #f
         (cond ((not subject)   (and predicate object
                                     get-subject))
               ((not predicate) (and subject object
                                     get-predicate))
               ((not object)    (and subject predicate
                                     get-object))
               (else
                'test-only)))
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
                (create-property model
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
               ((is-java-type? object <rdf-node>) ;already a RDFNode
                object)
               ((java-object? object)   ;some other Java obj
                (create-typed-literal model  object))
               ((string-prefix? "http://" object) ;string naming resource
                (create-resource model
                                 (->jstring object)))
               (else                    ;literal string
                (create-typed-literal model
                                      (->jstring object))))))
    (cond ((eq? accessor 'test-only)
           (jobject->list (list-statements model qsubject qpredicate qobject)))
          (accessor
           (map accessor
                (jobject->list (list-statements model qsubject qpredicate qobject))))
          (else
           (jobject->list (list-statements model qsubject qpredicate qobject))))))

;; RDF:MAKE-QUAESTOR-RESOURCE : string -> string
;; Given a fragment, return it prefixed by the Quaestor namespace
(define/contract (rdf:make-quaestor-resource (fragment string?) -> string?)
  (string-append "http://ns.eurovotech.org/quaestor#" fragment))

;; RDF:GET-REASONER : string -> reasoner
;; RDF:GET-REASONER : <model> -> reasoner
;; RDF:GET-REASONER : symbol -> list-of-string
;;
;; With a string argument, return a new Reasoner object indicated with the given name,
;; or #f if we are not to use a reasoner (if the reasoner-name is given as "none"),
;; and throw an error if the reasoner is not recognised.
;;
;; With a <model> parameter, find any object in the model which matches
;; ?s quaestor:requiredReasoner [ quaestor:level ?o ]
;; and return a Reasoner object indicated by the string ?o.
;; Return #f (no reasoner) if there are no such objects,
;; and throw an error if the string ?o is not a recognised reasoner.
;;
;; With a symbol argument, if the symbol is 'reasoner-list,
;; return the list of reasoner names as a list of strings.
;;
;; The names of the reasoners are the strings 
;; defaultOWL, miniOWL, microOWL, defaultRDFS, simpleRDFS, fullRDFS, transitive
;; or none
(define (rdf:get-reasoner reasoner-spec)

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
      (let ((reasoner-list `(("defaultOWL" .  ,(lambda () (get-owl-reasoner (java-null <registry>))))
                             ("miniOWL" .     ,(lambda () (get-owl-mini-reasoner (java-null <registry>))))
                             ("microOWL" .    ,(lambda () (get-owl-micro-reasoner (java-null <registry>))))
                             ("defaultRDFS" . ,(lambda () (config-rdfs-reasoner "default")))
                             ("simpleRDFS" .  ,(lambda () (config-rdfs-reasoner "simple")))
                             ("fullRDFS" .    ,(lambda () (config-rdfs-reasoner "full")))
                             ("transitive" .  ,(lambda () (get-transitive-reasoner (java-null <registry>))))
                             ;; the reasoner-level "none" is a valid 'reasoner',
                             ;; but is special-cased below
                             )))
        (lambda (name)
          (let ((getter (assoc name reasoner-list)))
            (cond ((and (symbol? name)
                        (eqv? name 'reasoner-list))
                   (map car reasoner-list))
                  ((string=? name "none")
                   #f)
                  ((not getter)
                   (error "I don't recognise that reasoner name: valid ones are ~s" (map car reasoner-list)))
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

  (cond ((string? reasoner-spec)
         (get-named-reasoner reasoner-spec))
        ((jena-model? reasoner-spec)
         (let ((levelres (rdf:select-statements reasoner-spec
                                                #f
                                                (rdf:make-quaestor-resource "requiredReasoner")
                                                #f)))
           (define-generic-java-methods get-object to-string)
           (cond ((null? levelres)
                  (get-named-reasoner "none"))
                 ((rdf:get-property-on-resource (get-object (car levelres))
                                                (rdf:make-quaestor-resource "level"))
                  => (lambda (level)
                       (get-named-reasoner (->string (to-string level)))))
                 (else
                  (get-named-reasoner "none")))))
        ((and (symbol? reasoner-spec)
              (eqv? reasoner-spec 'reasoner-list))
         (get-named-reasoner 'reasoner-list))
        (else
         #f)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Apply Talis changeset.
;;
;; See <http://n2.talis.com/wiki/ChangeSets> for spec.
;; We don't implement everything here:
;;
;; * The Talis Changeset Protocol <http://n2.talis.com/wiki/Changeset_Protocol>
;;   says that there is no checking that the subjectOfChange is indeed the
;;   subject of each of the addition/removal statement; we do indeed check this
;;   and throw an error if it's not so
;; 
;; * The protocol permits multiple ChangeSet objects in a POST; we don't, for now.

;; RDF:MUTATE/CHANGESET! : model model -> model
;; Apply the CHANGESET, which is an RDF model conforming to <http://n2.talis.com/wiki/ChangeSets>,
;; to the statements in the MODEL.  Return the (changed) input model, or throw an 
;; error on any problems.
;;
;; We only look at the cs:subjectOfChange, cs:addition and cs:removal properties, 
;; and we do so on any single object which is of type cs:ChangeSet.
(define/contract (rdf:mutate/changeset! (model jena-model?) (changeset jena-model?) -> jena-model?)
  (define-generic-java-methods as to-string get-statement get-subject equals add remove contains)
  (define-java-classes
    (<reified-statement> |com.hp.hpl.jena.rdf.model.ReifiedStatement|)
    (<statement> |com.hp.hpl.jena.rdf.model.Statement|))
  (define (cs-ns x)
    (string-append "http://purl.org/vocab/changeset/schema#" x))
  (define (reified->stmt reified-statement-node)
    (with/fc (lambda (m e)
               (error "object ~a is not a Reified Statment" (->string (to-string reified-statement-node))))
      (lambda ()
        (get-statement (as reified-statement-node <reified-statement>)))))
  (let ((changeset-list (rdf:select-statements changeset #f "a" (cs-ns "ChangeSet"))))
    (cond ((and (= (length changeset-list) 1)
                (car changeset-list))
           => (lambda (cs)
                (let ((subject-of-change (rdf:get-property-on-resource cs (cs-ns "subjectOfChange")))
                      (removals (map reified->stmt (rdf:get-properties-on-resource cs (cs-ns "removal"))))
                      (additions (map reified->stmt (rdf:get-properties-on-resource cs (cs-ns "addition")))))
                  (cond ((not subject-of-change)
                         (error "rdf:mutate/changeset!: changeset has no subjectOfChange"))
                        ((not (andf (map (lambda (stmt)
                                           (->boolean (equals (get-subject stmt) subject-of-change)))
                                         (append removals additions))))
                         (error "rdf:mutate/changeset!: all addition/removal statements must have subjectOfChange as their subject"))
                        ((not (andf (map (lambda (stmt)
                                           (->boolean (contains model stmt)))
                                         removals)))
                         (error "rdf:mutate/changeset!: all removal statements must be initially present in the model"))
                        (else
                         ;; normal case
                         (if (not (null? removals))
                             (remove model (->jarray removals <statement>)))
                         (if (not (null? additions))
                             (add model (->jarray additions <statement>)))
                         model)))))
          (else
           (error "rdf:mutate/changeset!: changeset must contain exactly one cs:ChangeSet")))))

;; RDF:->URI : any -> <uri> or #f
;; Given one of a range of things which has a URI representation, return the URI.
;; If the object does not have a URI, return #f
(define (rdf:->uri obj)
  (define-java-classes <java.io.file>)
  (define-generic-java-methods (get-uri |getURI|) (to-uri |toURI|) (is-uri |isURI|) as-node)
  (cond ((string? obj)
         (java-new <uri> (->jstring obj)))
        ((is-java-type? obj <java.lang.string>)
         (java-new <uri> obj))
        ((is-java-type? obj <uri>)
         obj)
        ((is-java-type? obj <java.io.file>)
         (to-uri obj))
        ((or (is-java-type? obj <com.hp.hpl.jena.rdf.model.resource>)
             (is-java-type? obj <com.hp.hpl.jena.rdf.model.property>))
         (java-new <uri> (get-uri obj)))
        ((is-java-type? obj <rdfnode>)
         (let ((node (as-node obj)))
           (and (->boolean (is-uri node))
                (java-new <uri> (get-uri node)))))))
  

)
