// Project CSI2120/CSI2520 
// Winter 2022
// Robert Laganiere, uottawa.ca

package main

import (
	"fmt"
	"time"
	"sync"
)

func main() {

    // channel for sending jobs
	// here a job is a simple int
	jobs := make(chan int, 5)
	
	// mutex for synchronisation
	var mutex sync.WaitGroup
	mutex.Add(2) 

    // start one consumer
	go consomme(jobs, "A", &mutex) 
	go consomme(jobs, "B", &mutex) 

	// producer
	// produces 10 jobs here
	for j := 1; j <= 10; j++ {
		jobs <- j
		time.Sleep(time.Second) // wasting time
		fmt.Printf("job sent: %2d\n", j)
	}
	
	close(jobs)

    // wait for consumers to terminate
	mutex.Wait()
}

// consumer function
func consomme(jobs chan int, name string, done *sync.WaitGroup) {
	for {
		j, more := <-jobs

		if more {
			fmt.Printf("%s consuming: %3d\n", name, j*j)
			time.Sleep(4 * time.Second) // wasting time

		} else {
			fmt.Println("done!")
			done.Done()
			return
		}
	}
}