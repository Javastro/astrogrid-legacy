;; SISC library module for Quaestor

(import s2j)
(require-library 'sisc/libs/srfi/srfi-1)
(require-library 'sisc/libs/srfi/srfi-13)

(require-library 'util/lambda-contract)


(module utils
 (;sexp->xml
  collection->list
  iterator->list
  jlist->list
  enumeration->list
  java-retrieve-static-object
  is-java-type?
  url-decode-to-jstring
  report-exception
  chatter
  parse-http-accept-header
  parse-query-string
  format-error-record
  )

(import* srfi-1
         remove)
(import* srfi-13
         string-index
         string-index-right)

;; predicates used in contracts
(define-java-classes
  <java.lang.string>)
(define (jiterator? x)
  (is-java-type? x '|java.util.Iterator|))
(define (jlist? x)
  (is-java-type? x '|java.util.List|))
(define (jenumeration? x)
  (is-java-type? x '|java.util.Enumeration|))
(define (jstring? x)
  (is-java-type? x <java.lang.string>))
;; (define (jstring-or-null? x) ; null objects are also deemed true
;;   (or (java-null? x)
;;       (is-java-type? x <java.lang.string>)))


;; Given a Java colllection, extract its contents into a list.
;; Return the empty list if the input collection is (Java) null.
(define/contract (collection->list
                  (coll (or (java-null? coll)
                            (is-java-type? coll '|java.util.Collection|)))
                  -> list?)
  (define-generic-java-methods
    iterator)
  (if (java-null? coll)
      '()
      (iterator->list (iterator coll))))

;; Given a Java iterator, JITER, extract each of its contents into a list
(define/contract (iterator->list (jiter jiterator?) -> list?)
  (define-generic-java-methods
    (has-next? |hasNext|)
    next)
  (let loop ((l '()))
    (if (->boolean (has-next? jiter))
        (loop (cons (next jiter) l))
        (reverse l))))

;; The same, but taking a Java List as input
(define/contract (jlist->list (jlist jlist?) -> list?)
  (define-generic-java-method iterator)
  (iterator->list (iterator jlist)))

;; The same, but taking a Java Enumeration as input
(define/contract (enumeration->list (enum jenumeration?) -> list?)
  (define-generic-java-methods
    has-more-elements
    next-element)
  (if (->boolean (has-more-elements enum))
      (let ((n (next-element enum)))
        (cons n
              (enumeration->list enum)))
      '()))

 ;; Retrieve a Java static object by its full Java name, which may be a symbol
 ;; such as '|java.lang.System.out|, or a string
 (define (java-retrieve-static-object javaobj)
   (let ((javaobjname (if (string? javaobj)
                          javaobj
                          (symbol->string javaobj))))
     (let ((dot (string-index-right javaobjname #\.)))
       (and dot
            (let ((classname (substring javaobjname 0 dot))
                  (objname (substring javaobjname
                                      (+ 1 dot)
                                      (string-length javaobjname))))
              ((generic-java-field-accessor (string->symbol objname))
               (java-null (java-class (string->symbol classname)))))))))


 ;; Verifies that a JOBJECT is an instance of a Java class CLASS (which
 ;; can be a class, a string, or a symbol).  Returns true if so, #f it
 ;; it's not, or if it's not a Java object at all.
 (define (is-java-type? jobject class)
   (if (java-object? jobject)
       (->boolean ((generic-java-method '|isInstance|)
                   (if (java-class? class)
                       class
                       (java-class (if (string? class)
                                       (string->symbol class)
                                       class)))
                   jobject))
       #f))

;; Given a string S, return the URL-decoded string as a jstring
(define/contract (url-decode-to-jstring (s string?) -> jstring?)
  (define-java-classes
    (<url-decoder> |java.net.URLDecoder|))
  (define-generic-java-method
    decode)
  (decode (java-null <url-decoder>)
          (->jstring s)
          (->jstring "UTF-8")))

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
;;     (chatter fmt . args)  ; Format a message and return #t.
;;     (chatter)             ; return list of messages, or #f if none,
;;                           ; and clear list.
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

    (let ((chatter-list (make-circular-list 8)))
      (lambda msg
        (if (null? msg)                 ;retrieve messages
            (let ((r (reduce-circular-list (lambda (l r) (if l (cons l r) r))
                                           '()
                                           chatter-list)))
              (set! chatter-list (make-circular-list 8)) ; new clear list
              (and (not (null? r)) r))  ;return list or #f
            (begin (set! chatter-list   ;append a new message
                         (append-circular-list chatter-list
                                               (apply format (cons #f msg))))
                   #t))))))             ;return true

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
    (cond ((is-java-type? msg <throwable>)
           (get-message (get-undeclared-throwable msg)))
          ((is-java-type? msg <exception>)
           (to-string msg))
          ((format-error-ancestors rec))
;;           (msg)
;;           ((error-message (error-parent-error rec)))
;;           ((error-message
;;             (error-parent-error
;;              (error-parent-error rec))))
          (else
           rec))))

 ;; Parse an HTTP Accept header into a list of acceptable MIME types.
 ;; The JHEADER is a Java string in the format required by RFC 2616, that is,
 ;; as a comma-separated list of media-type tokens:
 ;;   media-type     = type "/" subtype *( ";" parameter )
 ;;   parameter      = attribute "=" value
 ;;   attribute      = token
 ;;   value          = token | quoted-string
 ;; There's a good discussion, albeit in a Python context, at
 ;; <http://www.xml.com/pub/a/2005/06/08/restful.html>
 ;; The result is sorted in order of descending weight parameter, though
 ;; the sort isn't stable, so order of equally-weighted types isn't preserved.
 (define parse-http-accept-header
   (let ((commas #f)
         (parameters #f))
     (define-generic-java-methods
       split
       compile
       matcher
       matches
       group
       parse-float)
     (define-java-classes
       <java.util.regex.pattern>
       <java.lang.float>)
     (lambda (jheader)
       (if (not commas)
           (begin (set! commas
                        (compile (java-null <java.util.regex.pattern>)
                                 (->jstring " *, *")))
                  (set! parameters
                        (compile (java-null <java.util.regex.pattern>)
                                 (->jstring "\([^;]*\); *q=\([0-9.]+\).*")))))
       (let ((pairs (map (lambda (js)
                           (let ((js-matcher (matcher parameters js)))
                             (if (->boolean (matches js-matcher))
                                 (cons (->string
                                        (group js-matcher (->jint 1)))
                                       (string->number
                                        (->string
                                         (group js-matcher (->jint 2)))))
                                 (cons (->string js) 1))))
                         (->list (split commas jheader)))))
         (remove (lambda (s) (= (string-length s) 0))
                 (map car
                      (sort-list pairs
                                 (lambda (a b)
                                   (> (cdr a) (cdr b))))))))))

 ;; Simple implementation of heapsort (?).  Probably not massively efficient
 ;; but there isn't a SRFI for sorting yet.  Sort the given list L with
 ;; respect to the given sorting function, <=.
 ;;
 ;; NOTE: this isn't a stable sort.
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
            (merge-lists (sort-list left <=)
                         (cons pe (sort-list right <=)))))))))

;; parse-query-string string -> pair-of-strings
;;
;; Given a query string "keyword=value", parse it into a pair of strings
;; representing the text before and after the equals sign.  If either
;; is missing, replace it with #f.  If the query is #f, return (#f . #f)
;;
;; Very simple-minded.  Query strings can be more generic than this,
;; but they're not, in this application.
(define/contract (parse-query-string (qs (or (not qs) (string? qs)))
                                     -> pair?)
  (cond ((not qs)
         '(#f . #f))
        ((string-index qs #\=)
         => (lambda (index)
              (let ((k (substring qs 0 index))
                    (v (substring qs (+ index 1) (string-length qs))))
                (cons (if (> (string-length k) 0)
                          k
                          #f)
                      (if (> (string-length v) 0)
                          v
                          #f)))))
        ((= (string-length qs) 0)
         '(#f . #f))
        (else
         (cons qs #f))))

)

