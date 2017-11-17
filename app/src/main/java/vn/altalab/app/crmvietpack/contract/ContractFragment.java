package vn.altalab.app.crmvietpack.contract;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
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

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;
import io.realm.Realm;
import vn.altalab.app.crmvietpack.CrmMainActivity;
import vn.altalab.app.crmvietpack.R;
import vn.altalab.app.crmvietpack.contract.Adapter.ContractAdapter;
import vn.altalab.app.crmvietpack.contract.object.Contract;
import vn.altalab.app.crmvietpack.utility.MySingleton;

/**
 * Created by boybe on 04/28/2017.
 */

public class ContractFragment extends Fragment {
    private static final String PREFS_NAME = "CRMVietPrefs";
    public static List<Contract> dscontract; // bên dưới xử lý
    public static ContractAdapter contractAdapter;
    private PullToRefreshListView lvContract; // trên giao diện
    private View mLayoutView;
    private Realm realm;
    private SharedPreferences settings;
    private ProgressDialog progressDialog;
    private Button btnFind;
    private EditText edtFind;
    private LinearLayout lnFind;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contract, container, false);
        CrmMainActivity.toolbar.setSubtitle("Hợp đồng");
        if (settings == null) {
            settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        }

        if (realm == null) {
            realm = Realm.getDefaultInstance();
        }
        lnFind = (LinearLayout) view.findViewById(R.id.lnFind);

        if (dscontract == null) {
            dscontract = new ArrayList<>();
        }
        if (contractAdapter == null) {
            contractAdapter = new ContractAdapter(getActivity(), R.layout.list_item_contract, dscontract);
        }
        lvContract = (PullToRefreshListView) view.findViewById(R.id.lstContract);
        lvContract.setAdapter(contractAdapter);
        lvContract.setMode(PullToRefreshBase.Mode.BOTH);
        lvContract.getRefreshableView().setDividerHeight(5);
        // addControls Tìm kiếm
        btnFind = (Button) view.findViewById(R.id.btnFind);
        edtFind = (EditText) view.findViewById(R.id.edtFind);


        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
        }
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Loading..");

        doGetContractAPI(0);
        lvContract.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                doGetContractAPI(0);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if ("".equals(settings.getString("api_server", ""))) {
                    Toast.makeText(refreshView.getContext(), refreshView.getContext().getResources().getString(R.string.login_empty_api_server), Toast.LENGTH_SHORT).show();
                    return;
                }
                doGetContractAPI(dscontract.size());
            }
        });
        lvContract.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailContract.class);
                Bundle bundle = new Bundle();
                bundle.putLong("CONTRACT_ID", dscontract.get(position - 1).getContractID());
                bundle.putString("CONTRACT_NAME", dscontract.get(position - 1).getContractName());
                bundle.putString("CONTRACT_CODE", dscontract.get(position - 1).getContractCode());
                bundle.putString("STATUS_NAME", dscontract.get(position - 1).getStatusName());
                bundle.putString("CONTRACT_PRICE", dscontract.get(position - 1).getContractPrice());
                bundle.putString("PAID", dscontract.get(position - 1).getPaid());
                bundle.putString("DEBT", dscontract.get(position - 1).getDebt());

                bundle.putString("CONTRACT_OWNER_NAME", dscontract.get(position - 1).getContractOwnerName());

                bundle.putString("CUSTOMER_NAME", dscontract.get(position - 1).getCustomerName());
                bundle.putLong("customer_id", dscontract.get(position - 1).getCustomerId());
                bundle.putString("startDate", dscontract.get(position - 1).getStartDate());
                bundle.putString("endDate", dscontract.get(position - 1).getEndDate());

                bundle.putInt("Status", dscontract.get(position - 1).getStatus());
                bundle.putInt("CONTRACT_OWNER_ID", dscontract.get(position - 1).getContractOwnerId());
                bundle.putInt("position",position);

                intent.putExtras(bundle);
                startActivityForResult(intent,4);
            }
        });
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabContract);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddContract.class);
                intent.putExtra("contract", "text");
                startActivityForResult(intent, 4);
            }
        });
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dscontract.clear();
                progressDialog.show();
                doGetContractSearch(0);
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        });

        edtFind.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    dscontract.clear();
                    progressDialog.show();
                    doGetContractSearch(0);
                    //tắt bàn phím sau khi click
                    final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 4) {
            doGetContractAPI(0);
        }
        if (requestCode == 5) {
            if(data.getIntExtra("position",0)!=0){
                int position = data.getIntExtra("position",0);
                dscontract.remove(position-1);
                contractAdapter.notifyDataSetChanged();
            }
        }
    }

    public static String KeyAccent(String s) {
        s = s.replace("đ", "d");
        s = s.replace("Đ", "D");

        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }

    private void doGetContractSearch(final int index) {
        if (index == 0) {
            dscontract.clear();
        }

        String url = settings.getString("api_server", "") + "/api/v1/contract/search?USER_ID=" + settings.getString(getResources().getString(R.string.user_id_object), "")
                + "&keyword=" + KeyAccent(edtFind.getText().toString()) + "&index=" + index;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (jsonObject != null && !"".equals(jsonObject.toString()) && "success".equals(jsonObject.optString("messages"))) {

                            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                            JSONArray array = jsonObject.optJSONArray("data");
                            for (int i = 0; i < array.length(); i++) {
                                final JSONObject object = array.optJSONObject(i);
                                int contractId = object.optInt("CONTRACT_ID");
                                Contract contract = new Contract();
                                contract.setContractID((int) object.optLong("CONTRACT_ID"));
                                contract.setContractID(contractId);
                                contract.setContractName(object.optString("CONTRACT_NAME"));
                                contract.setContractCode(object.optString("CONTRACT_CODE"));
                                contract.setStatusName(object.optString("STATUS_NAME"));
                                contract.setCustomerId(object.optLong("CUSTOMER_ID"));


                                contract.setStatus(object.optInt("STATUS"));
                                contract.setContractOwnerId(object.optInt("CONTRACT_OWNER"));


                                contract.setContractPrice(object.optString("CONTRACT_PRICE"));
                                contract.setPaid(object.optString("PAID"));
                                contract.setDebt(object.optString("DEBT"));
                                contract.setContractOwnerName(object.optString("CONTRACT_OWNER_NAME"));


                                contract.setCustomerName(object.optString("CUSTOMER_NAME"));
                                String a = object.optString("START_DATE");
                                if (a.equals(null) || a.equals("null") || a.equals("") || a.equals("0000-00-00 00:00:00")) {
                                    contract.setStartDate("");
                                } else {
                                    String b[] = a.split("-");
                                    String b2[] = b[2].split(" ");
                                    contract.setStartDate(b2[0] + "/" + b[1] + "/" + b[0]);
                                }

                                String a1 = object.optString("END_DATE");
                                if (a1.equals(null) || a1.equals("null") || a1.equals("") || a1.equals("0000-00-00 00:00:00")) {
                                    contract.setEndDate("");
                                } else {
                                    String b[] = a1.split("-");
                                    String b2[] = b[2].split(" ");
                                    contract.setEndDate(b2[0] + "/" + b[1] + "/" + b[0]);
                                }
                                dscontract.add(contract);

                            }
                            if (dscontract.size() == 0) {
                                if (progressDialog != null && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                Toasty.normal(getActivity(), "Hợp đồng không tồn tại !", Toast.LENGTH_LONG).show();
                            }
                            contractAdapter.notifyDataSetChanged();
                        }

                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        if (lvContract.isRefreshing()) {
                            lvContract.onRefreshComplete();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (lvContract.isRefreshing()) {
                    lvContract.onRefreshComplete();
                }
                Log.e("loadContract" + "Lỗi\n", volleyError.toString());
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

    private void doGetContractAPI(final int index) {
        progressDialog.show();
        if (index == 0) {
            dscontract.clear();
        }
        String url = settings.getString("api_server", "") + "/api/v1/contracts?USER_ID=" + settings.getString("userId", "") + "&index=" + index;

        //JsonRequest(Method Post or Get -> Params -> listener )/ Error/ Header
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                if (jsonObject != null && !"".equals(jsonObject.toString())
                        && "success".equals(jsonObject.optString("messages"))) {
                    JSONArray array = jsonObject.optJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        final JSONObject object = array.optJSONObject(i);
                        boolean check = false;
                        int contractId = object.optInt("CONTRACT_ID");
                        if (dscontract.size() > 0) {
                            for (Contract cc : dscontract) {
                                if (cc != null && cc.getContractID() != null && !"".equals(cc.getContractName())) {
                                    if (cc.getContractID() == contractId) {
                                        check = true;
                                        break;
                                    }
                                }
                            }
                        }
                        if (!check) {
                            Contract contract = new Contract();
                            contract.setContractID((int) object.optLong("CONTRACT_ID"));
                            contract.setContractID(contractId);
                            contract.setContractName(object.optString("CONTRACT_NAME"));
                            contract.setContractCode(object.optString("CONTRACT_CODE"));
                            contract.setStatusName(object.optString("STATUS_NAME"));

                            contract.setCustomerId(object.optLong("CUSTOMER_ID"));

                            contract.setStatus(object.optInt("STATUS"));
                            contract.setContractOwnerId(object.optInt("CONTRACT_OWNER"));


                            contract.setContractPrice(object.optString("CONTRACT_PRICE"));
                            contract.setPaid(object.optString("PAID"));
                            contract.setDebt(object.optString("DEBT"));
                            contract.setContractOwnerName(object.optString("CONTRACT_OWNER_NAME"));

                            contract.setCustomerName(object.optString("CUSTOMER_NAME"));
                            String a = object.optString("START_DATE");
                            if (a.equals(null) || a.equals("null") || a.equals("") || a.equals("0000-00-00 00:00:00")) {
                                contract.setStartDate("");
                            } else {
                                String b[] = a.split("-");
                                String b2[] = b[2].split(" ");
                                contract.setStartDate(b2[0] + "/" + b[1] + "/" + b[0]);
                            }

                            String a1 = object.optString("END_DATE");
                            if (a1.equals(null) || a1.equals("null") || a1.equals("") || a1.equals("0000-00-00 00:00:00")) {
                                contract.setEndDate("");
                            } else {
                                String b[] = a1.split("-");
                                String b2[] = b[2].split(" ");
                                contract.setEndDate(b2[0] + "/" + b[1] + "/" + b[0]);
                            }
                            dscontract.add(contract);
                        }
                    }
                    contractAdapter.notifyDataSetChanged();
                }
                if (lvContract.isRefreshing()) {
                    lvContract.onRefreshComplete();
                }
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (lvContract.isRefreshing()) {
                    lvContract.onRefreshComplete();
                }
                Log.e("getContract", volleyError.toString());
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
