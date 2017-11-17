package vn.altalab.app.crmvietpack.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
 * Created by Tung on 3/7/2017.
 */

public class ProductAdapter extends ArrayAdapter {
    private Context context;
    private LayoutInflater inflater;
    private int resource;
    private List<Product> productList;
    private static final String PREFS_NAME = "CRMVietPrefs";
    private SharedPreferences settings;
    private SimpleDateFormat simpleDateFormat;

    public ProductAdapter(Context context, int resource, List<Product> data) {

        super(context, resource, data);
        this.context = context;
        this.productList = data;
        this.resource = resource;

        if (settings == null) {
            settings = context.getSharedPreferences(PREFS_NAME, 0);
        }

        if (simpleDateFormat == null) {
            simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
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
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ProductAdapter.ViewHolder holder = null;
        if (convertView == null) {
            inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(resource, parent, false);
            holder = new ProductAdapter.ViewHolder();

            holder.txtProductName = (TextView) convertView.findViewById(R.id.tvNameProduct);
            holder.txtNSX = (TextView) convertView.findViewById(R.id.tvNhsx);
            holder.txtMota = (TextView) convertView.findViewById(R.id.tvMota);
            holder.txtPrice = (TextView) convertView.findViewById(R.id.tvPrice);
            holder.txtSoluong = (TextView) convertView.findViewById(R.id.tvSoluong);
            holder.img = (ImageView) convertView.findViewById(R.id.imv);


            convertView.setTag(holder);
        } else {
            holder = (ProductAdapter.ViewHolder) convertView.getTag();
        }

        final Product product = productList.get(position);

        holder.txtProductName.setText(product.getProductName());

        String link_image = product.getUrl_image();
        if (link_image.equals("")) {
            holder.img.setImageResource(R.drawable.product_box);
        } else {
            Picasso.with(context)
                    .load(product.getUrl_image())
                    // chỉnh lại dung lượng của file ảnh cho giảm xuống.
                    .resize(50, 50)
                    .placeholder(R.drawable.product_box)
                    .error(R.drawable.product_box)
                    .into(holder.img);
        }


        String a1 = product.getProductManufactory();
        if (!a1.equals("null")) {
            holder.txtNSX.setText(product.getProductManufactory());
        }

        if(!product.getDescription().equals("null")){
            String a = String.valueOf(Html.fromHtml(product.getDescription()));
            String b[] = a.split("\n");
            holder.txtMota.setText(b[0]);

        }


        holder.txtPrice.setText(DecimalFormat.getInstance().format(Double.parseDouble(product.getPrice())));


        holder.txtSoluong.setText(String.valueOf(product.getQuantity()));


        return convertView;
    }


    private static class ViewHolder {
        TextView txtProductName;
        TextView txtNSX;
        TextView txtMota;
        TextView txtPrice;
        TextView txtSoluong;
        ImageView img;

    }
}
