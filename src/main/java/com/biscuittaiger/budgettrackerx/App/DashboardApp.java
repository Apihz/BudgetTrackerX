package com.biscuittaiger.budgettrackerx.App;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DashboardApp {
    private String userId;
    private int month;
    private ArrayList<ArrayList<Double>> userData = new ArrayList<>();

    public DashboardApp(String userId, int month) {
        this.userId = userId;
        this.month = month;
        for (int i = 0; i < 12; i++) {
            userData.add(new ArrayList<>());
        }
    }

    public void readInformationFromFile() {
        InputStream file = getClass().getResourceAsStream("/com/biscuittaiger/budgettrackerx/budget_info.txt");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file))) {
            String line;
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length == 7 && data[0].equals(userId)) {
                    int dataMonth = Integer.parseInt(data[1]);
                    if (dataMonth >= 1 && dataMonth <= 12) {
                        ArrayList<Double> monthData = new ArrayList<>();
                        monthData.add(Double.parseDouble(data[2]));
                        monthData.add(Double.parseDouble(data[3]));
                        monthData.add(Double.parseDouble(data[4]));
                        monthData.add(Double.parseDouble(data[5]));
                        monthData.add(Double.parseDouble(data[6]));
                        userData.set(dataMonth - 1, monthData);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double getBalance(int month) {
        return userData.get(month - 1).get(0);
    }

    public double getIncome(int month) {
        return userData.get(month - 1).get(1);
    }

    public double getExpense(int month) {
        return userData.get(month - 1).get(2);
    }

    public double getBudget(int month) {
        return userData.get(month - 1).get(3);
    }

    public double getSavings(int month) {
        return userData.get(month - 1).get(4);
    }
}
