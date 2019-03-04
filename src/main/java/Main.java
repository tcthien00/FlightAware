import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        // prepare json file
        File myFile = new File("ThienTruongFlightAware.json");

        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(myFile, JsonEncoding.UTF8);
        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("name", "Thien Truong");
        jsonGenerator.writeStringField("email", "tcthien@gmail.com");
        jsonGenerator.writeStringField("position", "Software Developer in Test");

        jsonGenerator.writeFieldName("urls");
        jsonGenerator.writeStartArray();
        jsonGenerator.writeString("https://my.indeed.com/r/Thien-Truong/3f347064eb13c0fd/byId/Eek-r6BK24a3Kvf3GB6Mxg/pdf");
        jsonGenerator.writeString("https://www.linkedin.com/in/thientruong");
        jsonGenerator.writeString("https://github.com/tcthien00/FlightAware");
        jsonGenerator.writeEndArray();

        jsonGenerator.writeStringField("comment", "Please contact me for more information");

        jsonGenerator.writeEndObject();
        jsonGenerator.close();

        // submit application
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://flightaware.com/about/careers/apply?data=");

        FileEntity entity = new FileEntity(myFile);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = client.execute(httpPost);
        Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
        client.close();
    }
}
