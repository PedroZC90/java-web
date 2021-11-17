package com.pedrozc90.javaweb;

public class Debug {

    private static final Database db = new Database();

    public static void main(String[] args) {
        // connect to the database
        db.connect();
        if (db.isConnected()) {
            // launch application
            // launch(args);
            System.out.println("HELLO DATABASE");
        }
        // disconnect from database
        db.disconnect();
        // stop program
        System.exit(0);
    }

}
