package com.example.sunatlanticsrider.retrofit;

import com.example.sunatlanticsrider.model.BaseResponse;
import com.example.sunatlanticsrider.model.LoginAuthResponse;
import com.example.sunatlanticsrider.model.LoginRequest;
import com.example.sunatlanticsrider.model.LoginResponse;
import com.example.sunatlanticsrider.model.MobileNumUpdateRequest;
import com.example.sunatlanticsrider.model.OrderRequest;
import com.example.sunatlanticsrider.model.OrderResponseDTO;
import com.example.sunatlanticsrider.model.OrdersResponse;
import com.example.sunatlanticsrider.model.PasswordUpdateRequest;
import com.example.sunatlanticsrider.model.PreviousOrderedResponseDTO;
import com.example.sunatlanticsrider.model.UserNameUpdateRequest;
import com.example.sunatlanticsrider.utils.BaseURL;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

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

    @POST(BaseURL.DOMAIN_NAME + "auth/login")
    @Headers({"Content-Type:application/json"})
    Call<LoginAuthResponse> doCheckLogin(@Body LoginRequest loginRequest);

    @POST(BaseURL.DOMAIN_NAME + "auth/User")
    @Headers({"Content-Type:application/json"})
    Call<LoginResponse> doGetUserDetails(@Header("Authorization") String token);

    /*@Headers({"Content-Type:application/json"})
    // @GET(BaseURL.DOMAIN_NAME+BaseURL.SUB_CHECK)
    @GET(BaseURL.DOMAIN_NAME + "check/{mobileNumber}")
    Call<BaseResponse> checkMobileNumberInServer(@Path("mobileNumber") String mobile);
*/

    /*@GET(BaseURL.DOMAIN_NAME + "deliveryDetails/{userid}")
    @Headers({"Content-Type:application/json"})
    Call<OrderResponseDTO> getMyOrderDetails(@Header("Authorization") String token, @Path("userid") int userid);
*/
    @GET(BaseURL.DOMAIN_NAME + "deliveryDetails/{userid}")
    @Headers({"Content-Type:application/json"})
    Call<OrderResponseDTO> getMyCurrentOrders(@Header("Authorization") String token,@Path("userid") int user_id);

    @GET(BaseURL.DOMAIN_NAME + "orderList/{userid}")
    @Headers({"Content-Type:application/json"})
    Call<PreviousOrderedResponseDTO> getmyPreviousOrders(@Header("Authorization") String token, @Path("userid") int user_id);

    @PUT(BaseURL.DOMAIN_NAME + "updateStatus")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> updateDeliveryProgressStatus(@Header("Authorization") String token,@Body OrderRequest orderRequest);

    @PUT(BaseURL.DOMAIN_NAME + "updateMobileno")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> updateMobileNumber(@Header("Authorization") String token, @Body MobileNumUpdateRequest mobileNumUpdateRequest);


    @PUT(BaseURL.DOMAIN_NAME + "updateuserName")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> updateUserName(@Header("Authorization") String token, @Body UserNameUpdateRequest userNameUpdateRequest);


    @PUT(BaseURL.DOMAIN_NAME + "updatePassword")
    @Headers({"Content-Type:application/json"})
    Call<BaseResponse> updatePassword(@Header("Authorization") String token, @Body PasswordUpdateRequest passwordUpdateRequest);



}
