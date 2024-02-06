package SpotifyAudioFeatureExtended;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SpotifyAudioFeatureExtended {
	private double acousticness;
	private String analysis_url;
	private double danceability;
	private int duration_ms;
	private double energy;
	private String id;
	private double instrumentalness;
	private int key;
	private double liveness;
	private double loudness;
	private int mode;
	private double speechiness;
	private double tempo;
	private int time_signature;
	private String track_href;
	private String type;
	private String uri;
	private double valence;
	private String eventTime;
}