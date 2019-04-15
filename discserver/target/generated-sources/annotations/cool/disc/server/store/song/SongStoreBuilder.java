package cool.disc.server.store.song;

import io.norberg.automatter.AutoMatter;
import javax.annotation.Generated;
import javax.annotation.Nullable;
import org.bson.types.ObjectId;

@Generated("io.norberg.automatter.processor.AutoMatterProcessor")
public final class SongStoreBuilder {
  private ObjectId getSongId;

  private String artist;

  private String album;

  private Integer score;

  private String title;

  private String url;

  public SongStoreBuilder() {
  }

  private SongStoreBuilder(SongStore v) {
    this.getSongId = v.getSongId();
    this.artist = v.artist();
    this.album = v.album();
    this.score = v.score();
    this.title = v.title();
    this.url = v.url();
  }

  private SongStoreBuilder(SongStoreBuilder v) {
    this.getSongId = v.getSongId;
    this.artist = v.artist;
    this.album = v.album;
    this.score = v.score;
    this.title = v.title;
    this.url = v.url;
  }

  public ObjectId getSongId() {
    return getSongId;
  }

  public SongStoreBuilder getSongId(ObjectId getSongId) {
    if (getSongId == null) {
      throw new NullPointerException("getSongId");
    }
    this.getSongId = getSongId;
    return this;
  }

  public String artist() {
    return artist;
  }

  public SongStoreBuilder artist(String artist) {
    if (artist == null) {
      throw new NullPointerException("artist");
    }
    this.artist = artist;
    return this;
  }

  public String album() {
    return album;
  }

  public SongStoreBuilder album(@Nullable String album) {
    this.album = album;
    return this;
  }

  public Integer score() {
    return score;
  }

  public SongStoreBuilder score(@Nullable Integer score) {
    this.score = score;
    return this;
  }

  public String title() {
    return title;
  }

  public SongStoreBuilder title(String title) {
    if (title == null) {
      throw new NullPointerException("title");
    }
    this.title = title;
    return this;
  }

  public String url() {
    return url;
  }

  public SongStoreBuilder url(@Nullable String url) {
    this.url = url;
    return this;
  }

  public SongStore build() {
    return new Value(getSongId, artist, album, score, title, url);
  }

  public static SongStoreBuilder from(SongStore v) {
    return new SongStoreBuilder(v);
  }

  public static SongStoreBuilder from(SongStoreBuilder v) {
    return new SongStoreBuilder(v);
  }

  private static final class Value implements SongStore {
    private final ObjectId getSongId;

    private final String artist;

    private final String album;

    private final Integer score;

    private final String title;

    private final String url;

    private Value(@AutoMatter.Field("getSongId") ObjectId getSongId,
        @AutoMatter.Field("artist") String artist, @AutoMatter.Field("album") String album,
        @AutoMatter.Field("score") Integer score, @AutoMatter.Field("title") String title,
        @AutoMatter.Field("url") String url) {
      if (getSongId == null) {
        throw new NullPointerException("getSongId");
      }
      if (artist == null) {
        throw new NullPointerException("artist");
      }
      if (title == null) {
        throw new NullPointerException("title");
      }
      this.getSongId = getSongId;
      this.artist = artist;
      this.album = album;
      this.score = score;
      this.title = title;
      this.url = url;
    }

    @AutoMatter.Field
    @Override
    public ObjectId getSongId() {
      return getSongId;
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

    public SongStoreBuilder builder() {
      return new SongStoreBuilder(this);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof SongStore)) {
        return false;
      }
      final SongStore that = (SongStore) o;
      if (getSongId != null ? !getSongId.equals(that.getSongId()) : that.getSongId() != null) {
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
      result = 31 * result + (this.getSongId != null ? this.getSongId.hashCode() : 0);
      result = 31 * result + (this.artist != null ? this.artist.hashCode() : 0);
      result = 31 * result + (this.album != null ? this.album.hashCode() : 0);
      result = 31 * result + (this.score != null ? this.score.hashCode() : 0);
      result = 31 * result + (this.title != null ? this.title.hashCode() : 0);
      result = 31 * result + (this.url != null ? this.url.hashCode() : 0);
      return result;
    }

    @Override
    public String toString() {
      return "SongStore{" +
      "getSongId=" + getSongId +
      ", artist=" + artist +
      ", album=" + album +
      ", score=" + score +
      ", title=" + title +
      ", url=" + url +
      '}';
    }
  }
}
