package com.testing;
import java.io.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;


public class SlashCom extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("help")) {
            event.reply("Usage : weather [city(required)] [options], the options for now are:\n"
                    + "detail : gets detail of the weather\n"
                    + "daily : gets daily weather forecast\n"
                    + "hourly : gets hourly weather forecase\n").queue();
        }
        if (event.getName().equals("addcity")) {
            String linklocation = "C:\\Users\\user\\IdeaProjects\\pd2-final\\src\\main\\resources\\city.json";
            OptionMapping city = event.getOption("city");
            OptionMapping link = event.getOption("link");
            String linkText = link.getAsString();
            String cityName = city.getAsString();
            if (!linkText.contains("https://weather.com/")){
                System.out.println("the link is not from weather.com");
                return;
            }
            for (char letter : cityName.toCharArray()) {
                boolean isletter = false;
                if ((letter > 'a' && letter <= 'z') || (letter > 'A' && letter <= 'Z')) {
                    isletter = true;
                }
                if (!isletter) {
                    System.out.println("city name is invalid");
                    return;
                }
            }
            /*try (BufferedReader br = new BufferedReader(new FileReader(linklocation))) { //handle later

            }*/
        }
        /*if (event.getName().equals("rainrate")) {
            OptionMapping cityName = event.getOption("cityname");
            String city = cityName.getAsString();
            OptionMapping hourly = event.getOption("hourly");
            if (hourly != null); {

            }
            //event.reply("hello").queue();
        }*/


    }
}
