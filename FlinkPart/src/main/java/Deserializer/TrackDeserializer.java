package Deserializer;

import Tracks.Track;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecord;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.TypeInfo;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.typeutils.TupleTypeInfo;
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema;
import org.apache.flink.formats.avro.registry.confluent.ConfluentRegistryAvroDeserializationSchema;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
import org.apache.flink.util.Collector;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;

public class TrackDeserializer implements KafkaRecordDeserializationSchema<Track> {

	private final TypeInformation<Track> typeInformation;

	private final DeserializationSchema<Track> deserializationSchemaValue;

	public TrackDeserializer(final Class<Track> trackClass,final String schemaRegistryUrl) {
		this.typeInformation =  TypeInformation.of(trackClass);
		this.deserializationSchemaValue = ConfluentRegistryAvroDeserializationSchema.forSpecific(trackClass,schemaRegistryUrl);
	}


//	@Override
//	public Track deserialize(ConsumerRecord<byte[], byte[]> consumerRecord) throws Exception {
//		 Track deserializedTrack = deserializationSchemaValue.deserialize(consumerRecord.value());
//		return deserializedTrack;
//	}

	@Override
	public void deserialize(ConsumerRecord<byte[], byte[]> consumerRecord, Collector<Track> collector) throws IOException {


		try {


			Track deserializedTrack = deserializationSchemaValue.deserialize(consumerRecord.value());
			collector.collect(deserializedTrack);
		} catch (IOException e) {
			System.out.println("Error deserializing Track: " + e.getMessage());
			e.printStackTrace();
		}


	}

	@Override
	public TypeInformation<Track> getProducedType() {
		return  typeInformation;
	}

// DeserializationSchema<Track> {

//	private final ObjectMapper objectMapper = new ObjectMapper();
//	@Override
//	public void open(InitializationContext context) throws Exception {
//		DeserializationSchema.super.open(context);
//	}
//
//	@Override
//	public Track deserialize(byte[] bytes) throws IOException {
//		return objectMapper.readValue(bytes, Track.class);
//	}
//
//
//
//	@Override
//	public boolean isEndOfStream(Track track) {
//		return false;
//	}
//
//	@Override
//	public TypeInformation<Track> getProducedType() {
//		return TypeInformation.of(Track.class);
//	}
}
