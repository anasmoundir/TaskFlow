package com.taskFlow.demo.mapper;

import com.taskFlow.demo.Model.DTOs.TagDTO;
import com.taskFlow.demo.Model.Entities.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TagMapper {
    TagDTO toDto(Tag tag);

    Tag toEntity(TagDTO tagDto);

    void updateTagFromDto(TagDTO tagDto, @MappingTarget Tag tag);
}
