;; Test harness.
;; The first argument to MAIN should be the full path to this script.

(import s2j)

;; We don't actually import this library here, but the tested modules
;; do import quaestor-support
(require-library 'org/eurovotech/quaestor/scheme-wrapper-support)

(import* quaestor-support chatter)

(define failures-in-block?
  (let ((failures? #f))
    (lambda arg
      (cond ((null? arg)
             (let ((t failures?))
               (set! failures? #f)
               t))
            ((boolean? (car arg))
             (set! failures? (car arg)))
            (else
             (error "Bad call to FAILURES-IN-BLOCK?"))))))

;; (FAILURES) : return the number of failures
;; (FAILURES n) : increment the number of failures by n
(define failures
  (let ((n 0))
    (lambda arg
      (cond ((null? arg)
             n)
            ((number? (car arg))
             (set! n (+ n (car arg)))
             (failures-in-block? #t))
            (else
             (error "Bad call to FAILURES"))))))

;; (SUCCESS x) : increment the internal count of successes and return x
;; (SUCCESS) : return the number of successes, and reset the count.
(define success
  (let ((n 0))
    (lambda args
      (cond ((null? args)
             (let ((retval n))
               (set! n 0)
               retval))
            (else
             (set! n (+ n 1))
             (car args))))))

(define (test-fc id expected)
  (lambda (m e)
    (report "Test ~a~%    produced error~a: ~a~%    expected ~s"
            id
            (if (error-location m)
                (format #f " in ~a" (error-location m))
                "")
            (or (error-message m)
                (error-message (error-parent-error m))
                (error-message
                 (error-parent-error
                  (error-parent-error m)))
                m)
            expected)
    (failures 1)))

;; Expect the body to evaluate to the value EXPECTED
(define-syntax expect
  (syntax-rules ()
    ((_ id expected body ...)
     (with/fc (test-fc (quote id) expected)
       (lambda ()
         (let ((test ((lambda ()
                        body ...))))
           (or (success (equal? expected test))
               (begin (report "Test ~a~%    produced ~s~%    expected ~s"
                              (quote id) test expected)
                      (failures 1)))))))))

;; Expect the body to throw an error
(define-syntax expect-failure
  (syntax-rules ()
    ((_ id body ...)
     (with/fc (lambda (m e)
                ;;failed -- OK
                (success #t))
        (lambda ()
          (let ((test ((lambda () body ...))))
            (report "Test ~a~%    produced ~s~%    expected ERROR"
                    (quote id) test)
            (failures 1)))))))

;; Expect the body to evaluate to some non-#f result
(define-syntax expect-true
  (syntax-rules ()
    ((_ id body ...)
     (with/fc (test-fc (quote id) #t)
       (lambda ()
         (or (success ((lambda () body ...)))
             (begin (report "Test ~a: expected true, was false"
                            (quote id))
                    (failures 1))))))))

(define files-to-test '("quaestor/utils.scm"
                        "quaestor/knowledgebase.scm"
                        "quaestor/jena.scm"
                        "quaestor/sparql.scm"
                        "util/sexp-xml.scm"
                        "util/xmlrpc.scm"
                        ))

(define (main . args)
  (define-generic-java-method
    exit)
  (define-java-class <java.lang.system>)
  ;(chatter (current-output-port))
  (let ((total-ok 0))
    (with-current-url (car args)
      (lambda ()
        (for-each (lambda (f)
                    (let ((n-ok (run-test-file f)))
                      (set! total-ok (+ total-ok n-ok))
                      (report "...~a ok" n-ok)
                      (if (failures-in-block?)
                          (report "...and some FAILUREs (cumulative ~a)" (failures)))))
                  files-to-test)))
    (report "Tests successful: ~a" total-ok)
    (if (> (failures) 0)
        (report "Tests FAILED:     ~a" (failures))))
  (exit (java-null <java.lang.system>) (->jint (failures))))

(define (run-test-file file-name)
  (let ((url (normalize-url (current-url) file-name)))
    (or url
        (error (format #f "Can't find resource ~a" file-name)))
    (report "Running tests in ~a..." file-name)
    (load url)
    (success)))

(define report
  (let ((chatter? #t))
    (lambda (fmt . args)
      (cond ((boolean? fmt)
             (set! chatter? fmt))
            (chatter?
             (apply format `(#t ,(string-append fmt "~%") . ,args)))
            (else
             #f)))))


