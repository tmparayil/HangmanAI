#!/bin/bash

#This is the bash executable which runs the solver code.
echo "Running the hangman game"

#Check if runtime values have been provided
file=$1
#Check done#

if [ $file == "tparayil@uci.edu" ]; then
		for ((i = 0 ; i < $2 ; i++)); do
  		{
  			javac HangmanAI.java;
  			javac HuluObject.java;
  			javac -cp .:gson-2.6.2.jar Solver.java;
  			java -cp .:gson-2.6.2.jar Solver;
  		}
		done
fi