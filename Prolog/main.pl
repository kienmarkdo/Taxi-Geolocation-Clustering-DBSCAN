/*
Nom / Name             : Kien Do
Cours / Course         : CSI2520 - Paradigmes de programmation / Programming Paradigms
Professeur / Professor : Robert Laganière, uottawa.ca
Session / Semester     : Hiver 2022 / Winter 2022
Projet / Project       : Merging DBSCAN-Clustered Taxi Geolocation Partitions
*/

/* ==============  Start program with "start."  ============== */
start :-
    consult("import.pl"),
    import  % predicate in file import.pl
.

% Facts

% Relations

% Predicates/Rules

/* ===============  Cluster merging algorithm  =============== */


/* ===================  Helper predicates  =================== */
% mergeClusters produces a list of all points with their cluster ID
mergeClusters().

