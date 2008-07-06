(module quaestor-support
    (apply-with-top-fc
     make-fc
     format-error-record
     report-exception
     set-response-status!
     chatter)

(import s2j)
(import debugging)                      ;for enhanced print-stack-trace
(import string-io)                      ;for with-output-to-string

;; APPLY-WITH-TOP-FC procedure args... -> object
;; This is intended to be a main entry point for the functions here.
;; Call the given procedure with the given arguments, in the context
;; of a suitable failure-continuation.
;;
;; Since the functions which are called via this typically have their
;; own error handlers, this failure-continuation will generally only
;; be called if something quite bad has gone wrong; therefore the
;; failure-continuation shows debugging information and chatter, and
;; returns a Java exception.
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

;; make-fc java-request java-response symbol -> procedure
;;
;; Make a SISC failure continuation.  Return a two-argument procedure
;; which can be used as the handler for with-failure-continuation.
;; See REPORT-EXCEPTION for an error procedure which allows you to override
;; the status given here.
(define (make-fc request response status)
;(define/contract (make-fc (request java-object?)
;                          (response java-object?)
;                          (status symbol?)
;                          -> procedure?)
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
    (define (is-java-type? jobject class)
      (define-generic-java-method instance?)
      (and (java-object? jobject)
           (->boolean (instance? class jobject))))
    (cond ((is-java-type? msg <throwable>)
           (get-message (get-undeclared-throwable msg)))
          ((is-java-type? msg <exception>)
           (->string (to-string msg)))
          ((format-error-ancestors rec))
          (else
           rec))))

;; report-exception : symbol integer string . obj -> does-not-return
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
;;     (chatter fmt . args)  ; Format a message and return #t
;;     (chatter)             ; return list of messages, or #f if none
;;     (chatter #t)          ; ditto, but additionally clear the list
;;     (chatter Object)      ; Object must have a log(String) method
;; In the latter case, subsequent calls to (chatter fmt . args) will put the
;; message in the chatter buffer, but will additionally send it to the given
;; logging object.
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

    (let ((chatter-list (make-circular-list 16)))
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
                     (set! chatter-list (make-circular-list 16))) ; new clear list
                 (and (not (null? r)) r)))  ;return list or #f
              ((java-object? (car msg))     ;an object which has a log(String) method
               (chatter-to-log (car msg)))
              (else
               (apply chatter-to-log msg)
               (set! chatter-list   ;append a new message
                     (append-circular-list chatter-list
                                           (apply format (cons #f msg))))
               #t))))))

;; CHATTER-TO-LOG : <Object> -> void
;; CHATTER-TO-LOG : string . args -> void
;; If given a Java object, it must be an object which has a
;; log(String) method, and this object is saved.
;; If given a message (a list of objects starting with a format
;; string), and if the log-object is present, then it sends the given
;; string to the indicated logging stream.
;;
;; Called from CHATTER.
(define chatter-to-log
  (let ((log-object #f))
    (lambda msg
      (define-generic-java-methods log)
      (let ((fmt (and (not (null? msg))
                         (car msg))))
        (cond ((not fmt)
               (error "bad call to chatter-to-log, with null argument"))
              ((java-object? fmt) ;an object which has a log(String) method
               (set! log-object fmt)
               #t)
              ((and (string? fmt)
                    log-object)
               (log log-object
                    (->jstring (apply format `(#f ,(string-append "chatter:" fmt) . ,(cdr msg))))))
              ((string? fmt)
               ;; do nothing -- no log-object defined
               #t)
              (else
               (error "bad call to chatter-to-log, with args: ~s" msg)))))))

) ; end of module
