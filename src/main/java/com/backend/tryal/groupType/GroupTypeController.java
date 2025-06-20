package com.backend.tryal.groupType;

import com.backend.tryal.groupType.service.GroupTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/groupTypes")
public class GroupTypeController {
    private final GroupTypeService groupTypeService;

    public GroupTypeController(GroupTypeService groupTypeService) {
        this.groupTypeService = groupTypeService;
    }

    // Get all categories
    @GetMapping()
    public ResponseEntity<List<GroupType>> getAllGroupTypes() {
        try {
            List<GroupType> groupTypes = new ArrayList<>(groupTypeService.getAllGroupTypes());
            if (groupTypes.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(groupTypes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get Category by id
    @GetMapping("/{groupTypeId}")
    public ResponseEntity<GroupType> getGroupTypeById(@PathVariable Long groupTypeId) {
        try {
            GroupType groupType = groupTypeService.getGroupTypeById(groupTypeId);

            if (groupType == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(groupType, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<GroupType> createGroupType(@RequestBody GroupType newGroupType) {
        try {
            GroupType createdGroupType = groupTypeService.createGroupType(newGroupType);
            return new ResponseEntity<>(createdGroupType, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{groupTypeId}")
    public ResponseEntity<GroupType> updateGroupType(
            @PathVariable Long groupTypeId,
            @RequestBody GroupType updatedGroupType
    ) {
        try {
            GroupType groupType = groupTypeService.updateGroupTypeById(groupTypeId, updatedGroupType);
            if (groupType == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(groupType, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/{groupTypeId}")
    public ResponseEntity<String> deleteGroupTypeById(@PathVariable Long groupTypeId) {
        try {
            boolean deleted = groupTypeService.deleteGroupTypeById(groupTypeId);
            if (!deleted) {
                return new ResponseEntity<>("Group type not found.",HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>("Group type deleted successfully",HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
