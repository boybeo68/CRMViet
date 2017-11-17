package vn.altalab.app.crmvietpack.hanghoa.Adapter;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import io.realm.Realm;
import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.hanghoa.object.Goods;


/**
 * Created by boybe on 03/23/2017.
 */

public class HangHoaAdapter extends ArrayAdapter<Goods> {
    private static final String PREFS_NAME = "CRMVietPrefs";
    private Context context;
    private LayoutInflater inflater;
    private int resource;
    private List<Goods> goodsList;
    private Realm realm;
    private SharedPreferences setting;

    public HangHoaAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Goods> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.goodsList = objects;
        setting = context.getSharedPreferences(PREFS_NAME, 0);
        if (realm == null) {

            realm = Realm.getDefaultInstance();
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        XemHolder holder = null;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {

            convertView = inflater.inflate(resource, parent, false);
            holder = new HangHoaAdapter.XemHolder();


            holder.txtProductName = (TextView) convertView.findViewById(R.id.txtProductName);
            holder.txtManufactory = (TextView) convertView.findViewById(R.id.txtManufactory);
            holder.txtDescription = (TextView) convertView.findViewById(R.id.txtdescription);
            holder.txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imv);
            convertView.setTag(holder);

        } else {
            holder = (HangHoaAdapter.XemHolder) convertView.getTag();
        }
        final Goods goods = goodsList.get(position);

        holder.txtProductName.setSingleLine();
        holder.txtProductName.setSelected(true);
        holder.txtProductName.setText(goods.getProductName());
        if (!goods.getProductManufactory().equals(null)&&!goods.getProductManufactory().equals("null")) {
            holder.txtManufactory.setSingleLine();
            holder.txtManufactory.setSelected(true);
            holder.txtManufactory.setText(Html.fromHtml(goods.getProductManufactory()));
        }

        holder.txtDescription.setSingleLine();
        holder.txtDescription.setSelected(true);
        if (!goods.getProductDescription().equals(null)&& !goods.getProductDescription().equals("null")) {
            holder.txtDescription.setText(Html.fromHtml(goods.getProductDescription()));
        }
        if (goods.getProductPrice() != null ) {
            holder.txtPrice.setText(DecimalFormat.getInstance().format(Double.parseDouble(goods.getProductPrice())));
        }
        String link_image = goods.getImageUrl();
        if(!link_image.equals("")){
            Picasso.with(context)
                    .load(link_image)
                    // chỉnh lại dung lượng của file ảnh cho giảm xuống.
                    .resize(100, 100)
                    .placeholder(R.drawable.product_box)
                    .error(R.drawable.product_box)
                    .into(holder.imageView);
        }else {
            holder.imageView.setImageResource(R.drawable.product_box);
        }

        return convertView;
    }

    @Nullable
    @Override
    public Goods getItem(int position) {
        return goodsList.get(position);
    }

    @Override
    public int getCount() {
        return goodsList.size();
    }

    private static class XemHolder {
        TextView txtManufactory;
        TextView txtDescription;
        TextView txtPrice;
        TextView txtProductName;
        ImageView imageView;
    }
}
