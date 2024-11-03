package project.entities;

public class Hashtag {

    private Long id;
    private String title;

    public Hashtag() {
    }

    public Hashtag(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Hashtag{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
