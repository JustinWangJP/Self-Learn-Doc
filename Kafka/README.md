1. Partitioner使用方法
   + Sample Hash Partitioner 
   ```
      Public int partition(Object key, int numPartitions){
          if((key instanceof Integer)){
              return Math.abs(Integer.parseInt(key.toString()))%numPartitions;
          }
          return Math.abs(key.hashCode()%numPartitions);
      }
   ```
   + Round Robin Partitioner
    ```
      private static AtomicLong next=new AtomicLong();
      
      Public int partition(Object key, int numPartitions){
          long nextIndex= next.incrementAndGet();
          return (int)nextIndex%numPartitions;
      }
    ```


2. 