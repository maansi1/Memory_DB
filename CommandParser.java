package memory_DB;

import java.util.ArrayList;
import java.util.List;


enum CommandType {
    PUT, GET, START, STOP, DELETE, STATUS, HELP, SIZE, KEYS, CLEAR, CLEANUP, INFO, EXIT
}


class Command {
    CommandType type;
    Integer key;
    String value;
    Long ttlSeconds; 
}

public class CommandParser {

    public Command parse(String input) {

       
        if (input == null || input.trim().isEmpty()) {
            throw new InvalidCommandException("Empty command. Type HELP to see usage.");
        }

        List<String> tokens = tokenize(input.trim());


        if (tokens.isEmpty()) {
            throw new InvalidCommandException("Empty command. Type HELP.");
        }

        Command cmd = new Command();

      
        try {
            cmd.type = CommandType.valueOf(tokens.get(0).toUpperCase());
        } catch (Exception e) {
            throw new InvalidCommandException("Unknown command: " + tokens.get(0) +
                    ". Type HELP for commands.");
        }

       
        switch (cmd.type) {

            case PUT: {
                
                if (tokens.size() < 3) {
                    throw new InvalidCommandException("Usage: PUT <key> <value> [ttlSeconds]");
                }

                cmd.key = parseKey(tokens.get(1));
                cmd.value = tokens.get(2);

               
                if (tokens.size() >= 4) {
                    cmd.ttlSeconds = parseTtl(tokens.get(3));
                } else {
                    cmd.ttlSeconds = 0L;
                }
                break;
            }

            case GET:
            case DELETE: {
             
                if (tokens.size() < 2) {
                    throw new InvalidCommandException("Usage: " + cmd.type + " <key>");
                }
                cmd.key = parseKey(tokens.get(1));
                break;
            }

            case START:
            case STOP:
            case STATUS:
            case HELP:
            case SIZE:
            case KEYS:
            case CLEAR:
            case CLEANUP:
            case INFO:
            case EXIT:
              
                break;

            default:
                throw new InvalidCommandException("Unsupported command. Type HELP.");
        }

        return cmd;
    }

    
    private List<String> tokenize(String input) {
        List<String> out = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);

            if (ch == '"') {
                inQuotes = !inQuotes;
                continue;
            }

            if (!inQuotes && Character.isWhitespace(ch)) {
                if (cur.length() > 0) {
                    out.add(cur.toString());
                    cur.setLength(0);
                }
            } else {
                cur.append(ch);
            }
        }

        if (cur.length() > 0) out.add(cur.toString());

     
        if (inQuotes) {
            throw new InvalidCommandException("Missing closing quote (\") in value.");
        }

        return out;
    }

    private int parseKey(String token) {
        try {
            return Integer.parseInt(token);
        } catch (NumberFormatException e) {
            throw new InvalidKeyException("Invalid key: " + token + ". Key must be an integer.");
        }
    }

    private long parseTtl(String token) {
        try {
            long ttl = Long.parseLong(token);
            if (ttl < 0) {
                throw new InvalidTTLException("TTL cannot be negative. Use 0 for never expire.");
            }
            return ttl;
        } catch (NumberFormatException e) {
            throw new InvalidTTLException("Invalid TTL: " + token + ". TTL must be a number.");
        }
    }



    static class DatabaseNotRunningException extends RuntimeException {
        DatabaseNotRunningException() {
            super("Database is not running. Use START first.");
        }
    }

    static class InvalidCommandException extends RuntimeException {
        InvalidCommandException(String msg) {
            super(msg);
        }
    }

    static class InvalidKeyException extends RuntimeException {
        InvalidKeyException(String msg) {
            super(msg);
        }
    }

    static class InvalidTTLException extends RuntimeException {
        InvalidTTLException(String msg) {
            super(msg);
        }
    }

    static class KeyNotFoundException extends RuntimeException {
        KeyNotFoundException(String msg) {
            super(msg);
        }
    }
}
