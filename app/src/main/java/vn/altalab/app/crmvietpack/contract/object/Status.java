package vn.altalab.app.crmvietpack.contract.object;

import java.io.Serializable;

/**
 * Created by boybe on 05/05/2017.
 */

public class Status  {
    private Integer statusId;
    private String statusName;

    public Status() {
    }

    public Status(Integer statusId, String statusName) {
        this.statusId = statusId;
        this.statusName = statusName;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
