package com.testing;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Listeners extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        String[] messageSplit = message.split(" ");
        if(messageSplit[0].equalsIgnoreCase("weather")) {
            if(messageSplit.length == 1) {
                event.getChannel().sendMessage("Usage : weather [city]").queue();
            } else {
                GetCurrentWeather(event,messageSplit[1]);
            }
        }
    }
    public void GetCurrentWeather(@NotNull MessageReceivedEvent event ,String city){

        String jsonCityString = Main.readConfigFile("city.json");
        JSONObject jsonCity = new JSONObject(jsonCityString);
        String url = jsonCity.getString(city);

        String jsonIconString = Main.readConfigFile("weatherIcon.json");
        JSONObject jsonIcon = new JSONObject(jsonIconString);

        MessageChannel channel = event.getChannel();
        String temperature = "";
        String weather = "";
        String current = "";

        try {
            // connect to weather.com
            Document doc = Jsoup.connect(url).get();

            Elements elements = doc.select(".CurrentConditions--primary--2DOqs");

            for (Element element : elements) {

                Element temperatureElement = element.selectFirst(".CurrentConditions--tempValue--MHmYY");
                temperature = temperatureElement.text().trim();

                Element weatherElement = element.selectFirst(".CurrentConditions--phraseValue--mZC_p");
                weather = weatherElement.text().trim();

                Element currentCondition = element.selectFirst(".CurrentConditions--tempHiLoValue--3T1DG");
                current = currentCondition.text().trim();
            }

            // output
            EmbedBuilder embed = new EmbedBuilder();
            embed.setImage(SelectIcon(weather,jsonIcon));
            embed.setTitle("Current Weather Information");
            embed.setDescription("Temperature : " + FtoC(temperature.substring(0,2))+"\n"
                                 +"Weather : " + weather+"\n"
                                 +"Current : " + "Day "+FtoC(current.substring(4,6))+" Night " + FtoC(current.substring(16, 18)));
            channel.sendMessage("").setEmbeds(embed.build()).queue();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String FtoC(String temperature) {
        double temperatureF = Double.parseDouble(temperature);
        double temperatureC = (temperatureF - 32) / 9 * 5;
        return Integer.toString((int) Math.round(temperatureC)).concat("°");
    }

    public String SelectIcon(String weather , JSONObject jsonIcon) {
        String iconUrl = "";
        if (weather.contains("Sunny")) {
            iconUrl = jsonIcon.getString("Sunny");
        } else if (weather.contains("Cloudy")) {
            iconUrl = jsonIcon.getString("Cloudy");
        }  else if (weather.contains("Rain")) {
            iconUrl = jsonIcon.getString("Rain");
        }
        return iconUrl;
    }

    @Override
    public void onReady(ReadyEvent event) {
        event.getJDA().getGuildById(1247376009616031754L);

    }
}
