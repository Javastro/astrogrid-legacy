(require-library 'sisc/libs/srfi/srfi-26) ;for cut and cute
(module quaestor-support
    (apply-with-top-fc
     make-fc
     format-error-record
     report-exception
     chatter)

(import s2j)
(import debugging)                      ;for enhanced print-stack-trace
(import string-io)                      ;for with-output-to-string
(import srfi-26)

;; APPLY-WITH-TOP-FC procedure args... -> object
;; This is intended to be a main entry point for the functions here.
;; Call the given procedure with the given arguments, in the context
;; of a suitable failure-continuation.
;;
;; Since the functions which are called via this typically have their
;; own error handlers, this failure-continuation will generally only
;; be called if something quite bad has gone wrong; therefore the
;; failure-continuation shows debugging information and chatter, and
;; throws a Java exception (which will be wrapped in a SchemeException
;; before it gets back to the servlet code).
(define (apply-with-top-fc proc . args)
  (with/fc
      (lambda (m kontinuation)
        (define-java-class <javax.servlet.servlet-exception>)
        (error
         (java-new
          <javax.servlet.servlet-exception>
          (->jstring
           (format #f "Top-level error: ~a~%" (format-error-record m))))))
;; The following is much fuller, but unnecessary, since for this level
;; of debugging I can probably just look in the logs.
;;           (format #f "Top-level error: ~a~%~a~%~%Stack trace: ~a~%"
;;                   (format-error-record m)
;;                   (let ((c (chatter)))
;;                     (cond ((list? c)    ;normal case
;;                            (apply string-append
;;                                   (map (lambda (x)
;;                                          (format #f "[chatter: ~a]~%" x))
;;                                        c)))
;;                           ((not c)      ;no chatter
;;                            "")
;;                           (else         ;can't happen!
;;                            (format #f
;;                                    "[Can't happen: (chatter) produced ~s]" c))))
;;                   (with-output-to-string
;;                     (lambda () (print-stack-trace kontinuation))))
    (lambda ()
      (chatter "Applying proc ~s..." proc)
      (parameterize ((suppressed-stack-trace-source-kinds '()))
                    (apply proc args)))))

;; MAKE-FC : symbol -> procedure
;; MAKE-FC : boolean -> void
;; MAKE-FC : java-object -> void
;; MAKE-FC : output-port -> void
;;
;; Make a SISC failure continuation.  Return a two-argument procedure
;; which can be used as the handler for with-failure-continuation.
;; See REPORT-EXCEPTION for an error procedure which allows you to override
;; the status given here.
;;
;; If the argument is a boolean, then instead switch on or off the display
;; of a stack trace on errors.
;;
;; The last two are for logging.  In each case, use the object to log to;
;; the java-object must be something which has a log(String) method on it.
(define make-fc
  (let ((show-stack-trace-on-error? #t)
        (logger (lambda (fmt . args) (apply format `(#t ,fmt . ,args)))))
    (lambda (fc-arg)
      (cond ((symbol? fc-arg)
             ;; normal case
             (lambda (error-record cont)
               (let* ((msg-or-pair (error-message error-record))
                      (show-debugging? (not (pair? msg-or-pair)))
                      ;(msg (if (pair? msg-or-pair) (cdr msg-or-pair) msg-or-pair))
                      (msg (cond ((pair? msg-or-pair)
                                  (cdr msg-or-pair))
                                 (msg-or-pair)
                                 (else (format-error-record error-record)))) ;getting desperate now...
                      ;; I'm not convinced this extra debugging info is helpful
                      (full-msg
                       (and show-stack-trace-on-error?
                            (format #f "~%Error: ~a~%~a~%~%Stack trace:~%~a~%"
                                    (format-error-record error-record)
                                    (let ((c (chatter)))
                                      (cond ((list? c) ;normal case
                                             (apply string-append
                                                    (map (cut format #f "[chatter: ~a]~%" <>)
                                                         c)))
                                            ((not c) ;no chatter
                                             "")
                                            (else ;can't happen!
                                             (format #f
                                                     "[Can't happen: (chatter) produced ~s]" c))))
                                    (with-output-to-string
                                      (lambda () (print-stack-trace cont)))))))
                 ;(logger "MAKE-FC: ~a~%full message: ~a [[[record=~s]]]~%" msg (or full-msg "") error-record)
                 (logger "MAKE-FC: ~a~%~a~%" msg (or full-msg ""))
                 (list (if (pair? msg-or-pair)
                           (car msg-or-pair)
                           fc-arg)
                       `("That didn't work"
                         (p "Something's unfortunately gone wrong.")
                         (pre (@ (class error))
                              ,(format #f "~%Error~a: ~a~%"
                                       (cond ((error-location error-record)
                                              => (cut format #f " at ~a" <>))
                                             (else ""))
                                       msg))
                         (p "For further information, see the server logs")
;;                          ,(if full-msg
;;                               `(pre ,full-msg)
;;                               '(p "For further information, see the server logs"))
                         )))))
            ((boolean? fc-arg)
             (set! show-stack-trace-on-error? fc-arg))
            ((java-object? fc-arg)
             (set! logger
                   (lambda (log-format . args)
                     (define-generic-java-methods log)
                     (log fc-arg (->jstring (apply format `(#f ,log-format . ,args)))))))
            ((output-port? fc-arg)
             (set! logger
                   (lambda args
                     (apply format (cons fc-arg args)))))
            (else
             (error 'make-fc "Bad argument to make-fc: ~s" fc-arg))))))

;; Format the given error record, as passed as the first argument of a
;; failure-continuation.  It's to be formatted for display.
;; This ought to be able to handle most of the
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
               (parent-msg (format-error-ancestors (error-parent-error rec)))
               (loc (error-location rec)))
           (cond ((and (not msg) (not parent-msg))
                  "Eh?  What just happened?")
                 ((not msg)
                  parent-msg)
                 ((not loc)
                  (format #f "Error: ~a~a"
                          (if (java-object? msg)
                              (->string (to-string msg))
                              msg)
                          (if parent-msg (format #f "~%Caused by: ~a" parent-msg) "")))
                 ((and (eq? loc 'java/invoke-method) parent-msg)
                  ;; this error record is uninteresting -- it seems to wrap
                  ;; all scheme exceptions; so skip it
                  parent-msg)
                 ((eq? loc 'java/invoke-method)
                  ;; but here we seem to have no choice but to pass it on
                  msg)
                 (else
                  (format #f "Error at ~s: ~a~a"
                          loc msg (if parent-msg (format #f "~%Caused by: ~a" parent-msg) "")))))))
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

;; report-exception : symbol symbol string . obj -> does-not-return
;; Variant of ERROR, which can be called in a region handled by the failure
;; continuation created by MAKE-FC.  Throw an error, in the given LOCATION,
;; with a message formatted with the given FMT and ARGS.  However instead
;; of exiting with the status code defaulted when the fc was created
;; by MAKE-FC, use the given NEW-STATUS.
;;
;; The NEW-STATUS must be a symbol, such as '|SC_OK|, indicating one of the
;; status codes defined in class javax.servlet.http.HttpServletResponse.
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
;;     (chatter number?)     ; make the circular buffer this many elements long
;;     (chatter Object)      ; Object must have a log(String) method, return #t
;;     (chatter <output-port>) ; copy chatter to port, return #t
;; In the two last cases, subsequent calls to (chatter fmt . args) will put the
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
              ((string? (car msg))
               (apply chatter-to-log msg)
               (set! chatter-list   ;append a new message
                     (append-circular-list chatter-list
                                           (apply format (cons #f msg))))
               #t)
              ((number? (car msg))
               (set! chatter-list (make-circular-list (car msg))))
              ((chatter-to-log (car msg))) ;try giving it to the log function
              (else
               (error "chatter: I don't know what you expect me to do with a ~s" msg)))))))

;; CHATTER-TO-LOG : string . args -> void
;; CHATTER-TO-LOG : <Object> -> #t
;; CHATTER-TO-LOG : <output-port> -> #t
;; CHATTER-TO-LOG : anything -> #f
;; If given a Java object, it must be an object which has a
;; log(String) method, and this object is saved.
;; If given a message (a list of objects starting with a format
;; string), and if the procedure log-it is present, then it sends the given
;; string to the indicated logging stream.
;;
;; Called from CHATTER.
(define chatter-to-log
  (let ((log-it #f))
    (lambda msg
      (let ((fmt (and (not (null? msg))
                      (car msg))))
        (cond ((not fmt)
               (error "bad call to chatter-to-log, with null argument"))
              ((and (string? fmt)
                    log-it)
               (apply log-it msg))
              ((string? fmt)
               ;; do nothing -- no log-it defined
               #t)
              ((java-object? fmt)       ;fmt is an object which should have a log(String) method
               ;; A sophistication would be to use the Java reflection interface
               ;; to check that the object does indeed have a log(String) method.
               ;; See SISC function java-class-declared-methods
               (set! log-it
                     (lambda (log-format . args)
                       (define-generic-java-methods log)
                       (log fmt (->jstring (apply format `(#f ,(string-append "chatter:" log-format) . ,args))))))
               (make-fc fmt)
               #t)
              ((output-port? fmt)
               (set! log-it
                     (lambda args
                       (apply format (cons fmt args))))
               (make-fc fmt)
               #t)
              (else #f))))))

) ; end of module
