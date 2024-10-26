package com.hunmin.domain.dto.word;


import com.hunmin.domain.entity.Word;
import com.hunmin.domain.entity.WordScore;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WordScoreRequestDTO {
    private Long wordScoreId;
    private Long memberId; // 로그인한 사용자 ID
    private String testLang; // 선택한 언어
    private String testLevel; // 선택한 레벨
    private int testScore;
    private Double testRankScore;

    private int correctCount;


    public WordScore toEntity() {
        return WordScore.builder()
                .wordScoreId(wordScoreId)
                .testLang(testLang)
                .testLevel(testLevel)
                .testScore(testScore)
                .testRankScore(testRankScore)
                .build();
    }
}

