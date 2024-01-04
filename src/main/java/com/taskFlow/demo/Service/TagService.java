package com.taskFlow.demo.Service;

import com.taskFlow.demo.Model.DTOs.TagDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TagService {
    List<TagDTO> getAllTags();

    TagDTO getTagById(Long id);

    TagDTO createTag(TagDTO tagDTO);

    TagDTO updateTag(Long id, TagDTO tagDTO);

    void deleteTag(Long id);
}
