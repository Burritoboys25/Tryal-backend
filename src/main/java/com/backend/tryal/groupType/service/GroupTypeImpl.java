package com.backend.tryal.groupType.service;

import com.backend.tryal.groupType.GroupType;
import com.backend.tryal.groupType.GroupTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GroupTypeImpl implements GroupTypeService{
    private final GroupTypeRepository groupTypeRepository;

    public GroupTypeImpl(GroupTypeRepository groupTypeRepository) {this.groupTypeRepository = groupTypeRepository;}

    @Override
    public List<GroupType> getAllGroupTypes() {return groupTypeRepository.findAll();}

    @Override
    public GroupType getGroupTypeById(UUID groupTypeId) {
        return groupTypeRepository.findById(groupTypeId).orElse(null);
    }

    @Override
    public GroupType createGroupType(GroupType groupType) {
        groupTypeRepository.save(groupType);
        return groupType;
    }

    @Override
    public GroupType updateGroupTypeById(UUID groupTypeId, GroupType groupType) {
        if (getGroupTypeById(groupTypeId) != null) {
            GroupType updatedGroupType = getGroupTypeById(groupTypeId);

            if (groupType.getName() != null) {
                updatedGroupType.setName(groupType.getName());
            }
            if (groupType.getDescription() != null) {
                updatedGroupType.setDescription(updatedGroupType.getDescription());
            }
            groupTypeRepository.save(updatedGroupType);
            return updatedGroupType;
        }
        return null;
    }

    @Override
    public boolean deleteGroupTypeById(UUID groupTypeId) {
        if (getGroupTypeById(groupTypeId) != null) {
            groupTypeRepository.deleteById(groupTypeId);
            return true;
        }
        return false;
    }
}
