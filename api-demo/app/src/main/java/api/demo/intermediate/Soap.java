package api.demo.intermediate;

//@WebService
//@SOAPBinding(style= SOAPBinding.Style.RPC)
public class Soap {
//    @Inject
    UserManager ejb;
    //@WebParam(name = "name")
    public void newAccount( String name) {
        ejb.createAccount(name);
    }
}
