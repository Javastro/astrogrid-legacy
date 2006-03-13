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

(require-library 'sisc/libs/srfi/srfi-1)
(import* srfi-1
         remove
         filter)
(require-library 'sisc/libs/srfi/srfi-13)
(import* srfi-13
         string-prefix?
         string-downcase)

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
  (response-page "Quaestor"
                 `((p "I don't recognise that URL.")
                   (p "The details of the request follow:")
                   ,@(tabulate-request-information request))))

;; Display an HTML page reporting on the knowledgebases available
(define (get-knowledgebase-list path-info-list query-string request response)
  (define (display-kb-info kb-name)
    (let* ((info ((kb-get kb-name) 'info))
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
      (response-page "Quaestor: list of knowledgebases"
                     `((p "Knowledgebases available:")
                       (ul ,@(map (lambda (kb-name)
                                    (display-kb-info kb-name))
                                  (kb-get-names)))
                       ))
      #f))

;; If path-info-list has one element, and the query-string starts with "sparql",
;; then it's a SPARQL query to make of the model named in (car path-info-list).
(define (get-model-query path-info-list query-string request response)
  (define (sparql-encoded-query model-name q)
    (with-failure-continuation
       (make-fc request response '|SC_BAD_REQUEST|)
     (lambda ()
       (define-generic-java-method set-content-type)
       (sparql:perform-query model-name
                             (url-decode-to-jstring q)
                             (make-lazy-output-stream response)
                             (request->accept-mime-types request)
                             (lambda (mimetype)
                               (set-content-type response
                                                 (->jstring mimetype))))
       #t)))
  (if (and (= (length path-info-list) 1)
           (string=? (substring query-string 0 6) "sparql"))
      (sparql-encoded-query
       (car path-info-list)
       (substring query-string 7 (string-length query-string)))
      #f))

;; Retrieve the submodel named by the two-element path-info-list, and
;; write it to the response.  Return #t if successful, or set a
;; suitable response code and return a string representing an HTTP
;; error document, if there are any problems.
(define (get-model path-info-list query-string request response)
  (define-generic-java-methods
    write
    get-output-stream
    set-status
    set-content-type
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
      (msglist (format #f "lang-mime-list=~s" lang-mime-list))
      (if (null? lang-mime-list)
          (cons (rdf:language->mime-type "RDF/XML")
                "RDF/XML")              ;explicit default language
          (let loop ((ml lang-mime-list))
            (if (null? ml)
                #f                      ;ooops
                (let ((lang-string (rdf:mime-type->language (car ml))))
                  (msglist "rdf:mime-type->language: ~s -> ~s"
                           (car ml) lang-string)
                  (if lang-string
                      (cons (car ml)
                            lang-string)
                      (loop (cdr ml)))))))))

  (case (length path-info-list)
    ((1 2)
     (let ((kb-name (car path-info-list))
           (submodel-name (if (null? (cdr path-info-list))
                               #f       ;no submodel
                               (cadr path-info-list))))
       (let ((kb (kb-get kb-name))
             (mime-and-lang (find-ok-language request))
             (query (string->symbol (or query-string "model"))))
         (cond ((and kb
                     (eq? query 'model)
                     mime-and-lang) ;normal case
                (set-http-response response '|SC_OK|)
                (set-content-type response
                                  (->jstring (car mime-and-lang)))
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
                           (msglist)))

               ((and kb
                     (eq? query 'metadata))
                (set-http-response response '|SC_OK|)
                (set-content-type response ;currently wrong: md is text
                                  (->jstring (car mime-and-lang)))
                (println (get-writer response)
                         (or (kb 'get-metadata-as-jstring)
                             (->jstring "")))
                #t)

               ((and kb
                     (eq? query 'sparql))
                (no-can-do response
                           '|SC_NOT_IMPLEMENTED|
                           "GET with query is not yet implemented"))

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

  (with-failure-continuation
       (make-fc request response '|SC_INTERNAL_SERVER_ERROR|)
    (lambda ()
      (define-generic-java-methods
        get-reader
        get-input-stream)
      (let ((path-list (request->path-list request))
            (query-string (request->query-string request)))
        (cond ((= (length path-list) 1)
               (manage-knowledgebase (car path-list)
                                     (get-reader request)
                                     (request->query-string request)
                                     response))

              ((= (length path-list) 2)
               (update-submodel (car path-list)
                                (cadr path-list)
                                (or (not query-string)
                                    (string=? query-string "tbox"))
                                (request->content-header-strings request)
                                (get-input-stream request)
                                response))

              (else                     ;ooops
               (let ()
                 (define-generic-java-method get-path-info)
                 (set-http-response response '|SC_BAD_REQUEST|)
                 (response-page
                  "Quaestor: bad request"
                  `((p "The request path "
                       (code ,(->string (get-path-info request)))
                       " has the wrong number of elements (1 or 2)"))))))))))

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

          ((and kb query)  ;unrecognised query
           (no-can-do response '|SC_BAD_REQUEST|
                      "Unrecognised query ~a?~a"
                      kb-name query))

          (kb              ;already exists
           (no-can-do response '|SC_FORBIDDEN| ;correct? or SC_CONFLICT?
                      "Knowledgebase ~a already exists"
                      kb-name))

          (query           ;no knowledgebase, but there is a query
           (no-can-do response '|SC_BAD_REQUEST|
                      "Knowledgebase ~a does not exist, so query ~a is not allowed"
                      kb-name query))

          (else            ;normal case
           (let ((nkb (kb:new kb-name))) ;normal case
             (nkb 'set-metadata
                  (reader->jstring reader))
             (set-http-response response '|SC_NO_CONTENT|))))))

;; Given a knowledgebase called KB-NAME, upload a RDF/XML submodel called
;; KB-NAME which is available from the given STREAM.  The CONTENT-HEADERS
;; are an alist (symbol . string), where the symbol is 'type, 'length, and
;; so on.
(define (update-submodel kb-name
                         submodel-name
                         tbox?
                         content-headers
                         stream
                         response)

  ;; Given an alist ALIST of (key . value) pairs, and a list
  ;; RECOGNISED of recognised symbols, return true if each of the alist
  ;; keys, which are all symbols, is a member of the list RECOGNISED.
  (define (check-cars alist recognised)
    (if (null? alist)
        #t
        (if (memq (caar alist) recognised)
            (check-cars (cdr alist) recognised)
            #f)))

  (let ((kb (kb:get kb-name))
        (ok-headers '(type length)))
    (cond ((not (check-cars content-headers ok-headers))
           (no-can-do response '|SC_NOT_IMPLEMENTED|
                      "PUT found content headers (~a), can only handle (~a)"
                      (map (lambda (h) (string-append "content-" (car h)))
                           content-headers)
                      (map (lambda (h) (string-append "content-" h))
                           ok-headers)))

          (kb                         ;normal case
           (let* ((rdf-mime (and (assq 'type content-headers)
                                 (cdr (assq 'type content-headers))))
                  (submodel (rdf:ingest-from-stream
                             stream
                             rdf-mime)))
             (msglist "content-headers=~s~%  rdf-mime=~s => lang=~s"
                      content-headers rdf-mime
                      (rdf:mime-type->language rdf-mime))
             (if submodel
                 (if (kb (if tbox? 'add-tbox 'add-abox)  ;normal case
                         submodel-name
                         submodel)
                     (set-http-response response '|SC_NO_CONTENT|)
                     (no-can-do response
                                '|SC_INTERNAL_ERROR| ;correct?
                                "Unable to update model!"))
                 (no-can-do response '|SC_BAD_REQUEST|
                            "Bad RDF MIME type! ~a~%~a"
                            rdf-mime (msglist)))))

          (else
           (no-can-do response '|SC_BAD_REQUEST|
                      (format #f "No such knowledgebase ~a"
                              kb-name))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; POST support

;; Handle POST requests.  Return #t on success, or a string response
(define (http-post request response)
  (let ((get-lazy-output-stream (make-lazy-output-stream response)))
    (with-failure-continuation
        (make-fc request response '|SC_INTERNAL_SERVER_ERROR|
                 get-lazy-output-stream)
      (lambda ()
        (define-generic-java-methods
          get-reader
          set-content-type)
        (if (java-null? response)        ;eh??!
            (no-can-do response
                       '|SC_BAD_REQUEST|
                       "null response!"))
        (let ((path-list (request->path-list request))
              (query-string (request->query-string request)))
          ;; First, insist that there's just one element in the path-list.
          ;; Check also that the content-type of the incoming SPARQL query is
          ;; application/sparql-query (http://www.w3.org/TR/rdf-sparql-query/)
          (if (= (length path-list) 1)
              (let ((kb (kb:get (car path-list))))
                (or kb
                    (error "Don't know about knowledgebase ~a" (car path-list)))
                (set-http-response response '|SC_OK|)
                (or (with-failure-continuation
                     (make-fc request response '|SC_BAD_REQUEST|)
                     (lambda ()
                       (sparql:perform-query
                        kb
                        (reader->jstring (get-reader request))
                        get-lazy-output-stream
                        (request->accept-mime-types request)
                        (lambda (mimetype)
                          (set-content-type response
                                            (->jstring mimetype))))
                       #t))
                    (no-can-do response
                               '|SC_BAD_REQUEST|
                               "Error performing SPARQL query")))
              (no-can-do response
                         '|SC_BAD_REQUEST|
                         "POST SPARQL request must have one path element, and query=sparql")))))))

;; Return a function which, when called, will return the response output stream.
;; This extracts the output stream lazily, so that we don't call
;; GET-OUTPUT-STREAM on the underlying response unless and until we need to,
;; thus leaving any error handler free to do so instead.
;; May be called multiple times (unlike GET-OUTPUT-STREAM).
(define (make-lazy-output-stream original-response)
  (define-generic-java-method
    get-output-stream)
  (let ((response original-response)
        (stream #f))
    (lambda ()
      (if (not stream)
          (set! stream (get-output-stream response)))
      stream)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; DELETE support

;; Handle DELETE requests.  Return #t on success, or a string response.
(define (http-delete request response)
  (with-failure-continuation
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

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; XML-RPC support

;; Handle a single XML-RPC request.  The procedure may read the body of the 
;; request from the given READER.  It should return a response as a string
;; containing XML, success or failure, but if anything unexpected happens,
;; it can throw an error.
(define (handle-xmlrpc reader)
  (sexp->xml
   (with/fc
      (lambda (m e)
        (xmlrpc:create-fault 0 "Malformed request: ~a" (error-message m)))
    (lambda ()
      (let ((call (xmlrpc:new-call reader)))
        (if (= (xmlrpc:number-of-params call) 1)
            (xmlrpc:create-response (format #f
                                            "Response: method name is ~a; param 1 is ~a"
                                            (xmlrpc:method-name call)
                                            (xmlrpc:method-param call 1)))
            (xmlrpc:create-fault 99 "Wrong number of parameters: ~a"
                                 (xmlrpc:number-of-params call))))))
   '(|methodResponse| fault params struct) ;make it look pretty
   '(param member)))

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

;; Extract the content-* headers from the REQUEST, and return them as an alist:
;; (name . value), where 'name' is the header name with 'content-' removed,
;; and 'value' is the value as a Scheme string.
(define (request->content-header-strings request)
  (filter (lambda (x) x)
          (map (lambda (p)
                 (let ((h (car p)))
                   (and (string-prefix? "content-" h)
                        (cons (string->symbol (substring h 8 (string-length h)))
                              (cdr p)))))
               (request->header-alist request))))

;; Return the set of request headers as an alist, each element of which is
;; of the form (header-string . value-string) (both scheme strings)
(define (request->header-alist request)
  (define-generic-java-methods
    get-header-names
    get-header)
  (map (lambda (header-jname)
         (cons (->string header-jname)
               (->string (get-header request header-jname))))
       (enumeration->list (get-header-names request))))

;; Return the contents of the Accept header as a list of scheme strings.
;; Each one is a MIME type.
(define (request->accept-mime-types request)
  (define-generic-java-method
    get-headers)
  (map ->string
       (enumeration->list (get-headers request (->jstring "accept")))))

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

;; Print the given response.  RESPONSE may be
;;     a string (it is to be printed; return #t)
;;     #t/#f    (print nothing; return #t/#f)
;; The SERVLET-RESPONSE is where the response is to go.
;; (define (print-http-response response servlet-response)
;;   (define-generic-java-methods
;;     println
;;     get-writer)
;;   (cond ((string? response)
;;          (println (get-writer servlet-response)
;;                   (->jstring response))
;;          #t)
;;         ((boolean? response)
;;          response)
;;         (else
;;          (error "print-http-response: illegal argument ~s" response))))

;; Given a RESPONSE, set the response status to the given RESPONSE-CODE,
;; and produce a status page using the given format and arguments.
;; This expects to be called before there has been any other output.
(define (no-can-do response response-code fmt . args)
  (let ((msg (apply format `(#f ,fmt ,@args))))
    (set-http-response response response-code)
    (response-page "Quaestor: no can do" `((p ,msg)))))

;; Make a SISC failure continuation.  Return a two-argument procedure
;; which can be used as the handler for with-failure-continuation.
;; See ERROR-WITH-STATUS for an error procedure which allows you to override
;; the status given here.  If GET-OUTPUT-STREAM is given, then the car of
;; it is a function which should be called to get the output stream, rather
;; than getting it from the RESPONSE.
(define (make-fc request response status . dummy)
  (define-generic-java-methods
    set-content-type)
  (lambda (error-record cont)
    (let ((msg-or-pair (error-message error-record)))
      (set-http-response response (if (pair? msg-or-pair)
                                      (car msg-or-pair)
                                      status))
      (set-content-type response (->jstring "text/plain"))
      (format #f "Internal server error~%~%Error: ~a~%~%Stack trace:~%~a~%"
              (if (pair? msg-or-pair)
                  (cdr msg-or-pair)
                  msg-or-pair)
              (with-output-to-string
                (lambda () (print-stack-trace cont)))))))
;; Following is better, because it uses the lazy output stream (like the
;; comments say).  But it doesn't appear to work.
(define (not-make-fc request response status . get-output-stream)
  (define-generic-java-methods
    set-content-type
    println
    get-writer)
  (define-java-class
    <java.io.print-writer>)
  (let ((get-output-writer (if (null? get-output-stream)
                               (lambda ()
                                 (get-writer response))
                               (lambda ()
                                 (java-new <java.io.print-writer>
                                           ((car get-output-stream)))))))
    (lambda (error-record cont)
      (let ((msg-or-pair (error-message error-record)))
        (if (java-null? response)
            (error 'make-fc "Arghhhh, response is null"))
        (set-http-response response (if (pair? msg-or-pair)
                                        (car msg-or-pair)
                                        status))

        (if #f
            (begin (set-content-type response (->jstring "text/html"))
                   (response-page "Internal server error"
                                  `((p (strong "Error: " ,(error-message error-record)))
                                    (h2 "Stack trace:")
                                    (pre ,(with-output-to-string
                                            (lambda ()
                                              (print-stack-trace cont))))
                                    ,@(tabulate-request-information request))))
            (let ((writer (get-output-writer)))
              (set-content-type response (->jstring "text/plain"))
              (println writer
                       (->jstring
                        (format #f "Internal server error~%~%Error: ~a~%~%Stack trace:~%~a~%"
                                (if (pair? msg-or-pair)
                                    (cdr msg-or-pair)
                                    msg-or-pair)
                                (with-output-to-string
                                  (lambda () (print-stack-trace cont))))))
              #f))))))

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
    (sexp->xml s)))

;; Set the HTTP response to the given value.  The RESPONSE-SYMBOL is
;; one of the SC_* fields in javax.servlet.http.HttpServletResponse.
;; Eg: (set-http-response response '|SC_OK|).
;; Returns #t for convenience, so that the above SC_OK call may (but
;; need not be) the final call in a handler function.
(define set-http-response
  (let ((response-object #f))
    (define-generic-java-method
      set-status)
    (lambda (response response-symbol)
      (if (not response-object)         ;first time
          (set! response-object (java-null
                                 (java-class
                                  '|javax.servlet.http.HttpServletResponse|))))
      (set-status response
                  ((generic-java-field-accessor response-symbol)
                   response-object))
      #t)))

;; Mostly for debugging.
;; Accumulate remarks, to supply later.
(define msglist
  (let ((l '()))
    (lambda msg
      (if (null? msg)
          (let ((r (reverse l)))
            (set! l '())
            (apply string-append r))
          (set! l
                (cons (apply format `(#f
                                      ,(string-append (car msg) "~%")
                                      ,@(cdr msg)))
                      l))))))
