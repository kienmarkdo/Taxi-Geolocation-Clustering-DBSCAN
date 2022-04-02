#lang scheme

; testing list manipulations
(define L '((1 2 3) (4 5 6) (7 8 9)))
(define Temp1 (cdr (car L))) ; takes (1 2 3) then removes the 1; returns (2 3)
(define Temp2 (cdr L)) ; returns the rest of the list without (1 2 3)
(define Combine (cons Temp1 Temp2)) ; combine (2 3) and ((4 5 6) (7 8 9)) to make ((2 3) (4 5 6) (7 8 9))

Temp1
Temp2
Combine

; ======= thoughts while working on relabel procedure START =======
; for relabel, there are several things we can do to update the last parameter CLUSTER_ID
; we can make a new list without the last element then add NEW_CLUSTER_ID to the end of the new list
;   we can reverse L, get the cdr, then reverse it again to remove the last element; append NEW_CLUSTER_ID to the end of that new list
;   we can make a procedure to remove the last element from the list, then append NEW_CLUSTER_ID to that list

; we could directly update the element at the last index, thereby directly modifying the original list and not needing to use any extra memory
;   that recursion or tail-recursion would need. However, this is not "pure recursion" because how would we know when to stop? This is more
;   of a solution for a for-loop.
; ======= thoughts while working on relabel procedure END   =======

; testing replace-last
(define (replace-last L New-id)
  (if (null? (cdr L))
      (list New-id)
      (cons (car L) (replace-last (cdr L) New-id)))) ; alternate: (append (list(car L)) (replace-last (cdr L) New-id))))
(replace-last '(1 2 3) 9)

; relabels the points of current list of clusters having OldClusterID with NewClusterID
(define (relabel old-id new-id L)
  (cond
    ((null? L) '())
    ((= old-id (last (car L))) (cons (replace-last (car L) new-id) (relabel old-id new-id (cdr L))))
    (else (cons (car L) (relabel old-id new-id (cdr L))))
  )
)
(relabel 33 77 '((1 2.2 3.1 33) (2 2.1 3.1 22) (3 2.5 3.1 33) (4 2.1 4.1 33) (5 4.1 3.1 30)))

; test overlap
(define (overlap X L)
  (cond
    ((null? L) '())
    ((= (car X) (car (car L))) (list (last X)))
    (else (overlap X (cdr L)))
  )
)
(overlap '(2 2.1 3.1 99) '((1 2.2 3.1 33) (2 2.1 3.1 22) (3 2.5 3.1 33) (4 2.1 4.1 33) (5 4.1 3.1 30))) ; 99
(overlap '(99 99 99 99) '((1 2.2 3.1 33) (2 2.1 3.1 22) (3 2.5 3.1 33) (4 2.1 4.1 33) (5 4.1 3.1 30))) ; ()
(equal? '() (overlap '(99 99 99 99) '((1 2.2 3.1 33) (2 2.1 3.1 22) (3 2.5 3.1 33) (4 2.1 4.1 33) (5 4.1 3.1 30)))) ; true

;(mergeClusters (import))
;(define list '((1 2.2 3.1 33) (2 2.1 3.1 22) (3 2.5 3.1 33) (4 2.1 4.1 33) (5 4.1 3.1 30) (1 2.2 3.1 100) (2 2.1 3.1 101) (3 2.5 3.1 102) (4 2.1 4.1 103)))
;(mergeClusters list)
;(overlap (car list) '((1 2.2 3.1 33) (2 2.1 3.1 22)))