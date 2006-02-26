;; emacs interaction for Quaestor.
;;
;; Defines function quaestor-eval-defun, which sends the current defun to
;; Quaestor, using the PUT interface to the URL in the variable
;; quaestor-update-url.
;;
;; Defines C-M-x to call it.
;;
;; Invoke by (load-file ...)
;;
;; Some parts of the following are closely based on (ie, nicked from) url-dav.el

;; Is this right...?
(eval-when-compile (require 'url))

(defvar quaestor-update-url "http://localhost:8080/quaestor/code")

(defmacro quaestor-http-success-p (status)
  "Return whether PROPERTIES was the result of a successful PUT request"
  `(= (/ (or ,status 500) 100) 2))

(defun quaestor-upload-code (url content)
  "HTTP-PUT the given CONTENT to the URL.  Return true if successful."
  (let ((buffer nil)
        (result nil)
        (url-request-method "PUT")
        (url-request-data content)
        (url-request-extra-headers nil))
    (setq buffer (url-retrieve-synchronously url))
    (when buffer
      (unwind-protect
          (with-current-buffer buffer
            (setq result (quaestor-http-success-p url-http-response-status)))
        (kill-buffer buffer)))
    result))

(defun current-function-as-string ()
  "Return the contents of the current function as a string."
  ;(interactive)
  (save-excursion
    (mark-defun)
    (buffer-substring (point) (mark))))

(defun quaestor-eval-defun ()
  "Upload the current defun to Quaestor"
  (interactive)
  (let ((is-ok (quaestor-upload-code quaestor-update-url
                                     (current-function-as-string))))
      (message (if is-ok "defun uploaded" "error uploading defun"))))

;(define-key lisp-mode-shared-map [(meta control x)] 'quaestor-eval-defun)
(local-set-key [(meta control x)] 'quaestor-eval-defun)
