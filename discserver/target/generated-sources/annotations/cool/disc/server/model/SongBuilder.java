package cool.disc.server.model;

import io.norberg.automatter.AutoMatter;
import javax.annotation.Generated;
import javax.annotation.Nullable;
import org.bson.types.ObjectId;

@Generated("io.norberg.automatter.processor.AutoMatterProcessor")
public final class SongBuilder {
  private ObjectId id;

  private String artist;

  private String album;

  private Integer score;

  private String title;

  private String url;

  public SongBuilder() {
  }

  private SongBuilder(Song v) {
    this.id = v.id();
    this.artist = v.artist();
    this.album = v.album();
    this.score = v.score();
    this.title = v.title();
    this.url = v.url();
  }

  private SongBuilder(SongBuilder v) {
    this.id = v.id;
    this.artist = v.artist;
    this.album = v.album;
    this.score = v.score;
    this.title = v.title;
    this.url = v.url;
  }

  public ObjectId id() {
    return id;
  }

  public SongBuilder id(ObjectId id) {
    if (id == null) {
      throw new NullPointerException("id");
    }
    this.id = id;
    return this;
  }

  public String artist() {
    return artist;
  }

  public SongBuilder artist(String artist) {
    if (artist == null) {
      throw new NullPointerException("artist");
    }
    this.artist = artist;
    return this;
  }

  public String album() {
    return album;
  }

  public SongBuilder album(@Nullable String album) {
    this.album = album;
    return this;
  }

  public Integer score() {
    return score;
  }

  public SongBuilder score(@Nullable Integer score) {
    this.score = score;
    return this;
  }

  public String title() {
    return title;
  }

  public SongBuilder title(String title) {
    if (title == null) {
      throw new NullPointerException("title");
    }
    this.title = title;
    return this;
  }

  public String url() {
    return url;
  }

  public SongBuilder url(@Nullable String url) {
    this.url = url;
    return this;
  }

  public Song build() {
    return new Value(id, artist, album, score, title, url);
  }

  public static SongBuilder from(Song v) {
    return new SongBuilder(v);
  }

  public static SongBuilder from(SongBuilder v) {
    return new SongBuilder(v);
  }

  private static final class Value implements Song {
    private final ObjectId id;

    private final String artist;

    private final String album;

    private final Integer score;

    private final String title;

    private final String url;

    private Value(@AutoMatter.Field("id") ObjectId id, @AutoMatter.Field("artist") String artist,
        @AutoMatter.Field("album") String album, @AutoMatter.Field("score") Integer score,
        @AutoMatter.Field("title") String title, @AutoMatter.Field("url") String url) {
      if (id == null) {
        throw new NullPointerException("id");
      }
      if (artist == null) {
        throw new NullPointerException("artist");
      }
      if (title == null) {
        throw new NullPointerException("title");
      }
      this.id = id;
      this.artist = artist;
      this.album = album;
      this.score = score;
      this.title = title;
      this.url = url;
    }

    @AutoMatter.Field
    @Override
    public ObjectId id() {
      return id;
    }

    @AutoMatter.Field
    @Override
    public String artist() {
      return artist;
    }

    @AutoMatter.Field
    @Override
    public String album() {
      return album;
    }

    @AutoMatter.Field
    @Override
    public Integer score() {
      return score;
    }

    @AutoMatter.Field
    @Override
    public String title() {
      return title;
    }

    @AutoMatter.Field
    @Override
    public String url() {
      return url;
    }

    public SongBuilder builder() {
      return new SongBuilder(this);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof Song)) {
        return false;
      }
      final Song that = (Song) o;
      if (id != null ? !id.equals(that.id()) : that.id() != null) {
        return false;
      }
      if (artist != null ? !artist.equals(that.artist()) : that.artist() != null) {
        return false;
      }
      if (album != null ? !album.equals(that.album()) : that.album() != null) {
        return false;
      }
      if (score != null ? !score.equals(that.score()) : that.score() != null) {
        return false;
      }
      if (title != null ? !title.equals(that.title()) : that.title() != null) {
        return false;
      }
      if (url != null ? !url.equals(that.url()) : that.url() != null) {
        return false;
      }
      return true;
    }

    @Override
    public int hashCode() {
      int result = 1;
      long temp;
      result = 31 * result + (this.id != null ? this.id.hashCode() : 0);
      result = 31 * result + (this.artist != null ? this.artist.hashCode() : 0);
      result = 31 * result + (this.album != null ? this.album.hashCode() : 0);
      result = 31 * result + (this.score != null ? this.score.hashCode() : 0);
      result = 31 * result + (this.title != null ? this.title.hashCode() : 0);
      result = 31 * result + (this.url != null ? this.url.hashCode() : 0);
      return result;
    }

    @Override
    public String toString() {
      return "Song{" +
      "id=" + id +
      ", artist=" + artist +
      ", album=" + album +
      ", score=" + score +
      ", title=" + title +
      ", url=" + url +
      '}';
    }
  }
}
