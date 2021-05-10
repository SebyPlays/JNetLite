package com.github.sebyplays.jnetlite.events.server;

import com.github.sebyplays.jevent.Event;
import com.github.sebyplays.jnetlite.utils.net.Channel;
import lombok.Getter;

public class ClientConnectedEvent extends Event {

    @Getter private Channel channel;

    public ClientConnectedEvent(){

    }

    public ClientConnectedEvent(Channel channel){
        this.channel = channel;
    }

}
