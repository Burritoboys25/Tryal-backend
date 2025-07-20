package com.backend.tryal.timeslot;

import com.backend.tryal.timeslot.dto.TimeslotRequestDTO;

import com.backend.tryal.timeslot.response.TimeslotResponse;
import com.backend.tryal.timeslot.dto.TimeslotDTO;
import com.backend.tryal.timeslot.mapper.TimeslotMapper;
import com.backend.tryal.timeslot.service.TimeslotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/timeslots")
public class TimeslotController {
    @Autowired
    TimeslotService timeslotService;

    // get all timeslots
    @GetMapping()
    public ResponseEntity<List<TimeslotDTO>> getAllTimeslots() {
        try {
            List<TimeslotDTO> timeslots = timeslotService.getAllTimeslots()
                    .stream()
                    .map(TimeslotMapper::mapTimeslotDto)
                    .collect(Collectors.toList());

            if (timeslots.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(timeslots, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // get timeslot by ID
    @GetMapping("/{timeslotId}")
    public ResponseEntity<TimeslotResponse> getTimeslotById(@PathVariable UUID timeslotId) {
        try {
            Timeslot timeslot = timeslotService.getTimeslotById(timeslotId);

            if (timeslot == null) {
                return new ResponseEntity<>(new TimeslotResponse(null, "Experience timeslot not found."),HttpStatus.NOT_FOUND);
            }

            TimeslotDTO timeslotDTO = TimeslotMapper.mapTimeslotDto(timeslot);

            return new ResponseEntity<>(new TimeslotResponse(timeslotDTO, "Experience timeslot found."), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // create timeslot
    @PostMapping()
    public ResponseEntity<TimeslotResponse> createTimeslot(@RequestBody TimeslotRequestDTO timeslotRequestDTO, @RequestParam UUID experienceId) {
        try {
            TimeslotDTO timeslotDTO = TimeslotMapper.mapTimeslotDto(timeslotService.createTimeslot(experienceId, timeslotRequestDTO));

            if(timeslotDTO == null){
                return new ResponseEntity<>(new TimeslotResponse(null, "Experience with id: " + experienceId +  " not found."), HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(new TimeslotResponse(timeslotDTO, "Timeslot created successfully."), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // patch timeslot
    @PatchMapping("/{timeslotId}")
    public ResponseEntity<TimeslotResponse> updateTimeslotById(@RequestBody TimeslotRequestDTO timeslotRequestDTO, @PathVariable UUID timeslotId) {
        try {
            Timeslot updatedTimeslot = timeslotService.updateTimeslotById(timeslotId, timeslotRequestDTO);

            if(updatedTimeslot == null){
                return new ResponseEntity<>(new TimeslotResponse(null, "Timeslot not found."), HttpStatus.NOT_FOUND);
            }

            TimeslotDTO timeslotDTO = TimeslotMapper.mapTimeslotDto(updatedTimeslot);

            return new ResponseEntity<>(new TimeslotResponse(timeslotDTO, "Timeslot updated successfully."), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // delete timeslot
    @DeleteMapping("/{timeslotId}")
    public ResponseEntity<String> deleteTimeslotById(@PathVariable UUID timeslotId) {
        try {

            if (timeslotService.deleteTimeslotById(timeslotId)) {
                return new ResponseEntity<>("Timeslot deleted successfully.", HttpStatus.OK);
            }

            return new ResponseEntity<>("Timeslot not found.", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
