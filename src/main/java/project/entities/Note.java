package project.entities;

public class Note {
    private Long id;
    private String title;
    private String message;

    public Note(Long id, String title, String message) {
        this.id = id;
        this.title = title;
        this.message = message;
    }


    public Note() {
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
