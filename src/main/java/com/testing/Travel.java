package com.testing;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Travel extends Listeners {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        String[] messageSplit = message.split(" ");
        if(messageSplit[0].equalsIgnoreCase("travel")) {
            event.getChannel().sendMessage("Where would you like to travel?\nLet me tell you the weather over there ,please enter airport code").queue();
            String jsonCityString = Main.readConfigFile("Travelcity.json");
            JSONObject jsonCity = new JSONObject(jsonCityString);
            messageSplit[1] = messageSplit[1].toUpperCase();
            EmbedBuilder embed = new EmbedBuilder();
            embed.setImage(getFlags(messageSplit[1]));
            event.getChannel().sendMessage("").setEmbeds(embed.build()).queue();
            GetCurrentWeather(event, messageSplit[1],jsonCity);
        }
    }
    public String getFlags(String country) {
        String flagUrl = "";
        String jsonFlagsString = Main.readConfigFile("Flags.json");
        JSONObject jsonFlags = new JSONObject(jsonFlagsString);
        flagUrl = jsonFlags.getString(country);
        return flagUrl;
    }
}
