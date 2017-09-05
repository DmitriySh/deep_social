#!/bin/sh

java -jar -Dfile.encoding=UTF-8 ../build/libs/deep_social-all-*.jar csv ./sample_installs.csv ./result.csv
