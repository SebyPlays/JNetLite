package com.github.sebyplays.jnetlite.events.client;

import com.github.sebyplays.jevent.Event;
import com.github.sebyplays.jnetlite.utils.net.Channel;
import lombok.Getter;

public class ClientDisconnectedEvent extends Event {

    @Getter private Channel channel;

    public ClientDisconnectedEvent(Channel channel){
        this.channel = channel;
        super.setEventName(this.getClass().getName());
    }

    public ClientDisconnectedEvent(){

    }

}
