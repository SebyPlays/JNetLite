package com.github.sebyplays.jnetlite.events.client;

import com.github.sebyplays.jevent.Event;
import com.github.sebyplays.jnetlite.utils.io.Packet;
import lombok.Getter;

public class ClientPacketPreSentEvent extends Event {

    @Getter private Packet packet;
    @Getter private boolean expectedCallback;

    public ClientPacketPreSentEvent(){

    }

    public ClientPacketPreSentEvent(Packet packet, boolean expectedCallback){
        this.packet = packet;
        this.expectedCallback = expectedCallback;
    }

}
