#! /bin/sh

# ${JAVA_HOME}/bin/java -cp "lib/*" lam.schedule.launch.LamScheduleLaunch > \
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
APP_MAIN="lam.schedule.launch.LamScheduleLaunch"

APP_PID=0

# get pid of this app, which main class is ${APP_MAIN}
#
getAppPid(){
    COMMAND_RS=`${JAVA_HOME}/bin/jps -l | grep ${APP_MAIN}`
    if [ -n "${COMMAND_RS}" ];
    then
        APP_PID=`echo ${COMMAND_RS} | awk '{print $1}'`
    else
	APP_PID=0
    fi
}

JAVA_OPTS="-server -Xloggc:${APP_LOG}/gc.log"
JAVA_OPTS="${JAVA_OPTS} -Dlam.schedule.home=${APP_HOME} -Dlam.schedule.test=false -Dlam.rpc.port=6666 -Dlam.container=filecache"
JAVA_OPTS="${JAVA_OPTS} -Dlam.schedule.in.advance.hour=3"

# classpath, including all jar in the "lib" directory
CLASSPATH="./classes"
for APP_JAR in ./lib/*.jar;
do
    CLASSPATH="${CLASSPATH}:${APP_JAR}"
done

startup(){
    getAppPid
    echo "==============================================================================================="
    echo "getAppPid, pid:${APP_PID}"
    if [ ${APP_PID} -ne 0 ];
    then
	echo "${APP_MAIN} already started(PID:${APP_PID})"
    else
	echo "Starting ${APP_MAIN}"
	echo "command:"
	echo "${JAVA_HOME}/bin/java ${JAVA_OPTS} -classpath ${CLASSPATH} ${APP_MAIN} > /dev/null &"
	${JAVA_HOME}/bin/java ${JAVA_OPTS} -classpath ${CLASSPATH} ${APP_MAIN} > /dev/null &
        getAppPid
	if [ ${APP_PID} -ne 0 ]; then
	    echo "${APP_MAIN} started in pid:${APP_PID}[success]"
	else
	    echo "${APP_MAIN} fail to start[fail]"
	fi
   fi
}

# call startup function
startup