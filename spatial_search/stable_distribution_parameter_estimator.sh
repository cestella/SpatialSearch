#!/bin/bash 

CLASSPATH="./target/spatial_search-1.0-SNAPSHOT.jar"
for i in ./target/dependency/*.jar;do
   CLASSPATH="$i:$CLASSPATH"
done
echo "CLASSPATH = $CLASSPATH"
java -Djava.util.logging.config.file=/usr/local/explorys/config/datagrid-logging.properties \
   -classpath "$CLASSPATH" \
   com.caseystella.math.stabledistribution.ParameterInvestigationCLI "$@"
