package SpotifyTrack;


import lombok.*;

import java.time.LocalDateTime;
import java.util.List;



@Data
@AllArgsConstructor
public
class SpotifyTrack {

	public SpotifyAlbum album;
	public List<SpotifyArtist> artists;
	private Integer disc_number;
	private Integer duration_ms;
	private boolean explicit;
	private String id;
	private String name;
	private Integer popularity;
	private String type;



}

