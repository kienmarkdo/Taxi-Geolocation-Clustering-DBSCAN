(define (readlist filename)
 (call-with-input-file filename
  (lambda (in)
    (read in))))
(define (import)
  (let ((p65 (readlist "partition65.scm"))
        (p74 (readlist "partition74.scm")) 
        (p75 (readlist "partition74.scm")))
    (append p65 p74 p75)))