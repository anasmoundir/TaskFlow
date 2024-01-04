package com.taskFlow.demo.Service.ServiceImplementation;

import com.taskFlow.demo.Exceptions.TagNotFoundException;
import com.taskFlow.demo.Model.DTOs.TagDTO;
import com.taskFlow.demo.Model.Entities.Tag;
import com.taskFlow.demo.Repository.TagRepo;
import com.taskFlow.demo.Service.TagService;
import com.taskFlow.demo.mapper.TagMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TagServiceImplementation implements TagService {

    @Autowired
    private TagRepo tagRepository;

    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<TagDTO> getAllTags() {
        List<Tag> tags = tagRepository.findAll();
        return tags.stream().map(tagMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public TagDTO getTagById(Long id) {
        Tag tag = tagRepository.findById(id).orElseThrow(() -> new TagNotFoundException("Tag not found"));
        return tagMapper.toDto(tag);
    }

    @Override
    public TagDTO createTag(TagDTO tagDTO) {
        Tag tag = tagMapper.toEntity(tagDTO);
        tag = tagRepository.save(tag);
        return tagMapper.toDto(tag);
    }

    @Override
    public TagDTO updateTag(Long id, TagDTO tagDTO) {
        Tag existingTag = tagRepository.findById(id).orElseThrow(() -> new TagNotFoundException("Tag not found"));

        tagMapper.updateTagFromDto(tagDTO, existingTag);
        existingTag = tagRepository.save(existingTag);
        return tagMapper.toDto(existingTag);
    }

    @Override
    public void deleteTag(Long id) {
        Tag tag = tagRepository.findById(id).orElseThrow(() -> new TagNotFoundException("Tag not found"));
        tagRepository.delete(tag);
    }
}
