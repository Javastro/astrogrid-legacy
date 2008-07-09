;; a few test cases for the quaestor-support package,
;; in file src/main/org/eurovotech/quaestor/scheme-wrapper-support.scm
;;
;; Yes, this test support is rather slim, but since these functions are used
;; extensively throughout the code, they do tend to get battered anyway.
;; Still, if any bugs show up in them, we can add tests for them here.

(import quaestor-support)

(expect report-exception-1
        '((t1 1 "hello")
          (t2 "x" "hello there"))
        (map (lambda (l)
               (with/fc
                (lambda (m e)
                  (list (error-location m)
                        (car (error-message m))
                        (cdr (error-message m))))
                (lambda ()
                  (apply REPORT-EXCEPTION l))))
             '((t1 1 "hello")
               (t2 "x" "hello ~a" "there"))))
