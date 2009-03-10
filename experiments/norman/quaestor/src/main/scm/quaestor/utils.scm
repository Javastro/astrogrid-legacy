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
 (jobject->list
  java-retrieve-static-object
  is-java-type?
  java-class-present
  url-decode-to-jstring
  reader->jstring
  input-stream->jstring
  parse-http-accept-header
  acceptable-mime
  parse-query-string
  string-split
  sort-list

  ;; Transaction support functions.  Should these perhaps be moved
  ;; wholesale into scheme-wrapper-support.scm ?
  ;; (these functions currently have no test coverage)
  service-http-call/checking
  service-http-call
  request->path-list
  request->query-string
  request->content-type
  webapp-base-from-request
  content-headers-ok?
  request->header-alist
  request->accept-mime-types
  no-can-do
  tabulate-request-information
  response-page
  )


(import* sexp-xml
         sexp-xml:sexp->xml
         sexp-xml:escape-string-for-xml)
(import* srfi-1
         remove
         filter-map)
(import* srfi-13
         string-prefix?
         string-downcase
         string-index
         string-index-right
         string-null?)

;(import debugging)                      ;for print-stack-trace

(define-syntax unless
  (syntax-rules ()
    ((_ test form)
     (if (not test) form))))

;; predicates used in contracts
(define-java-classes
  <java.lang.string>
  <java.io.reader>
  <java.io.input-stream>)
;; (define (jiterator? x)
;;   (is-java-type? x '|java.util.Iterator|))
;; (define (jlist? x)
;;   (is-java-type? x '|java.util.List|))
;; (define (jenumeration? x)
;;   (is-java-type? x '|java.util.Enumeration|))
(define (jstring? x)
  (is-java-type? x <java.lang.string>))

;; JOBJECT->LIST : thing -> list
;; For a variety of Java objects (collection, iterator, List, Enumeration),
;; convert it to a list.  Return an empty list if the input is (Java) null.
(define/contract (jobject->list jobject -> list?)
  (define (collection->list coll)
    (define-generic-java-methods
      iterator)
    (if (java-null? coll)
        '()
        (iterator->list (iterator coll))))
  ;; Given a Java iterator, JITER, extract each of its contents into a list
  (define (iterator->list jiter)
    (define-generic-java-methods
      (has-next? |hasNext|)
      next)
    (let loop ((l '()))
      (if (->boolean (has-next? jiter))
          (loop (cons (next jiter) l))
          (reverse l))))
  ;; The same, but taking a Java List as input
  (define (jlist->list jlist)
    (define-generic-java-method iterator)
    (iterator->list (iterator jlist)))
  ;; The same, but taking a Java Enumeration as input
  (define (enumeration->list enum)
    (define-generic-java-methods
      has-more-elements
      next-element)
    (if (->boolean (has-more-elements enum))
        (let ((n (next-element enum)))
          (cons n
                (enumeration->list enum)))
        '()))

  (cond ((java-null? jobject)
         '())
        ((is-java-type? jobject '|java.util.Collection|)
         (collection->list jobject))
        ((is-java-type? jobject '|java.util.Iterator|)
         (iterator->list jobject))
        ((is-java-type? jobject '|java.util.List|)
         (jlist->list jobject))
        ((is-java-type? jobject '|java.util.Enumeration|)
         (enumeration->list jobject))
        (else
         (error "I don't know how to convert an object like ~s to a list" jobject))))

;; JAVA-RETRIEVE-STATIC-OBJECT : string -> <jobject>
;; JAVA-RETRIEVE-STATIC-OBJECT : symbol -> <jobject>
;; JAVA-RETRIEVE-STATIC-OBJECT : symbol symbol -> <jobject>
;; Retrieve a Java static object by its full Java name, which may be a
;; symbol such as '|java.lang.System.out|,
;; or a string "java.lang.System.out",
;; or two symbols '|java.lang.System| and '|out|
(define (java-retrieve-static-object x . args)
   (cond ((null? args)
          (let ((javaobjname (if (string? x)
                                 x
                                 (symbol->string x))))
            (let ((dot (string-index-right javaobjname #\.)))
              (and dot
                   (let ((classname (substring javaobjname 0 dot))
                         (objname (substring javaobjname
                                             (+ 1 dot)
                                             (string-length javaobjname))))
                     ((generic-java-field-accessor (string->symbol objname))
                      (java-null (java-class (string->symbol classname)))))))))
         ((and (symbol? x) (symbol? (car args)))
          ((generic-java-field-accessor (car args)) (java-null (java-class x))))
         (else
          (error "Bad arguments to java-retrieve-static-object: ~s ~s" x args))))

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

;; JAVA-CLASS-PRESENT : symbol -> boolean
;; Return #t if the named class is present in the classpath, and #f otherwise
(define (java-class-present class-name)
  (with/fc (lambda (m e) #f)
    (lambda ()
      (java-class class-name) ;throws an exception if this isn't in the classpath
      #t)))

;; ->STRING-OR-EMPTY : jstring -> string
;; Like ->STRING, except that it returns "" if the input string is null
(define (->string-or-empty js)
  (if (java-null? js)
      ""
      (->string js)))

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
;;
;; If the value is not a syntactically valid MIME type, we ignore it rather than 
;; producing an error.
(define parse-http-accept-header
  (let ()
    (define-generic-java-methods compile split matcher matches group)
    (define-java-classes <java.util.regex.pattern>)
    (let ((commas (compile (java-null <java.util.regex.pattern>)
                           (->jstring ",")))
          (content-type (compile (java-null <java.util.regex.pattern>)
                                 (->jstring " *([^/]+)/([^; ]+)(?:; *(?:q=([0-9.]+))?.*)? *"))))
      (lambda/contract ((jheader jstring?) -> (lambda (res) (or (not res) (list? res))))
        (let ((key-and-mimes
               (remove
                (lambda (x) (not x))
                (map (lambda (js)
                       (let ((js-matcher (matcher content-type js)))
                         (if (->boolean (matches js-matcher))
                             (let ((type    (->string (group js-matcher (->jint 1))))
                                   (subtype (->string (group js-matcher (->jint 2))))
                                   (qparam  (group js-matcher (->jint 3))))
                               `(#(,type ,subtype ,(if (java-null? qparam)
                                                       1
                                                       (string->number (->string qparam))))
                                 . ,(string-append type "/" subtype)))
                             #f     ; what's this? -- ignore it anyway
                             )))
                     (->list (split commas jheader))))))
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
                                    (else #t)))))))))))

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

;; PARSE-QUERY-STRING : string -> (listof (symbol . string))
;;
;; Given a query string "keyword=value&...", parse it into a list of pairs,
;; each of which has the query parameter in its car, and the value in its cdr.
;; Skip empty or malformed (no keyword) queries; "keyword" or "keyword=" both parse
;; OK as having no value
(define/contract (parse-query-string (qs (or (not qs) (string? qs))) -> list?)
  (if (not qs)
      '()
      (filter-map (lambda (v)
                    (cond ((string-index v #\=)
                           => (lambda (index)
                                (and (> index 0)
                                     (cons (string->symbol (substring v 0 index))
                                           (substring v (+ index 1) (string-length v))))))
                          (else (cons (string->symbol v) ""))))
                  (string-split qs #\&))))

;; STRING-SPLIT : string char [number] -> (listof string)
;; The function unaccountably not in srfi-13.  Split the string at occurrences of the
;; char, skipping empty strings.  If present, the number indicates where to start searching.
(define (string-split s ch . start-arg)
  (let ((start (if (null? start-arg) 0 (car start-arg)))
        (len (string-length s)))
    (cond ((>= start len)
           '())
          ((string-index s ch start)
           => (lambda (i)
                (if (= i start)
                    (string-split s ch (+ start 1))
                    (cons (substring s start i)
                          (string-split s ch (+ i 1))))))
          (else
           (list (substring s start len))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Support functions for the HTTP transaction


;; SERVICE-HTTP-CALL : <request> <response> procedure -> string or #t
;;
;; The main handler, which wraps the per-method handlers.  Each
;; method-handler must satisfy the contract
;;    handler : <request> -> list
;; The list is a collection of items which control the response, and
;; may be composed of:
;;    symbol          : must be one of the symbols representing status
;;                      codes in the javax.servlet.http.HttpServletResponse class
;;    procedure       : (procedure : <output-stream> (string -> void) -> void).
;;                      When called, this procedure calls the second
;;                      argument with the required mime-type as argument,
;;                      then writes its output to the given output-stream.
;;    (string . sexp) : Generate an HTML response page with the first
;;                      string as title, and the sexp as the page body.
;;    (symbol . sexp) : Generate an XML page
;;    string          : set the content-type
;;    (string . string) : set an extra response header

;; This version calls a checker internally, to assert that what it's about
;; to return conforms to what the WADL says.
;; The RESPONSE-MATCHES-WADL? argument is either #f or a procedure
;;   response-matches-wadl? : method:symbol
;;                            request-path:(listof string)
;;                            status:symbol
;;                            content-type:string
;;                            output:string-or-procedure
;;                            headers:alist
;;                            -> void
;; which takes the details about to be returned and throws an error if they're not appropriate.
(define (service-http-call/checking request response method-handler response-matches-wadl?)
  (define (set-http-response-status! status)
    (define-generic-java-methods set-status)
    (set-status response (java-retrieve-static-object '|javax.servlet.http.HttpServletResponse| status)))
  (define-generic-java-methods
    get-output-stream set-content-type set-header
    get-method get-servlet-path)

  ;; CALL-HANDLER-AND-RETURN-RESPONSE : -> symbol string string-or-procedure alist
  ;; Call the METHOD-HANDLER function, with the REQUEST as argument.
  ;; This returns a list, as discussed above: process this list, and
  ;; return the status, content-type, output and extra headers as
  ;; multiple values.
  (define (call-handler-and-return-response)
    (let loop ((resp (with/fc
                         (lambda (error-record cont)
                           ;; Throw a nested error clearly blaming the method handler
                           (let ((msg-or-pair (error-message error-record)))
                             (throw
                              (make-nested-error
                               (make-error "Error calling the method handler: ~a"
                                           (if (pair? msg-or-pair) (cdr msg-or-pair) msg-or-pair))
                               error-record cont))))
                       (lambda ()
                         (method-handler request))))
               (status #f)              ;symbol
               (content-type #f)        ;string
               (the-output #f)              ;sexp or procedure
               (headers '()))           ;alist
      (chatter "resp=~s" resp)
      (cond ((null? resp)
             ;; we're finished
             (values status content-type the-output headers))

            ((symbol? (car resp))
             ;; response status
             (loop (cdr resp)
                   (car resp)
                   content-type
                   the-output
                   headers))

            ((procedure? (car resp))
             ;; output procedure
             (if the-output
                 (error "request tried to return two outputs!"))
             (loop (cdr resp)
                   status
                   content-type
                   (car resp)           ;new output
                   headers))

            ((and (list? (car resp))
                  (string? (caar resp)))
             ;; sexp with leading string -- generate a response-page
             (if the-output
                 (error "request tried to return two outputs!"))
             (loop (cdr resp)
                   status
                   "text/html;charset=utf-8" ;new content-type
                   (sexp-xml:sexp->xml (response-page request (caar resp) (cdar resp))) ;new output
                   headers))

            ((and (list? (car resp))
                  (symbol? (caar resp)))
             ;;sexp -- generate output
             (if the-output
                 (error "request tried to return two outputs!"))
             (loop (cdr resp)
                   status
                   (or content-type
                       "text/html;charset=utf-8")  ;new content-type
                   (sexp-xml:sexp->xml (car resp)) ;new output
                   headers))

            ((string? (car resp))
             ;; content type
             (loop (cdr resp)
                   status
                   (car resp)           ;new content-type
                   the-output
                   headers))

            ((and (pair? (car resp))
                  (string? (caar resp))
                  (string? (cdar resp)))
             ;; pair of strings -- set an extra response header
             (loop (cdr resp)
                   status
                   content-type
                   the-output
                   (cons (car resp) headers))) ;new header)

            (else
             ;; ooops
             (report-exception 'service-http-call
                               '|SC_INTERNAL_SERVER-ERROR|
                               "Unexpected response from servlet: servlet ~s produced ~s"
                               method-handler
                               (sexp-xml:escape-string-for-xml (format #f "~s" resp)))))))

  (set-http-response-status! '|SC_INTERNAL_SERVER_ERROR|)
  (with/fc
      (lambda (error-record cont)
        (set-http-response-status! '|SC_INTERNAL_SERVER_ERROR|)
        (set-content-type response (->jstring "text/html"))
        (sexp-xml:sexp->xml
         (response-page request
                        "Ooops"
                        `((p "Error in service-http-call/checking.  That wasn't supposed to happen")
                          (p "Error was " (em ,(format-error-record error-record)))
                          (p "Recent chatter:")
                          (ul ,@(map (lambda (x) `(li ,(sexp-xml:escape-string-for-xml x)))
                                     (chatter)))
;;                           (pre ,(sexp-xml:escape-string-for-xml
;;                                  (with-output-to-string (lambda () (print-stack-trace cont)))))
                          ))))
    (lambda ()
      ;;       (receive (status content-type handler-output headers)
      ;;           (call-handler-and-return-response)
      ;; hmmmmm, RECEIVE seemed not to work here, so do it by hand
      ;; (peculiar -- no time to worry about it now...)
      (call-with-values call-handler-and-return-response
        (lambda (status-symbol content-type handler-output headers)
          (if response-matches-wadl?
              ;; Here check that the response is one documented by the WADL file.
              ;; Note that if the OUTPUT is a procedure, then we can't check its content-type.
              ;; Perhaps rework this part of the interface, to avoid the second argument to
              ;; the output procedure, and so oblige this to commit to a content-type beforehand.
              (let ((method (string->symbol (string-downcase (->string (get-method request)))))
                    (request-path (cons (let ((s (->string (get-servlet-path request))))
                                          (substring s 1 (string-length s))) ; hacky: presumes that the servlet is of the form '/foo'
                                        (request->path-list request)))
                    (status-number (->number (java-retrieve-static-object '|javax.servlet.http.HttpServletResponse| status-symbol)))
                    (request-query-params (cond ((request->query-string request)
                                                 => (lambda (qs)
                                                      (map car (parse-query-string qs))))
                                                (else #f))))
                (unless (response-matches-wadl? method request-path
                                                status-number content-type handler-output headers)
                        (error 'service-http-call
                               "Request ~a ~a produced undocumented response: status=~a content-type=~a"
                               method
                               request-path
                               status-number
                               content-type))))

          (set-http-response-status! status-symbol)
          (if content-type
              (set-content-type response (->jstring content-type)))
          (for-each (lambda (header-pair)
                      (set-header response (->jstring (car header-pair)) (->jstring (cdr header-pair))))
                    headers)
          (cond ((not handler-output)
                 #t)
                ((string? handler-output)
                 handler-output)
                ((procedure? handler-output)
                 (handler-output (get-output-stream response)
                                 (lambda (mime-type)
                                   (set-content-type response (->jstring mime-type))))
                 #t)                    ;produce no further output
                (else
                 (error 'service-http-call "Impossible output: ~s" handler-output))))))))

;; the same, except that it doesn't call the checking routine,
;; and (consequently) has a simpler loop
(define (service-http-call request response method-handler)
  (define set-http-response-status!
    (let ((http-servlet-response (java-null (java-class '|javax.servlet.http.HttpServletResponse|))))
      (define-generic-java-methods set-status)
      (lambda (response status)
        (set-status response ((generic-java-field-accessor status) http-servlet-response)))))
  (define-generic-java-methods
    get-output-stream set-content-type set-header)

  (set-http-response-status! response '|SC_INTERNAL_SERVER_ERROR|)
  (with/fc
      (lambda (error-record cont)
        (sexp-xml:sexp->xml
         (response-page request
                        "Ooops"
                        `((p "Error in service-http-call.  That wasn't supposed to happen")
                          (p "Error was " (em ,(format-error-record error-record)))
                          (p "Recent chatter:")
                          (ul ,@(map (lambda (x) `(li ,(sexp-xml:escape-string-for-xml x)))
                                     (chatter)))))))
    (lambda ()
      (let loop ((resp (with/fc
                           (make-fc '|SC_INTERNAL_SERVER_ERROR|)
                         (lambda ()
                           (method-handler request))))
                 (seen-status? #f)
                 (seen-content-type? #f))
        (chatter "resp=~s" resp)
        (cond ((null? resp)
               ;; we're finished, and there's been no output -- return quietly
               #t)

              ((symbol? (car resp))
               ;; set the response status
               (set-http-response-status! response (car resp))
               (loop (cdr resp) #t seen-content-type?))

              ((procedure? (car resp))
               ;; generate output, and finish
               ((car resp)
                (get-output-stream response)
                (lambda (mime-type)
                  (set-content-type response (->jstring mime-type))))
               #t)                      ;produce no further output

              ((and (list? (car resp))
                    (string? (caar resp)))
               ;; sexp with leading string -- generate a response-page, and finish
               (set-content-type response (->jstring "text/html;charset=utf-8"))
               (sexp-xml:sexp->xml (response-page request (caar resp) (cdar resp))))

              ((and (list? (car resp))
                    (symbol? (caar resp)))
               ;;sexp -- generate output, and finish
               (or seen-content-type?
                   (set-content-type response (->jstring "text/html;charset=utf-8")))
               (sexp-xml:sexp->xml (car resp)))

              ((string? (car resp))
               ;; set the content type
               (set-content-type response (->jstring (car resp)))
               (loop (cdr resp) seen-status? #t))

              ((and (pair? (car resp))
                    (string? (caar resp))
                    (string? (cdar resp)))
               ;; pair of strings -- set an extra response header
               (set-header response (->jstring (caar resp)) (->jstring (cdar resp)))
               (loop (cdr resp) seen-status? seen-content-type?))

              (else
               ;; ooops
               (set-http-response-status! response '|SC_INTERNAL_SERVER_ERROR|)
               (set-content-type response (->jstring "text/html"))
               (sexp-xml:sexp->xml
                (response-page request
                               "Server error (sorry...)"
                               `((p "Unexpected response from servlet")
                                 (p ,(sexp-xml:escape-string-for-xml
                                      (format #f "Servlet ~s produced" method-handler)))
                                 (pre ,(sexp-xml:escape-string-for-xml
                                        (format #f "~s" resp))))))))))))

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
           (string-split (->string path-string) #\/)
;;            (remove (lambda (str) (= (string-length str) 0))
;;                    (map ->string
;;                         (->list (split path-string (->jstring "/")))))
           ))))
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
;; (for example http://localhost:8080/quaestor); that is, regenerate
;; the URL corresponding to the host plus context-path.  If the
;; optional boolean  argument is true, include the servlet path,
;; without path info (producing for example
;; http://localhost:8080/quaestor/kb)
;;
;; The one-arg form will have the same value for any request made to a given servlet.
;;
;; This isn't bulletproof, since we can't _really_ know what the
;; original URL was, but it gets it right enough almost all of the
;; time.
(define (webapp-base-from-request request . with-servlet?)
  (define-generic-java-methods
    get-server-name get-server-port
    get-context-path get-servlet-path
    to-string concat)
  (define-java-classes (<url> |java.net.URL|))
  (define (header-host req) ;get the intended target (virtual?) host from the HTTP/1.1 'Host' header
    (define-generic-java-methods get-header)
    (let* ((hhj (get-header req (->jstring "hOsT")))
           (hhs (->string-or-empty hhj)))
      (cond ((= (string-length hhs) 0)  ;'Host:' header was null; return #f
             #f)
            ((string-index hhs #\:)     ;includes a port
             => (lambda (idx) (->jstring (substring hhs 0 idx))))
            (else hhj))))
 (let ((include-servlet-path? (and (not (null? with-servlet?))
                                   (car with-servlet?))))
;;     (chatter "server-name=~s  header-host=~s  server-port=~s  context-path=~s  servlet-path=~s  include-servlet?=~a"
;;              (->string-or-empty (get-server-name request))
;;              (or (header-host request) "<none>")
;;              (->string-or-empty (get-server-port request))
;;              (->string-or-empty (get-context-path request))
;;              (->string-or-empty (get-servlet-path request))
;;              include-servlet-path?)
    (->string
     (to-string
      (java-new <url>
                (->jstring "http")
                (or (header-host request) (get-server-name request))
                (let ((port (get-server-port request)))
                  ;; avoid adding the explicit port number, if it's on port 80
                  (if (= (->number port) 80)
                      (->jint -1)
                      port))
                ;;(get-server-port request) ;not quite right, if the original URL was on port 80,
                                        ;since this inserts a spurious :80
                (if include-servlet-path?
                    (concat (get-context-path request)
                            (get-servlet-path request))
                    (get-context-path request)))))))

;; Called with one argument, verify that the "Content-*" headers are
;; all in the allowed set, returning #t if so.  Otherwise, return #f.
;; Called with no arguments, return the set of allowed headers.
;;
;; This function is here because RFC 2616 section 9.6 says that a recipient
;; must not ignore content-* headers it does not recognise
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
       (jobject->list (get-header-names request))))

;; Return the contents of the Accept header as a list of scheme strings.
;; Each one is a MIME type.  If there are no Accept headers, return #f.
(define (request->accept-mime-types request)
  (define-generic-java-methods
    get-headers
    append)
  ;; merge all the "accept" headers into a single comma-separated Java string,
  ;; or #f if there is no Accept header
  (cond
   ((let loop ((headers (jobject->list (get-headers request (->jstring "accept")))))
      (case (length headers)
        ((0) #f)                        ;no header
        ((1) (car headers))
        (else
         (append (append (car headers) (->jstring ", "))
                 (loop (cdr headers))))))
    => parse-http-accept-header)
   (else                                ;no Accept header
    #f)))

;; response->lazy-output-stream http-response -> java-output-stream
;;
;; Return a function which, when called, will return the response output stream.
;; This extracts the output stream using a LazyOutputStream, so that we
;; don't call GET-OUTPUT-STREAM on the underlying response unless and
;; until we need to, thus leaving any error handler free to do so instead.
;; May be called multiple times (unlike GET-OUTPUT-STREAM).
;; (define (response->lazy-output-stream response)
;;   (define-java-classes
;;     (<lazy-output-stream> |org.eurovotech.quaestor.LazyOutputStream|))
;;   (define-generic-java-methods
;;     get-output-stream
;;     get-lazy-output-stream)
;;   (get-output-stream (get-lazy-output-stream (java-null <lazy-output-stream>) response)))

;; Given a RESPONSE, set the response status to the given RESPONSE-CODE,
;; and produce a status page using the given format and arguments.
;; This expects to be called before there has been any other output.
(define (no-can-do request response-code fmt . args)
  (let ((msg (apply format `(#f ,fmt ,@args))))
    (list response-code
          (response-page request "Quaestor: no can do" `((p ,(sexp-xml:escape-string-for-xml msg)))))))

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
                     (jobject->list (get-header-names request))))))



;; RESPONSE-PAGE : <request> string (listof sexp) -> sexp
;; Evaluates to a sexp corresponding to a HTML page.  The TITLE-STRING
;; is a string containing a page title, and the BODY-SEXP is a list of sexps.
(define (response-page request title-string body-sexp)
  (define-generic-java-method get-context-path)
  `(html (head (title ,title-string)
               (link (@ (rel stylesheet)
                        (type text/css)
                        (href ,(string-append
                                (->string-or-empty (get-context-path request))
                                "/base.css")))))
         (body (h1 ,title-string)
               ,@body-sexp)))

)
