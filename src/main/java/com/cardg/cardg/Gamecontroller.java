package com.cardg.cardg;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class Gamecontroller {

    private Gameservice gameserv;
    private Instructions info;
    
    public Gamecontroller() { 
        this.gameserv = new Gameservice();
        this.info = new Instructions();
    }
    

    @GetMapping("/info")
    public String getinfo() {
        return info.Game_instructions();
    }


    @GetMapping("/stats/{player}")
    public Player getplayer_c(@PathVariable String player) {
        return Player.getPlayerFromDatabase(player);
    }


    @PostMapping("/newplayer")
    public String createPlayer(@RequestParam String name) {
        return Player.savePlayerToDatabase(name, "Rookie", 0, 0, 0);
        
    }

    
    @GetMapping("/rankings")
    public List<Map<String, Object>> getRankings() {
        Rankings rankings = new Rankings();
        return rankings.getPlayerStats();
    }


    @PostMapping("/game")
    public String displayQuestion(
        @RequestParam String player, 
        @RequestParam (defaultValue = "10", required = false) Integer len,
        @RequestParam (defaultValue = "false", required = false) Boolean rev) {
        return gameserv.startGame(player, len, rev);
        }


    @PutMapping("/game")
    public String checkAnswer(@RequestParam String input) {
        return gameserv.checkAnswer(input);
    }


}






