#!/bin/sh

bootstrap_sh=bootstrap.sh
chmod +x ${bootstrap_sh}
./bootstrap.sh stop
echo EScheduler-job has been shutdown...