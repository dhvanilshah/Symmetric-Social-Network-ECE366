package cool.disc.server.data;

public class Track {
    private final String name;
    private final Artist artist;

    public String getName() {
        return name;
    }

    public Artist getArtist() {
        return artist;
    }

    public Track(String name, Artist artist) {
        this.name = name;
        this.artist = artist;
    }
}
