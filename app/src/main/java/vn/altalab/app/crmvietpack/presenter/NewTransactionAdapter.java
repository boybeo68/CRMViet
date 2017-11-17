package vn.altalab.app.crmvietpack.presenter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.model.UsersModel;
import vn.altalab.app.crmvietpack.object.Transaction;
import vn.altalab.app.crmvietpack.object.Users;

/**
 * Created by Simple on 11/25/2016.
 */

public class NewTransactionAdapter extends ArrayAdapter<Transaction> {

    private LayoutInflater inflater;
    private int resource;
    private Context context;
    private List<Transaction> transactions;
    private Realm realm;

    public NewTransactionAdapter(Context context, int resource, List<Transaction> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        this.transactions = objects;

        if (realm == null) {
            realm = Realm.getDefaultInstance();
        }
    }

    @Override
    public int getCount() {
        return transactions.size();
    }

    @Nullable
    @Override
    public Transaction getItem(int position) {
        return transactions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getPosition(Transaction item) {
        return transactions.indexOf(item);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(resource, parent, false);
            holder = new ViewHolder();
            holder.imageView = (TextView) convertView.findViewById(R.id.imgv);
            holder.txtNewTransName = (TextView) convertView.findViewById(R.id.txtNewTransName);
            holder.txtNewTransAssigner = (TextView) convertView.findViewById(R.id.txtNewTransAssigner);
            holder.txtNewTransDateFinish = (TextView) convertView.findViewById(R.id.txtNewTransDateFinish);
            holder.txtNewTransTypeId = (TextView) convertView.findViewById(R.id.txtNewTransType);
//            holder.txtNewTransShared = (TextView) convertView.findViewById(R.id.txtNewTransShared);
            holder.txtNewTransStartDate = (TextView) convertView.findViewById(R.id.txtNewTransStartDate);
            holder.txtNewTransUserAssigned = (TextView) convertView.findViewById(R.id.txtNewTransUserAssigned);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Transaction transaction = transactions.get(position);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        holder.txtNewTransName.setText(Html.fromHtml(transaction.getTransactionName()));
        if (transaction.getStartDate() != null) {
            holder.txtNewTransStartDate.setText(format.format(transaction.getStartDate()));
        }
        if (transaction.getEndDate() != null) {
            holder.txtNewTransDateFinish.setText(format.format(transaction.getEndDate()));
        }
        if(transaction.getTransactionTypeId()==-1){
            holder.txtNewTransTypeId.setText("Giao dịch gọi điện tổng đài");
        }
        if(transaction.getTransactionTypeId()==1){
            holder.txtNewTransTypeId.setText("GD1_Gọi điện thoại");
        }
        if(transaction.getTransactionTypeId()==2){
            holder.txtNewTransTypeId.setText("GD2_Gặp mặt khách hàng");
        }
        if(transaction.getTransactionTypeId()==3){
            holder.txtNewTransTypeId.setText("GD3_Đàm phán");
        }
        if(transaction.getTransactionTypeId()==4){
            holder.txtNewTransTypeId.setText("GD4_Kí hợp đồng");
        }
        if(transaction.getTransactionTypeId()==5){
            holder.txtNewTransTypeId.setText("GD5_CSKH Sau bán");
        }
        if(transaction.getTransactionTypeId()==6){
            holder.txtNewTransTypeId.setText("GD6_Hoàn thành thanh toán");
        }
        if(transaction.getTransactionTypeId()==7){
            holder.txtNewTransTypeId.setText("GD7_Khác");
        }
        UsersModel usersModel = new UsersModel();
        Users users = usersModel.getUserById(realm, transaction.getRegUser());
        holder.txtNewTransAssigner.setText(users.getUserName());

        if (transaction.getUsers() != null) {
            users = usersModel.getUserById(realm, transaction.getUsers().getUserId());
            holder.txtNewTransUserAssigned.setText(users.getUserName());
        }

        if(transaction.getStatus()==1){
            holder.imageView.setBackgroundColor(Color.parseColor("#8B6914"));
            holder.imageView.setText("Chưa thực hiện");
        }
        if(transaction.getStatus()==2){
            holder.imageView.setBackgroundColor(Color.parseColor("#D2691E"));
            holder.imageView.setText("Đang thực hiện");
        }
        if(transaction.getStatus()==4){
            holder.imageView.setBackgroundColor(Color.parseColor("#0000FF"));
            holder.imageView.setText("Đã giải quyết");
        }
        if(transaction.getStatus()==3){
            holder.imageView.setBackgroundColor(Color.parseColor("#006400"));
            holder.imageView.setText("Đã hoàn thành");
        }
//        if (transaction.getCheckShare() != null && !"".equals(transaction.getCheckShare())) {
//            StringBuilder builder = usersModel.getUserBuilder(realm, transaction.getCheckShare());
//            holder.txtNewTransShared.setText(context.getResources().getString(R.string.transaction_share).concat(": ").concat(builder.toString()));
//        }

        return convertView;
    }

    private class ViewHolder {
        TextView txtNewTransName;
        TextView txtNewTransStartDate;
        TextView txtNewTransDateFinish;
        TextView txtNewTransAssigner;
        TextView txtNewTransUserAssigned;
        //        TextView txtNewTransShared;
        TextView imageView;
        TextView txtNewTransTypeId;
    }
}
