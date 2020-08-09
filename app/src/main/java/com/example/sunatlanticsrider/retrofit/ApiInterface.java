package com.example.sunatlanticsrider.retrofit;

import com.example.sunatlanticsrider.model.LoginAuthResponse;
import com.example.sunatlanticsrider.model.LoginRequest;
import com.example.sunatlanticsrider.model.LoginResponse;
import com.example.sunatlanticsrider.utils.BaseURL;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {

    //Login
   /* @Headers({"Content-Type:application/json"})
    @POST("get_single_user.php")
    Call<SignedInJSONResponse> getLoginUser(@Body SignInDTO signInDTO);
*/

   /*@GET(BuildConfig.NAJDI_END_POINTS + "products")
    @Headers({"Content-Type:application/json", "Authorization" + ": " + Constants.BASIC_64_AUTH})
    Call<List<ProductListResponse>> getProducts();
*/

    @POST(BaseURL.DOMAIN_NAME+BaseURL.SUB_PATH+"login")
    @Headers({"Content-Type:application/json"})
    Call<LoginAuthResponse> doCheckLogin(@Body LoginRequest loginRequest);

    @POST(BaseURL.DOMAIN_NAME+BaseURL.SUB_PATH+"User")
    @Headers({"Content-Type:application/json"})
    Call<LoginResponse> doGetUserDetails(@Header("Authorization") String token);



}
