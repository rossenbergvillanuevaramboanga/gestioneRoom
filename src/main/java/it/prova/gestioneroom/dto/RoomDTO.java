package it.prova.gestioneroom.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.prova.gestioneroom.model.Room;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomDTO {

    private Long id;

    @NotBlank(message = "{name.notblank}")
    private String name;

    private Long idReservation;

    public Room buildRoomModel(){
        Room result = Room.builder().id(id).name(name).idReservation(idReservation).build();
        return result;
    }

    public static RoomDTO buildRoomDTOFromModel(Room room){
        RoomDTO result = RoomDTO.builder().id(room.getId()).name(room.getName()).idReservation(room.getIdReservation()).build();
        return result;
    }

    public static List<RoomDTO> createListRoomDTOFromModelList(List<Room> roomList){
        return roomList.stream().map(room -> {
            return RoomDTO.buildRoomDTOFromModel(room);
        }).collect(Collectors.toList());
    }


}
