# Concurrent programming (Golang)
In the Go version of this project, the DBSCAN algorithm is run concurrently on partitions of the Trip Record data. The partitions are created by dividing the geographical area into a grid of NxN. The following figure illustrates the case of a partition made of 4x4 cells (NOTE: In this case, the DBSCAN algorithm would run on 16 concurrent threads):

<p align="center">
  <img height="300" src="https://user-images.githubusercontent.com/67518620/155905579-7ba41e0f-f315-4c67-a57b-dbd189e8fffc.png">
</p>

## Implementation
### General algorithm (MapReduce pattern)
The parallel DBSCAN algorithm is based on the MapReduce pattern, a widely used pattern in concurrent programming, and proceeds as follows:
  1. Map the data into overlapping partitions (the overlap with other partitions must be at least equals to eps along its border)
  2. Apply the DBSCAN algorithm over each partition.
  3. Reduce the results by collecting the clusters from all partitions. Intersecting clusters must be merged.

Note that the 3rd step (Reduce) is skipped in this implementation.

### Experimentation
This concurrent version of the DBSCAN algorithm is based on the producer-consumer pattern (worker pools), specifically the [multiple producer multiple consumer variation](https://betterprogramming.pub/hands-on-go-concurrency-the-producer-consumer-pattern-c42aab4e3bd2).

## Dataset
The dataset is taken from NYC's 2009 taxi database that recorded taxi trips in the span of **12 hours** (from 9h to 21h) on January 15, 2009. The dataset is contained in a CSV file, with each line corresponding to a trip record and the columns representing the relevant attributes of each trip, and contains _232,051_ points.

_Note: This dataset cannot be plotted on Google Maps due to the sheer size of the CSV file._



## Results
### Version 1 (eps = 0.0003, min_pts = 5, N = 4)

## Takeaways
Of course, no one can build a project without running into problems. Since I dealt with concurrency in this version of the project, most of the challenges I encountered were regarding the efficiency of my program. 

I learned that the built-in `math.Pow()` function is significantly slower than simply doing `num*num` as the built-in function has additional error checking mechanisms. This significantly reduced the execution time by several minutes. 

I also learned that it is much slower to check whether an item is in a slice by writing a `contains()` function than to convert the slice into a map then removing the duplicate keys ([see problem and solution](https://stackoverflow.com/questions/66643946/how-to-remove-duplicates-strings-or-int-from-slice-in-go)).
