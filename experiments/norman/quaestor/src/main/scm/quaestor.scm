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
         sexp-xml:sexp->xml
         sexp-xml:xml->sexp/reader
         sexp-xml:escape-string-for-xml)
(require-library 'util/lambda-contract)

(require-library 'sisc/libs/srfi/srfi-13)
(import* srfi-13
         string-index)

(define (ident)
  (define-java-classes
    <sisc.util.version>
    (<SDB> |com.hp.hpl.jena.sdb.SDB|))
  (define-generic-java-field-accessor :version |VERSION|)
  `((quaestor.version . "@VERSION@")
    (sisc.version . ,(->string (:version (java-null <sisc.util.version>))))
    (sdb.version . ,(->string (:version (java-null <SDB>))))
    (string
     . "quaestor.scm @VERSION@ ($Revision: 1.50 $ $Date: 2008/07/06 16:21:26 $)")))

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
(define (string-or-true? x)
  (or (string? x)
      (and (boolean? x) x)))
(define (string-or-false? x)
  (or (not x)
      (string? x)))

(define-syntax receive
  (syntax-rules ()
    ((_ formals expression body ...)
     (call-with-values (lambda () expression)
       (lambda formals
         body ...)))))

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
  (let ((kb (get-init-parameter scheme-servlet (->jstring "kb-context")))
        (xmlrpc (get-init-parameter scheme-servlet (->jstring "xmlrpc-context")))
        (pickup (get-init-parameter scheme-servlet (->jstring "pickup-context"))))
    (define (reg method context proc)
      (register-handler scheme-servlet
                        (->jstring method)
                        context
                        (java-wrap proc)))
    (reg "GET" kb http-get)
    (reg "GET" (->jstring "/debug") http-debug-query)
    (reg "HEAD" kb http-head)
    (reg "PUT" kb http-put)
    (reg "POST" kb http-post)
    (reg "DELETE" kb http-delete)
    (reg "POST" xmlrpc xmlrpc-handler)
    (reg "GET" pickup pickup-dispatcher)

    ;; initialise the logging, by passing the Servlet to (CHATTER),
    ;; which relies on this object having a log(String) method on it.
    (chatter scheme-servlet)
    (chatter "Initialised quaestor")))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; GET support

;; Handle a single GET request.  This examines the path-list from the
;; request, and works through a list of handlers.  It may return a
;; string, or #t on success; if it returns a string, it is to be
;; returned as the response by the caller.
(define/contract (http-get (request  request?)
                           (response response?)
                           -> string-or-true?)
  (with-failure-continuation
       (make-fc request response '|SC_INTERNAL_SERVER_ERROR|)
    (lambda ()
      (let ((path-list (request->path-list request))
            (query-string (request->query-string request)))
        (set-response-status! response '|SC_OK|) ;default response
        (let loop ((h get-handlers))
          (if (null? h)
              (error 'get "No applicable GET handlers found!")
              (or ((car h) path-list query-string request response)
                  (loop (cdr h)))))))))

;; GET-HANDLERS is a list of procedures which take four arguments:
;;     a list of strings representing the path-info
;;     a query-string
;;     a HTTP Request
;;     a HTTP Response
;; Each procedure should decide whether it can handle this query, and if it
;; does, it should do so and return either a string or #t; otherwise
;; it should do nothing and return #f.  If the procedure returns a
;; string, then that should be printed out as the response.

;; Fallback get handler.  Matches everything, and returns a string.
(define (get-fallback path-info-list query-string request response)
  (define-generic-java-method
    set-content-type)
  (set-content-type response (->jstring "text/html"))
  (response-page request response
                 "Quaestor"
                 `((p "Debugging the query.")
                   (p "The details of the request follow:")
                   ,@(tabulate-request-information request))))

;; Display an HTML page reporting on the knowledgebases available
(define (get-knowledgebase-list path-info-list query-string request response)
  (define (display-kb-info kb-name)
    (define-generic-java-methods write to-string)
    (define (model-to-string model)
      (define-java-class <java.io.string-writer>)
      (let ((sw (java-new <java.io.string-writer>)))
        (write model sw (->jstring "N3"))
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
           (pre
            ,(sexp-xml:escape-string-for-xml
              (model-to-string (kb 'get-metadata))))
           ;; Is this the best thing?  Perhaps better would be to iterate
           ;; through the predicates on the model and list them in some
           ;; semi-readable fashion.
           )))
  (if (= (length path-info-list) 0)     ;we handle this one
      (let ((namelist (kb:get-names)))
        (set-response-status! response '|SC_OK| "text/html")
        (response-page request response
                       "Quaestor: list of knowledgebases"
                       (if (null? namelist)
                           `((p "No knowledgebases available"))

                           `((p "Knowledgebases available:")
                             (ul ,@(map (lambda (kb-name)
                                          (display-kb-info kb-name))
                                        (kb:get-names)))))))
      #f))

;; If path-info-list has one element, and the query-string starts with "sparql",
;; then the query is a URL-encoded SPARQL query to make of the model
;; named in (car path-info-list).
(define (get-model-query path-info-list query-string request response)
  (define (sparql-encoded-query model-name encoded-query)
    (with/fc
        (make-fc request response '|SC_BAD_REQUEST|)
      (lambda ()
        (define-generic-java-methods
          set-content-type
          get-output-stream)
        (let ((model (kb:get model-name)))
          (or model
              (error 'get-model-query
                     "unknown knowledgebase ~a" model-name))
          (let ((runner
                 (sparql:make-query-runner
                  model
                  (url-decode-to-jstring encoded-query)
                  (request->accept-mime-types request))))
            (runner
             (response->lazy-output-stream response)
             (lambda (mimetype)
               (set-content-type response
                                 (->jstring mimetype))))
            #t)))))
  (let ((qp (parse-query-string query-string)))
    (and (= (length path-info-list) 1)
         (car qp)
         (string=? (car qp) "sparql")
         ;; the world is calling...
         (if (cdr qp)
             (sparql-encoded-query (request->kb-uri request)
                                   (cdr qp))
             (no-can-do request response
                        '|SC_BAD_REQUEST|
                        "found empty SPARQL query in GET request")))))

;; Retrieve the submodel named by the two-element path-info-list, and
;; write it to the response.  Return #t if successful, or set a
;; suitable response code and return a string representing an HTTP
;; error document, if there are any problems.
(define (get-model path-info-list query-string request response)
  (define-generic-java-methods
    write
    get-output-stream
    set-status
    get-writer
    to-string
    println)
  (define-java-classes
    <java.lang.string>)

  ;; Examine any Accept headers in the request.  Return #f if there were
  ;; Accept headers and we can't satisfy them.
  ;; Return (mime-string . rdf-language) if we can satisfy a request.
  ;; Return a default pair (the language which MIME type */* maps to)
  ;; if there were no Accept headers.
  (define (find-ok-language rq)
    (let ((lang-mime-list (request->accept-mime-types rq)))
      (chatter (format #f "lang-mime-list=~s" lang-mime-list))
      (if (null? lang-mime-list)
          (let ((deflang (rdf:mime-type->language #f)))
            (cons (rdf:language->mime-type deflang)
                  deflang))             ;use the default language
          (let loop ((ml lang-mime-list))
            (if (null? ml)
                #f                      ;ooops
                (let* ((requested-mime-type (car ml))
                       (lang-string (rdf:mime-type->language requested-mime-type)))
                  (chatter "rdf:mime-type->language: ~s -> ~s"
                           requested-mime-type lang-string)
                  (if lang-string
                      (cond ((string-index requested-mime-type #\*)
                             ;; convert lang back to mime-type: don't use (car ml)
                             ;; since it includes a star (eg, */*); also this canonicalises the type
                             (cons (rdf:language->mime-type lang-string)
                                   lang-string))
                            (else
                             (cons requested-mime-type lang-string)))
                      (loop (cdr ml)))))))))

  (chatter "get-model: path-info-list=~s, query-string=~s" path-info-list query-string)
  (case (length path-info-list)
    ((1 2)
     (let ((kb-name (request->kb-uri request))
           (submodel-name (if (null? (cdr path-info-list))
                               #f       ;no submodel
                               (cadr path-info-list))))
       (let ((kb (kb:get kb-name))
             (mime-and-lang (find-ok-language request))
             (query (string->symbol (or query-string "model"))))
;;          (chatter "get-model: kb-name=~s  submodel-name=~s  mime-and-lang=~s  query=~s"
;;                   kb-name submodel-name mime-and-lang query)
         (cond ((and kb
                     (eq? query 'model)
                     mime-and-lang) ;normal case
                (set-response-status! response '|SC_OK| (car mime-and-lang))
                (let ((m (if submodel-name
                             (kb 'get-model submodel-name)
                             (kb 'get-model))))
                  (if m
                      (write m
                             (get-output-stream response)
                             (->jstring (cdr mime-and-lang)))
                      (begin (chatter "get-model: failed to find model with kb-name=~s, submodel=~s"
                                      kb-name submodel)
                             (no-can-do request response
                                        '|SC_NOT_FOUND|
                                        "There is no model to return"))))
                #t)

               ((and kb
                     (eq? query 'model))
                (no-can-do request response
                           '|SC_NOT_ACCEPTABLE|
                           "Cannot generate requested content-type:~%~a"
                           (chatter)))

               ((and kb
                     (eq? query 'metadata)
                     mime-and-lang)
                (set-response-status! response '|SC_OK| (car mime-and-lang))
                ((generic-java-method '|write|)
                 (kb 'get-metadata)
                 (get-writer response)
                 (->jstring (cdr mime-and-lang)))
                #t)

               ((and kb (eq? query 'metadata))
                (no-can-do
                 request response
                 '|SC_NOT_ACCEPTABLE|
                 "Can't return metadata as acceptable type (requested ~a)"
                 (request->accept-mime-types request)))

               (kb
                (no-can-do request response
                           '|SC_BAD_REQUEST|
                           "bad query: ~a" query-string))

               (else
                (no-can-do request response
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
(define (http-debug-query request response)
  (define-generic-java-method
    set-content-type)
  (set-content-type response (->jstring "text/html"))
  (response-page request response
                 "Quaestor"
                 `((p "I don't recognise that URL.")
                   (p ,(format #f "KB base: ~s" (request->kb-uri request)))
                   (p "The details of the request follow:")
                   ,@(tabulate-request-information request))))

(define/contract (http-head (request  request?)
                            (response response?)
                            -> string-or-true?)
  (with/fc
      (make-fc request response '|SC_INTERNAL_SERVER_ERROR|)
    (lambda ()
      (let ((path-list (request->path-list request)))
        (set-response-status! response '|SC_OK|) ;default response
        (case (length path-list)
          ((0)
           #t)
          ((1)                          ;URL referring to a knowledgebase
           (if (kb:get (request->kb-uri request))
               #t
               (set-response-status! response '|SC_NOT_FOUND|)))
          ((2)                          ;URL referring to a submodel
           (let ((status (let ((kb (kb:get (request->kb-uri request))))
                           (cond ((not kb)
                                  '|SC_NOT_FOUND|)
                                 ((kb 'get-model (cadr path-list))
                                  '|SC_OK|)
                                 (else
                                  '|SC_NOT_FOUND|)))))
             (set-response-status! response status)))
          (else
           (set-response-status! response '|SC_NOT_FOUND|)))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; PUT support

;; Handle a single PUT request.  Examines the path-list from the
;; request, and either creates a new knowledgebase (if there is only
;; one element in the path), or creates or updates a submodel (if there
;; are two elements in the path).  It is an error to create a
;; knowledgebase where one of that name exists already.
(define/contract (http-put (request  request?)
                           (response response?)
                           -> string-or-true?)

  ;; Create a new KB, or manage an existing one.  The knowledgebase is
  ;; called kb-name (a symbol), and the content of the request is read
  ;; from the given reader.
  (define (manage-knowledgebase kb-name query)
     (define-generic-java-methods
       get-reader
       get-input-stream)
    (let ((kb (kb:get kb-name))
          (content-type (request->content-type request)))
      (cond
       ;; at one time there was a (and kb query (string=? query "metadata"))
       ;; condition here, to update the metadata -- I no longer support this.

       ((and kb query)                  ;unrecognised query
        (no-can-do request response '|SC_BAD_REQUEST|
                   "Unrecognised query ~a?~a"
                   kb-name query))

       (kb                              ;already exists
        (no-can-do request response
                   '|SC_FORBIDDEN|      ;correct? or SC_CONFLICT?
                   "Knowledgebase ~a already exists (~s)"
                   kb-name kb))

       (query                  ;no knowledgebase, but there is a query
        (no-can-do request response '|SC_BAD_REQUEST|
                   "Knowledgebase ~a does not exist, so query ~a is not allowed"
                   kb-name query))

       ((not content-type)
        (no-can-do request response
                   '|SC_BAD_REQUEST|
                   "Can't post metadata without a content-type"))

       (else                           ;normal case -- create a new KB
        (kb:new kb-name
                (get-metadata-from-source (if (string=? content-type "text/plain")
                                              (reader->jstring (get-reader request))
                                              (get-input-stream request))
                                          kb-name
                                          content-type))
        (set-response-status! response '|SC_NO_CONTENT|)))))

  ;; Given a knowledgebase called KB-NAME, upload a RDF/XML submodel called
  ;; KB-NAME which is available from the given STREAM.  The RDF-MIME is
  ;; the MIME type of the incoming stream.
  (define (update-submodel kb-name
                           submodel-name
                           tbox?
                           rdf-mime
                           stream
                           response)
    (define-generic-java-methods concat to-string)
    (let ((kb (kb:get kb-name))
          (ok-headers '(type length))
          (submodel-uri
           ;; this is a rather clumsy way of constructing a sub-URI
           ;; -- isn't there a better way?
           (java-new <uri> (concat (to-string kb-name)
                                                (->jstring (string-append "/" submodel-name))))))
      (if kb
          (with/fc
              (make-fc request response '|SC_BAD_REQUEST|)
            (lambda ()
              (chatter "update-submodel: about to read ~s (base=~s)" submodel-name submodel-uri)
              (let ((m (rdf:ingest-from-stream/language stream submodel-uri rdf-mime)))
                (chatter "update-submodel: kb=~s submodel=~s"
                         kb submodel-name)
                (if (kb (if tbox? 'add-tbox 'add-abox)
                        submodel-name
                        m)
                    (set-response-status! response '|SC_NO_CONTENT|)
                    (no-can-do request response
                               '|SC_INTERNAL_SERVER_ERROR| ;correct?
                               "Unable to update model!")))))
          (no-can-do request response '|SC_BAD_REQUEST|
                     "No such knowledgebase ~a" kb-name))))

  (with/fc
      (make-fc request response '|SC_INTERNAL_SERVER_ERROR|)
   (lambda ()
     (define-generic-java-methods
       get-input-stream)
     (let ((path-list (request->path-list request))
           (query-string (request->query-string request)))
       (cond ((not (content-headers-ok? request))
              (no-can-do request response '|SC_NOT_IMPLEMENTED|
                         "Found unexpected content-* header; allowed ones are ~a"
                         (content-headers-ok?)))

             ((= (length path-list) 1)
              (manage-knowledgebase (request->kb-uri request)
                                    query-string))

             ((= (length path-list) 2)
              (update-submodel (request->kb-uri request)
                               (cadr path-list)
                               (or (not query-string)
                                   (string=? query-string "tbox"))
                               (request->content-type request)
                               (get-input-stream request)
                               response))

             (else                      ;ooops
              (let ()
                (define-generic-java-method get-path-info)
                (no-can-do request response '|SC_BAD_REQUEST|
                           "The request path ~a has the wrong number of elements (1 or 2)"
                           (->string (get-path-info request))))))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; POST support

;; Handle POST requests.  Return #t on success, or a string response
(define/contract (http-post (request  request?)
                            (response response?)
                            -> string-or-true?)
  (with/fc
      (make-fc request response '|SC_INTERNAL_SERVER_ERROR|)
    (lambda ()
      (define-generic-java-methods
        get-reader
        set-content-type)
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
              (let ((runner
                     (sparql:make-query-runner
                      kb
                      (reader->jstring (get-reader request))
                      (request->accept-mime-types request))))
                (runner (response->lazy-output-stream response)
                        (lambda (mimetype)
                          (set-content-type response
                                            (->jstring mimetype))))
                #t))
            (no-can-do request response
                       '|SC_BAD_REQUEST|
                       "POST SPARQL request must have one path element, no query, and content-type application/sparql-query~%(path=~s, query=~a, content-type=~a)"
                       path-list query-string content-type))))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; DELETE support

;; Handle DELETE requests.  Return #t on success, or a string response.
(define/contract (http-delete (request  request?)
                              (response response?)
                              -> string-or-true?)
  (with/fc
      (make-fc request response '|SC_INTERNAL_SERVER_ERROR|)
    (lambda ()
      (let ((path-list (request->path-list request))
            (kb-name (request->kb-uri request)))
        (case (length path-list)
          ((1)
           (if (kb:discard kb-name)
               (set-response-status! response '|SC_NO_CONTENT|)
               (no-can-do request response
                          '|SC_BAD_REQUEST| ;correct?
                          "There was no knowledgebase \"~a\" to delete"
                          (car path-list))))
          ((2)
           (let ((submodel (cadr path-list)))
             (cond ((kb:get kb-name)
                    => (lambda (kb)
                         (if (kb 'drop-submodel submodel)
                             (set-response-status! response '|SC_NO_CONTENT|)
                             (no-can-do request response
                                        '|SC_BAD_REQUEST|
                                        "No such submodel \"~a\""
                                        submodel))))
                   (else
                    (no-can-do request response
                               '|SC_BAD_REQUEST|
                               "No such knowledgebase \"~a\"" kb-name)))))
          (else
           (no-can-do request response
                      '|SC_BAD_REQUEST|
                      "The request path has too many elements: ~s"
                      path-list)))))))

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
                 ))))

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

  ;; Handle a single XML-RPC request.  Return a response as a string
  ;; containing XML, success or failure.
  (define (_xmlrpc-handler request response)
    (define-generic-java-methods get-reader)

    ;; pre-emptively set the response status and content-type
    ;; (error handlers may change this)
    (set-response-status! response '|SC_OK| "text/xml")
    (sexp-xml:sexp->xml
     (with/fc 
         (make-fc-xmlrpc 'protocol-error "malformed request")
       (lambda ()
         (cond  ((not (content-headers-ok? request))
                 ;; Unexpected Content-* header found
                 (no-can-do request response '|SC_NOT_IMPLEMENTED|
                            "Found unexpected content-* header; allowed ones are ~a"
                            (content-headers-ok?)))

                ((let ((type (request->content-type request)))
                   (and type (or (string=? type "text/xml")
                                 ;; http://www.xmlrpc.com/spec
                                 ;; says text/xml only
                                 ;; but allow application/xml, too
                                 (string=? type "application/xml"))))
                 ;; Good -- normal case
                 ;; Now do the actual work of reading the method
                 ;; call from the input reader.
                 (do-xmlrpc-call
                  (java-new <uri> (->jstring (string-append (webapp-base-from-request request) "/")))
                  (get-reader request)))

                (else
                 ;; bad Content-Type
                 (fault 'protocol-error
                        "Request content-type must be text/xml, not ~a"
                        (or (request->content-type request)
                            "<null>"))))))
     '(|methodResponse| fault params struct) ;make it look pretty
     '(param member)))
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
(define/contract (pickup-dispatcher (request  request?)
                                    (response response?)
                                    -> string-or-true?)
  (import f-store)
  (define-generic-java-method
    set-content-type)
  (with/fc
   ;; Since all the error-handling was supposed to be done before
   ;; the callback functions were stored, any errors other than
   ;; malformed calls are our fault.
   (make-fc request response '|SC_INTERNAL_SERVER_ERROR|)
   (lambda ()
     (let ((path-list (request->path-list request)))
       (if (= (length path-list) 1)
           (let ((callback (ftoken->f (car path-list))))
             (if callback
                 ;; it's OK for this callback to be all side-effect, but make sure we don't return #f
                 (or (callback (response->lazy-output-stream response)
                               (lambda (mimetype)
                                 (set-content-type response
                                                   (->jstring mimetype))))
                     #t)
                 (no-can-do request response
                            '|SC_BAD_REQUEST|
                            "can't find callback for token ~a (have you called this more than once?)"
                            (car path-list))))
           (no-can-do request response
                      '|SC_BAD_REQUEST|
                      "found multiple path elements in pickup URL: ~s"
                      path-list))))))


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
