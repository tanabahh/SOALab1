package model;

public class QueryAdditions {
    public TableQueryAdditions table;
    public String column;
    public String firstValue;
    public Boolean isAsc;

    public QueryAdditions(TableQueryAdditions table, String column, Boolean isAsc) {
        this.column = column;
        this.table = table;
        this.firstValue = null;
        this.isAsc = isAsc;
    }

    public QueryAdditions(TableQueryAdditions table, String column,
        String firstValue) {
        this.column = column;
        this.table = table;
        this.firstValue = firstValue;;
    }
}
