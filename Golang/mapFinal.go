/*
Nom / Name             : Kien Do
Cours / Course         : CSI2520 - Paradigmes de programation / Programming Paradigms
Professeur / Professor : Robert Laganière, uottawa.ca
Session                : Hiver 2022 / Winter 2022
Projet / Project       : Clustering Taxi Geolocation Using Concurrent DBSCAN
*/

package main

import (
	"encoding/csv"
	"fmt"
	"io"
	"math"
	"os"
	"runtime"
	"strconv"
	"time"
)

type GPScoord struct {
	lat  float64
	long float64
}

type LabelledGPScoord struct {
	GPScoord
	ID    int // point ID (0 = undefined, -1 = noise)
	Label int // cluster ID
}

// Job structure used to produce jobs
// Struct Job utilisé pour produire des jobs
type Job struct {
	coords []LabelledGPScoord
	minPts int
	eps    float64
	offset int
}

// Uses a semaphore for synchronisation instead of Waitgroup
// Utilisation d'une semaphore pour la synchronisation au lieu d'utiliser Waitgroup
type semaphore chan bool

// function that waits for all the go routines
// fonction qui va attendre toutes les go routines
func (s semaphore) Wait(n int) {
	for i := 0; i < n; i++ {
		<-s
	}
}

// function that signals the end of a go routine (similar to Waitgroup.Done())
// fonction qui signale la fin d'une go routine
func (s semaphore) Signal() {
	s <- true
}

// constant variables
// variables constantes
const ConsumerCount = 200
const N int = 20
const MinPts int = 5
const eps float64 = 0.0003
const filename string = "yellow_tripdata_2009-01-15_9h_21h_clean.csv"

// main function
// fonction main
func main() {

	start := time.Now()

	gps, minPt, maxPt := readCSVFile(filename)
	fmt.Printf("Number of points: %d\n", len(gps))

	minPt = GPScoord{40.7, -74.}
	maxPt = GPScoord{40.8, -73.93}

	// geographical limits
	fmt.Printf("SW:(%f , %f)\n", minPt.lat, minPt.long)
	fmt.Printf("NE:(%f , %f) \n\n", maxPt.lat, maxPt.long)

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

	// Parallel DBSCAN STEP 2.
	// Apply DBSCAN on each partition
	// Follows the producer-consumer pattern, specifically the Single Producer Mutiple Consumer pattern
	jobs := make(chan Job)
	mutex := make(semaphore)

	fmt.Printf("N = %d and %d consumer threads.\n\n", N, ConsumerCount)

	for i := 0; i < ConsumerCount; i++ {
		go consume(jobs, mutex) // Multiple consumer threads that cluster partitions
	}

	for j := 0; j < N; j++ {
		for i := 0; i < N; i++ {
			// Single producer thread that produces jobs (partition to be clustered)
			jobs <- Job{grid[i][j], MinPts, eps, i*10000000 + j*1000000}
		}
	}

	close(jobs)
	mutex.Wait(ConsumerCount)

	// Parallel DBSCAN step 3.
	// merge clusters
	// *DO NOT PROGRAM THIS STEP

	end := time.Now()
	fmt.Printf("\nExecution time: %s of %d points\n", end.Sub(start), partitionSize)
	fmt.Printf("Number of CPUs: %d", runtime.NumCPU())
}

// Consumer function takes produced jobs and processes them
// Fonction consomme prend des jobs produits et les traite
func consume(jobs <-chan Job, sem semaphore) {

	for {
		j, more := <-jobs // takes a job out of the production queue; more indicates there are still more jobs in the queue

		if more {
			DBscan(&j.coords, j.minPts, j.eps, j.offset)
		} else {
			sem.Signal() // closes the semaphore by signalling that there are no jobs left to be consumed
			return
		}
	}
}

// Applies DBSCAN algorithm on LabelledGPScoord points
// LabelledGPScoord: the slice of LabelledGPScoord points
// MinPts, eps: parameters for the DBSCAN algorithm
// offset: label of first cluster (also used to identify the cluster)
// returns number of clusters found
func DBscan(coords *[]LabelledGPScoord, MinPts int, eps float64, offset int) (nclusters int) {

	nclusters = 0 // Cluster counter

	for p := 0; p < len(*coords); p++ {
		if (*coords)[p].Label != 0 { // 0 means undefined; Previously processed in inner loop
			continue
		}

		neighbours := rangeQuery(*coords, (*coords)[p], eps) // Find neighbours (excludes core pointer)

		// Density check
		if len(neighbours) < MinPts {
			(*coords)[p].Label = -1 // Label as noise with value -1
			continue
		}

		nclusters++                             // increase cluster count
		(*coords)[p].Label = nclusters + offset // Label initial point; add offset to avoid overlapping IDs in partitions

		var seedSet []*LabelledGPScoord          // Seedset stores the core point's neighbours
		seedSet = append(seedSet, neighbours...) // NOTE: Removal of core point/p!=q condition is in rangeQuery()

		// Find the neighbours of seedSet (find the neighbours of the core point's neighbours)
		for q := 0; q < len(seedSet); q++ {
			if seedSet[q].Label == -1 { // -1 means noise
				seedSet[q].Label = nclusters + offset // Change noise to border point
			}

			if seedSet[q].Label != 0 { // not undefined; point has been processed (e.g.: border point)
				continue
			}

			seedSet[q].Label = nclusters + offset // Label initial point; add offset to avoid overlapping IDs in partitions
			seedNeighbours := rangeQuery(*coords, *seedSet[q], eps)

			if len(seedNeighbours) >= MinPts {
				// NOTE: no need to check for duplicates when appending to seedSet
				// core point/p!=q condition is removed in rangeQuery()
				seedSet = append(seedSet, seedNeighbours...)
			}
		} // end of inner for loop

	} // end of outer for loop

	// Printing the result (do not remove)
	fmt.Printf("Partition %10d : [%4d,%6d]\n", offset, nclusters, len(*coords))

	return nclusters
}

// Scans the database/partition and returns a list of neighbouring LabelledGPScoords within the eps radius around a given LabelledGPScoord
// NOTE: This version of rangeQuery() does not include the core point as a neighbouring point
// This is a helper function to DBSCAN()
func rangeQuery(db []LabelledGPScoord, p LabelledGPScoord, eps float64) []*LabelledGPScoord {

	var neighbours []*LabelledGPScoord

	for i := 0; i < len(db); i++ {
		// NOTE: Does not include the core point as a neighbour
		if p != db[i] && calculateDistance(p, db[i]) <= eps {
			neighbours = append(neighbours, &db[i])
		}
	}
	return neighbours
}

// Returns the Eucidian distance (float64) between two LabelledGPScoords
// This is a helper function to rangeQuery()
func calculateDistance(p1 LabelledGPScoord, p2 LabelledGPScoord) float64 {
	return math.Sqrt((p1.lat-p2.lat)*(p1.lat-p2.lat) + (p1.long-p2.long)*(p1.long-p2.long))
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

		// get lattitude
		lat, err := strconv.ParseFloat(record[9], 64)
		if err != nil {
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
		long, err := strconv.ParseFloat(record[8], 64)
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
