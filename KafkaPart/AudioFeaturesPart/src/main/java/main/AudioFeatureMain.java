package main;

import AudioFeatureAPICall.AudioFeatureAPICall;
import AudioFeatures.AudioFeature;
import Kafka.KafkaProduce;
import Kafka.Util;
import SpotifyAudioFeatureExtended.SpotifyAudioFeatureExtended;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Properties;

public class AudioFeatureMain {
	public static final String topic_name = "audio_beach";
	public static void main(String[] args) throws IOException {
		 Gson gson = new Gson();

		 AudioFeatureAPICall audioFeatureAPICall = new AudioFeatureAPICall();
		 //SpotifyAudioFeatureExtended audioFeatureExtended=audioFeatureAPICall.getAudioFeautreExtended();
		AudioFeature tobeSent =  audioFeatureAPICall.getAudioFeautreExtended();
		Properties properties= Util.loadConfig(args[0]);
		KafkaProduce kafkaProduce = new KafkaProduce();
		kafkaProduce.init_producer(properties);
		//AudioFeature audioFeature = kafkaProduce.convertAudioFeature(gson.toJson(audioFeatureExtended));
		kafkaProduce.sendRecord(tobeSent);
		kafkaProduce.closeProducer();
		 //System.out.println(gson.toJson(audioFeatureExtended));
		 System.out.println(tobeSent.getId() + " " + tobeSent.getEventTime() + " " + tobeSent.getKey());

	}
}
