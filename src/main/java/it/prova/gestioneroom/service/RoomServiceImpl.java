package it.prova.gestioneroom.service;

import it.prova.gestioneroom.model.Room;
import it.prova.gestioneroom.repository.room.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RoomServiceImpl implements RoomService{

    @Autowired
    private RoomRepository repository;

    @Override
    public List<Room> listAllRoom() {
        return (List<Room>) repository.findAll();
    }

    @Override
    public Room caricaSingoloRoom(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional
    public Room aggiorna(Room roomInstance) {

        Room roomReloaded = repository.findById(roomInstance.getId()).orElse(null);
        if(roomReloaded == null) throw new RuntimeException("Element not found");
        roomReloaded.setName(roomInstance.getName());
        roomReloaded.setIdReservation(roomInstance.getIdReservation());
        return repository.save(roomReloaded);
    }

    @Transactional
    public Room inserisciNuovo(Room clienteInstance) {
        return repository.save(clienteInstance);
    }

    @Transactional
    public void rimuovi(Long idToRemove) {
        Room roomReloaded = repository.findById(idToRemove).orElse(null);
        if(roomReloaded == null) throw new RuntimeException("Element not found");
        repository.deleteById(idToRemove);
    }
}
