package vn.altalab.app.crmvietpack.orders_fragment.order_detail;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.object.Product;

/**
 * Created by Tung on 3/14/2017.
 */

public class ChoseProductAdapter extends ArrayAdapter<Product> {
    customButtonListener customListner;

    public interface customButtonListener {
        public void onButtonClickListner(int position, String value);
    }

    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }


    private Context context;
    private LayoutInflater inflater;
    private int resource;
    private List<Product> productList;
    private static final String PREFS_NAME = "CRMVietPrefs";
    private SharedPreferences settings;
    private SimpleDateFormat simpleDateFormat;

    public ChoseProductAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Product> data) {
        super(context, resource, data);
        this.context = context;
        this.productList = data;
        this.resource = resource;

        if (settings == null) {
            settings = context.getSharedPreferences(PREFS_NAME, 0);
        }

        if (simpleDateFormat == null) {
            simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        }
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Product getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long) productList.get(position).getProductId();

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewHolder holder;

        if (convertView == null) {
//            inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(resource, parent, false);
            holder = new ViewHolder();
            holder.txtProductName = (TextView) convertView.findViewById(R.id.txtNameProduct);
            holder.CheckChose = (CheckBox) convertView.findViewById(R.id.checkboxChose);
            holder.soluongtv = (TextView) convertView.findViewById(R.id.soluong);
            holder.mota = (TextView) convertView.findViewById(R.id.tvMota);
            holder.price = (TextView) convertView.findViewById(R.id.tvPrice);
            holder.txtNSX = (TextView) convertView.findViewById(R.id.tvNhsx);
            holder.INVENTORY = (TextView) convertView.findViewById(R.id.tvINVENTORY);
            holder.imv = (ImageView) convertView.findViewById(R.id.imv);

            holder.CheckChose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int getPosition = (Integer) buttonView.getTag();
                    productList.get(getPosition).setSelected(buttonView.isChecked());
                }
            });

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Product product = productList.get(position);


        String link_image = product.getUrl_image();
        if (link_image.equals("")) {
            holder.imv.setImageResource(R.drawable.product_box);
        } else {
            Picasso.with(context)
                    .load(product.getUrl_image())
                    // chỉnh lại dung lượng của file ảnh cho giảm xuống.
                    .resize(50, 50)
                    .placeholder(R.drawable.product_box)
                    .error(R.drawable.product_box)
                    .into(holder.imv);
        }


        holder.txtProductName.setText(product.getProductName());
        if (!product.getDescription().equals("null")) {
            String a = String.valueOf(Html.fromHtml(product.getDescription()));
            String b[] = a.split("\n");
            holder.mota.setText(b[0]);
        }

        holder.price.setText(DecimalFormat.getInstance().format(Double.parseDouble(product.getPrice())));
        holder.INVENTORY.setText(product.getINVENTORY());


        String nsx = product.getProductManufactory();
        if (!nsx.equals("null")) {
            holder.txtNSX.setText(product.getProductManufactory());
        }

        holder.CheckChose.setTag(position);
        holder.CheckChose.setChecked(productList.get(position).isSelected());


//số lượng
        holder.soluongtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customListner != null) {
                    customListner.onButtonClickListner(position, String.valueOf(productList));
                }
            }
        });

        //set số lượng sản phẩm
//        if (product.getQuantity() == 0) {
//            holder.soluongtv.setText("1");
//        } else {
            holder.soluongtv.setText(String.valueOf(product.getQuantity()));
//        }

        return convertView;
    }

    private static class ViewHolder {
        TextView txtProductName;
        CheckBox CheckChose;
        TextView soluongtv;
        TextView txtNSX;
        TextView mota;
        TextView price;
        TextView INVENTORY;
        ImageView imv;
    }
}
