package api.demo.basic;

import javax.jws.WebService;

@WebService( serviceName = "soap", endpointInterface = "api.demo.basic.SoapInt")
public class SoapImpl implements SoapInt {
    @Override
    public String getHelloAsString(String input) {
        return "Input: " + input;
    }
}
