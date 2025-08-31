package com.backend.tryal.groupType.service;

import com.backend.tryal.groupType.GroupType;

import java.util.List;
import java.util.UUID;

public interface GroupTypeService {
    List<GroupType> getAllGroupTypes();
    GroupType getGroupTypeById(UUID groupTypeId);
    GroupType createGroupType(GroupType groupType);
    GroupType updateGroupTypeById(UUID groupTypeId, GroupType groupType);
    void deleteGroupTypeById(UUID groupTypeId);
}
