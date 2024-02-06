/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package FlinkPart;

import Deserializer.TrackDeserializer;
import Tracks.Track;
import org.apache.avro.generic.GenericData;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.formats.avro.registry.confluent.ConfluentRegistryAvroDeserializationSchema;
import org.apache.flink.formats.avro.utils.AvroKryoSerializerUtils;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class DataStreamJob {

	public static final String topic66 = "topic_66";
	static final String SCHEMA_REGISTRY_URL ="http://localhost:8081/";


	public static void main(String[] args) throws Exception {

		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		env.getConfig().enableForceAvro();

		//SchemaRegistryClient schemaRegistryClient = new CachedSchemaRegistryClient(SCHEMA_REGISTRY_URL);
		env.getConfig().disableForceKryo();
		env.getConfig().disableGenericTypes();
		//env.getConfig().registerAvroSchema(schemaRegistryClient.getLatestSchema(topic66, schemaRegistryClient.getSchemaVersion(topic66)), Track.class);

		KafkaSource<Track> kafkaSource = KafkaSource.<Track>builder()
				.setTopics(topic66)
				.setGroupId("flink-group")
				.setBootstrapServers("localhost:9092")
				.setStartingOffsets(OffsetsInitializer.earliest())
				//.setValueOnlyDeserializer(ConfluentRegistryAvroDeserializationSchema.forSpecific(Track.class,SCHEMA_REGISTRY_URL))
				.setDeserializer(new TrackDeserializer(Track.class,SCHEMA_REGISTRY_URL))
				//.setDeserializer(new CustomKafkaRecordDeserializer(Track.class,SCHEMA_REGISTRY_URL))
				//.setValueOnlyDeserializer(new CustomDeserializer())
				//.setValueOnlyDeserializer((DeserializationSchema<Track>) new TrackDeserializer(Track.class,SCHEMA_REGISTRY_URL))
				//.setProperty("key.converter","org.apache.kafka.connect.storage.StringConverter")
				//.setValueOnlyDeserializer(ConfluentRegistryAvroDeserializationSchema.forSpecific(Track.class,SCHEMA_REGISTRY_URL))
				.build();





		//DataStream<Track> dataStream = env.fromSource(kafkaSource, WatermarkStrategy.noWatermarks(),"reading Tracks");
		DataStream<Track> dataStream = env.fromSource(kafkaSource, WatermarkStrategy.noWatermarks(),"reading Tracks")
				.returns(Types.POJO(Track.class));
			//.returns(TypeInformation.of(SpotifyTrackExtended.class)); //.POJO(Track.class));



		dataStream.print();

		// Execute program, beginning computation.
		env.execute("Flink Java API Skeleton");
	}
}
