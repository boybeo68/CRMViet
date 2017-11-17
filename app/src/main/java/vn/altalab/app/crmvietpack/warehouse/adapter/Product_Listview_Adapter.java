package vn.altalab.app.crmvietpack.warehouse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.warehouse.object.Product_Setget;

public class Product_Listview_Adapter extends BaseAdapter {

    Context context;
    ArrayList<Product_Setget> listProduct;
    int layout;

    public Product_Listview_Adapter(Context context, int layout, ArrayList<Product_Setget> listProduct) {
        this.layout = layout;
        this.listProduct = listProduct;
        this.context = context;
    }

    public class view_Holder {

        ImageView ivWare;
        TextView tvIdProduct, tvNameProduct, tvAmountProduct, tvWareHouse;

    }

    @Override
    public int getCount() {
        return listProduct.size();
    }

    @Override
    public Object getItem(int i) {
        return listProduct.get(i);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

//    @Override
//    public long getItemId(int i) {
//        return Long.parseLong(listProduct.get(i).getPRODUCT_CODE());
//    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewRow = convertView;

        if (viewRow == null) {
            viewRow = layoutInflater.inflate(layout, viewGroup, false);
            view_Holder viewHolder = new view_Holder();

            viewHolder.tvIdProduct = (TextView) viewRow.findViewById(R.id.tvIdProduct);
            viewHolder.tvNameProduct = (TextView) viewRow.findViewById(R.id.tvNameProduct);
            viewHolder.tvAmountProduct = (TextView) viewRow.findViewById(R.id.tvAmountProduct);
            viewHolder.tvWareHouse = (TextView) viewRow.findViewById(R.id.tvWareHouse);
            viewHolder.ivWare = (ImageView) viewRow.findViewById(R.id.ivWare);

            viewRow.setTag(viewHolder);
        }

        view_Holder viewHolder = (view_Holder) viewRow.getTag();

        viewHolder.tvIdProduct.setText(listProduct.get(position).getPRODUCT_CODE());
        viewHolder.tvNameProduct.setText(listProduct.get(position).getPRODUCT_NAME());
        viewHolder.tvAmountProduct.setText(listProduct.get(position).getINVENTORY());
        viewHolder.tvWareHouse.setText(listProduct.get(position).getWAREHOUSE_NAME());
        String link_image = listProduct.get(position).getLINK_IMAGE();
        if(!link_image.equals("")){
            Picasso.with(context)
                    .load(link_image)
                    // chỉnh lại dung lượng của file ảnh cho giảm xuống.
                    .resize(100, 100)
                    .placeholder(R.drawable.product_box)
                    .error(R.drawable.product_box)
                    .into(viewHolder.ivWare);
        }else {
            viewHolder.ivWare.setImageResource(R.drawable.product_box);
        }

        return viewRow;
    }
}
