package com.coweconomy.domain.word.dto;

import com.coweconomy.domain.word.entity.EconomyWord;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@DynamicInsert
public class ArticleWordDto {
    private Long articleId;
    private EconomyWord economyWord;

    public ArticleWordDto(Long articleId, EconomyWord word) {
        this.articleId = articleId;
        this.economyWord = word;
    }
}
