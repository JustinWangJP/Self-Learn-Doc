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


2. Consumer Group
   + 消息被消费后，并不会被删除，只是相应的offset加一
   + 对于每条消息，在同一个Consumer Group里只会被一个Consumer消费
   + 不同Consumer Group可消费同一条消息
   + Kafka的设计理念之一就是同时提供对离线批处理和在线流处理。
   + 可同时使用Hadoop系统进行离线批处理，Storm或其他流处理系统进行流处理
   + 可使用kafka的Mirror Maker将消息从一个数据中心镜像到另一个数据中心
