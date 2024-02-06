package Deserializer;

import Tracks.Album;
import Tracks.Artist;
import Tracks.Track;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema;
import org.apache.flink.formats.avro.registry.confluent.ConfluentRegistryAvroDeserializationSchema;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
import org.apache.flink.util.Collector;
import org.apache.kafka.clients.consumer.ConsumerRecord;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CustomKafkaRecordDeserializer implements KafkaRecordDeserializationSchema<Track> {

	Logger logger = Logger.getLogger(CustomKafkaRecordDeserializer.class.getName());
	private final TypeInformation<Track> typeInformation;

	//private final DeserializationSchema<Track> deserializationSchemaValue;
	public ConfluentRegistryAvroDeserializationSchema<Track> deserializationSchema;
	//public KafkaAvroDeserializer avroDeserializer;
	public CustomKafkaRecordDeserializer(final Class<Track> trackClass,final String schemaRegistryUrl) {
		this.typeInformation =  TypeInformation.of(trackClass);
	//	this.avroDeserializer= new KafkaAvroDeserializer();
		this.deserializationSchema = ConfluentRegistryAvroDeserializationSchema.forSpecific(trackClass,schemaRegistryUrl);
	}



//	public CustomDeserializer(String schemaRegistryUrl) {
//		Properties properties = new Properties();
//		properties.put("schema.registry.url", schemaRegistryUrl);
//		this.avroDeserializer = new KafkaAvroDeserializer();
//	}

//	@Override
//	public Track deserialize(byte[] bytes) throws IOException {
//		GenericRecord avrorecord = (GenericRecord) avroDeserializer.deserialize(null,bytes);
//
//		///SpecificDatumReader<Track> reader = new SpecificDatumReader<>(Track.class);
//		//Track track = new Track();
//	//	reader.read(track, reader.)
//		return convertToTrack(avrorecord);
//	}



	@Override
	public void deserialize(ConsumerRecord<byte[], byte[]> consumerRecord, Collector<Track> collector) throws IOException {
		GenericRecord avrorecord = (GenericRecord) deserializationSchema.deserialize(consumerRecord.value()); //.deserialize(null,consumerRecord.value());
		System.out.println("Avro_Ardit"  + avrorecord);
		logger.info("Is it this shit " + avrorecord.toString());
		///SpecificDatumReader<Track> reader = new SpecificDatumReader<>(Track.class);
		//Track track = new Track();
		//	reader.read(track, reader.)
		 collector.collect(convertToTrack(avrorecord));
	}

	@Override
	public TypeInformation<Track> getProducedType() {
		return TypeInformation.of(Track.class);
	}


	private Track convertToTrack(GenericRecord genericRecord) {
		// Assuming the Avro schema fields match the fields in your Track class

		Album album = convertToAlbum((GenericRecord) genericRecord.get("album"));

		List<Artist> artists = new ArrayList<>();
		GenericData.Array artistArray = (GenericData.Array) genericRecord.get("artists");
		for (Object artistObject : artistArray) {
			artists.add(convertToArtist((GenericRecord) artistObject));
		}

		int discNumber = (int) genericRecord.get("disc_number");
		int durationMs = (int) genericRecord.get("duration_ms");
		boolean explicit = (boolean) genericRecord.get("explicit");
		CharSequence id = (CharSequence) genericRecord.get("id");
		CharSequence name = (CharSequence) genericRecord.get("name");
		int popularity = (int) genericRecord.get("popularity");
		CharSequence type = (CharSequence) genericRecord.get("type");
		CharSequence eventTime = (CharSequence) genericRecord.get("eventTime").toString();
		System.out.println("WTF " + genericRecord.get("eventTime").toString());

		// Create and return a new Track instance
		return new Track(album, artists, discNumber, durationMs, explicit, id, name, popularity, type, eventTime);
	}


	private Album convertToAlbum(GenericRecord genericRecord) {
		// Implement logic to convert GenericRecord to Album
	return new Album(genericRecord.get("name").toString());
	//	return null;
	}

	private Artist convertToArtist(GenericRecord genericRecord) {
		// Implement logic to convert GenericRecord to Artist
		return new Artist(genericRecord.get("name").toString());
		//return null;
	}


//	private transient ObjectMapper objectMapper;
//
//
//	@Override
//	public SpotifyTrackExtended deserialize(byte[] bytes) throws IOException {
//		return objectMapper.readValue(bytes,SpotifyTrackExtended.class);
//	}
//
//	@Override
//	public boolean isEndOfStream(SpotifyTrackExtended spotifyTrackExtended) {
//		return false;
//	}
//
//	@Override
//	public TypeInformation<SpotifyTrackExtended> getProducedType() {
//		return  TypeInformation.of(SpotifyTrackExtended.class);
//	}
}
