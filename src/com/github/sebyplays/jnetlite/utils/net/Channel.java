package com.github.sebyplays.jnetlite.utils.net;

import com.github.sebyplays.jevent.api.JEvent;
import com.github.sebyplays.jnetlite.events.server.ClientDisconnectedEvent;
import com.github.sebyplays.jnetlite.events.server.ServerPacketPreSentEvent;
import com.github.sebyplays.jnetlite.events.server.ServerPacketReceivedEvent;
import com.github.sebyplays.jnetlite.utils.ClientHandler;
import com.github.sebyplays.jnetlite.utils.io.Packet;
import com.github.sebyplays.jnetlite.utils.io.Packets;
import com.github.sebyplays.logmanager.api.LogManager;
import com.github.sebyplays.logmanager.api.LogType;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.UUID;

public class Channel extends Thread implements Serializable {

    @Getter
    private UUID uuid;
    @Getter @Setter private String channelName;
    @Getter private boolean isAuthenticated = false;
    @Getter transient private Socket socket;
    @Getter transient private ObjectOutputStream objectOutputStream;
    @Getter transient private ObjectInputStream objectInputStream;
    @Getter private int clientId;
    @Getter private String auth;

    @SneakyThrows
    public Channel(Socket socket, UUID uuid, int id, String auth){
        this.uuid = uuid;
        this.clientId = id;
        this.auth = auth;
        this.channelName = "ch-" + getUuid();
        this.setName(channelName);
        this.socket = socket;
        this.socket.setKeepAlive(true);
        this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        LogManager.getLogManager("JNetServer").log(LogType.NORMAL, "Channel " + getName() + " OutputStream Initialized", true, false, true ,true);
        this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        LogManager.getLogManager("JNetServer").log(LogType.NORMAL, "Channel " + getName() + " InputStream Initialized", true, false, true ,true);
        LogManager.getLogManager("JNetServer").log(LogType.NORMAL, "Channel " + getName() + " initialized successfully", true, false, true ,true);
    }

    protected Channel() {

    }

    @SneakyThrows
    @Override
    public void run() {
        LogManager.getLogManager("JNetServer").log(LogType.NORMAL, "Channel " + getName() + " starting..", true, false, true ,true);
        Packet packet = null;
        LogManager.getLogManager("JNetServer").log(LogType.NORMAL, "Channel " + getName() + " awaiting authentication..", true, false, true ,true);
        if( getAuth() != null && !getAuth().equals(null) && !isAuthenticated){
            if(read().getAdditional().equals(getAuth())){
                isAuthenticated = true;
                sendPacketNoCallback(new Packet("auth", getAuth()));
                LogManager.getLogManager("JNetServer").log(LogType.NORMAL, "Authentication successful!", true, false, true ,true);
            } else {
                LogManager.getLogManager("JNetServer").log(LogType.NORMAL, "Authentication not successful! closing!", true, false, true ,true);
                socket.close();
            }
        }
        while(!socket.isClosed() && (packet = read()) != null){
            if(packet.getAdditional().equals("CLIENT_CLOSE_PACKET")){
                break;
            }
            new JEvent(new ServerPacketReceivedEvent(getChannelInstance(), packet)).callEvent();
        }
        disconnect();
    }

    public boolean isConnected(){
        return socket.isConnected();
    }

    @SneakyThrows
    public void disconnect(){
        if(!this.socket.isClosed()){
            sendPacketNoCallback(Packets.SERVER_CLOSE.getPacket());
            this.socket.close();
        }
        ClientHandler.channels.remove(this);
        new JEvent(new ClientDisconnectedEvent(this)).callEvent();
    }

    @SneakyThrows
    public Packet sendPacket(Packet packet){
        if(!socket.isClosed()){
            if(!new JEvent(new ServerPacketPreSentEvent(packet, true)).callEvent().getEvent().isCancelled()){
                this.objectOutputStream.writeObject(packet);
                this.objectOutputStream.flush();
                return read();
            }
        }
        LogManager.getLogManager("JNetServer").log(LogType.ERROR, "Cannot send information, if not connected!", true, false, true, true);
        return Packets.ERROR.getPacket();
    }

    @SneakyThrows
    public void sendPacketNoCallback(Packet packet){
        if(!socket.isClosed()){
            if(!new JEvent(new ServerPacketPreSentEvent(packet, false)).callEvent().getEvent().isCancelled()){
                try {
                    this.objectOutputStream.writeObject(packet);
                    this.objectOutputStream.flush();
                } catch (IOException e) {
                }
            }
            return;
        }
        LogManager.getLogManager("JNetServer").log(LogType.ERROR, "Cannot send information, if not connected!", true, false, true, true);
        return;
    }

    @SneakyThrows
    public Packet read(){
        if(!socket.isClosed()){
            return (Packet) this.objectInputStream.readObject();
        }
        LogManager.getLogManager("JNetServer").log(LogType.ERROR, "Cannot send information, if not connected!", true, false, true, true);
        return Packets.ERROR.getPacket();
    }

    public Channel getChannelInstance(){
        return this;
    }
}
