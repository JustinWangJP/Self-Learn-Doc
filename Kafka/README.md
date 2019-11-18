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
   + Consumer是绑定到partition上的，而不是消息。
 
 3. High Level Consumer Rebalance
   + Consumer启动及Rebalance流程
   ++ zookeeper上的路径为/consumers/[consumer group]/ids/[consumer id]
   ++ 在/consumers/[consumer group]/ids上注册Watch
   ++ 在/brokers/ids上注册Watch
   ++ 如果Consumer通过Topic Filter创建消息流，则它会同时在/brokers/topics上也创建Watch
   ++ 强制自己在其Consumer Group内启动Rebalance流程
   
 4. Low Level Consumer
   + 使用Low Level Consumer(Simple Consumer)的主要原因是，用户希望比Consumer Group更好的控制数据的消费，
    如
    ++ 同一条消息读多次，方便Replay
    ++ 只消费某个Topic的部分Partition
    ++ 管理事务，从而确保每条消息被处理一次（Exactly Once）
   + 与High Level Consumer相对，Low Level Consumer要求用户做大量的额外工作
    ++ 在应用程序中跟中处理Offset,并决定下一跳消费哪条消息
    ++ 获取每个Partition的Leader
    ++ 处理Leader的变化
    ++ 处理多Consumer的协作
