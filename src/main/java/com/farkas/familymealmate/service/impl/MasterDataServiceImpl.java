package com.farkas.familymealmate.service.impl;

import com.farkas.familymealmate.mapper.MasterDataMapper;
import com.farkas.familymealmate.model.dto.masterdata.IngredientDto;
import com.farkas.familymealmate.model.dto.masterdata.TagDto;
import com.farkas.familymealmate.model.entity.IngredientEntity;
import com.farkas.familymealmate.model.entity.TagEntity;
import com.farkas.familymealmate.repository.IngredientRepository;
import com.farkas.familymealmate.repository.TagRepository;
import com.farkas.familymealmate.service.MasterDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MasterDataServiceImpl implements MasterDataService {

    private final TagRepository tagRepository;
    private final IngredientRepository ingredientRepository;
    private final MasterDataMapper mapper;

    @Override
    @Cacheable(value = "tags")
    public Map<Long, TagDto> getTags() {
        log.info("Loading tags from DB");
        return tagRepository.findAll()
                .stream()
                .collect(Collectors.toMap(TagEntity::getId, mapper::toTagDto));
    }

    @Override
    @Cacheable(value = "ingredients")
    public Map<Long, IngredientDto> getIngredients() {
        log.info("Loading ingredients from DB");
        return ingredientRepository.findAll().stream()
                .collect(Collectors.toMap(IngredientEntity::getId, mapper::toIngredientDto));
    }
}
