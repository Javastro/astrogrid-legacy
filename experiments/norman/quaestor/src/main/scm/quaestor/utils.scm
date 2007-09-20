;; SISC library module for Quaestor
;;
;; The following are pretty generic support functions, which aren't
;; quite generic enough to live in ../util

(import s2j)
(import quaestor-support)

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
  input-stream->jstring
  parse-http-accept-header
  acceptable-mime
  parse-query-string

  ;; Transaction support functions.  Should these perhaps be moved
  ;; wholesale into scheme-wrapper-support.scm ?
  ;; (these functions currently have no test coverage)
  request->path-list
  request->query-string
  request->content-type
  webapp-base-from-request
  content-headers-ok?
  request->header-alist
  request->accept-mime-types
  response->lazy-output-stream
  no-can-do
  tabulate-request-information
  response-page
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

;(import debugging)                      ;for print-stack-trace



;; predicates used in contracts
(define-java-classes
  <java.lang.string>
  <java.io.reader>
  <java.io.input-stream>)
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
  (define-generic-java-method instance?)
  (cond ((java-null? jobject)
         #f)
        ((java-object? jobject)
         (->boolean
          (instance? (cond ((java-class? class)
                            class)
                           ((string? class)
                            (java-class (string->symbol class)))
                           ((symbol? class)
                            (java-class class))
                           (else
                            (error "Bad class in is-java-type?: ~s" class)))
                     jobject)))
        (else #f)))

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

;; INPUT-STREAM->JSTRING input-stream -> jstring
;;
;; Given a SOURCE which is a Java InputStream, return a Java string containing
;; the contents of the stream
(define/contract (input-stream->jstring
                  (source (is-java-type? source <java.io.input-stream>))
                  -> jstring?)
  (define-java-classes
    <java.io.input-stream-reader>)
  (reader->jstring (java-new <java.io.input-stream-reader> source)))

;; Parse an HTTP Accept header into a list of acceptable MIME types.
;; The JHEADER is a Java string in the format required by RFC 2616, that is,
;; as a comma-separated list of media-type tokens:
;;   media-type     = type "/" subtype *( ";" parameter )
;;   parameter      = attribute "=" value
;;   attribute      = token
;;   value          = token | quoted-string
;; See RFC 2616, section 14.1.
;; There's a good discussion, albeit in a Python context, at
;; <http://www.xml.com/pub/a/2005/06/08/restful.html>
;; The result is sorted in order of descending weight parameter, though
;; the sort isn't stable, so order of equally-weighted types isn't preserved.
;;
;; Types or subtypes equal to "*" sort lower than all other strings.
;; We ignore other parameters such as ";level=1" (but make sure they're
;; not errors).
(define parse-http-accept-header
  (let ((commas #f)
        (content-type #f))
    (define (non-null x)
      (and (not (java-null? x)) x))
    (define-generic-java-methods
      split
      compile
      matcher
      matches
      group)
    (define-java-classes
      <java.util.regex.pattern>)
    (lambda (jheader)
      (if (not commas)
          (begin (set! commas
                       (compile (java-null <java.util.regex.pattern>)
                                (->jstring " *, *")))
                 (set! content-type
                       (compile (java-null <java.util.regex.pattern>)
                                (->jstring "([^/]+)/([^;]+)(?:; *(?:q=([0-9.]+))?.*)?")))))
      (let ((key-and-mimes
             (remove
              (lambda (x) (not x))
              (map (lambda (js)
                     (let ((js-matcher (matcher content-type js)))
                       (if (->boolean (matches js-matcher))
                           (let ((type
                                  (->string (group js-matcher (->jint 1))))
                                 (subtype
                                  (->string (group js-matcher (->jint 2))))
                                 (qparam
                                  (non-null (group js-matcher (->jint 3)))))
                             `(#(,type ,subtype ,(if qparam
                                                     (string->number
                                                      (->string qparam))
                                                     1))
                               . ,(string-append type "/" subtype)))
                           #f       ; what's this? -- ignore it anyway
                           )))
                   (->list (split commas jheader))))))
;;         (format #t "header=~a -> mimes ~a~%"
;;                 (->string jheader)
;;                 key-and-mimes)
        (map cdr
             (sort-list key-and-mimes
                        (lambda (a b)
                          (let ((ta  (vector-ref (car a) 0))
                                (sta (vector-ref (car a) 1))
                                (qa  (vector-ref (car a) 2))
                                (tb  (vector-ref (car b) 0))
                                (stb (vector-ref (car b) 1))
                                (qb  (vector-ref (car b) 2)))
                            (cond ((not (= qa qb))
                                   (>= qa qb))
                                  ((string=? ta "*")
                                   #f)
                                  ((string=? tb "*")
                                   #t)
                                  ((string=? sta "*")
                                   #f)
                                  ((string=? stb "*")
                                   #t)
                                  (else #t))))))))))

;; ACCEPTABLE-MIME : string list-of-string -> string-or-#f
;; ACCEPTABLE-MIME : list-of-string list-of-string -> string-or-#f
;;
;; Test whether a given MIME type is acceptable.  Given a single
;; AVAILABLE MIME type,
;; return a MIME type which is present in the ACCEPTABLE (which may
;; include "*/*"), or #f if there is none such.  The return value will
;; typically be the same as the input MIME argument, unless this is
;; not acceptable. A non-#f return will be different from the input if
;; the input includes a wildcard as above.
;;
;; If AVAILABLE is a list of MIME types, then return the first element in
;; the ACCEPTABLE list (which should be in preference order, as returned
;; by PARSE-HTTP-ACCEPT-HEADER) which is present in the AVAILABLE list,
;; or #f if there are none.
;;
;; At present, */* is the only wildcard interpreted, so text/* for example
;; won't work.
(define/contract (acceptable-mime (available (or (string? available)
                                                 (list? available)))
                                  (acceptable list?)
                                  -> (lambda (ret)
                                       (or (string? ret)
                                           (not ret))))
  (define (one-mime-in-list available acceptable)
    (cond ((null? acceptable)
         #f)
        ((member "*/*" acceptable)
         available)
        ((member available acceptable)
         available)
        (else
         #f)))
  (define (first-in-list-in-list available acceptable)
    (let loop ((l acceptable))
      (cond ((null? l)
             #f)
            ((string=? (car l) "*/*")
             (car available))
            ((member (car l) available)
             => car)
            (else
             (loop (cdr l))))))
  (chatter "acceptable-mime: available=~s  acceptable=~s" available acceptable)
  (if (list? available)
      (first-in-list-in-list available acceptable)
      (one-mime-in-list available acceptable)))

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
;; argument is true, include the servlet path, without path info (producing for example
;; http://localhost:8080/quaestor/kb)
;;
;; This is surprisingly complicated, and the only way to do it appears to be to get
;; the request URL (getRequestURL -> StringBuffer) and then trim the servlet and path-info
;; strings from it.
;;
;; The one-arg form will have the same value for any request made to a given servlet.
(define (webapp-base-from-request request . with-servlet?)
  (define-generic-java-methods
    (get-request-url |getRequestURL|)
    get-servlet-path get-path-info
    to-string append delete length)
  (let ((req-url (get-request-url request)) ;a StringBuffer
        (servlet-path-length (->number (length (get-servlet-path request))))
        (path-info-length (let ((pi (get-path-info request)))
                            (if (java-null? pi) 0 (->number (length pi)))))
        (include-servlet-path? (and (not (null? with-servlet?))
                                    (car with-servlet?))))
    ;; getRequestURL returns the full reconstructed URL.  If the second argument is true,
    ;; then this is what we want; if it's false, then we have to remove from this the
    ;; stuff following .../quaestor, which is contained in getServletPath+getPathInfo.
    ;;(chatter "webapp-base-from-request: url=~s  servlet=~a  path=~a  include?=~a" req-url servlet-path-length path-info-length include-servlet-path?)
    (->string
     (to-string
      ;; yuk: the following is a mess, caused by having to convert between SISC and Java ints
      ;; in a very ugly way
      (if include-servlet-path?
          (delete req-url
                  (->jint (- (->number (length req-url))
                             path-info-length))
                  (length req-url))
          (delete req-url
                  (->jint (- (->number (length req-url))
                             servlet-path-length
                             path-info-length))
                  (length req-url)))))))

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
  ;; merge all the "accept" headers into a single comma-separated Java string
  (cond
   ((let loop ((headers
                (enumeration->list (get-headers request (->jstring "accept"))))
               (res #f))
      (if (null? headers)
          res
          (loop (cdr headers)
                (if res
                    (append (append res (->jstring ", "))
                            (car headers))
                    (car headers)))))
    => parse-http-accept-header)
   (else
    #f)))

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

;; For debugging and error reporting.  Given a request, produce a list
;; of sexps describing the content of the request.
(define (tabulate-request-information request)
  (define-generic-java-methods
    (get-request-uri |getRequestURI|)
    (get-request-url |getRequestURL|)
    get-servlet-path
    get-context-path
    get-path-info
    get-query-string
    get-method
    get-header
    get-header-names
    get-local-name
    get-local-port
    to-string)
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
                       ("request URL"  . ,(get-request-url request))
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

)
