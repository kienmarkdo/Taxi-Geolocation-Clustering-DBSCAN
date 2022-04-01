#|
Nom / Name             : Kien Do
Cours / Course         : CSI2520 - Paradigmes de programmation / Programming Paradigms
Professeur / Professor : Robert Laganière, uottawa.ca
Session / Semester     : Hiver 2022 / Winter 2022
Projet / Project       : Merging DBSCAN-Clustered Taxi Geolocation Partitions
|#

#lang scheme

; =====================================  Import partition files  ===================================== ;
; Partition format: (PARTITION_ID, POINT_ID, X, Y, CLUSTER_ID).
(define (readlist filename)
 (call-with-input-file filename
  (lambda (in)
    (read in))))
(define (import)
  (let ((p65 (readlist "partition65.scm"))
        (p74 (readlist "partition74.scm")) 
        (p75 (readlist "partition75.scm")))
    (append p65 p74 p75)))


; ====================================  Merge partition clusters  ==================================== ;





; ==================================  Helper/auxiliary predicates  =================================== ;


; ==================================  Output results to text file  =================================== ;