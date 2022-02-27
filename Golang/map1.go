// Project CSI2120/CSI2520
// Winter 2022
// Robert Laganiere, uottawa.ca

package main

import (
	"encoding/csv"
	"fmt"
	"io"
	"math"
	"os"
	"strconv"
	"time"
)

type GPScoord struct {
	lat  float64
	long float64
}

type LabelledGPScoord struct {
	GPScoord
	ID    int // point ID
	Label int // cluster ID => 0 = undefined, -1 = noise
}

const N int = 4
const MinPts int = 5
const eps float64 = 0.0003
const filename string = "yellow_tripdata_2009-01-15_9h_21h_clean.csv"

func main() {

	start := time.Now()

	gps, minPt, maxPt := readCSVFile(filename)
	fmt.Printf("Number of points: %d\n", len(gps))

	// minPt and MaxPt are limited to the downtown manhattan region for even concurrency
	// otherwise, 2-3 threads will be working on 90% of the work in downtown manhatten, while the rest
	// of the threads are only working on a select few clusters - that would defeat the purpose of concurrency
	minPt = GPScoord{-74., 40.7}
	maxPt = GPScoord{-73.93, 40.8}

	// geographical limits
	fmt.Printf("NW:(%f , %f)\n", minPt.long, minPt.lat)
	fmt.Printf("SE:(%f , %f) \n\n", maxPt.long, maxPt.lat)

	// Parallel DBSCAN STEP 1.
	incx := (maxPt.long - minPt.long) / float64(N)
	incy := (maxPt.lat - minPt.lat) / float64(N)

	var grid [N][N][]LabelledGPScoord // a grid of GPScoord slices

	// Create the partition
	// triple loop! not very efficient, but easier to understand

	partitionSize := 0
	for j := 0; j < N; j++ {
		for i := 0; i < N; i++ {

			for _, pt := range gps {

				// is it inside the expanded grid cell
				if (pt.long >= minPt.long+float64(i)*incx-eps) && (pt.long < minPt.long+float64(i+1)*incx+eps) && (pt.lat >= minPt.lat+float64(j)*incy-eps) && (pt.lat < minPt.lat+float64(j+1)*incy+eps) {

					grid[i][j] = append(grid[i][j], pt) // add the point to this slide
					partitionSize++
				}
			}
		}
	}

	// ***
	// This is the non-concurrent procedural version
	// It should be replaced by a producer thread that produces jobs (partition to be clustered)
	// And by consumer threads that clusters partitions
	// for j := 0; j < N; j++ {
	// 	for i := 0; i < N; i++ {
	// 		DBscan(&grid[i][j], MinPts, eps, i*10000000+j*1000000)
	// 	}
	// }

	// Parallel DBSCAN STEP 2.
	// Apply DBSCAN on each partition
	// Learned about the producer-worker pattern / worker-jobs pattern from here: https://betterprogramming.pub/hands-on-go-concurrency-the-producer-consumer-pattern-c42aab4e3bd2
	// Implemented using Single/Multiple Producer Multiple Consumer variation
	const consumerCount = 4
	const producerCount = N

	jobs := make(chan int)
	//done := make(chan bool)
	// wp := &sync.WaitGroup{}
	// wc := &sync.WaitGroup{}
	wp := make(chan bool)
	//wc := make(chan bool)

	// wp.Add(producerCount)
	// wc.Add(consumerCount)

	// for j := 0; j < producerCount; j++ {
	// 	go produce(jobs, &grid, j, wp)
	// }
	for j := 0; j < N; j++ {
		for i := 0; i < N; i++ {
			go produce(jobs, &grid, j, i, wp)
		}
	}

	for i := 0; i < consumerCount; i++ {
		go consume(jobs)
	}

	//wp.Wait()
	for i := 0; i < producerCount; i++ {
		<-wp // Blocks waiting for a receive
	}
	close(jobs)
	// for i := 0; i < consumerCount; i++ {
	// 	<-wc // Blocks waiting for a receive
	// }
	//wc.Wait()

	// Parallel DBSCAN step 3.
	// merge clusters
	// *DO NOT PROGRAM THIS STEP

	end := time.Now()
	fmt.Printf("\nExecution time: %s of %d points\n", end.Sub(start), partitionSize)
}

func produce(jobs chan<- int, grid *[N][N][]LabelledGPScoord, j int, i int, wg chan bool) {
	//defer wg.Done()

	jobs <- DBscan(&grid[i][j], MinPts, eps, i*10000000+j*1000000)

	close(jobs)

}

func consume(jobs <-chan int) {
	//defer wg.Done()
	for range jobs {
		<-jobs
	}
}

// Applies DBSCAN algorithm on LabelledGPScoord points
// LabelledGPScoord: the slice of LabelledGPScoord points
// MinPts, eps: parameters for the DBSCAN algorithm
// offset: label of first cluster (also used to identify the cluster)
// returns number of clusters found
func DBscan(coords *[]LabelledGPScoord, MinPts int, eps float64, offset int) (nclusters int) {

	nclusters = 0

	for _, currTrip := range *coords {
		if currTrip.Label != 0 { // if label is undefined
			continue
		}

		neighbours := rangeQuery(coords, currTrip, eps)

		if len(neighbours) < MinPts {
			currTrip.Label = -1 // if label is noise
			continue
		}

		nclusters++

		currTrip.Label = nclusters

		var seedSet []LabelledGPScoord      // Neighbours to expand
		seedSet = append(seedSet, currTrip) // set core trip
		addToSeed(&seedSet, neighbours)

		for i := 0; i < len(seedSet); i++ {
			if seedSet[i].Label == -1 { // if label is noise
				seedSet[i].Label = nclusters
			}
			if seedSet[i].Label != 0 { // if label is undefined
				continue
			}
			seedSet[i].Label = nclusters
			seedNeighbours := rangeQuery(coords, seedSet[i], eps)

			if len(seedNeighbours) >= MinPts {
				addToSeed(&seedSet, seedNeighbours)
			}

		} // end of inner for loop

	} // end of coords (outer) for loop

	// End of DBscan function
	// Printing the result (do not remove)
	fmt.Printf("Partition %10d : [%4d,%6d]\n", offset, nclusters, len(*coords))

	return nclusters
}

// Scans the database and returns a list of neighbouring LabelledGPScoords within the eps radius around a given LabelledGPScoord.
// This is a helper function to DBSCAN.
func rangeQuery(db *[]LabelledGPScoord, currCoord LabelledGPScoord, eps float64) []LabelledGPScoord {

	var neighbours []LabelledGPScoord

	for _, otherTrip := range *db {
		if calculateDistance(currCoord, otherTrip) <= eps {
			neighbours = append(neighbours, otherTrip)
		}
	}

	return neighbours
}

// Returns the float64 distance between two LabelledGPScoords
func calculateDistance(c1 LabelledGPScoord, c2 LabelledGPScoord) float64 {
	return math.Sqrt(math.Pow(c1.lat-c2.lat, 2) + math.Pow(c1.long-c2.long, 2))
}

// Adds a set of LabelledGPScoord onto the current cluster.
func addToSeed(seedSet *[]LabelledGPScoord, neighbours []LabelledGPScoord) {
	for _, newTrip := range neighbours {
		if !contains(seedSet, newTrip) {
			*seedSet = append(*seedSet, newTrip)
		}
	}
}

// Returns true if a coordinate is in a list of LabelledGPScoords
func contains(coordsList *[]LabelledGPScoord, coord LabelledGPScoord) bool {
	for _, curr := range *coordsList {
		if coord == curr {
			return true
		}
	}
	return false
}

// reads a csv file of trip records and returns a slice of the LabelledGPScoord of the pickup locations
// and the minimum and maximum GPS coordinates
func readCSVFile(filename string) (coords []LabelledGPScoord, minPt GPScoord, maxPt GPScoord) {

	coords = make([]LabelledGPScoord, 0, 5000)

	// open csv file
	src, err := os.Open(filename)
	defer src.Close()
	if err != nil {
		panic("File not found...")
	}

	// read and skip first line
	r := csv.NewReader(src)
	record, err := r.Read()
	if err != nil {
		panic("Empty file...")
	}

	minPt.long = 1000000.
	minPt.lat = 1000000.
	maxPt.long = -1000000.
	maxPt.lat = -1000000.

	var n int = 0

	for {
		// read line
		record, err = r.Read()

		// end of file?
		if err == io.EOF {
			break
		}

		if err != nil {
			panic("Invalid file format...")
		}

		// get latitude
		lat, err := strconv.ParseFloat(record[8], 64)
		if err != nil {
			fmt.Printf("\n%d lat=%s\n", n, record[8])
			panic("Data format error (lat)...")
		}

		// is corner point?
		if lat > maxPt.lat {
			maxPt.lat = lat
		}
		if lat < minPt.lat {
			minPt.lat = lat
		}

		// get longitude
		long, err := strconv.ParseFloat(record[9], 64)
		if err != nil {
			panic("Data format error (long)...")
		}

		// is corner point?
		if long > maxPt.long {
			maxPt.long = long
		}

		if long < minPt.long {
			minPt.long = long
		}

		// add point to the slice
		n++
		pt := GPScoord{lat, long}
		coords = append(coords, LabelledGPScoord{pt, n, 0})
	}

	return coords, minPt, maxPt
}
