;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Support for the `knowledgebase' for Quaestor

(require-library 'quaestor/jena)

(require-library 'sisc/libs/srfi/srfi-1)
(import* srfi-1
         filter)

(module knowledgebase
  (kb-new
   kb-get
   kb-discard
   kb-get-names)

  (import jena)

  ;; A `knowledgebase' is a named model, consisting of a number of named
  ;; `submodels'.  When any of these are updated, the new submodel is
  ;; merged with the main model.
  ;;
  ;; The knowledgebase object that is returned is a function which takes
  ;; a number of subcommands.

  ;; The model list is a list of pairs (name-symbol . knowledgebase-proc)
  (define _model-list '())

  ;; Given a string or symbol, return a symbol.  If it's neither a
  ;; string nor a symbol, return #f
  (define (as-symbol s)
    (cond ((symbol? s)
           s)
          ((string? s)
           (string->symbol s))
          (else
           #f)))

  ;; Retrieve the knowledgebase with the given name, which is
  ;; a symbol or string.  Return #f if there is no KB with this name.
  (define (kb-get kb-name-param)
    (let ((kb-name (as-symbol kb-name-param)))
      (or kb-name
          (error 'kb-get "bad call to kb-get with object ~s" kb-name-param))
      (let ((kbpair (assq kb-name _model-list)))
        (and kbpair (cdr kbpair)))))

  ;; Create a new knowledgebase from scratch.  It must not already exist.
  ;; Return the new knowledgebase.  Either succeeds or throws an error.
  (define (kb-new kb-name-param)
    (let ((kb-name (as-symbol kb-name-param)))
      (or kb-name
          (error 'kb-new "bad call to kb-new with object ~s" kb-name-param))
      (if (kb-get kb-name)
          (error 'kb-new
                 "bad call to kb-new: knowledgebase ~a already exists"
                 kb-name))
      (let ((kb (make-kb kb-name)))
        (set! _model-list
              (cons (cons kb-name kb)
                    _model-list))
        kb)))

  ;; Returns a list of available knowledgebases.
  (define (kb-get-names)
    (map car _model-list))

  ;; Remove a given knowledgebase from the list.  Returns it, or
  ;; returns #f if no such knowledgebase existed
  (define (kb-discard kb-name-string)
    (let ((kb-name (as-symbol kb-name-string))
          (ret #f))
      (let loop ((new-list '())
                 (l _model-list))
        (cond ((null? l)
               (set! _model-list new-list))
              ((eq? kb-name (caar l))   ;found it
               (set! ret (cdar l))
               (loop new-list (cdr l)))
              (else
               (loop (cons (car l) new-list)
                     (cdr l))))
        ret)))

  ;; Add a new submodel to the model.  Returns the original or an updated
  ;; submodel list, or #f on any errors (there's nothing which triggers #f
  ;; at present, but it's documented to do this just in case)
  (define (add-submodel ;model
                        submodel-list
                        new-submodel-name
                        new-submodel-model
                        tbox?)
;;     (define-generic-java-method
;;       add)
    (if (not (and (symbol? new-submodel-name)
                  (is-java-type?
                   new-submodel-model
                   '|com.hp.hpl.jena.rdf.model.Model|)))
        (error 'make-kb "Bad call (add ~s ~s)"
               new-submodel-name new-submodel-model))
;;     ;; The following should be a bit more sophisticated:
;;     ;; we should remove the previous submodel before adding
;;     ;; the new one.  Can we detect any errors?
;;     (add model new-submodel-model)
    (let ((sm-list (assq new-submodel-name submodel-list)))
      (if sm-list
          (begin (set-cdr! (cdr sm-list) submodel)        ;already exists
                 submodel-list)
          (cons `(,new-submodel-name ,tbox? . ,new-submodel-model) ;new
                submodel-list))))
  

  ;; Create a new knowledgebase.  The knowledgebase is a procedure.
  ;; The model and submodels may be retrieved, and new ones added, by
  ;; calling the procedure with a command as the second argument.
  ;;
  ;;    (kb 'add-abox/tbox SUBMODEL-NAME SUBMODEL)
  ;;        Add or update a abox/tbox with the given name.
  ;;        SUBMODEL-NAME may be a string or a symbol.
  ;;    (kb 'get-model)
  ;;        Return the model, or #f if none exists.
  ;;    (kb 'get-model-abox/tbox)
  ;;        Return the merged abox/tbox submodels, or #f if there are none.
  ;;    (kb 'get-metadata-as-string)
  ;;    (kb 'get-metadata-as-jstring)
  ;;        Return the model's metadata, if any, as a Scheme or Java string
  ;;    (kb 'get SUBMODEL-NAME)
  ;;        Return the named submodel, or #f if none exists.
  ;;    (kb 'set-metadata INFO)
  ;;        Set the metadata to the arbitrary string INFO.
  ;;    (kb 'info)
  ;;        Return alist with info
  (define (make-kb kb-name)
    (let (;(model (new-empty-model)) ;overall model for the KB
          (submodels '())     ;a list of (name tbox? . submodel) pseudo-lists
          (metadata #f))

      (lambda (cmd . args)
        (case cmd
          ((add-abox add-tbox)
           ;; (kb 'add-abox/tbox SUBMODEL-NAME SUBMODEL)
           (if (not (= (length args) 2))
               (error 'make-kb
                      "Bad call to add-abox/tbox: wrong number of args in ~s"
                      args))
           (set! submodels
                 (add-submodel ;model
                               submodels
                               (as-symbol (car args))
                               (cadr args)
                               (eq? cmd 'add-tbox))))

          ((get-model)
           ;; (kb 'get-model [SUBMODEL-NAME])
           ;; Return model or #f
           (case (length args)
             ((0)
              ;model
              (rdf-merge-models (map cddr submodels))
              )
             ((1)
              (let ((sm (assq (as-symbol (car args))
                              submodels)))
                (and sm (cddr sm))))
             (else
              (error 'make-kb
                     "Bad call to get: wrong number of args in ~a"
                     args))))

          ((get-model-tbox get-model-abox)
           ;; (kb 'get-model-tbox/abox)
           ;; return merged models or #f if none
           (or (null? args)
               (error 'make-kb
                      "Bad call to get-model-tbox/abox: wrong no args ~s" args))
           (let ((models (filter (if (eq? cmd 'get-model-tbox)
                                     (lambda (x) (cadr x))
                                     (lambda (x) (not (cadr x))))
                                 submodels)))
             (if (null? models)
                 #f
                 (rdf-merge-models (map cddr models)))))

          ((get-metadata-as-string)
           (cond ((is-java-type? metadata '|java.lang.String|)
                  (->string metadata))
                 ((string? metadata)
                  metadata)
                 (else
                  (error "Impossible -- kb metadata isn't a string"))))

          ((get-metadata-as-jstring)
           (cond ((is-java-type? metadata '|java.lang.String|)
                  metadata)
                 ((string? metadata)
                  (->jstring metadata))
                 (else
                  (error "Impossible -- kb metadata isn't a string"))))

          ((set-metadata)
           ;; (kb 'set-metadata INFO)
           ;; Set model metadata to INFO
           (if (not (= (length args) 1))
               (error 'make-kb
                      "bad call to set-metadata: wrong number of args in ~s"
                      args))
           (set! metadata (car args)))

          ((info)
           (list (cons 'submodels (map (lambda (sm)
                                         `((name . ,(car sm))
                                           (tbox . ,(cadr sm))))
                                       submodels))))

          (else
           (error 'make-kb "impossible command for knowledgebase: ~s" cmd))))))

  )
