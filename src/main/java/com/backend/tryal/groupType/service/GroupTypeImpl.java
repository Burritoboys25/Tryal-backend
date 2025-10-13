package com.backend.tryal.groupType.service;

import com.backend.tryal.groupType.GroupType;
import com.backend.tryal.groupType.GroupTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class GroupTypeImpl implements GroupTypeService {

  private final GroupTypeRepository groupTypeRepository;

  public GroupTypeImpl(GroupTypeRepository groupTypeRepository) {
    this.groupTypeRepository = groupTypeRepository;
  }

  @Override
  public List<GroupType> getAllGroupTypes() {
    return groupTypeRepository.findAll();
  }

  @Override
  public GroupType getGroupTypeById(UUID groupTypeId) {
    GroupType groupType = groupTypeRepository.findById(groupTypeId).orElse(null);
    if (groupType == null) {
      throw new EntityNotFoundException("Group Type not found with id: " + groupTypeId);
    }
    return groupType;
  }

  @Override
  public GroupType createGroupType(GroupType groupType) {
    groupTypeRepository.save(groupType);
    return groupType;
  }

  @Override
  public GroupType updateGroupTypeById(UUID groupTypeId, GroupType groupType) {
    GroupType updatedGroupType = getGroupTypeById(groupTypeId);
    if (updatedGroupType == null) {
      throw new EntityNotFoundException("Group Type not found with id: " + groupTypeId);
    }
    if (groupType.getName() != null) {
      updatedGroupType.setName(groupType.getName());
    }
    if (groupType.getDescription() != null) {
      updatedGroupType.setDescription(updatedGroupType.getDescription());
    }
    groupTypeRepository.save(updatedGroupType);
    return updatedGroupType;
  }

  @Override
  public void deleteGroupTypeById(UUID groupTypeId) {
    if (getGroupTypeById(groupTypeId) == null) {
      throw new EntityNotFoundException("Group Type not found with id: " + groupTypeId);
    }
    groupTypeRepository.deleteById(groupTypeId);
  }
}
