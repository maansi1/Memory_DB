package memory_DB;

import java.util.Scanner;
import memory_DB.CommandParser.*;

public class Main {

    private static void printHelp() {
        System.out.println("""
HELP:
  START
  STOP
  STATUS
  PUT <key> <value> [ttlSeconds]
     - value can be quoted: PUT 1 "hello world" 10
  GET <key>
  DELETE <key>
  SIZE
  KEYS
  CLEAR
  CLEANUP
  INFO
  EXIT
""");
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        CommandParser parser = new CommandParser();
        DB db = new DB(); 
        System.out.println("Welcome to Memory-DB v1");
        System.out.println("Type HELP to see commands.");
        System.out.println();

        while (true) {
            System.out.print("memory-db> ");

            try {
                String input = scanner.nextLine();
                Command cmd = parser.parse(input);

                switch (cmd.type) {

                    case START -> db.start();
                    case STOP -> db.stop();
                    case STATUS -> System.out.println(db.isRunning() ? "RUNNING" : "STOPPED");

                    case PUT -> db.put(cmd.key, cmd.value, cmd.ttlSeconds);

                    case GET -> System.out.println("OK: " + db.get(cmd.key));

                    case DELETE -> db.delete(cmd.key);

                    case SIZE -> System.out.println("OK: size=" + db.size());

                    case KEYS -> System.out.println("OK: keys=" + db.keys());

                    case CLEAR -> db.clear();

                    case CLEANUP -> {
                        db.manualCleanup();
                        System.out.println("OK: cleanup done");
                    }

                    case INFO -> System.out.println(db.info());

                    case HELP -> printHelp();

                    case EXIT -> {
                        System.out.println("Goodbye!");
                        scanner.close();
                        return;
                    }

                    default -> System.out.println("ERROR: Unknown command (Type HELP)");
                }

            } catch (InvalidCommandException |
                     InvalidKeyException |
                     InvalidTTLException |
                     DatabaseNotRunningException |
                     KeyNotFoundException e) {
              
                System.out.println("ERROR: " + e.getMessage());

            } catch (Exception e) {
          
                System.out.println("ERROR: Something went wrong: " + e.getClass().getSimpleName());
            }
        }
    }
}
