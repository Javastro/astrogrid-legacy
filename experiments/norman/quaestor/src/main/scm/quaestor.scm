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
;(require-library 'quaestor/jena)
;(import jena)

(require-library 'sisc/libs/srfi/srfi-1)
(import* srfi-1
         remove)

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
    println)
  (define (no-can-do code msg)
    (set-http-response response code)
    (response-page "Quaestor: not found" `((p ,msg))))
  (cond ((= (length path-info-list) 1)
         (let ((kb-name (car path-info-list)))
           (let ((kb (get-kb kb-name)))
             (if kb                     ;normal case
                 (begin (set-http-response response '|SC_OK|)
                        (cond ((or (not query-string)
                                   (string=? query-string "model"))
                               (write (kb 'get-model)
                                      (get-output-stream response)))
                              ((string=? query-string "metadata")
                               (set-content-type response
                                                 (->jstring "text/plain"))
                               (println (get-writer response)
                                        (or (kb 'get-metadata-as-jstring)
                                            (->jstring ""))))
                              (else
                               (no-can-do '|SC_BAD_REQUEST|
                                          (format #f "bad query: ~a"
                                                  query-string)))))
                 (no-can-do '|SC_NOT_FOUND|
                            (format #f "No such knowledgebase: ~a" kb-name))))))
        ((= (length path-info-list) 2)
         (let ((kb-name (car path-info-list))
               (submodel-name (cadr path-info-list)))
           (let ((kb (get-kb kb-name)))
             (if kb
                 (let ((submodel (kb 'get-model submodel-name)))
                   (if submodel
                       (begin (set-http-response response '|SC_OK|)
                              (write submodel
                                     (get-output-stream response))
                              #t)
                       (no-can-do '|SC_NOT_FOUND|
                                  (format #f "No such submodel ~a/~a"
                                          kb-name submodel-name))))
                 (no-can-do '|SC_NOT_FOUND|
                            (format #f "No such knowledgebase ~a"
                                    kb-name))))))
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
  (define-generic-java-method
    get-reader)
  (with-failure-continuation
       (make-fc request response '|SC_INTERNAL_SERVER_ERROR|)
    (lambda ()
      (define (no-can-do response-code msg)
        (set-http-response response response-code)
        (response-page "Quaestor: no can do" `((p ,msg))))
      (let ((path-list (request->path-list request)))
        (cond ((= (length path-list) 1) ;create new knowledgebase
              ;; should this check that there's no content?  How?
               (let ((kb-name (car path-list)))
                 (if (get-kb kb-name)           ;already exists
                     (no-can-do '|SC_FORBIDDEN| ;correct? or SC_CONFLICT?
                                (format #f "Knowledgebase ~a already exists"
                                        kb-name))
                     (let ((kb (new-kb kb-name))) ;normal case
                       (kb 'set-metadata (reader->jstring (get-reader request)))
                       (set-http-response response '|SC_NO_CONTENT|)
                       #t))))

              ((= (length path-list) 2)
               ;; Upload a RDF/XML submodel
               (let ((kb-name (car path-list))
                     (submodel-name (cadr path-list)))
                 (let ((kb (get-kb kb-name)))
                   (define-generic-java-method
                     get-input-stream)
                   (if kb
                       (begin (kb 'add-submodel  ;normal case
                                  submodel-name
                                  (rdf-ingest-from-stream
                                   (get-input-stream request)))
                              (set-http-response response '|SC_NO_CONTENT|)
                              #t)
                       (no-can-do '|SC_BAD_REQUEST|
                                  (format #f "No such knowledgebase ~a"
                                          kb-name))))))

              (else                     ;ooops
               (let ()
                 (define-generic-java-method get-path-info)
                 (set-http-response response '|SC_BAD_REQUEST|)
                 (response-page
                  "Quaestor: bad request"
                  `((p "The request path "
                       (code ,(->string (get-path-info request)))
                       " has the wrong number of elements (1 or 2)"))))))))))

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

;; Extract the query-string from the request, returning it as a (scheme) string
(define (request->query-string request)
  (define-generic-java-method
    get-query-string)
  (let ((qs (get-query-string request)))
    (if (java-null? qs)
        #f
        (->string qs))))

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

    (set-content-type response (->jstring "text/html"))
    (response-page "Internal server error"
                   `((p (strong "Error: " ,(error-message error-record)))
                     (h2 "Stack trace:")
                     (pre ,(with-output-to-string
                             (lambda ()
                               (print-stack-trace cont))))
                     ,@(tabulate-request-information request)))

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


