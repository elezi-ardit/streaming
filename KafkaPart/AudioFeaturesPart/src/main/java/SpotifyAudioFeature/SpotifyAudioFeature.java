package SpotifyAudioFeature;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SpotifyAudioFeature {
	public double acousticness;
	public String analysis_url;
	public double danceability;
	public int duration_ms;
	public double energy;
	public String id;
	public double instrumentalness;
	public int key;
	public double liveness;
	public double loudness;
	public int mode;
	public double speechiness;
	public double tempo;
	public int time_signature;
	public String track_href;
	public String type;
	public String uri;
	public double valence;
}
