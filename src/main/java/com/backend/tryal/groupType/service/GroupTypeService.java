package com.backend.tryal.groupType.service;

import com.backend.tryal.groupType.GroupType;

import java.util.List;

public interface GroupTypeService {
    List<GroupType> getAllGroupTypes();
    GroupType getGroupTypeById(Long groupTypeId);
    GroupType createGroupType(GroupType groupType);
    GroupType updateGroupTypeById(Long groupTypeId, GroupType groupType);
    boolean deleteGroupTypeById(Long groupTypeId);
}
