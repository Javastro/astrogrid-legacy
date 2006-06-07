;; Specify lambdas with contracts:
;;
;; (lambda/contract (<argspec>*)    body ...)
;; (lambda/contract (<argspec>* -> ensure?)    body ...)
;; (define/contract (funcname <argspec>*) body ...)
;; (define/contract (funcname <argspec>* -> ensure?) body ...)
;;
;; <argspec>* is a list of zero or more <argspec>
;; <argspec> is either ARGUMENT or (ARGUMENT REQUIRE?) or (ARGUMENT (<expr>))
;;     where <expr> is an expression involving ARGUMENT, which evaluates
;;     to true or false
;;
;; REQUIRE? and ENSURE? are predicates.
;;
;; For example:
;;
;;    (define/contract (my-sqrt x
;;                              (y positive?)
;;                              (z (> z 0))
;;                              -> positive?) (sqrt (+ x y z)))
;;
;; The ENSURE? predicate must, at present, be a procedure, and can't yet
;; be an expr.
;;
;; Code which uses these transformers may define the handler function
;; (VIOLATED-CONTRACT FMT . ARGS), which is called when a contract is
;; violated, and which takes a format and arguments and handles them
;; as appropriate.  The default VIOLATED-CONTRACT function throws an ERROR.
;;
;; $Revision: 1.2 $


;; VIOLATED-CONTRACT is called with a format string and arguments when
;; a contract is violated
(define (violated-contract fmt . args)
  (error (apply format `(#f ,fmt . ,args))))

;; syntax-error trick not used
;; (define-syntax syntax-error
;;    (syntax-rules ()
;;      ((syntax-error) (syntax-error "Bad use of syntax error!"))))

(define-syntax lambda/contract
  (syntax-rules (->)
    ;; interface
    ((_ ((arg (req ...)) . args) form . forms)
     (lambda/contract "gen" () args (arg)
                      ((,(lambda (arg) (req ...)) ,arg (req ...) arg))
                      form . forms))
    ((_ ((arg req) . args) form . forms)
     (lambda/contract "gen" () args (arg) 
                      ((,req ,arg req arg))
                      form . forms))
    ((_ (-> ensure?) form . forms)
     (lambda/contract "gen" () (-> ensure?) () ()
                      form . forms))
    ((_ (arg . args) form . forms)
     (lambda/contract "gen" () args (arg) ()
                      form . forms))
    ((_ () form . forms)                ;normal lambda
     (lambda () form . forms))

    ;; generate the final LAMBDA or DEFINE

    ;;     ((_ "gen" funcname (-> (expr . exprs)) arglist reqlist form . forms)
    ;;      (lambda arglist
    ;;        (lambda/contract "dochecks" funcname reqlist)
    ;;        (let ((result (let () form . forms)))
    ;;          (if ((lambda (x) (expr . exprs)) result)
    ;;              result
    ;;              (violated-contract
    ;;               "Func ~a failed: blame ~a: result ~a does not satisfy ~a"
    ;;               (quote funcname) (quote funcname) result (expr . exprs))))))

    ;; No FUNCNAME => produce a LAMBDA
    ((_ "gen" () (-> ensure?) arglist reqlist form . forms)
     (lambda arglist
       (lambda/contract "dochecks" "<anonymous>" reqlist)
       (let ((result (let () form . forms)))
         (if (ensure? result)
             result
             (violated-contract
              "Func <anonymous> failed: blame <anonymous>: result ~a does not satisfy ~a"
              result (quote ensure?))))))
    ((_ "gen" () () arglist reqlist form . forms)
     (lambda arglist
       (lambda/contract "dochecks" "<anonymous>" reqlist)
       (let ()
         form . forms)))

    ;; Non-null FUNCNAME => produce a DEFINE
    ((_ "gen" funcname (-> ensure?) arglist reqlist form . forms)
     (define (funcname . arglist)
       (lambda/contract "dochecks" funcname reqlist)
       (let ((result (let () form . forms)))
         (if (ensure? result)
             result
             (violated-contract
              "Func ~a failed: blame ~a: result ~a does not satisfy ~a"
              (quote funcname) (quote funcname) result (quote ensure?))))))
    ((_ "gen" funcname () arglist reqlist form . forms)
     (define (funcname . arglist)
       (lambda/contract "dochecks" funcname reqlist)
       (let ()
         form . forms)))

    ;; "dochecks" produces the reqlist checks, if any
    ((_ "dochecks" funcname ())
     #f)
    ((_ "dochecks" funcname reqlist)
     (let loop ((checks (quasiquote reqlist)))
       (or (null? checks)
           (if ((caar checks) (cadar checks))
               (loop (cdr checks))
               (let ((c (car checks)))
                 (violated-contract
                  "Func ~a failed: blame caller: argument ~a=~a does not satisfy ~a"
                  (quote funcname)
                  (cadddr c)
                  (cadr c)
                  (caddr c)))))))

    ;; iterate through the arguments
    ((_ "gen" funcname ((arg (req ...)) . args) (arglist ...) reqlist
        form . forms)
     (lambda/contract "gen" funcname args (arglist ... arg) 
                      ((,(lambda (arg) (req ...)) ,arg (req ...) arg) . reqlist) 
                      form . forms))
    ((_ "gen" funcname ((arg req) . args) (arglist ...) reqlist form . forms)
     (lambda/contract "gen" funcname args (arglist ... arg) ((,req ,arg req arg) . reqlist) form . forms))
    ((_ "gen" funcname (arg . args) (arglist ...) reqlist form . forms)
     (lambda/contract "gen" funcname args (arglist ... arg) reqlist form . forms))))

(define-syntax define/contract
  (syntax-rules (->)
    ((_ (funcname (arg (req ...)) . args) form . forms)
     (lambda/contract "gen" funcname args (arg)
                      ((,(lambda (arg) (req ...)) ,arg (req ...) arg))
                      form . forms))
    ((_ (funcname (arg req) . args) form . forms)
     (lambda/contract "gen" funcname args (arg)
                      ((,req ,arg req arg)) 
                      form . forms))
    ((_ (funcname -> ensure?) form . forms)
     (lambda/contract "gen" funcname (-> ensure?) () () form . forms))
    ((_ (funcname arg . args) form . forms)
     (lambda/contract "gen" funcname args (arg) () form . forms))))



;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; ;;
;; ;; Following is test code

;; ;; setup

;; (define fails
;;   (let ((count 0))
;;     (lambda args
;;       (if (null? args)
;;           (begin (if (= count 0)
;;                      (display "All tests passed!")
;;                      (display (format #f "Total failures: ~a" count)))
;;                  (newline))
;;           (begin (display (format #f "Test failure evaluating ~a~%  produced: ~s~%  expected: ~s~%"
;;                          (car args) (cadr args) (caddr args)))
;;                  (set! count (+ count 1)))))))

;; (define-syntax expect
;;   (syntax-rules ()
;;     ((_ expected body ...)
;;      (let ((result (call-with-current-continuation
;;                     (lambda (escape)
;;                       (violated-contract escape)
;;                       ((lambda () body ...))))))
;;        (if (not (eq? result expected))
;;            (fails '(body ...) result expected))))))

;; ;; violated-contract escape-producedure -> undef
;; ;; violated-contract fmt . args -> (escape #f)
;; (define violated-contract
;;   (let ((escape #f))
;;     (lambda (arg1 . args)
;;       (if (null? args)
;;           (set! escape arg1)
;;           (escape #f)))))

;; (define (positive? x) (and (real? x) (> x 0)))
;; (define (negative? x) (and (real? x) (< x 0)))

;; ;; tests

;; ;; lambda/contract

;; ;; degenerate case: no contract
;; (define f0 (lambda/contract (x) (* x x)))
;; (expect 4 (f0 2))

;; ;; simple one-arg case
;; (define f1 (lambda/contract ((x positive?)) (* x x)))
;; (expect 4 (f1 2))
;; (expect #f (f1 -2))

;; ;; multiple args and result checking
;; (define f2 (lambda/contract ((x positive?) (y negative?) -> positive?)
;;                             (- (* x y))))
;; (expect 6 (f2 2 -3))
;; (expect #f (f2 2 3))

;; ;; result fails check
;; (define f3 (lambda/contract (x -> negative?) (* x x)))
;; (expect #f (f3 2))

;; ;; procedure check
;; (define f4a (lambda/contract (x (y (> y 0))) (* x y)))
;; (expect 6 (f4a 2 3))
;; (expect #f (f4a 2 -3))
;; ;; same, but with check on first arg
;; (define f4b (lambda/contract ((x (> x 0)) (y (< y 0))) (* x y)))
;; (expect -6 (f4b 2 -3))
;; (expect #f (f4b 2 3))

;; ;; ;; procedure checks on result
;; ;; doesn't work
;; ;; (define f5 (lambda/contract (x y -> (> x 0)) (* x y))) (newline)
;; ;; (expect 6 (f5 2 3))
;; ;; (expect #f (f5 2 -3))


;; ;; define/contract

;; ;; degenerate case: no contract
;; (define/contract (f10 x y) (* x y))
;; (expect 6 (f10 2 3))

;; ;; simple case
;; (define/contract (f11 x -> positive?) (* x x))
;; (expect 4 (f11 2))

;; ;; no-arg case
;; (define/contract (f12 -> positive?) -2)
;; (expect #f (f12))

;; ;; multiple args and result checking, with define/contract
;; (define/contract (f13 (x positive?) (y negative?) -> positive?)
;;   (- (* x y)))
;; (expect 6 (f13 2 -3))
;; (expect #f (f13 2 3))

;; ;; procedure checks
;; (define/contract (f14 (x (> x 0)) (y (< y 0))) (* x y))
;; (expect -6 (f14 2 -3))
;; (expect #f (f14 2 3))

;; ;; report number of failures
;; (fails)
