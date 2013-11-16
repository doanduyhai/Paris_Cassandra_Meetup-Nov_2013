package info.archinnov.demo.model;

public class RateLimit {

    private String value;
    private int ttl;

    public RateLimit() {
    }

    public RateLimit(String value, int ttl) {
        this.value = value;
        this.ttl = ttl;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }
}
