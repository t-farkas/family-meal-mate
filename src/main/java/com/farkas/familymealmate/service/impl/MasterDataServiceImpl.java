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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MasterDataServiceImpl implements MasterDataService {

    private final TagRepository tagRepository;
    private final IngredientRepository ingredientRepository;
    private final MasterDataMapper mapper;

    @Override
    public List<TagDto> getTags() {
        List<TagEntity> tags = tagRepository.findAll();
        return mapper.toTagDtoList(tags);
    }

    @Override
    public List<IngredientDto> getIngredients() {
        List<IngredientEntity> ingredients = ingredientRepository.findAll();
        return mapper.toIngredientDtoList(ingredients);
    }
}
