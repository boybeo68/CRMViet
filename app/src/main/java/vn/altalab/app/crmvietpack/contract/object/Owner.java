package vn.altalab.app.crmvietpack.contract.object;

import java.io.Serializable;

/**
 * Created by boybe on 05/06/2017.
 */

public class Owner implements Serializable  {
    private Integer contractOwnerId;
    private String contractOwnerName;

    public Owner() {
    }

    public Owner(Integer contractOwnerId, String contractOwnerName) {
        this.contractOwnerId = contractOwnerId;
        this.contractOwnerName = contractOwnerName;
    }

    public Integer getContractOwnerId() {
        return contractOwnerId;
    }

    public void setContractOwnerId(Integer contractOwnerId) {
        this.contractOwnerId = contractOwnerId;
    }

    public String getContractOwnerName() {
        return contractOwnerName;
    }

    public void setContractOwnerName(String contractOwnerName) {
        this.contractOwnerName = contractOwnerName;
    }
}
