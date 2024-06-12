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
            String linklocation = "C:\\Users\\user\\IdeaProjects\\pd2-final\\src\\main\\resources\\tempCity.json";
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
            int linecount = 0, tempLineCount = 0;
            String secondtoLastLine = "";
            try (BufferedReader br = new BufferedReader(new FileReader(linklocation))) { //handle later
                String line;
                while ((line = br.readLine()) != null) {
                    linecount++;
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try (BufferedReader br = new BufferedReader(new FileReader(linklocation))) { //handle later
                String line;
                while ((line = br.readLine()) != null) {
                    tempLineCount++;
                    if (tempLineCount == linecount - 1) {
                        secondtoLastLine = line;
                    }
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            StringBuilder sb = new StringBuilder();
            sb.append(secondtoLastLine)
                    .append(",\n")
                    .append("  " + "\"" + cityName + "\"" + ": " + "\"" + linkText + "\"" + "\n" + "}");
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(linklocation,true))) {
                bw.write(sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
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
