package cloud.models;

import lombok.Data;

@Data
public class User {
    private String userId;
    private String serverIp;
    private int serverSize;

    public String getUserId(){
        return userId == null ? "" : userId;
    }
    public int getServerSize() {
        return serverSize == 0 ? 102 : serverSize;
    }
}
