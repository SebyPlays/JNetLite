package com.github.sebyplays.jnetlite.events.server;

import com.github.sebyplays.jevent.Event;
import com.github.sebyplays.jnetlite.utils.io.Packet;
import lombok.Getter;

public class ServerPacketPreSentEvent extends Event {

    @Getter private Packet packet;
    @Getter boolean expectedCallback;

    public ServerPacketPreSentEvent(){

    }

    public ServerPacketPreSentEvent(Packet packet, boolean expectedCallback){
        this.packet = packet;
        this.expectedCallback = expectedCallback;
    }

}
