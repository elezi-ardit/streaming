package SpotifyApiCall;

import SpotifyTracksExtended.SpotifyExtendedAlbum;
import SpotifyTracksExtended.SpotifyExtendedArtist;
import SpotifyTracksExtended.SpotifyTrackExtended;
import SpotifyTrack.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
 public class TrackApiCall {
	private static final String API_BASE_URL = "https://api.spotify.com/v1";

	private static final String AUTH_TOKEN = "Bearer BQA-MaT4AJMz3hD25EF-U6i3k5KVermBou7-4c71uO_4PXL_Fk3kdI3TdvgRGJV63nWPTBoLW35eYcB2hM_XwlhADl9XQIfPaXGHD0u1RBIJOTG71IzfbQasj7nLvIRvIA1vBXX_zMTKTjzMtk2ty2tdGbI5_l6BRRGV2CO6bpJJ-x1xiojb6JCLguMfKtL1DGEfqx_aHUg_86mA_YIgpw";

	private final WebClient webClient;

	private Gson gson;

	public TrackApiCall() {
		this.webClient = WebClient.builder()
				.clientConnector(new ReactorClientHttpConnector())
				.baseUrl(API_BASE_URL)
				.build();
		this.gson = new GsonBuilder().create();
	}

	public SpotifyTrackExtended getSpotifyExtendedTrack() {
		return apiCall().block();
	}


	private Mono<SpotifyTrackExtended> apiCall() {

		return webClient.get()
				.uri(uriBuilder -> uriBuilder.path("/tracks/{id}")
						.queryParam("market", "NL")
						.build("7AtwQRLPdttsxcLM5tPL0t"))
				//.queryParam("ids","7AtwQRLPdttsxcLM5tPL0t")
				//.queryParam("market","NL")
				//.build())
				//.queryParam("id","6Nd6ntkzr4t8o1FKPGOSMt")
				//.queryParam("market","NL")
				//.build("6Nd6ntkzr4t8o1FKPGOSMt"))
				.header("Authorization", AUTH_TOKEN)
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals, clientResponse -> {
					throw new RuntimeException("Internal Server Error" + clientResponse.rawStatusCode());
				})
				.onStatus(HttpStatus.BAD_REQUEST::equals, clientResponse -> {
					throw new RuntimeException("Bad Request " + clientResponse.rawStatusCode());
				})
				.onStatus(HttpStatus.UNAUTHORIZED::equals, clientResponse -> {
					throw new RuntimeException("Not authorized " + clientResponse.rawStatusCode());
				})
				.bodyToMono(String.class)
				.doOnNext(s -> System.out.println("Print " + s))
				//.map(x->gson.fromJson(x,SpotifyTrack.class))
				//.doOnNext(s-> System.out.println(s.getId() + s.getAlbum() +s.getArtists()))
				.map(x -> {

					SpotifyTrack originalTrack = gson.fromJson(x, SpotifyTrack.class);

					// Create a SpotifyExtendedAlbum instance
					SpotifyExtendedAlbum extendedAlbum = new SpotifyExtendedAlbum(originalTrack.getAlbum().getName());

					// Create a list of SpotifyExtendedArtist instances
					List<SpotifyExtendedArtist> extendedArtists = originalTrack.getArtists()
							.stream()
							.map(artist -> new SpotifyExtendedArtist(artist.getName()))
							.collect(Collectors.toList());

					// Create a SpotifyTrackExtended instance
					return new SpotifyTrackExtended(extendedAlbum, extendedArtists,
							originalTrack.getDisc_number(), originalTrack.getDuration_ms(),
							originalTrack.isExplicit(), originalTrack.getId(),
							originalTrack.getName(), originalTrack.getPopularity(),
							originalTrack.getType(), LocalDateTime.now());
				})
				.doOnNext(print -> System.out.println("Atq " + print.getArtists() + print.getId() + print.getEventTime()));

	}
}
