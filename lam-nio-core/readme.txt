#当前开发系统是在win10下，jdk1.7

linux 启动脚本/lam-nio-core/src/main/resources/start.sh

1.rpc framework=>lam.rpcframework, test=>src/test/java/RpcFramework..Test.java
2.delay task=>lam.delaytask, test=>src/test/java/DelayTaskTest.java
3.expired key-value=>src/main/java/lam/concurrent/ExpiredConcurrentMap.java, test=>src/test/java/ExpiredCucurrentMapTest.java
  simulate redis function:expired key
4.有穷自动机=>lam/automaton/DeterministicFiniteAutomaton.java, test=>lam/automaton/DeterministicFiniteAutomaton.java
5.LUR cache=>lam/util/LruHashMap.java
6.分布式锁-基于redis=>lam.concurrent.lock.DistributedExecutor
7.同一台机器锁-基于文件=>lam.concurrent.lock.LFileLock
8.分布式事务处理=>
	A节点=>减钱:lam.dubbo.banka.user.service.impl.UserServiceImpl.decreaseUserMoneyCrossBank(Integer, Integer, double)
	     A节点会同一事务（减钱和发消息）操作
	B节点=>监听加钱消息:lam.mq.consumer.ActiveMQConsumer.start()
9.create id=>lam.dao.id.util.IdSequence
       test=>lam-dao/src/test/java/IdSequenceTest
10.async log4j=>lam.log.LamScheduleAsyncAppender
	      test=>lam-dao/src/test/java/IdSequenceTest
11.zookeeper，队列的消费者集群，只有一个消费者在消费，其他消费者在stand by.=>lam.mq.consumer.DistributedActiveMQConsumer
12.Consistent hash，一致性哈希，lam-nio-core/src/main/java/lam.distribution.ConsistentHash
13.ObjectUtil,clone field from one Object to another Object.