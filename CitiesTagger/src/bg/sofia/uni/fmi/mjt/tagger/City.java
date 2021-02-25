package bg.sofia.uni.fmi.mjt.tagger;

public class City {
    private String name;
    private String country;
    private Integer countOfTags;

    City(String name, String country) {
        this.name = name;
        this.country = country;
        this.countOfTags = 0;
    }

    public String getName() {
        return this.name;
    }

    public String getCountry() {
        return this.country;
    }

    public Integer getCountOfTags() {
        return this.countOfTags;
    }

    public void addTag() {
        this.countOfTags += 1;
    }
}
