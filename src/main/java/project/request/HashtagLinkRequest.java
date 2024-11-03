package project.request;

import project.exception.ValidationException;

public class HashtagLinkRequest {
    private Long noteId;
    private Long hashtagId;

    public HashtagLinkRequest() {
    }

    public Long getNoteId() {
        return noteId;
    }

    public void setNoteId(Long noteId) {
        this.noteId = noteId;
    }

    public Long getHashtagId() {
        return hashtagId;
    }

    public void setHashtagId(Long hashtagId) {
        this.hashtagId = hashtagId;
    }

    public void valid(){
        if (this.hashtagId == null || this.noteId == null) {
            throw new ValidationException("Отсутствует userId и/или noteId");
        }
    }
}
