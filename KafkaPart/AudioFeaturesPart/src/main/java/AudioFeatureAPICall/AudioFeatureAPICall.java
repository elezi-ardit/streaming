package AudioFeatureAPICall;

import AudioFeatures.AudioFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import SpotifyAudioFeature.SpotifyAudioFeature;
import SpotifyAudioFeatureExtended.SpotifyAudioFeatureExtended;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public class AudioFeatureAPICall {
	private static final String API_BASE_URL = "https://api.spotify.com/v1";
	private static final String AUTH_TOKEN = "Bearer BQAGo9r8q8-FuF4ZoIcUkOIrHxMBFrAhqKMqf6cogVBdUqP_A3POXnZ6hQ4m3U25PQDBSeykajM1K148NUGiRrrv1ORHpTHxkmrkRMCIxkavA8BpvinPNHjOuw4JKcuovz_gaSdK42AGWbUxdaY5UrgXCBkIJPAn0xBvwQuCDVUZgvJsa_62JWBmZ0NNj1Q1tRVUhbEWHutNhNFPxfm0zg";

	private final WebClient webClient;
	private Gson gson;


	public AudioFeatureAPICall() {
		this.webClient = WebClient.builder()
				.clientConnector(new ReactorClientHttpConnector())
				.baseUrl(API_BASE_URL)
				.build();
		this.gson = new GsonBuilder().create();
	}


	public AudioFeature getAudioFeautreExtended() {return apiCall().block();}


	private Mono<AudioFeature> apiCall() {

		return webClient.get()
				.uri(uriBuilder -> uriBuilder.path("/audio-features/{id}")
						.build("7AtwQRLPdttsxcLM5tPL0t"))
				.header("Authorization",AUTH_TOKEN)
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
				.map(json -> {
					AudioFeature audioFeature = gson.fromJson(json, AudioFeature.class);
					audioFeature.setEventTime(LocalDateTime.now().toString());
					return audioFeature;

				})
//				.map(x -> {
//					 SpotifyAudioFeatureExtended audioFeature=gson.fromJson(x , SpotifyAudioFeatureExtended.class);
//					 audioFeature.setEventTime(LocalDateTime.now().toString());
//					 return audioFeature;
//					 return  new SpotifyAudioFeatureExtended(
//							 audioFeature.getAcousticness(),
//							 audioFeature.getAnalysis_url(),
//							 audioFeature.getDanceability(),
//							 audioFeature.getDuration_ms(),
//							 audioFeature.getEnergy(),
//							 audioFeature.getId(),
//							 audioFeature.getInstrumentalness(),
//							 audioFeature.getKey(),
//							 audioFeature.getLiveness(),
//							 audioFeature.getLoudness(),
//							 audioFeature.getMode(),
//							 audioFeature.getSpeechiness(),
//							 audioFeature.getTempo(),
//							 audioFeature.getTime_signature(),
//							 audioFeature.getTrack_href(),
//							 audioFeature.getType(),
//							 audioFeature.getUri(),
//							 audioFeature.getValence(),
//							 LocalDateTime.now().toString());
//				}
//				)
				.doOnNext(print -> System.out.println("Atq " + print.getKey() + print.getId() + print.getEventTime()));

	}


}
