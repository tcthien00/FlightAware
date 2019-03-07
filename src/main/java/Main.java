import org.apache.http.Header;
import org.apache.http.HttpMessage;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.testng.Assert;

import java.io.IOException;
import java.net.URI;

public class Main {

    public static void main(String[] args) throws IOException {
        String json = "{\n" +
                "\t\"name\": \"Thien Truong\",\n" +
                "\t\"email\": \"tcthien@gmail.com\",\n" +
                "\t\"position\": \"Software Developer in Test\",\n" +
                "\t\"urls\": [\"https://my.indeed.com/r/Thien-Truong/3f347064eb13c0fd/byId/Eek-r6BK24a3Kvf3GB6Mxg/pdf\", \"https://www.linkedin.com/in/thientruong\", \"https://github.com/tcthien00/FlightAware\"],\n" +
                "\t\"comment\": \"Please contact me for more information\"\n" +
                "}";
      StringEntity entity = new StringEntity(json);

        // prepare POST
        HttpPost httpPost = new HttpPost("https://flightaware.com/about/careers/apply?data=");
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setEntity(entity);
        printMessageHeaders(httpPost);

        // send POST to submit application
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = client.execute(httpPost);
        printMessageHeaders(response);

        //read the response code
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY){
            String newLoc = response.getHeaders("Location").toString();
            System.out.println(newLoc);
            httpPost.setURI(URI.create(newLoc));
            response = client.execute(httpPost);
            printMessageHeaders(response);
        }

        // verify response code
        Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
        client.close();
    }

    public static void printMessageHeaders(HttpMessage message) {
        Header[] headers = message.getAllHeaders();
        System.out.println("\n------------------------");
        if (message instanceof HttpUriRequest)
            System.out.println(((HttpUriRequest) message).getRequestLine());
        else
            System.out.println(((HttpResponse) message).getStatusLine());
        for (Header h : headers) {
            System.out.println(h.getName() + ": " + h.getValue());
        }
        System.out.println("------------------------");
    }
}
