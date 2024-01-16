package SpotifyTracksExtended;

import lombok.Data;

@Data

public class SpotifyExtendedAlbum {
	private String name;

	public SpotifyExtendedAlbum(String name) {
		this.name = name;
	}
}
