package client;

public class PostResult implements Comparable<PostResult> {
    private final Long start;
    private final Long latency;
    private final Integer blockNumber;
    private final Integer requestNumber;
    private final boolean success;

    public PostResult(Long start, Long latency, Integer blockNumber, Integer requestNumber, boolean success) {
        this.start = start;
        this.latency = latency;
        this.blockNumber = blockNumber;
        this.requestNumber = requestNumber;
        this.success = success;
    }

    public Long getStart() {
        return start;
    }

    public Long getLatency() {
        return latency;
    }

    public Integer getBlockNumber() {
        return blockNumber;
    }

    public Integer getRequestNumber() {
        return requestNumber;
    }

    public Boolean getSuccess() {
        return success;
    }

    @Override
    public int compareTo(PostResult other) {
        if(this.getStart() > other.getStart()) {
            return 1;
        }
        return -1;
    }
}
