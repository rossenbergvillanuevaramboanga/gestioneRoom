package it.prova.gestioneroom.web.api;

import it.prova.gestioneroom.WebClientConfiguration;
import it.prova.gestioneroom.dto.DataDTO;
import it.prova.gestioneroom.dto.RoomDTO;
import it.prova.gestioneroom.model.Room;
import it.prova.gestioneroom.service.RoomService;
import it.prova.gestioneroom.web.api.exception.IdNotNullForInsertException;
import it.prova.gestioneroom.web.api.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private WebClient webClient;

    @GetMapping
    public List<RoomDTO> getAll() {
        return RoomDTO.createListRoomDTOFromModelList(roomService.listAllRoom());
    }

    @PostMapping
    public RoomDTO createNew(@Valid @RequestBody RoomDTO roomInput) {
        if (roomInput.getId() != null)
            throw new IdNotNullForInsertException("Non è ammesso fornire un id per la creazione");
        Room room = roomService.inserisciNuovo(roomInput.buildRoomModel());
        return RoomDTO.buildRoomDTOFromModel(room);
    }

    @GetMapping("/{id}")
    public RoomDTO findById(@PathVariable(value = "id", required = true) long id) {
        Room room = roomService.caricaSingoloRoom(id);
        if (room == null)
            throw new NotFoundException("Room not found con id: " + id);
        return RoomDTO.buildRoomDTOFromModel(room);
    }

    @PutMapping("/{id}")
    public RoomDTO update(@Valid @RequestBody RoomDTO roomInput, @PathVariable(required = true) Long id) {
        Room room = roomService.caricaSingoloRoom(id);

        if (room == null)
            throw new NotFoundException("Room not found con id: " + id);

        roomInput.setId(id);
        Room roomAggiornato = roomService.aggiorna(roomInput.buildRoomModel());
        return RoomDTO.buildRoomDTOFromModel(roomAggiornato);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable(value = "id", required = true) long id) {
        Room room = roomService.caricaSingoloRoom(id);
        if (room == null)
            throw new NotFoundException("Room not found con id: " + id);
        roomService.rimuovi(id);
    }


    @Value("${reservation.api.url}")
    private String reservationApiUrl;

    @PostMapping("/search")
    public List<RoomDTO> search(@Valid @RequestBody DataDTO dataInput){

        List<Long> idReservationList = webClient.post().uri(reservationApiUrl + "/search")
                .body(Mono.just(
                                new DataDTO(dataInput.getDatainizio(), dataInput.getDatafine())),
                        DataDTO.class)
                .retrieve().bodyToFlux(new ParameterizedTypeReference<Long>() {
                }).collectList().block();


        return RoomDTO.createListRoomDTOFromModelList(roomService.listAllRoom().stream().filter(
                room ->  idReservationList.contains(room.getId())
        ).collect(Collectors.toList()));

    }



}
