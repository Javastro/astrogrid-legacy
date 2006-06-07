;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Support for the `knowledgebase' for Quaestor

(import s2j)

(require-library 'quaestor/jena)
(require-library 'quaestor/utils)
(require-library 'util/lambda-contract)

(require-library 'sisc/libs/srfi/srfi-1)
(import* srfi-1
         filter)

(module knowledgebase
  (kb:new
   kb:get
   kb:discard
   kb:get-names
   kb:knowledgebase?)

  (import* jena
           rdf:ingest-from-stream
           rdf:get-reasoner
           rdf:merge-models)
  (import* utils is-java-type?)
  (define (jena-model? x)
    (is-java-type? x '|com.hp.hpl.jena.rdf.model.Model|))

  ;; A `knowledgebase' is a named model, consisting of a number of named
  ;; `submodels'.  When any of these are updated, the new submodel is
  ;; merged with the main model.
  ;;
  ;; The knowledgebase object that is returned is a function which takes
  ;; a number of subcommands.

  ;; The model list is a list of pairs (name-symbol . knowledgebase-proc)
  (define _model-list '())

  ;; We'd like to synchronize, below, on _model-list, but we can't
  ;; because it's not a Java object, so create a dummy Java object as
  ;; a lock.
  (define _model-list-lock-object (->jstring ""))

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
  (define (kb:get kb-name-param)
    (let ((kb-name (as-symbol kb-name-param)))
      (or kb-name
          (error 'kb:get "bad call to kb:get with object ~s" kb-name-param))
      (let ((kbpair (assq kb-name _model-list)))
        (and kbpair (cdr kbpair)))))

  ;; kb:new string -> knowledgebase
  ;;
  ;; Create a new knowledgebase from scratch, registering it with the given
  ;; KB-NAME-PARAM (a string).  It must not already exist.
  ;; Return the new knowledgebase.  Either succeeds or throws an error.
  (define (kb:new kb-name-param)
    (let ((kb-name (as-symbol kb-name-param)))
      (or kb-name
          (error 'kb:new "bad call to kb:new with object ~s" kb-name-param))
      (if (kb:get kb-name)
          (error 'kb:new
                 "bad call to kb:new: knowledgebase ~a already exists"
                 kb-name))
      (let ((kb (make-kb kb-name)))
        (java-synchronized _model-list-lock-object
          (lambda ()
            (set! _model-list
                  (cons (cons kb-name kb)
                        _model-list))))
        kb)))

  ;; kb:get-names -> list
  ;;
  ;; Returns a list of available knowledgebases.  Returns the names
  ;; as symbols, or a null list if there are none.
  (define (kb:get-names)
    (map car _model-list))

  ;; kb:discard knowledgebase -> knowledgebase/#f
  ;;
  ;; Remove a given knowledgebase from the list.  Returns it, or
  ;; returns #f if no such knowledgebase existed
  (define (kb:discard kb-name-string)
    (let ((kb-name (as-symbol kb-name-string))
          (ret #f))
      (java-synchronized _model-list-lock-object
        (lambda ()
          (let loop ((new-list '())
                     (l _model-list))
            (cond ((null? l)
                   (set! _model-list new-list))
                  ((eq? kb-name (caar l)) ;found it
                   (set! ret (cdar l))
                   (loop new-list (cdr l)))
                  (else
                   (loop (cons (car l) new-list)
                         (cdr l))))
            ret)))))

  ;; add-submodel list symbol jena-model boolean -> list
  ;; Add a new submodel to the model.  Returns the original or an updated
  ;; submodel list, or #f on any errors (there's nothing which triggers #f
  ;; at present, but it's documented to do this just in case)
  (define/contract (add-submodel (submodel-list      list?)
                                 (new-submodel-name  symbol?)
                                 (new-submodel-model jena-model?)
                                 (tbox?              boolean?)
                                 -> list?)
    (let ((sm-list (assq new-submodel-name submodel-list)))
      (if sm-list
          (begin (set-cdr! sm-list      ;already exists
                           (cons tbox? new-submodel-model))
                 submodel-list)
          (cons `(,new-submodel-name ,tbox? . ,new-submodel-model) ;new
                submodel-list))))
  

  ;; make-kb symbol -> knowledgebase
  ;;
  ;; Create a new knowledgebase.  The knowledgebase is a procedure.
  ;; The model and submodels may be retrieved, and new ones added, by
  ;; calling the procedure with a command as the second argument.
  ;;
  ;;    (kb 'add-abox/tbox SUBMODEL-NAME SUBMODEL)
  ;;        Add or update a abox/tbox with the given name.
  ;;        SUBMODEL-NAME may be a string or a symbol.  Return #t on success
  ;;    (kb 'get-inferencing-model)
  ;;        As with GET-MODEL, except that it is an inferencing model.
  ;;        Return #f on error.
  ;;    (kb 'get-model)
  ;;        Return the model, or #f if none exists.
  ;;    (kb 'get-model SUBMODEL-NAME)
  ;;        Return the named submodel, or #f if none exists.
  ;;        SUBMODEL-NAME may be a symbol or a string.
  ;;    (kb 'get-model-abox/tbox)
  ;;        Return the merged abox/tbox submodels, or #f if there are none.
  ;;    (kb 'get-metadata-as-string)
  ;;    (kb 'get-metadata-as-jstring)
  ;;        Return the model's metadata, if any, as a Scheme or Java string
  ;;    (kb 'get-name)
  ;;        Return the knowledgebase's name, as a symbol
  ;;    (kb 'has-model [SUBMODEL-NAME])
  ;;        Return true if the named (sub)model exists, that is, if
  ;;        (kb 'get-model [SUBMODEL-NAME]) would succeed.  Return #f otherwise.
  ;;    (kb 'set-metadata INFO)
  ;;        Set the metadata to the arbitrary string INFO.
  ;;    (kb 'info)
  ;;        Return alist with info
  (define (make-kb kb-name)
    (let ((submodels '())     ;a list of (name tbox? . submodel) pseudo-lists
          (myname kb-name)
          (metadata #f)
          (merged-model #f)
          (merged-tbox #f)
          (merged-abox #f)
          (inferencing-model #f)
          (sync-object (->jstring "")))

      (define (clear-memos)
        (set! merged-model #f)
        (set! merged-abox #f)
        (set! merged-tbox #f)
        (set! inferencing-model #f))

      ;; Return the abox or tbox.  Caches result in merged-abox/tbox.
      (define (get-abox-or-tbox tbox?)
        (java-synchronized sync-object
          (lambda ()
            (let ((models (filter (if tbox?
                                      (lambda (x) (cadr x))
                                      (lambda (x) (not (cadr x))))
                                  submodels)))
              (cond ((null? models)
                     #f)
                    ((and tbox? merged-tbox))
                    ((and (not tbox?) merged-abox))
                    (tbox?
                     (set! merged-tbox
                           (rdf:merge-models (map cddr models)))
                     merged-tbox)
                    (else
                     (set! merged-abox
                           (rdf:merge-models (map cddr models)))
                     merged-abox))))))

      (lambda (cmd . args)
        (case cmd

          ;;;;;;;;;;
          ;; Commands which explicitly mutate the knowledgebase
          
          ((add-abox add-tbox)
           ;; (kb 'add-abox/tbox SUBMODEL-NAME SUBMODEL)
           (if (not (= (length args) 2))
               (error 'make-kb
                      "Bad call to add-abox/tbox: wrong number of args in ~s"
                      args))
           (java-synchronized sync-object
             (lambda ()
               (set! submodels
                     (add-submodel submodels
                                   (as-symbol (car args))
                                   (cadr args)
                                   (eq? cmd 'add-tbox)))
               (clear-memos)))
           #t)

          ((set-metadata)
           ;; (kb 'set-metadata INFO)
           ;; Set model metadata to INFO
           (if (not (= (length args) 1))
               (error 'make-kb
                      "bad call to set-metadata: wrong number of args in ~s"
                      args))
           (java-synchronized sync-object
             (lambda ()
               (set! metadata (car args)))))

          ;;;;;;;;;;
          ;; Commands which implicitly mutate the knowledgebase, via memoisation

          ((get-model)
           ;; (kb 'get-model [SUBMODEL-NAME])
           ;; Return newly-merged model or #f if no models exist
           (case (length args)
             ((0)
              (cond ((null? submodels)
                     #f)
                    (merged-model)
                    (else
                     (java-synchronized sync-object
                       (lambda ()
                         (set! merged-model
                               (rdf:merge-models (map cddr submodels)))
                         merged-model)))))
             ((1)
              (let ((sm (assq (as-symbol (car args))
                              submodels)))
                (and sm (cddr sm))))
             (else
              (error 'make-kb
                     "Bad call to get: wrong number of args in ~a"
                     args))))

          ((get-inferencing-model)
           ;; return #f on error
           (java-synchronized sync-object
             (lambda ()
               (if (not inferencing-model)
                   (let ((tbox (get-abox-or-tbox #t))
                         (abox (get-abox-or-tbox #f)))
                     (define-java-classes
                       (<factory> |com.hp.hpl.jena.rdf.model.ModelFactory|))
                     (define-generic-java-method
                       create-inf-model)
                     (set! inferencing-model
                           (cond ((and tbox abox)
                                  (create-inf-model (java-null <factory>)
                                                    (rdf:get-reasoner)
                                                    tbox
                                                    abox))
                                 (tbox
                                  (create-inf-model (java-null <factory>)
                                                    (rdf:get-reasoner)
                                                    tbox))
                                 (else
                                  #f)))))))
           inferencing-model)

          ((get-model-tbox get-model-abox)
           ;; (kb 'get-model-tbox/abox)
           ;; return merged models or #f if none
           (or (null? args)
               (error 'make-kb
                      "Bad call to get-model-tbox/abox: wrong no args ~s" args))
           (get-abox-or-tbox (eq? cmd 'get-model-tbox)))

          ;;;;;;;;;;
          ;; Commands which do not mutate the knowledgebase

          ((has-model)
           ;; (kb 'has-model [SUBMODEL-NAME])
           ;; With an argument, this is redundant with
           ;; (kb 'get-model SUBMODEL-NAME), but without, it saves the
           ;; redundant merging of the models, as well as being more
           ;; intelligible
           (cond ((= (length args) 0)
                  (not (null? submodels)))
                 ((= (length args) 1)
                  (assq (as-symbol (car args)) submodels))
                 (else
                  (error 'make-kb
                         "Bad call to has-model: wrong no. args ~s" args))))

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

          ((get-name)
           myname)

          ((info)
           (list (cons 'submodels (map (lambda (sm)
                                         `((name . ,(car sm))
                                           (tbox . ,(cadr sm))))
                                       submodels))))

          (else
           (error 'make-kb "impossible command for knowledgebase: ~s" cmd))))))

  ;; kb:knowledgebase? object -> boolean
  ;;
  ;; Returns true if the object is a knowledgebase
  (define (kb:knowledgebase? object)
    (and (procedure? object)
         (with/fc
             (lambda (m e) #f)
           (lambda ()
             (let ((name (object 'get-name)))
               (symbol? name))))))

  )
