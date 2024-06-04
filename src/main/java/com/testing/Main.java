package com.testing;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class Main {
    public static final String token = "MTI0NzEwMjI5NDU5MjMyNzcyMA.GjXg1q.UtM4UGAcXRZSANhJX1FrdWGscBLXo13ieWntQU";

    public static void main(String[] args) throws LoginException {
        JDA jda = JDABuilder.createDefault(token) // enable all default intents
                  .enableIntents(GatewayIntent.GUILD_MEMBERS)// also enable privileged intent
                  .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                  .build();
        jda.addEventListener(new Listeners());
    }

}
