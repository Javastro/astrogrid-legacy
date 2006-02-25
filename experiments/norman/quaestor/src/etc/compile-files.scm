;; Compile .scm files.
;; Called from build.xml: for script arguments, see procedure COMPILE below.

(import file-manipulation)
(require-library 'sisc/libs/srfi/srfi-13)
(import* srfi-13
         string-index-right
         string-suffix?)

(import s2j)

;; Compile all the .scm files in the directory SCM-DIR, putting the resulting 
;; .scc files in the directory SCC-DIR.
(define (compile scm-dir scc-dir)
  (for-each (lambda (fn)
              (if (string-suffix? ".scm" fn)
                  (possibly-compile-file fn
                                         scm-dir
                                         scc-dir)))
            (directory-list scm-dir))
  0)

;; Given a file F, with no path, but which lives in FROMDIR, compile it if
;; the corresponding .scc file in TODIR is older.
(define (possibly-compile-file f fromdir todir)
  (let* ((dotpos (string-index-right f #\.))
         (basename (if dotpos
                       (substring f 0 dotpos)
                       f))
         (scm-file (string-append fromdir "/" f))
         (scc-file (string-append todir "/" basename ".scc")))
    (if (not (and (file-exists? scc-file)
                  (> (file-last-modified scc-file)
                     (file-last-modified scm-file))))
        (begin (format #t "compiling ~a -> ~a~%" scm-file scc-file)
               (compile-file scm-file scc-file)))))
