package com.cardg.cardg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class Player {
    private String playerName;
    private String playerRank;
    private int playerTotalGames;
    private int playerTotalGuesses;
    private int PlayerSuccessfulGuesses;
    
    private static final String URL = "jdbc:sqlite:src/main/db/cardsg.sqlite3"; 


    // Getters and setters
    public int getPlayerSuccessfulGuesses() {
        return PlayerSuccessfulGuesses;
    }

    public void setPlayerSuccessfulGuesses(int playerSuccessfulGuesses) {
        PlayerSuccessfulGuesses = playerSuccessfulGuesses;
    }

    public Player(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerRank() {
        return playerRank;
    }

    public void setPlayerRank(String playerRank) {
        this.playerRank = playerRank;
    }

    public int getPlayerTotalGames() {
        return playerTotalGames;
    }

    public void setPlayerTotalGames(int playerTotalGames) {
        this.playerTotalGames = playerTotalGames;
    }

    public int getPlayerTotalGuesses() {
        return playerTotalGuesses;
    }

    public void setPlayerTotalGuesses(int playerTotalGuesses) {
        this.playerTotalGuesses = playerTotalGuesses;
    }


    
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }



    public static String savePlayerToDatabase(String playerName, String playerRank, int playerTotalGames, int playerTotalGuesses, int PlayerSuccessfulGuesses) {
        if (Player.getPlayerFromDatabase(playerName) != null) {
            return "Error";
        }
        else {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO players (playerNAME, playerRank, playerTotalGames, playerTotalGuesses, PlayerSuccessfulGuesses) VALUES (?, ?, ?, ?, ?)")) {

            
            preparedStatement.setString(1, playerName);
            preparedStatement.setString(2, playerRank);
            preparedStatement.setInt(3, playerTotalGames);
            preparedStatement.setInt(4, playerTotalGuesses);
            preparedStatement.setInt(5, PlayerSuccessfulGuesses);
            preparedStatement.executeUpdate();



            System.out.println("Player '" + playerName + "' saved to the db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Success";
    }
    }



    public static void updatePlayerInDatabase(String playerName, String playerRank, int playerTotalGames, int playerTotalGuesses, int PlayerSuccessfulGuesses) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE players SET playerRank = ?, playerTotalGames = ?, playerTotalGuesses = ?, PlayerSuccessfulGuesses = ? WHERE playerNAME = ?")) {


            preparedStatement.setString(1, playerRank);
            preparedStatement.setInt(2, playerTotalGames);
            preparedStatement.setInt(3, playerTotalGuesses);
            preparedStatement.setInt(4, PlayerSuccessfulGuesses);
            preparedStatement.setString(5, playerName);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    public static Player getPlayerFromDatabase(String playerName) {
        
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT playerNAME, playerRank, playerTotalGames, playerTotalGuesses, PlayerSuccessfulGuesses FROM players WHERE playerNAME = ?")) {

            preparedStatement.setString(1, playerName);

            ResultSet resultSet = preparedStatement.executeQuery();

            
            if (resultSet.next()) {
                Player player = new Player(resultSet.getString("playerNAME"));
                player.setPlayerRank(resultSet.getString("playerRank"));
                player.setPlayerTotalGames(resultSet.getInt("playerTotalGames"));
                player.setPlayerTotalGuesses(resultSet.getInt("playerTotalGuesses"));
                player.setPlayerSuccessfulGuesses(resultSet.getInt("PlayerSuccessfulGuesses"));
                

                return player;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

