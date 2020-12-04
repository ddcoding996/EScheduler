#!/bin/sh

cd ..
current_dir=$(pwd)
lib_dir=${current_dir}/lib
java_command=java

cd ${lib_dir}

${java_command} -jar EScheduler-cluster.jar $1