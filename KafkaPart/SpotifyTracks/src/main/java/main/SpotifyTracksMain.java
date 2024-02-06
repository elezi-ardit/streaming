package main;


import Kafka.KafkaProduce;
import Kafka.Util;
import SpotifyApiCall.TrackApiCall;
import SpotifyTracksExtended.SpotifyTrackExtended;
import Tracks.Track;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Properties;

public class SpotifyTracksMain {

	public static String topic_name = "topic_66";
	public static void main(String[] args) throws IOException {

		Properties properties = Util.loadConfig(args[0]);

		TrackApiCall trackApiCall = new TrackApiCall();

		SpotifyTrackExtended spotifyTrackExtended = trackApiCall.getSpotifyExtendedTrack();
		Gson gson = new Gson();
		System.out.println("Json" + gson.toJson(spotifyTrackExtended));
		String TrackGson = gson.toJson(spotifyTrackExtended);
		KafkaProduce kafkaProduce = new KafkaProduce();
		kafkaProduce.init_producer(properties);
		Track TrackAvro =kafkaProduce.convertToAvroTrack(TrackGson);
		System.out.println("Detajet e Avro " + TrackAvro.getId() + "  " + TrackAvro.getName() + " " + TrackAvro.getEventTime() );
		kafkaProduce.sendRecord(TrackAvro);
		kafkaProduce.closeProducer();

	}
}
