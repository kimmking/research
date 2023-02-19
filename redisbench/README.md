# research-redisbench

A  redis benchmark in java like redis-benchmark.

## benchmark for standalone with different client

1. create a standalone redis server
2. fill server with underlying data
3. create jedis pool to test it with C connections, T threads, D bytes payload.
4. create lettuce test.
5. create redisson test.
6. generate all test report.

## Performance Benchmark(Jedis/Lettuce)

```
==== Performance Benchmark for Redis Client ====
==== Jedis/BIO-Socket vs Lettuce/NIO-Netty ====
Test case for[data=100000,threads=20,poolSize=20]
dbsize:100000
db has been initialized, this init be ignore.
===> jedis bench 100000 starting ...
===> Jedis bench finish in 1574 ms
*** jedis 63532.40152477763 qps
===> lettuce bench 100000 starting ...
===> lettuce bench finish in 1179 ms
*** lettuce 84817.64206955046 qps
```

