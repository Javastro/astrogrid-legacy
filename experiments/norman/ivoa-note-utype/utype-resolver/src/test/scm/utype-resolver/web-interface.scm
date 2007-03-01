;; Test the RESTful interface to the resolver

(import s2j)
(import java-io)
(require-library 'sisc/libs/srfi/srfi-8)      ;receive
(import srfi-8)

(define-java-classes
  (<jstring> |java.lang.String|))

;; Set the current URL, for normalisation purposes, to the value
;; determined in the caller
(current-url (option-service-url))

;; resolve-resolver-url : string -> string
(define (resolve-resolver-url url)
  (normalize-url (current-url) url))

;; get-url : string string-or-false string-or-false -> number procedure string
(define (get-url relative-part mime-types method)
  (define-java-classes
    (<url> |java.net.URL|))
  (define-generic-java-methods
    open-connection
    set-request-method
    set-request-property
    set-follow-redirects
    get-response-code
    get-input-stream
    get-error-stream
    connect)
  (let ((full-url (resolve-resolver-url relative-part)))
    ;;(format #t "Testing URL: ~a~%" full-url)
    (with/fc (lambda (m e)
               (format #t "Error retrieving <~a>:~%  ~s~%"
                       full-url
                       (print-exception (make-exception m e)))
               (values #f #f #f))
      (lambda ()
        (let ((c (open-connection (java-new <url> (->jstring full-url))))
              (hh (header-handler)))
          (if mime-types
              (set-request-property c
                                    (->jstring "Accept")
                                    (->jstring mime-types))
              (set-request-property c   ; override `helpful' URLConnection
                                        ; (there's no way to remove this header
                                        ; completely, so we can't test that)
                                    (->jstring "Accept")
                                    (->jstring "*/*")))
          (set-follow-redirects c (->jboolean #f))
          (if method
              (set-request-method c (->jstring method)))
          (connect c)
          (hh c)
          (let ((status (->number (get-response-code c))))
            (values status
                    hh
                    (stream->string
                     ;; stupid HttpURLConnection throws an exception if you
                     ;; call get-input-stream on statuses >=400
                     (if (< status 400)
                         (get-input-stream c)
                         (get-error-stream c))))))))))

(define (non-null x)
  (and (not (java-null? x)) x))

;; header-handler : -> procedure
;; The returned procedure can be called as
;;   proc url-connection -> unspecified   ; extract headers from connection
;;   proc symbol -> string-or-false       ; get value for keyword (SISC strings)
(define (header-handler)
  (define-java-classes
    (<urlconnection> |java.net.HttpURLConnection|))
  (define-generic-java-methods get-header-field get-header-field-key
    instance? to-lower-case)
  (let ((headers #f))
    (lambda (arg)
      (cond ((and (java-object? arg)
                  (->boolean (instance? <urlconnection> arg)))
             (let loop ((l '())
                        (n 0))
               (cond ((or (non-null (get-header-field-key arg (->jint n)))
                          (= n 0)) ; field 0 may hold status line with null key 
                      => (lambda (keystring)
                           (loop (cons (cons (string->symbol
                                              (if (java-object? keystring)
                                                  (->string
                                                   (to-lower-case keystring))
                                                  "_status-line"))
                                             (->string
                                              (get-header-field arg (->jint n))))
                                       l)
                                 (+ n 1))))
                     (else
                      (set! headers l)))))
            ((symbol? arg)
             (cond ((not headers)
                    (error "header-handler called out of order"))
                   ((assq arg headers)
                    => cdr)
                   (else
                    #f)))
            ((boolean? arg)
             headers)
            (else
             (error (format #f "Bad call to header-handler proc with arg ~s" arg)))))))

(define (stream->string input-stream)
  (let ((cp (open-character-input-port
             (->binary-input-port input-stream)))
        (len 1024))
    (let loop ((strings '()))
      (let* ((b (make-string len))
             (n (read-string b 0 len cp)))
        (cond ((> n len)
               (error "Can't happen: n>len in stream->string"))
              ((< n 0)                  ;EOF
               (apply string-append (reverse strings)))
              ((= n len)                ;full buffer
               (loop (cons b strings)))
              ((= n 0)                  ;can this happen?
               (loop strings))
              (else                     ;0 < n < len -- partial buffer
               (loop (cons (substring b 0 n)
                           strings))))))))


;; CALL-AND-EXPECT-RESPONSE : symbol string number string proc -> void
;; CALL-AND-EXPECT-RESPONSE : symbol string (string) number string proc proc -> void
;;
;; Retrieve the relurl, check that its status and content-type are as shown,
;; then call the first procedure with the content string as argument.
;; If the second procedure is present and not #f, then call it with the
;; header function as argument.
;; Any of expected-status, expected-type, check-content and check-headers
;; may be #f to omit the corresponding check.
(define-syntax call-and-expect-response
  (syntax-rules ()
    ((_ id relurl
        expected-status expected-type
        check-content)
     (call-and-expect-response id (#f) relurl (#f)
                               expected-status expected-type
                               check-content #f))
    ((_ id relurl (accept-header)
        expected-status expected-type
        check-content check-headers)
     (call-and-expect-response id (#f) relurl (accept-header)
                               expected-status expected-type
                               check-content check-headers))
    ((_ id (method) relurl (accept-header)
        expected-status expected-type
        check-content check-headers)
     (receive (status headers content)
         (get-url relurl accept-header method)
       (if status
           (let ()
             (if expected-status
                 (expect (id status) expected-status status))
             (if expected-type
                 (expect (id type) expected-type (headers 'content-type)))
             (if check-content
                 (check-content content))
             (if check-headers
                 (check-headers headers))
             (if (failures-in-block?)
                 (format #t "test ~a:~%  status=~a(~a)  type=~a(~a)~a  content-length=~a~%  content=~a~%"
                         (quasiquote id)
                         status expected-status
                         (headers 'content-type) expected-type
                         (if accept-header
                             (format #f "(accept ~a)" accept-header)
                             "")
                         (and content (string-length content))
                         (cond ((not content)
                                "<none>")
                               ((= status expected-status)
                                "<suppressed>")
                               (else
                                content)))))
           #f)))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Start tests

(show-test #t)

(receive (status headers content)
    (get-url "." #f #f)
  (if (not status)
      (error "server is not running -- web-interface tests skipped")))

(call-and-expect-response
 top "."
 200
 "text/html"
 (lambda (content-string)
   (expect (top content) #t (> (string-length content-string) 0))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Test redirections for simple1

;; Note that we don't care about the MIME type on the redirections, 303.
;; This will typically be text/html, but that's just for browser
;; convenience, and an RDF reader typically won't care.
(call-and-expect-response
 simple1 "test/testcases/simple1" (#f)
 303                                    ;303 status
 #f                                     ;any MIME type
 #f                                     ;any content
 (lambda (hh)                           ;check Location header
      (expect simple1-headers
              (resolve-resolver-url "test/testcases/simple1.n3")
              (hh 'location))))
(call-and-expect-response
 simple1-n3 "test/testcases/simple1" ("text/rdf+n3")
 303
 #f
 #f
 (lambda (hh)
   (expect simple1-n3-headers
           (resolve-resolver-url "test/testcases/simple1.n3")
           (hh 'location))))
(call-and-expect-response
 simple1-rdf                            ; there's no rdf+xml rep'n of this
 "test/testcases/simple1" ("application/rdf+xml")
 406
 #f
 #f
 (lambda (hh)
   (expect simple1-rdf-headers #f (hh 'location))))

;; so much for redirections: now retrieve the results
(call-and-expect-response
 simple1-retrieve-plain
 "test/testcases/simple1.n3"
 200
 "text/rdf+n3"
 #f)
;; same, but with an accept header
(call-and-expect-response
 simple1-retrieve-accept
 "test/testcases/simple1.n3" ("text/rdf+n3")
 200
 "text/rdf+n3"
 #f
 #f)


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; same for simple2

;; First, a simple call with no accept header (or rather, */*)
(call-and-expect-response
 simple2 "test/testcases/simple2" (#f)
 303
 #f
 #f
 (lambda (hh)
   (expect simple2-headers
           (resolve-resolver-url "test/testcases/simple2.html")
           (hh 'location))))
;; simple calls, with single accept headers
(for-each (lambda (p)
            (let ((mime (car p))
                  (ext  (cdr p)))
              (call-and-expect-response
               (simple2 ,ext) "test/testcases/simple2" (mime)
               303
               #f
               #f
               (lambda (hh)
                 (expect (simple2-headers ,ext)
                         (resolve-resolver-url
                          (string-append "test/testcases/simple2" ext))
                         (hh 'location))))))
          '(("text/rdf+n3" . ".n3")
            ("application/rdf+xml" . ".rdf")
            ("text/html" . ".html")))
;; (call-and-expect-response
;;  simple2-n3 "test/testcases/simple2" ("text/rdf+n3")
;;  303
;;  #f
;;  #f
;;  (lambda (hh)
;;    (expect simple2-n3-headers
;;            (resolve-resolver-url "test/testcases/simple2.n3")
;;            (hh 'location))))
;; (call-and-expect-response
;;  simple2-rdf "test/testcases/simple2" ("application/rdf+xml")
;;  303
;;  #f
;;  #f
;;  (lambda (hh)
;;    (expect simple2-rdf-headers
;;            (resolve-resolver-url "test/testcases/simple2.rdf")
;;            (hh 'location))))
;; (call-and-expect-response
;;  simple2-html "test/testcases/simple2" ("text/html")
;;  303
;;  #f
;;  #f
;;  (lambda (hh)
;;    (expect simple2-html-headers
;;            (resolve-resolver-url "test/testcases/simple2.html")
;;            (hh 'location))))

;; The same two calls, but with both MIME types in the Accept header
(call-and-expect-response
 simple2-n3-2
 "test/testcases/simple2" ("application/rdf+xml;q=0.5, text/rdf+n3")
 303
 #f
 #f
 (lambda (hh)
   (expect simple2-n3-2-headers
           (resolve-resolver-url "test/testcases/simple2.n3")
           (hh 'location))))
(call-and-expect-response
 simple2-rdf-2
 "test/testcases/simple2" ("application/rdf+xml, text/rdf+n3;q=0.9")
 303
 #f
 #f
 (lambda (hh)
   (expect simple2-rdf-2-headers
           (resolve-resolver-url "test/testcases/simple2.rdf")
           (hh 'location))))

;; Call with unsupported MIME type, should return 406, Not Acceptable
(call-and-expect-response
 simple2-badmime
 "test/testcases/simple2" ("application/wibble")
 406
 #f
 #f
 #f)

;; supported MIME type, after unsupported one
(call-and-expect-response
 simple2-badmime2
 "test/testcases/simple2" ("application/wibble,text/html")
 303
 #f
 #f
 (lambda (hh)
   (expect simple2-badmime2-headers
           (resolve-resolver-url "test/testcases/simple2.html")
           (hh 'location))))

;; MIME type which has a mapping, but to a missing file -- server error, 500
(call-and-expect-response
 simple2-missingmime
 "test/testcases/simple2" ("text/undefined")
 500
 #f
 #f
 (lambda (hh)
   (expect simple2-missingmime-headers #f
           (hh 'location))))

;; grddl?.html are served as XHTML (application/xhtml+xml), but 
;; grddl-malformed1.html is served as HTML (text/html).
(call-and-expect-response
 retrieve-xhtml
 "test/testcases/grddl1.html"
 200
 "application/xhtml+xml"
 #f)
(call-and-expect-response
 retrieve-html
 "test/testcases/grddl-malformed1.html"
 200
 "text/html"
 #f)


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Now, finally, the reasoning/resolver cases

;; simple1 is served as simple1.n3, but rewritten by the server from .../simple1
(let ((testcases-url (resolve-resolver-url "/utype-resolver/test/testcases/")))
  (format #t "testcases-url=~a~%" testcases-url)

  ;; Call /superclasses without any query
  (call-and-expect-response
   superclasses-no-query
   "superclasses"
   400 ; bad request
   #f
   #f)

  ;; ...and with null query
  (call-and-expect-response
   superclasses-null-query
   "superclasses?"
   400 ; bad request
   #f
   #f)

  (call-and-expect-response
   resolve1
   (string-append "superclasses?" testcases-url "simple1%23c2")
   200
   "text/plain"
   (lambda (content)
     (expect resolve1-content
             (string-append testcases-url "simple1#c1\r\n")
             content)))

  ;; the same, but requesting an unsupported type in the response
  (call-and-expect-response
   resolve1-badmime
   (string-append "superclasses?" testcases-url "simple1%23c2")
   ("text/rdf+n3")                      ;only text/plain is supported
   406                                  ;406 Not Acceptable
   #f
   #f
   #f)

  ;; simple2 is just for testing the resolver/rewriter

  ;; simple3 is served only as simple3.rdf, without any server-side rewriting
  (call-and-expect-response
   resolve3
   (string-append "superclasses?" testcases-url "simple3%23c2")
   200
   "text/plain"
   (lambda (content)
     (expect resolve3-content
             (string-append testcases-url "simple3#c1\r\n")
             content)))

  ;; A query to the same namespace, but querying for the superclasses of
  ;; the top class, which should have no superclasses.
  (call-and-expect-response
   resolve3-top
   (string-append "superclasses?" testcases-url "simple3%23c1")
   204                                  ;204 No Content
   #f                                   ;don't care about the type
   (lambda (content)
     (expect resolve3-top-content       ;content should be empty (status 204)
             "" content)))

  ;; The GRDDL tests are served only as grddl{1,2,3,4}.html, and so
  ;; require transformation.
  (let-syntax ((test (syntax-rules ()
                       ((_ base)
                          (call-and-expect-response
                           base
                           (string-append "superclasses?"
                                          testcases-url
                                          base
                                          "%23c2")
                           200
                           "text/plain"
                           (lambda (content)
                             (expect (base content)
                                     (string-append testcases-url
                                                    base
                                                    "#c1\r\n")
                                     content)))))))
    (test "grddl1")                     ;<code class="namespace">
    (test "grddl2")                     ;<html:base>
    (test "grddl3")                     ;<base>
    (test "grddl4")                     ;nothing -- defaulted from location URL
    )

  ;; grddl-malformed1 is served only as .html, and so requires transformation,
  ;; but it's hideously malformed
  (call-and-expect-response
   resolve-html1
   (string-append "superclasses?" testcases-url "grddl-malformed1%23c2")
   200
   "text/plain"
   (lambda (content)
     (expect resolve5-content
             (string-append testcases-url "grddl-malformed1#c1\r\n")
             content)))

  ;; grddl-malformed2.html is almost identical to grddl-malformed1,
  ;; but is served (erroneously) as application/xhtml+xml.  We should
  ;; fail gracefully, being sure (status 502 Bad Gateway) to blame the
  ;; source of the HTML.
  (call-and-expect-response
   resolve-html2
   (string-append "superclasses?" testcases-url "grddl-malformed2%23c2")
   502                                  ;502 Bad Gateway
   #f                                   ;don't care about the type
   #f)                                  ;...or the content

  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  ;;
  ;; Descriptions

  ;; no query/namespace
  (call-and-expect-response
   description-no-query
   "description"
   200
   "text/html"
   #f)

  (call-and-expect-response
   description-null-query
   "description?"
   200
   "text/html"
   #f)

  (call-and-expect-response
   description-no-query-n3
   "description"
   ("text/rdf+n3")
   200
   "text/rdf+n3"
   #f
   #f)

  (call-and-expect-response
   description-no-query-unacceptable
   "description"
   ("text/wibble")
   406
   #f
   #f
   #f)

  ;; normal case -- a namespace we know about.  Defaults to text/html
  (call-and-expect-response
   description-n3
   (string-append "description?" testcases-url "grddl-malformed1%23c2")
   200
   "text/html"
   #f)

  ;; same, but requiring N-TRIPLES
  (call-and-expect-response
   description-triples
   (string-append "description?" testcases-url "grddl-malformed1%23c2")
   ("text/plain")
   200
   "text/plain"
   (lambda (content)
     ;; check that the string starts with an angle bracket
     ;; (ideally, we'd check that every line starts with that, or
     ;; match a suitable regexp on every line)
     (expect description-triples-angle-bracket
             #\<
             (string-ref content 0)))
   #f)

  ;; same, but requiring N3
  (call-and-expect-response
   description-unknown
   (string-append "description?" testcases-url "grddl-malformed1%23c2")
   ("text/rdf+n3")
   200
   "text/rdf+n3"
   #f
   #f)

  ;; same, but requiring a bad type
  (call-and-expect-response
   description-triples
   (string-append "description?" testcases-url "grddl-malformed1%23c2")
   ("text/wibble")
   406
   #f
   #f
   #f)

  ;; unknown namespace
  (call-and-expect-response
   description-unknown
   (string-append "description?" testcases-url "wibble%23c2")
   404
   #f
   #f)

  ;; forgetting namespaces...

  ;; first, deleting an unknown namespace
  (call-and-expect-response
   delete-unknown ("DELETE")
   (string-append "description?" testcases-url "wibble")
   ("*/*")
   400                                  ;400 Bad Request
   #f
   #f #f)

  ;; forget a known namespace

  ;; first, just double-check that it is there beforehand
  (call-and-expect-response
   pre-delete-simple1
   (string-append "description?" testcases-url "simple1")
   200
   "text/html"
   #f)

  (call-and-expect-response
   delete-simple1 ("DELETE")
   (string-append "description?" testcases-url "simple1%23anything")
   ("*/*")
   204                                  ;204 No Content
   #f
   (lambda (content)
     (expect delete-simple1-content ;content should be empty (status 204)
             "" content))
   #f)

  ;; ...and check that it's gone, and that other stuff hasn't
  (call-and-expect-response
   post-delete-simple1
   (string-append "description?" testcases-url "simple1")
   404
   "text/html"
   #f)
  ;; simple2 wasn't dropped
  (call-and-expect-response
   post-delete-simple3
   (string-append "description?" testcases-url "simple3")
   200
   "text/html"
   #f)

  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  ;;
  ;; Error cases
  (for-each (lambda (f)
              (call-and-expect-response
               (invalid-rdf ,f)
               (string-append "superclasses?" testcases-url f)
               400 ; Bad Request
               #f
               #f))
            '("error1.n3" "error2.n3" "error3.rdf" "error4.rdf"))

) ; end of LET for TESTCASES-URL

