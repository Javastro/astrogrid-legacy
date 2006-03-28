;; SISC program to create a Scheme server
;;
;; See http://www.ietf.org/rfc/rfc2616.txt

(import s2j)
(import debugging)                      ;for print-stack-trace
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
(require-library 'util/sisc-xml)
(import* sisc-xml
         sisc-xml:sexp->xml)

(require-library 'sisc/libs/srfi/srfi-1)
(import* srfi-1
         remove
         filter)
(require-library 'sisc/libs/srfi/srfi-13)
(import* srfi-13
         string-prefix?
         string-downcase
         string-index)

;; (set! this to #t to produce more verbose (but XML ill-formed) error messages
(define *debugging* #f)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; GET support

;; Handle a single GET request.  This examines the path-list from the
;; request, and works through a list of handlers.  It may return a
;; string, or #t on success; if it returns a string, it is to be
;; returned as the response by the caller.
(define (http-get request response)
  (with-failure-continuation
       (make-fc request response '|SC_INTERNAL_SERVER_ERROR|)
    (lambda ()
      (let ((path-list (request->path-list request))
            (query-string (request->query-string request)))
        (set-http-response response '|SC_OK|) ;default response
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
  (response-page "Quaestor"
                 `((p "I don't recognise that URL.")
                   (p "The details of the request follow:")
                   ,@(tabulate-request-information request))))

;; Display an HTML page reporting on the knowledgebases available
(define (get-knowledgebase-list path-info-list query-string request response)
  (define (display-kb-info kb-name)
    (let* ((info ((kb:get kb-name) 'info))
           (submodel-pair (assq 'submodels info))) ;cdr is list of alists
      `(li "Knowledgebase "
           (strong ,kb-name)
           ", submodels:"
           (ul ,@(map (lambda (sm-alist)
                        (let ((name-pair (assq 'name sm-alist))
                              (tbox-pair (assq 'tbox sm-alist)))
                          (list 'li
                                (format #f "~a (~a)"
                                        (if name-pair (cdr name-pair) "???")
                                        (cond ((not tbox-pair)
                                               "???")
                                              ((cdr tbox-pair)
                                               "tbox")
                                              (else
                                               "abox"))))))
                      (cdr submodel-pair))))))
  (if (= (length path-info-list) 0)     ;we handle this one
      (let ((namelist (kb:get-names)))
        (set-http-response response '|SC_OK| "text/html")
        (response-page "Quaestor: list of knowledgebases"
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
             (sparql-encoded-query (car path-info-list)
                                   (cdr qp))
             (no-can-do response
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
    println)
  (define-java-classes
    <java.lang.string>)

  ;; Examine any Accept headers in the request.  Return #f if there were
  ;; Accept headers and we can't satisfy them;
  ;; return (mime-string . rdf-language) if we can satisfy a request;
  ;; return (mime-string . "RDF/XML") if there were no Accept headers.
  (define (find-ok-language rq)
    (let ((lang-mime-list (request->accept-mime-types rq)))
      (chatter (format #f "lang-mime-list=~s" lang-mime-list))
      (if (null? lang-mime-list)
          (cons (rdf:language->mime-type "RDF/XML")
                "RDF/XML")              ;explicit default language
          (let loop ((ml lang-mime-list))
            (if (null? ml)
                #f                      ;ooops
                (let ((lang-string (rdf:mime-type->language (car ml))))
                  (chatter "rdf:mime-type->language: ~s -> ~s"
                           (car ml) lang-string)
                  (if lang-string
                      ;; convert lang back to mime-type: don't use (car ml)
                      ;; in case it was */*; also this uses a canonical language
                      (cons (rdf:language->mime-type lang-string)
                            lang-string)
                      (loop (cdr ml)))))))))

  (case (length path-info-list)
    ((1 2)
     (let ((kb-name (car path-info-list))
           (submodel-name (if (null? (cdr path-info-list))
                               #f       ;no submodel
                               (cadr path-info-list))))
       (let ((kb (kb:get kb-name))
             (mime-and-lang (find-ok-language request))
             (query (string->symbol (or query-string "model"))))
         (cond ((and kb
                     (eq? query 'model)
                     mime-and-lang) ;normal case
                (set-http-response response '|SC_OK| (car mime-and-lang))
                (write (if submodel-name
                           (kb 'get-model submodel-name)
                           (kb 'get-model))
                       (get-output-stream response)
                       (->jstring (cdr mime-and-lang)))
                #t)

               ((and kb
                     (eq? query 'model))
                (no-can-do response
                           '|SC_NOT_ACCEPTABLE|
                           "Cannot generate requested content-type:~%~a"
                           (chatter)))

               ((and kb
                     (eq? query 'metadata))
                (set-http-response response '|SC_OK| "text/plain")
                (println (get-writer response)
                         (or (kb 'get-metadata-as-jstring)
                             (->jstring "")))
                #t)

               (kb
                (no-can-do response
                           '|SC_BAD_REQUEST|
                           "bad query: ~a" query-string))

               (else
                (no-can-do response
                           '|SC_NOT_FOUND|
                           "No such knowledgebase: ~a" kb-name))))))

        (else
         #f)))

(define get-handlers (list get-knowledgebase-list
                           get-model-query
                           get-model
                           get-fallback))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; PUT support

;; Handle a single PUT request.  Examines the path-list from the
;; request, and either creates a new knowledgebase (if there is only
;; one element in the path), or creates or updates a submodel (if there
;; are two elements in the path).  It is an error to create a
;; knowledgebase where one of that name exists already.
(define (http-put request response)

  ;; Create a new KB, or manage an existing one.  The knowledgebase is
  ;; called kb-name (a symbol), and the content of the request is read
  ;; from the given reader.
  (define (manage-knowledgebase kb-name reader query response)
    (let ((kb (kb:get kb-name)))
      (cond ((and kb
                  query
                  (string=? query "metadata")) ;update metadata
             (kb 'set-metadata
                 (reader->jstring reader))
             (set-http-response response '|SC_NO_CONTENT|))

            ((and kb query)             ;unrecognised query
             (no-can-do response '|SC_BAD_REQUEST|
                        "Unrecognised query ~a?~a"
                        kb-name query))

            (kb                                ;already exists
             (no-can-do response '|SC_FORBIDDEN| ;correct? or SC_CONFLICT?
                        "Knowledgebase ~a already exists"
                        kb-name))

            (query             ;no knowledgebase, but there is a query
             (no-can-do response '|SC_BAD_REQUEST|
                        "Knowledgebase ~a does not exist, so query ~a is not allowed"
                        kb-name query))

            (else                        ;normal case
             (let ((nkb (kb:new kb-name))) ;normal case
               (nkb 'set-metadata
                    (reader->jstring reader))
               (set-http-response response '|SC_NO_CONTENT|))))))

  ;; Given a knowledgebase called KB-NAME, upload a RDF/XML submodel called
  ;; KB-NAME which is available from the given STREAM.  The RDF-MIME is
  ;; the MIME type of the incoming stream.
  (define (update-submodel kb-name
                           submodel-name
                           tbox?
                           rdf-mime
                           stream
                           response)
    (let ((kb (kb:get kb-name))
          (ok-headers '(type length)))
      (if kb                            ;normal case
          (let ((submodel (rdf:ingest-from-stream
                           stream
                           rdf-mime)))
            (chatter "rdf-mime=~s => lang=~s"
                     rdf-mime (rdf:mime-type->language rdf-mime))
            (if submodel
                (if (kb (if tbox? 'add-tbox 'add-abox) ;normal case
                        submodel-name
                        submodel)
                    (set-http-response response '|SC_NO_CONTENT|)
                    (no-can-do response
                               '|SC_INTERNAL_SERVER_ERROR| ;correct?
                               "Unable to update model!"))
                (no-can-do response '|SC_BAD_REQUEST|
                           "Bad RDF MIME type! ~a~%~a"
                           rdf-mime (chatter))))
          (no-can-do response '|SC_BAD_REQUEST|
                     (format #f "No such knowledgebase ~a"
                             kb-name)))))

  (with/fc
      (make-fc request response '|SC_INTERNAL_SERVER_ERROR|)
   (lambda ()
     (define-generic-java-methods
       get-reader
       get-input-stream)
     (let ((path-list (request->path-list request))
           (query-string (request->query-string request)))
       (cond ((not (content-headers-ok? request))
              (no-can-do
               response '|SC_NOT_IMPLEMENTED|
               "Found unexpected content-* header; allowed ones are ~a"
               (content-headers-ok?)))

             ((= (length path-list) 1)
              (manage-knowledgebase (car path-list)
                                    (get-reader request)
                                    (request->query-string request)
                                    response))

             ((= (length path-list) 2)
              (update-submodel (car path-list)
                               (cadr path-list)
                               (or (not query-string)
                                   (string=? query-string "tbox"))
                               (request->content-type request)
                               (get-input-stream request)
                               response))

             (else                      ;ooops
              (let ()
                (define-generic-java-method get-path-info)
                (no-can-do response '|SC_BAD_REQUEST|
                           "The request path ~a has the wrong number of elements (1 or 2)"
                           (->string (get-path-info request))))))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; POST support

;; Handle POST requests.  Return #t on success, or a string response
(define (http-post request response)
  (with/fc
      (make-fc request response '|SC_INTERNAL_SERVER_ERROR|)
    (lambda ()
      (define-generic-java-methods
        get-reader
        set-content-type)
      (if (java-null? response)         ;eh??!
          (no-can-do response
                     '|SC_BAD_REQUEST|
                     "null response!"))
      (let ((path-list (request->path-list request))
            (query-string (request->query-string request))
            (content-type (request->content-type request)))
        ;; First, insist that there's just one element in the path-list.
        ;; We should check also that the content-type of the incoming
        ;; SPARQL query is application/sparql-query
        ;; (see <http://www.w3.org/TR/rdf-sparql-query/>)
        (if (and (= (length path-list) 1)
                 (not query-string)
                 content-type
                 (string=? content-type "application/sparql-query"))
            (let ((kb (kb:get (car path-list))))
              (or kb
                  (error 'http-post
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
            (no-can-do response
                       '|SC_BAD_REQUEST|
                       "POST SPARQL request must have one path element, no query, and content-type application/sparql-query~%(path=~s, query=~a, content-type=~a)"
                       path-list query-string content-type))
        ))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; DELETE support

;; Handle DELETE requests.  Return #t on success, or a string response.
(define (http-delete request response)
  (with/fc
      (make-fc request response '|SC_INTERNAL_SERVER_ERROR|)
    (lambda ()
      (let ((path-list (request->path-list request)))
        (if (= (length path-list) 1)
            (if (kb:discard (car path-list))
                (set-http-response response '|SC_NO_CONTENT|)
                (no-can-do response
                           '|SC_NOT_FOUND| ;correct?
                           "There was no knowledgebase ~a to delete"
                           (car path-list)))
            (no-can-do response
                       '|SC_BAD_REQUEST|
                       "The request path has too many elements"))))))

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
      (if *debugging*
          (fault message-code
                 "~a (~a)~%~a"
                 message-text
                 (error-message m)
                 (with-output-to-string
                   (lambda () (print-stack-trace e))))
          (fault message-code
                 "~a (~a)"
                 message-text
                 (error-message m)))))

  ;; Get a single model.  The response contains a URL which points
  ;; to one of the HTTP GET methods.
  (define (xmlrpc-get-model quaestor-url . args)
    (cond ((and (= (length args) 1)
                (string? (car args)))
           (let ((kb (kb:get (car args))))
             (if kb
                 (xmlrpc:create-response "~a/kb/~a"
                                         quaestor-url (car args))
                 (fault 'unknown-object
                        "no such knowledgebase ~a" (car args)))))
          ((and (= (length args) 2)
                (string? (car args))
                (string? (cadr args)))
           (let ((kb (kb:get (car args))))
             (if (and kb (kb 'has-model (cadr args)))
                 (xmlrpc:create-response "~a/kb/~a/~a"
                                         quaestor-url
                                         (car args)
                                         (cadr args))
                 (fault 'unknown-object
                        "no such model ~a/~a"
                        (car args) (cadr args)))))
          (else
           (fault 'malformed-request
                  "get-model requires 1 or 2 string args, got ~a ~s"
                  (length args) args))))

  ;; xmlrpc-query-model string string string -> sexp
  ;;
  ;; Do a query against the given model.
  (define (xmlrpc-query-model quaestor-url model-name query-string . mime-type)
    (let ((kb (kb:get model-name)))
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
                (xmlrpc:create-response "~a/pickup/~a"
                                        quaestor-url
                                        (f->ftoken runner)))))
          (fault 'unknown-object
                 "don't know about knowledgebase ~a" model-name))))

  ;; method-name->handler symbol -> integer, procedure
  ;;
  ;; Map method names to procedures.
  ;;    (method-name->handler 'NAME) => NARGS, HANDLER
  ;; Given a method NAME as a symbol, return a pair of values giving
  ;; the number of arguments that method expects (or #f if it can
  ;; handle a variable number), and a procedure which will do the
  ;; work.  The handler must match the signature
  ;;    (handler BASE-URI ARG ...) => XML-SEXP
  ;; The handler is passed the BASE-URI of this service and the
  ;; requisite number of arguments extracted from the XML-RPC call,
  ;; and returns a sexp which is to be converted to XML as a
  ;; response.  This may most conveniently be generated by
  ;; XMLRPC:CREATE-RESPONSE or XMLRPC:CREATE-FAULT (wrapped in FAULT).
  (define method-name->handler
    (let ((mappings #f))
      (lambda (name)
        (or mappings
            (set! mappings
                  `((get-model #f ,xmlrpc-get-model)
                    (query-model #f ,xmlrpc-query-model))))
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
  (define (do-xmlrpc-call my-url reader)
    (let ((call (xmlrpc:new-call reader)))
      (call-with-values (lambda ()
                          (method-name->handler (xmlrpc:method-name call)))
        (lambda (nargs h)
          (cond ((not h)
                 (fault 'unrecognised-method
                        "unrecognised method: ~a" (xmlrpc:method-name call)))
                ((not (procedure? h))
                 (error 'do-xmlrpc-call
                        "handler was not procedure! ~a -> ~s"
                        (xmlrpc:method-name call) h))
                (nargs
                 (if (= nargs (xmlrpc:number-of-params call))
                     (apply h         ;normal case 1 -- fixed no. args
                            (cons my-url (xmlrpc:method-param-list call)))
                     (fault 'malformed-request
                            "method ~a expected ~a params, got ~a"
                            (xmlrpc:method-name call)
                            nargs
                            (xmlrpc:number-of-params call))))
                (else              ;normal case 2 -- variable no. args
                 (apply h
                        (cons my-url (xmlrpc:method-param-list call)))))))))

  ;; Handle a single XML-RPC request.  Return a response as a string
  ;; containing XML, success or failure.
  (define (_xmlrpc-handler request response)
    (define-generic-java-methods
      get-reader
      get-local-name
      get-local-port
      get-context-path)

    ;; pre-emptively set the response status and content-type
    ;; (error handlers may change this)
    (set-http-response response '|SC_OK| "text/xml")
    (sisc-xml:sexp->xml
     (with/fc 
         (make-fc-xmlrpc 'protocol-error "malformed request")
       (lambda ()
         (cond  ((not (content-headers-ok? request))
                 ;; Unexpected Content-* header found
                 (no-can-do
                  response '|SC_NOT_IMPLEMENTED|
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
                  (format #f "http://~a:~a~a"
                          (->string (get-local-name request))
                          (->number (get-local-port request))
                          (->string (get-context-path request)))
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
(define (pickup-dispatcher request response)
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
                  (callback (response->lazy-output-stream response)
                            (lambda (mimetype)
                              (set-content-type response
                                                (->jstring mimetype))))
                  (no-can-do response
                             '|SC_BAD_REQUEST|
                             "can't find callback for token ~a (have you called this more than once?)" (car path-list))))
            (no-can-do response
                       '|SC_BAD_REQUEST|
                       "found multiple path elements in pickup URL: ~s"
                       path-list))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Support functions for the HTTP transaction

;; Extract the path-list from the request, splitting it at '/', and
;; returning the result as a list of strings.  If there was no path-info,
;; return an empty list.
;; Any zero-length strings are removed (thus "/one//two/" is returned as
;; a two-element list).
(define (request->path-list request)
  (define-generic-java-methods
    get-path-info
    split
    (java-string-length |length|))
  (let ((path-string (get-path-info request)))
    (cond ((java-null? path-string)
           '())
          ((= (->number (java-string-length path-string)) 0)
           '())
          (else
           (remove (lambda (str) (= (string-length str) 0))
                (map ->string
                     (->list (split path-string (->jstring "/")))))))))
;; The same, except that the path-list components are URL-decoded
;; (which I don't now think is necessary)
;; (define (request->path-list-decoded request)
;;   (define-java-classes
;;     (<url-decoder> |java.net.URLDecoder|))
;;   (define-generic-java-methods
;;     get-path-info
;;     decode
;;     split
;;     (java-string-length |length|))
;;   (let ((path-string (get-path-info request)) ; Java string
;;         (n (java-null <url-decoder>)))
;;     (if (= (->number (java-string-length path-string)) 0)
;;         '()
;;         (remove (lambda (str) (= (string-length str) 0))
;;                 (map (lambda (s)
;;                        (->string (decode n s (->jstring "UTF-8"))))
;;                      (->list (split path-string (->jstring "/"))))))))

;; Extract the query-string from the request, returning it as a (scheme)
;; string, or #f if there is no query.
(define (request->query-string request)
  (define-generic-java-method
    get-query-string)
  (let ((qs (get-query-string request)))
    (if (java-null? qs)
        #f
        (->string qs))))

;; request->content-type java-request -> string-or-false
;;
;; Given a Java REQUEST, return the request content type as a scheme string,
;; or #f if it is not available
(define (request->content-type request)
  (define-generic-java-method get-content-type)
  (let ((content-jstring (get-content-type request)))
    (if (java-null? content-jstring)
        #f
        (->string content-jstring))))

;; Called with one argument, verify that the "Content-*" headers are
;; all in the allowed set, returning #t if so.  Otherwise, return #f.
;; Called with no arguments, return the set of allowed headers.
(define (content-headers-ok? . request)
  (define ok-headers '("content-type"
                       "content-length"))
  (define (header-ok? h)                ;return true if H is an allowed header
    (let loop ((good-list ok-headers))
      (cond ((null? good-list)
             #f)
            ((string-ci=? h (car good-list))
             #t)
            (else
             (loop (cdr good-list))))))
  (if (null? request)
      ok-headers
      (let loop ((header-list (request->header-alist (car request))))
        (cond ((null? header-list)
               #t)
              ((string-prefix? "content-" (caar header-list))
               (and (header-ok? (caar header-list))
                    (loop (cdr header-list))))
              (else
               (loop (cdr header-list)))))))

;; Return the set of request headers as an alist, each element of which is
;; of the form (header-string . value-string): both are scheme strings,
;; and the header-string is lowercased.
(define (request->header-alist request)
  (define-generic-java-methods
    get-header-names
    get-header
    to-lower-case)
  (map (lambda (header-jname)
         (cons (->string (to-lower-case header-jname))
               (->string (get-header request header-jname))))
       (enumeration->list (get-header-names request))))

;; Return the contents of the Accept header as a list of scheme strings.
;; Each one is a MIME type.  If there are no Accept headers, return #f.
(define (request->accept-mime-types request)
  (define-generic-java-methods
    get-headers
    append)
  (parse-http-accept-header
   ;; merge all the "accept" headers into a single comma-separated Java string
   (let loop ((headers
               (enumeration->list (get-headers request (->jstring "accept"))))
              (res #f))
     (if (null? headers)
         res
         (loop (cdr headers)
               (if res
                   (append (append res (->jstring ", "))
                           (car headers))
                   (car headers)))))))

;; Given a READER, return a Java string containing the contents of the stream.
(define (reader->jstring reader)
  (define-java-classes
    <java.io.buffered-reader>
    <java.lang.string-buffer>)
  (define-generic-java-methods
    read
    append
    to-string)
  (let ((buffered-reader (java-new <java.io.buffered-reader> reader))
        (carr (java-array-new <jchar> 512))
        (zo (->jint 0)))
    (let loop ((sb (java-new <java.lang.string-buffer>)))
      (let ((rlen (read buffered-reader carr zo (->jint 512))))
        (if (< (->number rlen) 0)
            (to-string sb)
            (loop (append sb carr zo rlen)))))))

;; response->lazy-output-stream http-response -> java-output-stream
;;
;; Return a function which, when called, will return the response output stream.
;; This extracts the output stream using a LazyOutputStream, so that we
;; don't call GET-OUTPUT-STREAM on the underlying response unless and
;; until we need to, thus leaving any error handler free to do so instead.
;; May be called multiple times (unlike GET-OUTPUT-STREAM).
(define (response->lazy-output-stream response)
  (define-java-classes
    (<lazy-output-stream> |org.eurovotech.quaestor.LazyOutputStream|))
  (define-generic-java-methods
    get-output-stream
    get-lazy-output-stream)
  (get-output-stream (get-lazy-output-stream (java-null <lazy-output-stream>)
                                             response)))

;; Given a RESPONSE, set the response status to the given RESPONSE-CODE,
;; and produce a status page using the given format and arguments.
;; This expects to be called before there has been any other output.
(define (no-can-do response response-code fmt . args)
  (let ((msg (apply format `(#f ,fmt ,@args))))
    (set-http-response response response-code "text/html")
    (response-page "Quaestor: no can do" `((p ,msg)))))

;; make-fc java-request java-response symbol -> procedure
;;
;; Make a SISC failure continuation.  Return a two-argument procedure
;; which can be used as the handler for with-failure-continuation.
;; See REPORT-EXCEPTION for an error procedure which allows you to override
;; the status given here.
(define (make-fc request response status)
  (or (and (java-object? request)
           (java-object? response)
           (symbol? status))
      (error 'make-fc
             "Bad call: request=~s response=~s status=~s"
             request response status))
  (lambda (error-record cont)
    (let* ((msg-or-pair (error-message error-record))
           (show-debugging? (not (pair? msg-or-pair))))
      (set-http-response response
                         (if (pair? msg-or-pair)
                             (car msg-or-pair)
                             status)
                         "text/plain")
      (if show-debugging?
          (format #f "~%Error: ~a~%~a~%~%Stack trace:~%~a~%"
                  msg-or-pair          ;show-debugging? => this is msg-string
                  (let ((c (chatter)))
                    (if c
                        (format #f "[chatter: ~a]" c)
                        ""))
                  (with-output-to-string
                    (lambda () (print-stack-trace cont))))
          (format #f "~%Error: ~a~%" (cdr msg-or-pair))))))

;; For debugging and error reporting.  Given a request, produce a list
;; of sexps describing the content of the request.
(define (tabulate-request-information request)
  (define-generic-java-methods
    (get-request-uri |getRequestURI|)
    get-servlet-path
    get-context-path
    get-path-info
    get-query-string
    get-method
    get-header
    get-header-names)
  (define (->string-or-empty js)
    (if (java-null? js)
        "EMPTY"
        (->string js)))
  (list '(h2 "Request parts")
        (append '(table (@ (border 1)))
                (map (lambda (p)
                       `(tr (td ,(car p))
                            (td ,(->string-or-empty (cdr p)))))
                     `(("method"       . ,(get-method request))
                       ("request URI"  . ,(get-request-uri request))
                       ("context path" . ,(get-context-path request))
                       ("servlet path" . ,(get-servlet-path request))
                       ("path info"    . ,(get-path-info request))

                       ("query string" . ,(get-query-string request)))))

        '(h2 "Request headers")
        (append (list 'table
                      '(@ (border 1)))
                (map (lambda (jheader)
                       `(tr (td ,(->string-or-empty jheader))
                            (td ,(->string-or-empty (get-header request jheader)))))
                     (enumeration->list (get-header-names request))))
        ))



;; Evaluates to a string corresponding to a HTML page.  The TITLE-STRING
;; is a string containing a page title, and the BODY-SEXP is a list of sexps.
(define (response-page title-string body-sexp)
  (let ((s `(html (head (title ,title-string)
                          (link (@ (rel stylesheet)
                                   (type text/css)
                                   (href /quaestor/base.css))))
                    (body (h1 ,title-string)
                          ,@body-sexp))))
    (sisc-xml:sexp->xml s)))

;; set-http-response java-response symbol -> #t
;; set-http-response java-response symbol string -> #t
;; side-effect: set the java-response's status and optionally MIME type
;;
;; Set the HTTP response to the given value.  The RESPONSE-SYMBOL is
;; one of the SC_* fields in javax.servlet.http.HttpServletResponse.
;; Eg: (set-http-response response '|SC_OK|).
;; If MIME-TYPE-L is non-null, then its car is a Scheme string representing
;; the MIME type which should be set on the response.
;; Returns #t for convenience, so that the call to this procedure may (but
;; need not be) the final call in a handler function.
(define set-http-response
  (let ((response-object #f))
    (define-generic-java-methods
      set-status
      set-content-type)
    (lambda (response response-symbol . mime-type-l)
      (if (not response-object)         ;first time
          (set! response-object (java-null
                                 (java-class
                                  '|javax.servlet.http.HttpServletResponse|))))
      (set-status response
                  ((generic-java-field-accessor response-symbol)
                   response-object))
      (or (null? mime-type-l)
          (set-content-type response (->jstring (car mime-type-l))))
      #t)))
