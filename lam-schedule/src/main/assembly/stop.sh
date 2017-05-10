#! /bin/sh

APP_MAIN="lam.schedule.launch.LamScheduleLaunch"

APP_PID=0

# get pid of this app, which main class is ${APP_MAIN}
getAppPid(){
    COMMAND_RS=`${JAVA_HOME}/bin/jps -l | grep ${APP_MAIN}`
    if [ -n "${COMMAND_RS}" ]; then
        APP_PID=`echo ${COMMAND_RS} | awk '{print $1}'`
    else
        APP_PID=0
    fi
}

shutdown(){
    getAppPid
    if [ ${APP_PID} -ne 0 ]; then
        echo "Stopping ${APP_MAIN}..."
        kill ${APP_PID}
        getAppPid
        if [ ${APP_PID} -eq 0 ]; then
            echo "Stop ${APP_MAIN} success."
        else
            echo "Fail to stop ${APP_MAIN}."
        fi
    else
        echo "${APP_MAIN} is not running."
    fi
}

# call function of stop
shutdown