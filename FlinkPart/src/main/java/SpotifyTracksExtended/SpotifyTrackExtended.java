package SpotifyTracksExtended;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpotifyTrackExtended implements Serializable {



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



}

