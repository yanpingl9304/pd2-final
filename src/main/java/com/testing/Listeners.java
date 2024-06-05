package com.testing;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Listeners extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        if(message.equalsIgnoreCase("how's the weather")) {
            GetCurrentWeather(event);
        }
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {

    }

    public void GetCurrentWeather(@NotNull MessageReceivedEvent event){
        String url = "https://weather.com/weather/today/l/5f9da4381a390189d917ae1caed305047455b2a6496f40b3c96f4d4fd46d20d1"; // LA
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
            event.getChannel().sendMessage("Temperature: " + FtoC(temperature.substring(0,2))).queue();
            event.getChannel().sendMessage("Weather: " + weather).queue();
            event.getChannel().sendMessage("Current: " + "Day "+FtoC(current.substring(4,6))+" • Night " + FtoC(current.substring(16, 18))).queue();
            // System.out.println("Temperature: " + FtoC(temperature.substring(0,2)));
            // System.out.println("Weather: " + weather);
            // System.out.println("Current: " + "Day "+FtoC(current.substring(4,6))+" • Night " + FtoC(current.substring(16, 18)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String FtoC(String temperature) {
        double temperatureF = Double.parseDouble(temperature);
        double temperatureC = (temperatureF - 32) / 9 * 5;
        return Integer.toString((int) Math.round(temperatureC)).concat("°");
    }

}
