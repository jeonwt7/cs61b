public class Edge {
    private long from;
    private long to;

    Edge(long from, long to) {
        this.from = from;
        this.to = to;
    }

    long getFrom() {
        return this.from;
    }

    long getTo() {
        return this.to;
    }
}
