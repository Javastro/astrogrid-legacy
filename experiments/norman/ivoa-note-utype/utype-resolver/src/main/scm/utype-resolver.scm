;; SISC program to resolve UTypes

(import s2j)
(import java-io)

(require-library 'sisc/libs/srfi/srfi-26)
(import* srfi-26
         cut)

(require-library 'utype-resolver/redirector)
(import redirector)
(require-library 'utype-resolver/knowledge)
(import knowledge)

(require-library 'util/lambda-contract)

(require-library 'quaestor/utils)
(import utils)

(define (ident)
  (define-java-class <sisc.util.version>)
  (define-generic-java-field-accessor :version |VERSION|)
  `((utype-resolver-version . "@VERSION@")
    (sisc.version . ,(->string (:version (java-null <sisc.util.version>))))
    (string
     . "utype-resolver.scm @VERSION@ ($Id: utype-resolver.scm,v 1.4 2007/02/12 22:25:07 norman Exp $)")))

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
       test-redirector))

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
        (servlet 'log "utype-resolver/http-get: path=~s  query=~s"
                 path-list query-string)
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

;; GET-RESOLVE : list-of-strings string http-request http-response -> string-or-false
;; The main handler.  If the PATH-INFO-LIST is null and the URL is non-false,
;; then resolve the URL and return an appropriate page (text/plain in the
;; case of a successful resolution, text/html otherwise).
(define (get-resolve path-info-list url request response)
  (chatter "get-resolve: path-info-list=~s, url=~s" path-info-list url)
  (cond ((and (= (length path-info-list) 0)
              url)                      ;normal case
         (cond ((resolve-uri url)
                => (lambda (superclass-strings)
                     (set-response-status! response '|SC_OK| "text/plain")
                     (apply string-append
                            (map (lambda (s) (string-append s "\r\n"))
                                 superclass-strings))))
               (else
                (set-response-status! response '|SC_BAD_REQUEST|)
                (response-page request response
                               "UType resolver: can't resolve URI"
                               `((p ,(format #f "Unable to resolve URL ~a" url)))))))
        ((= (length path-info-list) 0)  ;oops: for us, but query missing
         (set-response-status! response '|SC_BAD_REQUEST|)
         (response-page request response
                        "UType resolver: bad request"
                        `((p "Bad call to resolver service: no query"))))
        (else
         #f)))                          ;go to next handler

(define get-handlers `(,get-resolve ,get-fallback))

;; DECODE-URI : string -> string
;; Given a URI as a string, return a string with this %-decoded.
(define (decode-uri uri)
  (let ((modified? #f))
    (with/fc
        (lambda (m e)   ;on any malformedness errors (ie, malformed %xx),
          uri)          ;...return uri
      (lambda ()
        (let ((uri-list
               (let loop ((chars (string->list uri)))
                 (cond ((null? chars)
                        '())
                       ((char=? (car chars) #\%)
                        (set! modified? #t)
                        (cons (integer->char
                               (string->number
                                (list->string
                                 (list (cadr chars) (caddr chars)))
                                16))
                              (loop (cdddr chars))))
                       (else
                        (cons (car chars)
                              (loop (cdr chars))))))))
          (if modified?
              (list->string uri-list)
              uri))))))

;; RESOLVE-URI : string -> list-of-strings-or-false
;; Resolve the URI, and return a suitable response string
;; This is the principal function of this application
(define (resolve-uri uri-string)
  (define-java-class <uri> |java.net.URI|)
  (define-generic-java-methods
    get-fragment
    (to-url |toURL|)
    to-string)
  (let ((uri (java-new <uri> (->jstring (decode-uri uri-string)))))
    (chatter "resolve-uri: ~a -> frag ~a, URL ~a"
             uri-string
             (cond ((non-null (get-fragment uri))
                    => ->string)
                   (else
                    "<none>"))
             (->string (to-string uri)))
    (if (first-sight-of-namespace uri)
        (ingest-utype-declaration-from-uri! uri))
    (query-utype-superclasses uri)))


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
         (let ((logmsg (apply format (cons #f arg))))
           (chatter "log message: ~a" logmsg)
           (log servlet-context (->jstring logmsg))))
        ((parameter)
         (check-argnum 1)
         (get-init-parameter servlet (->jstring (car arg))))
        (else
         (error (format #f "Bad call to servlet with action ~a"
                        action)))))))

;; temporary debugging
(define (show-chatter)
  (for-each (lambda (s)
              (display s)
              (newline))
            (chatter)))
