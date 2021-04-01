package api.demo.basic;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;
import java.net.URL;

import static org.assertj.core.api.Assertions.*;

class SoapImplTest {
    private static final String HOST = "http://localhost:";
    private static final SoapImpl SOAP = new SoapImpl();
    private static final int PORT = 8080;
    private static Endpoint endpoint;

    @BeforeAll
    public static void setup() {
//       Endpoint.publish(HOST + PORT + "/soap", SOAP);
    }


    @Test
    public void testHelloAsString() throws Exception {
        URL wsdlUrl = new URL(HOST + PORT + "/demo/soap?wsdl");
        QName serviceName = new QName("http://basic.demo.api/","soap");
        Service service = Service.create(wsdlUrl, serviceName);

        SoapInt proxy = service.getPort(SoapInt.class);

        String result = proxy.getHelloAsString("Hayk");

        assertThat(result).isEqualTo("Input: Hayk");
    }
//    private static Service createService(String queryName) {
//
//    }
    @AfterAll
    public static void close() {
//        endpoint.stop();
    }
}