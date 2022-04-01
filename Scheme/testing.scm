#lang scheme

; testing list manipulations
(define L '((1 2 3) (4 5 6) (7 8 9)))
(define Temp1 (cdr (car L))) ; takes (1 2 3) then removes the 1; returns (2 3)
(define Temp2 (cdr L)) ; returns the rest of the list without (1 2 3)
(define Combine (cons Temp1 Temp2)) ; combine (2 3) and ((4 5 6) (7 8 9)) to make ((2 3) (4 5 6) (7 8 9))

Temp1
Temp2
Combine