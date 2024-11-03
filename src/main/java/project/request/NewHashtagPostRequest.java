package project.request;

import project.exception.ValidationException;

public class NewHashtagPostRequest {
    private String title;

    public NewHashtagPostRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void valid(){
        if (this.title == null) {
            throw new ValidationException("Отсутствует title");
        }
    }
}
