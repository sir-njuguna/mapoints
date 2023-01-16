package mapoints.user.view;

public class JwtView {
    private String jwtId;
    private String jwtToken;

    public JwtView(String jwtId, String jwtToken) {
        this.jwtId = jwtId;
        this.jwtToken = jwtToken;
    }

    public String getJwtId() {
        return jwtId;
    }

    public void setJwtId(String jwtId) {
        this.jwtId = jwtId;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
