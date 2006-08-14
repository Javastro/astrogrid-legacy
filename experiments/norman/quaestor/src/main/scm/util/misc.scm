;; Miscellaneous utilities for Quaestor
;; The functions in ../quaestor/utils.scm are utilities specific to Quaestor;
;; these are just miscellaneous.

(module misc
    (sort-list)

;; Simple-minded Quicksort implementation
;; Given a list, return a new list sorted according to the predicate <=
(define (sort-list l <=)
  (define (merge-lists ina inb)
    (let loop ((res '())
               (a ina)
               (b inb))
      (cond
       ((null? a)
        (append (reverse res) b))
       ((null? b)
        (append (reverse res) a))
       (else
        (if (<= (car a) (car b))
            (loop (cons (car a) res)
                  (cdr a)
                  b)
            (loop (cons (car b) res)
                  a
                  (cdr b)))))))

  (define (partition-list pe inl)
    (let loop ((pa '())
               (pb '())
               (l inl))
      (if (null? l)
          (values pa pb)
          (if (<= (car l) pe)
              (loop (cons (car l) pa) pb (cdr l))
              (loop pa (cons (car l) pb) (cdr l))))))

  (case (length l)
    ((0 1) l)
    ((2) (if (<= (car l) (cadr l))
             l
             (list (cadr l) (car l))))
    (else
     (let ((pe (car l)))
       (call-with-values
           (lambda () (partition-list pe (cdr l)))
         (lambda (left right)
           (merge-lists (sort-list left <=) (cons pe (sort-list right <=)))))))))

)
