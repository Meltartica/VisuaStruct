package com.visustruct;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DisplayFacts {
    private final List<String> factsList = new ArrayList<>();
    /**
     * Loads facts from the "facts.txt" file and populates the factsList.
     * Each line in the file represents a fact.
     * The method reads the file and adds each fact to the factsList.
     */
    void loadFactsFromFile() {
        try {
            InputStream inputStream = new FileInputStream("src/main/java/assets/facts.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                factsList.add(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getFactsList() {
        return factsList;
    }
}
