package com.github.sebyplays.jnetlite.events.client;

import com.github.sebyplays.jevent.Event;
import com.github.sebyplays.jnetlite.utils.io.Packet;
import lombok.Getter;

public class ClientPacketReceivedEvent extends Event {

    @Getter private Packet packet;

    public ClientPacketReceivedEvent(Packet packet){
        this.packet = packet;
    }

    public ClientPacketReceivedEvent(){

    }
}
