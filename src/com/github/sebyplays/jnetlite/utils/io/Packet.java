package com.github.sebyplays.jnetlite.utils.io;

import com.github.sebyplays.jnetlite.utils.net.Channel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

public class Packet implements Serializable {


    @Getter private Object packetObject;
    @Getter private String additional;

    public Packet(Object object, String additional){
        this.additional = additional;
        this.packetObject = object;
    }

}
