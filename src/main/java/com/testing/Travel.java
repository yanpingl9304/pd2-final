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



        if(messageSplit[0].equalsIgnoreCase("travel") && messageSplit.length == 1) {
            event.getChannel().sendMessage("Where would you like to travel?\n" +
                    "Let me tell you the weather over there ,please enter airport code\n" +
                    "travel [Airport Code]").queue();
        } else if (messageSplit[0].equalsIgnoreCase("travel") && messageSplit.length == 2) {
            JSONObject jsonCity = getJSONFile(messageSplit[0]);
            messageSplit[1] = messageSplit[1].toUpperCase();
            EmbedBuilder embed = getFlagsAndTime(messageSplit[1],jsonCity);
            event.getChannel().sendMessage("").setEmbeds(embed.build()).queue();
            GetCurrentWeather(event, messageSplit[1],jsonCity);
        } else if (messageSplit[0].equalsIgnoreCase("travel") && messageSplit.length == 3) {
            JSONObject jsonCity = getJSONFile(messageSplit[0]);
            switch (messageSplit[2]) {
                case "daily" :
                    getDailyForecast(event, messageSplit[1],jsonCity);
                    break;
                default:
                    break;
            }
        }

    }
    public EmbedBuilder getFlagsAndTime(String airportCode , JSONObject jsonCity) {
        EmbedBuilder embed = new EmbedBuilder();
        try {
            String url =  jsonCity.getString(airportCode);
            Document doc = Jsoup.connect(url).get();
            Elements elements = doc.select("span.CurrentConditions--timestamp--1ybTk");
            String time = elements.text().replace("As of ","");

            String flagUrl = "";
            String jsonFlagsString = Main.readConfigFile("Flags.json");
            JSONObject jsonFlags = new JSONObject(jsonFlagsString);
            flagUrl = jsonFlags.getString(airportCode);

            embed.setTitle("Local time "+time);
            embed.setImage(flagUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return embed;
    }
}
