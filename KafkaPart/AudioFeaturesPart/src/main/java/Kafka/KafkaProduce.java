package Kafka;

import AudioFeatures.AudioFeature;
import main.AudioFeatureMain;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.midi.Track;
import java.io.IOException;
import java.util.Properties;


public class KafkaProduce {
	private KafkaProducer<String, AudioFeature> kafkaProducer;
	private static Logger logger = LoggerFactory.getLogger(KafkaProduce.class);

	public void init_producer(Properties properties) {
		properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		properties.put("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");
		properties.put("schema.registry.url", "http://localhost:8081");
		kafkaProducer= new KafkaProducer<>(properties);
	}

	public AudioFeature convertAudioFeature(String audiofeature) throws IOException {
		Decoder decoder = DecoderFactory.get().jsonDecoder(AudioFeature.getClassSchema(),audiofeature);
		DatumReader<AudioFeature> reader = new SpecificDatumReader<>(AudioFeature.class);

		AudioFeature audioFeature = null;
		try {
			audioFeature =reader.read(null,decoder);

		}
		catch (IOException e) {
			System.out.println("Failed to Deserialize Audio Feature" + e.getMessage());
			throw e;
		}

		return audioFeature;
	}


	public void sendRecord (AudioFeature audioFeature) {

		if (audioFeature!=null){
			String id = audioFeature.getId().toString();

			ProducerRecord<String , AudioFeature>  producerRecord = new ProducerRecord<>(AudioFeatureMain.topic_name,id,audioFeature);

			kafkaProducer.send(producerRecord, new Callback() {
				@Override
				public void onCompletion(RecordMetadata recordMetadata, Exception e) {
					if (e!=null){
						e.printStackTrace();
					}
					else
					{
						System.out.println("Message sent " + recordMetadata.toString());
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





