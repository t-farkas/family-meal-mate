package com.farkas.familymealmate.init;

import com.farkas.familymealmate.model.entity.TagEntity;
import com.farkas.familymealmate.repository.TagRepository;
import com.farkas.familymealmate.util.CsvReaderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Slf4j
public class TagDataLoader implements ApplicationRunner {

    private final TagRepository tagRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (tagRepository.count() == 0) {
            loadTags();
        }
    }

    private void loadTags() {
        List<TagEntity> tags = CsvReaderUtil.readCsvFile("/data/tags.csv", ",", getRowMapper());
        tagRepository.saveAll(tags);
        log.info("Loaded " + tags.size() + " tags");
    }

    private Function<String[], TagEntity> getRowMapper() {
        return row -> new TagEntity(row[0].trim());
    }

}
