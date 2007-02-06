;; Test the RESTful interface to the resolver

(import s2j)
(import java-io)
(require-library 'sisc/libs/srfi/srfi-8)      ;receive
(import srfi-8)

;; Set the current URL, for normalisation purposes, to the value
;; determined in the caller
(current-url (option-service-url))

;; resolve-resolver-url : string -> string
(define (resolve-resolver-url url)
  (normalize-url (current-url) url))

;; get-url : string string-or-false -> number procedure string
(define (get-url relative-part mime-types)
  (define-java-classes
    (<url> |java.net.URL|))
  (define-generic-java-methods
    open-connection
    set-request-property
    set-follow-redirects
    get-response-code
    get-input-stream
    get-error-stream
    connect)
  (let ((full-url (resolve-resolver-url relative-part)))
    (format #t "Testing URL: ~a~%" full-url)
    (with/fc (lambda (m e)
               (format #t "Error retrieving <~a>:~%  ~s~%"
                       full-url
                       ;(error-message m)
                       (print-exception (make-exception m e))
                       )
               (values #f #f #f))
      (lambda ()
        (let ((c (open-connection (java-new <url> (->jstring full-url))))
              (hh (header-handler)))
          (if mime-types
              (set-request-property c
                                    (->jstring "Accept")
                                    (->jstring mime-types))
              (set-request-property c   ; override `helpful' URLConnection
                                    (->jstring "Accept")
                                    (->jstring "*/*")))
          (set-follow-redirects c (->jboolean #f))
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


;; call-with-content : string number string proc -> void
;; call-with-content : string (string) number string proc proc -> void
;; Retrieve the relurl, check that its status and content-type are as shown,
;; then call the first procedure with the content string as argument.
;; If the second procedure is present and not #f, then call it with the
;; header function as argument.
;; Any of expected-status, expected-type, check-content and check-headers
;; may be #f to omit the corresponding check.
(define-syntax call-and-expect-response
  (syntax-rules ()
    ((_ relurl expected-status expected-type check-content)
     (call-and-expect-response relurl (#f)
                               expected-status expected-type
                               check-content #f))
    ((_ relurl (accept-header)
        expected-status expected-type
        check-content check-headers)
     (receive (status headers content)
         (get-url relurl accept-header)
       (if status
           (let ()
;;              (format #t "status=~a(~a)  type=~a(~a, req ~a)  content-length=~a~%"
;;                      status expected-status
;;                      (headers 'content-type) expected-type accept-header
;;                      (and content (string-length content)))
             (if expected-status
                 (expect (relurl status) expected-status status))
             (if expected-type
                 (expect (relurl type) expected-type (headers 'content-type)))
             (if check-content
                 (check-content content))
             (if check-headers
                 (check-headers headers)))
           #f)))))


(define (ignore-content headers) #t)

(receive (status headers content)
    (get-url "." #f)
  (if (not status)
      (error "server is not running -- web-interface tests skipped")))

(call-and-expect-response
 "." 200 "text/html"
 (lambda (content-string)
   (expect (relurl content) #t (> (string-length content-string) 0))))

;; Test redirections for simple1
(call-and-expect-response
 "test/testcases/simple1" (#f) 303 #f
 ignore-content
 (lambda (hh)
      (expect (resolve-resolver-url "test/testcases/simple1.n3")
              (hh 'location))))
(call-and-expect-response
 "test/testcases/simple1" ("text/rdf+n3") 303 #f
 ignore-content
 (lambda (hh)
   (expect (resolve-resolver-url "test/testcases/simple1.n3")
           (hh 'location))))
(call-and-expect-response
 "test/testcases/simple1" ("application/rdf+xml") 404 #f
 ignore-content
 (lambda (hh)
   (expect #f (hh 'location))))

;; same for simple2
(call-and-expect-response
 "test/testcases/simple2" (#f) 303 #f
 ignore-content
 (lambda (hh)
      (expect (resolve-resolver-url "test/testcases/simple2.n3")
              (hh 'location))))
(call-and-expect-response
 "test/testcases/simple2" ("text/rdf+n3") 303 #f
 ignore-content
 (lambda (hh)
   (expect (resolve-resolver-url "test/testcases/simple2.n3")
           (hh 'location))))
(call-and-expect-response
 "test/testcases/simple2" ("application/rdf+xml") 303 #f
 ignore-content
 (lambda (hh)
   (expect (resolve-resolver-url "test/testcases/simple2.rdf")
           (hh 'location))))
(call-and-expect-response
 "test/testcases/simple2" ("text/html,application/wibble") 303 #f
 ignore-content
 (lambda (hh)
   (expect (resolve-resolver-url "test/testcases/simple2.html")
           (hh 'location))))
(call-and-expect-response ;there is a mapping for this, but to a missing file
 "test/testcases/simple2" ("text/undefined") 404 #f
 ignore-content
 (lambda (hh)
   (expect #f
           (hh 'location))))
