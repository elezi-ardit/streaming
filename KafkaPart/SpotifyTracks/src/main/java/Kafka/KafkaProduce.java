package Kafka;


import SpotifyTracksExtended.SpotifyTrackExtended;
import Tracks.Track;
import com.google.gson.Gson;
import main.SpotifyTracksMain;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.kafka.clients.producer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public class KafkaProduce {

	private KafkaProducer<String, Track> kafkaProducer;
	private  static Logger logger = LoggerFactory.getLogger(KafkaProduce.class);

	public void init_producer(Properties properties) throws IOException {
		properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		//properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		//properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
		properties.put("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");
		properties.put("schema.registry.url", "http://localhost:8081");
		kafkaProducer= new KafkaProducer<>(properties);
	}

	public Track convertToAvroTrack(String  spotifyTrackJson) throws IOException {

		Decoder decoder = DecoderFactory.get().jsonDecoder(Track.getClassSchema(), spotifyTrackJson);
		DatumReader<Track> reader = new SpecificDatumReader<>(Track.class);

		Track avroTrack = null;

		try {
			avroTrack = reader.read(null, decoder);
		} catch (IOException e) {
			System.out.println("Failed to deserialize Tracks.Tracks.Track: " + e.getMessage());
			throw e;
		}


		return avroTrack;
	}



	public void sendRecord (Track spotifyTrackExtended) throws IOException {

		if (spotifyTrackExtended!=null) {
			//List<Tracks.Tracks.Track> sendToKafka = convertToAvroTrack(spotifyTrack);
			//Gson gson = new Gson();
			//String json = gson.toJson(spotifyTrack);
			//	System.out.println("Jsons " + json);
			String id = spotifyTrackExtended.getId().toString();
			//String json = gson.toJson(spotifyTrackExtended);

			//System.out.println("Jsons " + json);
			//Track trackavro = convertToAvroTrack(json);
			ProducerRecord<String, Track> producerRecord = new ProducerRecord<>(SpotifyTracksMain.topic_name, id,spotifyTrackExtended);
			kafkaProducer.send(producerRecord, new Callback() {
				@Override
				public void onCompletion(RecordMetadata recordMetadata, Exception e) {
					if (e != null) {
						e.printStackTrace();
					} else {
						System.out.println("Message sent" + recordMetadata.toString());
					}
				}
			});


		}
	}
	public void closeProducer() {
		kafkaProducer.flush();
		kafkaProducer.close();
	}

}


