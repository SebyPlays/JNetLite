package com.github.sebyplays.jnetlite.utils.net.server;

import com.github.sebyplays.hashhandler.api.HashHandler;
import com.github.sebyplays.hashhandler.api.HashType;
import com.github.sebyplays.jevent.api.JEvent;
import com.github.sebyplays.jnetlite.events.server.ClientConnectedEvent;
import com.github.sebyplays.jnetlite.utils.ClientHandler;
import com.github.sebyplays.jnetlite.utils.Port;
import com.github.sebyplays.jnetlite.utils.io.Packet;
import com.github.sebyplays.jnetlite.utils.io.Packets;
import com.github.sebyplays.jnetlite.utils.net.Channel;
import com.github.sebyplays.logmanager.api.LogManager;
import com.github.sebyplays.logmanager.api.LogType;
import lombok.Getter;
import lombok.SneakyThrows;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

public class JNetServer {

    @Getter private Port port;
    @Getter private ServerSocket serverSocket = null;
    @Getter private Socket socket = null;
    @Getter private String auth;

    @SneakyThrows
    public JNetServer(Port port){
        this.port = port;
        LogManager.getLogManager("JNetServer").log(LogType.NORMAL, "Socket bound to port: " + port.getPort(), true, false, true, true);
    }

    @SneakyThrows
    public JNetServer(String auth, Port port){
        this.port = port;
        LogManager.getLogManager("JNetServer").log(LogType.NORMAL, "Socket bound to port: " + port.getPort(), true, false, true, true);
        this.auth = new HashHandler(HashType.SHA_1).hash(auth, false);
        LogManager.getLogManager("JNetServer").log(LogType.NORMAL, "Auth set to : " + getAuth(), true, false, true, true);
    }

    @SneakyThrows
    public void start(){
        new Thread("Server"){
            @SneakyThrows
            @Override
            public void run() {
                serverSocket = new ServerSocket(port.getPort());
                LogManager.getLogManager("JNetServer").log(LogType.NORMAL, "Listening for connections..", true, false, true, true);
                while(!serverSocket.isClosed()){
                    UUID uuid = UUID.randomUUID();
                    int id = 0;
                    ClientHandler.channels.add(new Channel(serverSocket.accept(), uuid, id++, auth));
                    LogManager.getLogManager("JNetServer").log(LogType.INFORMATION, "Client: ch-" + uuid + " connected from: " + ClientHandler.getChannel(uuid).getSocket().getRemoteSocketAddress() + "!", true, false, true, true);
                    LogManager.getLogManager("JNetServer").log(LogType.INFORMATION, "Client: ch-" + uuid + " assigned a channel.", true, false, true, true);
                    ClientHandler.getChannel(uuid).start();
                    LogManager.getLogManager("JNetServer").log(LogType.INFORMATION, "Client: ch-" + uuid + " Channel Thread started!", true, false, true, true);
                    LogManager.getLogManager("JNetServer").log(LogType.INFORMATION, "Client: ch-" + uuid + " Channel ready for communication!", true, false, true, true);
                    if(new JEvent(new ClientConnectedEvent(ClientHandler.getChannel(uuid))).callEvent().getEvent().isCancelled()){
                       ClientHandler.getChannel(uuid).sendPacketNoCallback(Packets.ERROR_CONNECTION_CANCELLED.getPacket());
                       ClientHandler.getChannel(uuid).disconnect();
                    }
                }
            }
        }.start();
    }


    @SneakyThrows
    public synchronized void stop(){
        broadcast(Packets.SERVER_CLOSE.getPacket());
        for(Channel channel : ClientHandler.channels){
            channel.disconnect();
        }
        serverSocket.close();
    }

    public void broadcast(Packet packet){
        ClientHandler.channels.forEach(channel -> channel.sendPacketNoCallback(packet));
    }


}
