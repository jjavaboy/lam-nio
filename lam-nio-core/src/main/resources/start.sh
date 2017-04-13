#! /bin/sh

# ${JAVA_HOME}/bin/java -cp "lib/*" lam.delaytask.launch.DelayTaskTestStartup > \
# /opt/deploy-service/lam-nio-core/logs/stdout.log 2>&1 &

${JAVA_HOME}/bin/java -cp "lib/*" lam.delaytask.launch.DelayTaskTestStartup > /dev/null 2>&1 &
