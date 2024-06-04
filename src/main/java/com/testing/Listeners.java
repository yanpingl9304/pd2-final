package com.testing;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class Listeners extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        if(message.equalsIgnoreCase("how's the weather")) {
            event.getChannel().sendMessage("SUNNYYY"+event.getAuthor().getName()).queue();
        }
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {

    }

}
