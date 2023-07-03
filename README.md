# Application Documentation - Fetch Dynamic IPs by Host Name

## Introduction

This document provides an overview and usage instructions for an application built in Java that fetches all dynamic IPs associated with a given host name. The application is designed to assist in retrieving the list of IP addresses for a specific host dynamically.

## Requirements

To run the application, the following requirements must be met:

-   Java Development Kit (JDK) 8 or above.
-   Internet connectivity to resolve host names.

## Usage

To use the application, follow the steps outlined below:

1.  Place the jar file and script to your local directory.
2.  Open a terminal or command prompt and navigate to the application directory.

### Running the Application

To run the application, execute the following command with at least one or multiple host:

shellCopy code

`getIP example1.com [example2.com]...`

The application will initiate a DNS lookup for the provided host name and retrieve the dynamic IPs associated with it. The IP addresses will be print in log files or based on host it will generates multiple text file with host name and print all IP addresses within the file.

### Example Output

diffCopy code

`
- 192.0.2.1
- 203.0.113.5
- 198.51.100.10`

## Limitations

-   The application relies on DNS resolution to fetch dynamic IPs. Therefore, the availability and accuracy of the IP addresses are dependent on the DNS configuration of the host name.
-   The application currently supports only IPv4 addresses. IPv6 addresses are not included in the output.

## Conclusion

The application provides a simple and straightforward method to fetch dynamic IPs associated with a host name. By following the instructions outlined in this documentation, you can retrieve the list of IP addresses dynamically.

Please note that this document describes the initial version of the application and may be updated in the future to include additional features or enhancements.