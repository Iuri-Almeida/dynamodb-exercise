package br.com.itau.letscode.ialmeida.DynamoDB.config;

import br.com.itau.letscode.ialmeida.DynamoDB.model.Task;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
@EnableDynamoDBRepositories(
        value = "br.com.itau.letscode.ialmeida.DynamoDB.repository",
        dynamoDBMapperRef = "dynamoDBMapper"
)
public class DynamoDBConfig {

    @Value(value = "${aws.access.key.id:fakeId}")
    private String awsAccessKeyId;

    @Value(value = "${aws.access.key.secret:fakeSecret}")
    private String awsAccessKeySecret;

    @Value(value = "${dynamodb.service.endpoint:http://localhost:8000/}")
    private String dynamoDBServiceEndpoint;

    @Value(value = "${dynamodb.service.region:sa-east-1}")
    private String dynamoDBRegion;

    @Bean(name = "dynamoDBMapper")
    public DynamoDBMapper dynamoDBMapper() {
        return new DynamoDBMapper(amazonDynamoDB());
    }

    @Bean(name = "amazonDynamoDB")
    public AmazonDynamoDB amazonDynamoDB() {
        return AmazonDynamoDBClientBuilder
                .standard()
                .withEndpointConfiguration(endpointConfiguration())
                .withCredentials(credentialsProvider())
                .build();
    }

    private AwsClientBuilder.EndpointConfiguration endpointConfiguration() {
        return new AwsClientBuilder.EndpointConfiguration(dynamoDBServiceEndpoint, dynamoDBRegion);
    }

    private AWSCredentialsProvider credentialsProvider() {
        return new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsAccessKeyId, awsAccessKeySecret));
    }

    @EventListener(classes = ApplicationReadyEvent.class)
    public void setupDB() {
        DynamoDBMapper dynamoDBMapper = this.dynamoDBMapper();
        AmazonDynamoDB amazonDynamoDB = this.amazonDynamoDB();

        CreateTableRequest createTableRequest = dynamoDBMapper.generateCreateTableRequest(Task.class);

        if (!amazonDynamoDB.listTables().getTableNames().contains(createTableRequest.getTableName())) {
            createTableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
            amazonDynamoDB.createTable(createTableRequest);
        }
    }

}
