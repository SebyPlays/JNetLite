package com.github.sebyplays.jnetlite.utils.net.client;

import com.github.sebyplays.hashhandler.api.HashHandler;
import com.github.sebyplays.hashhandler.api.HashType;
import com.github.sebyplays.jevent.api.JEvent;
import com.github.sebyplays.jnetlite.events.client.ClientPacketPreSentEvent;
import com.github.sebyplays.jnetlite.events.client.ClientPacketReceivedEvent;
import com.github.sebyplays.jnetlite.utils.Port;
import com.github.sebyplays.jnetlite.utils.io.Packet;
import com.github.sebyplays.jnetlite.utils.io.Packets;
import com.github.sebyplays.logmanager.api.LogManager;
import com.github.sebyplays.logmanager.api.LogType;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class JNetClient {


    private String hostname;
    private Port port;
    @Getter private Socket socket;
    private Thread thread;
    @Getter private boolean authenticated = false;
    @Getter private String auth;
    @Getter private ObjectOutputStream objectOutputStream;
    @Getter private ObjectInputStream objectInputStream;

    @SneakyThrows
    public JNetClient(String hostname, Port port){
        this.hostname = hostname;
        this.port = port;
        LogManager.getLogManager("JNetClient").log(LogType.NORMAL, "Client initialized", true, false, true, true);
    }

    @SneakyThrows
    public JNetClient(String hostname, Port port, String auth){
        this.auth = new HashHandler(HashType.SHA_1).hash(auth, false);
        this.hostname = hostname;
        this.port = port;
        LogManager.getLogManager("JNetClient").log(LogType.NORMAL, "Client initialized", true, false, true, true);
    }

    @SneakyThrows
    public void connect(){
        thread = new Thread("Client"){
            @SneakyThrows
            @Override
            public void run() {
                socket = new Socket(hostname, port.getPort());
                objectInputStream = new ObjectInputStream(socket.getInputStream());
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                LogManager.getLogManager("JNetClient").log(LogType.NORMAL, "Connection engaged, waiting for input!", true, false, true, true);
                Packet packet;
                LogManager.getLogManager("JNetClient").log(LogType.NORMAL, "Awaiting authentication..", true, false, true ,true);
                if(auth != null && !auth.equals(null) && !authenticated){
                    if(sendPacket(new Packet("auth", auth)).getAdditional().equals(auth)){
                        authenticated = true;
                        LogManager.getLogManager("JNetClient").log(LogType.NORMAL, "Authenticated!", true, false, true ,true);
                    } else {
                        LogManager.getLogManager("JNetClient").log(LogType.NORMAL, "Authentication not successful, closing!", true, false, true ,true);
                        socket.close();
                    }
                }
                while (!socket.isClosed() && (packet = read()) != null){
                    if (Packets.SERVER_CLOSE.getPacket().getAdditional().equals(packet.getAdditional())) { break; }
                    new JEvent(new ClientPacketReceivedEvent(packet)).callEvent();
                }
                terminate();
            }
        };
        thread.start();
        TimeUnit.SECONDS.sleep(4);
    }

    @SneakyThrows
    public void terminate(){
        if(!this.socket.isClosed()){
            sendPacketNoCallback(Packets.CLIENT_CLOSE.getPacket());
            this.socket.close();
        }
        this.objectOutputStream.close();
        this.objectInputStream.close();
        LogManager.getLogManager("JNetClient").log(LogType.NORMAL, "Connection disengaged!", true, false, true, true);
    }

    @SneakyThrows
    public Packet sendPacket(Packet packet){
        if(!socket.isClosed()){
            if(!new JEvent(new ClientPacketPreSentEvent(packet, true)).callEvent().getEvent().isCancelled()){
                this.objectOutputStream.writeObject(packet);
                this.objectOutputStream.flush();
                return read();
            }
        }
        LogManager.getLogManager("JNetServer").log(LogType.ERROR, "Cannot send information, if not connected!", true, false, true, true);
        return Packets.ERROR.getPacket();
    }

    public void sendRawData(byte[] bytes){

    }

    @SneakyThrows
    public void sendPacketNoCallback(Packet packet){
        if(!socket.isClosed()){
            if(!new JEvent(new ClientPacketPreSentEvent(packet, false)).callEvent().getEvent().isCancelled()){
                this.objectOutputStream.writeObject(packet);
                this.objectOutputStream.flush();
                return;
            }
        }
        LogManager.getLogManager("JNetServer").log(LogType.ERROR, "Cannot send information, if not connected!", true, false, true, true);
        return;
    }

    @SneakyThrows
    public Packet read(){
        if(!socket.isClosed()){
            return (Packet) this.objectInputStream.readObject();
        }
        LogManager.getLogManager("JNetServer").log(LogType.ERROR, "Cannot read information, if not connected!", true, false, true, true);
        return Packets.ERROR.getPacket();
    }


}
