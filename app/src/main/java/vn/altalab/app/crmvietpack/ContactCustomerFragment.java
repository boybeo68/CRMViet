package vn.altalab.app.crmvietpack;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.realm.Realm;
import vn.altalab.app.crmvietpack.object.ContactCustomer;
import vn.altalab.app.crmvietpack.presenter.ContactCustomerAdapter;
import vn.altalab.app.crmvietpack.utility.MySingleton;

public class ContactCustomerFragment extends Fragment {
    private final static int REQUEST_CODE = 1;

    public static List<ContactCustomer> contactCustomers;
    public static ContactCustomerAdapter contactCustomerAdapter;
    //pull to refresh page
    private PullToRefreshListView lstContactCustomer;
    private View mLayoutView;
    private Realm realm;

    private static final String PREFS_NAME = "CRMVietPrefs";
    private SharedPreferences settings;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_customer, container, false);
        CrmMainActivity.toolbar.setSubtitle(getActivity().getResources().getString(R.string.contact_customer));

        if (settings == null) {
            settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        }

        if (realm == null) {
            realm = Realm.getDefaultInstance();
        }

        //Layout
        mLayoutView = view.findViewById(R.id.layout_list_contact_customer);
        mLayoutView.setVisibility(View.VISIBLE);
        mLayoutView.invalidate();

        if (contactCustomers == null) {
            contactCustomers = new ArrayList<>();
        }
        if (contactCustomerAdapter == null) {
            contactCustomerAdapter = new ContactCustomerAdapter(getActivity(), R.layout.new_list_contact_customer, contactCustomers);
        }
        //initiate PulltoRefreshListView
        lstContactCustomer = (PullToRefreshListView) view.findViewById(R.id.lstContactCustomer);
        //show Progress
        /*showProgress(true);*/
        //get data via API
//        doGetContactCustomersAPI();
        //initiate ContactCustomerAdapter & set its list of items as list_item that's assigned to the realmContactCustomers above!
        // set Adapter for pullToView
        lstContactCustomer.setAdapter(contactCustomerAdapter);
        lstContactCustomer.setMode(PullToRefreshBase.Mode.BOTH);
        lstContactCustomer.getRefreshableView().setDividerHeight(1);
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
        }
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        doGetContactCustomersAPI(0);
        // anything changes will make the adapter change immediately

        //showProgress(false);
        lstContactCustomer.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                doGetContactCustomersAPI(0);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if ("".equals(settings.getString("api_server", ""))) {
                    Toast.makeText(refreshView.getContext(), refreshView.getContext().getResources().getString(R.string.login_empty_api_server), Toast.LENGTH_SHORT).show();
                    return;
                }
//                showProgress(true);
                doGetContactCustomersAPI(contactCustomers.size());

            }
        });
        lstContactCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabContact);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                Intent intent = new Intent(getActivity(), Customer_Contact_Create_Activity.class);
//                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                doGetContactCustomersAPI(0);
                Toast.makeText(getActivity(), getResources().getString(R.string.create_customer_success), Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void doGetContactCustomersAPI(final int index) {
            String url = settings.getString("api_server", "") + "/api/v1/contacts?USER_ID=" + settings.getString(getResources().getString(R.string.user_id_object), "") + "&index=" + index;

        //JsonRequest(Method Post or Get -> Params -> listener )/ Error/ Header
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                if (jsonObject != null && !"".equals(jsonObject.toString()) && "success".equals(jsonObject.optString("messages"))) {
                    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

                    JSONArray array = jsonObject.optJSONArray(getActivity().getResources().getString(R.string.contacts));
                    for (int i = 0; i < array.length(); i++) {
                        final JSONObject object = array.optJSONObject(i);

                        boolean check = false;
                        int contactId = object.optInt(getActivity().getResources().getString(R.string.contact_customer_id_db));
                        if (contactCustomers.size() > 0) {
                            for (ContactCustomer cc : contactCustomers) {
                                if (cc != null && cc.getContactId() != null && !"".equals(cc.getContactFullName())) {
                                    if (cc.getContactId() == contactId) {
                                        check = true;
                                        break;
                                    }
                                }
                            }
                        }
                        if (!check) {
                            ContactCustomer contactCustomer = new ContactCustomer();
                            contactCustomer.setCustomerId(object.optLong(getActivity().getResources().getString(R.string.customer_id_db)));
                            contactCustomer.setContactId(contactId);
                            contactCustomer.setContactEmail(object.optString(getActivity().getResources().getString(R.string.contact_customer_email_db)));
                            contactCustomer.setContactFullName(object.optString(getActivity().getResources().getString(R.string.contact_customer_full_name_db)));
                            contactCustomer.setContactMobiphone(object.optString(getActivity().getResources().getString(R.string.contact_customer_mobiphone_db)));
                            contactCustomer.setContactWorkphone(object.optString(getActivity().getResources().getString(R.string.contact_customer_workphone_db)));
                            contactCustomer.setContactAddress(object.optString(getActivity().getResources().getString(R.string.contact_customer_address_db)));
                            contactCustomer.setNickChat(object.optString(getActivity().getResources().getString(R.string.contact_customer_nickchat_db)));
                            contactCustomer.setGender(object.optInt(getActivity().getResources().getString(R.string.contact_customer_gender_db)));


                            try {
                                contactCustomer.setBirthday(simpleDateFormat.parse(object.optString(getActivity().getResources().getString(R.string.contact_customer_birthday))));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                            contactCustomers.add(contactCustomer);
                        }
                    }
                    contactCustomerAdapter.notifyDataSetChanged();
                }
                if (lstContactCustomer.isRefreshing()) {
                    lstContactCustomer.onRefreshComplete();
                }
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (lstContactCustomer.isRefreshing()) {
                    lstContactCustomer.onRefreshComplete();
                }
                Log.e("getContactCustomer", volleyError.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=UTF-8");
                headers.put("User-agent", System.getProperty("http.agent"));
                return headers;
            }
        };

        int socketTimeout = 60000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

}
