package com.github.sebyplays.jnetlite.utils.io;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Packets {
    SERVER_CLOSE(new Packet("SC01", "SERVER_CLOSE_PACKET")),
    CLIENT_CLOSE(new Packet("CC01", "CLIENT_CLOSE_PACKET")),
    ERROR(new Packet("NE01", "DIVERS_ERROR_PACKET"))
    ;

    @NonNull @Getter Packet packet;
}
