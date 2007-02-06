;; SISC library module for Quaestor
;;
;; The following are pretty generic support functions, which aren't
;; quite generic enough to live in ../util

(import s2j)
(require-library 'sisc/libs/srfi/srfi-1)
(require-library 'sisc/libs/srfi/srfi-13)

(require-library 'util/lambda-contract)
(require-library 'util/sexp-xml)

(module utils
 (collection->list
  iterator->list
  jlist->list
  enumeration->list
  java-retrieve-static-object
  is-java-type?
  url-decode-to-jstring
  reader->jstring
  report-exception
  chatter
  parse-http-accept-header
  parse-query-string
  format-error-record

  ;; transaction support functions
  ;; (these functions currently have no test coverage)
  apply-with-top-fc
  request->path-list
  request->query-string
  request->content-type
  webapp-base-from-request
  content-headers-ok?
  request->header-alist
  request->accept-mime-types
  response->lazy-output-stream
  no-can-do
  make-fc
  tabulate-request-information
  response-page
  set-response-status!
  )

(import* srfi-1
         remove)
(import* srfi-13
         string-prefix?
         string-downcase
         string-index
         string-index-right)

(import* sexp-xml
         sexp-xml:sexp->xml)

(import debugging)                      ;for print-stack-trace



;; predicates used in contracts
(define-java-classes
  <java.lang.string>
  <java.io.reader>)
(define (jiterator? x)
  (is-java-type? x '|java.util.Iterator|))
(define (jlist? x)
  (is-java-type? x '|java.util.List|))
(define (jenumeration? x)
  (is-java-type? x '|java.util.Enumeration|))
(define (jstring? x)
  (is-java-type? x <java.lang.string>))
;; (define (jstring-or-null? x) ; null objects are also deemed true
;;   (or (java-null? x)
;;       (is-java-type? x <java.lang.string>)))


;; Given a Java colllection, extract its contents into a list.
;; Return the empty list if the input collection is (Java) null.
(define/contract (collection->list
                  (coll (or (java-null? coll)
                            (is-java-type? coll '|java.util.Collection|)))
                  -> list?)
  (define-generic-java-methods
    iterator)
  (if (java-null? coll)
      '()
      (iterator->list (iterator coll))))

;; Given a Java iterator, JITER, extract each of its contents into a list
(define/contract (iterator->list (jiter jiterator?) -> list?)
  (define-generic-java-methods
    (has-next? |hasNext|)
    next)
  (let loop ((l '()))
    (if (->boolean (has-next? jiter))
        (loop (cons (next jiter) l))
        (reverse l))))

;; The same, but taking a Java List as input
(define/contract (jlist->list (jlist jlist?) -> list?)
  (define-generic-java-method iterator)
  (iterator->list (iterator jlist)))

;; The same, but taking a Java Enumeration as input
(define/contract (enumeration->list (enum jenumeration?) -> list?)
  (define-generic-java-methods
    has-more-elements
    next-element)
  (if (->boolean (has-more-elements enum))
      (let ((n (next-element enum)))
        (cons n
              (enumeration->list enum)))
      '()))

 ;; Retrieve a Java static object by its full Java name, which may be a symbol
 ;; such as '|java.lang.System.out|, or a string
 (define (java-retrieve-static-object javaobj)
   (let ((javaobjname (if (string? javaobj)
                          javaobj
                          (symbol->string javaobj))))
     (let ((dot (string-index-right javaobjname #\.)))
       (and dot
            (let ((classname (substring javaobjname 0 dot))
                  (objname (substring javaobjname
                                      (+ 1 dot)
                                      (string-length javaobjname))))
              ((generic-java-field-accessor (string->symbol objname))
               (java-null (java-class (string->symbol classname)))))))))


 ;; Verifies that a JOBJECT is an instance of a Java class CLASS (which
 ;; can be a class, a string, or a symbol).  Returns true if so, #f it
 ;; it's not, or if it's not a Java object at all.
 (define (is-java-type? jobject class)
   (define-generic-java-method is-instance)
   (if (java-object? jobject)
       (->boolean
        (is-instance (cond ((java-class? class)
                            class)
                           ((string? class)
                            (java-class (string->symbol class)))
                           ((symbol? class)
                            (java-class class))
                           (else
                            (error "Bad class in is-java-type?: ~s" class)))
                     jobject))
       #f))

;; Given a string S, return the URL-decoded string as a jstring
(define/contract (url-decode-to-jstring (s string?) -> jstring?)
  (define-java-classes
    (<url-decoder> |java.net.URLDecoder|))
  (define-generic-java-method
    decode)
  (decode (java-null <url-decoder>)
          (->jstring s)
          (->jstring "UTF-8")))

;; READER->JSTRING reader -> jstring
;;
;; Given an SOURCE which is a Java Reader
;; return a Java string containing the contents of the stream.
(define/contract (reader->jstring
                  (source (is-java-type? source <java.io.reader>))
                  -> jstring?)
  (define-java-classes
    <java.io.buffered-reader>
    <java.lang.string-buffer>)
  (define-generic-java-methods
    read
    append
    to-string)
  (let ((buffered-reader (java-new <java.io.buffered-reader> source))
        (carr (java-array-new <jchar> 512))
        (zo (->jint 0)))
    (let loop ((sb (java-new <java.lang.string-buffer>)))
      (let ((rlen (read buffered-reader carr zo (->jint 512))))
        (if (< (->number rlen) 0)
            (to-string sb)
            (loop (append sb carr zo rlen)))))))

;; Variant of ERROR, which can be called in a region handled by the failure
;; continuation created by MAKE-FC.  Throw an error, in the given LOCATION,
;; with a message formatted with the given FMT and ARGS.  However instead
;; of exiting with the status code defaulted when the fc was created
;; by MAKE-FC, use the given NEW-STATUS.
;;
;; The returned `message' consists of a pair of integer status and the
;; message, and when the MAKE-FC handler processes this particular structure,
;; it will _not_ output debugging information.  That is,
;; this is for throwing `normal' errors.
(define (report-exception location new-status fmt . args)
  (let ((msg (apply format `(#f ,fmt ,@args))))
    (error location (cons new-status msg))))


;; Mostly for debugging.
;; Accumulate remarks, to supply later.
;;     (chatter fmt . args)  ; Format a message and return #t.
;;     (chatter)             ; return list of messages, or #f if none.
;;     (chatter #t)          ; ditto, but additionally clear the list.
(define chatter
  (let ()
    (define (make-circular-list n)
      (if (> n 0)
          (let* ((init (list #f))
                 (l (let loop ((ln (- n 1))
                               (res init))
                      (if (<= ln 0)
                          (set-cdr! init res)
                          (loop (- ln 1) (cons #f res))))))
            init)
          '()))
    (define (append-circular-list l x)
      (if (null? l)
          l
          (begin (set-car! l x)
                 (cdr l))))
    (define (reduce-circular-list kons init l)
      (if (not (null? l))
          (kons (car l)
                (let loop ((i (cdr l)))
                  (if (eqv? i l)
                      init
                      (kons (car i) (loop (cdr i))))))))

    (let ((chatter-list (make-circular-list 8)))
      (lambda msg
        (cond ((null? msg)                 ;retrieve messages
               (let ((r (reduce-circular-list (lambda (l r) (if l (cons l r) r))
                                              '()
                                              chatter-list)))
                 (and (not (null? r)) r)))  ;return list or #f
              ((boolean? (car msg))
               (let ((r (reduce-circular-list (lambda (l r) (if l (cons l r) r))
                                              '()
                                              chatter-list)))
                 (if (car msg)
                     (set! chatter-list (make-circular-list 8))) ; new clear list
                 (and (not (null? r)) r)))  ;return list or #f
              (else
               (set! chatter-list   ;append a new message
                     (append-circular-list chatter-list
                                           (apply format (cons #f msg))))
               #t))))))             ;return true

;; Format the given error record, as passed as the first argument of a
;; failure-continuation.  This ought to be able to handle most of the
;; odd things thrown by scheme and the s2j interface.  This should
;; generally return a string, but if all else fails it returns the
;; error record object.
(define (format-error-record rec)
  (define-java-classes
    (<throwable> |java.lang.reflect.UndeclaredThrowableException|)
    (<exception> |java.lang.Exception|))
  (define-generic-java-methods
    get-message
    get-undeclared-throwable
    to-string)
  ;; Return the concatenation of the error messages of this error and all its
  ;; ancestors.  If none of these have error messages, return #f.
  (define (format-error-ancestors rec)
    (and rec
         (let ((msg (error-message rec))
               (parent-msg (format-error-ancestors (error-parent-error rec))))
           (if msg                      ;found a message: return a string
               (format #f "error at ~a: ~a~a"
                       (error-location rec)
                       msg
                       (if parent-msg
                           (string-append " :-- " parent-msg)
                           ""))
               parent-msg))))
  (let ((msg (error-message rec)))
    (cond ((is-java-type? msg <throwable>)
           (get-message (get-undeclared-throwable msg)))
          ((is-java-type? msg <exception>)
           (to-string msg))
          ((format-error-ancestors rec))
          (else
           rec))))

 ;; Parse an HTTP Accept header into a list of acceptable MIME types.
 ;; The JHEADER is a Java string in the format required by RFC 2616, that is,
 ;; as a comma-separated list of media-type tokens:
 ;;   media-type     = type "/" subtype *( ";" parameter )
 ;;   parameter      = attribute "=" value
 ;;   attribute      = token
 ;;   value          = token | quoted-string
 ;; There's a good discussion, albeit in a Python context, at
 ;; <http://www.xml.com/pub/a/2005/06/08/restful.html>
 ;; The result is sorted in order of descending weight parameter, though
 ;; the sort isn't stable, so order of equally-weighted types isn't preserved.
 (define parse-http-accept-header
   (let ((commas #f)
         (parameters #f))
     (define-generic-java-methods
       split
       compile
       matcher
       matches
       group
       parse-float)
     (define-java-classes
       <java.util.regex.pattern>
       <java.lang.float>)
     (lambda (jheader)
       (if (not commas)
           (begin (set! commas
                        (compile (java-null <java.util.regex.pattern>)
                                 (->jstring " *, *")))
                  (set! parameters
                        (compile (java-null <java.util.regex.pattern>)
                                 (->jstring "\([^;]*\); *q=\([0-9.]+\).*")))))
       (let ((pairs (map (lambda (js)
                           (let ((js-matcher (matcher parameters js)))
                             (if (->boolean (matches js-matcher))
                                 (cons (->string
                                        (group js-matcher (->jint 1)))
                                       (string->number
                                        (->string
                                         (group js-matcher (->jint 2)))))
                                 (cons (->string js) 1))))
                         (->list (split commas jheader)))))
         (remove (lambda (s) (= (string-length s) 0))
                 (map car
                      (sort-list pairs
                                 (lambda (a b)
                                   (> (cdr a) (cdr b))))))))))

 ;; Simple implementation of heapsort (?).  Probably not massively efficient
 ;; but there isn't a SRFI for sorting yet.  Sort the given list L with
 ;; respect to the given sorting function, <=.
 ;;
 ;; NOTE: this isn't a stable sort.
 (define (sort-list l <=)
   (define (merge-lists ina inb)
     (let loop ((res '())
                (a ina)
                (b inb))
       (cond
        ((null? a)
         (append (reverse res) b))
        ((null? b)
         (append (reverse res) a))
        (else
         (if (<= (car a) (car b))
             (loop (cons (car a) res)
                   (cdr a)
                   b)
             (loop (cons (car b) res)
                   a
                   (cdr b)))))))

   (define (partition-list pe inl)
     (let loop ((pa '())
                (pb '())
                (l inl))
       (if (null? l)
           (values pa pb)
           (if (<= (car l) pe)
               (loop (cons (car l) pa) pb (cdr l))
               (loop pa (cons (car l) pb) (cdr l))))))

   (case (length l)
     ((0 1) l)
     ((2) (if (<= (car l) (cadr l))
              l
              (list (cadr l) (car l))))
     (else
      (let ((pe (car l)))
        (call-with-values
            (lambda () (partition-list pe (cdr l)))
          (lambda (left right)
            (merge-lists (sort-list left <=)
                         (cons pe (sort-list right <=)))))))))

;; parse-query-string string -> pair-of-strings
;;
;; Given a query string "keyword=value", parse it into a pair of strings
;; representing the text before and after the equals sign.  If either
;; is missing, replace it with #f.  If the query is #f, return (#f . #f)
;;
;; Very simple-minded.  Query strings can be more generic than this,
;; but they're not, in this application.
(define/contract (parse-query-string (qs (or (not qs) (string? qs)))
                                     -> pair?)
  (cond ((not qs)
         '(#f . #f))
        ((string-index qs #\=)
         => (lambda (index)
              (let ((k (substring qs 0 index))
                    (v (substring qs (+ index 1) (string-length qs))))
                (cons (if (> (string-length k) 0)
                          k
                          #f)
                      (if (> (string-length v) 0)
                          v
                          #f)))))
        ((= (string-length qs) 0)
         '(#f . #f))
        (else
         (cons qs #f))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Support functions for the HTTP transaction

;; APPLY-WITH-TOP-FC procedure args... -> object
;; This is intended to be a main entry point for the functions here.
;; Call the given procedure with the given arguments, in the context
;; of a suitable failure-continuation.
;;
;; Since the functions which are called via this typically have their
;; own error handlers, this failure-continuation will generally only
;; be called if something quite bad has gone wrong; therefore the
;; failure-continuation shows debugging information and chatter, and
;; returns a Java excaption.
(define (apply-with-top-fc proc . args)
  (with/fc
      (lambda (m kontinuation)
        (define-java-class <javax.servlet.servlet-exception>)
        (java-new
         <javax.servlet.servlet-exception>
         (->jstring
          (format #f "Top-level error: ~a~%~a~%~%Stack trace: ~a~%"
                  (format-error-record m)
                  (let ((c (chatter)))
                    (cond ((list? c)    ;normal case
                           (apply string-append
                                  (map (lambda (x)
                                         (format #f "[chatter: ~a]~%" x))
                                       c)))
                          ((not c)      ;no chatter
                           "")
                          (else         ;can't happen!
                           (format #f
                                   "[Can't happen: (chatter) produced ~s]" c))))
                  (with-output-to-string
                    (lambda () (print-stack-trace kontinuation)))))))
    (lambda ()
      (chatter "Applying proc ~s..." proc)
      (parameterize ((suppressed-stack-trace-source-kinds '()))
                    (apply proc args)))))

;; Extract the path-list from the request, splitting it at '/', and
;; returning the result as a list of strings.  If there was no path-info,
;; return an empty list.
;; Any zero-length strings are removed (thus "/one//two/" is returned as
;; a two-element list).
(define/contract (request->path-list (request request?) -> list?)
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
;; string, or #f if there is no query, or if the query is the empty string.
(define/contract (request->query-string (request request?) -> string-or-false?)
  (define-generic-java-methods
    get-query-string
    length)
  (let ((qs (get-query-string request)))
    (cond ((java-null? qs)
           #f)
          ((= (->number (length qs)) 0)
           #f)
          (else
           (->string qs)))))

;; request->content-type java-request -> string-or-false
;;
;; Given a Java REQUEST, return the request content type as a scheme string,
;; or #f if it is not available
(define/contract (request->content-type (request request?) -> string-or-false?)
  (define-generic-java-method get-content-type)
  (let ((content-jstring (get-content-type request)))
    (if (java-null? content-jstring)
        #f
        (->string content-jstring))))

;; webapp-base-from-request java-request optional-bool -> string
;;
;; Given a Java REQUEST, return the URL for the webapp
;; (for example http://localhost:8080/quaestor).  If the optional boolean
;; argument is true, include the servlet path (producing for example
;; http://localhost:8080/quaestor/kb)
;;
;; This is different from request.getRequestURI(), as this synthesises
;; the base URI, and so (a) is independent of the servlet invoked, and
;; (b) avoids issues with trailing slashes and so on.  Property (a) means
;; that, although it is derived from a request, the one-arg form will give
;; the same value for any request made by a given servlet.
(define (webapp-base-from-request request . with-servlet?)
  (define-generic-java-methods
    get-local-name get-local-port get-context-path get-servlet-path)
  (format #f "http://~a:~a~a~a"
          (->string (get-local-name request))
          (->number (get-local-port request))
          (->string (get-context-path request))
          (if (or (null? with-servlet?)
                  (not (car with-servlet?)))
              ""
              (->string (get-servlet-path request)))))

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
(define/contract (request->header-alist (request request?) -> list?)
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
(define (no-can-do request response response-code fmt . args)
  (let ((msg (apply format `(#f ,fmt ,@args))))
    (set-response-status! response response-code "text/html")
    (response-page request response
                   "Quaestor: no can do" `((p ,msg)))))

;; make-fc java-request java-response symbol -> procedure
;;
;; Make a SISC failure continuation.  Return a two-argument procedure
;; which can be used as the handler for with-failure-continuation.
;; See REPORT-EXCEPTION for an error procedure which allows you to override
;; the status given here.
(define/contract (make-fc (request java-object?)
                          (response java-object?)
                          (status symbol?)
                          -> procedure?)
  (lambda (error-record cont)
    (let* ((msg-or-pair (error-message error-record))
           (show-debugging? (not (pair? msg-or-pair))))
      (set-response-status! response
                          (if (pair? msg-or-pair)
                              (car msg-or-pair)
                              status)
                          "text/plain")
      (if show-debugging?
          (format #f "~%Error: ~a~%~a~%~%Stack trace:~%~a~%"
                  (format-error-record error-record)
                  (let ((c (chatter)))
                    (cond ((list? c)    ;normal case
                           (apply string-append
                                  (map (lambda (x)
                                         (format #f "[chatter: ~a]~%" x))
                                       c)))
                          ((not c)      ;no chatter
                           "")
                          (else         ;can't happen!
                           (format #f
                                   "[Can't happen: (chatter) produced ~s]" c))))
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
    get-header-names
    get-local-name
    get-local-port)
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
                       ("query string" . ,(get-query-string request))
                       ("local name"   . ,(get-local-name request))
                       ("local port"   . ,(get-local-port request)))))

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
(define (response-page request response title-string body-sexp)
  (define-generic-java-method get-context-path)
  (let ((s `(html (head (title ,title-string)
                          (link (@ (rel stylesheet)
                                   (type text/css)
                                   (href ,(string-append
                                           (->string (get-context-path request))
                                           "/base.css")))))
                    (body (h1 ,title-string)
                          ,@body-sexp))))
    (sexp-xml:sexp->xml s)))

;; set-response-status! java-response symbol -> #t
;; set-response-status! java-response symbol string -> #t
;; side-effect: set the java-response's status and optionally MIME type
;;
;; Set the HTTP response to the given value.  The RESPONSE-SYMBOL is
;; one of the SC_* fields in javax.servlet.http.HttpServletResponse.
;; Eg: (set-http-response response '|SC_OK|).
;; If MIME-TYPE-L is non-null, then its car is a Scheme string representing
;; the MIME type which should be set on the response.
;; Returns #t for convenience, so that the call to this procedure may (but
;; need not be) the final call in a handler function.
(define set-response-status!
  (let ((response-object #f))
    (define-generic-java-methods
      set-status
      set-content-type)
    (lambda (response response-symbol . mime-type-l)
      (or response-object
          (set! response-object (java-null
                                 (java-class
                                  '|javax.servlet.http.HttpServletResponse|))))
      (set-status response
                  ((generic-java-field-accessor response-symbol)
                   response-object))
      (or (null? mime-type-l)
          (set-content-type response (->jstring (car mime-type-l))))
      #t)))

)
