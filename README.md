# Memory-DB

Memory-DB ek simple Java based command-line mini database hai.

Ye memory me key-value data store karta hai aur TTL (time-to-live) support karta hai.

---

## Features

- PUT → data store karne ke liye
- GET → data retrieve karne ke liye
- DELETE → data remove karne ke liye
- TTL support (data auto expire hota hai)
- Background cleanup thread
- START / STOP command
- SIZE / KEYS / CLEAR / INFO commands
- Error handling (program crash nahi hota)

---

## Commands

START  
STOP  
PUT <key> <value> [ttl]  
GET <key>  
DELETE <key>  
SIZE  
KEYS  
CLEAR  
INFO  
HELP  
EXIT  

---

## Example

PUT 1 maansi  
GET 1  
DELETE 1  

PUT 2 hello 5  
(after 5 seconds key expire ho jayegi)

---

## How to Run

Compile:
javac -d out src/memory_DB/*.java

Run:
java -cp out memory_DB.Main

---

## Author

Maansi
