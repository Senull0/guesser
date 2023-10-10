package com.cardg.cardg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

interface DataSource {
    Connection getConnection() throws SQLException;
}

class JdbcDataSource implements DataSource {
    private final String url;

    public JdbcDataSource(String url) {
        this.url = url;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }
}


public class Rankings {
    public List<Map<String, Object>> getPlayerStats() {
        List<Map<String, Object>> playerStatsList = new ArrayList<>();
        DataSource dataSource = new JdbcDataSource("jdbc:sqlite:src/main/db/cardsg.sqlite3");

        try (Connection connection = dataSource.getConnection()) {
            String sqlQuery = "SELECT PlayerNAME AS 'Player', " +
                    "ROW_NUMBER() OVER (ORDER BY PlayerSuccessfulGuesses DESC) AS 'Rank', " +
                    "PlayerSuccessfulGuesses AS 'Successful Guesses', " +
                    "playerTotalGuesses AS 'Total Guesses', " +
                    "ROUND((CAST(PlayerSuccessfulGuesses AS DOUBLE) / CAST(playerTotalGuesses AS DOUBLE) * 100.0), 3) as 'Guess Ration %' " +
                    "FROM Players " +
                    "ORDER BY PlayerSuccessfulGuesses DESC " +
                    "LIMIT 25;";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Map<String, Object> playerStats = new HashMap<>();
                        playerStats.put("Player", resultSet.getString("Player"));
                        playerStats.put("Rank", resultSet.getString("Rank"));
                        playerStats.put("Successful Guesses", resultSet.getInt("Successful Guesses"));
                        playerStats.put("Total Guesses", resultSet.getInt("Total Guesses"));
                        playerStats.put("Guess Ration %", resultSet.getDouble("Guess Ration %"));
                        playerStatsList.add(playerStats);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return playerStatsList;
    }


}
