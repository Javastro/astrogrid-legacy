;; A list of test cases for the utils package.

(require-library 'quaestor/utils)
(import utils)

(expect sexp->xml-1
        "<p>hello <em>there</em></p>\n"
        (sexp->xml '(p "hello " (em there))))
(expect sexp->xml-2
        "<p>hello <em>again</em></p>"
        (sexp->xml '(p "hello " (em there))))

