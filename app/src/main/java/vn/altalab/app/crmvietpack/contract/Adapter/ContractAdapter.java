package vn.altalab.app.crmvietpack.contract.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import io.realm.Realm;
import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.contract.object.Contract;
import vn.altalab.app.crmvietpack.hanghoa.Adapter.HangHoaAdapter;
import vn.altalab.app.crmvietpack.hanghoa.object.Goods;

/**
 * Created by boybe on 04/28/2017.
 */

public class ContractAdapter extends ArrayAdapter<Contract> {
    private static final String PREFS_NAME = "CRMVietPrefs";
    private Context context;
    private LayoutInflater inflater;
    private int resource;
    private List<Contract> contractList;
    private Realm realm;
    private SharedPreferences setting;
    public ContractAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Contract> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.contractList=objects;
        setting = context.getSharedPreferences(PREFS_NAME, 0);
        if (realm == null) {

            realm = Realm.getDefaultInstance();
        }
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ContractAdapter.XemHolder holder=null;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {

            convertView = inflater.inflate(resource, parent, false);
            holder = new ContractAdapter.XemHolder();

            holder.txtContractCode = (TextView) convertView.findViewById(R.id.txtContractCode);
            holder.txtContractName = (TextView) convertView.findViewById(R.id.txtContractName);
            holder.txtStatusName = (TextView) convertView.findViewById(R.id.txtStatusName);
            holder.txtContractPrice = (TextView) convertView.findViewById(R.id.txtContractPrice);
            holder.txtDebt = (TextView) convertView.findViewById(R.id.txtDebt);
            holder.txtcontractOwnerName = (TextView) convertView.findViewById(R.id.txtContractOwner);
            holder.txtpaid = (TextView) convertView.findViewById(R.id.txtPaid);
            holder.txtCusName= (TextView) convertView.findViewById(R.id.txtContractCusName);

            convertView.setTag(holder);

        } else {
            holder = (ContractAdapter.XemHolder) convertView.getTag();
        }
        final Contract contract=contractList.get(position);
        holder.txtContractCode.setText(contract.getContractCode());
        if (contract.getContractName() != null) {
            holder.txtContractName.setText(contract.getContractName());
        }
        if (contract.getStatusName() != null) {
            holder.txtStatusName.setText(Html.fromHtml(contract.getStatusName()));
        }
        if (contract.getContractPrice() != null) {
            holder.txtContractPrice.setText(DecimalFormat.getInstance().format(Double.parseDouble(contract.getContractPrice())));
        }
        if (contract.getDebt() != null) {
            holder.txtDebt.setText(DecimalFormat.getInstance().format(Double.parseDouble(contract.getDebt())));
        }
        if (contract.getContractOwnerName() != null) {
            holder.txtcontractOwnerName.setText(Html.fromHtml(contract.getContractOwnerName()));
        }
        if (contract.getPaid() != null) {
            holder.txtpaid.setText(DecimalFormat.getInstance().format(Double.parseDouble(contract.getPaid())));
        }
        if (contract.getCustomerName() != null) {
            holder.txtCusName.setText(Html.fromHtml(contract.getCustomerName()));
        }
        return convertView;
    }

    @Nullable
    @Override
    public Contract getItem(int position) {
        return contractList.get(position);
    }

    @Override
    public int getCount() {
        return contractList.size();
    }

    private static class XemHolder{
        TextView txtContractCode;
        TextView txtContractName;
        TextView txtStatusName;
        TextView txtContractPrice;
        TextView txtDebt;
        TextView txtcontractOwnerName;
        TextView txtpaid;
        TextView txtCusName;
    }
}
