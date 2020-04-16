package com.jetbrains.integrationtests;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.Network;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class FaultToleranceTest {

    private static String testUploadRequest;
    private static String testSearchRequest;

    private static GenericContainer cacheContainer;
    private static GenericContainer zuulContainer;
    private static GenericContainer eurekaContainer;
    private static GenericContainer uploaderContainer;
    private static GenericContainer searcherContainer;
    private static GenericContainer storageContainer;
    private static GenericContainer mySQLContainer;
    private static Network network;

    private static CloseableHttpClient httpClient;

    @BeforeEach
    public void refreshHttpClient() {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(30_000)
                .setConnectionRequestTimeout(30_000)
                .setSocketTimeout(30_000)
                .build();
        httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
    }

    @BeforeAll
    public static void setUp() throws InterruptedException {
        network = Network.builder().
                id("network")
                .build();

        mySQLContainer = new MySQLContainer()
                .withDatabaseName("index")
                .withUsername("storage")
                .withPassword("password")
                .withNetwork(network)
                .withNetworkAliases("db");

        mySQLContainer.start();

        eurekaContainer = new GenericContainer("maximochkin/eureka-server")
                .withExposedPorts(8010)
                .withNetwork(network)
                .withNetworkAliases("eureka");

        eurekaContainer.start();

        zuulContainer = new GenericContainer("maximochkin/api-gateway")
                .withEnv("eureka.client.service-url.defaultZone", "http://eureka:8010/eureka")
                .withExposedPorts(8011)
                .withNetwork(network);

        cacheContainer = new GenericContainer("maximochkin/cache")
                .withEnv("eureka.client.service-url.defaultZone", "http://eureka:8010/eureka")
                .withEnv("server.port", "8091")
                .withEnv("eureka.instance.prefer-ip-address", "true")
                .withExposedPorts(8091)
                .withNetwork(network);

        uploaderContainer = new GenericContainer("maximochkin/uploader")
                .withEnv("eureka.client.service-url.defaultZone", "http://eureka:8010/eureka")
                .withEnv("server.port", "8089")
                .withEnv("eureka.instance.prefer-ip-address", "true")
                .withExposedPorts(8089)
                .withNetwork(network);

        searcherContainer = new GenericContainer("maximochkin/searcher")
                .withEnv("eureka.client.service-url.defaultZone", "http://eureka:8010/eureka")
                .withEnv("server.port", "8090")
                .withEnv("eureka.instance.prefer-ip-address", "true")
                .withExposedPorts(8090)
                .withNetwork(network);;


        storageContainer = new GenericContainer("maximochkin/storage")
                .withEnv("eureka.client.service-url.defaultZone", "http://eureka:8010/eureka")
                .withEnv("server.port", "8092")
                .withEnv("eureka.instance.prefer-ip-address", "true")
                .withEnv("spring.jpa.hibernate.ddl-auto", "update")
                .withEnv("spring.datasource.username", "storage")
                .withEnv("spring.datasource.password", "password")
                .withEnv("spring.datasource.url", "jdbc:mysql://db:3306/index")
                .withExposedPorts(8092)
                .withNetwork(network);

        zuulContainer.start();
        cacheContainer.start();
        searcherContainer.start();
        uploaderContainer.start();
        storageContainer.start();

        testUploadRequest = "http://localhost:" + zuulContainer.getFirstMappedPort() + "/uploader/upload";
        testSearchRequest = "http://localhost:" + zuulContainer.getFirstMappedPort() + "/searcher/getFilesByWord?word=";

        Thread.sleep(TimeUnit.SECONDS.toMillis(60));
    }


    @Test
    public void afterCacheFailedAppKeepsWorking() {

    }

    @Test
    public void ordinaryFileUploadTest() throws IOException {
        HttpResponse response = uploadTestFile();
        Assert.assertTrue("File uploading with all apps up should be processed properly",
                response.getStatusLine().getStatusCode()==200);

        EntityUtils.consumeQuietly(response.getEntity());
    }

    @Test
    public void ordinaryFileSearchTest() throws IOException {
        String word = "test";
        HttpResponse response = searchByWord(word);
        Assert.assertEquals("Search by word with all apps up should be processed properly even " +
                        "if nothing has been uploaded before",
                HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        EntityUtils.consumeQuietly(response.getEntity());
    }

    @Test
    public void searchReturnsNotEmptyResultAfterUploading() throws IOException, InterruptedException {
        HttpResponse uploadResponse = uploadTestFile();
        Assert.assertEquals(HttpStatus.SC_OK, uploadResponse.getStatusLine().getStatusCode());
        EntityUtils.consumeQuietly(uploadResponse.getEntity());

        HttpResponse searchResponse = searchByWord("you");  // word from test file
        Assert.assertEquals(HttpStatus.SC_OK, searchResponse.getStatusLine().getStatusCode());
        Scanner scanner = new Scanner(new InputStreamReader(searchResponse.getEntity().getContent()));
        Assert.assertTrue("Response for word presented in file shouldn't be empty", scanner.hasNext());
//        EntityUtils.consumeQuietly(searchResponse.getEntity());
    }

    @Test
    public void userDoesntNoticeIfOneUploaderFailed() throws InterruptedException, IOException {
            final int numberOfRequests = 3;
            GenericContainer uploader2 = new GenericContainer("maximochkin/uploader")
                    .withEnv("eureka.client.service-url.defaultZone", "http://eureka:8010/eureka")
                    .withEnv("server.port", "8093")
                    .withEnv("eureka.instance.prefer-ip-address", "true")
                    .withExposedPorts(8093)
                    .withNetwork(network);
            uploader2.start();
            Thread.sleep(TimeUnit.SECONDS.toMillis(60)); // wait until zuul takes 2nd uploader into account
            for (int i = 0; i < numberOfRequests; i++) {
                HttpResponse response = uploadTestFile();
                Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
                EntityUtils.consumeQuietly(response.getEntity());
            }
            uploaderContainer.stop();
            for (int i = 0; i < numberOfRequests; i++) {
                HttpResponse response = uploadTestFile();
                Assert.assertEquals("After one uploader failed all requests should be processed by the others",
                        HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
                EntityUtils.consumeQuietly(response.getEntity());
            }
            uploaderContainer = uploader2;
    }

    @Test
    public void userDoesntNoticeIfOneSearcherFailed() throws InterruptedException, IOException {
            final int numberOfRequests = 3;
            String word = "test";
            GenericContainer searcher2 = new GenericContainer("maximochkin/searcher")
                    .withEnv("eureka.client.service-url.defaultZone", "http://eureka:8010/eureka")
                    .withEnv("server.port", "8094")
                    .withEnv("eureka.instance.prefer-ip-address", "true")
                    .withExposedPorts(8094)
                    .withNetwork(network);
            searcher2.start();
            Thread.sleep(TimeUnit.SECONDS.toMillis(60));
            for (int i = 0; i < numberOfRequests; i++) {
                HttpResponse response = searchByWord(word);
                Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
                EntityUtils.consumeQuietly(response.getEntity());
            }
            searcherContainer.stop();
            for (int i = 0; i < numberOfRequests; i++) {
                HttpResponse response = searchByWord(word);
                Assert.assertEquals("After one searcher failed all requests should be processed by the others",
                        HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
                EntityUtils.consumeQuietly(response.getEntity());
            }
            searcherContainer = searcher2;
    }

    @Test
    public void userDoesntNoticeIfOneStorageFailed() throws InterruptedException, IOException {
        final int numberOfRequests = 3;
        String word = "test";
        GenericContainer storage2 = new GenericContainer("maximochkin/storage")
                .withEnv("eureka.client.service-url.defaultZone", "http://eureka:8010/eureka")
                .withEnv("server.port", "8095")
                .withEnv("eureka.instance.prefer-ip-address", "true")
                .withEnv("spring.jpa.hibernate.ddl-auto", "update")
                .withEnv("spring.datasource.username", "storage")
                .withEnv("spring.datasource.password", "password")
                .withEnv("spring.datasource.url", "jdbc:mysql://db:3306/index")
                .withExposedPorts(8095)
                .withNetwork(network);
        storage2.start();
        Thread.sleep(TimeUnit.SECONDS.toMillis(60));
        for (int i = 0; i < numberOfRequests; i++) {
            HttpResponse response = searchByWord(word);
            Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
            EntityUtils.consumeQuietly(response.getEntity());
        }
        storageContainer.stop();
        for (int i = 0; i < numberOfRequests; i++) {
            HttpResponse response = searchByWord(word);
            Assert.assertEquals("After one storage service failed all requests should be processed by the others",
                    HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
            EntityUtils.consumeQuietly(response.getEntity());

            response = uploadTestFile();
            Assert.assertEquals("After one storage service failed all requests should be processed by the others",
                    HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
            EntityUtils.consumeQuietly(response.getEntity());
        }
        storageContainer = storage2;
    }

    @Test
    public void runWithUnavailableCache() throws IOException {
        String word = "test";
        cacheContainer.stop();
        HttpResponse response = searchByWord(word);
        Assert.assertEquals("Search doesn't fail without available cache",
                HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        EntityUtils.consumeQuietly(response.getEntity());

        response = uploadTestFile();
        Assert.assertEquals("Uploading doesn't fail without available cache",
                HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        EntityUtils.consumeQuietly(response.getEntity());
        cacheContainer.start();
    }

    private HttpResponse uploadTestFile() throws IOException {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        FileBody fileBody = new FileBody(new File(
                this.getClass().getClassLoader().getResource("file2.txt").getPath())
        );
        builder.addPart("file", fileBody);
        HttpEntity entity = builder.build();
        HttpPost request = new HttpPost(testUploadRequest);
        request.setEntity(entity);

        return httpClient.execute(request);
    }

    private HttpResponse searchByWord(String word) throws IOException {
        HttpGet request = new HttpGet(testSearchRequest + word);
        HttpResponse response = httpClient.execute(request);
        return response;
    }


}
