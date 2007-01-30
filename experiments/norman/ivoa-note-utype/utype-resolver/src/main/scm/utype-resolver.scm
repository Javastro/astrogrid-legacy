;; SISC program to resolve UTypes

(import s2j)

(require-library 'util/lambda-contract)

(require-library 'quaestor/utils)
(import utils)

(define (ident)
  (define-java-class <sisc.util.version>)
  (define-generic-java-field-accessor :version |VERSION|)
  `((utype-resolver-version . "@VERSION@")
    (sisc.version . ,(->string (:version (java-null <sisc.util.version>))))
    (string
     . "utype-resolver.scm @VERSION@ ($Id: utype-resolver.scm,v 1.1 2007/01/30 19:18:59 norman Exp $)")))

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

(define (get-resolve path-info-list url request response)
  (cond ((and (= (length path-info-list) 0)
              url)                      ;normal case
         (response-page request response
                        "UType resolver: Resolving"
                        `((p ,(format "Resolving URL ~a" url)))))
        ((= (length path-info-list) 0)  ;oops: for us, but query missing
         (set-response-status! response '|SC_BAD_REQUEST|)
         (response-page request response
                        "UType resolver: bad request"
                        `((p "Bad call to resolver service: no query"))))
        (else
         #f)))                          ;go to next handler

(define get-handlers `(,get-resolve ,get-fallback))
