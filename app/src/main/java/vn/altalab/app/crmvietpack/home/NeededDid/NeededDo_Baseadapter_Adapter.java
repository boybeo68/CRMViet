package vn.altalab.app.crmvietpack.home.NeededDid;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.home.Home_NeededDo_Detail;

public class NeededDo_Baseadapter_Adapter extends BaseAdapter {

    Context context;
    int layout;
    ArrayList<NeededDo_Setget> LIST;
    String json;
    TextView tvDATE, tvTIME, tvDESCRIPTION;
    RelativeLayout rlDATE;

    public NeededDo_Baseadapter_Adapter(Context context, int layout, ArrayList<NeededDo_Setget> LIST, String json) {
        this.context = context;
        this.layout = layout;
        this.LIST = LIST;
        this.json = json;
    }

    @Override
    public int getCount() {
        return LIST.size();
    }

    @Override
    public Object getItem(int i) {
        return LIST.get(i);
    }

    @Override
    public long getItemId(int i) {
        return Long.parseLong(LIST.get(i).getTRANSACTION_ID());
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewrow = layoutInflater.inflate(layout, parent, false);

        tvDATE = (TextView) viewrow.findViewById(R.id.tvDATE);
        tvTIME = (TextView) viewrow.findViewById(R.id.tvTIME);
        tvDESCRIPTION = (TextView) viewrow.findViewById(R.id.tvDESCRIPTION);
        rlDATE = (RelativeLayout) viewrow.findViewById(R.id.rlDATE);


        tvDESCRIPTION.setSingleLine();
        tvDESCRIPTION.setSelected(true);

        try {

            String DATE = LIST.get(position).getDATE();

            if (!DATE.equals("")) {
                    if (!LIST.get(position).getWORK_COURT().equals(""))
                        tvDATE.setText(DATE.substring(8, 10).trim() + DATE.substring(4, 7).trim() + "-" + DATE.substring(0, 4).trim() + " (" + LIST.get(position).getWORK_COURT() + ")");
                    else {
                        rlDATE.setVisibility(View.INVISIBLE);
                        ViewGroup.LayoutParams layoutParams = rlDATE.getLayoutParams();
                        layoutParams.height = 2;
                        rlDATE.setLayoutParams(layoutParams);
                        tvDATE.setText("");
                    }
                    String date1 = LIST.get(position).getSTART_DATE().trim();
                    tvTIME.setText(date1.substring(11, 16).trim());
                }

            tvDESCRIPTION.setText(LIST.get(position).getTRANSACTION_NAME_TEXT());

        } catch (Exception e){
            e.printStackTrace();
        }

        viewrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    Intent intent = new Intent(context, Home_NeededDo_Detail.class);

                    intent.putExtra("TRANSACTION_ID",  LIST.get(position).getTRANSACTION_ID());
                    intent.putExtra("TRANSACTION_NAME_TEXT", LIST.get(position).getTRANSACTION_NAME_TEXT());
                    intent.putExtra("CUSTOMER_NAME", LIST.get(position).getCUSTOMER_NAME());
                    intent.putExtra("ASSIGNED_USER_NAME", LIST.get(position).getASSIGNED_USER_NAME());
                    intent.putExtra("TELEPHONE", LIST.get(position).getTELEPHONE());
                    intent.putExtra("START_DATE", LIST.get(position).getSTART_DATE());
                    intent.putExtra("END_DATE", LIST.get(position).getEND_DATE());
                    intent.putExtra("TRANSACTION_DESCRIPTION", LIST.get(position).getTRANSACTION_DESCRIPTION());
                    intent.putExtra("CUSTOMER_ID", LIST.get(position).getCUSTOMER_ID());
                    intent.putExtra("OFFICE_ADDRESS", LIST.get(position).getOFFICE_ADDRESS());
                    intent.putExtra("checkPhone", LIST.get(position).getContactPhone());
                    intent.putExtra("checkEmail",LIST.get(position).getContactEmail());
                    context.startActivity(intent);

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        return viewrow;
    }
}
