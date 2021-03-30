package com.github.sebyplays.jnetlite.events.client;

import com.github.sebyplays.jevent.Event;
import com.github.sebyplays.jnetlite.utils.net.Channel;
import lombok.Getter;

public class ClientConnectedEvent extends Event {

    @Getter private Channel channel;

    public ClientConnectedEvent(){

    }

    public ClientConnectedEvent(Channel channel){
        this.channel = channel;
        super.setEventName("ClientConnectedEvent");
    }

}
