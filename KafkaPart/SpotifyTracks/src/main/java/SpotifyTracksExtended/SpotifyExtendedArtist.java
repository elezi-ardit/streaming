package SpotifyTracksExtended;

import lombok.Data;

@Data
public class SpotifyExtendedArtist {
	public SpotifyExtendedArtist(String name) {
		this.name = name;
	}

	private String name;
}
