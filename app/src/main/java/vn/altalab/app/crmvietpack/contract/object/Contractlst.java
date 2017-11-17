package vn.altalab.app.crmvietpack.contract.object;

import java.io.Serializable;
import java.util.List;

import vn.altalab.app.crmvietpack.object.Product;

/**
 * Created by boybe on 06/07/2017.
 */

public class Contractlst implements Serializable {
    List<Contract> contracts;

    public Contractlst() {

    }

    public List<Contract> getContracts() {
        return contracts;
    }

    public void setContracts(List<Contract> contracts) {
        this.contracts = contracts;
    }
}
