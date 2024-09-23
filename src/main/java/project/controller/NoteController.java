package project.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import project.repository.NoteRepository;
import project.entities.Note;
import project.entities.User;
import project.request.NotePatchRequest;
import project.request.NotePostRequest;
import project.response.NoteResponse;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NoteController {
    private final NoteRepository noteRepository;

    public NoteController(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public void getAllNotes(Context ctx) throws SQLException, JsonProcessingException {
        User user = ctx.attribute("user");
        assert user != null;
        List<Note> notes = noteRepository.readAllNotes(user);
        var responseArray = new ArrayList<NoteResponse>();
        for (var note : notes) {
            responseArray.add(new NoteResponse(note));
        }
        ctx.result(new ObjectMapper().writeValueAsString(responseArray));
    }

    public void getNoteById(Context ctx) throws SQLException, JsonProcessingException {
        Long id = Long.parseLong(ctx.pathParam("id"));
        User user = ctx.attribute("user");
        assert user != null;
        NoteResponse noteResponse = new NoteResponse(noteRepository.readNoteById(id, user));
        ctx.result(new ObjectMapper().writeValueAsString(noteResponse));
    }

    public void postNote(Context ctx) throws SQLException, JsonProcessingException {
        NotePostRequest notePostRequest = ctx.bodyAsClass(NotePostRequest.class);
        notePostRequest.valid();
        User user = ctx.attribute("user");
        assert user != null;
        Long id = noteRepository.writeNote(notePostRequest.getTitle(), notePostRequest.getMessage(),user);
        ctx.result(new ObjectMapper().writeValueAsString(id));
    }

    public void patchNote(Context ctx) throws SQLException {
        NotePatchRequest notePatchRequest = ctx.bodyAsClass(NotePatchRequest.class);
        notePatchRequest.valid();
        Long id = Long.parseLong(ctx.pathParam("id"));
        User user = ctx.attribute("user");
        noteRepository.patchNoteById(id, notePatchRequest.getTitle(), notePatchRequest.getMessage(), user);
    }

    public void deleteNote(Context ctx) throws SQLException {
        Long id = Long.parseLong(ctx.pathParam("id"));
        User user = ctx.attribute("user");
        assert user != null;
        noteRepository.deleteNoteById(id, user);
    }
}
