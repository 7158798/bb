set JAVA_OPTS= -Xmx1024M -Xms512M -XX:MaxPermSize=256m &
set GRADLE_OPTS=-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=9997,suspend=n &
gradle appRun