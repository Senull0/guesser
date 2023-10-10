package com.cardg.cardg;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class Questions {
    private List<Map.Entry<String, String>> questions; 

    public Questions(String filePath, int gameLength) {
        this.questions = new ArrayList<>();
        initializeQuestions(filePath, gameLength);
    }


    // Initialization of the random list of questions from a JSON file
    private void initializeQuestions(String filePath, int gameLength) {
        try (FileReader fileReader = new FileReader(filePath)) {
            JSONTokener jsonTokener = new JSONTokener(fileReader);
            JSONArray jsonArray = new JSONArray(jsonTokener);

            List<Integer> randomIndList = genIndexes(gameLength, jsonArray.length());

            for (int i : randomIndList) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String country = jsonObject.getString("country");
                String city = jsonObject.getString("city");
                questions.add(new AbstractMap.SimpleEntry<>(city, country));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Generation of a random indexes list (used in questions generation)
    private List<Integer> genIndexes(int gameLength, int maxIndex) {
        Random r_ind = new Random();
        List<Integer> randomIndList = new ArrayList<>();

        for (int i = 0; i < gameLength; i++) {
            int randomInd = r_ind.nextInt(maxIndex);
            randomIndList.add(randomInd);
        }
        Collections.sort(randomIndList);
        return randomIndList;
    }


    // Maps for game questions, with cities as keys & countries as values 
    // (or vice versa, based on the "rev" flag)
    public Map<String, String> gameQuestions(Boolean rev) {

        Map<String, String> qstmap = new HashMap<>();
        Map<String, String> qstmap_rev = new HashMap<>();

        for (Map.Entry<String, String> entry : questions) {
            String city = entry.getKey();
            String country = entry.getValue();
            qstmap.put(city, country);
            qstmap_rev.put(country, city);
        }
        if (rev) {
            return qstmap_rev; // keys: countries | values: capitals;
        }
        return qstmap; // keys: capitals | values: countries;
    }

    public List<Map.Entry<String, String>> getQuestions() {
        return questions;
    }


}



