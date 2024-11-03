package project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import project.entities.Hashtag;
import project.entities.Note;
import project.entities.User;
import project.repository.HashtagRepository;
import project.request.HashtagLinkRequest;
import project.request.NewHashtagPostRequest;
import project.response.HashtagResponse;
import project.response.NoteResponse;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HashtagController {
    private final HashtagRepository hashtagRepository;

    public HashtagController(HashtagRepository hashtagRepository) {
        this.hashtagRepository = hashtagRepository;
    }

    public void getAllHashtags(Context ctx) throws SQLException, JsonProcessingException {
        User user = ctx.attribute("user");
        assert user != null;
        List<Hashtag> hashtags = hashtagRepository.getAllHashtags(user);
        var responseArray = new ArrayList<HashtagResponse>();
        for (var hashtag : hashtags) {
            responseArray.add(new HashtagResponse(hashtag));
        }
        ctx.result(new ObjectMapper().writeValueAsString(responseArray));
    }

    public void getNotesByHashtag(Context ctx) throws SQLException, JsonProcessingException {
        Long id_hashtag = Long.parseLong(ctx.pathParam("id"));
        User user = ctx.attribute("user");
        assert user != null;
        List<Note> notes = hashtagRepository.getAllNotesByHashtag(id_hashtag, user);
        var responseArray = new ArrayList<NoteResponse>();
        for (var note : notes) {
            responseArray.add(new NoteResponse(note));
        }
        ctx.result(new ObjectMapper().writeValueAsString(responseArray));
    }

    public void postHashtag(Context ctx) throws SQLException, JsonProcessingException {
        NewHashtagPostRequest hashtagPostRequest = ctx.bodyAsClass(NewHashtagPostRequest.class);
        hashtagPostRequest.valid();
        User user = ctx.attribute("user");
        assert user != null;
        Long id = hashtagRepository.writeNewHashtag(hashtagPostRequest.getTitle(), user);
        ctx.result(new ObjectMapper().writeValueAsString(id));
    }

    public void hashtagLinkToNote(Context ctx) throws SQLException {
        HashtagLinkRequest hashtagLinkRequest = ctx.bodyAsClass(HashtagLinkRequest.class);
        hashtagLinkRequest.valid();
        User user = ctx.attribute("user");
        assert user != null;
        hashtagRepository.hashtagLinkToNote(hashtagLinkRequest.getHashtagId(), hashtagLinkRequest.getNoteId());
    }

    public void hashtagUnlinkingToNote(Context ctx) throws SQLException {
        HashtagLinkRequest hashtagLinkRequest = ctx.bodyAsClass(HashtagLinkRequest.class);
        hashtagLinkRequest.valid();
        User user = ctx.attribute("user");
        assert user != null;
        hashtagRepository.hashtagUnlinkingToNote(hashtagLinkRequest.getHashtagId(), hashtagLinkRequest.getNoteId());
    }

    public void deleteHashtag(Context ctx) throws SQLException {
        Long id_hashtag = Long.parseLong(ctx.pathParam("id"));
        User user = ctx.attribute("user");
        assert user != null;
        hashtagRepository.deleteHashtag(id_hashtag, user);
    }

}
