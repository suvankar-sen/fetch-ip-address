#!/bin/bash

# Check if Java 8 or above is installed
if ! java -version 2>&1 | grep -q "version \"1[.]8"; then
  echo "Error: Java 8 or above is required."
  exit 1
fi

# Check if the required arguments are provided
if [ "$#" -eq 0 ]; then
  echo "Error: At least one argument is required."
  exit 1
fi

# Run the Java JAR file with all arguments
java -jar "fetch-ip-address-1.0.0.jar" "$@"