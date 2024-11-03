package project.response;

import project.entities.Hashtag;

public class HashtagResponse {
    private Long id;
    private String title;

    public HashtagResponse(Hashtag hashtag) {
        this.id = hashtag.getId();
        this.title = hashtag.getTitle();
    }

    public HashtagResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
