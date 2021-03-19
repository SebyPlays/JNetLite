package com.github.sebyplays.jnetlite.utils;

import lombok.Getter;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.Socket;

public class Port {
    @Getter
    private int port;

    @SneakyThrows
    public Port(int port, boolean bypassAvailability) throws PortAlreadyOccupied {
        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("Choose a valid port between the port-range '0-65535'");
        }
        if(!isAvailable() && !bypassAvailability){
            throw new PortAlreadyOccupied("Port " + this.port + " is already being used by another instance!");
        }
        this.port = port;
    }

    public boolean isAvailable(){
        try { Socket socket = new Socket("localhost", this.port);
            return false;
        } catch (IOException e){
            return true;
        }
    }
}
