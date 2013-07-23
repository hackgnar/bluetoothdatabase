#!/bin/bash

echo #Starting script to log to file

cd ..

for (( i=1; i <= 500000; i++ )) do


./blucat services | tee -a blucat-`date +"%y-%m-%d"`.log

sleep 5m


done


