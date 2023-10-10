package com.cardg.cardg;

import org.springframework.stereotype.Service;
import java.util.Map;



@Service
public class Gameservice {

    private Map<String, String> qstmap;
    private Integer correctGuesses_pergame;
    private Integer game_moves;
    private String gameresults;
    private String currentPlayerName;
    private Boolean rev;
    private Boolean cleanhandler;



    // Game initialization
    public String startGame(String player, Integer len, Boolean rev) {
        
        this.rev = rev;
        qstmap = null;
        
        currentPlayerName = player;
        correctGuesses_pergame = 0;
        game_moves = 0;

        gameresults = "== Results == \n\n";
        cleanhandler= false;


        // Ensure a minimum game length of 5 questions.
        if (len < 5) {
            len = 10;
        }

        if (Player.getPlayerFromDatabase(player) == null) {
            Player.savePlayerToDatabase(player, "Rookie", 0, 0, 0);
        }
        
        Questions questions = new Questions("src/main/resources/countries_c.json", len);
        if (!rev) {
            qstmap = questions.gameQuestions(false);
            return "What's the capital of: " + qstmap.values().iterator().next();
        } else {
            qstmap = questions.gameQuestions(true);
            return "Where's this capital: " + qstmap.values().iterator().next();
        }
    }



    // Check and handle player's answer 
    public String checkAnswer(String input) {
        String firstKey = qstmap.keySet().stream().findFirst().orElse(null);
        String firstValue = qstmap.get(firstKey);

        if (!qstmap.isEmpty()) {
            game_moves++;
            if (firstKey.equalsIgnoreCase(input)) {
                handleCorrectGuess(input);
                gameresults += "[\u2713] ";
            } else {
                handleIncorrectGuess(input);
                gameresults += "[x] ";
            }
        }

        if (!rev) {
            gameresults += String.format("Capital: %s, Country: %s%n", firstKey, firstValue);
        } else {
            gameresults += String.format("Country: %s, Capital: %s%n", firstKey, firstValue);
        }
        

        
        if (qstmap.keySet().iterator().hasNext()) {
            try {
                qstmap.remove(qstmap.keySet().iterator().next());

                if (!rev) {
                    return "What is the capital of:  " + qstmap.values().iterator().next();
                } else {
                    return "Where's this capital: " + qstmap.values().iterator().next();
                }
            } catch (Exception e) {
            }
        }

        updatePlayerStats();
        gameresults += 
        "\npoints: " + correctGuesses_pergame + "/" + game_moves
        + "\n" + tillnewRank + " points to the next rank \u25B2" 
        + "\nNext Rank: " + nextRank;

        // null response after sending a PUT request when the game has finished
        if (cleanhandler) {gameresults = "";}
        cleanhandler = true;

        return gameresults; 
    }


    // Update player stats in the db
    private void updatePlayerStats() {
        Player upd = Player.getPlayerFromDatabase(currentPlayerName);
        int currentTotalGames = upd.getPlayerTotalGames();
        int currentTotalGuesses = upd.getPlayerTotalGuesses();
        int currentSuccessfulGuesses = upd.getPlayerSuccessfulGuesses();

        int newTotalGames = currentTotalGames + 1;
        int newTotalGuesses = currentTotalGuesses + game_moves;
        int newSuccessfulGuesses = currentSuccessfulGuesses + correctGuesses_pergame;
        String newRank = calculateRank(newSuccessfulGuesses);

        Player.updatePlayerInDatabase(currentPlayerName, newRank, newTotalGames, newTotalGuesses, newSuccessfulGuesses);
    }

    private void handleCorrectGuess(String input) {
        correctGuesses_pergame++;
        System.out.println("+");
    }

    private void handleIncorrectGuess(String input) {
        System.out.println("-");
    }


    // Calculate player's rank based on the successful guesses
    private String calculateRank(int successGuesses) {
        if (successGuesses < SUCCESS_THRESHOLD_ROOKIE) {
            tillnewRank = SUCCESS_THRESHOLD_ROOKIE - successGuesses;
            nextRank = "Explorer";
            return "Rookie";
        } else if (successGuesses < SUCCESS_THRESHOLD_EXPLORER) {
            tillnewRank = SUCCESS_THRESHOLD_EXPLORER - successGuesses;
            nextRank = "Adventurer";
            return "Explorer";
        } else if (successGuesses < SUCCESS_THRESHOLD_ADVENTURER) {
            tillnewRank = SUCCESS_THRESHOLD_ADVENTURER - successGuesses;
            nextRank = "Geographer";
            return "Adventurer";
        } else {
            tillnewRank = 0;
            nextRank = "-";
            return "Geographer";
        }
    }


    // Ranks
    private static final int SUCCESS_THRESHOLD_ROOKIE = 25;
    private static final int SUCCESS_THRESHOLD_EXPLORER = 150;
    private static final int SUCCESS_THRESHOLD_ADVENTURER = 300;
    private Integer tillnewRank;
    private String nextRank;
}

