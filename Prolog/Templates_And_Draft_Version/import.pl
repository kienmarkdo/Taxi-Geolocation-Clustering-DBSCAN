/* Import partition files provided by professor Robert Laganière */
import :-
    csv_read_file('partition65.csv', Data65, [functor(partition)]), maplist(assert, Data65),
    csv_read_file('partition74.csv', Data74, [functor(partition)]), maplist(assert, Data74),
    csv_read_file('partition75.csv', Data75, [functor(partition)]), maplist(assert, Data75),
    csv_read_file('partition76.csv', Data76, [functor(partition)]), maplist(assert, Data76),
    csv_read_file('partition84.csv', Data84, [functor(partition)]), maplist(assert, Data84),
    csv_read_file('partition85.csv', Data85, [functor(partition)]), maplist(assert, Data85),
    csv_read_file('partition86.csv', Data86, [functor(partition)]), maplist(assert, Data86),
    listing(partition) % partition(PARTITION_ID, POINT_ID, X, Y, CLUSTER_ID)
.