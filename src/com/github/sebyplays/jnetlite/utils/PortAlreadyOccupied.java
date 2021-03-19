package com.github.sebyplays.jnetlite.utils;

public class PortAlreadyOccupied extends Exception {

    public PortAlreadyOccupied(){
        super();
    }

    public PortAlreadyOccupied(String message){
        super(message);
    }

}
