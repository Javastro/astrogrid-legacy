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
(require-library 'quaestor/jena)
(import* jena rdf:select-statements)

(define (ident)
  (define-java-class <sisc.util.version>)
  (define-generic-java-field-accessor :version |VERSION|)
  `((utype-resolver-version . "@VERSION@")
    (sisc.version . ,(->string (:version (java-null <sisc.util.version>))))
    (string
     . "utype-resolver.scm @VERSION@ ($Id: utype-resolver.scm,v 1.8 2007/03/02 11:41:00 norman Exp $)")))

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
    register-handler)
  (define (reg method context proc)
    (register-handler scheme-servlet
                      (->jstring method)
                      context
                      (java-wrap proc)))

  (servlet 'set scheme-servlet)

  (reg "GET"
       (servlet 'parameter "resolver-context")
       get-resolve)
  (reg "GET"
       (servlet 'parameter "description-context")
       get-description)
  (reg "DELETE"
       (servlet 'parameter "description-context")
       delete-namespace)
  (reg "GET"
       (servlet 'parameter "test-server")
       test-redirector))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Implementation

;; GET-RESOLVE : list-of-strings string http-request http-response -> string-or-false
;; The main handler.  If the PATH-INFO-LIST is null and the URL is non-false,
;; then resolve the URL and return an appropriate page (text/plain in the
;; case of a successful resolution, text/html otherwise).
(define (get-resolve request response)
  (with/fc
      (make-fc request response '|SC_INTERNAL_SERVER_ERROR|)
    (lambda ()
      (let ((query-url (cond ((request->query-string request)
                              => decode-uri)
                             (else #f)))
            (acceptable (request->accept-mime-types request)))
        (cond ((not query-url)
               (set-response-status! response '|SC_BAD_REQUEST| "text/html")
               (response-page request response
                              "UType resolver: bad request"
                              '((p "Bad request: no query"))))

              ((not (acceptable-mime "text/plain" acceptable))
               (no-can-do request response '|SC_NOT_ACCEPTABLE|
                          "There is no representation of the set of superclasses, in one of the requested representations ~s"
                          acceptable))

              ((resolve-uri query-url)
               => (lambda (superclass-strings)
                    (set-response-status! response '|SC_OK| "text/plain")
                    (apply string-append
                           (map (lambda (s) (string-append s "\r\n"))
                                superclass-strings))))
              (else               ;no superclasses -- respond with 204
               (set-response-status! response '|SC_NO_CONTENT|)))))))

(define (get-description request response)
  (let ((query-url (cond ((request->query-string request)
                          => decode-uri)
                         (else
                          #f)))
        (acceptable (request->accept-mime-types request)))
    (define (model->string model lang mime)
      (define-java-class <java.io.string-writer>)
      (define-generic-java-methods write to-string)
      (let ((sw (java-new <java.io.string-writer>)))
        (write model
               sw
               (->jstring lang))
        (set-response-status! response '|SC_OK| mime)
        (->string (to-string sw))))
    (define (display-namespace-list-as-html)
      (let ((base-uri (webapp-base-from-request request #f)))
      (set-response-status! response '|SC_OK| "text/html")
      (response-page request response
                       "List of known namespaces"
                       `((p "The following namespaces are known")
                         (ul
                          ,@(map (lambda (s)
                                   `(li (a (@ (href ,(format #f "~a/description?~a"
                                                             base-uri s)))
                                       (code ,s))))
                                 (get-namespace-list)))))))
    (define (display-namespace-model-as-html ns model)
      (set-response-status! response '|SC_OK| "text/html")
      (response-page request response
                     (format #f "Namespace ~a" ns)
                     `((p ,(format #f
                                   "Namespace ~a defines the following UTypes:"
                                   ns))
                       (ul
                        ,@(let ((rdf-nodes
                                 (rdf:select-statements
                                  model
                                  #f
                                  "a"
                                  "http://example.ivoa.net/utypes#UType")))
                            (define-generic-java-methods to-string)
                            (map (lambda (node)
                                   (let ((url (->string (to-string node)))
                                         (comments (rdf:select-statements
                                                    model
                                                    node
                                                    "http://www.w3.org/2000/01/rdf-schema#comment"
                                                    #f)))
                                     `(li (a (@ (href ,url))
                                             ,url)
                                          ": "
                                          ,(if (null? comments)
                                               "[No description available]"
                                               (apply string-append
                                                      (map (lambda (n)
                                                             (->string
                                                              (to-string n)))
                                                           comments))))))
                                 rdf-nodes))))))

    (cond ((not query-url)
           (cond ((acceptable-mime '("text/html" "text/rdf+n3" "application/rdf+xml" "text/plain")
                                   acceptable)
                  => (lambda (mime)
                       (cond ((string=? mime "text/html")
                              (display-namespace-list-as-html))
                             ((string=? mime "text/rdf+n3")
                              (model->string (get-namespace-description)
                                             "N3" mime))
                             ((string=? mime "text/plain")
                              (model->string (get-namespace-description)
                                             "N-TRIPLE" mime))
                             ((string=? mime "application/rdf+xml")
                              (model->string (get-namespace-description)
                                             "RDF/XML" mime))
                             (else
                              (error "get-description: Unhandled mime type ~a"
                                     mime)))))
                 (else
                  (set-response-status! response
                                        '|SC_NOT_ACCEPTABLE|
                                        "text/html")
                  (response-page
                   request response
                   "No acceptable representations"
                   `((p ,(format #f "No representation for the description is available, in one of the requested representations ~s"
                                 acceptable)))))))

          ((get-namespace-description query-url)
           => (lambda (model)
                (cond ((acceptable-mime '("text/html" "text/rdf+n3" "application/rdf+xml" "text/plain")
                                        acceptable)
                       => (lambda (mime)
                            (cond ((string=? mime "text/html")
                                   (display-namespace-model-as-html query-url
                                                                    model))
                                  ((string=? mime "text/rdf+n3")
                                   (model->string model "N3" mime))
                                  ((string=? mime "text/plain")
                                   (model->string model "N-TRIPLE" mime))
                                  ((string=? mime "application/rdf+xml")
                                   (model->string model "RDF/XML" mime))
                                  (else
                                   (error "get-description: error processing MIME type (~s)"
                                          mime)))))
                      (else
                       (set-response-status! response
                                             '|SC_NOT_ACCEPTABLE|
                                             "text/html")
                       (response-page
                        request response
                        "No acceptable representations"
                        `((p "I know about the namespace "
                             (code ,query-url)
                             
                             ,(format #f ", but can't describe it in any of the acceptable MIME types ~s"
                                      acceptable))))))))
          (else
           (set-response-status! response '|SC_NOT_FOUND| "text/html")
           (response-page request response
                          "Unknown namespace"
                          `((p ,(format #f "I don't know anything about ~a"
                                        query-url))))))))

;; DELETE-NAMESPACE : request response -> string
;; Delete the namespace given in the query string (possibly after removing
;; a fragment).
(define (delete-namespace request response)
  (let ((query-url (cond ((request->query-string request)
                          => decode-uri)
                         (else
                          #f))))
    (if (forget-namespace! query-url)
        (set-response-status! response '|SC_NO_CONTENT|)
        (no-can-do request response '|SC_NOT_FOUND|
                   "I don't know anything about namespace ~s" query-url))))


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
;; This is the principal function of this application.  Return all the
;; superclasses as a list of strings, or return #f if there are no superclasses.
(define (resolve-uri uri-string)
  (define-java-class <uri> |java.net.URI|)
  (define-generic-java-methods
    get-fragment
    (to-url |toURL|)
    to-string)
  (let ((uri (java-new <uri> (->jstring uri-string))))
    (chatter "resolve-uri: ~a -> frag ~a, URL ~a"
             uri-string
             (cond ((non-null (get-fragment uri))
                    => ->string)
                   (else
                    "<none>"))
             (->string (to-string uri)))
    (if (not (namespace-seen? uri))
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
