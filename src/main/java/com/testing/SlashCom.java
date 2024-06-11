package com.testing;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class SlashCom extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("help")) return;
        /*if (event.getName().equals("rainrate")) {
            OptionMapping cityName = event.getOption("cityname");
            String city = cityName.getAsString();
            OptionMapping hourly = event.getOption("hourly");
            if (hourly != null); {

            }
            //event.reply("hello").queue();
        }*/

            event.reply("Usage : weather [city(required)] [options], the options for now are:\n"
            + "detail : gets detail of the weather\n"
            + "daily : gets daily weather forecast\n"
            + "hourly : gets hourly weather forecase\n").queue();

    }
}
