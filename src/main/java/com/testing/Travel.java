package com.testing;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class Travel extends Listeners {
    static {
        String jsonString = Main.readConfigFile("Travel.json");
        JSONObject city = new JSONObject(jsonString);
    }
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        String[] messageSplit = message.split(" ");

    }
}
