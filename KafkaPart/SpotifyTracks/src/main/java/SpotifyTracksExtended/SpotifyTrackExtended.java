package SpotifyTracksExtended;

import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SpotifyTrackExtended {



	private SpotifyExtendedAlbum album;
	private List<SpotifyExtendedArtist> artists;
	private Integer disc_number;
	private Integer duration_ms;
	private boolean explicit;
	private String id;
	private String name;
	private Integer popularity;
	private String type;
	private String  eventTime;

	public SpotifyTrackExtended(SpotifyExtendedAlbum album, List<SpotifyExtendedArtist> artists, Integer disc_number, Integer duration_ms, boolean explicit, String id, String name, Integer popularity, String type, String eventTime) {
		this.album = album;
		this.artists = artists;
		this.disc_number = disc_number;
		this.duration_ms = duration_ms;
		this.explicit = explicit;
		this.id = id;
		this.name = name;
		this.popularity = popularity;
		this.type = type;
		this.eventTime = eventTime;
	}


}

