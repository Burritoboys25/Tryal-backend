package com.backend.tryal.groupType;

import com.backend.tryal.groupType.service.GroupTypeService;
import com.backend.tryal.shared.response.ApiResponse;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/groupTypes")
public class GroupTypeController {

  private final GroupTypeService groupTypeService;

  public GroupTypeController(GroupTypeService groupTypeService) {
    this.groupTypeService = groupTypeService;
  }

  // Get all categories
  @GetMapping()
  public List<GroupType> getAllGroupTypes() {
    return groupTypeService.getAllGroupTypes();
  }

  // Get Category by id
  @GetMapping("/{groupTypeId}")
  public GroupType getGroupTypeById(@PathVariable UUID groupTypeId) {
    return groupTypeService.getGroupTypeById(groupTypeId);
  }

  @PostMapping
  public GroupType createGroupType(@RequestBody GroupType newGroupType) {
    return groupTypeService.createGroupType(newGroupType);
  }

  @PatchMapping("/{groupTypeId}")
  public GroupType updateGroupType(
      @PathVariable UUID groupTypeId,
      @RequestBody GroupType updatedGroupType
  ) {
    return groupTypeService.updateGroupTypeById(groupTypeId, updatedGroupType);
  }

  @DeleteMapping("/{groupTypeId}")
  public ApiResponse<String> deleteGroupTypeById(@PathVariable UUID groupTypeId) {
    groupTypeService.deleteGroupTypeById(groupTypeId);
    return new ApiResponse<>("Group type deleted successfully");
  }


}
