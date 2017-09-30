The application ThreadClient supports the following command line argument formats:

* javac ThreadClient
  No arguments, runs with default number of threads (10), default number of iterations
  per thread (100), default IP address(52.40.166.203) and default port(8080)

* javac ThreadClient <number of threads> <number of iterations>
  Runs with given number of threads and iterations, default IP address and port

* javac ThreadClient <number of threads> <number of iterations> <IP address> <port number>
  Runs with given number of threads, number of iterations, IP address, and port number
