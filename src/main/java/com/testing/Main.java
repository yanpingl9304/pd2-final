package com.testing;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.json.JSONObject;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Main {
    private static final String token;

    private static String readConfigFile(String fileName) {
        StringBuilder sb = new StringBuilder();
        try (InputStream is = Main.class.getClassLoader().getResourceAsStream(fileName);
             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    static {
        String jsonString = readConfigFile("config.json");
        JSONObject json = new JSONObject(jsonString);
        token = json.getString("token");
    }


    public static void main(String[] args) throws LoginException {

        JDA jda = JDABuilder.createDefault(token) // enable all default intents
                  .enableIntents(GatewayIntent.GUILD_MEMBERS)// also enable privileged intent
                  .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                  .build();
        jda.addEventListener(new Listeners());

    }
}
