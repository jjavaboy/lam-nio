#! /bin/sh

# ${JAVA_HOME}/bin/java -cp "lib/*" lam.dubbo.provider.start.LamDubboProviderStartUp > \
# /opt/deploy-service/lam-nio-core/logs/stdout.log 2>&1 &

APP_HOME=`pwd`

# directory of log
APP_LOG="${APP_HOME}/logs"

if ! test -e ${APP_LOG}
then
    echo "${APP_LOG} not exists, mkdir -p ${APP_LOG}"
    mkdir -p ${APP_LOG}
fi

# main class
APP_MAIN="lam.dubbo.provider.start.LamDubboProviderStartUp"

JAVA_OPTS="-server -Xloggc:${APP_LOG}/gc.log"

# classpath, including all jar in the "lib" directory
CLASSPATH="${APP_HOME}/classes"
for APP_JAR in ${APP_HOME}/lib/*.jar;
do
    CLASSPATH="${CLASSPATH}:${APP_JAR}"
done

echo "Starting ${APP_MAIN}"
echo "command:"
echo "nohup ${JAVA_HOME}/bin/java ${JAVA_OPTS} -classpath ${CLASSPATH} ${APP_MAIN} > ${APP_LOG}/nohup.log &"
nohup ${JAVA_HOME}/bin/java ${JAVA_OPTS} -classpath ${CLASSPATH} ${APP_MAIN} > ${APP_LOG}/nohup.log &