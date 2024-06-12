package com.testing;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Listeners extends ListenerAdapter {
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        Guild guild = event.getJDA().getGuildById(1247097625216548935L);
        /*guild.upsertCommand("rainrate","gets the rain rate of the city")
                .addOptions(new OptionData(OptionType.STRING,"cityname","the city name",true)
                ,new OptionData(OptionType.STRING,"hourly","Y if you want hourly forecast",false)
                ).queue();*/
        guild.upsertCommand("help","for command options").queue();
        guild.upsertCommand("addcity","adding the city to the list")
                .addOptions(new OptionData(OptionType.STRING,"city","the city name",true),
                new OptionData(OptionType.STRING,"link","the link of the city(must be a weather.com link)",true))
                .queue();

    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        String[] messageSplit = message.split(" ");
        if(messageSplit[0].equalsIgnoreCase("weather")) {
            if(messageSplit.length == 1) {
                event.getChannel().sendMessage("Usage : weather [city] [function]").queue();
            } else if(messageSplit.length == 2) {
                JSONObject jsonCity = getJSONFile(messageSplit[0]);
                GetCurrentWeather(event,messageSplit[1],jsonCity);
            } else if(messageSplit.length == 3) {
                String cityName = messageSplit[1];
                JSONObject jsonCity = new JSONObject();
                switch(messageSplit[2]){
                    case "detail" :
                        jsonCity = getJSONFile(messageSplit[0]);
                        getDetailWeather(event,cityName,jsonCity);
                        break;
                    case "daily" :
                        jsonCity = getJSONFile(messageSplit[0]);
                        getDailyForecast(event,cityName,jsonCity);
                        break;
                    case "hourly" :
                        jsonCity = getJSONFile(messageSplit[0]);
                        getHourlyForecast(event,cityName,jsonCity);
                        break;
                    default :
                        event.getChannel().sendMessage("Usage : weather [city] [function] , Current we have functions : \n" +
                                                       "detail : getDetailWeather\n" +
                                                       "daily : getDailyWeather\n" +
                                                       "hourly : getHourlyWeather").queue();
                        break;
                }
            }
        }
    }
    public JSONObject getJSONFile(String input) {
        JSONObject jsonCity = new JSONObject();
        if(input.equalsIgnoreCase("weather")) {
            String jsonCityString = Main.readConfigFile("city.json");
            jsonCity = new JSONObject(jsonCityString);

        } else if (input.equalsIgnoreCase("travel")) {
            String jsonTravelCityString = Main.readConfigFile("Travelcity.json");
            jsonCity = new JSONObject(jsonTravelCityString);

        }
        return jsonCity;
    }
    public void GetCurrentWeather(@NotNull MessageReceivedEvent event ,String city ,JSONObject jsonCity){

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

            Elements curElements = doc.select(".CurrentConditions--primary--2DOqs");

            for (Element element : curElements) {

                Element temperatureElement = element.selectFirst(".CurrentConditions--tempValue--MHmYY");
                temperature = temperatureElement.text().trim();
                Element weatherElement = element.selectFirst(".CurrentConditions--phraseValue--mZC_p");
                weather = weatherElement.text().trim();

                Element currentCondition = element.selectFirst(".CurrentConditions--tempHiLoValue--3T1DG");
                current = currentCondition.text().trim();
            }
            // 找出 "Day 104° • Night 88°" 104 , 88用
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(current);

            ArrayList<String> numbersList = new ArrayList<>();

            while (matcher.find()) {
                numbersList.add(matcher.group());
            }
            // output
            EmbedBuilder embed = new EmbedBuilder();
            embed.setImage(SelectIcon(weather,jsonIcon));
            embed.setTitle("Current Weather Information in "+getLocation(city ,jsonCity));
            embed.setDescription("Temperature : " + FtoC(temperature.replaceAll("\u2022",""))+"\n"
                                 +"Weather : " + weather+"\n"
                                 +"Current : " + "Day "+FtoC(numbersList.get(0))+ "  \u2022 Night " + FtoC(numbersList.get(1) + "\n\n"
                                 ));
            channel.sendMessage("").setEmbeds(embed.build()).queue();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void getDetailWeather(MessageReceivedEvent event ,String city,JSONObject jsonCity){
        String url = jsonCity.getString(city);
        MessageChannel channel = event.getChannel();

        String temperature = "";     //0
        String wind = "";            //1
        String Humidity = "";        //2
        String UV = "";              //5

        try {
            // connect to weather.com
            Document doc = Jsoup.connect(url).get();
            List<String> detailWeather = new ArrayList<>();
            Elements curElements = doc.select(".CurrentConditions--primary--2DOqs");

            Elements elements = doc.select("div.WeatherDetailsListItem--wxData--kK35q");
            for(Element element : elements) {
                detailWeather.add(element.text());
            }
            temperature = detailWeather.get(0);
            wind = detailWeather.get(1).replace("Wind Direction", "");
            BigDecimal WindSpeedInKph = new BigDecimal(Double.parseDouble(wind.split(" ")[0]) * 1.6).setScale(2, RoundingMode.HALF_UP);
            Humidity = detailWeather.get(2);
            UV = detailWeather.get(5);


            // output
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("Current Weather Detail Information in "+getLocation(city ,jsonCity));
            embed.setDescription("Temperature High / Low : " + FtoC(temperature.substring(0,2))+" / "+FtoC(temperature.substring(3,5)) + "\n"
                                    +"Wind : " + WindSpeedInKph +" KM/H \n"
                                    +"Humidity : " + Humidity + " , "+HumidityLevel(Humidity) + "\n"
                                    +"UV : " + UV + " , "+UVLevel(UV));

            channel.sendMessage("").setEmbeds(embed.build()).queue();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void getHourlyForecast (MessageReceivedEvent event ,String city,JSONObject jsonCity) {
        String url = jsonCity.getString(city);
        MessageChannel channel = event.getChannel();
        try {
            Document doc = Jsoup.connect(url).get();
            List<Elements> hourlyElements = new ArrayList<>();
            hourlyElements.add(doc.select("div.HourlyWeatherCard--TableWrapper--1OobO")
                    .select(".Ellipsis--ellipsis--3ADai"));//time
            hourlyElements.add(doc.select("div.HourlyWeatherCard--TableWrapper--1OobO")
                    .select(".Column--temp--1sO_J.Column--verticalStack--28b4K"));//temperature
            hourlyElements.add(doc.select("div.HourlyWeatherCard--TableWrapper--1OobO")
                    .select(".Column--weatherIcon--2w_Rf.Icon--icon--2aW0V.Icon--fullTheme--3Fc-5"));//condition
            /*hourlyElements.add(doc.select("div.HourlyWeatherCard--TableWrapper--1OobO")
                    .select(".Icon--icon--2aW0V.Icon--fullTheme--3Fc-5"));//weather*///
            hourlyElements.add(doc.select("div.HourlyWeatherCard--TableWrapper--1OobO")
                    .select("div.Column--precip--3JCDO"));//rain chance
//            System.out.println(doc.select("div.HourlyWeatherCard--TableWrapper--1OobO").text());
            String hourlyarray[] = doc.select("div.HourlyWeatherCard--TableWrapper--1OobO").text().trim().split("%");
//            System.out.println(hourlyarray.length);
            //String[] hourlytime = splitTime(hourlyElements.get(0).text().trim());
//            for (int i = 0; i < hourlyarray.length; i++) {
//                System.out.println(hourlyarray[i]);
//            }
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("Hourly Weather Forecast of "+getLocation(city ,jsonCity));
            StringBuilder description = new StringBuilder();
            for(String hourly : hourlyarray) {
                description.append(extractWeatherInfo(hourly));
                description.append("\n\n");
            }
            embed.setDescription(description.toString());
            channel.sendMessage("").setEmbeds(embed.build()).queue();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*for (String eachcondition : hourlyconditionarray) {
            System.out.println(eachcondition);
            eachcondition.replace("_"," ");
        }
        String[] hourlyrainrate = hourlyElements.get(3).text().replaceAll("[^0-9]"," ").trim().replaceAll("\\s+"," ").split(" ");*/

    }
    public void getDailyForecast(MessageReceivedEvent event ,String city,JSONObject jsonCity){
        String url = jsonCity.getString(city);
        MessageChannel channel = event.getChannel();

        try {
            // connect to weather.com
            Document doc = Jsoup.connect(url).get();
            Elements elements = doc.select(".DailyWeatherCard--TableWrapper--2bB37");
            String sentence = "";
            for(Element element : elements) {
                sentence = element.text();
            }
            String[] weatherInformation = sentence.split("%");
            // output
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("Daily Weather Forecast of "+getLocation(city ,jsonCity));
            StringBuilder description = new StringBuilder();
            for(String daily : weatherInformation) {
                description.append(extractWeatherInfo(daily));
                description.append("\n");
            }
            embed.setDescription(description.toString());
            channel.sendMessage("").setEmbeds(embed.build()).queue();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String FtoC(String temperature) {
        if(temperature.contains("/")) {
            String[] temperatureArray = temperature.replaceAll("\u00B0","").split("/");
            StringBuilder returnString = new StringBuilder();
            for(int i = 0; i < temperatureArray.length; i++){
                if(temperatureArray[i].equals("--")) {
                    returnString.append("--");
                } else {
                    double temperatureF = Double.parseDouble(temperatureArray[i]);
                    double temperatureC = (temperatureF - 32) / 9 * 5;
                    returnString.append(Integer.toString((int) Math.round(temperatureC)).concat("°"));
                }
                if(i != temperatureArray.length - 1) returnString.append("/");
            }
            return returnString.toString();
        }
        if(temperature.contains("\u00B0")) temperature = temperature.replace("\u00B0","");
        if(temperature.contains("--")) return ("--");
        double temperatureF = Double.parseDouble(temperature);
        double temperatureC = (temperatureF - 32) / 9 * 5;

        return Integer.toString((int) Math.round(temperatureC)).concat("\u00B0");

    }

    public String SelectIcon(String weather , JSONObject jsonIcon) {
        String iconUrl = "";
        if (weather.contains("Sunny")) {
            iconUrl = jsonIcon.getString("Sunny");
        } else if (weather.contains("Cloudy")) {
            iconUrl = jsonIcon.getString("Cloudy");
        }  else if (weather.contains("Rain")) {
            iconUrl = jsonIcon.getString("Rain");
        } else {
            iconUrl = null;
        }
        return iconUrl;
    }

    public static String[] splitTime(String input) {
        String regex = "Now|\\d+\\s(am|pm)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        ArrayList<String> resultList = new ArrayList<>();

        while (matcher.find()) {
            String target = matcher.group();
            if(matcher.group().length() != 5) {
                for(int i = matcher.group().length(); i < 5 ; i++)  target = target.concat(" ");
            }
            resultList.add(target);
        }

        return resultList.toArray(new String[0]);
    }
    public String HumidityLevel(String Humidity){
        int humidityInt = Integer.parseInt(Humidity.replace("%",""));
        String level = "";
        if(humidityInt < 30) {
            level = "TOO DRY";
        } else if (30<=humidityInt && humidityInt < 60) {
            level = "COMFORTABLE";
        } else if (60<=humidityInt && humidityInt < 100) {
            level = "TOO HIGH";
        } else if (humidityInt == 100) {
            level = "MAYBE RAINY NOW";
        }
        return level;
    }

    public String UVLevel(String UV){
        String[] temp = UV.split(" ");
        int UVLevel = Integer.parseInt(temp[0]);
        String level = "";
        if(0<=UVLevel && UVLevel <=2) {
            level = "LOW";
        } else if (3<=UVLevel && UVLevel <=5) {
            level = "MODERATE";
        } else if (6<=UVLevel && UVLevel <=7) {
            level = "HIGH";
        } else if (8<=UVLevel && UVLevel <=10) {
            level = "VERY HIGH";
        } else if (11<=UVLevel) {
            level = "EXTREME";
        }
        return level;
    }

    public String extractWeatherInfo(String input) {
        String[] array = input.replace("Rain Chance of Rain", "").split(" ");
        String day;
        String temp;
        String rainChance = array[array.length - 1] + "%";
        StringBuilder weatherBuilder = new StringBuilder();
        int startIndex;

        if (array[0].equalsIgnoreCase("today") || array[0].equalsIgnoreCase("Now")) {
            day = array[0];
            temp = array[1];
            if(array[0].equalsIgnoreCase("today")) startIndex = 3;
            else startIndex = 2;
        } else {
            day = array[1].trim() + " " + array[2];
            temp = array[3];
            if(!array[2].equalsIgnoreCase("am") && !array[2].equalsIgnoreCase("pm")) startIndex = 5;
            else startIndex = 4;
        }

        while ((startIndex < array.length) && !array[startIndex].matches("\\d+")) {
            weatherBuilder.append(array[startIndex]).append(" ");
            startIndex++;
        }

        String weather = weatherBuilder.toString().trim();

        return  day + "\n" +
                weather + "\n" +
                FtoC(temp) + "\n" +
                "Chance of Rain " + rainChance +
                "\n==========================";
    }

    public String getLocation(String airportCode , JSONObject jsonCity) {
        String location = "";
        try {
            String url =  jsonCity.getString(airportCode);
            Document doc = Jsoup.connect(url).get();
            Elements elements = doc.select("h1.CurrentConditions--location--1YWj_");
            location = elements.text();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }
}
