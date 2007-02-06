;; SISC program to resolve UTypes

(import s2j)
(import java-io)

(require-library 'sisc/libs/srfi/srfi-26)
(import* srfi-26
         cut)

(require-library 'utype-resolver/redirector)
(import redirector)

(require-library 'util/lambda-contract)

(require-library 'quaestor/utils)
(import utils)

(define (ident)
  (define-java-class <sisc.util.version>)
  (define-generic-java-field-accessor :version |VERSION|)
  `((utype-resolver-version . "@VERSION@")
    (sisc.version . ,(->string (:version (java-null <sisc.util.version>))))
    (string
     . "utype-resolver.scm @VERSION@ ($Id: utype-resolver.scm,v 1.3 2007/02/06 23:00:43 norman Exp $)")))

;; Predicates for contracts
(define-java-classes
  <java.lang.string>)
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

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Initialisation

(define (initialise-resolver scheme-servlet)
  (define-generic-java-methods
    register-handler
;    get-servlet-context
;    to-string
;    get-resource
    ;get-init-parameter
    )
;;   (define (get-param s)
;;     (get-init-parameter scheme-servlet (->jstring s)))
  (define (reg method context proc)
    (register-handler scheme-servlet
                      (->jstring method)
                      context
                      (java-wrap proc)))

  (servlet 'set scheme-servlet)

  (reg "GET"
       (servlet 'parameter "resolver-context") ;(get-param "resolver-context")
       http-get)
  (reg "GET"
       (servlet 'parameter "test-server") ;(get-param "test-server")
       test-redirector)
;;   (initialise-redirector
;;    (string-append (->string
;;                    (to-string
;;                     (get-resource
;;                      (get-servlet-context scheme-servlet)
;;                      (->jstring "/"))))
;;                   (->string (servlet 'parameter "test-server"))))
  )

;; servlet : symbol object -> object
;; Retain a pointer to the servlet handling us, and use it to answer queries.
;;   (servlet 'set x)
;;     register x as the servlet
;;   (servlet 'real-path "file")
;;     return the full filesystem path of "file" (scheme string), or #f
;;   (servlet 'log fmt . arguments)
;;     write the formatted string to 
;;   (servlet 'context)
;;     return the servlet context
(define servlet
  (let ((servlet #f)
        (servlet-context #f))
    (lambda (action . arg)
      (define-generic-java-methods
        get-init-parameter
        get-servlet-context
        get-real-path
        log)
      (define (check-argnum n)
        (or (= n (length arg))
            (error "Bad call to servlet: wrong number of args")))

      ;; first call must be (servlet servlet xxx): check this
      (if (not (or servlet-context (eq? action 'set)))
          (error "Call to servlet out of order: servlet not set"))

      (case action
        ((set)
         (check-argnum 1)
         (if servlet-context
             (error "Bad call to (servlet 'set): servlet already set"))
         (set! servlet (car arg))
         (set! servlet-context (get-servlet-context (car arg))))
        ((context)
         (check-argnum 0)
         servlet-context)
        ((real-path)
         (check-argnum 1)
         (non-null (get-real-path servlet-context
                                  (->jstring (car arg)))))
        ((log)
         (log servlet-context (->jstring (apply format (cons #f arg)))))
        ((parameter)
         (check-argnum 1)
         (get-init-parameter servlet (->jstring (car arg))))
        (else
         (error (format #f "Bad call to servlet with action ~a"
                        action)))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Implementation

(define/contract (http-get (request  request?)
                           (response response?)
                           -> string-or-true?)
  (with/fc
      (make-fc request response '|SC_INTERNAL_SERVER_ERROR|)
    (lambda ()
      (let ((path-list    (request->path-list request))
            (query-string (request->query-string request)))
        (set-response-status! response '|SC_OK| "text/html") ;sensible default
        (let loop ((h get-handlers))
          (if (null? h)
              (error 'get "No applicable GET handlers found!") ;shouldn't happen
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
  (set-response-status! response '|SC_NOT_FOUND| "text/html")
  (response-page request response
                 "Utype resolver"
                 `((p "I don't recognise that URL.")
                   (p "The details of the request follow:")
                   ,@(tabulate-request-information request))))

;; get-resolve : list-of-strings string http-request http-response -> string-or-false
;; The main handler.  If the PATH-INFO-LIST is null and the URL is non-false,
;; then resolve the URL and return an appropriate page (text/plain in the
;; case of a successful resolution, text/html otherwise).
(define (get-resolve path-info-list url request response)
  (cond ((and (= (length path-info-list) 0)
              url)                      ;normal case
         (cond ((resolve-uri url)
                => (lambda (resp)
                     (set-response-status! response '|SC_OK| "text/plain")
                     resp))
               (else
                (set-response-status! response '|SC_BAD_REQUEST|)
                (response-page request response
                               "UType resolver: can't resolve URI"
                               `((p "Unable to resolve URL ~a" url))))))
        ((= (length path-info-list) 0)  ;oops: for us, but query missing
         (set-response-status! response '|SC_BAD_REQUEST|)
         (response-page request response
                        "UType resolver: bad request"
                        `((p "Bad call to resolver service: no query"))))
        (else
         #f)))                          ;go to next handler

(define get-handlers `(,get-resolve ,get-fallback))

;; resolve-uri : string -> string-or-false
;; Resolve the URI, and return a suitable response string
;; This is the principal function of this application
(define (resolve-uri uri-string)
  (define-java-class <uri> |java.net.URI|)
  (define-generic-java-methods
    get-fragment
    (to-url |toURL|)
    to-string)
  (let ((uri (java-new <uri> (->jstring uri-string))))
    (format #f "~a -> frag ~a, URL ~a"
            url
            (->string (get-fragment uri))
            (->string (to-string (to-url uri))))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Useful utilities

;; non-null : object -> object-or-false
;; returns #f is x is java-null, and x otherwise
(define (non-null x)
  (and (not (java-null? x)) x))
;; ->string-if-non-null : jstring -> string-or-false
;; Convenience procedure
;; like NON-NULL, except that the argument is a jstring, and we return a string
(define (->string-if-non-null x)
  (and (not (java-null? x)) (->string x)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; TEST-REDIRECTOR handler

;; Define TEST-REDIRECTOR, which supports the sort of 303-based HTTP redirection
;; required to test the resolving logic above.
;;
;; That is, the following functionality is for the test harness, not the
;; final exposed functionality.
;; (define test-redirector #f)
;; (let ()
;; ;(module ; anonymous module
;; ;    (test-redirector)


;;   ;; our-resource-as-stream : string -> pair-or-false
;;   ;; Get one of our resources (only one level -- no /x/y) and return a
;;   ;; (stream . string) pair containing the stream connected to it, and
;;   ;; its mime type.  If there's no such resource, return #f.
;;   (define (our-resource-as-stream name)
;;     (define-generic-java-methods get-resource-as-stream get-mime-type)
;;     (let ((jname (->jstring name)))
;;       (cond ((non-null (get-resource-as-stream (servlet 'context) jname))
;;              => (lambda (stream)
;;                   (servlet 'log "resource ~s/~s produced stream ~s and mime-type ~s"
;;                            name jname stream
;;                            (->string-if-non-null
;;                             (get-mime-type (servlet 'context) jname)))
;;                   (cons stream
;;                         (->string-if-non-null
;;                          (get-mime-type (servlet 'context) jname)))))
;;             (else
;;              #f)))
;; ;;     (non-null (get-resource-as-stream (servlet 'context)
;; ;;                                       (->jstring name
;; ;;                                                  ;; XXX there's /x/y now...
;; ;;                                                  ;(string-append "/" name)
;; ;;                                                  )))
;;     )

;;   ;; rewrite-resource : string list-of-strings -> string-or-false
;;   ;; rewrite-resource : #f object -> #f
;;   ;; Given a RESOURCE and a list of mime types, return a string resource which
;;   ;; is what that should map to as controlled by the
;;   ;; /WEB-INF/rewrite-map.scm file.
;;   (define rewrite-resource
;;     (let ((rewrite-map #f)) ;map of ((name (mime file) (mime file))...)
;;       (define (match-name-and-mime name acceptable-mimes)
;;         (chatter "match-name-and-mime: name=~s  acceptable-mimes=~s"
;;                  name acceptable-mimes)
;;         ;; find an entry in rewrite-map which has the given name,
;;         ;; and a mime in acceptable-mimes.  Or #f if none found
;;         (cond ((assoc name rewrite-map)
;;                => (lambda (p)
;;                     (let ((mime-file-mappings (cdr p)))
;;                       (and (not (null? mime-file-mappings))
;;                            (let loop ((ok-mimes acceptable-mimes))
;;                              (cond ((null? ok-mimes)
;;                                     #f)
;;                                    ((string=? (car ok-mimes) "*/*")
;;                                     (cadar mime-file-mappings))
;;                                    ((assoc (car ok-mimes) mime-file-mappings)
;;                                     => cadr)
;;                                    (else
;;                                     (loop (cdr ok-mimes)))))))))
;;               (else
;;                #f)))
;;       (lambda (resource mime-type-list)
;;         (define-java-classes <java.io.input-stream-reader>)
;;         (if (not rewrite-map)
;;             (cond ((our-resource-as-stream "/WEB-INF/rewrite-map.scm")
;;                    => (lambda (p)
;;                         (set! rewrite-map
;;                               (read (->character-input-port
;;                                      (java-new <java.io.input-stream-reader>
;;                                                (car p)))))
;;                         (chatter "rewrite-map: map=~s" rewrite-map)))
;;                   (else
;;                    (error 'test-redirector "Can't find rewrite-map"))))
;;         (let ((ans (cond ((not resource) ;short-circuit special case
;;                           #f)
;;                          ((match-name-and-mime resource mime-type-list))
;;                          (else
;;                           #f))))
;;           (servlet 'log
;;                    "rewrite-resource: resource=~s, mime-list=~s => ~s"
;;                    resource mime-type-list ans)
;;           ans)
;; ;;         (cond ((not resource)           ;short-circuit special case
;; ;;                #f)
;; ;;               ((match-name-and-mime resource mime-type-list))
;; ;;               (else
;; ;;                #f))
;;         )))

;;   ;; send-stream-to-response : response java-input-stream -> #f
;;   ;; Send the contents of the input stream to the response
;;   (define (send-stream-to-response response istream)
;;     (define-java-classes
;;       (<q-utils> |org.eurovotech.quaestor.helpers.Util|))
;;     (define-generic-java-methods
;;       get-output-stream
;;       flush
;;       close
;;       copy)
;;     (let ((ostream (get-output-stream response)))
;;       (set-response-status! response '|SC_OK| "text/html")
;;       (copy (java-null <q-utils>) istream ostream)
;;       (flush ostream)
;;       (close istream)
;;       (close ostream)
;;       #f))

;;   (define test-context #f)

;;   ;; test-redirector : request response -> string
;;   ;; If the request refers to an actual resource, return it as a stream
;;   ;; (thus returning #f from this procedure).  If the request instead
;;   ;; refers to something which REWRITE-RESOURCE knows about, then set
;;   ;; a 303 response and return something pointing to that.  Otherwise
;;   ;; set the response to be 404.
;;   (define (%test-redirector request response)
;;     (with/fc
;;         (make-fc request response '|SC_INTERNAL_SERVER_ERROR|)
;;       (lambda ()
;;         (define-generic-java-methods get-path-info)
;;         (let ((res (or (->string-if-non-null (get-path-info request))
;;                        "index.html"))
;;               (acceptable (request->accept-mime-types request)))
;;           (define-generic-java-methods
;;             set-header
;;             to-string)
;;           (chatter "test-redirector: res=~s request MIME-types=~s"
;;                    res acceptable)
;;           (if (not test-context)
;;               (set! test-context
;;                     (string-append (webapp-base-from-request request)
;;                                    (->string (servlet 'parameter "test-server")))))
;;           ;; XXX handle MIME-types below
;;           (cond (#f
;;                  (let ((ctx (servlet 'context)))
;;                    (define-generic-java-methods
;;                      to-string get-resource get-resource-paths)
;;                    (response-page request response "DEBUG context"
;;                                 `((p "context stuff")
;;                                   (table
;;                                    (tr (td "Context")
;;                                        (td ,(->string (to-string ctx))))
;;                                    (tr (td "/simple1.n3")
;;                                        (td ,(cond ((non-null (get-resource ctx (->jstring "/simple1.n3"))) => (lambda (s) (->string (to-string s)))) (else "X"))))
;;                                    (tr (td "/testcases/simple1.n3")
;;                                        (td ,(cond ((non-null (get-resource ctx (->jstring "/testcases/simple1.n3"))) => (lambda (s) (->string (to-string s)))) (else "X"))))
;;                                    (tr (td "paths")
;;                                        (td ,(format #f "paths:~s" (map ->string
;;                                                   (collection->list
;;                                                    (get-resource-paths
;;                                                     ctx
;;                                                     (->jstring "/"))))))))))))
;;                 ((our-resource-as-stream res)
;;                  => (lambda (p)
;;                       (let ((stream (car p))
;;                             (mime (cdr p)))
;;                         (set-response-status! response '|SC_OK| mime)
;;                         (send-stream-to-response response stream))))

;;                 ((rewrite-resource res acceptable)
;;                  => (lambda (newres)
;;                       (cond ((servlet 'full-path newres)
;;                              (let ((new-url (string-append
;;                                              test-context
;;                                              newres)))
;;                                (set-response-status! response
;;                                                      '|SC_SEE_OTHER|
;;                                                      "text/html")
;;                                (set-header response
;;                                            (->jstring "Location")
;;                                            (->jstring new-url))
;;                                (response-page request response "See elsewhere"
;;                                               `((p ,(format #f "The resource you are looking for can be found at ~a" new-url))))))

;;                             (else
;;                              (set-response-status! response
;;                                                    '|SC_NOT_FOUND|
;;                                                    "text/html")
;;                              (response-page request response "Not found"
;;                                             `((p ,(format #f "Resource ~a not found" res))))))))

;;                 (else
;;                  (set-response-status! response
;;                                        '|SC_NOT_FOUND|
;;                                        "text/html")
;;                  (response-page request response "Unknown resource"
;;                                 `((p ,(format #f "I don't know anything about ~a"
;;                                               res))
;;                                   (p ,(format #f "You asked for MIME types: ~s"
;;                                               acceptable))))
;; ;;                  (error (format #f "Nothing found for res=~a: MIME types=~s"
;; ;;                                 res acceptable))
;;                  ))))))
;;   (set! test-redirector %test-redirector))
