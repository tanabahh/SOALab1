package model;

import java.io.Serializable;
import java.util.Date;

public class GroupByCreationDateDto implements Serializable {
    private Date creationDate;
    private long count;

    public GroupByCreationDateDto(){};

    public GroupByCreationDateDto(Date creationDate, long count) {
        this.count = count;
        this.creationDate = creationDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

}
