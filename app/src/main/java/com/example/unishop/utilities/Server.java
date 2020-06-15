package com.example.unishop.utilities;

public class Server {
    private static Server server = new Server();
    public static Server get(){
        return server;
    }
    private  String BASE_URL = "https://histogenetic-exhaus.000webhostapp.com/";
    public String LOGIN_URL = BASE_URL +"unishop/authenticate.php";
    public String REGISTER_URL = BASE_URL +"unishop/create_user.php";
    public String CONSULTANT_URL = BASE_URL +"unishop/list_consultants.php";
    public String PRODUCTS_URL = BASE_URL +"unishop/list_products.php";
    public String USER_PROFILE_URL = BASE_URL +"unishop/user_profile.php";
    public String UPDATE_PHONE_URL = BASE_URL +"unishop/update_user_phone.php";
    public String ADD_ITEM_URL = BASE_URL +"unishop/add_item.php";
    public String UPDATE_PRICE_URL = BASE_URL +"unishop/update_price.php";
    public String UPDATE_QUANTITY_URL = BASE_URL +"unishop/update_quantity.php";
    public String DELETE_PRODUCT_URL = BASE_URL +"unishop/delete_product.php";
}
