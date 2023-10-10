package com.cardg.cardg;



public class Instructions {
    public String Game_instructions() {

        final String gameInstructions = "Game Instructions:\n\n"
        + "1. Setting up a game:\n"
        + "   - Send a POST request to `/game` with these parameters:\n"
        + "     - 'player' (required): Your player name.\n"
        + "     - 'len' (optional, default is 10): Length of the game sequence.\n"
        + "     - 'rev' (optional, default is false): Set to true for a reverse game (guess the country for the city).\n"
        + "   - Example: `localhost/game?player=Alex`\n\n"
        + "2. Creating a Player:\n"
        + "   - Send a POST request to `/newplayer` with the 'name' parameter.\n"
        + "   - Example: `localhost/newplayer?name=Alex`\n\n"
        + "3. Playing the Game:\n"
        + "   - Send a PUT request to `/game` with the 'input' parameter to make your guess.\n"
        + "   - Example: `localhost/game?input=city`\n"
        + "   - Note: Create a game first!\n\n"
        + "4. Checking Your Progress:\n"
        + "   - Send a GET request to `/stats` with the 'player' variable.\n"
        + "   - Example: `localhost/stats?player=Alex`\n\n"
        + "5. Viewing Rankings:\n"
        + "   - Send a GET request to `/rankings` to view the rankings.\n"
        + "   - Example: `localhost/rankings`\n\n"
        + "!Note: this game was created for learning and testing purposes. Have fun";

        return gameInstructions;
    }
}
