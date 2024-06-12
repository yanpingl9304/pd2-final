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
            String linklocation = "C:\\Users\\yanpi\\Desktop\\DiscordBot\\src\\main\\resources\\city.json";
            OptionMapping city = event.getOption("city");
            OptionMapping link = event.getOption("link");
            String cityCopy = ("C:\\Users\\yanpi\\Desktop\\DiscordBot\\src\\main\\resources\\tempCity.json");
            String linkText = link.getAsString();
            String cityName = city.getAsString();
            if (!linkText.contains("https://weather.com/")){
                event.reply("the link is not from weather.com");
                return;
            }
            for (char letter : cityName.toCharArray()) {
                boolean validName = true;
                if (Character.isLetter(letter)) {
                    validName = false;
                }
                if (validName) {
                    event.reply("city name is invalid");
                    return;
                }
            }
            int linecount = 0, tempLineCount = 0;
            String secondtoLastLine = "";
            StringBuilder tempCity = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(cityCopy))) { //handle later
                String line;
                while ((line = br.readLine()) != null) {
                    linecount++;
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try (BufferedReader br = new BufferedReader(new FileReader(cityCopy))) { //handle later
                String line;
                while ((line = br.readLine()) != null) {
                    tempLineCount++;
                    if (tempLineCount >= linecount - 1) {
                        secondtoLastLine = line;
                        break;
                    } else {
                        tempCity.append(line + "\n");
                    }
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            StringBuilder sb = new StringBuilder();
            sb.append(tempCity)
                    .append(secondtoLastLine)
                    .append(",\n")
                    .append("  " + "\"" + cityName + "\"" + ": " + "\"" + linkText + "\"" + "\n" + "}");
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(linklocation))) {
                bw.write(sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(cityCopy))) {
                bw.write(sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            event.reply("City " + cityName + " added");
        }
    }
}
