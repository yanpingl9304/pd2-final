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
            String fileLocation = "src\\main\\resources\\City.json";//相對路徑
            OptionMapping city = event.getOption("city");
            OptionMapping link = event.getOption("link");
            String cityCopy = ("src\\main\\resources\\tempCity.json");//相對路徑
            String linkText = link.getAsString();
            String cityName = city.getAsString();
            if (!linkText.contains("https://weather.com/")){
                event.reply("The link is not from weather.com!").queue();
                return;
            }
            for (char letter : cityName.toCharArray()) {
                boolean validName = true;
                if (Character.isLetter(letter)) {
                    validName = false;
                }
                if (validName) {
                    event.reply("The city name is invalid!").queue();
                    return;
                }
            }
            StringBuilder tempCity = new StringBuilder();
            addRegion(tempCity,cityCopy,cityName,linkText,fileLocation);
            event.reply("City " + cityName + " added\n" +
                        "Please restart the bot after you add a new city").queue();
        }
        if (event.getName().equals("addtraveldest")) {
            String fileLocation = "\\src\\main\\resources\\TravelCity.json";
            OptionMapping airport = event.getOption("airport");
            OptionMapping link = event.getOption("link");
            String airportCopy = "src\\main\\resources\\tempTravelCity.json";
            String linkText = link.getAsString();
            String airportName = airport.getAsString();
            if (!linkText.contains("https://weather.com/")){
                event.reply("The link is not from weather.com!").queue();
                return;
            }
            for (char letter : airportName.toCharArray()) {
                boolean validName = true;
                if (Character.isLetter(letter)) {
                    validName = false;
                }
                if (validName) {
                    event.reply("The airport name is invalid!").queue();
                    return;
                }
            }
            StringBuilder tempAirport = new StringBuilder();
            addRegion(tempAirport,airportCopy,airportName,linkText,fileLocation);
            event.reply("Airport " + airportName + " added\n" +
                        "Please restart the bot after you add a new airport").queue();
        }

    }

    //adds region with the provided link and region name
    //tempRegion is for copying the original file until the second to last line
    public void addRegion (StringBuilder tempRegion,
                                  String regionCopy,
                                  String regionName,
                                  String regionLink,
                                  String regionFileLocation) {
        int linecount = 0, tempLineCount = 0;
        String secondtoLastLine = "";
        try (BufferedReader br = new BufferedReader(new FileReader(regionCopy))) {
            String line;
            while ((line = br.readLine()) != null) {
                linecount++;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader br = new BufferedReader(new FileReader(regionCopy))) {
            String line;
            while ((line = br.readLine()) != null) {
                tempLineCount++;
                if (tempLineCount >= linecount - 1) {
                    secondtoLastLine = line;
                    break;
                } else {
                    tempRegion.append(line + "\n");
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder regionAdder = new StringBuilder();
        regionAdder.append(tempRegion)
                .append(secondtoLastLine)
                .append(",\n")
                .append("  " + "\"" + regionName + "\"" + ": " + "\"" + regionLink + "\"" + "\n" + "}");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(regionFileLocation))) {
            bw.write(regionAdder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(regionCopy))) {
            bw.write(regionAdder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
