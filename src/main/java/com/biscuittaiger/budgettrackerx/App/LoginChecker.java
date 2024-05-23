package com.biscuittaiger.budgettrackerx.App;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class LoginChecker {
    private Scanner scanner;
    public LoginChecker(String path) {
        try {
            scanner = new Scanner(path);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // comma regex
                var result = line.split(",",0);
                if(result[0].equals("dwa")){
                    break;
                }
            }

        } catch ( Exception e) {
            throw new RuntimeException(e);
        }
    }
}
