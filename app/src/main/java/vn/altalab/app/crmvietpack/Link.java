package vn.altalab.app.crmvietpack;

import android.content.Context;

public class Link {

    public static String Domain = "http://demo.altalab.vn:8080/altaapi/api/v1";
    public static String Id = "1";

    String Start_Date_Query = "";
    public static String PREFS_NAME = "CRMVietPrefs";

    Context context;
    Shared_Preferences sharedPreferences;

    public static String POSITION_ID = "/position";

    private String home_Overview_Paid_Link = Domain + "/payment/days?USER_ID=1&index=0&type=1";
    private String home_Overview_Customer_Link = Domain + "/customer/days/total?USER_ID=1&index=0&type=1";
    private String home_Overview_PaidEmployees_Link = Domain + "/revenue/days?USER_ID=1&index=0&type=1";
    private String home_Overview_Job_Link = Domain + "/transaction/in/out?USER_ID=1&index=0&type=1";

    private String home_Needed_Link = Domain + "/transaction/assigned/7days?USER_ID=1&index=0&type=3";
    private String home_Deathline_Link = Domain + "/transaction/assign/out?USER_ID=1&index=0&type=3";
    private String home_Consigned_Link = Domain + "/transaction/assign?USER_ID=1&index=0&type=3";

    // TYPE
    private String Type_Link = "/transaction/type";

    // CUSTOMER
    private String customer_Link = "/customer/potential?index=0&USER_ID=1&potential_status=0";

    // CUSTOMER Search
    private String customer_Search_Link = "/customers?keyword=abc&USER_ID=1";

    // TRANSACTION SEARCH
    private String transaction_Search_Link = "/transactions/search?USER_ID=2&keyword=gọi điện thuyết phục&index=0";

    // CUSTOMER Creation
    private String CUSTOMER_CREATION_link = "/customers/add";

    // CUSTOMER Detail
    private String customerDetail_Infomation_Link = "/customers/single/1";
    private String customerDetail_Transaction_Link = "/customer/transaction/1?index=0";
    private String customerDetail_Contact_Link = "/customers/contact/1?index=0";

    // CUSTOMERDETAIL EditCreat
    private String CUSTOMERDETAIL_InfomationEditation_Link = "/customers/edit";
    private String CUSTOMERDETAIL_ContactAdditon_Link = "/contact/add";
    private String CUSTOMERDETAIL_ContactEditation_POSTLINK = "/contact/edit";
    private String CUSTOMERDETAIL_TransactionAdditon_Link = "/transaction/add";
    // PRODUCT CREATE
    private String PRODUCT_CREATE_LINK="/products/add";
    // CUSTOMERDETAIL CONTACTDETAIL
    private String CUSTOMERDETAIL_CONTACTDETAIL = "/contact/1";

    // transactions
    private String transaction = "/transactions";
    private String transactionDetail = "/transaction/1";
    private String transactionEdit = "/transaction/edit";

    // TRANSACTION STATUS
    private String TRANSACTION_STATUS_LINK = "/transaction/status";

    // warehouse
    private String warehouse_Link = "/warehouses/1";
    private String product_Link = "/warehouses/1";
    private String approval_Notification = "/notifications/appr?user_id=1&index_search=0";
    private String transaction_Notification = "/notifications/trans?user_id=1&index_search=0";

    public Link(Context context) {
        this.context = context;

        sharedPreferences = new Shared_Preferences(context, PREFS_NAME);
        Id = sharedPreferences.getString(context.getString(R.string.user_id_object));
        Domain = sharedPreferences.getString("api_server") + "/api/v1";

        Start_Date_Query = sharedPreferences.getString("start_date_query");

    }

    public Link() {
    }

    public String getId() {
        return Id;
    }

    public String get_TYPE() {
        Type_Link = Domain + "/transaction/type";
        return Type_Link;
    }

    public String get_files_transactions(String transId) {
        Type_Link = Domain + "/files_transactions/"+transId;
        return Type_Link;
    }

    public String files_upload_detailTransaction() {
        Type_Link = Domain + "/files_upload_detailTransaction";
        return Type_Link;
    }
    public String files_upload_detailContract() {
        Type_Link = Domain + "/files_upload_detailContract";
        return Type_Link;
    }
    public String files_upload_detailCustomer() {
        Type_Link = Domain + "/files_upload_detailCustomer";
        return Type_Link;
    }
    public String get_POSITION_ID() {
        POSITION_ID = Domain + "/position";
        return POSITION_ID;
    }

    // Home
    public String get_Home_Needed_Link(int index) {
        home_Needed_Link = Domain + "/transaction/assigned/7days?USER_ID=" + Id + "&index=" + index + "&type=2";
        return home_Needed_Link;
    }

    public String get_Home_Consigned_Link(int index) {
        home_Consigned_Link = Domain + "/transaction/assign?USER_ID=" + Id + "&index=" + index + "&type=3";
        return home_Consigned_Link;
    }

    public String get_Home_Deathline_Link(int index) {
        home_Deathline_Link = Domain + "/transaction/assign/out?USER_ID=" + Id + "&index=" + index + "&type=3";
        return home_Deathline_Link;
    }

    public String get_Home_Overview_Paid_Link(int type) {
        home_Overview_Paid_Link = Domain + "/payment/days?USER_ID=" + Id + "&index=0&type=" + type;
        return home_Overview_Paid_Link;
    }

    public String get_Home_Overview_Customer_Link(int type) {
        home_Overview_Customer_Link = Domain + "/customer/days/total?USER_ID=" + Id + "&index=0&type=" + type;
        return home_Overview_Customer_Link;
    }

    public String get_Home_Overview_PaidEmployees_Link(int position, int type) {
        home_Overview_PaidEmployees_Link = Domain + "/revenue/days?USER_ID=" + Id + "&index=" + position + "&type=" + type;
        return home_Overview_PaidEmployees_Link;
    }

    public String get_Home_Overview_Job_Link(int position, int type) {
        home_Overview_Job_Link = Domain + "/transaction/in/out?USER_ID=" + Id + "&index=" + position + "&type=" + type;
        return home_Overview_Job_Link;
    }

    // CUSTOMER
    public String GET_CUSTOMER_LINK(int index, String potential_status) {
        if (!potential_status.equals(""))
            customer_Link = Domain + "/customer/potential?index=" + index + "&USER_ID=" + Id + "&potential_status=" + potential_status;
        else customer_Link = Domain + "/customer/potential?index=" + index + "&USER_ID=" + Id;
        return customer_Link;
    }

    // CUSTOMER SEARCH
    public String GET_SEARCH_LINK(String keyword, int potential_status ,int index, int index2) {
        customer_Search_Link = Domain + "/find/customers?USER_ID=" + Id +"&potential_status="+potential_status
        +"&keyword=" + keyword + "&index=" + index+"&index2="+index2;
        return customer_Search_Link;
    }

    public String GET_SEARCH_LINK_ALL(String keyword,int index, int index2) {
        customer_Search_Link = Domain + "/find/customers?USER_ID=" + Id +"&keyword=" + keyword + "&index=" + index+"&index2="+index2;
        return customer_Search_Link;
    }
    // TRANSACTION SEARCH

    public String GET_TRANSACTIONSEARCH_LINK(String keyword, int index) {
        transaction_Search_Link = Domain + "/transactions/search?USER_ID="+Id+"&keyword="+keyword+"&index=" + index;
        return transaction_Search_Link;
    }


    // CUSTOMER CREATTION
    public String post_CUSTOMERCREATION_link(){
        CUSTOMER_CREATION_link = Domain + "/customers/add";
        return CUSTOMER_CREATION_link;
    }

    // CUSTOMERDETAIL
    public String get_CUSTOMERDETAIL_Infomation_Link(long customerId){
        customerDetail_Infomation_Link = Domain + "/customers/single/" + customerId;
        return customerDetail_Infomation_Link;
    }

    public String get_CUSTOMERDETAIL_Transaction_Link(long customerId, int index){
        customerDetail_Transaction_Link = Domain + "/customer/transaction/" + customerId + "?index=" + index;
        return customerDetail_Transaction_Link;
    }

    public String get_CUSTOMERDETAIL_Contact_Link(long customerId, int index) {
        customerDetail_Contact_Link = Domain + "/customers/contact/" + customerId + "?index=" + index;
        return customerDetail_Contact_Link;
    }

    // CUSTOMERDETAIL EDITATION_CREATION

    public String post_CUSTOMERDETAIL_InfomationEditation_Link(){
        CUSTOMERDETAIL_InfomationEditation_Link = Domain + "/customers/edit";
        return CUSTOMERDETAIL_InfomationEditation_Link;
    }

    public String post_CUSTOMERDETAIL_ContactAdditon_Link(){
        CUSTOMERDETAIL_ContactAdditon_Link = Domain + "/contact/add";
        return CUSTOMERDETAIL_ContactAdditon_Link;
    }

    public String post_CUSTOMERDETAIL_TransactionAdditon_Link(){
        CUSTOMERDETAIL_TransactionAdditon_Link = Domain + "/transaction/add";
        return CUSTOMERDETAIL_TransactionAdditon_Link;
    }
    public String post_PRODUCT_CREATE_Link(){
        PRODUCT_CREATE_LINK = Domain + "/products/add";
        return PRODUCT_CREATE_LINK;
    }
    public String post_CUSTOMERDETAIL_ContactEditation_Link(){
        CUSTOMERDETAIL_ContactEditation_POSTLINK = Domain + "/contact/edit";
        return CUSTOMERDETAIL_ContactEditation_POSTLINK;
    }

    // --------------- CUSTOMERDETAIL CONTACT
    public String get_CUSTOMERDETAIL_CONTACTDETAIL_Link(String contact_id){
        CUSTOMERDETAIL_CONTACTDETAIL = Domain + "/contact/" + contact_id;
        return CUSTOMERDETAIL_CONTACTDETAIL;
    }

    // Notification
    public String get_Notification_Transaction_Link(int index) {
        transaction_Notification = Domain + "/notifications/trans?USER_ID=" + Id + "&index=" + index;
        return transaction_Notification;
    }

    public String get_Notification_Customer_Link(int index) {
        transaction_Notification = Domain + "/notifications/cus?USER_ID=" + Id + "&index=" + index;
        return transaction_Notification;
    }

    public String get_Notification_Approval_Link(int index) {
        approval_Notification = Domain + "/notifications/appr?USER_ID=" + Id + "&index=" + index;
        return approval_Notification;
    }

    // Transaction
    public String get_Transaction_Link(int index){
        transaction = Domain + "/transactions?USER_ID="+Id+"&start_date_query=" + Start_Date_Query + "&index=" + index;
        return transaction;
    }

    // TransactionDetail
    public String get_TransactionDetail_GetLink(String TRANSACTION_ID){
        transactionDetail = Domain + "/transaction/" + TRANSACTION_ID;
        return transactionDetail;
    }

    // TransactionEdit
    public String post_TRANSACTION_EDITION_Link(){
        transactionEdit = Domain + "/transaction/edit";
        return transactionEdit;
    }

    // TransactionStatus
    public String post_TRANSACTION_STATUS_link(){
        TRANSACTION_STATUS_LINK = Domain + "/transaction/status";
        return TRANSACTION_STATUS_LINK;
    }

    // Warehouse
    public String get_Warehouse_Link() {
        warehouse_Link = Domain + "/warehouses" ;
        return warehouse_Link;
    }

    public String post_WAREHOUSEPRODUCT_link() {
        product_Link = Domain + "/products/inventory";
        return product_Link;
    }

}
