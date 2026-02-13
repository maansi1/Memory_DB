Memory-DB

Memory-DB is a simple Java-based command-line mini database.

It stores key-value data in memory and supports TTL (Time-To-Live), background cleanup, and database start/stop control.

Project Overview

This project demonstrates:

In-memory data storage using HashMap

TTL (auto-expiry) functionality

Background cleanup thread

Thread-safe database operations

Custom error handling

Safe command parsing

Command-line interface design

Features
Core Commands

PUT <key> <value> [ttl]
Store a key-value pair. TTL is optional.

GET <key>
Retrieve a stored value.

DELETE <key>
Remove a key from the database.

TTL (Time-To-Live)

Example:

PUT 1 aman 5


The key will automatically expire after 5 seconds.

TTL Rules:

TTL > 0 → Expires after given seconds

TTL <= 0 → Never expires

TTL < 0 → Invalid (rejected)

Expired keys are removed:

During GET

By a background cleanup thread

Database Control Commands

START → Start the database

STOP → Stop the database

STATUS → Show database running state

If the database is stopped and you try GET or PUT, it shows:

ERROR: Database is not running. Use START first.

Utility Commands

SIZE → Show number of active keys

KEYS → Show all stored keys

CLEAR → Remove all entries

CLEANUP → Manually remove expired keys

INFO → Show database statistics

HELP → Show command list

EXIT → Exit the program

Example Usage
memory-db> START
INFO: Database started.

memory-db> PUT 1 hello
OK: Stored key=1

memory-db> GET 1
OK: hello

memory-db> SIZE
OK: size=1

memory-db> STOP
INFO: Database stopped.

memory-db> GET 1
ERROR: Database is not running. Use START first.

How To Run
Compile
javac -d out src/memory_DB/*.java

Run
java -cp out memory_DB.Main

Project Structure
memory_DB/
 ├── Main.java
 ├── DB.java
 ├── Entry.java
 └── CommandParser.java

Technical Highlights

Uses HashMap for in-memory storage

Supports TTL with expiry timestamp logic

Uses a daemon cleanup thread

Thread-safe using synchronized methods

Uses custom exceptions for better error handling

Prevents application crashes on invalid input

Future Improvements

Use ConcurrentHashMap for better concurrency

Add persistence (export/import to file)

Add performance metrics (hit/miss count)

Convert to REST API using Spring Boot

Author

Maansi
