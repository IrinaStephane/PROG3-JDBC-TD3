import java.time.Instant;
import java.util.Objects;

public class TableOrder {
    private Integer id;
    private Table table;
    private Instant arrivalDatetime;
    private Instant departureDatetime;

    public TableOrder() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Table getTable() { return table; }
    public void setTable(Table table) { this.table = table; }

    public Instant getArrivalDatetime() { return arrivalDatetime; }
    public void setArrivalDatetime(Instant arrivalDatetime) { this.arrivalDatetime = arrivalDatetime; }

    public Instant getDepartureDatetime() { return departureDatetime; }
    public void setDepartureDatetime(Instant departureDatetime) { this.departureDatetime = departureDatetime; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TableOrder)) return false;
        TableOrder that = (TableOrder) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(table, that.table) &&
                Objects.equals(arrivalDatetime, that.arrivalDatetime) &&
                Objects.equals(departureDatetime, that.departureDatetime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, table, arrivalDatetime, departureDatetime);
    }

    @Override
    public String toString() {
        return "TableOrder{" +
                "id=" + id +
                ", table=" + table +
                ", arrivalDatetime=" + arrivalDatetime +
                ", departureDatetime=" + departureDatetime +
                '}';
    }
}
