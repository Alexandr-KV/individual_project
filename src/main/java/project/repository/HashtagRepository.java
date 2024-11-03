package project.repository;

import project.entities.Hashtag;
import project.entities.Note;
import project.entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class HashtagRepository {

    private final Connection connection;
    private final Statement statement;
    private final NoteRepository noteRepository;

    public HashtagRepository(Connection connection, Statement statement, NoteRepository noteRepository) {
        this.connection = connection;
        this.statement = statement;
        this.noteRepository = noteRepository;
    }

    public Long writeNewHashtag(String title, User user) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("insert into hashtag (title, user_id) values (?, ?);", RETURN_GENERATED_KEYS);
        ps.setString(1, title);
        ps.setLong(2, user.getId());
        ps.executeUpdate();
        ResultSet generatedKeys = ps.getGeneratedKeys();
        return (long) generatedKeys.getInt(1);
    }

    public void hashtagLinkToNote(Long hashtagId, Long noteId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("insert into hashtag_note_link (id_hashtag, id_note) values (?, ?);");
        ps.setLong(1, hashtagId);
        ps.setLong(2, noteId);
        ps.executeUpdate();
    }

    public void hashtagUnlinkingToNote(Long hashtagId, Long noteId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("DELETE FROM hashtag_note_link WHERE id_hashtag = (?) AND id_note = (?)");
        ps.setLong(1, hashtagId);
        ps.setLong(2, noteId);
        ps.executeUpdate();
    }

    public List<Note> getAllNotesByHashtag(Long hashtagId, User user) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM hashtag_note_link WHERE id_hashtag = (?)");
        ps.setLong(1, hashtagId);
        var resSet = ps.executeQuery();
        List<Note> notes = new ArrayList<>();
        while (resSet.next()) {
            Long id_note = resSet.getLong("id_note");
            notes.add(noteRepository.readNoteById(id_note, user));
        }
        return notes;
    }

    public List<Hashtag> getAllHashtags(User user) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM hashtag WHERE user_id = (?)");
        ps.setLong(1, user.getId());
        var resSet = ps.executeQuery();
        List<Hashtag> hashtags = new ArrayList<>();
        while (resSet.next()) {
            Long id = resSet.getLong("id");
            String title = resSet.getString("title");
            hashtags.add(new Hashtag(id, title));
        }
        return hashtags;
    }

    public void deleteHashtag(Long hashtagId, User user) throws SQLException {
        unlinkAllNotes(hashtagId);
        PreparedStatement ps = connection.prepareStatement("DELETE FROM hashtag WHERE id = (?) AND user_id = (?)");
        ps.setLong(1, hashtagId);
        ps.setLong(2, user.getId());
        ps.executeUpdate();
    }

    private void unlinkAllNotes(Long hashtagId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("DELETE FROM hashtag_note_link WHERE id_hashtag = (?)");
        ps.setLong(1, hashtagId);
        ps.executeUpdate();
    }
}
