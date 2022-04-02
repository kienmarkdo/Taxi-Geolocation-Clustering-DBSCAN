# Concurrent programming (Go)
In this Go version of the comprehensive project, the [DBSCAN algorithm](https://en.wikipedia.org/wiki/DBSCAN) is run concurrently on partitions of the Trip Record data. The partitions are created by dividing the geographical area into a grid of NxN. The following figure illustrates the case of a partition made of 4x4 cells (NOTE: In this case, the DBSCAN algorithm would run on 16 concurrent threads):

<p align="center">
  <img height="300" src="https://user-images.githubusercontent.com/67518620/155905579-7ba41e0f-f315-4c67-a57b-dbd189e8fffc.png">
</p>

## How to run
Make sure [Go](https://go.dev/) is downloaded.

Include the dataset file `yellow_tripdata_2009-01-15_9h_21h_clean.csv` in the same directory as `mainFinal.go`.

Run the file `mainFinal.go` in the command line with `go run mainFinal.go`.


## Implementation
### General algorithm (MapReduce pattern)
The parallel DBSCAN algorithm is based on the [MapReduce pattern](https://en.wikipedia.org/wiki/MapReduce), a widely used pattern in concurrent programming, and proceeds as follows:
  1. Map the data into overlapping partitions (the overlap with other partitions must be at least equals to eps along its border)
  2. Apply the DBSCAN algorithm over each partition.
  3. Reduce the results by collecting the clusters from all partitions. Intersecting clusters must be merged.

In other words, the steps are: create partitions, concurrently perform DBSCAN on each partition, merge the partitions to get one big result. However, the 3rd step (merging the partitions step) will not be implemented as we are only interested in the concurrency aspect (step 2) of this Go version of the project. The 3rd step will be implemented in the Prolog version of the project.

**NOTE:** The 3rd step (Merge/Reduce) is skipped in this implementation. It is implemented in the Prolog version of this project.

### Experimentation
This concurrent version of the DBSCAN algorithm is based on the [producer-consumer pattern](https://en.wikipedia.org/wiki/Producer%E2%80%93consumer_problem) (worker pools), specifically the [Single Producer Multiple Consumer variation](https://betterprogramming.pub/hands-on-go-concurrency-the-producer-consumer-pattern-c42aab4e3bd2). The experimentation is carried out with several combinations of different numbers of **partitions** and **consumer threads** in order to observe the optimal solution. A [semaphore](https://en.wikipedia.org/wiki/Semaphore_(programming)#Producer%E2%80%93consumer_problem) structure is implemented to solve the producer-consumer problem.

### Additional Notes
The DBSCAN implementation has a restricted minimum point of `GPScoord{-74., 40.7}` and a maximum point of `GPScoord{-73.93, 40.8}`. These coordinates represent the area of downtown Manhattan, NYC.

If we were to create partitions on the entire city of New York, one or two partitions in the downtown Manhattan area would be doing a lot of the work to cluster the points (because that area is busier than the rest of the city). Meanwhile, the number of clusters around the city would be very small, giving those partitions almost no work to do. This would defeat the purpose of concurrency; therefore, the boundaries of the partitioning step (step 1 of MapReduce) will only be restricted to the downtown Manhattan area for maximum efficiency.

## Dataset
The dataset is taken from NYC's 2009 taxi database that recorded taxi trips in the span of **12 hours** (from 9h to 21h) on January 15, 2009. The dataset is contained in a CSV file, with each line corresponding to a trip record and the columns representing the relevant attributes of each trip, and contains _232,051_ points.

_Note: This dataset cannot be plotted on Google Maps due to the sheer size of the CSV file._

![image](https://user-images.githubusercontent.com/67518620/156251067-9c223a05-01fe-4051-a189-fbc458548c08.png)


## Results
### Eps = 0.0003, MinPts = 5
<!-- NOTE: HTML Scroll Box doesn't work in GitHub markdown so <details> and <summary> are used instead. -->

<!-- ===================================  DETAIL SEPARATOR  =================================== -->
<details>
  <summary>
    Version 1 (N = 4 and 4 consumer threads)
  </summary>
  
```
Number of points: 232050
SW:(40.700000 , -74.000000) 
NE:(40.800000 , -73.930000) 

N = 4 and 4 consumer threads.

Partition   30000000 : [   0,    17]
Partition   20000000 : [   9,   272]
Partition   10000000 : [  12,   540]
Partition   21000000 : [   6,   288]
Partition   31000000 : [   7,   226]
Partition          0 : [  33,  6514]
Partition   11000000 : [  20, 14022]
Partition   22000000 : [  15, 19940]
Partition   32000000 : [  15,  1192]
Partition    3000000 : [   4,  2015]
Partition   13000000 : [  14, 15370]
Partition    2000000 : [  25, 31599]
Partition   33000000 : [  19,  2187]
Partition   23000000 : [  20, 16535]
Partition    1000000 : [  18, 38774]
Partition   12000000 : [   9, 56611]

Execution time: 9.3677512s of 206102 points
Number of CPUs: 8
```
</details>
<!-- ===================================  DETAIL SEPARATOR  =================================== -->
<details>
  <summary>
    Version 2 (N = 10 and 4 consumer threads)
  </summary>
  
```
Number of points: 232050
SW:(40.700000 , -74.000000) 
NE:(40.800000 , -73.930000) 

N = 10 and 4 consumer threads.

Partition          0 : [   1,    31]
Partition   40000000 : [   0,     5]
Partition   20000000 : [   0,     3]
Partition   60000000 : [   0,    11]
Partition   70000000 : [   0,     3]
Partition   80000000 : [   0,     3]
Partition   90000000 : [   0,     0]
Partition   10000000 : [   4,   118]
Partition   50000000 : [   1,    25]
Partition   30000000 : [   0,     3]
Partition   21000000 : [   8,   165]
Partition   41000000 : [   0,    10]
Partition   31000000 : [   3,    80]
Partition   51000000 : [   5,   110]
Partition   71000000 : [   0,    16]
Partition   81000000 : [   0,     3]
Partition   91000000 : [   0,     1]
Partition   61000000 : [   3,    88]
Partition   11000000 : [   9,   876]
Partition    1000000 : [  13,   985]
Partition   32000000 : [   7,   477]
Partition   42000000 : [   0,    14]
Partition   52000000 : [   1,    36]
Partition   62000000 : [   2,    65]
Partition   72000000 : [   1,    41]
Partition   82000000 : [   0,     5]
Partition   92000000 : [   0,     2]
Partition   22000000 : [  10,  2218]
Partition   12000000 : [   9,  3558]
Partition    2000000 : [   6,  5101]
Partition   33000000 : [  13,  1329]
Partition   43000000 : [   0,    11]
Partition   53000000 : [   0,    14]
Partition   63000000 : [   1,    67]
Partition   73000000 : [   1,    18]
Partition   83000000 : [   0,     8]
Partition   93000000 : [   0,    13]
Partition   23000000 : [   5,  4365]
Partition    3000000 : [  12,  5498]
Partition    4000000 : [   6,  6330]
Partition   13000000 : [   2,  8760]
Partition   44000000 : [   3,   892]
Partition   54000000 : [   0,    24]
Partition   64000000 : [   1,    53]
Partition   74000000 : [   3,   102]
Partition   84000000 : [   4,    89]
Partition   94000000 : [   2,    94]
Partition   24000000 : [   5,  8553]
Partition    5000000 : [  11,  4353]
Partition   34000000 : [   5,  6032]
Partition   14000000 : [   2,  9790]
Partition   25000000 : [   1, 10329]
Partition   55000000 : [   7,  1797]
Partition   65000000 : [   4,    74]
Partition   75000000 : [   2,    63]
Partition   85000000 : [   7,   204]
Partition   95000000 : [   4,   122]
Partition   45000000 : [   3,  8902]
Partition    6000000 : [  12,  1312]
Partition   15000000 : [   4, 10764]
Partition   16000000 : [   5,  4340]
Partition   36000000 : [   1,  9388]
Partition   35000000 : [   1, 15660]
Partition   26000000 : [   1, 12393]
Partition   76000000 : [   2,    95]
Partition   86000000 : [   1,    20]
Partition   96000000 : [   3,    77]
Partition    7000000 : [   0,    18]
Partition   56000000 : [   7,  6330]
Partition   66000000 : [   4,  4619]
Partition   46000000 : [   3, 11442]
Partition   17000000 : [   4,  1913]
Partition   37000000 : [   5,  1772]
Partition   47000000 : [   3,  1140]
Partition   77000000 : [   5,  3619]
Partition   87000000 : [   2,    94]
Partition   97000000 : [   0,     2]
Partition    8000000 : [   0,     3]
Partition   18000000 : [   1,    26]
Partition   28000000 : [   4,  3293]
Partition   27000000 : [   7,  5791]
Partition   48000000 : [   1,  1934]
Partition   58000000 : [   2,  1526]
Partition   67000000 : [   2,  6578]
Partition   78000000 : [   7,  1840]
Partition   88000000 : [   3,   127]
Partition   98000000 : [   0,     2]
Partition    9000000 : [   0,     7]
Partition   19000000 : [   0,     4]
Partition   29000000 : [   1,    31]
Partition   38000000 : [   1,  4446]
Partition   39000000 : [   4,  1913]
Partition   59000000 : [   9,   613]
Partition   69000000 : [   4,   684]
Partition   79000000 : [   7,   259]
Partition   89000000 : [   8,   282]
Partition   99000000 : [   2,    56]
Partition   49000000 : [  11,  2481]
Partition   57000000 : [   2,  7967]
Partition   68000000 : [   4,  5688]

Execution time: 2.4269867s of 222488 points
Number of CPUs: 8
```
</details>

For more Output results, view the [Ouput folder](Output_Results).

## Takeaways
Of course, no one can build a project without running into problems. Since I dealt with concurrency in this version of the project, most of the challenges I encountered were regarding the efficiency of my program. 

I learned that the built-in `math.Pow()` function is significantly slower than simply doing `num*num` as the built-in function has additional error checking mechanisms. This modification significantly reduced the execution time by several minutes. 

I also learned that it is much slower to check whether an item is in a slice by writing a `contains()` function than to convert the slice into a map then removing the duplicate keys ([see problem and solution](https://stackoverflow.com/questions/66643946/how-to-remove-duplicates-strings-or-int-from-slice-in-go)).
