# Matrix-Mutiplication---Single-and-Multi-Threaded
A simple project used to practice matrix multiplication algorithms and learn multi threading operations to optimize program speed.

The project was completed in Java and utilizes a basic algorithm for single-threaded matrix multiplication while the multi-threaded algorithm decouples the dot product calculation from the inner most loop and executes the dotproduct in each thread.

The threads are managed by ExecutorService and the program was tested utilizing three different types of thread pools: Work Stealing, Cached, and Fixed. A report is included, showcasing the performance of each type of thread pool as well as a description of the algorithm and some challenges faced while working on the project.
