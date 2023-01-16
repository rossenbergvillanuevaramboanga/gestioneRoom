package it.prova.gestioneroom.service;

import it.prova.gestioneroom.model.Room;

import java.util.List;

public interface RoomService {

    public List<Room> listAllRoom();

    public Room caricaSingoloRoom(Long id);

    public Room aggiorna(Room roomInstance);

    public Room inserisciNuovo(Room roomInstance);

    public void rimuovi(Long idToRemove);
}
