/*
Nom / Name             : Kien Do
Cours / Course         : CSI2520 - Paradigmes de programmation / Programming Paradigms
Professeur / Professor : Robert Laganière, uottawa.ca
Session / Semester     : Hiver 2022 / Winter 2022
Projet / Project       : Merging DBSCAN-Clustered Taxi Geolocation Partitions
*/

/* ==============  Import partition files  ============== */
import :-
    csv_read_file('partition65.csv', Data65, [functor(partition)]), maplist(assert, Data65),
    csv_read_file('partition74.csv', Data74, [functor(partition)]), maplist(assert, Data74),
    csv_read_file('partition75.csv', Data75, [functor(partition)]), maplist(assert, Data75),
    csv_read_file('partition76.csv', Data76, [functor(partition)]), maplist(assert, Data76),
    csv_read_file('partition84.csv', Data84, [functor(partition)]), maplist(assert, Data84),
    csv_read_file('partition85.csv', Data85, [functor(partition)]), maplist(assert, Data85),
    csv_read_file('partition86.csv', Data86, [functor(partition)]), maplist(assert, Data86)
    % listing(partition) % partition(PARTITION_ID, POINT_ID, X, Y, CLUSTER_ID)
.



% Facts
clusterList([[]]).

% Relations

% Predicates/Rules

/* ===============  Cluster merging algorithm  =============== */


/* ===================  Helper predicates  =================== */
% mergeClusters produces a list of all points with their cluster ID
mergeClusters(L) :-
    findall([D,X,Y,C], partition(_,D,X,Y,C), LL),
    populateClusterList(LL)
.

% always start with adding to the empty cluster list
populateClusterList([H|T]) :-
    populateClusterList([H|T], add, [], ClusterList).

% check to see if we should be relabelling or adding the current point to the cluster list
populateClusterList([H|T], check, A, ClusterList) :-
    notre_member(H, A),
    populateClusterList([H|T], add, A, ClusterList),
    !
.
populateClusterList([H|T], check, A, ClusterList) :-
    not(notre_member(H, A)),
    populateClusterList([H|T], relabel, A, ClusterList)
.

% ========= relabel / add to clusterList =========
% relabel all labels of intersection points in A to label H
populateClusterList([H|T], relabel, A, ClusterList) :-
    relabel(A, H, AA)
.
populateClusterList([H|T], add, A, ClusterList) :-
    insert(H, A, AA),
    populateClusterList(T, check, AA, ClusterList)
.

relabel([_, X, Y, CLUSTER_ID], C, L) :-

.

% loop through all the points (paritions == points)
% findall([D,X,Y,C], partition(_,D,X,Y,C), L).

% notre_member/2
% notre_member(X, [_|L]) returns true if elements X is in list [_|L]
notre_member(X,[X|_]).
notre_member(X,[_|L]) :- notre_member(X,L).

% make a list of 2 lists
% combineTwoLists(L1, L2, [L1, L2]).

% insert in front of list
insert(A, L, [A|L]).

/*
1. Loop through all the points ( partitions = points)
2. Check if the points are in clusterlist
3. If it is, relabel them
4. If not, just add to your clusterlist
5. Keep going till you hit the end
*/

