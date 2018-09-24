package lam.study.sample.dto;

/**
 * @description: PersonDto
 * @author: linanmiao
 * @date: 2018/9/24 20:13
 * @version: 1.0
 */
public class PersonDto {

    private Integer key;

    private String value;

    private Long ages;

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getAges() {
        return ages;
    }

    public void setAges(Long ages) {
        this.ages = ages;
    }
}
