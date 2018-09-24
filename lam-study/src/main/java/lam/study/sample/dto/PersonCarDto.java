package lam.study.sample.dto;

/**
 * @description: PersonCarDto
 * @author: linanmiao
 * @date: 2018/9/24 21:06
 * @version: 1.0
 */
public class PersonCarDto {

    private String make;
    private int seatCount;
    private String type;

    private Integer id;
    private String name;
    private Long age;

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }
}
