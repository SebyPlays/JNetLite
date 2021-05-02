package com.github.sebyplays.jnetlite.events.server;

import com.github.sebyplays.jevent.Event;
import com.github.sebyplays.jnetlite.utils.io.Packet;
import com.github.sebyplays.jnetlite.utils.net.Channel;
import lombok.Getter;

public class ServerPacketReceivedEvent extends Event {

    @Getter private Packet packet;
    @Getter private Channel channel;

    public ServerPacketReceivedEvent(Channel channel, Packet packet){
        this.packet = packet;
        this.channel = channel;
    }

    public ServerPacketReceivedEvent(){

    }
}
