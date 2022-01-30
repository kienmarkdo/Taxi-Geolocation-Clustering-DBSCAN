This version produces MULTIPLE CSV files, one CSV file PER CLUSTER.
Each CSV file contains the following data

ClusterID, starting longitude of EACH TripRecord, starting latitude of each TripRecord, number of points/TripRecords in this cluster.

The following DBSCAN values were used
- EPS: 0.0003
- MIN_PTS: 5

The output is in the All_Clusters_0.0003_5 folder.
The output CSV files are named by their cluster ID (in the format of a counter, starting at cluster_1.csv)

The top 10 largest clusters (sort the CSV files by largest size to smallest size in your directory) can then be imported onto Google Maps.