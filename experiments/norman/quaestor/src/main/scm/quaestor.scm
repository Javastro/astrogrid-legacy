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
(define (get request response)
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
                 `((p "Details of Quaestor request")
                   ,@(tabulate-request-information request))))

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
    get-headers
    println)
  (define-java-classes
    <java.lang.string>)

  (define msgs
    (let ((msglist '()))
      (lambda msg
        (if (null? msg)
            (apply string-append (reverse msglist))
            (set! msglist (cons msg msglist))))))

  ;; Examine any Accept headers in the request.  Return #f if there were
  ;; Accept headers and we can't satisfy them;
  ;; return (mime-string . rdf-language) if we can satisfy a request;
  ;; return (mime-string . "RDF/XML") if there were no Accept headers.
  (define (find-ok-language rq)
    (let ((lang-mime-list
           (map ->string
                (enumeration->list (get-headers rq (->jstring "accept"))))))
      (msgs (format #f "lang-mime-list=~s~%" lang-mime-list))
      (if (null? lang-mime-list)
          (cons (rdf-language->mime-type "RDF/XML")
                "RDF/XML")              ;explicit default language
          (let loop ((ml lang-mime-list))
            (if (null? ml)
                #f                      ;ooops
                (let ((lang-string (mime-type->rdf-language (car ml))))
                  (msgs (format #f "mime-type->rdf-language: ~s -> ~s"
                                (car ml) lang-string))
                  (if lang-string
                      (cons (car ml)
                            lang-string)
                      (loop (cdr ml)))))))))

  (cond ((= (length path-info-list) 1)
         (let ((kb-name (car path-info-list)))
           (let ((kb (get-kb kb-name))
                 (mime-and-lang (find-ok-language request))
                 (query (string->symbol (or query-string "model"))))
             (cond ((and kb
                         (eq? query 'model)
                         mime-and-lang) ;normal case
                    (set-http-response response '|SC_OK|)
                    (set-content-type response
                                      (->jstring (car mime-and-lang)))
                    (write (kb 'get-model)
                           (get-output-stream response)
                           (->jstring (cdr mime-and-lang)))
                    #t)

                   ((and kb
                         (eq? query 'model))
                    (no-can-do response
                               '|SC_NOT_ACCEPTABLE|
                               "Cannot generate requested content-type:~%~a"
                               (msgs)))

                   ((and kb
                         (eq? query 'metadata))
                    (set-http-response response '|SC_OK|)
                    (set-content-type response ;currently wrong: md is text
                                      (->jstring (car mime-and-lang)))
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

        ((= (length path-info-list) 2)
         (no-can-do response
                    '|SC_NOT_IMPLEMENTED|
                    "Retrieval of submodel is disabled pending refactoring")
;;          (let ((kb-name (car path-info-list))
;;                (submodel-name (cadr path-info-list)))
;;            (let ((kb (get-kb kb-name)))
;;              (if kb
;;                  (let ((submodel (kb 'get-model submodel-name))
;;                        (lang-pair (find-ok-language request)))
;;                    (cond ((and submodel lang-pair)
;;                           (set-http-response response '|SC_OK|)
;;                           (write submodel
;;                                  (get-output-stream response)
;;                                  (->jstring (cdr lang-pair)))
;;                           #t)XXX
;;                        (no-can-do response
;;                                   '|SC_NOT_FOUND|
;;                                   (format #f "No such submodel ~a/~a"
;;                                           kb-name submodel-name))))
;;                  (no-can-do response
;;                             '|SC_NOT_FOUND|
;;                             (format #f "No such knowledgebase ~a"
;;                                     kb-name)))))
         )
        (else
         #f)))

(define get-handlers (list get-model
                           get-fallback))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; PUT support

;; Handle a single PUT request.  Examines the path-list from the
;; request, and either creates a new knowledgebase (if there is only
;; one element in the path), or creates or updates a submodel (if there
;; are two elements in the path).  It is an error to create a
;; knowledgebase where one of that name exists already.
(define (put request response)

  (with-failure-continuation
       (make-fc request response '|SC_INTERNAL_SERVER_ERROR|)
    (lambda ()
      (define-generic-java-methods
        get-reader
        get-input-stream)
      (let ((path-list (request->path-list request)))
        (cond ((= (length path-list) 1)
               (manage-knowledgebase (car path-list)
                                     (get-reader request)
                                     (request->query-string request)
                                     response))

              ((= (length path-list) 2)
               (update-submodel (car path-list)
                                (cadr path-list)
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
  (let ((kb (get-kb kb-name)))
    (cond ((and kb
                query
                (string=? query "metadata")) ;update metadata
           (kb 'set-metadata
               (reader->jstring reader))
           (set-http-response response '|SC_NO_CONTENT|)
           #t)

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
           (let ((nkb (new-kb kb-name))) ;normal case
             (nkb 'set-metadata
                  (reader->jstring reader))
             (set-http-response response '|SC_NO_CONTENT|)
             #t)))))

;; Given a knowledgebase called KB-NAME, upload a RDF/XML submodel called
;; KB-NAME which is available from the given STREAM.  The CONTENT-HEADERS
;; are an alist (symbol . string), where the symbol is 'type, 'length, and
;; so on.
(define (update-submodel kb-name submodel-name content-headers stream response)
  ;; Given an alist ALIST of (key . value) pairs, and a list
  ;; RECOGNISED of recognised symbols, return true if each of the alist
  ;; keys, which are all symbols, is a member of the list RECOGNISED.
  (define (check-cars alist recognised)
    (if (null? alist)
        #t
        (if (memq (caar alist) recognised)
            (check-cars (cdr alist) recognised)
            #f)))

  (let ((kb (get-kb kb-name))
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
                  (submodel (rdf-ingest-from-stream stream rdf-mime)))
             (if submodel
                 (begin (kb 'add-submodel  ;normal case
                            submodel-name
                            submodel)
                        (set-http-response response '|SC_NO_CONTENT|)
                        #t)
                 (no-can-do response '|SC_BAD_REQUEST|
                            "Bad RDF MIME type! ~a" mime))))

          (else
           (no-can-do response '|SC_BAD_REQUEST|
                      (format #f "No such knowledgebase ~a"
                              kb-name))))))

;; Given a RESPONSE, set the response status to the given RESPONSE-CODE,
;; and produce a status page using the given format and arguments.
(define (no-can-do response response-code fmt . args)
  (let ((msg (apply format `(#f ,fmt ,@args))))
    (set-http-response response response-code)
    (response-page "Quaestor: no can do" `((p ,msg)))))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Support functions for the HTTP transaction

;; Extract the path-list from the request, splitting it at '/', and
;; returning the result as a list of strings.
;; Any zero-length strings are removed (thus "/one//two/" is returned as
;; a two-element list).
(define (request->path-list request)
  (define-generic-java-methods
    get-path-info
    split
    (java-string-length |length|))
  (let ((path-string (get-path-info request)))
    (if (= (->number (java-string-length path-string)) 0)
        '()
        (remove (lambda (str) (= (string-length str) 0))
                (map ->string
                     (->list (split path-string (->jstring "/"))))))))
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
(define request->header-alist
  (let ((alist #f))
    (lambda (request)
      (define-generic-java-methods
        get-header-names
        get-header)
      (or alist
          (set! alist
                (map (lambda (header-jname)
                       (cons (->string header-jname)
                             (->string (get-header request header-jname))))
                     (enumeration->list (get-header-names request)))))
      alist)))

;; Given a READER, return a Java string containing the contents of the stream.
(define (reader->jstring reader)
  (define-java-classes
    <java.io.buffered-reader>
    (<stringbuffer> |java.lang.StringBuffer|))
  (define-generic-java-methods
    read-line
    append
    to-string)
  (let ((buffered-reader (java-new <java.io.buffered-reader> reader)))
    (let loop ((sb (java-new <stringbuffer>)))
      (let ((line (read-line buffered-reader)))
        (if (java-null? line)
            (to-string sb)
            (loop (append sb line)))))))

;; Print the given response.  RESPONSE may be
;;     a string (it is to be printed; return #t)
;;     #t/#f    (print nothing; return #t/#f)
;; The SERVLET-RESPONSE is where the response is to go.
(define (print-http-response response servlet-response)
  (define-generic-java-methods
    println
    get-writer)
  (cond ((string? response)
         (println (get-writer servlet-response)
                  (->jstring response))
         #t)
        ((boolean? response)
         response)
        (else
         (error "print-http-response: illegal argument ~s" response))))

;; Make a SISC failure continuation.  Return a two-argument procedure
;; which can be used as the handler for with-failure-continuation.
(define (make-fc request response status)
  (define-generic-java-methods
    set-content-type
    println
    get-writer)
  (lambda (error-record cont)
    (set-http-response response status)

;;     (set-content-type response (->jstring "text/html"))
;;     (response-page "Internal server error"
;;                    `((p (strong "Error: " ,(error-message error-record)))
;;                      (h2 "Stack trace:")
;;                      (pre ,(with-output-to-string
;;                              (lambda ()
;;                                (print-stack-trace cont))))
;;                      ,@(tabulate-request-information request)))
    (set-content-type response (->jstring "text/plain"))
    (format #f "Internal server error~%~%Error: ~a~%~%Stack trace:~%~a~%"
            (error-message error-record)
            (with-output-to-string
              (lambda () (print-stack-trace cont))))

;;     (set-content-type response (->jstring "text/plain"))
;;     (let ((w (get-writer response)))
;;       (println w
;;                (->jstring
;;                 (format #f "Internal server error~%Error: ~a~%~%Stack trace:~%~a~%"
;;                         (error-message error-record)
;;                         (with-output-to-string
;;                           (lambda ()
;;                             (print-stack-trace cont)))))))

    ))

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
;; Eg: (http-response '|SC_OK|)
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
                   response-object)))))


