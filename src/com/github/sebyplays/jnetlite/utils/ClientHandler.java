package com.github.sebyplays.jnetlite.utils;

import com.github.sebyplays.jnetlite.utils.net.Channel;

import java.util.ArrayList;
import java.util.UUID;

public class ClientHandler {

    public static ArrayList<Channel> channels = new ArrayList<>();

    //get a channel by its name
    public static Channel getChannel(String channelName){
        for (Channel channel : channels){
            if(channel.getChannelName().equals(channelName)){
                return channel;
            }
        }
        return null;
    }
    //get a channel by its UUID
    public static Channel getChannel(UUID uuid){
        for (Channel channel : channels){
            if(channel.getUuid().equals(uuid)){
                return channel;
            }
        }
        return null;
    }

    public static boolean exists(String channelName){
        return getChannel(channelName) != null;
    }

    public static boolean exists(UUID uuid){
        return getChannel(uuid) != null;
    }

}
