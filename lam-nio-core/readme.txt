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