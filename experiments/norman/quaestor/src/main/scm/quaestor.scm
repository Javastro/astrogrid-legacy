;; SISC program to create a Scheme server which acts as a general-purpose,
;; adaptable, SPARQL endpoint.
;;
;; See http://www.ietf.org/rfc/rfc2616.txt

(import s2j)
(import debugging)                      ;for print-stack-trace
;(emit-annotations #t)                   ;should be default, yes?
;(emit-debugging-symbols #t)

(import string-io)                      ;for string ports

(require-library 'quaestor/utils)
(import utils)
(require-library 'quaestor/knowledgebase)
(import knowledgebase)
(require-library 'quaestor/jena)
(import jena)
(require-library 'quaestor/sparql)
(import sparql)
(require-library 'util/xmlrpc)
(import xmlrpc)
(require-library 'util/sexp-xml)
(import* sexp-xml
         sexp-xml:xml->sexp/reader
         sexp-xml:escape-string-for-xml)
(require-library 'util/lambda-contract)

(require-library 'sisc/libs/srfi/srfi-13)
(import* srfi-13
         string-index)

(define (ident)
  (define-java-classes <sisc.util.version>)
  (define-generic-java-field-accessor :version |VERSION|)
  (define (get-sdb-version)
    (and (java-class-present '|com.hp.hol.jena.sdb.SDB|)
         (->string (:version (java-null (java-class '|com.hp.hpl.jena.sdb.SDB|))))))
  (define (get-tdb-version)
    (and (java-class-present '|com.hp.hol.jena.tdb.TDB|)
         (->string (:version (java-null (java-class '|com.hp.hpl.jena.tdb.TDB|))))))
  `((quaestor.version . "@VERSION@")
    (sisc.version . ,(->string (:version (java-null <sisc.util.version>))))
    (sdb.version . ,(get-sdb-version))
    (tdb.version . ,(get-tdb-version))
    (string
     . "quaestor.scm @VERSION@ ($Revision: 1.57 $ $Date: 2008/09/28 22:35:45 $)")))

;; Predicates for contracts
(define-java-classes
  <java.lang.string>
  (<uri> |java.net.URI|))
(define (request? x)
  (define-java-class <javax.servlet.servlet-request>)
  (is-java-type? x <javax.servlet.servlet-request>))
(define (response? x)
  (define-java-class <javax.servlet.servlet-response>)
  (is-java-type? x <javax.servlet.servlet-response>))
(define (list-or-false? x)
  (or (not x)
      (list? x)))
(define (string-or-false? x)
  (or (not x)
      (string? x)))

(define-syntax receive
  (syntax-rules ()
    ((_ formals expression body ...)
     (call-with-values (lambda () expression)
       (lambda formals
         body ...)))))

;; The PERSISTENCE-FACTORY is used to generate persistent models.
;; If available, this will be set up in hte initialiser.
(define persistence-factory #f)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Implementation

;; INITIALISE-QUAESTOR : <QuaestorServlet> -> void
;; Initialise Quaestor, with a servlet object.  We require this object
;; to implement interface QuaestorServlet
(define (initialise-quaestor scheme-servlet)
  (define-generic-java-methods
    register-handler
    get-init-parameter)
  (define (get-param s)                 ;get parameter, or #f if java-null
    (let ((val (get-init-parameter scheme-servlet (->jstring s))))
      (and (not (java-null? val))
           val)))

  (let ((persistence-directory (get-param "persistence-directory")))
    (if persistence-directory
        (set! persistence-factory
              (rdf:create-persistent-model-factory
               (rdf:ingest-from-string/turtle
                (format #f "[] <~a> \"~a\"."
                        (rdf:make-quaestor-resource "persistenceDirectory")
                        (->string persistence-directory)))))))

  (let ((kb (get-param "kb-context"))
        (xmlrpc (get-param "xmlrpc-context"))
        (pickup (get-param "pickup-context"))
        (config (get-param "config-context")))
    (define (reg method context proc)
      (register-handler scheme-servlet
                        (->jstring method)
                        context
                        (java-wrap (lambda (request response)
                                     (service-http-call request response proc)))))
    (if (not (and kb xmlrpc pickup))
        (let ()
          (define-java-class <javax.servlet.servlet-exception>)
          (error (java-new <javax.servlet.servlet-exception>
                           (->jstring (format #f "initialise-quaestor: some parameters not available: kb=~s, xmlrpc=~s, pickup=~s"
                                              kb xmlrpc pickup))))))

    (reg "GET" kb http-get)
    (reg "GET" (->jstring "/debug") http-debug-query)
    (reg "HEAD" kb http-head)
    (reg "PUT" kb http-put)
    (reg "POST" kb http-post)
    (reg "DELETE" kb http-delete)
    (reg "POST" xmlrpc xmlrpc-handler)
    (reg "GET" pickup pickup-dispatcher)
    (reg "GET" config config-handler)

    ;; initialise the logging, by passing the Servlet to (CHATTER),
    ;; which relies on this object having a log(String) method on it.
    (let ((verbosity (get-param "logging")))
      (if (and verbosity
               (equal? (->string verbosity) "verbose"))
          (chatter scheme-servlet)))
    (chatter "Initialised quaestor")))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; GET support

;; Handle a single GET request.  This examines the path-list from the
;; request, and works through a list of handlers, until one of them returns
;; a list.
(define/contract (http-get (request request?) -> list?)
  (let ((path-list (request->path-list request))
        (query-string (request->query-string request)))
    (cons '|SC_OK|                      ;default response
          (let loop ((h get-handlers))
            (if (null? h)
                (error 'get "No applicable GET handlers found!")
                (or ((car h) path-list query-string request)
                    (loop (cdr h))))))))

;; GET-HANDLERS is a list of procedures which take three arguments:
;;     a list of strings representing the path-info
;;     a query-string
;;     a HTTP Request
;; Each procedure should decide whether it can handle this query, and if it
;; does, it should do so and return a list as required by service-http-request.
;;
;; That is, each should abide by the contract
;; (define/contract (handler (path-info-list list?)
;;                           (query-string   string-or-false?)
;;                           (request        request?)
;;                           -> list-or-false?)
;;   ...)

;; Fallback get handler.  Matches everything, and returns a string.
(define/contract (get-fallback (path-info-list list?)
                               (query-string string-or-false?)
                               (request request?)
                               -> list-or-false?)
  (list '|SC_OK|
        (response-page request 
                       "Quaestor"
                       `((p "Debugging the query.")
                         (p "The details of the request follow:")
                         ,@(tabulate-request-information request)))))

;; Display an HTML page reporting on the knowledgebases available
(define/contract (get-knowledgebase-list (path-info-list list?)
                                         (query-string string-or-false?)
                                         (request request?)
                                         -> list-or-false?)
  (define (display-kb-info kb-name)
    (define-generic-java-methods write to-string)
    (define (model-to-string model)
      (define-java-class <java.io.string-writer>)
      (let ((sw (java-new <java.io.string-writer>)))
        (write model sw (->jstring "TURTLE"))
        (->string (to-string sw))))

    (let* ((kb (kb:get kb-name))
           (kb-name-string (->string (to-string kb-name)))
           (info (kb 'info))
           (submodels (cdr (assq 'submodels info)))) ;cdr is list of alists
      `(li "Knowledgebase "
           (a (@ (href ,kb-name-string))
              (strong ,kb-name-string))
           ", submodels: "
           ,(if (null? submodels)
               '(strong "None")
               `(ul ,@(map (lambda (sm-alist)
                        (let ((name-pair (assq 'name sm-alist))
                              (tbox-pair (assq 'tbox sm-alist))
                              (namespaces (assq 'namespaces sm-alist)))
                          `(li
                            (p
                             (a (@ (href ,(format #f "~a/~a" kb-name-string (cdr name-pair))))
                                ,(cdr name-pair))
                             ,(format #f " (~a)"
                                      (cond ((not tbox-pair)
                                             "???")
                                            ((cdr tbox-pair)
                                             "tbox")
                                            (else
                                             "abox"))))
                            (p "Namespaces")
                            (table
                             (tr (th "prefix") (th "namespace"))
                             ,@(map (lambda (ns)
                                      `(tr (td (@ (style "text-align: right"))
                                               ,(string-append (car ns) ":"))
                                           (td ,(cdr ns))))
                                   (cdr namespaces))))))
                      submodels)))
           (p "Knowledgebase metadata...")
           (pre ,(sexp-xml:escape-string-for-xml (model-to-string (kb 'get-metadata))))
           ;; Is this the best thing?  Perhaps better would be to iterate
           ;; through the predicates on the model and list them in some
           ;; semi-readable fashion.
           )))
  (if (= (length path-info-list) 0)     ;we handle this one
      (let ((namelist (kb:get-names)))
        (list '|SC_OK|
              (response-page request
                             "Quaestor: list of knowledgebases"
                             (if (null? namelist)
                                 `((p "No knowledgebases available"))

                                 `((p "Knowledgebases available:")
                                   (ul ,@(map (lambda (kb-name)
                                                (display-kb-info kb-name))
                                              (kb:get-names))))))))
      #f))

;; If path-info-list has one element, and the query-string starts with "query",
;; then the query is a URL-encoded SPARQL query to make of the model
;; named in (car path-info-list).
(define/contract (get-model-query (path-info-list list?)
                                  (query-string string-or-false?)
                                  (request request?)
                                  -> list-or-false?)
  (define (sparql-encoded-query model-name encoded-query)
    (with/fc
        (make-fc '|SC_BAD_REQUEST|)
      (lambda ()
        (let ((model (kb:get model-name)))
          (or model
              (error 'get-model-query
                     "unknown knowledgebase ~a" model-name))
          (sparql:make-query-runner model
                                    (url-decode-to-jstring encoded-query)
                                    (request->accept-mime-types request))))))
  (and (= (length path-info-list) 1)
       query-string
       (let ((qp (parse-query-string query-string)))
         (cond ((assq 'query qp)
                => (lambda (query-spec)
                     (list '|SC_OK|
                           (sparql-encoded-query (request->kb-uri request)
                                                 (cdr query-spec)))))
               (else #f)))))

;; Retrieve the submodel named by the two-element path-info-list, and
;; write it to the response.  Return #t if successful, or set a
;; suitable response code and return a string representing an HTTP
;; error document, if there are any problems.
(define/contract (get-model (path-info-list list?)
                            (query-string string-or-false?)
                            (request request?)
                            -> list-or-false?)
  (define-generic-java-methods write to-string)
  (define-java-classes <java.lang.string>)

  ;; Examine any Accept headers in the request.  Return #f if there were
  ;; Accept headers and we can't satisfy them.
  ;; Return (mime-string . rdf-language) if we can satisfy a request.
  ;; Return a default pair (the language which MIME type */* maps to)
  ;; if there were no Accept headers.
  (define (find-acceptable-rdf-language rq)
    (let ((acceptable-mimes (request->accept-mime-types rq)))
      (cond ((null? acceptable-mimes)
             (let ((deflang (rdf:mime-type->language #f)))
               (cons (rdf:language->mime-type deflang) deflang)))
            ((acceptable-mime (rdf:mime-type-list) acceptable-mimes)
             => (lambda (ok-mime)
                  (cons ok-mime (rdf:mime-type->language ok-mime))))
            (else
             #f))))

  (chatter "get-model: path-info-list=~s, query-string=~s" path-info-list query-string)
  (case (length path-info-list)
    ((1 2)
     (let ((kb-name (request->kb-uri request)))
       (let ((kb (kb:get kb-name))
             (mime-and-lang (find-acceptable-rdf-language request))
             (query (string->symbol (or query-string "model")))
             (submodel-name (if (null? (cdr path-info-list))
                                #f       ;no submodel
                                (cadr path-info-list))))
         (chatter "get-model: kb-name=~s  submodel-name=~s  mime-and-lang=~s  query=~s"
                  kb-name submodel-name mime-and-lang query)
         (cond ((and kb
                     (eq? query 'model)
                     mime-and-lang) ;normal case
                (let ((m (if submodel-name
                             (kb 'get-model submodel-name)
                             (kb 'get-model))))
                  (if m
                      (list '|SC_OK|
                            (lambda (stream set-mime-type)
                              (set-mime-type (car mime-and-lang))
                              (write m stream (->jstring (cdr mime-and-lang)))))
                      (begin (chatter "get-model: failed to find model with kb-name=~s, submodel=~s"
                                      kb-name submodel)
                             (no-can-do request
                                        '|SC_NOT_FOUND|
                                        "There is no model to return: I failed to find model with kb-name=~s, submodel=~s"
                                        kb-name submodel)))))

               ((and kb
                     (eq? query 'model))
                (no-can-do request
                           '|SC_NOT_ACCEPTABLE|
                           "Cannot generate requested content-type:~%~a"
                           (chatter)))

               ((and kb
                     (eq? query 'metadata)
                     mime-and-lang)
                (cond ((not submodel-name) ;want knowledgebase metadata
                       (list '|SC_OK|
                             (lambda (stream set-mime-type)
                               (set-mime-type (car mime-and-lang))
                               (write (kb 'get-metadata)
                                      stream
                                      (->jstring (cdr mime-and-lang))))))
                      ((kb 'get-metadata submodel-name)
                       => (lambda (model)
                            (list '|SC_OK|
                                  (lambda (stream set-mime-type)
                                    (set-mime-type (car mime-and-lang))
                                    (write model stream (->jstring (cdr mime-and-lang)))))))
                      (else
                       (no-can-do request
                                  '|SC_NOT_FOUND|
                                  "The submodel ~a has no metadata" submodel-name))))

               ((and kb (eq? query 'metadata))
                (no-can-do request 
                           '|SC_NOT_ACCEPTABLE|
                           "Can't return metadata as acceptable type (requested ~a)"
                           (request->accept-mime-types request)))

               (kb
                (no-can-do request
                           '|SC_BAD_REQUEST|
                           "bad query: ~a" query-string))

               (else
                (no-can-do request
                           '|SC_NOT_FOUND|
                           "No such knowledgebase: ~a (path=~s)"
                           (->string (to-string kb-name))
                           path-info-list))))))

        (else
         #f)))

(define get-handlers (list get-knowledgebase-list
                           get-model-query
                           get-model
                           get-fallback))

;; A special debugging handler: handles /debug/*
(define (http-debug-query request)
  (response-page request
                 "Quaestor"
                 `((p "I don't recognise that URL.")
                   (p ,(format #f "KB base: ~s" (request->kb-uri request)))
                   (p "The details of the request follow:")
                   ,@(tabulate-request-information request))))

(define/contract (http-head (request request?) -> list?)
  (let ((path-list (request->path-list request)))
    (case (length path-list)
      ((0)
       #t)
      ((1)                           ;URL referring to a knowledgebase
       (if (kb:get (request->kb-uri request))
           '(|SC_OK|)
           '(|SC_NOT_FOUND|)))
      ((2)                              ;URL referring to a submodel
       (let ((kb (kb:get (request->kb-uri request))))
         (cond ((not kb)
                '(|SC_NOT_FOUND|))
               ((kb 'get-model (cadr path-list))
                '(|SC_OK|))
               (else
                '(|SC_NOT_FOUND|)))))
      (else
       '(|SC_NOT_FOUND|)))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; PUT support

;; Handle a single PUT request.  Examines the path-list from the
;; request, and either creates a new knowledgebase (if there is only
;; one element in the path), or creates or updates a submodel (if there
;; are two elements in the path).  It is an error to create a
;; knowledgebase where one of that name exists already.
(define/contract (http-put (request request?) -> list?)
  (let ((path-list (request->path-list request)))
    (cond ((not (content-headers-ok? request))
           (no-can-do request '|SC_NOT_IMPLEMENTED|
                      "Found unexpected content-* header; allowed ones are ~a"
                      (content-headers-ok?)))
          (else
           (let loop ((h put-handlers))
             (cond ((null? h)
                    (no-can-do request '|SC_BAD_REQUEST|
                               "I don't know what to do with that URL: ~a" (request->kb-uri request)))
                   (((car h) path-list request))
                   (else (loop (cdr h)))))))))

;; Create a new KB, or manage an existing one.  The knowledgebase is
;; called kb-name (a symbol), and the content of the request is read
;; from the given reader.
(define/contract (handle-manage-knowledgebase (path-list list?) (request request?) -> list-or-false?)
  (define-generic-java-methods get-reader get-input-stream)
  (and (= (length path-list) 1)
       (let ((kb (kb:get (request->kb-uri request)))
             (content-type (request->content-type request))
             (query (request->query-string request)))
         (cond
          ((and kb query)               ;unrecognised query
           (no-can-do request '|SC_BAD_REQUEST|
                      "Unrecognised query ~a?~a"
                      (kb 'get-name-as-string) query))

          (kb                           ;already exists
           (no-can-do request
                      '|SC_FORBIDDEN|   ;correct? or SC_CONFLICT?
                      "Knowledgebase ~a already exists (~s)"
                      (kb 'get-name-as-string) kb))

          (query               ;no knowledgebase, but there is a query
           (no-can-do request '|SC_BAD_REQUEST|
                      "Knowledgebase ~a does not exist, so query ~a is not allowed"
                      (kb 'get-name-as-string) query))

          ((not content-type)
           (no-can-do request
                      '|SC_BAD_REQUEST|
                      "Can't post metadata without a content-type"))

          (else                        ;normal case -- create a new KB
           (let* ((new-kb-name (request->kb-uri request))
                  (md (get-metadata-from-source (if (string=? content-type "text/plain")
                                                    (reader->jstring (get-reader request))
                                                    (get-input-stream request))
                                                new-kb-name
                                                content-type))
                  (persistent-submodels
                   (rdf:select-statements md new-kb-name (rdf:make-quaestor-resource "persistentSubmodel") #f)))
             (chatter "persistent-submodels=~s" persistent-submodels)
             (cond ((not (null? persistent-submodels))
                    (let ((pkb (kb:new new-kb-name md)))
                      (define-generic-java-methods (get-uri |getURI|))
                      (for-each (lambda (sm) ;SM is a RDFNode which is a Resource
                                  (chatter "SM=~s" sm)
                                  (pkb 'add-abox! (->string (get-uri sm)) (rdf:new-persistent-model (get-uri sm) md)))
                                persistent-submodels)))
                   (else (kb:new new-kb-name md)))
             '(|SC_NO_CONTENT|)))

;;           (else                        ;normal case -- create a new KB
;;            (let ((new-kb-name (request->kb-uri request)))
;;              (kb:new new-kb-name
;;                      (get-metadata-from-source (if (string=? content-type "text/plain")
;;                                                    (reader->jstring (get-reader request))
;;                                                    (get-input-stream request))
;;                                                new-kb-name
;;                                                content-type)))
;;            '(|SC_NO_CONTENT|))
          ))))

;; Given a knowledgebase called KB-NAME, upload a RDF/XML submodel called
;; KB-NAME which is available from the given STREAM.  The RDF-MIME is
;; the MIME type of the incoming stream.
(define/contract (handle-update-submodel (path-list list?) (request request?) -> list-or-false?)
  (define-generic-java-methods get-input-stream)
  (define (do-update-submodel kb-name
                              submodel-name
                              tbox?
                              metadata?
                              rdf-mime
                              stream)
    (define-generic-java-methods concat to-string)
    (let ((kb (kb:get kb-name))
          (ok-headers '(type length))
          (submodel-uri (request->submodel-uri request)))
      (if kb
          (with/fc
              (make-fc '|SC_BAD_REQUEST|)
            (lambda ()
              (chatter "update-submodel: about to read ~s (base=~s)" submodel-name submodel-uri)
              (let ((m (rdf:ingest-from-stream/language stream submodel-uri rdf-mime)))
                (chatter "update-submodel: kb=~s submodel=~s" kb submodel-name)

                (cond ((and metadata?
                            (not (null? (rdf:select-statements m submodel-uri "a"
                                                               (rdf:make-quaestor-resource "PersistentModel")))))
                       (if persistence-factory
                           (let ((persistent-model (persistence-factory submodel-uri)))
                             (chatter "Created persistent submodel with URI ~a" submodel-uri)
                             (kb 'add-abox! submodel-name persistent-model)
                             (kb 'set-metadata! submodel-name m)
                             (list '|SC_NO_CONTENT|))
                           (no-can-do request '|SC_NOT_IMPLEMENTED| "No persistence configured in Quaestor")))
                      (metadata?
                       (kb 'set-metadata! submodel-name m)
                       '(|SC_NO_CONTENT|))
                      ((kb 'replace-submodel! submodel-name m)
                       ;; This submodel already exists.
                       ;; Remove all the statements from it, and add the new ones.
                       ;; Do this, rather than simply calling add-[at]box! with the new model M,
                       ;; so that if the submodel is a persistent one, for example,
                       ;; it stays that way.
                       (chatter "Replaced model at URI ~a" submodel-uri)
                       '(|SC_NO_CONTENT|))
                      ((kb (if tbox? 'add-tbox! 'add-abox!) submodel-name m)
                       (chatter "Created transient submodel with URI ~a" submodel-uri)
                       '(|SC_NO_CONTENT|))
                      (else
                       (no-can-do request '|SC_INTERNAL_SERVER_ERROR| ;correct?
                                  "Unable to update model!"))))))
          (no-can-do request '|SC_BAD_REQUEST|
                     "No such knowledgebase ~a" kb-name))))

  (and (= (length path-list) 2)
       (let ((query-string (request->query-string request)))
         (do-update-submodel (request->kb-uri request)
                             (cadr path-list)
                             (or (not query-string)
                                 (string=? query-string "tbox"))
                             (and query-string (string=? query-string "metadata"))
                             (request->content-type request)
                             (get-input-stream request)))))

(define put-handlers (list handle-manage-knowledgebase handle-update-submodel))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; POST support

(define/contract (http-post (request request?) -> list?)
  (let ((path-list (request->path-list request)))
    (cond ((not (content-headers-ok? request))
           (no-can-do request '|SC_NOT_IMPLEMENTED|
                      "Found unexpected content-* header; allowed ones are ~a"
                      (content-headers-ok?)))
          (else
           (let loop ((h post-handlers))
             (cond ((null? h)
                    (no-can-do request '|SC_BAD_REQUEST|
                               "I don't know how to respond to a POST to that URL: ~a" (request->kb-uri request)))
                   (((car h) path-list request))
                   (else (loop (cdr h)))))))))

;; Handle a POST of a SPARQL query.  Must have content-type APPLICATION/SPARQL-QUERY
(define/contract (handle-post-sparql-query (path-list list?) (request request?) -> list-or-false?)
  (define-generic-java-methods get-reader)
  (and (= (length path-list) 1)
       (not (request->query-string request))
       (string=? (request->content-type request) "application/sparql-query")
;;        (let ((content-type (request->content-type request)))
;;          (or (not content-type)
;;              (string=? content-type "application/sparql-query")))
       (let ((kb (kb:get (request->kb-uri request))))
         (or kb
             (report-exception 'http-post
                               '|SC_BAD_REQUEST|
                               "don't know about knowledgebase ~a" (car path-list)))
         (list
          '|SC_OK|
          (sparql:make-query-runner kb
                                    (reader->jstring (get-reader request))
                                    (request->accept-mime-types request))))))

;; Handle a POST of RDF to a submodel URL
(define/contract (handle-post-appended-rdf (path-list list?) (request request?) -> list-or-false?)
  (define-generic-java-methods get-input-stream add size to-string)
  (define (print-model-statements model); XXX DEBUG
    (let ((pu (java-null (java-class '|com.hp.hpl.jena.util.PrintUtil|))))
      (define-generic-java-methods
        list-statements
        print)
      (sort-list
       (map (lambda (stmt)
              (->string (print pu stmt)))
            (jobject->list (list-statements model)))
       string<=?)))
  (let ((mime-type (request->content-type request))
        (kb (kb:get (request->kb-uri request))))
    (and (= (length path-list) 2)
         (not (request->query-string request))
         mime-type
         kb
         (cond ((rdf:mime-type->language mime-type)
                => (lambda (rdf-language)
                     (let ((submodel (kb 'get-model (cadr path-list)))
                           (new-model (rdf:ingest-from-stream/language (get-input-stream request)
                                                                       (request->submodel-uri request)
                                                                       mime-type)))
                       (cond (submodel
                              (kb 'append-to-submodel! (cadr path-list) new-model)
                              (list '|SC_OK|
                                    (response-page request
                                                   "Quaestor"
                                                   `((p ,(format #f "Added ~a statements to submodel ~a"
                                                                 (->number (size new-model))
                                                                 (->string (to-string (request->submodel-uri request)))))
                                                     ;;(p "Model now is...")
                                                     ;;(pre ,@(print-model-statements (kb 'get-model (cadr path-list))))
                                                     ;;(p "Model metadata is...")
                                                     ;;(pre ,@(print-model-statements (kb 'get-metadata (cadr path-list))))
                                                     ))))
                             (else
                              (no-can-do request
                                         '|SC_NOT_FOUND|
                                         "I can't find submodel ~a" path-list))))))
               (else
                (no-can-do request
                           '|SC_BAD_REQUEST|
                           "MIME type ~a is not an RDF type I recognise" mime-type))))))

(define post-handlers (list handle-post-sparql-query handle-post-appended-rdf))

;; Handle POST requests.  Return #t on success, or a string response
(define/contract (xxx-http-post (request  request?) -> list?)
  (define-generic-java-methods get-reader set-content-type)
  (let ((path-list (request->path-list request))
        (query-string (request->query-string request))
        (content-type (request->content-type request)))
    ;; First, insist that there's just one element in the path-list.
    ;; If there is a content type on the incoming query, we check
    ;; that it is application/sparql-query
    ;; (see <http://www.w3.org/TR/rdf-sparql-query/>)
    (if (and (= (length path-list) 1)
             (not query-string)
             (or (not content-type)
                 (string=? content-type "application/sparql-query")))
        (let ((kb (kb:get (request->kb-uri request) ;; (car path-list)
                          )))
          (or kb
              (report-exception 'http-post
                                '|SC_BAD_REQUEST|
                                "don't know about knowledgebase ~a" (car path-list)))
          (list
           '|SC_OK|
           (sparql:make-query-runner kb
                                     (reader->jstring (get-reader request))
                                     (request->accept-mime-types request))))
        (no-can-do request
                   '|SC_BAD_REQUEST|
                   "POST SPARQL request must have one path element, no query, and content-type application/sparql-query~%(path=~s, query=~a, content-type=~a)"
                   path-list query-string content-type))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; DELETE support

;; Handle DELETE requests.  Return a list
(define/contract (http-delete (request request?) -> list?)
  (let ((path-list (request->path-list request))
        (kb-name (request->kb-uri request)))
    (case (length path-list)
      ((1)
       (if (kb:discard kb-name)
                                        ;(set-response-status! response '|SC_NO_CONTENT|)
           '(|SC_NO_CONTENT|)
           (no-can-do request
                      '|SC_BAD_REQUEST| ;correct?
                      "There was no knowledgebase \"~a\" to delete"
                      (car path-list))))
      ((2)
       (let ((submodel (cadr path-list)))
         (cond ((kb:get kb-name)
                => (lambda (kb)
                     (cond ((kb 'drop-submodel! submodel)
                            => (lambda (sm)
                                 (define-generic-java-methods close)
                                 ;(close sm)
                                 '(|SC_NO_CONTENT|)))
                           (else
                            (no-can-do request
                                       '|SC_BAD_REQUEST|
                                       "No such submodel \"~a\"" submodel)))))
               (else
                (no-can-do request
                           '|SC_BAD_REQUEST|
                           "No such knowledgebase \"~a\"" kb-name)))))
      (else
       (no-can-do request
                  '|SC_BAD_REQUEST|
                  "The request path has too many elements: ~s"
                  path-list)))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Helpers specific to this module

;; REQUEST->KB-URI : request -> uri-or-false
;; Return a URI which will be used as the name of the KB referred to by the request
;; (namely, the URL of the model).  Return false if there is no path
(define (request->kb-uri request)
  (let ((path-list (request->path-list request)))
    (and (not (null? path-list))
         (java-new <uri>
                   (->jstring
                    (string-append (webapp-base-from-request request #t)
                                   "/"
                                   (car path-list)))))))

;; REQUEST->SUBMODEL-URI : request -> uri-or-false
;; Return a URI which is used as the name of a KB submodel.
;; Return #f if there is no path, or it doesn't have enough elements.
(define (request->submodel-uri request)
  (let ((path-list (request->path-list request)))
    (and (= (length path-list) 2)
         (java-new <uri>
                   (->jstring
                    (string-append (webapp-base-from-request request #t)
                                   "/"
                                   (car path-list)
                                   "/"
                                   (cadr path-list)))))))

;; GET-METADATA-FROM-SOURCE : jinput-stream base-uri content-type -> jena-model
;; GET-METADATA-FROM-SOURCE : jstring base-uri <ignored> -> jena-model
;;
;; Given an input stream, read a model from it; given a string, make
;; a model which has this as a dc:description.
(define (get-metadata-from-source source base-uri content-type)
  (define-generic-java-method to-string)
  (let ((base-uri-jstring (to-string base-uri)))
    (if (is-java-type? source <java.lang.string>)
        (let ((new-model (rdf:new-empty-model))) ;content is string
          (define-generic-java-methods create-resource create-statement create-literal add)
          (add new-model
               (create-statement new-model
                                 (create-resource new-model base-uri-jstring)
                                 (java-retrieve-static-object
                                  '|com.hp.hpl.jena.vocabulary.DC.description|)
                                 (create-literal new-model source (->jstring "en")))))
        (rdf:ingest-from-stream/language source ;content is model
                                         base-uri-jstring
                                         content-type))))

;; Small module to wrap a hashtable, which stores functions
;; (and possibly, later, continuations) in exchange for a token
(module f-store
    (f->ftoken ftoken->f)

  (import hashtable)
  (define fmap #f)
  (define fmap-sync-object (->jstring ""))

  (define (f->ftoken f)
    (define-java-class <java.lang.system>)
    (define-generic-java-methods
      current-time-millis)
    (or fmap (set! fmap (make-hashtable)))
    (java-synchronized fmap-sync-object
      (lambda ()
        (let ((tok (format #f "f~a"
                           (->number (current-time-millis
                                      (java-null <java.lang.system>))))))
          (hashtable/put! fmap tok f)
          tok))))

  (define (ftoken->f ftoken)
    (if fmap
        (let ((f (hashtable/get fmap ftoken)))
          (java-synchronized fmap-sync-object
            (lambda ()
              (and f (hashtable/remove! fmap ftoken))))
          f)
        #f)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; XML-RPC support

(define xmlrpc-handler #f)
;; (module xmlrpc-support
;;     (xmlrpc-handler)
(let ()

  (import f-store)

  ;; Wrapper for xmlrpc:create-fault.
  ;; Given a fault code as a symbol, turn it into an integer using the
  ;; contained alist, and produce an error message using the given format
  ;; and arguments.
  (define (fault code fmt . args)
    (let ((fault-list '((protocol-error 0)
                        (unrecognised-method 1)
                        (malformed-request 2)
                        (unknown-object 10)
                        (unparseable-query 20))))
      (let ((l (assq code fault-list)))
        (if l
            (xmlrpc:create-fault
             (cadr l)
             (apply format
                    `(#f
                      ,(string-append "~a:" fmt)
                      ,code
                      ,@args)))
            (error 'fault "Ooops: unrecognised fault code ~s" code)))))

  ;; make-fc-xmlrpc symbol string -> procedure
  ;;
  ;; Make a failure-continuation suitable for XML-RPC errors.  Given a
  ;; MESSAGE-CODE symbol acceptable to FAULT, and a MESSAGE-TEXT string,
  ;; return a procedure suitable for use as a failure-continuation.
  (define (make-fc-xmlrpc message-code message-text)
    (lambda (m e)
      ;; don't print the stack trace except when debugging:
      ;; angle brackets result in malformed XML
      (list '|SC_OK|
            "text/xml"
            (if (debugging?)
                (fault message-code
                       "~a (~a)~%~a"
                       message-text
                       (format-error-record m) ;(error-message m)
                       (with-output-to-string
                         (lambda () (print-stack-trace e))))
                (fault message-code
                       "~a (~a)"
                       message-text
                       (format-error-record m) ;(error-message m) is tidier, but gives less info
                       )))))

  ;; Get a single model.  The response contains a URL which points
  ;; to one of the HTTP GET methods.
  (define (xmlrpc-get-model quaestor-url . args)
    (define-generic-java-methods resolve to-string)
    (let* ((kb-shortname (and (not (null? args))
                              (car args)))
           (kb-name (resolve quaestor-url (->jstring (format #f "kb/~a" kb-shortname)))))
      (chatter "xmlrpc-get-model: kb-shortname=~s  base=~s  kb-name=~s" kb-shortname quaestor-url kb-name)
      (case (length args)
        ((1)
         (let ((kb (kb:get kb-name)))
           (if kb
               (xmlrpc:create-response "~a" (->string (to-string kb-name)))
               (fault 'unknown-object
                      "no such knowledgebase ~a (~a)" kb-name kb-shortname))))
        ((2)
         (let ((kb (kb:get kb-name))
               (submodel (cadr args)))
           (if (and kb (kb 'has-model submodel))
               (xmlrpc:create-response "~a/~a"
                                       (->string (to-string kb-name)) 
                                       submodel)
               (fault 'unknown-object "no such model ~a/~a" kb-shortname submodel))))
        (else
         (fault 'malformed-request
                "get-model requires 1 or 2 string args, got ~s"
                args)))))

  ;; xmlrpc-query-model uri string ?string -> sexp
  ;;
  ;; Do a query against the given model.
  (define (xmlrpc-query-model quaestor-url model-name query-string . mime-type)
    (define-generic-java-methods resolve to-string)
    (or (string? model-name)
        (fault 'malformed-request "malformed model name ~s (expected string)" model-name))
    (let ((kb (kb:get (resolve quaestor-url (->jstring (string-append "kb/" model-name))))))
      (if kb
          (with/fc
              (make-fc-xmlrpc 'unparseable-query "can't process SPARQL query")
            (lambda ()
              (let ((runner
                     (sparql:make-query-runner kb
                                               query-string
                                               (if (null? mime-type)
                                                   '("*/*")
                                                   `(,(car mime-type))))))
                (xmlrpc:create-response "~apickup/~a"
                                        (->string (to-string quaestor-url))
                                        (f->ftoken runner)))))
          (fault 'unknown-object
                 "don't know about knowledgebase ~a" model-name))))

  ;; method-name->handler symbol -> integer, procedure
  ;;
  ;; Map method names to procedures.
  ;;    (method-name->handler 'NAME) => CHECKER, HANDLER
  ;; Given a method NAME as a symbol, return a pair of values giving
  ;; a procedure which will validate that method's arguments,
  ;; and a procedure which will do the
  ;; work.  The handler must match the signature
  ;;    (handler BASE-URI ARG ...) => XML-SEXP
  ;; The handler is passed the BASE-URI of this service and the
  ;; arguments extracted from the XML-RPC call,
  ;; and returns a sexp which is to be converted to XML as a
  ;; response.  This may most conveniently be generated by
  ;; XMLRPC:CREATE-RESPONSE or XMLRPC:CREATE-FAULT (wrapped in FAULT).
  (define method-name->handler
    (let ((mappings #f))
      (lambda (name)
        (or mappings
            (set! mappings
                  `((get-model   ,(lambda (args)
                                    (let ((s (map string? args)))
                                      (or (equal? s '(#t))
                                          (equal? s '(#t #t)))))
                                 ,xmlrpc-get-model)
                    (query-model ,(lambda (args)
                                    (let ((s (map string? args)))
                                      (or (equal? s '(#t #t))
                                          (equal? s '(#t #t #t)))))
                                 ,xmlrpc-query-model))))
        (cond ((not name)
               (values #f #f))
              ((assq name mappings)
               => (lambda (l)
                    (apply values (cdr l))))
              (else
               (values #f #f))))))

  ;; Do the actual XML-RPC call, given a READER from which to read the 
  ;; XML, and a URL, which is the base URI of the service.  Return a sexp
  ;; which is to be turned into XML.
  (define (do-xmlrpc-call base-uri reader)
    (let ((call (xmlrpc:new-call (sexp-xml:xml->sexp/reader reader))))
      (receive (checker h) (method-name->handler (xmlrpc:method-name call))
        (cond ((not h)
               (fault 'unrecognised-method
                      "unrecognised method: ~a" (xmlrpc:method-name call)))
              ((not (procedure? h))
               (error 'do-xmlrpc-call
                      "handler was not procedure! ~a -> ~s"
                      (xmlrpc:method-name call) h))
              (checker
               (let ((arglist (xmlrpc:method-param-list call)))
                 (if (checker arglist)
                     (apply h (cons base-uri arglist)) ;normal case
                     (fault 'malformed-request
                            "method ~a got wrong number or type of arguments: ~a"
                            (xmlrpc:method-name call) arglist))))
                
              (else                ;normal case -- variable no. args
               (apply h
                      (cons base-uri (xmlrpc:method-param-list call))))))))

  ;; Handle a single XML-RPC request.  Return a response as a list
  (define/contract (_xmlrpc-handler (request request?) -> list?)
    (define-generic-java-methods get-reader)

    (with/fc 
        (make-fc-xmlrpc 'protocol-error "malformed request")
      (lambda ()
        (cond  ((not (content-headers-ok? request))
                ;; Unexpected Content-* header found
                (no-can-do request '|SC_NOT_IMPLEMENTED|
                           "Found unexpected content-* header; allowed ones are ~a"
                           (content-headers-ok?)))

               ((let ((type (request->content-type request)))
                  (and type (or (string=? type "text/xml")
                                ;; http://www.xmlrpc.com/spec says text/xml only,
                                ;; but allow application/xml, too
                                (string=? type "application/xml"))))
                ;; Good -- normal case
                ;; Now do the actual work of reading the method
                ;; call from the input reader.

                (list '|SC_OK|
                      "text/xml"
                      (do-xmlrpc-call
                       (java-new <uri> (->jstring (string-append (webapp-base-from-request request) "/")))
                       (get-reader request))))

               (else
                ;; bad Content-Type
                (list '|SC_OK|
                      "text/xml"
                      (fault 'protocol-error
                             "Request content-type must be text/xml, not ~a"
                             (or (request->content-type request)
                                 "<null>"))))))))
  (set! xmlrpc-handler _xmlrpc-handler)
)                                     ;end of module xmlrpc-support

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Pickup
;;
;; We get tokens which were created by f->ftoken, turn them back into
;; functions, and call them.  There's only one type of function signature
;; at present, namely those created by xmlrpc-query-runner above, which takes
;; an output stream and a function to set a response MIME type.  More
;; are clearly possible in future.

;; pickup-dispatcher java-request java-response -> object
;; side-effect: dereference the ftoken in the request, and call the function,
;;   which will send output to the response output stream.
;;
;; Handle the pickup functions.  Return whatever the stored function returns.
(define/contract (pickup-dispatcher (request request?) -> list?)
  (import f-store)
  (define-generic-java-method
    set-content-type)
  (let ((path-list (request->path-list request)))
    (cond ((not (= (length path-list) 1))
           (no-can-do request
                      '|SC_BAD_REQUEST|
                      "found multiple path elements in pickup URL: ~s"
                      path-list))
          ((ftoken->f (car path-list))
           => (lambda (callback)
                (list '|SC_OK| callback)))
          (else
           (no-can-do request
                      '|SC_BAD_REQUEST|
                      "can't find callback for token ~a (have you called this more than once?)"
                      (car path-list))))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Config handling.  Simply display a status page.

(define/contract (config-handler (request request?) -> list?)
  (list '|SC_OK|
        (response-page request
                       "Quaestor config"
                       `((p "Quaestor configuration and versions")
                         (table (@ (border 1))
                                ,@(map (lambda (pair)
                                         `(tr (td ,(symbol->string (car pair)))
                                              (td ,(or (cdr pair) "not available"))))
                                       (ident)))))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Really miscellaneous stuff

;; Return true if debugging is on; false otherwise.
;; If given an argument, set the debugging state to that value and return the
;; previous one
(define debugging?
  (let ((flag #f))
    (lambda args
      (if (null? args)
          flag
          (let ((prev flag))
            (set! flag (car args))
            prev)))))
