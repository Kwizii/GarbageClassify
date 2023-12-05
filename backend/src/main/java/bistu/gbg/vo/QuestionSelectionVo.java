package bistu.gbg.vo;

import bistu.gbg.entity.QuestionCategory;
import bistu.gbg.entity.QuestionLevel;
import bistu.gbg.entity.QuestionType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class QuestionSelectionVo {
    @JsonProperty("types")
    private List<QuestionType> questionTypeList;

    @JsonProperty("categories")
    private List<QuestionCategory> questionCategoryList;

    @JsonProperty("levels")
    private List<QuestionLevel> questionLevelList;
}
