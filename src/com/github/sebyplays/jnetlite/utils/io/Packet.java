package com.github.sebyplays.jnetlite.utils.io;

import com.github.sebyplays.jnetlite.utils.net.Channel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

public class Packet implements Serializable {


    @Getter private Object packetObject;
    @Getter private String additional;
    //@Getter byte[] bytes;
    //Planing
    //0 = Source; 1 = Destination; 2 = bytes; 3 = Checksum;
    //@Getter private String[] header;

    public Packet(Object object, String additional){
        this.additional = additional;
        this.packetObject = object;
    }

}
