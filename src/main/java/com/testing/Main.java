package com.testing;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.json.JSONObject;

import javax.security.auth.login.LoginException;
import java.nio.charset.StandardCharsets;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Main {

    private static final String token;

    //read config.json
     public static String readConfigFile(String fileName) {
        StringBuilder sb = new StringBuilder();
        try (InputStream is = Main.class.getClassLoader().getResourceAsStream(fileName);
               BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    // Get token from json
    static {
        String jsonString = readConfigFile("config.json");
        JSONObject json = new JSONObject(jsonString);
        token = json.getString("token");
    }

    // Build a Bot
    public static void main(String[] args) throws LoginException {

        JDA jda = JDABuilder.createDefault(token)
                  .enableIntents(GatewayIntent.GUILD_MEMBERS)
                  .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                  .build();
        jda.addEventListener(new Listeners());
        jda.addEventListener(new SlashCom());
    }
}
