package com.backend.tryal.timeslot;

import com.backend.tryal.shared.response.ApiResponse;
import com.backend.tryal.timeslot.dto.TimeslotDTO;
import com.backend.tryal.timeslot.dto.TimeslotRequestDTO;
import com.backend.tryal.timeslot.mapper.TimeslotMapper;
import com.backend.tryal.timeslot.service.TimeslotService;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/timeslots")
public class TimeslotController {

  @Autowired
  TimeslotService timeslotService;

  // get all timeslots
  @GetMapping()
  public List<TimeslotDTO> getAllTimeslots() {
    return timeslotService.getAllTimeslots()
        .stream()
        .map(TimeslotMapper::mapTimeslotDto)
        .collect(Collectors.toList());
  }

  // get timeslot by ID
  @GetMapping("/{timeslotId}")
  public TimeslotDTO getTimeslotById(@PathVariable UUID timeslotId) {
    Timeslot timeslot = timeslotService.getTimeslotById(timeslotId);
    return TimeslotMapper.mapTimeslotDto(timeslot);
  }

  // create timeslot
  @PostMapping()
  public TimeslotDTO createTimeslot(@RequestBody TimeslotRequestDTO timeslotRequestDTO) {
    Timeslot timeslot = timeslotService.createTimeslot(timeslotRequestDTO);
    return TimeslotMapper.mapTimeslotDto(timeslotService.createTimeslot(timeslotRequestDTO));
  }

  // patch timeslot
  @PatchMapping("/{timeslotId}")
  public TimeslotDTO updateTimeslotById(@RequestBody TimeslotRequestDTO timeslotRequestDTO,
      @PathVariable UUID timeslotId) {
    Timeslot updatedTimeslot = timeslotService.updateTimeslotById(timeslotId, timeslotRequestDTO);
    return TimeslotMapper.mapTimeslotDto(updatedTimeslot);
  }

  // delete timeslot
  @DeleteMapping("/{timeslotId}")
  public ApiResponse<String> deleteTimeslotById(@PathVariable UUID timeslotId) {
    timeslotService.deleteTimeslotById(timeslotId);
    return new ApiResponse<>("Timeslot deleted successfully.");
  }
}
